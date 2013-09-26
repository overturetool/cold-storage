/*******************************************************************************
 *
 *	Copyright (c) 2008 Fujitsu Services Ltd.
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

package junit.overture.evaluate;

import org.overturetool.vdmj.Release;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.lex.Dialect;

import junit.overture.OvertureTest;

public class ClassTest extends OvertureTest
{
	public void test_Evaluate1()
	{
		evaluate("evaluate1", ResultType.TRUE);
	}

	public void test_Evaluate2()
	{
		evaluate("evaluate2", ResultType.VOID);
	}

	public void test_IotaTypeBind()
	{
		evaluate("iota_type_bind", ResultType.TRUE);
	}

	public void test_Patterns()
	{
		evaluate("patterns", ResultType.TRUE, 0, Release.VDM_10);
	}

	public void test_Measures()
	{
		evaluate("measures", ResultType.VOID);
	}

	public void test_SeqComp()
	{
		evaluate("seqcomp", ResultType.TRUE);
	}

	public void test_Postcond()
	{
		evaluate("postcond", ResultType.TRUE);
	}

	public void test_Typeparams()
	{
		evaluate("typeparams", ResultType.TRUE);
	}

	public void test_Setrange()
	{
		evaluate("setrange", ResultType.TRUE);
	}

	public void test_Inference()
	{
		evaluate("inference", ResultType.TRUE);
	}

	public void test_Narrow()
	{
		evaluate("narrow", ResultType.TRUE, 0, Release.VDM_10);
	}

	public void test_Curried()
	{
		evaluate("curried", ResultType.TRUE);
	}
	
	public void test_ValueFactory()
	{
		evaluate("factorytest", ResultType.TRUE);
	}

	public void test_Stop()
	{
		Settings.dialect = Dialect.VDM_RT;
		evaluate("stoptest", ResultType.VOID, 0, Release.VDM_10);
	}

	public void test_Sporadic()
	{
		Settings.dialect = Dialect.VDM_RT;
		evaluate("sporadic", ResultType.VOID, 0, Release.VDM_10);
	}
}
