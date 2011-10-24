// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaIndexForLoop extends JavaForLoop implements IJavaIndexForLoop
{
	// private member variable (VarName)
	private String m_VarName = new String();

	// public operation to retrieve the embedded private field value
	public String getVarName()
	{
		return m_VarName;
	}

	// public operation to set the embedded private field value
	public void setVarName(String p_VarName)
	{
		// consistency check (field must be non null!)
		assert(p_VarName != null);

		// instantiate the member variable
		m_VarName = p_VarName;
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

	// private member variable (Step)
	private IJavaExpression m_Step = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getStep()
	{
		return m_Step;
	}

	// public operation to set the embedded private field value
	public void setStep(IJavaExpression p_Step)
	{
		// consistency check (field must be non null!)
		assert(p_Step != null);

		// instantiate the member variable
		m_Step = p_Step;

		// set the parent of the parameter passed
		p_Step.setParent(this);
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
	public JavaIndexForLoop()
	{
		super();
		m_VarName = null;
		m_From = null;
		m_To = null;
		m_Step = null;
		m_statement = null;
	}

	// auxiliary constructor
	public JavaIndexForLoop(
		String p_VarName,
		IJavaExpression p_From,
		IJavaExpression p_To,
		IJavaExpression p_Step,
		IJavaStatement p_statement
	) {
		super();
		setVarName(p_VarName);
		setFrom(p_From);
		setTo(p_To);
		setStep(p_Step);
		setStatement(p_statement);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitIndexForLoop(this); }

	// the identity function
	public String identify() { return "JavaIndexForLoop"; }
}