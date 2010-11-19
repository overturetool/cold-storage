/*******************************************************************************
 *
 *	Copyright (c) 2010 Fujitsu Services Ltd.
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

package org.overturetool.vdmj.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.overturetool.vdmj.messages.RTLogger;
import org.overturetool.vdmj.runtime.ExitException;
import org.overturetool.vdmj.values.CPUValue;
import org.overturetool.vdmj.values.QuoteValue;

public class BUSResource extends Resource
{
    private static final long serialVersionUID = 1L;
	private static int nextBUS = 1;
	private static BUSResource vBUS = null;

	private final int busNumber;
	private transient final ControlQueue cq;
	private final double speed;
	private final List<CPUResource> cpus;
	private final List<MessagePacket> messages;
	private transient final HashMap<Long,MessagePacket> currentlyProcessing;
	private final List<Long> discardResponse;
	
	private ISchedulableThread busThread = null;

	public BUSResource(boolean isVirtual,
		SchedulingPolicy policy, double speed, List<CPUResource> cpus)
	{
		super(policy);

		this.busNumber = isVirtual ? 0 : nextBUS++;
		this.cq = new ControlQueue();
		this.speed = speed;
		this.cpus = cpus;
		this.messages = new LinkedList<MessagePacket>();
		this.currentlyProcessing = new HashMap<Long, MessagePacket>();
		this.discardResponse = new ArrayList<Long>();
		busThread = null;
		
		if (isVirtual)
		{
			vBUS = this;
		}
	}

	public static void init()
	{
		MessagePacket.init();
		nextBUS = 1;
		vBUS = null;
	}

	@Override
	public void reset()
	{
		messages.clear();
		cq.reset();
		policy.reset();
	}

	@Override
	public void setName(String name)
	{
		super.setName(name);

		if (busNumber != 0)
		{
    		RTLogger.log(
    			"BUSdecl -> id: " + busNumber +
    			" topo: " + cpusToSet() +
    			" name: \"" + name + "\"");
		}
	}

	@Override
	public boolean reschedule()
	{
		// This is scheduling threads, as though for a CPU, but really we
		// want to schedule (ie. order) the messages on the queue for the BUS.
		// There is only one BUS thread.

		if (policy.reschedule())
		{
			busThread = policy.getThread();
			busThread.runslice(policy.getTimeslice());
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public long getMinimumTimestep()
	{
		if (busThread == null)
		{
			return Long.MAX_VALUE;		// We're not in timestep
		}
		else
		{
			switch (busThread.getRunState())
			{
				case TIMESTEP:
					return busThread.getTimestep();

				case RUNNING:
					return -1;			// Can't timestep

				default:
					return Long.MAX_VALUE;
			}
		}
	}

	public boolean links(CPUResource from, CPUResource to)
	{
		if (from.equals(to))
		{
			return false;
		}
		else
		{
			return cpus.contains(from) && cpus.contains(to);
		}
	}

	public void transmit(MessageRequest request)
	{
		RTLogger.log(
			"MessageRequest -> busid: " + request.bus.getNumber() +
			" fromcpu: " + request.from.getNumber() +
			" tocpu: " + request.to.getNumber() +
			" msgid: " + request.msgId +
			" callthr: " + request.thread.getId() +
			" opname: " + "\"" + request.operation.name + "\"" +
			" objref: " + request.target.objectReference +
			" size: " + request.getSize());

		messages.add(request);
		cq.stim();
	}

	public void reply(MessageResponse response)
	{
		RTLogger.log(
			"ReplyRequest -> busid: " + response.bus.getNumber() +
			" fromcpu: " + response.from.getNumber() +
			" tocpu: " + response.to.getNumber() +
			" msgid: " + response.msgId +
			" origmsgid: " + response.originalId +
			" callthr: " + response.caller.getId() +
			" calleethr: " + response.thread.getId() +
			" size: " + response.getSize());

		messages.add(response);
		cq.stim();
	}

	public void process(ISchedulableThread th)
	{
		cq.join(null, null);		// Never leaves

		while (true)
		{
    		while (messages.isEmpty())
    		{
    			cq.block(null, null);
    		}
    		MessagePacket m = messages.remove(0);

    		RTLogger.log(
				"MessageActivate -> msgid: " + m.msgId);

    		if (m instanceof MessageRequest)
    		{
    			MessageRequest mr = (MessageRequest)m;

    			if (!mr.bus.isVirtual())
    			{
    				long pause = getDataDuration(mr.getSize());
    				th.duration(pause, null, null);
    			}

    			currentlyProcessing.put(m.msgId, m);
    			AsyncThread thread = new AsyncThread(mr);
    			thread.start();
    		}
    		else
    		{
    			MessageResponse mr = (MessageResponse)m;

    			if (!mr.bus.isVirtual())
    			{
    				long pause = getDataDuration(mr.getSize());
    				th.duration(pause, null, null);
    			}
    			
    			//response cannot be used as there is no connection.
    			if(discardResponse.contains(mr.originalId))
    			{
					ExitException ee = new ExitException(new QuoteValue("ResponseLost"), null, null);
					messages.add(new MessageResponse(ee, mr));
					discardResponse.remove(mr.originalId);
    			}
    			else
    			{
	    			mr.replyTo.set(mr);
    			}
    			
    			currentlyProcessing.remove(mr.originalId);
    		}

    		RTLogger.log(
				"MessageCompleted -> msgid: " + m.msgId);
		}
	}

	@Override
	public boolean isVirtual()
	{
		return this == vBUS;
	}

	private long getDataDuration(long bytes)
	{
		if (speed == 0)
		{
			return 0;			// Infinitely fast virtual bus
		}
		else
		{
			return bytes;		// For now...
		}
	}

	private String cpusToSet()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		String prefix = "";

		for (CPUResource cpu: cpus)
		{
			sb.append(prefix);
			sb.append(cpu.getNumber());
			prefix=",";
		}

		sb.append("}");
		return sb.toString();
	}

	@Override
	public String getStatus()
	{
		return name + " queue = " + messages.size();
	}

	public int getNumber()
	{
		return busNumber;
	}
	
	public double getSpeed()
	{
		return speed;		
	}
	
	public boolean addCPU(CPUValue newCPU)
	{
		if(!cpus.contains(newCPU.resource))
		{
			cpus.add(newCPU.resource);
			return true;
		}

		return false;
	}
	
	public void removeCPU(CPUValue cpu)
	{
		if(cpus.contains(cpu.resource))
		{
			cpus.remove(cpu.resource);
		}
	}
	
	public List<CPUResource> getCPUs()
	{
		return cpus;	
	}
	
	public boolean isConnectedTo(CPUValue cpu)
	{
		return cpus.contains(cpu.resource);
	}
	
	public void RemoveMessages(CPUValue cpu)
	{
		if(messages.isEmpty())
		{
			return;
		}
		
		//look through message in the queue
		LinkedList<MessagePacket> msgToRemove = new LinkedList<MessagePacket>();
		LinkedList<MessagePacket> errorMsg = new LinkedList<MessagePacket>();
		MessagePacket m;
		for(int i=0; i < messages.size() ; i++)
		{			
			m = messages.get(i);

			if(m.to.equals(cpu) || m.from.equals(cpu))
			{
				if(m.replyTo != null)
				{
					//sync call, somebody is waiting
					ExitException ee; 
					
					if (m instanceof MessageResponse)
					{
						ee = new ExitException(new QuoteValue("ResponseLost"), null, null);
						
					} else
					{
						ee =  new ExitException(new QuoteValue("MessageLost"), null, null);;
					}  
					
					//add error message 
					errorMsg.add(new MessageResponse(ee, m));
				} 

				//drop msg on bus
				msgToRemove.add(m);
			}
		}
		
		//we won't kill remote threads processing a remote call, so 
		//look through current async processing, to determine which responses needs to be ignored. 
		if(!currentlyProcessing.isEmpty())
		{
			MessagePacket mp;
			Iterator<MessagePacket> itr = currentlyProcessing.values().iterator();	
			while(itr.hasNext())
			{
				mp = itr.next();
				
				//both processing occurring on the disconnected CPU and 
				//on others CPUs on behalf of the disconnect CPU will
				//not be able to deliver the result.
				if(mp.from.equals(cpu) || mp.to.equals(cpu))
				{
					discardResponse.add(mp.msgId);
				}
			}
		}
		
		//loops completed, now alter list.
		messages.removeAll(msgToRemove);
		messages.addAll(errorMsg);
		cq.stim();
	}
}
