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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.config.Properties;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.values.ObjectValue;

public abstract class SchedulableThread extends Thread implements Serializable
{
    private static final long serialVersionUID = 1L;

	private static List<SchedulableThread> allThreads =
						new LinkedList<SchedulableThread>();

	protected final Resource resource;
	protected final ObjectValue object;
	private final boolean periodic;

	protected RunState state;
	private Signal signal;
	private long timeslice;
	private long steps;
	private long timestep;
	private long swapInBy;

	public SchedulableThread(
		Resource resource, ObjectValue object, long priority,
		boolean periodic, long swapInBy)
	{
		this.resource = resource;
		this.object = object;
		this.periodic = periodic;
		this.setSwapInBy(swapInBy);

		steps = 0;
		timeslice = 0;
		state = RunState.CREATED;
		signal = null;

		resource.register(this, priority);

		synchronized (allThreads)
		{
			allThreads.add(this);
		}

		setName("SchedulableThread-" + getId());
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof SchedulableThread)
		{
			SchedulableThread to = (SchedulableThread)other;
			return getId() == to.getId();
		}

		return false;
	}

	@Override
	public int hashCode()
	{
		return new Long(getId()).hashCode();
	}

	@Override
	public String toString()
	{
		return "SchedulableThread-" + getId() + " (" + state + ")";
	}

	@Override
    public synchronized void start()
	{
		super.start();

		while (state == RunState.CREATED)
		{
			sleep();
		}

		// Log the creation here so that it is deterministic...

		if (resource instanceof CPUResource)
		{
			CPUResource cpu = (CPUResource)resource;
			cpu.createThread(this);
		}
	}

	@Override
	public void run()
	{
		reschedule();
		body();
		setState(RunState.COMPLETE);
		resource.unregister(this);

		synchronized (allThreads)
		{
			allThreads.remove(this);
		}
	}

	abstract protected void body();

	public void step()
	{
		if (Settings.dialect == Dialect.VDM_RT)
		{
			duration(Properties.rt_duration_default);
		}
		else
		{
			SystemClock.advance(Properties.rt_duration_default);
		}

		if (++steps >= timeslice)
		{
			reschedule();
			steps = 0;
		}
	}

	public synchronized RunState getRunState()
	{
		return state;
	}

	public synchronized void setState(RunState newstate)
	{
		state = newstate;
		notifyAll();
	}

	private synchronized void reschedule()
	{
		// Yield control but remain runnable - called by thread
		waitUntilState(RunState.RUNNABLE, RunState.RUNNING);
	}

	public synchronized void waiting()
	{
		// Enter a waiting state - called by thread
		waitUntilState(RunState.WAITING, RunState.RUNNING);
	}

	public synchronized void runslice(long slice)
	{
		// Run one time slice - called by Scheduler
		timeslice = slice;
		waitWhileState(RunState.RUNNING, RunState.RUNNING);
	}

	public synchronized void duration(long pause)
	{
		// Wait until pause has passed - called by thread
		setTimestep(pause);
		long end = SystemClock.getWallTime() + pause;

		while (getTimestep() > 0)
		{
			waitUntilState(RunState.TIMESTEP, RunState.RUNNING);
			setTimestep(end - SystemClock.getWallTime());
		}
	}

	private synchronized void waitWhileState(RunState newstate, RunState until)
	{
		setState(newstate);

		while (state == until)
		{
			sleep();
		}
	}

	private synchronized void waitUntilState(RunState newstate, RunState until)
	{
		setState(newstate);

		while (state != until)
		{
			sleep();
		}
	}

	private synchronized void sleep()
	{
		while (true)
		{
    		try
    		{
   				wait();
    			return;
    		}
    		catch (InterruptedException e)
    		{
    			handleSignal(signal);
    		}
		}
	}

	protected void handleSignal(Signal sig)
	{
		switch (sig)
		{
			case TERMINATE:
				throw new ThreadDeath();

			case SUSPEND:
				break;

			case BREAK:
				System.out.println("Break " + this);
				break;
		}
	}

	public void suspendOthers()
	{
		synchronized (allThreads)
		{
    		for (SchedulableThread th: allThreads)
    		{
    			if (!th.equals(this))
    			{
    				th.setSignal(Signal.SUSPEND);
    			}
    		}
		}
	}

	public static void signalAll(Signal sig)
	{
		synchronized (allThreads)
		{
    		for (SchedulableThread th: allThreads)
    		{
   				th.setSignal(sig);
    		}
		}
	}

	private synchronized void setSignal(Signal sig)
	{
		signal = sig;
		interrupt();
	}

	public ObjectValue getObject()
	{
		return object;
	}

	public void setSwapInBy(long swapInBy)
	{
		this.swapInBy = swapInBy;
	}

	public long getSwapInBy()
	{
		return swapInBy;
	}

	public boolean isPeriodic()
	{
		return periodic;
	}

	public boolean isActive()
	{
		return state == RunState.TIMESTEP || state == RunState.WAITING;
	}

	public void setTimestep(long timestep)
	{
		this.timestep = timestep;
	}

	public long getTimestep()
	{
		return timestep;
	}
}
