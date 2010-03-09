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

package org.overturetool.vdmj.messages;

import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;

import org.overturetool.vdmj.runtime.SystemClock;

public class RTLogger
{
	private static boolean enabled = false;
	private static List<String> events = new Vector<String>();
	private static PrintWriter logfile = null;

	public static synchronized void enable(boolean on)
	{
		if (!on)
		{
			dump(true);
		}

		enabled = on;
	}

	public static synchronized void log(String event)
	{
		event = event + " time: " + SystemClock.getWallTime();
		
		if (!enabled)
		{
			return;
		}
		else if (logfile == null)
		{
			Console.out.println(event);
		}
		else
		{
    		events.add(event);

    		if (events.size() > 1000)
    		{
    			dump(false);
    		}
		}
	}

	public static void setLogfile(PrintWriter out)
	{
		enabled = true;
		dump(true);		// Write out and close previous
		logfile = out;
	}

	public static int getLogSize()
	{
		return events.size();
	}

	public static synchronized void dump(boolean close)
	{
		if (logfile != null)
		{
    		for (String event: events)
    		{
    			logfile.println(event);
    		}

    		logfile.flush();
    		events.clear();

    		if (close)
    		{
    			logfile.close();
    		}
		}
	}
}
