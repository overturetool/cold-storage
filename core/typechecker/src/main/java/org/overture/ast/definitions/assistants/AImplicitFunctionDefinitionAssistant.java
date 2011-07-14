package org.overture.ast.definitions.assistants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;

import org.overture.ast.definitions.AExplicitFunctionDefinition;
import org.overture.ast.definitions.AImplicitFunctionDefinition;
import org.overture.ast.definitions.ALocalDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.node.NodeList;
import org.overture.ast.node.NodeListList;
import org.overture.ast.patterns.PPattern;
import org.overture.ast.patterns.assistants.PPatternAssistant;
import org.overture.ast.types.AFunctionType;
import org.overture.ast.types.AParameterType;
import org.overture.ast.types.AUnknownType;
import org.overture.ast.types.PType;
import org.overture.ast.types.assistants.AFunctionTypeAssistant;
import org.overture.ast.types.assistants.PTypeAssistant;
import org.overture.runtime.TypeChecker;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.typechecker.NameScope;

public class AImplicitFunctionDefinitionAssistant {

	public static AFunctionType getType(AImplicitFunctionDefinition impdef, List<PType> actualTypes)
	{		
		Iterator<PType> ti = actualTypes.iterator();
		AFunctionType ftype = (AFunctionType)impdef.getType();

		for (LexNameToken pname: impdef.getTypeParams())
		{
			PType ptype = ti.next();
			//AFunctionTypeAssistent.
			ftype = (AFunctionType)PTypeAssistant.polymorph(ftype,pname, ptype);
		}

		return ftype;
	}

	public static Collection<? extends PDefinition> getTypeParamDefinitions(
			AImplicitFunctionDefinition node) {
		
		List<PDefinition> defs = new ArrayList<PDefinition>();

		for (LexNameToken pname: node.getTypeParams())
		{
			PDefinition p = new ALocalDefinition(
				pname.location, pname, NameScope.NAMES,false,null, null, new AParameterType(null,false,null,pname),false);

			PDefinitionAssistant.markUsed(p);
			defs.add(p);
		}

		return defs;
	}

	public static PDefinition findName(AImplicitFunctionDefinition d,
			LexNameToken sought, NameScope scope) {
		
		if (PDefinitionAssistant.findNameBaseCase(d, sought, scope) != null)
		{
			return d;
		}

		PDefinition predef = d.getPredef();
		if (predef != null && PDefinitionAssistant.findName(predef, sought, scope) != null)
		{
			return predef;
		}

		PDefinition postdef = d.getPostdef();
		if (postdef != null && PDefinitionAssistant.findName(postdef,sought, scope) != null)
		{
			return postdef;
		}

		return null;
	}

	public static List<PDefinition> getDefinitions(AImplicitFunctionDefinition d) {
		List<PDefinition> defs = new Vector<PDefinition>();
		defs.add(d);

		if (d.getPredef() != null)
		{
			defs.add(d.getPredef());
		}

		if (d.getPostdef() != null)
		{
			defs.add(d.getPostdef());
		}

		return defs;
	}

	public static LexNameList getVariableNames(AImplicitFunctionDefinition d) {
		return new LexNameList(d.getName());
	}
}