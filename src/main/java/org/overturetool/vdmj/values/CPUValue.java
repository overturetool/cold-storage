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

import java.util.List;
import java.util.Vector;

import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.runtime.CPUPolicy;
import org.overturetool.vdmj.runtime.RunState;
import org.overturetool.vdmj.runtime.SchedulingPolicy;
import org.overturetool.vdmj.types.ClassType;
import org.overturetool.vdmj.types.Type;

public class CPUValue extends ObjectValue
{
	private static final long serialVersionUID = 1L;
	private static int nextCPU = 1;
	public static List<CPUValue> allCPUs;

	public final int cpuNumber;
	public final SchedulingPolicy policy;
	public final double cyclesPerSec;
	public final List<ObjectValue> deployed;

	public String name;
	public Thread runningThread;
	public ObjectValue runningObject;

	public static void init()
	{
		nextCPU = 1;
		allCPUs = new Vector<CPUValue>();
	}

	public CPUValue(Type classtype, NameValuePairMap map, ValueList argvals)
	{
		super((ClassType)classtype, map, new Vector<ObjectValue>());

		this.cpuNumber = nextCPU++;

		QuoteValue parg = (QuoteValue)argvals.get(0);
		CPUPolicy cpup = CPUPolicy.valueOf(parg.value.toUpperCase());
		policy = cpup.factory(this);

		RealValue sarg = (RealValue)argvals.get(1);
		this.cyclesPerSec = sarg.value;

		deployed = new Vector<ObjectValue>();

		allCPUs.add(this);
	}

	public CPUValue(
		int number, Type classtype, NameValuePairMap map, ValueList argvals)
	{
		super((ClassType)classtype, map, new Vector<ObjectValue>());

		this.cpuNumber = number;

		QuoteValue parg = (QuoteValue)argvals.get(0);
		CPUPolicy cpup = CPUPolicy.valueOf(parg.value.toUpperCase());
		policy = cpup.factory(this);

		RealValue sarg = (RealValue)argvals.get(1);
		this.cyclesPerSec = sarg.value;

		deployed = new Vector<ObjectValue>();

		allCPUs.add(this);
	}

	public void addDeployed(ObjectValue obj)
	{
		deployed.add(obj);
	}

	public boolean setPriority(String opname, long priority)
	{
		boolean found = false;

		for (ObjectValue obj: deployed)
		{
			for (LexNameToken m: obj.members.keySet())
			{
				if (m.getExplicit(true).getName().equals(opname))
				{
					OperationValue op = (OperationValue)obj.members.get(m);
					op.setPriority(priority);
					found = true;
				}
			}
		}

		return found;
	}

	public long getDuration(long cycles)
	{
		return (long)(cycles/cyclesPerSec * 1000);		// millisecs
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}

	public String declString(String sysname, boolean explicit)
	{
		return
			"CPUdecl -> id: " + cpuNumber +
			" expl: " + explicit +
			" sys: \"" + sysname + "\"" +
			" name: \"" + name + "\"";
	}

	public synchronized void addThread(Thread thread, ObjectValue object)
	{
		policy.addThread(thread, object);
	}

	public synchronized void removeThread(Thread thread)
	{
		policy.removeThread(thread);
	}

	public synchronized void reschedule(RunState newstate)
	{
		setState(runningThread, newstate);

		if (policy.reschedule())
		{
			runningThread = policy.getThread();
			runningObject = policy.getObject();

			synchronized (runningThread)
			{
				runningThread.notify();		// Wake from sleep
			}

			sleep();
		}
	}

	private synchronized void sleep()
	{
		Thread current = Thread.currentThread();

		while (runningThread != current)
		{
			synchronized (current)
			{
				try
				{
					current.wait();
				}
				catch (InterruptedException e)
				{
					// So?
				}
			}
		}

		setState(current, RunState.RUNNABLE);
	}

	public synchronized void setState(Thread thread, RunState newstate)
	{
		policy.setState(thread, newstate);
	}
}
