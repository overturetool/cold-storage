// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaRecordModification extends JavaNode implements IJavaRecordModification
{
	// private member variable (fieldname)
	private IJavaIdentifier m_fieldname = null;

	// public operation to retrieve the embedded private field value
	public IJavaIdentifier getFieldname()
	{
		return m_fieldname;
	}

	// public operation to set the embedded private field value
	public void setFieldname(IJavaIdentifier p_fieldname)
	{
		// consistency check (field must be non null!)
		assert(p_fieldname != null);

		// instantiate the member variable
		m_fieldname = p_fieldname;

		// set the parent of the parameter passed
		p_fieldname.setParent(this);
	}

	// private member variable (value)
	private IJavaExpression m_value = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getValue()
	{
		return m_value;
	}

	// public operation to set the embedded private field value
	public void setValue(IJavaExpression p_value)
	{
		// consistency check (field must be non null!)
		assert(p_value != null);

		// instantiate the member variable
		m_value = p_value;

		// set the parent of the parameter passed
		p_value.setParent(this);
	}

	// default constructor
	public JavaRecordModification()
	{
		super();
		m_fieldname = null;
		m_value = null;
	}

	// auxiliary constructor
	public JavaRecordModification(
		IJavaIdentifier p_fieldname,
		IJavaExpression p_value
	) {
		super();
		setFieldname(p_fieldname);
		setValue(p_value);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitRecordModification(this); }

	// the identity function
	public String identify() { return "JavaRecordModification"; }
}
