// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaEmptyStatement extends JavaStatement implements IJavaEmptyStatement
{
	// default constructor
	public JavaEmptyStatement()
	{
		super();
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitEmptyStatement(this); }

	// the identity function
	public String identify() { return "JavaEmptyStatement"; }
}
