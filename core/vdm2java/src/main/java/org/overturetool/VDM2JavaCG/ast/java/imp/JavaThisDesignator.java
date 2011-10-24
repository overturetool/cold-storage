// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaThisDesignator extends JavaObjectDesignator implements IJavaThisDesignator
{
	// private member variable (This)
	private IJavaThisExpression m_This = null;

	// public operation to retrieve the embedded private field value
	public IJavaThisExpression getThis()
	{
		return m_This;
	}

	// public operation to set the embedded private field value
	public void setThis(IJavaThisExpression p_This)
	{
		// consistency check (field must be non null!)
		assert(p_This != null);

		// instantiate the member variable
		m_This = p_This;

		// set the parent of the parameter passed
		p_This.setParent(this);
	}

	// default constructor
	public JavaThisDesignator()
	{
		super();
		m_This = null;
	}

	// auxiliary constructor
	public JavaThisDesignator(
		IJavaThisExpression p_This
	) {
		super();
		setThis(p_This);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitThisDesignator(this); }

	// the identity function
	public String identify() { return "JavaThisDesignator"; }
}