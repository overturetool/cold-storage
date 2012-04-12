/*******************************************************************************
 *
 *	Copyright (c) 2009 Fujitsu Services Ltd.
 *
 *	Author: Nick Battle
 *
 *	This file is part of VDMJ.
 *
 *	VDMJ is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	VDMJ is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with VDMJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package org.overturetool.vdmj.values;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.scheduler.BUSConnection;
import org.overturetool.vdmj.scheduler.BusThread;
import org.overturetool.vdmj.scheduler.CPUResource;
import org.overturetool.vdmj.scheduler.FCFSPolicy;
import org.overturetool.vdmj.scheduler.MessageRequest;
import org.overturetool.vdmj.scheduler.MessageResponse;
import org.overturetool.vdmj.scheduler.BUSResource;
import org.overturetool.vdmj.scheduler.ResourceScheduler;
import org.overturetool.vdmj.scheduler.SchedulingPolicy;
import org.overturetool.vdmj.types.ClassType;

public class BUSValue extends ObjectValue
{
	private static final long serialVersionUID = 1L;
	private static List<BUSValue> busses = new LinkedList<BUSValue>();
	private static HashMap<CPUResource, HashMap<CPUResource, BUSConnection>> cpumap = null;
	
	public static BUSValue vBUS = null;
	public final BUSResource resource;
	private boolean terminated;
	
	public BUSValue(ClassType classtype, NameValuePairMap map, ValueList argvals)
	{
		super(classtype, map, new Vector<ObjectValue>(), null);

		QuoteValue parg = (QuoteValue)argvals.get(0);
		SchedulingPolicy policy = SchedulingPolicy.factory(parg.value.toUpperCase());

		RealValue sarg = (RealValue)argvals.get(1);
		double speed = sarg.value;

		SetValue set = (SetValue)argvals.get(2);
		List<CPUResource> cpulist = new Vector<CPUResource>();

		for (Value v: set.values)
		{
			CPUValue cpuv = (CPUValue)v.deref();
			cpulist.add(cpuv.resource);
		}

		resource = new BUSResource(false, policy, speed, cpulist);
		busses.add(this);
	
		terminated = false;
	}

	public BUSValue(ClassType type, ValueSet cpus)
	{
		super(type, new NameValuePairMap(), new Vector<ObjectValue>(), null);
		List<CPUResource> cpulist = new Vector<CPUResource>();

		for (Value v: cpus)
		{
			CPUValue cpuv = (CPUValue)v.deref();
			cpulist.add(cpuv.resource);
		}

		resource = new BUSResource(true, new FCFSPolicy(), 0, cpulist);
		vBUS = this;
		busses.add(this);
	}

	public void setup(ResourceScheduler scheduler, String name)
	{
		resource.setName(name);
		scheduler.register(resource);
	}

	public void transmit(MessageRequest request)
	{
		resource.transmit(request);
	}

	public void reply(MessageResponse response)
	{
		resource.reply(response);
	}

	@Override
	public String toString()
	{
		return resource.toString();
	}

	public boolean isVirtual()
	{
		return resource.isVirtual();
	}

	public static void init()
	{
		BUSResource.init();
		busses.clear();
		// Can't create the vBUS until we know all the CPUs.
	}

	public static void start()
	{
		for (BUSValue bus: busses)
		{
			new BusThread(bus.resource, 0).start();
		}
	}

	public String getName()
	{
		return resource.getName();
	}

	public int getNumber()
	{
		return resource.getNumber();
	}

	public static void createMap(Context ctxt, ValueSet allCPUs)
	{
		int max = allCPUs.size() +1; //vCPU missing
		cpumap = new HashMap<CPUResource, HashMap<CPUResource,BUSConnection>>();
		cpumap.put(CPUValue.vCPU.resource, new HashMap<CPUResource, BUSConnection>(max));
		
		for (Value fv: allCPUs)
		{
			CPUValue from = (CPUValue)fv;
			//connect vCPU to CPU via virtual bus
			cpumap.get(CPUValue.vCPU.resource).put(from.resource, new BUSConnection(vBUS));
			//and CPU to vCPU via virtual bus
			cpumap.put(from.resource, new HashMap<CPUResource, BUSConnection>(max));
			cpumap.get(from.resource).put(CPUValue.vCPU.resource, new BUSConnection(vBUS));
			
			for (Value tv: allCPUs)
			{
				CPUValue to = (CPUValue)tv;
				
				if (from == to)
				{
					continue;
				}

				BUSValue bus = findRealBUS(from, to);

				if (bus == null)
				{
					cpumap.get(from.resource).put(to.resource, new BUSConnection()); //add BUSConnection object for later use, but do not supply a bus
					continue;	// May be OK - separated island CPUs
				} else
				{
					cpumap.get(from.resource).put(to.resource, new BUSConnection(bus));
				}
			}
		}
	}

	private static BUSValue findRealBUS(CPUValue from, CPUValue to)
	{
		for (BUSValue bus : busses)
		{
			if (bus != vBUS &&
				bus.resource.links(from.resource, to.resource))
			{
				return bus;
			}
		}

		return null;
	}

	public static BUSValue lookupBUS(CPUValue from, CPUValue to)
	{
		if(cpumap.containsKey(from.resource) && cpumap.get(from.resource).containsKey(to.resource))
		{
			return cpumap.get(from.resource).get(to.resource).fastest();
		}
		
		return null;
	}
	
	public static void connectCPUToBUS(CPUValue newCPU, BUSValue bus)
	{
		BUSResource busRes = bus.resource;
		CPUResource newCpuRes = newCPU.resource;
		 
		//vCPU is always connected
		if(newCPU.isVirtual())  return;
		
		//only map if cpu is not connected to the bus already. 
		if(busRes.addCPU(newCpuRes))
		{
			for(CPUResource cpuRes : busRes.getCPUs())
			{
				//don't map to self
				if(cpuRes == newCpuRes) continue;
				
				//BusConnection object might need to be added if the CPU was added dynamically. 
				if(cpumap.get(newCpuRes).containsKey(cpuRes))
				{
					cpumap.get(newCpuRes).get(cpuRes).add(bus);
				}
				else
				{
					cpumap.get(newCpuRes).put(cpuRes, new BUSConnection(bus));
				}
				
				if(cpumap.get(cpuRes).containsKey(newCpuRes))
				{
					cpumap.get(cpuRes).get(newCpuRes).add(bus);
				}
				else
				{
					cpumap.get(cpuRes).put(newCpuRes, new BUSConnection(bus));
				}
			}
		}
	}
	
	public static void disconnectCPUFromBUS(CPUResource removeCpuRes, BUSValue bus)
	{
		BUSResource busRes = bus.resource;
		
		//vCPU is always connected
		if(removeCpuRes.isVirtual())  return;
		
		//vBUS is always connected
		if(bus.isVirtual()) return;
		
		if(busRes.isConnectedTo(removeCpuRes))
		{
			busRes.removeCPU(removeCpuRes);
			
			for(CPUResource cpuRes : busRes.getCPUs())
			{
				cpumap.get(removeCpuRes).get(cpuRes).remove(bus);
				cpumap.get(cpuRes).get(removeCpuRes).remove(bus);
			}
			
			busRes.RemoveMessages(removeCpuRes);
		}
	}
	
	public static void disconnectCPUFromAllBusses(CPUValue cpu)
	{
		for (BUSValue bus : busses) {
			disconnectCPUFromBUS(cpu.resource, bus);
		}	
	}
	
	
	public static void connectCPUToVirtualBUS(CPUValue newCPU)
	{
		BUSResource busRes = vBUS.resource;
		CPUResource newCpuRes = newCPU.resource;
		 
		//only map if cpu is not connected to the bus already. 
		if(busRes.addCPU(newCpuRes))
		{
			cpumap.get(CPUValue.vCPU.resource).put(newCpuRes, new BUSConnection(vBUS));
			cpumap.put(newCpuRes, new HashMap<CPUResource,BUSConnection>());
			cpumap.get(newCpuRes).put(CPUValue.vCPU.resource, new BUSConnection(vBUS));
		}
	}
	
	public static void migrateMessages(ObjectValue migratingObj, BUSValue targetBus)
	{
		for(BUSValue bus : busses){
			
			if(bus.isVirtual()) continue;
			bus.resource.migrateMessages(migratingObj, targetBus);
		}
	}

	public void terminate() 
	{
		if(!terminated)
		{
			terminated = true;
			resource.reset();
		}
	}

	public boolean isTerminated() 
	{
		return terminated;
	}
}
