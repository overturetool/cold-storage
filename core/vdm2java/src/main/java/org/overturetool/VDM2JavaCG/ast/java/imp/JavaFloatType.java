// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaFloatType extends JavaType implements IJavaFloatType
{
	// default constructor
	public JavaFloatType()
	{
		super();
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitFloatType(this); }

	// the identity function
	public String identify() { return "JavaFloatType"; }
}