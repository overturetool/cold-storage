// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaStatement extends JavaNode implements IJavaStatement
{
	// default constructor
	public JavaStatement()
	{
		super();
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitStatement(this); }

	// the identity function
	public String identify() { return "JavaStatement"; }
}
