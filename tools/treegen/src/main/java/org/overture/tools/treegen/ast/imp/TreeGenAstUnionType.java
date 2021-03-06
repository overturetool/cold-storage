// this file is automatically generated by treegen. do not modify!

package org.overture.tools.treegen.ast.imp;

// import the abstract tree interfaces
import org.overture.tools.treegen.ast.itf.*;

public class TreeGenAstUnionType extends TreeGenAstTypeSpecification implements ITreeGenAstUnionType
{
	// default version identifier for serialize
	public static final long serialVersionUID = 1L;

	// private member variable (lhs)
	private ITreeGenAstTypeSpecification m_lhs = null;

	// public operation to retrieve the embedded private field value
	public ITreeGenAstTypeSpecification getLhs()
	{
		return m_lhs;
	}

	// public operation to set the embedded private field value
	public void setLhs(ITreeGenAstTypeSpecification p_lhs)
	{
		// consistency check (field must be non null!)
		assert(p_lhs != null);

		// instantiate the member variable
		m_lhs = p_lhs;

		// set the parent of the parameter passed
		p_lhs.setParent(this);
	}

	// private member variable (rhs)
	private ITreeGenAstTypeSpecification m_rhs = null;

	// public operation to retrieve the embedded private field value
	public ITreeGenAstTypeSpecification getRhs()
	{
		return m_rhs;
	}

	// public operation to set the embedded private field value
	public void setRhs(ITreeGenAstTypeSpecification p_rhs)
	{
		// consistency check (field must be non null!)
		assert(p_rhs != null);

		// instantiate the member variable
		m_rhs = p_rhs;

		// set the parent of the parameter passed
		p_rhs.setParent(this);
	}

	// default constructor
	public TreeGenAstUnionType() { super(); }

	// auxiliary constructor
	public TreeGenAstUnionType(
		ITreeGenAstTypeSpecification p_lhs,
		ITreeGenAstTypeSpecification p_rhs
	) {
		super();
		setLhs(p_lhs);
		setRhs(p_rhs);
	}

	// visitor support
	public void accept(ITreeGenAstVisitor pVisitor) { pVisitor.visitUnionType(this); }

	// the identity function
	public String identify() { return "TreeGenAstUnionType"; }

	// the toString function
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("new "+identify()+"(");
		buf.append(convertToString(getLhs()));
		buf.append(",");
		buf.append(convertToString(getRhs()));
		buf.append(")");
		return buf.toString();
	}
}
