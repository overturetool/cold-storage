package org.overture.vdmj.typechecker.tests.external;

import java.io.File;

import org.overture.vdmj.typechecker.tests.OvertureTestHelper;
import org.overturetool.test.framework.TestResourcesResultTestCase;
import org.overturetool.vdmj.Release;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.typechecker.TypeChecker;

public class NewClassTestCase extends TestResourcesResultTestCase
{
	public NewClassTestCase()
	{
		super();
	}

	public NewClassTestCase(File file)
	{
		super(file);
	}

	public NewClassTestCase(String name, String content)
	{
		super(name, content);
	}

	public NewClassTestCase(File file, String suiteName, File testSuiteRoot)
	{
		super(file, suiteName, testSuiteRoot);
	}

	@Override
	public void test() throws Exception
	{
		assertNotNull("File not set", file);
		compareResults(new OvertureTestHelper().typeCheckPp(file), "typechecker.result");

	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Settings.dialect = Dialect.VDM_PP;
		Settings.release = Release.VDM_10;
		TypeChecker.clearErrors();
	}

}