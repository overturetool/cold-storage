// this file is automatically generated by treegen. do not modify!

package org.overture.tools.treegen.ast.imp;

// import the abstract tree interfaces
import org.overture.tools.treegen.ast.itf.*;

public class TreeGenAstSetType extends TreeGenAstTypeSpecification implements ITreeGenAstSetType
{
	// default version identifier for serialize
	public static final long serialVersionUID = 1L;

	// private member variable (type)
	private ITreeGenAstTypeSpecification m_type = null;

	// public operation to retrieve the embedded private field value
	public ITreeGenAstTypeSpecification getType()
	{
		return m_type;
	}

	// public operation to set the embedded private field value
	public void setType(ITreeGenAstTypeSpecification p_type)
	{
		// consistency check (field must be non null!)
		assert(p_type != null);

		// instantiate the member variable
		m_type = p_type;

		// set the parent of the parameter passed
		p_type.setParent(this);
	}

	// default constructor
	public TreeGenAstSetType() { super(); }

	// auxiliary constructor
	public TreeGenAstSetType(
		ITreeGenAstTypeSpecification p_type
	) {
		super();
		setType(p_type);
	}

	// visitor support
	public void accept(ITreeGenAstVisitor pVisitor) { pVisitor.visitSetType(this); }

	// the identity function
	public String identify() { return "TreeGenAstSetType"; }

	// the toString function
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("new "+identify()+"(");
		buf.append(convertToString(getType()));
		buf.append(")");
		return buf.toString();
	}
}
