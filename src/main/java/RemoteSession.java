/*******************************************************************************
 *
 *	Copyright (c) 2011 Fujitsu Services Ltd.
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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.overturetool.vdmj.debug.RemoteControl;
import org.overturetool.vdmj.debug.RemoteInterpreter;
import org.overturetool.vdmj.runtime.SourceFile;

public class RemoteSession implements RemoteControl
{
	private final static String COVERAGE = "./generated/word/";

	public void run(RemoteInterpreter interpreter)
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		boolean carryOn = true;
		System.out.println("Session started in " + new File(".").getAbsolutePath());

		while (carryOn)
		{
			try
			{
				System.out.print(">> ");
				String line = in.readLine();

				if (line == null)
				{
					return;	// EOF
				}
				else if (line.length() == 0)
				{
					// Fine...
				}
				else if (line.equals("quit"))
				{
					in.close();
					carryOn = false;
				}
				else if (line.equals("?") || line.equals("help"))
				{
					System.out.println("? or help    - show this message");
					System.out.println("init         - reinitialize the specification");
					System.out.println("create       - create a local value");
					System.out.println("env          - show the local environment and values");
					System.out.println("files        - list the source files of the specification");
					System.out.println("coverage     - write Word HTML coverage for all files");
					System.out.println("modules      - list the modules loaded");
					System.out.println("classes      - list the classes loaded");
					System.out.println("<expression> - evaluate the expression");
					System.out.println("quit         - leave the session");
				}
				else if (line.equals("init"))
				{
					long before = System.currentTimeMillis();
					interpreter.init();
		   			long after = System.currentTimeMillis();
		   			System.out.println("Initialized in " + (double)(after-before)/1000 + " secs.");
				}
				else if (line.startsWith("create "))
				{
					Pattern p = Pattern.compile("^create (\\w+)\\s*?:=\\s*(.+)$");
					Matcher m = p.matcher(line);

					if (m.matches())
					{
						String var = m.group(1);
						String exp = m.group(2);

						interpreter.create(var, exp);
						System.out.println(var + " = " + interpreter.execute(var));
					}
					else
					{
						System.out.println("Usage: create <id> := <value>");
					}
				}
				else if (line.equals("env"))
				{
					System.out.print(interpreter.getEnvironment());
				}
				else if (line.equals("files"))
				{
					for (File file: interpreter.getSourceFiles())
					{
						System.out.println(file.getAbsolutePath());
					}
				}
				else if (line.equals("coverage"))
				{
					for (File file: interpreter.getSourceFiles())
					{
						doCoverage(interpreter.getSourceFile(file));
					}
				}
				else if (line.equals("modules"))
				{
					for (String name: interpreter.getModules())
					{
						System.out.println(name);
					}
				}
				else if (line.equals("classes"))
				{
					for (String name: interpreter.getClasses())
					{
						System.out.println(name);
					}
				}
				else
				{
					long before = System.currentTimeMillis();
					String output = interpreter.execute(line);
		   			long after = System.currentTimeMillis();
					System.out.println(line + " = " + output);
		   			System.out.println("Executed in " + (double)(after-before)/1000 + " secs.");
				}
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
	}

	private void doCoverage(SourceFile source)
	{
		try
		{
			File dir = new File(COVERAGE);
			dir.mkdirs();
			File html = new File(COVERAGE + source.filename.getName() + ".doc");
			PrintWriter pw = new PrintWriter(html, "UTF-8");
			source.printWordCoverage(pw);
			pw.close();
			System.out.println("Word HTML coverage written to " + html);
		}
		catch (Exception e)
		{
			System.out.println("coverage: " + e.getMessage());
		}
	}
}
