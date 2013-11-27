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

package org.overturetool.vdmj.statements;

import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.pog.POContextStack;
import org.overturetool.vdmj.pog.ProofObligationList;
import org.overturetool.vdmj.runtime.ClassInterpreter;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ObjectContext;
import org.overturetool.vdmj.runtime.RootContext;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.scheduler.ObjectThread;
import org.overturetool.vdmj.scheduler.PeriodicThread;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.types.ClassType;
import org.overturetool.vdmj.types.SetType;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.VoidType;
import org.overturetool.vdmj.values.ObjectValue;
import org.overturetool.vdmj.values.OperationValue;
import org.overturetool.vdmj.values.SetValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueSet;
import org.overturetool.vdmj.values.VoidValue;

public class StartStatement extends Statement
{
	private static final long serialVersionUID = 1L;
	public final Expression objects;

	public StartStatement(LexLocation location, Expression obj)
	{
		super(location);
		this.objects = obj;
	}

	@Override
	public Type typeCheck(Environment env, NameScope scope)
	{
		Type type = objects.typeCheck(env, null, scope);

		if (type.isSet())
		{
			SetType set = type.getSet();

			if (!set.setof.isClass())
			{
				objects.report(3235, "Expression is not a set of object references");
			}
			else
			{
				ClassType ctype = set.setof.getClassType();

				if (ctype.classdef.findThread() == null)
				{
					objects.report(3236, "Class does not define a thread");
				}
			}
		}
		else if (type.isClass())
		{
			ClassType ctype = type.getClassType();

			if (ctype.classdef.findThread() == null)
			{
				objects.report(3237, "Class does not define a thread");
			}
		}
		else
		{
			objects.report(3238, "Expression is not an object reference or set of object references");
		}

		return new VoidType(location);
	}

	@Override
	public Value eval(Context ctxt)
	{
		breakpoint.check(location, ctxt);

		try
		{
			Value value = objects.eval(ctxt);

			if (value.isType(SetValue.class))
			{
				ValueSet set = value.setValue(ctxt);

				for (Value v: set)
				{
					ObjectValue target = v.objectValue(ctxt);
					OperationValue op = target.getThreadOperation(ctxt);

					start(target, op, ctxt);
				}
			}
			else
			{
				ObjectValue target = value.objectValue(ctxt);
				OperationValue op = target.getThreadOperation(ctxt);

				start(target, op, ctxt);
			}

			return new VoidValue();
		}
		catch (ValueException e)
		{
			return abort(e);
		}
	}

	private void start(ObjectValue target, OperationValue op, Context ctxt)
		throws ValueException
	{
		if (op.body instanceof PeriodicStatement)
		{
    		RootContext global = ClassInterpreter.getInstance().initialContext;
    		Context pctxt = new ObjectContext(op.name.location, "periodic", global, target);
			PeriodicStatement ps = (PeriodicStatement)op.body;
			
			// We disable the swapping and time (RT) as periodic evaluation should be "free".
			try
			{
				pctxt.threadState.setAtomic(true);
				ps.eval(pctxt);	// Ignore return value
			}
			finally
			{
				pctxt.threadState.setAtomic(false);
			}
			
			OperationValue pop = pctxt.lookup(ps.opname).operationValue(pctxt);

			long period = ps.values[0];
			long jitter = ps.values[1];
			long delay  = ps.values[2];
			long offset = ps.values[3];

			// Note that periodic threads never set the stepping flag

			new PeriodicThread(
				target, pop, period, jitter, delay, offset, 0, false).start();
		}
		else if (op.body instanceof SporadicStatement)
		{
    		RootContext global = ClassInterpreter.getInstance().initialContext;
    		Context pctxt = new ObjectContext(op.name.location, "sporadic", global, target);
    		SporadicStatement ss = (SporadicStatement)op.body;
			
			// We disable the swapping and time (RT) as sporadic evaluation should be "free".
			pctxt.threadState.setAtomic(true);
			ss.eval(pctxt);	// Ignore return value
			pctxt.threadState.setAtomic(false);
			
			OperationValue pop = pctxt.lookup(ss.opname).operationValue(pctxt);

			long delay  = ss.values[0];
			long jitter = ss.values[1];		// Jitter used for maximum delay
			long offset = ss.values[2];
			long period = 0;

			// Note that periodic threads never set the stepping flag

			new PeriodicThread(
				target, pop, period, jitter, delay, offset, 0, true).start();
		}
		else
		{
			new ObjectThread(location, target, ctxt).start();
		}
	}

	@Override
	public Expression findExpression(int lineno)
	{
		return objects.findExpression(lineno);
	}

	@Override
	public ProofObligationList getProofObligations(POContextStack ctxt)
	{
		return objects.getProofObligations(ctxt);
	}

	@Override
	public String kind()
	{
		return "start";
	}

	@Override
	public String toString()
	{
		return kind() + "(" + objects + ")";
	}
}
