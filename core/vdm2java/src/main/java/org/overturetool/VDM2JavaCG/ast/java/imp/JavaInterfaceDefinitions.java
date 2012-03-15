// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

// import java collection types
import java.util.*;

public class JavaInterfaceDefinitions extends JavaSpecification implements IJavaInterfaceDefinitions
{
	// private member variable (interface_list)
	private List<IJavaInterfaceDefinition> m_interface_list = new Vector<IJavaInterfaceDefinition>();

	// public operation to retrieve the embedded private field value
	public List<IJavaInterfaceDefinition> getInterfaceList()
	{
		return m_interface_list;
	}

	// public operation to set the embedded private field value
	public void setInterfaceList(List<IJavaInterfaceDefinition> p_interface_list)
	{
		// consistency check (field must be non null!)
		assert(p_interface_list != null);

		// instantiate the member variable
		m_interface_list = p_interface_list;

		// set the parent of each element in the sequence parameter passed
		for (IJavaNode lnode: p_interface_list) lnode.setParent(this);
	}

	// default constructor
	public JavaInterfaceDefinitions()
	{
		super();
		m_interface_list = null;
	}

	// auxiliary constructor
	public JavaInterfaceDefinitions(
		List<IJavaInterfaceDefinition> p_interface_list
	) {
		super();
		setInterfaceList(p_interface_list);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitInterfaceDefinitions(this); }

	// the identity function
	public String identify() { return "JavaInterfaceDefinitions"; }
}