// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaMapApplication extends JavaMapExpression implements IJavaMapApplication
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

	// private member variable (key)
	private IJavaExpression m_key = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getKey()
	{
		return m_key;
	}

	// public operation to set the embedded private field value
	public void setKey(IJavaExpression p_key)
	{
		// consistency check (field must be non null!)
		assert(p_key != null);

		// instantiate the member variable
		m_key = p_key;

		// set the parent of the parameter passed
		p_key.setParent(this);
	}

	// default constructor
	public JavaMapApplication()
	{
		super();
		m_name = null;
		m_key = null;
	}

	// auxiliary constructor
	public JavaMapApplication(
		IJavaExpression p_name,
		IJavaExpression p_key
	) {
		super();
		setName(p_name);
		setKey(p_key);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitMapApplication(this); }

	// the identity function
	public String identify() { return "JavaMapApplication"; }
}
