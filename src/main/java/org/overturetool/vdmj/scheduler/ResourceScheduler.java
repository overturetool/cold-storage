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

import org.overturetool.vdmj.runtime.MainThread;
import org.overturetool.vdmj.values.BUSValue;

public class ResourceScheduler
{
	public String name = "scheduler";
	private List<Resource> resources = new LinkedList<Resource>();
	private MainThread mainThread = null;

	public void init()
	{
		resources.clear();
		mainThread = null;
	}

	public void reset()
	{
		for (Resource r: resources)
		{
			r.reset();
		}
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void register(Resource resource)
	{
		resources.add(resource);
		resource.setScheduler(this);
	}

	public void unregister(Resource resource)
	{
		resources.remove(resource);
		resource.setScheduler(null);
	}

	public void start(MainThread main)
	{
		mainThread = main;
		BUSValue.start();	// Start BUS threads first...

		boolean idle = true;

		do
		{
			long minstep = Long.MAX_VALUE;
			idle = true;

			for (Resource resource: resources)
			{
				if (resource.reschedule())
				{
					idle = false;
				}
				else
				{
					long d = resource.getTimestep();

					if (d < minstep)
					{
						minstep = d;
					}
				}
			}

			if (idle && minstep >= 0 && minstep < Long.MAX_VALUE)
			{
				SystemClock.advance(minstep);

				for (Resource resource: resources)
				{
					resource.advance();
				}

				idle = false;
			}
		}
		while (!idle && main.getRunState() != RunState.COMPLETE);


		if (main.getRunState() != RunState.COMPLETE)
		{
    		for (Resource resource: resources)
    		{
    			if (resource.hasActive())
    			{
    				raise(new RuntimeException("DEADLOCK detected"));
    				break;
    			}
    		}
		}

		SchedulableThread.signalAll(Signal.TERMINATE);
	}

	public String getStatus()
	{
		StringBuilder sb = new StringBuilder();

		for (Resource r: resources)
		{
			sb.append(r.getStatus());
			sb.append("\n");
		}

		return sb.toString();
	}

	public void raise(RuntimeException exception)
	{
		mainThread.setException(exception);
	}
}
