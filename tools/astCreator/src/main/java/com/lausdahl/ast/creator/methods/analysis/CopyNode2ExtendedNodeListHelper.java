package com.lausdahl.ast.creator.methods.analysis;

import java.util.HashSet;
import java.util.Set;

import com.lausdahl.ast.creator.Environment;
import com.lausdahl.ast.creator.methods.Method;

public class CopyNode2ExtendedNodeListHelper extends Method
{
	Environment env;
	Environment envDest;

	public CopyNode2ExtendedNodeListHelper()
	{
		super(null, null);
	}

	public CopyNode2ExtendedNodeListHelper(Environment env, Environment envDest)
	{
		super(null, env);
		this.env = env;
		this.envDest = envDest;
	}

	@Override
	protected void prepare()
	{
		this.name = "copyList";
		this.arguments.add(new Argument(Environment.listDef.getImportName()
				+ "<? extends "
				+ env.node.getImportName() + ">", "list"));
//		this.annotation = "@SuppressWarnings({ \"rawtypes\"/*, \"unchecked\" */})";
		this.returnType = envDest.nodeList.getImportName();
		StringBuilder bodySb = new StringBuilder();

		bodySb.append("\t\t" + this.returnType + " newList = new "
				+ this.returnType + "<" + envDest.node.getPackageName() + "."
				+ envDest.node.getName() + ">(null);\n");
		bodySb.append("\t\t" + "for( " + env.node.getPackageName() + "."
				+ env.node.getName() + " n : list)\n");
		bodySb.append("\t\t" + "{\n");
		bodySb.append("\t\t" + "\tnewList.add(checkCache(n,n.apply(this)));\n");
		bodySb.append("\t\t" + "}\n");
		bodySb.append("\t\t" + "return newList;");
		this.body = bodySb.toString();

	}

	@Override
	public Set<String> getRequiredImports()
	{
		Set<String> imports = new HashSet<String>();
		imports.addAll(super.getRequiredImports());
		imports.add(Environment.listDef.getImportName());
		return imports;
	}

	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// public static NodeListInterpreter copyList(NodeList<? extends Node> list)
	// {
	// NodeListInterpreter newList = new NodeListInterpreter<NodeInterpreter>(null);
	// for (Node n : list)
	// {
	// newList.add(n);
	// }
	// return newList;
	// }
}