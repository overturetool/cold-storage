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

package org.overturetool.vdmj.debug;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.definitions.ClassList;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.messages.MessageException;
import org.overturetool.vdmj.runtime.Breakpoint;
import org.overturetool.vdmj.runtime.ClassInterpreter;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.Interpreter;

public class DBGPReader
{
	private InputStreamReader input;
	private OutputStreamWriter output;
	private Interpreter interpreter;

	private int sessionId;
	private DBGPStatus status = null;
	private String statusReason = "";
	private String command = "";
	private String transaction = "";
	private DBGPFeatures features;
	private Context breakContext = null;

	public static void main(String[] args) throws Exception
	{
		Settings.dialect = Dialect.VDM_PP;
		DBGPReader r = new DBGPReader(new ClassInterpreter(new ClassList()), 0);
		r.run();
	}

	public DBGPReader(Interpreter interpreter, int port) throws Exception
	{
		this.interpreter = interpreter;

		if (port > 0)
		{
			InetAddress server = InetAddress.getLocalHost();
			Socket socket = new Socket(server, port);
			input = new InputStreamReader(socket.getInputStream());
			output = new OutputStreamWriter(socket.getOutputStream());
		}
		else
		{
			input = new InputStreamReader(System.in);
			output = new OutputStreamWriter(System.out);
		}

		init();
	}

	private void init() throws IOException
	{
		sessionId = Math.abs(new Random().nextInt());
		status = DBGPStatus.STARTING;
		features = new DBGPFeatures();

		StringBuilder sb = new StringBuilder();

		sb.append("<init ");
		sb.append("appid=\"");
		sb.append(features.getProperty("language_name"));
		sb.append("\" ");
		sb.append("idekey=\"\" ");
		sb.append("session=\"" + sessionId + "\" ");
		sb.append("thread=\"");
		sb.append(Thread.currentThread().getId());
		sb.append("\" ");
		sb.append("parent=\"");
		sb.append(features.getProperty("language_name"));
		sb.append("\" ");
		sb.append("language=\"");
		sb.append(features.getProperty("language_name"));
		sb.append("\" ");
		sb.append("protocol_version=\"");
		sb.append(features.getProperty("protocol_version"));
		sb.append("\" ");

		// Get source files from interpreter...
		sb.append("fileuri=\"file://???\" ");

		sb.append("/>\n");

		write(sb);
	}

	private String readLine() throws IOException
	{
		StringBuilder line = new StringBuilder();
		char c = (char)input.read();

		while (c != '\n' && c >= 0)
		{
			if (c != '\r') line.append(c);		// Ignore CRs
			c = (char)input.read();
		}

		return (line.length() == 0 && c == -1) ? null : line.toString();
	}

	private void write(StringBuilder data) throws IOException
	{
		output.write(Integer.toString(data.length()));
		output.write(0);
		output.write(data.toString());
		output.write(0);
		output.flush();
	}

	private void response(StringBuilder hdr, StringBuilder body) throws IOException
	{
		StringBuilder sb = new StringBuilder();

		sb.append("<response command=\"");
		sb.append(command);
		sb.append("\"");

		if (hdr != null)
		{
			sb.append(" ");
			sb.append(hdr);
		}

		sb.append(" transaction_id=\"");
		sb.append(transaction);
		sb.append("\">");

		if (body != null)
		{
			sb.append(body);
		}

		sb.append("</response>\n");

		write(sb);
	}

	private void errorResponse(DBGPErrorCode errorCode, String reason)
	{
		try
		{
			StringBuilder sb = new StringBuilder();

			sb.append("<error code=\"");
			sb.append(errorCode);
			sb.append("\" apperr=\"\"><message>");
			sb.append(reason);
			sb.append("</message></error>");

			response(null, sb);
		}
		catch (IOException e)
		{
			throw new MessageException("Internal 0029: DBGP: " + reason);
		}
	}

	private void statusResponse() throws IOException
	{
		statusResponse(status, statusReason);
	}

