// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaCardinalityExpression extends JavaExpression implements IJavaCardinalityExpression
{
	// private member variable (name)
	private IJavaExpression m_name = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getName()
	{
		return m_name;
	}

	// public operation to set the embedded private field value
	public void setName(IJavaExpression p_name)
	{
		// consistency check (field must be non null!)
		assert(p_name != null);

		// instantiate the member variable
		m_name = p_name;

		// set the parent of the parameter passed
		p_name.setParent(this);
	}

	// default constructor
	public JavaCardinalityExpression()
	{
		super();
		m_name = null;
	}

	// auxiliary constructor
	public JavaCardinalityExpression(
		IJavaExpression p_name
	) {
		super();
		setName(p_name);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitCardinalityExpression(this); }

	// the identity function
	public String identify() { return "JavaCardinalityExpression"; }
}
