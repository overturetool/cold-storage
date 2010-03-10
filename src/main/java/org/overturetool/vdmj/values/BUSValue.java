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

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.overturetool.vdmj.scheduler.CPUResource;
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
	public static BUSValue vBUS = null;
	public final BUSResource resource;

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
			CPUValue cpuv = (CPUValue)v;
			cpulist.add(cpuv.resource);
		}

		resource = new BUSResource(policy, speed, cpulist);
	}

	public static BUSValue findBus(CPUValue from, CPUValue to)
	{
		for (BUSValue bus : busses)
		{
			if (bus.resource.links(from.resource, to.resource))
			{
				return bus;
			}
		}

		return vBUS;
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
	}
}
