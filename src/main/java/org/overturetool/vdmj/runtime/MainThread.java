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

import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.scheduler.CPUResource;
import org.overturetool.vdmj.scheduler.SchedulableThread;
import org.overturetool.vdmj.values.TransactionValue;
import org.overturetool.vdmj.values.UndefinedValue;
import org.overturetool.vdmj.values.Value;

/**
 * A class representing the main VDM thread.
 */

public class MainThread extends SchedulableThread
{
	private static final long serialVersionUID = 1L;
	public final Context ctxt;
	public final Expression expression;

	private Value result = new UndefinedValue();
	private RuntimeException exception = null;

	public MainThread(Expression expr, Context ctxt)
	{
		super(CPUResource.vCPU, null, 0, false, 0);

		this.expression = expr;
		this.ctxt = ctxt;

		setName("MainThread-" + getId());
	}

	@Override
	public int hashCode()
	{
		return (int)getId();
	}

	@Override
	public void body()
	{
		try
		{
			result = expression.eval(ctxt);
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

	public void setException(RuntimeException exception)
	{
		this.exception = exception;
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