	private void statusResponse(DBGPStatus s, String reason) throws IOException
	{
		StringBuilder sb = new StringBuilder();

		status = s;
		statusReason = reason;

		sb.append("status=\"");
		sb.append(status);
		sb.append(" reason=\"");
		sb.append(statusReason);

		response(sb, null);
	}

	private StringBuilder breakpointResponse(Breakpoint bp)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("<breakpoint id=\"" + bp.number + "\"");
		sb.append(" type=\"line\"");
		sb.append(" state=\"enabled\"");
		sb.append(" filename=\"" + bp.location.file + "\"");
		sb.append(" lineno=\"" + bp.location.startLine + "\"");
		sb.append(" function=\"?\"");
		sb.append(" exception=\"?\"");
		sb.append(" hit_value=\"?\"");
		sb.append(" hit_condition=\"?\"");
		sb.append(" hit_count=\"?\">");
		sb.append("<expression>" + bp.trace + "</expression>");
		sb.append("</breakpoint>");

		return sb;
	}

	private StringBuilder stackResponse(Context ctxt, int level)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("<stack level=\"" + level + "\"");
		sb.append(" type=\"file\"");
		sb.append(" filename=\"" + ctxt.location.file + "\"");
		sb.append(" lineno=\"" + ctxt.location.startLine);
		sb.append(" cmdbegin=\"" + ctxt.location.file + ":" + ctxt.location.startPos + "\"");
		sb.append("</stack>");

		return sb;
	}

	private void run() throws IOException
	{
		String line = null;

		do
		{
			line = readLine();
		}
		while (line != null && process(line));
	}

	private boolean process(String line)
	{
		try
		{
			command = "?";
			transaction = "?";

    		String[] parts = line.split("\\s+");
    		DBGPCommand c = parse(parts);

    		switch (c.type)
    		{
    			case STATUS:
    				processStatus(c);
    				break;

    			case FEATURE_GET:
    				processFeatureGet(c);
    				break;

    			case FEATURE_SET:
    				processFeatureSet(c);
    				break;

    			case RUN:
    				processRun(c);
    				break;

    			case STEP_INTO:
    				processStepInto(c);
    				break;

    			case STEP_OVER:
    				processStepOver(c);
    				break;

    			case STEP_OUT:
    				processStepOut(c);
    				break;

    			case STOP:
    				processStop(c);
    				break;

    			case BREAKPOINT_GET:
    				breakpointGet(c);
    				break;

    			case BREAKPOINT_SET:
    				breakpointSet(c, null);
    				break;

    			case BREAKPOINT_UPDATE:
    				breakpointUpdate(c);
    				break;

    			case BREAKPOINT_REMOVE:
    				breakpointRemove(c);
    				break;

    			case BREAKPOINT_LIST:
    				breakpointList(c);
    				break;

    			case STACK_DEPTH:
    				stackDepth(c);
    				break;

    			case STACK_GET:
    				stackGet(c);
    				break;

    			case CONTEXT_NAMES:
    			case CONTEXT_GET:
    			case DETACH:
    			default:
    				errorResponse(DBGPErrorCode.NOT_AVAILABLE, c.type.value);
    		}
		}
		catch (DBGPException e)
		{
			errorResponse(e.code, e.reason);
		}
		catch (Exception e)
		{
			errorResponse(DBGPErrorCode.PARSE, e.getMessage());
		}

		return true;	// carry on
	}

	private DBGPCommand parse(String[] parts) throws DBGPException
	{
		// "<type> [<options>] [-- <args>]"

		DBGPCommandType type = null;
		List<DBGPOption> options = new Vector<DBGPOption>();
		List<String> args = new Vector<String>();
		boolean doneOpts = false;
		boolean gotXID = false;

		try
		{
			type = DBGPCommandType.lookup(parts[0]);
			command = type.value;

			for (int i=1; i<parts.length; i++)
			{
				if (doneOpts)
				{
					args.add(parts[i]);
				}
				else
				{
	    			if (parts[i].equals("--"))
	    			{
	    				doneOpts = true;
	    			}
	     			else
	    			{
	    				DBGPOptionType opt = DBGPOptionType.lookup(parts[i++]);

	    				if (opt == DBGPOptionType.TRANSACTION_ID)
	    				{
	    					gotXID = true;
	    					transaction = parts[i];
	    				}

						options.add(new DBGPOption(opt, parts[i]));
	     			}
				}
			}

			if (!gotXID)
			{
				throw new Exception("No transaction_id");
			}
		}
		catch (DBGPException e)
		{
			throw e;
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			throw new DBGPException(
				DBGPErrorCode.INVALID_OPTIONS, "Option arg missing");
		}
		catch (Exception e)
		{
			if (doneOpts)
			{
				throw new DBGPException(DBGPErrorCode.PARSE, e.getMessage());
			}
			else
			{
				throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, e.getMessage());
			}
		}

		return new DBGPCommand(type, options, args);
	}

	private void checkArgs(DBGPCommand c, int n) throws DBGPException
	{
		if (!c.args.isEmpty())
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		if (c.options.size() != n)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}
	}

	private void processStatus(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 1);
		statusResponse();
	}

	private void processFeatureGet(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 2);
		DBGPOption option = c.getOption(DBGPOptionType.N);

		if (option == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		String feature = features.getProperty(option.value);

		if (feature == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		StringBuilder hdr = new StringBuilder();

		hdr.append("feature_name=\"");
		hdr.append(option.value);
		hdr.append("\" supported=\"1\"");

		StringBuilder body = new StringBuilder();
		body.append(feature);

		response(hdr, body);
	}

	private void processFeatureSet(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 3);
		DBGPOption option = c.getOption(DBGPOptionType.N);

		if (option == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		String feature = features.getProperty(option.value);

		if (feature == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		DBGPOption newval = c.getOption(DBGPOptionType.V);

		if (newval == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		features.setProperty(option.value, newval.value);

		StringBuilder hdr = new StringBuilder();

		hdr.append("feature_name=\"");
		hdr.append(option.value);
		hdr.append("\" success=\"1\"");

		response(hdr, null);
	}

	private void processRun(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 1);

		if (status != DBGPStatus.STARTING)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_CONTEXT, c.toString());
		}

		statusResponse(DBGPStatus.RUNNING, "OK");
		// actually start... !!
	}

	private void processStepInto(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 1);

		if (status != DBGPStatus.BREAK)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_CONTEXT, c.toString());
		}

		statusResponse(DBGPStatus.BREAK, "OK");
		// actually step... !!
	}

	private void processStepOver(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 1);

		if (status != DBGPStatus.BREAK)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_CONTEXT, c.toString());
		}

		statusResponse(DBGPStatus.BREAK, "OK");
		// actually step... !!
	}

	private void processStepOut(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 1);

		if (status != DBGPStatus.BREAK)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_CONTEXT, c.toString());
		}

		statusResponse(DBGPStatus.BREAK, "OK");
		// actually step... !!
	}

	private void processStop(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 1);

		if (status != DBGPStatus.BREAK)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_CONTEXT, c.toString());
		}

		statusResponse(DBGPStatus.STOPPING, "OK");
		// actually stop... !!
	}

	private void breakpointGet(DBGPCommand c) throws DBGPException
	{
		checkArgs(c, 2);

		DBGPOption option = c.getOption(DBGPOptionType.D);

		if (option == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		Breakpoint bp = interpreter.breakpoints.get(Integer.parseInt(option.value));

		if (bp == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_BREAKPOINT, c.toString());
		}

		breakpointResponse(bp);
	}

	private void breakpointSet(DBGPCommand c, Breakpoint update)
		throws DBGPException, IOException
	{
		DBGPOption option = c.getOption(DBGPOptionType.T);

		if (option == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		DBGPBreakpointType type = DBGPBreakpointType.lookup(option.value);

		if (type == null)
		{
			throw new DBGPException(DBGPErrorCode.BREAKPOINT_TYPE_UNSUPPORTED, option.value);
		}

		option = c.getOption(DBGPOptionType.S);

		if (option != null)
		{
    		if (!option.value.equalsIgnoreCase("enabled"))
    		{
    			throw new DBGPException(DBGPErrorCode.INVALID_BREAKPOINT, option.value);
    		}
		}

		option = c.getOption(DBGPOptionType.F);
		String filename = null;

		if (option != null)
		{
			filename = new File(option.value).getPath();
		}

		option = c.getOption(DBGPOptionType.N);
		int lineno = 0;

		if (option != null)
		{
			lineno = Integer.parseInt(option.value);
		}

		option = c.getOption(DBGPOptionType.M);

		if (option != null)
		{
   			throw new DBGPException(DBGPErrorCode.INVALID_BREAKPOINT, option.value);
		}

		option = c.getOption(DBGPOptionType.X);

		if (option != null)
		{
   			throw new DBGPException(DBGPErrorCode.INVALID_BREAKPOINT, option.value);
		}

		option = c.getOption(DBGPOptionType.H);

		if (option != null)
		{
   			throw new DBGPException(DBGPErrorCode.INVALID_BREAKPOINT, option.value);
		}

		option = c.getOption(DBGPOptionType.O);

		if (option != null)
		{
   			throw new DBGPException(DBGPErrorCode.INVALID_BREAKPOINT, option.value);
		}

		option = c.getOption(DBGPOptionType.R);

		if (option != null)
		{
   			throw new DBGPException(DBGPErrorCode.INVALID_BREAKPOINT, option.value);
		}

		// set or update the breakpoint!
		Breakpoint bp = null;

		response(null, breakpointResponse(bp));
	}

	private void breakpointUpdate(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 2);

		DBGPOption option = c.getOption(DBGPOptionType.D);

		if (option == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		Breakpoint bp = interpreter.breakpoints.get(Integer.parseInt(option.value));

		if (bp == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_BREAKPOINT, c.toString());
		}

		breakpointSet(c, bp);
	}

	private void breakpointRemove(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 2);

		DBGPOption option = c.getOption(DBGPOptionType.D);

		if (option == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		Breakpoint bp = interpreter.breakpoints.get(Integer.parseInt(option.value));

		if (bp == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_BREAKPOINT, c.toString());
		}

		response(null, null);
	}

	private void breakpointList(DBGPCommand c) throws IOException, DBGPException
	{
		checkArgs(c, 1);
		StringBuilder bps = new StringBuilder();

		for (Integer key: interpreter.breakpoints.keySet())
		{
			Breakpoint bp = interpreter.breakpoints.get(key);
			bps.append(breakpointResponse(bp));
		}

		response(null, bps);
	}

	private void stackDepth(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 1);

		if (status != DBGPStatus.BREAK)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_CONTEXT, c.toString());
		}

		StringBuilder sb = new StringBuilder();
		sb.append(breakContext.getDepth());

		response(null, sb);
	}

	private void stackGet(DBGPCommand c) throws DBGPException, IOException
	{
		checkArgs(c, 2);

		if (status != DBGPStatus.BREAK)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_CONTEXT, c.toString());
		}

		DBGPOption option = c.getOption(DBGPOptionType.D);

		if (option == null)
		{
			throw new DBGPException(DBGPErrorCode.INVALID_OPTIONS, c.toString());
		}

		int depth = Integer.parseInt(option.value);	// 0 to n-1

		if (depth >= breakContext.getDepth())
		{
			throw new DBGPException(DBGPErrorCode.INVALID_STACK_DEPTH, c.toString());
		}

		Context ctxt = breakContext.getFrame(depth);

		response(null, stackResponse(ctxt, depth));
	}
}
