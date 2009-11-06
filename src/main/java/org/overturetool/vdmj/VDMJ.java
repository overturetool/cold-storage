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

package org.overturetool.vdmj;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.overturetool.vdmj.messages.Console;
import org.overturetool.vdmj.runtime.Interpreter;


/**
 * The main class of the VDMJ parser/checker/interpreter.
 */

abstract public class VDMJ
{
	protected static boolean warnings = true;
	protected static boolean interpret = false;
	protected static boolean pog = false;
	protected static boolean quiet = false;
	protected static String script = null;
	protected static String outfile = null;
	protected static String logfile = null;

	public static String filecharset = Charset.defaultCharset().name();

	private static class Filter implements FilenameFilter
	{
		private String pattern;

		public Filter(String pattern)
		{
			this.pattern = pattern;
		}

		public boolean accept(File dir, String filename)
		{
			return filename.matches(pattern);
		}
	}

	/**
	 * The main method. This validates the arguments, then parses and type
	 * checks the files provided (if any), and finally enters the interpreter
	 * if required.
	 *
	 * @param args Arguments passed to the program.
	 */

	public static void main(String[] args)
	{
		List<File> filenames = new Vector<File>();
		List<String> largs = Arrays.asList(args);
		VDMJ controller = null;
		Filter filter = new Filter("*\\.*");

		for (Iterator<String> i = largs.iterator(); i.hasNext();)
		{
			String arg = i.next();

    		if (arg.equals("-vdmsl"))
    		{
    			controller = new VDMSL();
    			filter = new Filter(".+\\.vdm|.+\\.vdmsl");
    		}
    		else if (arg.equals("-vdmpp"))
    		{
    			controller = new VDMPP();
    			filter = new Filter(".+\\.vpp|.+\\.vdmpp");
    		}
    		else if (arg.equals("-vdmrt"))
    		{
    			controller = new VDMRT();
    			filter = new Filter(".+\\.vpp|.+\\.vdmrt");
    		}
    		else if (arg.equals("-overture"))
    		{
    			controller = new VDMOV();
    		}
    		else if (arg.equals("-w"))
    		{
    			warnings = false;
    		}
    		else if (arg.equals("-i"))
    		{
    			interpret = true;
    		}
    		else if (arg.equals("-p"))
    		{
    			pog = true;
    		}
    		else if (arg.equals("-q"))
    		{
    			quiet = true;
    		}
    		else if (arg.equals("-e"))
    		{
    			interpret = true;
    			pog = false;

    			if (i.hasNext())
    			{
    				script = i.next();
    			}
    			else
    			{
    				usage("-e option requires an expression");
    			}
    		}
    		else if (arg.equals("-o"))
    		{
    			if (i.hasNext())
    			{
    				if (outfile != null)
    				{
    					usage("Only one -o option allowed");
    				}

    				outfile = i.next();
    			}
    			else
    			{
    				usage("-o option requires a filename");
    			}
    		}
    		else if (arg.equals("-c"))
    		{
    			if (i.hasNext())
    			{
    				filecharset = validateCharset(i.next());
    			}
    			else
    			{
    				usage("-c option requires a charset name");
    			}
    		}
    		else if (arg.equals("-t"))
    		{
    			if (i.hasNext())
    			{
    				Console.setCharset(validateCharset(i.next()));
    			}
    			else
    			{
    				usage("-t option requires a charset name");
    			}
    		}
    		else if (arg.equals("-d"))
    		{
    			if (i.hasNext())
    			{
    				File dir = new File(i.next());

    				if (!dir.isDirectory())
    				{
    					usage("-d argument is not a directory: " + dir);
    				}
    				else
    				{
    					for (File file: dir.listFiles(filter))
    					{
    						if (file.isFile())
    						{
    							filenames.add(file);
    						}
    					}
    				}
    			}
    			else
    			{
    				usage("-d option requires a directory");
    			}
    		}
    		else if (arg.equals("-pre"))
    		{
    			Settings.prechecks = false;
    		}
    		else if (arg.equals("-post"))
    		{
    			Settings.postchecks = false;
    		}
    		else if (arg.equals("-inv"))
    		{
    			Settings.invchecks = false;
    		}
    		else if (arg.equals("-dtc"))
    		{
    			// NB. Turn off both when no DTC
    			Settings.invchecks = false;
    			Settings.dynamictypechecks = false;
    		}
    		else if (arg.equals("-log"))
    		{
    			if (i.hasNext())
    			{
    				logfile = i.next();
    			}
    			else
    			{
    				usage("-log option requires a filename");
    			}
    		}
    		else if (arg.startsWith("-"))
    		{
    			usage("Unknown option " + arg);
    		}
    		else
    		{
    			filenames.add(new File(arg));
    		}
		}

		if (controller == null)
		{
			usage("You must specify either -vdmsl, -vdmpp, -vdmrt or -overture");
		}

		if (logfile != null && !(controller instanceof VDMRT))
		{
			usage("The -log option can only be used with -vdmrt");
		}

		ExitStatus status = null;

		if (filenames.isEmpty() && !interpret)
		{
			usage("You didn't specify any files");
			status = ExitStatus.EXIT_ERRORS;
		}
		else
		{
			do
			{
				if (filenames.isEmpty())
				{
					status = controller.interpret(filenames);
				}
				else
				{
            		status = controller.parse(filenames);

            		if (status == ExitStatus.EXIT_OK)
            		{
            			status = controller.typeCheck();

            			if (status == ExitStatus.EXIT_OK && interpret)
            			{
            				status = controller.interpret(filenames);
            			}
            		}
				}
			}
			while (status == ExitStatus.RELOAD);
		}

		if (interpret)
		{
			infoln("Bye");
		}

		System.exit(status == ExitStatus.EXIT_OK ? 0 : 1);
	}

