// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

// import java collection types
import java.util.*;

public class JavaSetEnumExpression extends JavaSetExpression implements IJavaSetEnumExpression
{
	// private member variable (args)
	private List<IJavaExpression> m_args = new Vector<IJavaExpression>();

	// public operation to retrieve the embedded private field value
	public List<IJavaExpression> getArgs()
	{
		return m_args;
	}

	// public operation to set the embedded private field value
	public void setArgs(List<IJavaExpression> p_args)
	{
		// consistency check (field must be non null!)
		assert(p_args != null);

		// instantiate the member variable
		m_args = p_args;

		// set the parent of each element in the sequence parameter passed
		for (IJavaNode lnode: p_args) lnode.setParent(this);
	}

	// default constructor
	public JavaSetEnumExpression()
	{
		super();
		m_args = null;
	}

	// auxiliary constructor
	public JavaSetEnumExpression(
		List<IJavaExpression> p_args
	) {
		super();
		setArgs(p_args);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitSetEnumExpression(this); }

	// the identity function
	public String identify() { return "JavaSetEnumExpression"; }
}
