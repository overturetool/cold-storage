// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaTextLiteral extends JavaLiteral implements IJavaTextLiteral
{
	// private member variable (val)
	private String m_val = new String();

	// public operation to retrieve the embedded private field value
	public String getVal()
	{
		return m_val;
	}

	// public operation to set the embedded private field value
	public void setVal(String p_val)
	{
		// consistency check (field must be non null!)
		assert(p_val != null);

		// instantiate the member variable
		m_val = p_val;
	}

	// default constructor
	public JavaTextLiteral()
	{
		super();
		m_val = null;
	}

	// auxiliary constructor
	public JavaTextLiteral(
		String p_val
	) {
		super();
		setVal(p_val);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitTextLiteral(this); }

	// the identity function
	public String identify() { return "JavaTextLiteral"; }
}