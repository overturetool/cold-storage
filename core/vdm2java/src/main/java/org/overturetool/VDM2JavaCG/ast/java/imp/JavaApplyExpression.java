// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

// import java collection types
import java.util.*;

public class JavaApplyExpression extends JavaExpression implements IJavaApplyExpression
{
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

	// private member variable (expression_list)
	private List<IJavaExpression> m_expression_list = new Vector<IJavaExpression>();

	// public operation to retrieve the embedded private field value
	public List<IJavaExpression> getExpressionList()
	{
		return m_expression_list;
	}

	// public operation to set the embedded private field value
	public void setExpressionList(List<IJavaExpression> p_expression_list)
	{
		// consistency check (field must be non null!)
		assert(p_expression_list != null);

		// instantiate the member variable
		m_expression_list = p_expression_list;

		// set the parent of each element in the sequence parameter passed
		for (IJavaNode lnode: p_expression_list) lnode.setParent(this);
	}

	// default constructor
	public JavaApplyExpression()
	{
		super();
		m_expression = null;
		m_expression_list = null;
	}

	// auxiliary constructor
	public JavaApplyExpression(
		IJavaExpression p_expression,
		List<IJavaExpression> p_expression_list
	) {
		super();
		setExpression(p_expression);
		setExpressionList(p_expression_list);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitApplyExpression(this); }

	// the identity function
	public String identify() { return "JavaApplyExpression"; }
}