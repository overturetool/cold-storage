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

import java.util.LinkedList;
import java.util.List;

import org.overturetool.vdmj.messages.RTLogger;

public class BUSResource extends Resource
{
	private static int nextBUS = 0;
	private static BUSResource vBUS = null;

	private final int busNumber;
	private final ControlQueue cq;
	private final double speed;
	private final List<CPUResource> cpus;
	private final List<MessagePacket> messages;

	public BUSResource(
		SchedulingPolicy policy, double speed, List<CPUResource> cpus)
	{
		super(policy);

		this.busNumber = nextBUS++;
		this.cq = new ControlQueue();
		this.speed = speed;
		this.cpus = cpus;
		this.messages = new LinkedList<MessagePacket>();

		if (busNumber == 0)
		{
			vBUS = this;
		}
	}

	public static void init()
	{
		MessagePacket.init();
		nextBUS = 0;
		vBUS = null;
	}

	@Override
	public void reset()
	{
		messages.clear();
		cq.reset();
	}

	@Override
	public void setName(String name)
	{
		super.setName(name);

		RTLogger.log(
			"BUSdecl -> id: " + busNumber +
			" topo: " + cpusToSet() +
			" name: \"" + name + "\"");
	}

	@Override
	public boolean reschedule()
	{
		// This is scheduling threads, as though for a CPU, but really we
		// want to schedule (ie. order) the messages on the queue for the BUS.
		// There is only one BUS thread.

		if (policy.reschedule())
		{
			SchedulableThread best = policy.getThread();
			best.runslice(policy.getTimeslice());
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public long getTimestep()
	{
		return policy.getTimestep();
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
			"MessageRequest -> busid: " + request.bus.resource.busNumber +
			" fromcpu: " + request.from.getCPU() +
			" tocpu: " + request.to.getCPU() +
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
			"ReplyRequest -> busid: " + response.bus.resource.busNumber +
			" fromcpu: " + response.from.getCPU() +
			" tocpu: " + response.to.getCPU() +
			" msgid: " + response.msgId +
			" origmsgid: " + response.originalId +
			" callthr: " + response.caller.getId() +
			" calleethr: " + response.thread.getId() +
			" size: " + response.getSize());

		messages.add(response);
		cq.stim();
	}

	public void process(SchedulableThread th)
	{
		cq.join();		// Never leaves

		while (true)
		{
    		while (messages.isEmpty())
    		{
    			cq.block();
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
    				th.duration(pause);
    			}

    			AsyncThread thread = new AsyncThread(mr);
    			thread.start();
    		}
    		else
    		{
    			MessageResponse mr = (MessageResponse)m;

    			if (!mr.bus.isVirtual())
    			{
    				long pause = getDataDuration(mr.getSize());
    				th.duration(pause);
    			}

    			mr.replyTo.set(mr);
    		}

    		RTLogger.log(
				"MessageCompleted -> msgid: " + m.msgId);
		}
	}

	public boolean isVirtual()
	{
		return this == vBUS;
	}

	private long getDataDuration(long bytes)
	{
		return (long)(bytes/speed + 1);		// Same as VDMTools
	}

	private String cpusToSet()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		String prefix = "";

		for (CPUResource cpu: cpus)
		{
			sb.append(prefix);
			sb.append(cpu.getCpuNumber());
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
}
