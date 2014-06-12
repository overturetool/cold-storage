package org.overture.modelcheckers.probsolver;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.overture.ast.lex.Dialect;

@RunWith(value = Parameterized.class)
public class QuotesTests extends AllTest
{
	public QuotesTests(Dialect dialect, File source, String operationName,
			String name)
	{
		super(dialect, source, operationName, name);
	}

	@Parameters(name = "{3}")
	public static Collection<Object[]> getData()
	{
		String root = "src/test/resources/modules/quotes/";

		Collection<Object[]> tests = new LinkedList<Object[]>();

		tests.addAll(getTests(new File(root)));

		return tests;
	}
	
	@Override
	protected String getPropertyId()
	{
		return "quotes";
	}
}