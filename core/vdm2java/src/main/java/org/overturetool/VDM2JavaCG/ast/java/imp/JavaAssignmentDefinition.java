// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaAssignmentDefinition extends JavaDefinition implements IJavaAssignmentDefinition
{
	// private member variable (identifier)
	private IJavaIdentifier m_identifier = null;

	// public operation to retrieve the embedded private field value
	public IJavaIdentifier getIdentifier()
	{
		return m_identifier;
	}

	// public operation to set the embedded private field value
	public void setIdentifier(IJavaIdentifier p_identifier)
	{
		// consistency check (field must be non null!)
		assert(p_identifier != null);

		// instantiate the member variable
		m_identifier = p_identifier;

		// set the parent of the parameter passed
		p_identifier.setParent(this);
	}

	// private member variable (type)
	private IJavaType m_type = null;

	// public operation to retrieve the embedded private field value
	public IJavaType getType()
	{
		return m_type;
	}

	// public operation to set the embedded private field value
	public void setType(IJavaType p_type)
	{
		// consistency check (field must be non null!)
		assert(p_type != null);

		// instantiate the member variable
		m_type = p_type;

		// set the parent of the parameter passed
		p_type.setParent(this);
	}

	// private member variable (expression)
	private IJavaExpression m_expression = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getExpression()
	{
		return m_expression;
	}

	// public operation to set the embedded private field value
	public void setExpression(IJavaExpression p_expression)
	{
		// consistency check (field must be non null!)
		assert(p_expression != null);

		// instantiate the member variable
		m_expression = p_expression;

		// set the parent of the parameter passed
		p_expression.setParent(this);
	}

	// default constructor
	public JavaAssignmentDefinition()
	{
		super();
		m_identifier = null;
		m_type = null;
		m_expression = null;
	}

	// auxiliary constructor
	public JavaAssignmentDefinition(
		IJavaIdentifier p_identifier,
		IJavaType p_type,
		IJavaExpression p_expression
	) {
		super();
		setIdentifier(p_identifier);
		setType(p_type);
		setExpression(p_expression);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitAssignmentDefinition(this); }

	// the identity function
	public String identify() { return "JavaAssignmentDefinition"; }
}