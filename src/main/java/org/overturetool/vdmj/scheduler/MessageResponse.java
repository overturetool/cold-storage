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

package org.overturetool.vdmj.scheduler;

import org.overturetool.vdmj.runtime.ExitException;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.values.Value;

public class MessageResponse extends MessagePacket
{
	private static final long serialVersionUID = 1L;
	public final Value result;
	public final ValueException exception;
	public final ExitException exitException;
	public final ISchedulableThread caller;
	public final long originalId;

	public MessageResponse(Value result, MessageRequest request)
	{
		super(request.bus, request.to, request.from,	// NB to->from
			request.target, request.operation, request.replyTo);

		this.result = result;
		this.exception = null;
		this.exitException = null;	
		this.caller = request.thread;
		this.originalId = request.msgId;
	}

	public MessageResponse(ValueException exception, MessageRequest request)
	{
		super(request.bus, request.to, request.from,	// NB to->from
			request.target, request.operation, request.replyTo);

		this.result = null;
		this.exception = exception;
		this.exitException = null;	
		this.caller = request.thread;
		this.originalId = request.msgId;
	}

	public MessageResponse(ExitException exception, MessagePacket message) {
		super(message.bus, message.to, message.from,	// NB to->from
				message.target, message.operation, message.replyTo);

			this.result = null;
			this.exception = null;
			this.exitException = exception;	
			this.caller = message.thread;
			this.originalId = message.msgId;
	}
	
	public MessageResponse(ExitException exception, MessageResponse response) {
		super(response.bus, response.to, response.from,	// NB to->from
				response.target, response.operation, response.replyTo);

			this.result = null;
			this.exception = null;
			this.exitException = exception;	
			this.caller = response.thread;
			this.originalId = response.originalId;
	}

	public Value getValue() throws ValueException
	{
		if (exception != null)
		{
			throw exception;
		}
		else if (exitException != null)
		{
			throw exitException;
		}

		return result;
	}

	@Override
	public String toString()
	{
		String str;
		if(result != null) 
		{
			str = result.toString();
		}
		else if(exception != null)
		{
			str = exception.getMessage();
		} else
		{
			str = exitException.getMessage();
		}
		
		return str; 
	}

	public int getSize()
	{
		int size;
			
		if(result != null) 
		{
			size = result.toString().length();
		}
		else if(exception != null)
		{
			size = exception.toString().length();
		} else
		{
			size = exitException.toString().length();
		}
		
		return size;
	}
}
