package com.lausdahl.ast.creator.methods.analysis.depthfirst;

import com.lausdahl.ast.creator.Environment;
import com.lausdahl.ast.creator.definitions.Field;
import com.lausdahl.ast.creator.definitions.IClassDefinition;
import com.lausdahl.ast.creator.definitions.InterfaceDefinition;
import com.lausdahl.ast.creator.methods.GetMethod;
import com.lausdahl.ast.creator.methods.Method;

public class DepthFirstCaseMethod extends Method
{
	public DepthFirstCaseMethod()
	{
		super(null, null);
	}

	public DepthFirstCaseMethod(IClassDefinition c, Environment env)
	{
		super(c, env);
	}

	@Override
	protected void prepare()
	{
		IClassDefinition c = classDefinition;
		StringBuilder sb = new StringBuilder();
		sb.append("\t/**\n");
		sb.append("\t* Called by the {@link "
				+ c.getName()
				+ "} node from {@link "
				+ c.getName()
				+ "#apply("
				+ (c.getInterfaces().isEmpty() ? c.getSignatureName()
						: c.getInterfaces().iterator().next().getSignatureName())
				+ ")}.\n");
		sb.append("\t* @param node the calling {@link " + c.getName()
				+ "} node\n");
		sb.append("\t*/");
		this.javaDoc = sb.toString();
		this.name = "case" + InterfaceDefinition.javaClassName(c.getName());
		this.arguments.add(new Argument(classDefinition.getName(), "node"));
		this.requiredImports.add("java.util.ArrayList");
		this.requiredImports.add("java.util.List");

		StringBuffer bodySb = new StringBuffer();
		for (Field f : c.getFields())
		{
			if (f.isTokenField)
			{
				continue;
			}
			Method getMethod = new GetMethod(c, f, env);
			getMethod.getJavaSourceCode();
			String getMethodName = getMethod.name;
			String getter = "node." + getMethodName + "()";

			if (!f.isList)
			{
				bodySb.append("\t\tif(" + getter + " != null) {\n");
				bodySb.append("\t\t\t" + getter + ".apply(this);\n");
				bodySb.append("\t\t}\n");
			} else if(f.isList && !f.isDoubleList)
			{
				bodySb.append("\t\t{\n");
				bodySb.append("\t\t\tList<" + f.getInnerTypeForList()
						+ "> copy = new ArrayList<" + f.getInnerTypeForList()
						+ ">(" + getter + ");\n");
				bodySb.append("\t\t\tfor( " + f.getInnerTypeForList()
						+ " e : copy) {\n");
				bodySb.append("\t\t\t\te.apply(this);\n");
				bodySb.append("\t\t\t}\n");

				bodySb.append("\t\t}\n");
			}else if(f.isDoubleList)
			{
				bodySb.append("\t\t{\n");
				bodySb.append("\t\t\tList<List<" + f.getInnerTypeForList()
						+ ">> copy = new ArrayList<List<" + f.getInnerTypeForList()
						+ ">>(" + getter + ");\n");
				bodySb.append("\t\t\tfor( List<" + f.getInnerTypeForList()
						+ "> list : copy) {\n");

				bodySb.append("\t\t\t\tfor( " + f.getInnerTypeForList()
						+ " e : list) {\n");
				bodySb.append("\t\t\t\t\te.apply(this);\n");
				bodySb.append("\t\t\t\t}\n");
				
				bodySb.append("\t\t\t}\n");

				bodySb.append("\t\t}\n");
//				List<List<PExp>> copy = new ArrayList<List<PExp>>(node.getFf());
//				for (List<PExp> list : copy)
//				{
//					for( PExp e : list) {
//						e.apply(this);
//					}
//				}
			}
		}

		this.body = bodySb.toString();
		// this.annotation="@override";
		// if (cd.getSuperDef() != null
		// && !(cd instanceof ExternalJavaClassDefinition))
		// {
		// this.body = "\t\t"
		// + (addReturnToBody ? "return " : "")
		// + "default"
		// + InterfaceDefinition.javaClassName(c.getSuperDef().getName()
		// + "(" + getAdditionalBodyCallArguments() + ");");
		// } else
		// {
		// this.body = "" + (addReturnToBody ? "\t\treturn null;" : "");
		// }
	}
}