	private static void usage(String msg)
	{
		System.err.println("VDMJ: " + msg + "\n");
		System.err.println("Usage: VDMJ <-vdmsl | -vdmpp | -vdmrt | -overture> [<options>] [<files>]");
		System.err.println("-vdmsl: parse files as VDM-SL");
		System.err.println("-vdmpp: parse files as VDM++");
		System.err.println("-vdmrt: parse files as VICE");
		System.err.println("-overture: parse files as VICE with Overture");
		System.err.println("-w: suppress warning messages");
		System.err.println("-q: suppress information messages");
		System.err.println("-i: run the interpreter if successfully type checked");
		System.err.println("-p: generate proof obligations and stop");
		System.err.println("-e <exp>: evaluate <exp> and stop");
		System.err.println("-c <charset>: select a file charset");
		System.err.println("-t <charset>: select a console charset");
		System.err.println("-o <filename>: saved type checked specification");
		System.err.println("-d <directory>: load all files in this directory");
		System.err.println("-pre: disable precondition checks");
		System.err.println("-post: disable postcondition checks");
		System.err.println("-inv: disable type/state invariant checks");
		System.err.println("-dtc: disable all dynamic type checking");
		System.err.println("-log: enable real-time event logging");

		System.exit(1);
	}

	/**
	 * Parse the list of files passed. The value returned is the number of
	 * syntax errors encountered.
	 *
	 * @param files The files to parse.
	 * @return The number of syntax errors.
	 */

	public abstract ExitStatus parse(List<File> files);

	/**
	 * Type check the files previously parsed by {@link #parse(List)}. The
	 * value returned is the number of type checking errors.
	 *
	 * @return The number of type check errors.
	 */

	public abstract ExitStatus typeCheck();

	/**
	 * Generate an interpreter from the classes parsed.
	 * @return An initialized interpreter.
	 * @throws Exception
	 */

	public abstract Interpreter getInterpreter() throws Exception;

	/**
	 * Interpret the type checked specification. The number returned is the
	 * number of runtime errors not caught by the interpreter (usually zero
	 * or one).
	 *
	 * @param filenames The filenames currently loaded.
	 * @return The exit status of the interpreter.
	 */

	abstract protected ExitStatus interpret(List<File> filenames);

	public void setWarnings(boolean w)
	{
		warnings = w;
	}

	public void setCharset(String charset)
	{
		filecharset = charset;
		Console.setCharset(charset);
	}

	protected static void info(String m)
	{
		if (!quiet)
		{
			print(m);
		}
	}

	protected static void infoln(String m)
	{
		if (!quiet)
		{
			println(m);
		}
	}

	protected static void print(String m)
	{
		Console.out.print(m);
		Console.out.flush();
	}

	protected static void println(String m)
	{
		Console.out.println(m);
	}

	protected String plural(int n, String s, String pl)
	{
		return n + " " + (n != 1 ? s + pl : s);
	}

	private static String validateCharset(String cs)
	{
		if (!Charset.isSupported(cs))
		{
			println("Charset " + cs + " is not supported\n");
			println("Available charsets:");
			println("Default = " + Charset.defaultCharset());
			Map<String,Charset> available = Charset.availableCharsets();

			for (String name: available.keySet())
			{
				println(name + " " + available.get(name).aliases());
			}

			println("");
			usage("Charset " + cs + " is not supported");
		}

		return cs;
	}
}
