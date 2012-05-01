package org.overture.parser.tests;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import junit.framework.Test;

import org.overture.parser.tests.framework.BaseTestSuite;

public class SpecificationSlTestSuite extends BaseTestSuite
{

	public static Test suite() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException
	{
		String name = "Parser Specification SL TestSuite";
		String root = "src\\test\\resources\\specifications\\sl";
		return createTestCompleteFile(name,root,SpecificatopnSlTestCase.class);
	}
}