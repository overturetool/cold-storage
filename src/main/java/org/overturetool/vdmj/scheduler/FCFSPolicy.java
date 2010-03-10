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

package org.overturetool.vdmj.scheduler;

import java.util.LinkedList;
import java.util.List;

public class FCFSPolicy extends SchedulingPolicy
{
	protected final List<SchedulableThread> threads;
	protected SchedulableThread bestThread = null;
	protected long minimumDuration = -1;

	public FCFSPolicy()
	{
		threads = new LinkedList<SchedulableThread>();
	}

	@Override
	public void reset()
	{
		threads.clear();
		bestThread = null;
		minimumDuration = -1;
	}

	@Override
	public void register(SchedulableThread thread, long priority)
	{
		synchronized (threads)
		{
			threads.add(thread);
		}
	}

	@Override
	public void unregister(SchedulableThread thread)
	{
		synchronized (threads)
		{
			threads.remove(thread);
		}
	}

	@Override
	public boolean reschedule()
	{
		bestThread = null;
		minimumDuration = Long.MAX_VALUE;

		synchronized (threads)
		{
    		out: for (SchedulableThread th: threads)
    		{
    			switch (th.getRunState())
    			{
    				case RUNNABLE:
    					minimumDuration = -1;	// Can't step yet
        				bestThread = th;
        				threads.remove(th);
        				threads.add(th);
        				break out;

    				case TIMESTEP:
    					if (th.getTimestep() < minimumDuration)
    					{
    						minimumDuration = th.getTimestep();
    					}
    					break;

    				default:
    					break;
    			}
    		}
		}

		return (bestThread != null);
	}

	@Override
	public SchedulableThread getThread()
	{
		return bestThread;
	}

	@Override
	public long getTimeslice()
	{
		return DEFAULT_TIMESLICE;
	}

	@Override
	public long getTimestep()
	{
		return minimumDuration;
	}

	@Override
	public void advance()
	{
		synchronized (threads)
		{
    		for (SchedulableThread th: threads)
    		{
    			if (th.getRunState() == RunState.TIMESTEP)
    			{
    				th.setState(RunState.RUNNABLE);
    			}
    		}
		}
	}

	@Override
	public boolean hasActive()
	{
		synchronized (threads)
		{
    		for (SchedulableThread th: threads)
    		{
    			if (th.getRunState() == RunState.TIMESTEP)
    			{
    				return true;
    			}
    		}
		}

		return false;
	}

	@Override
	public boolean hasPriorities()
	{
		return false;
	}

	@Override
	public String getStatus()
	{
		StringBuilder sb = new StringBuilder();

		for (SchedulableThread th: threads)
		{
			sb.append(th);
			sb.append("\n");
		}

		return sb.toString();
	}
}
