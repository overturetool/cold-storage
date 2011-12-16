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
import java.util.Map;

import org.overture.interpreter.ast.analysis.IAnalysisInterpreter;
import org.overture.interpreter.ast.analysis.IAnswerInterpreter;
import org.overture.interpreter.ast.analysis.IQuestionAnswerInterpreter;
import org.overture.interpreter.ast.analysis.IQuestionInterpreter;
import org.overture.interpreter.ast.expressions.PExpInterpreter;
import org.overture.interpreter.ast.node.NodeEnumInterpreter;
import org.overture.interpreter.ast.node.NodeInterpreter;
import org.overturetool.interpreter.vdmj.lex.LexLocation;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexException;
import org.overturetool.vdmj.messages.Console;
import org.overturetool.vdmj.scheduler.BasicSchedulableThread;
import org.overturetool.vdmj.syntax.ParserException;


/**
 * The root of the breakpoint class hierarchy.
 */

public class Breakpoint extends NodeInterpreter implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** The location of the breakpoint. */
	public final LexLocation location;
	/** The number of the breakpoint. */
	public final int number;
	/** The condition or trace expression, in parsed form. */
	public final PExpInterpreter parsed;
	/** The condition or trace expression, in raw form. */
	public final String trace;

	/** The number of times this breakpoint has been reached. */
	public long hits = 0;

	/** The condition saying if a the breakpoint is enabled */
	protected boolean enabled = true;
	
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
			parsed = BreakpointParser.parse(trace, location);
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

	public String stoppedAtString()
	{
		return "Stopped [" + BasicSchedulableThread.getThreadName(Thread.currentThread()) + "] " + location;
	}

	public void clearHits()
	{
		hits = 0;
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
		//skips if breakpoint is disabled
//		if(!enabled){
//			return;
//		}
		
		location.hit();
		hits++;

		ThreadState state = ctxt.threadState;

		if (Settings.dialect != Dialect.VDM_SL)
		{
			state.reschedule(ctxt, execl);
		}

		if (state.stepline != null)
		{
			if (execl.startLine != state.stepline.startLine)	// NB just line, not pos
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
	
	public void setEnabled(boolean bool)
	{
		this.enabled = bool;
	}
	
	public boolean isEnabled()
	{
		return this.enabled;
	}

	@Override
	public Object clone()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeInterpreter clone(
			Map<NodeInterpreter, NodeInterpreter> oldToNewMap)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeEnumInterpreter kindNode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeChild(NodeInterpreter child)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void apply(IAnalysisInterpreter analysis)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public <A> A apply(IAnswerInterpreter<A> caller)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Q> void apply(IQuestionInterpreter<Q> caller, Q question)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public <Q, A> A apply(IQuestionAnswerInterpreter<Q, A> caller, Q question)
	{
		// TODO Auto-generated method stub
		return null;
	}
}