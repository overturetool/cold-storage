// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaSubVectorExpression extends JavaExpression implements IJavaSubVectorExpression
{
	// private member variable (VecName)
	private IJavaExpression m_VecName = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getVecName()
	{
		return m_VecName;
	}

	// public operation to set the embedded private field value
	public void setVecName(IJavaExpression p_VecName)
	{
		// consistency check (field must be non null!)
		assert(p_VecName != null);

		// instantiate the member variable
		m_VecName = p_VecName;

		// set the parent of the parameter passed
		p_VecName.setParent(this);
	}

	// private member variable (From)
	private IJavaExpression m_From = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getFrom()
	{
		return m_From;
	}

	// public operation to set the embedded private field value
	public void setFrom(IJavaExpression p_From)
	{
		// consistency check (field must be non null!)
		assert(p_From != null);

		// instantiate the member variable
		m_From = p_From;

		// set the parent of the parameter passed
		p_From.setParent(this);
	}

	// private member variable (To)
	private IJavaExpression m_To = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getTo()
	{
		return m_To;
	}

	// public operation to set the embedded private field value
	public void setTo(IJavaExpression p_To)
	{
		// consistency check (field must be non null!)
		assert(p_To != null);

		// instantiate the member variable
		m_To = p_To;

		// set the parent of the parameter passed
		p_To.setParent(this);
	}

	// default constructor
	public JavaSubVectorExpression()
	{
		super();
		m_VecName = null;
		m_From = null;
		m_To = null;
	}

	// auxiliary constructor
	public JavaSubVectorExpression(
		IJavaExpression p_VecName,
		IJavaExpression p_From,
		IJavaExpression p_To
	) {
		super();
		setVecName(p_VecName);
		setFrom(p_From);
		setTo(p_To);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitSubVectorExpression(this); }

	// the identity function
	public String identify() { return "JavaSubVectorExpression"; }
}