// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

// import java collection types
import java.util.*;

public class JavaIfExpression extends JavaExpression implements IJavaIfExpression
{
	// private member variable (if_expression)
	private IJavaExpression m_if_expression = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getIfExpression()
	{
		return m_if_expression;
	}

	// public operation to set the embedded private field value
	public void setIfExpression(IJavaExpression p_if_expression)
	{
		// consistency check (field must be non null!)
		assert(p_if_expression != null);

		// instantiate the member variable
		m_if_expression = p_if_expression;

		// set the parent of the parameter passed
		p_if_expression.setParent(this);
	}

	// private member variable (then_expression)
	private IJavaExpression m_then_expression = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getThenExpression()
	{
		return m_then_expression;
	}

	// public operation to set the embedded private field value
	public void setThenExpression(IJavaExpression p_then_expression)
	{
		// consistency check (field must be non null!)
		assert(p_then_expression != null);

		// instantiate the member variable
		m_then_expression = p_then_expression;

		// set the parent of the parameter passed
		p_then_expression.setParent(this);
	}

	// private member variable (elseif_expression_list)
	private List<IJavaElseIfExpression> m_elseif_expression_list = new Vector<IJavaElseIfExpression>();

	// public operation to retrieve the embedded private field value
	public List<IJavaElseIfExpression> getElseifExpressionList()
	{
		return m_elseif_expression_list;
	}

	// public operation to set the embedded private field value
	public void setElseifExpressionList(List<IJavaElseIfExpression> p_elseif_expression_list)
	{
		// consistency check (field must be non null!)
		assert(p_elseif_expression_list != null);

		// instantiate the member variable
		m_elseif_expression_list = p_elseif_expression_list;

		// set the parent of each element in the sequence parameter passed
		for (IJavaNode lnode: p_elseif_expression_list) lnode.setParent(this);
	}

	// private member variable (else_expression)
	private IJavaExpression m_else_expression = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getElseExpression()
	{
		return m_else_expression;
	}

	// public operation to set the embedded private field value
	public void setElseExpression(IJavaExpression p_else_expression)
	{
		// consistency check (field must be non null!)
		assert(p_else_expression != null);

		// instantiate the member variable
		m_else_expression = p_else_expression;

		// set the parent of the parameter passed
		p_else_expression.setParent(this);
	}

	// default constructor
	public JavaIfExpression()
	{
		super();
		m_if_expression = null;
		m_then_expression = null;
		m_elseif_expression_list = null;
		m_else_expression = null;
	}

	// auxiliary constructor
	public JavaIfExpression(
		IJavaExpression p_if_expression,
		IJavaExpression p_then_expression,
		List<IJavaElseIfExpression> p_elseif_expression_list,
		IJavaExpression p_else_expression
	) {
		super();
		setIfExpression(p_if_expression);
		setThenExpression(p_then_expression);
		setElseifExpressionList(p_elseif_expression_list);
		setElseExpression(p_else_expression);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitIfExpression(this); }

	// the identity function
	public String identify() { return "JavaIfExpression"; }
}
