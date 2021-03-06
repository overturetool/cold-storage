/*******************************************************************************
 * Copyright (c) 2009, 2011 Overture Team and others.
 *
 * Overture is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Overture is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Overture.  If not, see <http://www.gnu.org/licenses/>.
 * 	
 * The Overture Tool web-site: http://overturetool.org/
 *******************************************************************************/
package org.overturetool.tools.packworkspace.testing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessConsolePrinter extends Thread
{
	File file = null;
	InputStream stream = null;
	String headerMessage = null;

	public ProcessConsolePrinter(File file, InputStream inputStream)
	{
		this.file = file;
		this.stream = inputStream;
	}

	public ProcessConsolePrinter(File file, InputStream inputStream,
			String headerMessage)
	{
		this.file = file;
		this.stream = inputStream;
		this.headerMessage = headerMessage;
	}

	@Override
	public void run()
	{

		String line = null;
		
		FileWriter outputFileReader;
		BufferedWriter outputStream = null;
		try
		{
			outputFileReader = new FileWriter(file, false);
			outputStream = new BufferedWriter(outputFileReader);
			if (headerMessage != null)
			{
				outputStream.write("\n" + headerMessage + "\n");
				outputStream.flush();
			}
			
			BufferedReader input = new BufferedReader(new InputStreamReader(stream));
			while ((line = input.readLine()) != null)
			{
				outputStream.write("\n" + line);
				outputStream.flush();
			}

		} catch (IOException e)
		{

		} finally
		{
			try
			{
				outputStream.close();
			} catch (Exception e)
			{

			}
		}
	}
}