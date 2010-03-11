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

import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.debug.DBGPReader;
import org.overturetool.vdmj.debug.DBGPReason;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.runtime.ClassInterpreter;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ContextException;
import org.overturetool.vdmj.runtime.ObjectContext;
import org.overturetool.vdmj.runtime.RootContext;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.values.CPUValue;
import org.overturetool.vdmj.values.ObjectValue;
import org.overturetool.vdmj.values.OperationValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueList;

public class AsyncThread extends SchedulableThread
{
	public final MessageRequest request;

	public final ObjectValue self;
	public final OperationValue operation;
	public final ValueList args;
	public final CPUValue cpu;
	public final boolean breakAtStart;

	public AsyncThread(MessageRequest request)
	{
		super(
			request.target.getCPU().resource,
			request.target,
			request.operation.getPriority(),
			false, 0);

		setName("AsyncThread-" + getId());

		this.self = request.target;
		this.operation = request.operation;
		this.args = request.args;
		this.cpu = self.getCPU();
		this.breakAtStart = request.breakAtStart;
		this.request = request;
	}

	public AsyncThread(
		ObjectValue self, OperationValue operation, ValueList args, boolean stepping)
	{
		super(
			self.getCPU().resource,
			self,
			operation.getPriority(),
			false, 0);

		setName("Async Thread " + getId());

		this.self = self;
		this.operation = operation;
		this.args = args;
		this.cpu = self.getCPU();
		this.breakAtStart = stepping;
		this.request = null;
	}

	@Override
	protected void body()
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

	private void runDBGP()
	{
		DBGPReader reader = null;

		try
		{
    		MessageResponse response = null;

    		try
    		{
        		RootContext global = ClassInterpreter.getInstance().initialContext;
        		LexLocation from = self.type.classdef.location;
        		Context ctxt = new ObjectContext(from, "async", global, self);
    			reader = ctxt.threadState.dbgp.newThread(cpu);
    			ctxt.setThreadState(reader, cpu);

    			if (breakAtStart)
    			{
    				// Step at the first location you check (start of body)
    				ctxt.threadState.setBreaks(new LexLocation(), null, null);
    			}

        		Value rv = operation.localEval(operation.name.location, args, ctxt, false);
       			response = new MessageResponse(rv, request);
    		}
    		catch (ValueException e)
    		{
    			response = new MessageResponse(e, request);
    		}

    		if (request.replyTo != null)
    		{
    			request.bus.reply(response);
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
	}

	private void runCmd()
	{
		try
		{
    		RootContext global = ClassInterpreter.getInstance().initialContext;
    		LexLocation from = self.type.classdef.location;
    		Context ctxt = new ObjectContext(from, "async", global, self);
    		ctxt.setThreadState(null, cpu);

			if (breakAtStart)
			{
				// Step at the first location you check (start of body)
				ctxt.threadState.setBreaks(new LexLocation(), null, null);
			}

    		Value result = operation.localEval(
    			operation.name.location, args, ctxt, false);

			if (request.replyTo != null)
			{
				request.bus.reply(new MessageResponse(result, request));
			}
		}
		catch (ValueException e)
		{
			if (request.replyTo != null)
			{
				request.bus.reply(new MessageResponse(e, request));
			}
		}
	}
}
