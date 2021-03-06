/*******************************************************************************
 *
 *	Copyright (c) 2009 IHA
 *
 *	Author: Peter Gorm Larsen
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

package junit.overture.combtest;

import junit.overture.OvertureTest;

public class TraceRepeatTest extends OvertureTest
{
	public void test_Traces1()
	{
		combtest("traces/tracerepeat/tracerepeat-01", "UseStack`PushBeforePop");
	}

	public void test_Traces2()
	{
		combtest("traces/tracerepeat/tracerepeat-02", "UseStack`PushBeforePop");
	}

	public void test_Traces3()
	{
		combtest("traces/tracerepeat/tracerepeat-03", "UseStack`PushBeforePop");
	}

	public void test_Traces4()
	{
		combtest("traces/tracerepeat/tracerepeat-04", "UseStack`PushBeforePop");
	}

	public void test_Traces5()
	{
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-051", "UseStack`trace1", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-052", "UseStack`trace2", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-053", "UseStack`trace3", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-054", "UseStack`trace4", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-055", "UseStack`trace5", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-056", "UseStack`trace6", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-057", "UseStack`trace7", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-058", "UseStack`trace8", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-059", "UseStack`trace9", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-0510", "UseStack`trace10", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-0511", "UseStack`trace11", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-0512", "UseStack`trace12", 0);
		combtest("traces/tracerepeat/tracerepeat-05", "traces/tracerepeat/tracerepeat-0513", "UseStack`trace13", 0);
	}

	public void test_Traces6()
	{
		combtest("traces/tracerepeat/tracerepeat-06", "UseA`trace1");
	}

	public void test_Traces7()
	{
		combtest("traces/tracerepeat/tracerepeat-07", "UseA`trace1");
	}

	public void test_Traces8()
	{
		combtest("traces/tracerepeat/tracerepeat-08", "UseA`trace1");
	}
}
