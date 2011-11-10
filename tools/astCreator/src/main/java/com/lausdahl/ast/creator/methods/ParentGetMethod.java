package com.lausdahl.ast.creator.methods;

import com.lausdahl.ast.creator.Environment;
import com.lausdahl.ast.creator.definitions.Field;

public class ParentGetMethod extends Method
{
	Field f;
	String structureClassName;
	public ParentGetMethod(String structureClassName, Field f,Environment env)
	{
		super(null,env);
		this.f = f;
		this.structureClassName = structureClassName;
	}
	
	@Override
	protected void prepare()
	{
		name = "parent";
		returnType = structureClassName;
		body = "\t\treturn " + f.getName() + ";";
	}

}