// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.itf;

// import java collection types
import java.util.*;

public abstract interface IJavaIfStatement extends IJavaStatement
{
	public abstract IJavaExpression getExpression();
	public abstract IJavaStatement getThenStatement();
	public abstract List<IJavaElseIfStatement> getElselist();
	public abstract boolean hasElseStatement();
	public abstract IJavaStatement getElseStatement();
}