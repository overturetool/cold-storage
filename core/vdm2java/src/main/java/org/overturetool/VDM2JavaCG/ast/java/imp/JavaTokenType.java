// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaTokenType extends JavaType implements IJavaTokenType
{
	// default constructor
	public JavaTokenType()
	{
		super();
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitTokenType(this); }

	// the identity function
	public String identify() { return "JavaTokenType"; }
}
