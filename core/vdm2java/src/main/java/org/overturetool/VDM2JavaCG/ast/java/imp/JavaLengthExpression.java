// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaLengthExpression extends JavaExpression implements IJavaLengthExpression
{
	// private member variable (SeqName)
	private IJavaExpression m_SeqName = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getSeqName()
	{
		return m_SeqName;
	}

	// public operation to set the embedded private field value
	public void setSeqName(IJavaExpression p_SeqName)
	{
		// consistency check (field must be non null!)
		assert(p_SeqName != null);

		// instantiate the member variable
		m_SeqName = p_SeqName;

		// set the parent of the parameter passed
		p_SeqName.setParent(this);
	}

	// default constructor
	public JavaLengthExpression()
	{
		super();
		m_SeqName = null;
	}

	// auxiliary constructor
	public JavaLengthExpression(
		IJavaExpression p_SeqName
	) {
		super();
		setSeqName(p_SeqName);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitLengthExpression(this); }

	// the identity function
	public String identify() { return "JavaLengthExpression"; }
}
