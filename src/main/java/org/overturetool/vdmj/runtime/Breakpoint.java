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

import java.io.Serializable;

import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexException;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.messages.Console;
import org.overturetool.vdmj.syntax.ExpressionReader;
import org.overturetool.vdmj.syntax.ParserException;

/**
 * The root of the breakpoint class hierarchy.
 */

public class Breakpoint implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** The location of the breakpoint. */
	public final LexLocation location;
	/** The number of the breakpoint. */
	public final int number;
	/** The condition or trace expression, in parsed form. */
	public final Expression parsed;
	/** The condition or trace expression, in raw form. */
	public final String trace;

	public Breakpoint(LexLocation location)
	{
		this.location = location;
		this.number = 0;
		this.trace = null;
		this.parsed = null;
	}

	/**
	 * Create a breakpoint at the given location. The trace string is
	 * parsed to an Expression structure for subsequent evaluation.
	 *
	 * @param location The location of the breakpoint.
	 * @param number The number that appears in the "list" command.
	 * @param trace Any condition or trace expression.
	 *
	 * @throws ParserException
	 * @throws LexException
	 */

	public Breakpoint(LexLocation location, int number, String trace)
		throws ParserException, LexException
	{
		this.location = location;
		this.number = number;
		this.trace = trace;

		if (trace != null)
		{
			LexTokenReader ltr = new LexTokenReader(trace, Dialect.VDM_PP);
			ExpressionReader reader = new ExpressionReader(ltr);
			reader.setCurrentModule(location.module);
			parsed = reader.readExpression();
		}
		else
		{
			parsed = null;
		}
	}

	@Override
	public String toString()
	{
		return location.toString();
	}

	/**
	 * Check whether to stop. The implementation in Breakpoint is used to check
	 * for the "step" and "next" commands, using the stepline, nextctxt and
	 * outctxt fields. If the current line is different to the last step line,
	 * and the current context is not "above" the next context or the current
	 * context equals the out context or neither the next or out context are
	 * set, a {@link Stoppoint} is created and its check method is called -
	 * which starts a DebuggerReader session.
	 *
	 * @param execl The execution location.
	 * @param ctxt The execution context.
	 */

	public void check(LexLocation execl, Context ctxt)
	{
		location.hit();
		ThreadState state = ctxt.threadState;
		handleInterrupt(execl, ctxt);

		if (state.stepline > 0)
		{
			if (execl.startLine != state.stepline)
			{
				if ((state.nextctxt == null && state.outctxt == null) ||
					(state.nextctxt != null && !isAboveNext(ctxt.getRoot())) ||
					(state.outctxt != null && isOutOrBelow(ctxt)))
				{
        			try
        			{
        				new Stoppoint(location, 0, null).check(location, ctxt);
        			}
        			catch (DebuggerException e)
        			{
        				throw e;
        			}
        			catch (Exception e)
        			{
        				// This happens when the Stoppoint throws an error, which
        				// can't happen. But we need a catch clause for it anyway.

        				throw new DebuggerException(
        					"Breakpoint [" + number + "]: " + e.getMessage());
        			}
				}
			}
		}
	}

	/**
	 * Handle an interrupted thread condition. If the interrupt action is set
	 * to SUSPEND, the method sleeps until it receives another interrupt; if
	 * it is set to CONTINUE, it continues immediately; and if it is set to
	 * STOP, it throws a StopException.
	 */

	public static void handleInterrupt(LexLocation loc, Context ctxt)
	{
		do
		{
			switch (ctxt.threadState.action)
			{
				case SUSPENDED:
					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException e)
					{
						// Change of action
						break;
					}

				case RUNNING:
					break;

				case STOPPING:
					throw new StopException(loc, ctxt);
			}
		}
		while (ctxt.threadState.action != InterruptAction.RUNNING);
	}


	/**
	 * True, if the context passed is above nextctxt. That means that the
	 * current context must have an "outer" chain that reaches nextctxt.
	 *
	 * @param current The context to test.
	 * @return True if the current context is above nextctxt.
	 */

	private boolean isAboveNext(Context current)
	{
		Context c = current.outer;

		while (c != null)
		{
			if (c == current.threadState.nextctxt) return true;
			c = c.outer;
		}

		return false;
	}


	/**
	 * True, if the context passed is equal to or below outctxt. That means that
	 * outctxt must have an "outer" chain that reaches current context.
	 *
	 * @param current The context to test.
	 * @return True if the current context is at or below outctxt.
	 */

	private boolean isOutOrBelow(Context current)
	{
		Context c = current.threadState.outctxt;

		while (c != null)
		{
			if (c == current) return true;
			c = c.outer;
		}

		return false;
	}

	protected void print(String line)
	{
		Console.out.print(line);
		Console.out.flush();
	}

	protected void println(String line)
	{
		Console.out.println(line);
	}
}
