// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaSpecification extends JavaNode implements IJavaSpecification
{
	// default constructor
	public JavaSpecification()
	{
		super();
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitSpecification(this); }

	// the identity function
	public String identify() { return "JavaSpecification"; }
}
