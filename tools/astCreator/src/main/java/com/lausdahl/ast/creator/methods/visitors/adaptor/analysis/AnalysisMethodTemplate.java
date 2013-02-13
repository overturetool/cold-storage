package com.lausdahl.ast.creator.methods.visitors.adaptor.analysis;

import com.lausdahl.ast.creator.env.Environment;
import com.lausdahl.ast.creator.definitions.IClassDefinition;
import com.lausdahl.ast.creator.definitions.IInterfaceDefinition;
import com.lausdahl.ast.creator.methods.Method;
import com.lausdahl.ast.creator.methods.visitors.AnalysisUtil;

public abstract class AnalysisMethodTemplate extends Method
{
	protected boolean addReturnToBody = false;
	protected IInterfaceDefinition intf;
	protected String defaultPostFix = "";

	public AnalysisMethodTemplate(IClassDefinition c)
	{
		super(c);
		intf = c;
	}

	@Override
	public void setClassDefinition(IClassDefinition c)
	{
		super.setClassDefinition(c);
		intf = c;
	}
	
	@Override
	protected void prepare(Environment env)
	{
		super.prepare(env);
		throwsDefinitions.add(env.analysisException);
	}

	protected String getAdditionalBodyCallArguments()
	{
		String tmp = "";
		for (Argument a : arguments)
		{
			tmp += a.name + ", ";
		}
		if (!arguments.isEmpty())
		{
			tmp = tmp.substring(0, tmp.length() - 2);
		}
		return tmp;
	}

	protected void setupArguments(Environment env)
	{
		if (classDefinition == null)
		{
			this.arguments.add(new Argument(intf.getName().getName(), "node"));
		} else
		{
			this.arguments.add(new Argument(AnalysisUtil.getCaseClass(env, classDefinition).getName().getName(), "node"));

		}
	}

	
	public void setDefaultPostfix(String nm)
	{
		this.defaultPostFix = nm;
	}
}