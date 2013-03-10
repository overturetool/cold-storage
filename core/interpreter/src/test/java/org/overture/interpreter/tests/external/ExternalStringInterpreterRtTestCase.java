package org.overture.interpreter.tests.external;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.overture.interpreter.tests.InterpreterStringRtTestCase;

public class ExternalStringInterpreterRtTestCase extends InterpreterStringRtTestCase
{
	public ExternalStringInterpreterRtTestCase()
	{
		super();

	}

	public ExternalStringInterpreterRtTestCase(File file)
	{
		super(file);
	}

	public ExternalStringInterpreterRtTestCase(File rootSource, String name, String content)
	{
		super(rootSource, name, content);
	}
	
	public ExternalStringInterpreterRtTestCase(File file, String suiteName, File testSuiteRoot)
	{
		super(file,suiteName,testSuiteRoot);
	}

	@Override
	protected String baseExamplePath()
	{
		return file.getParentFile().getAbsolutePath();
	}
	
	protected String search(File file, String name) throws IOException
	{
		File readme = new File(file,(name.contains(".")?name.substring(0,name.lastIndexOf('.'))+".assert": name+".assert"));
		if (readme.exists())
		{
			BufferedReader reader = new BufferedReader(new FileReader(readme));
			String text = null;
			String entry ="";
			while ((text = reader.readLine()) != null)
			{
				entry += text;
			}
			entry = entry.replace('\n', ' ');
			reader.close();
			return entry;
		}
		return null;
	}

	

}