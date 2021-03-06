package org.overturetool.umltrans;

import org.overturetool.umltrans.basic.ToUmlTestHelper;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ToUmlTest extends TestCase
{
	//instancevariables
	public void testbasicDefaultInstanceVariablesClass() throws Exception
	{
		Assert.assertEquals(true,
				new ToUmlTestHelper("instancevariables","basicDefaultInstanceVariablesClass").run());
	}
	
	public void testbasicInstanceVariablesClass() throws Exception
	{
		Assert.assertEquals(true,
				new ToUmlTestHelper("instancevariables","basicInstanceVariablesClass").run());
	}
	
	public void testobjectDefaultRefInstanceVariablesClass() throws Exception
	{
		Assert.assertEquals(true,
				new ToUmlTestHelper("instancevariables","objectDefaultRefInstanceVariablesClass").run());
	}
	public void testobjectRefInstanceVariablesClass() throws Exception
	{
		Assert.assertEquals(true,
				new ToUmlTestHelper("instancevariables","objectRefInstanceVariablesClass").run());
	}
	
	public void testvisibilityBasicInstanceVariablesClass() throws Exception
	{
		Assert.assertEquals(true,
				new ToUmlTestHelper("instancevariables","visibilityBasicInstanceVariablesClass").run());
	}
	
	// types
	public void testexpert() throws Exception
	{
		Assert.assertEquals(true,
				new ToUmlTestHelper("types","expert").run());
	}
//This one only works sometines//	public void teststring() throws Exception
//	{
//		Assert.assertEquals(true,
//				new ToUmlTestHelper("types","string").run());
//	}

	//fndefs
//	public void testExtExplFnClass() throws Exception
//	{
//		Assert.assertEquals(true,
//				new ToUmlTestHelper("fndefs","ExtExplFn").run());
//	}

//	public void testImplFnClass() throws Exception
//	{
//		Assert.assertEquals(true,
//				new ToUmlTestHelper("fndefs","ImplFn").run());
//	}

	//opdefs
//	public void testExtExplOpClass() throws Exception
//	{
//		Assert.assertEquals(true,
//				new ToUmlTestHelper("opdefs","ExtExplOp").run());
//	}

//	public void testImplOpClass() throws Exception
//	{
//		Assert.assertEquals(true,
//				new ToUmlTestHelper("opdefs","ImplOp").run());
//	}

	//subclassresponsibility
//	public void testExplOpSubClassRespClass() throws Exception
//	{
//		Assert.assertEquals(true,
//				new ToUmlTestHelper("subclassresponsibility","ExplOpSubClassResp").run());
//	}
//
//	public void testExtExplOpSubClassRespClass() throws Exception
//	{
//		Assert.assertEquals(true,
//				new ToUmlTestHelper("subclassresponsibility","ExtExplOpSubClassResp").run());
//	}
	
	// other
	public void testsimpleClass() throws Exception
	{
		Assert.assertEquals(true,
				new ToUmlTestHelper("other","simpleClass").run());
	}
}
