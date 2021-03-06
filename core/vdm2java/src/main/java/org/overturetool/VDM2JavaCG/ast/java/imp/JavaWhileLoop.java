// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaWhileLoop extends JavaStatement implements IJavaWhileLoop
{
	// private member variable (Condition)
	private IJavaExpression m_Condition = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getCondition()
	{
		return m_Condition;
	}

	// public operation to set the embedded private field value
	public void setCondition(IJavaExpression p_Condition)
	{
		// consistency check (field must be non null!)
		assert(p_Condition != null);

		// instantiate the member variable
		m_Condition = p_Condition;

		// set the parent of the parameter passed
		p_Condition.setParent(this);
	}

	// private member variable (statement)
	private IJavaStatement m_statement = null;

	// public operation to retrieve the embedded private field value
	public IJavaStatement getStatement()
	{
		return m_statement;
	}

	// public operation to set the embedded private field value
	public void setStatement(IJavaStatement p_statement)
	{
		// consistency check (field must be non null!)
		assert(p_statement != null);

		// instantiate the member variable
		m_statement = p_statement;

		// set the parent of the parameter passed
		p_statement.setParent(this);
	}

	// default constructor
	public JavaWhileLoop()
	{
		super();
		m_Condition = null;
		m_statement = null;
	}

	// auxiliary constructor
	public JavaWhileLoop(
		IJavaExpression p_Condition,
		IJavaStatement p_statement
	) {
		super();
		setCondition(p_Condition);
		setStatement(p_statement);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitWhileLoop(this); }

	// the identity function
	public String identify() { return "JavaWhileLoop"; }
}
