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

import java.io.PrintWriter;

import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.values.Value;

/**
 * A root context for non-object method invocations.
 */

@SuppressWarnings("serial")
public class StateContext extends RootContext
{
	/** The state context, if any. */
	public final Context stateCtxt;

	/**
	 * Create a RootContext from the values passed.
	 *
	 * @param location The location of the context.
	 * @param title The name of the location.
	 * @param outer The context chain (not searched).
	 * @param sctxt Any state context.
	 */

	public StateContext(LexLocation location, String title,
		Context freeVariables, Context outer, Context sctxt)
	{
		super(location, title, freeVariables, outer);
		this.stateCtxt = sctxt;
	}

	public StateContext(LexLocation location, String title,
		Context outer, Context sctxt)
	{
		this(location, title, null, outer, sctxt);
	}

	/**
	 * Create a RootContext with no outer context or state.
	 * @param location The location of the context.
	 * @param title The name of the location.
	 */

	public StateContext(LexLocation location, String title)
	{
		super(location, title, null, null);
		this.stateCtxt = null;
	}

	/**
	 * Check for the name in the current context and state, and if
	 * not present search the global context. Note that the context
	 * chain is not followed.
	 *
	 * @see org.overturetool.vdmj.runtime.Context#check(org.overturetool.vdmj.lex.LexNameToken)
	 */

	@Override
	public Value check(LexNameToken name)
	{
		Value v = get(name);

		if (v != null)
		{
			return v;
		}

		if (freeVariables != null)
		{
			v = freeVariables.get(name);

			if (v != null)
			{
				return v;
			}
		}

		// A RootContext stops the name search from continuing down the
		// context chain. It first checks any state context, then goes
		// down to the global level.

		if (v == null)
		{
			if (stateCtxt != null)
			{
				v = stateCtxt.check(name);

				if (v != null)
				{
					return v;
				}
			}

			Context g = getGlobal();

			if (g != this)
			{
				return g.check(name);
			}
		}

		return v;
	}

	@Override
	public String toString()
	{
		if (stateCtxt != null)
		{
			return super.toString() + "\tState visible\n";
		}
		else
		{
			return super.toString();
		}
	}

	@Override
	public void printStackTrace(PrintWriter out, boolean variables)
	{
		if (outer == null)		// Don't expand initial context
		{
			out.println("In root context of " + title);
		}
		else
		{
			if (variables)
			{
    			out.print(this.format("\t", this));

    			if (stateCtxt != null)
    			{
    				out.println("\tState visible");
    			}
			}

			out.println("In root context of " + title + " " + location);
			outer.printStackTrace(out, false);
		}
	}
}
