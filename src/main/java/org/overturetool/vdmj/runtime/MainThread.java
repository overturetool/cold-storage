/*******************************************************************************
 *
 *	Copyright (C) 2008 Fujitsu Services Ltd.
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

package org.overturetool.vdmj.runtime;

import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.debug.DBGPReader;
import org.overturetool.vdmj.debug.DBGPReason;
import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.scheduler.CPUResource;
import org.overturetool.vdmj.scheduler.SchedulableThread;
import org.overturetool.vdmj.values.CPUValue;
import org.overturetool.vdmj.values.ObjectValue;
import org.overturetool.vdmj.values.OperationValue;
import org.overturetool.vdmj.values.TransactionValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueList;

/**
 * A class representing a VDM thread.
 */

public class MainThread extends SchedulableThread
{
	public final ObjectValue object;
	public final OperationValue operation;
	public final Context ctxt;
	public final String title;
	public final boolean breakAtStart;
	public final Expression expression;

	private Value result = null;
	private RuntimeException exception = null;

	public MainThread(LexLocation location, ObjectValue object, Context ctxt)
		throws ValueException
	{
		super(object.getCPU().resource, object, 0, false, 0);

		this.title =
			"Thread " + getId() +
			", self #" + object.objectReference +
			", class " + object.type.name.name;

		this.object = object;
		this.ctxt = new ObjectContext(location, title, ctxt.getGlobal(), object);
		this.operation = object.getThreadOperation(ctxt);
		this.breakAtStart = ctxt.threadState.isStepping();
		this.expression = null;
	}

	public MainThread(Expression expr, Context ctxt)
	{
		super(CPUResource.vCPU, null, 0, false, 0);

		this.object = null;
		this.operation = null;
		this.title = "main";
		this.expression = expr;
		this.ctxt = ctxt;
		this.breakAtStart = false;
	}

	@Override
	public int hashCode()
	{
		return (int)getId();
	}

	@Override
	public void body()
	{
		if (Settings.usingDBGP)
		{
			runDBGP();
		}
		else
		{
			runCmd();
		}
	}

	private void runCmd()
	{
		try
		{
			if (expression != null)
			{
				result = expression.eval(ctxt);
			}
			else
			{
     			ctxt.setThreadState(null, operation.getCPU());

    			if (breakAtStart)
    			{
    				// Step at the first location you check (start of body)
    				ctxt.threadState.setBreaks(new LexLocation(), null, null);
    			}

    			result = operation.eval(ctxt.location, new ValueList(), ctxt);
			}
		}
		catch (ValueException e)
		{
			exception = new ContextException(e, ctxt.location);
		}
		catch (RuntimeException e)
		{
			exception = e;
		}
		finally
		{
			TransactionValue.commitAll();
		}
	}

	private void runDBGP()
	{
		DBGPReader reader = null;

		try
		{
			if (exception != null)
			{
				result = expression.eval(ctxt);
				TransactionValue.commitAll();
			}
			else
			{
    			CPUValue cpu = operation.getCPU();
    			reader = ctxt.threadState.dbgp.newThread(cpu);
    			ctxt.setThreadState(reader, cpu);

    			if (breakAtStart)
    			{
    				// Step at the first location you check (start of body)
    				ctxt.threadState.setBreaks(new LexLocation(), null, null);
    			}

    			result = operation.eval(ctxt.location, new ValueList(), ctxt);
			}

			reader.complete(DBGPReason.OK, null);
		}
		catch (ContextException e)
		{
			reader.complete(DBGPReason.EXCEPTION, e);
		}
		catch (Exception e)
		{
			if (reader != null)
			{
				reader.complete(DBGPReason.EXCEPTION, null);
			}
		}
		finally
		{
			TransactionValue.commitAll();
		}
	}

	public Value getResult() throws ContextException
	{
		if (exception != null)
		{
			throw exception;
		}

		return result;
	}
}
