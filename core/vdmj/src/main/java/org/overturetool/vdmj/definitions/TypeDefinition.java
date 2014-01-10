/*******************************************************************************
 *
 *	Copyright (c) 2008 Fujitsu Services Ltd.
 *
 *	Author: Nick Battle
 *
 *	This file is part of VDMJ.
 *
 *	VDMJ is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	VDMJ is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with VDMJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package org.overturetool.vdmj.definitions;

import java.util.List;
import java.util.Vector;

import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.patterns.Pattern;
import org.overturetool.vdmj.patterns.PatternList;
import org.overturetool.vdmj.pog.POContextStack;
import org.overturetool.vdmj.pog.ProofObligationList;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.typechecker.Pass;
import org.overturetool.vdmj.typechecker.TypeCheckException;
import org.overturetool.vdmj.typechecker.TypeComparator;
import org.overturetool.vdmj.types.BooleanType;
import org.overturetool.vdmj.types.Field;
import org.overturetool.vdmj.types.FunctionType;
import org.overturetool.vdmj.types.InvariantType;
import org.overturetool.vdmj.types.NamedType;
import org.overturetool.vdmj.types.RecordType;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.TypeList;
import org.overturetool.vdmj.types.UnresolvedType;
import org.overturetool.vdmj.values.FunctionValue;
import org.overturetool.vdmj.values.NameValuePair;
import org.overturetool.vdmj.values.NameValuePairList;


/**
 * A class to hold a type definition.
 */

public class TypeDefinition extends Definition
{
	private static final long serialVersionUID = 1L;
	public InvariantType type;
	public final Pattern invPattern;
	public final Expression invExpression;
	public ExplicitFunctionDefinition invdef;
	public boolean infinite = false;
	private DefinitionList composeDefinitions;

	public TypeDefinition(LexNameToken name, InvariantType type, Pattern invPattern,
		Expression invExpression)
	{
		super(Pass.TYPES, name.location, name, NameScope.TYPENAME);

		this.type = type;
		this.invPattern = invPattern;
		this.invExpression = invExpression;
		
		type.definitions = new DefinitionList(this);
		composeDefinitions = new DefinitionList();
	}

	@Override
	public String toString()
	{
		return accessSpecifier.ifSet(" ") +
				name.name + " = " + type.toDetailedString() +
				(invPattern == null ? "" :
					"\n\tinv " + invPattern + " == " + invExpression);
	}

	@Override
	public void implicitDefinitions(Environment base)
	{
		if (invPattern != null)
		{
    		invdef = getInvDefinition();
    		type.setInvariant(invdef);
		}
		else
		{
			invdef = null;
		}
		
		// Type definitions of the form "A = compose B of ... end" also define the type
		// B, which can be used globally. Here, we assume all compose types are legal
		// but in the typeCheck we check whether they match any existing definitions.
		
		if (type instanceof NamedType)
		{
			composeDefinitions.clear();
			NamedType nt = (NamedType)type;

			for (Type compose: nt.type.getComposeTypes())
			{
				RecordType rtype = (RecordType)compose;
				composeDefinitions.add(new TypeDefinition(rtype.name, rtype, null, null));
			}
		}
	}
	
	@Override
	public void typeResolve(Environment base)
	{
		try
		{
			infinite = false;
			type = (InvariantType)type.typeResolve(base, this);

			if (infinite)
			{
				report(3050, "Type '" + name + "' is infinite");
			}

			if (invdef != null)
			{
				invdef.typeResolve(base);
				invPattern.typeResolve(base);
			}
			
			composeDefinitions.typeResolve(base);
		}
		catch (TypeCheckException e)
		{
			type.unResolve();
			throw e;
		}
	}

	@Override
	public void typeCheck(Environment base, NameScope scope)
	{
		if (invdef != null)
		{
			invdef.typeCheck(base, NameScope.NAMES);
		}
		
		if (type instanceof NamedType)
		{
			// Rebuild the compose definitions, after we check whether they already exist
			composeDefinitions.clear();
			NamedType nt = (NamedType)type;

			for (Type compose: TypeComparator.checkComposeTypes(nt.type, base, true))
			{
				RecordType rtype = (RecordType)compose;
				composeDefinitions.add(new TypeDefinition(rtype.name, rtype, null, null));
			}
		}

		// We have to do the "top level" here, rather than delegating to the types
		// because the definition pointer from these top level types just refers
		// to the definition we are checking, which is never "narrower" than itself.
		// See the narrowerThan method in NamedType and RecordType.
		
		if (type instanceof NamedType)
		{
			NamedType ntype = (NamedType)type;
			
			if (ntype.type.narrowerThan(accessSpecifier))
			{
				report(3321, "Type component visibility less than type's definition");
			}
		}
		else if (type instanceof RecordType)
		{
			RecordType rtype = (RecordType)type;
			
			for (Field field: rtype.fields)
			{
				if (field.type.narrowerThan(accessSpecifier))
				{
					field.tagname.report(3321, "Field type visibility less than type's definition");
				}
			}
		}
	}

	@Override
	public Type getType()
	{
		return type;
	}

	@Override
	public Definition findName(LexNameToken sought, NameScope incState)
	{
		if (invdef != null && invdef.findName(sought, incState) != null)
		{
			return invdef;
		}

		return null;
	}

	@Override
	public Definition findType(LexNameToken sought, String fromModule)
	{
		if (composeDefinitions != null)
		{
			Definition d = composeDefinitions.findType(sought, fromModule);
			
			if (d != null)
			{
				return d;
			}
		}
		
		return super.findName(sought, NameScope.TYPENAME);
	}

	@Override
	public Expression findExpression(int lineno)
	{
		if (invdef != null)
		{
			Expression found = invdef.findExpression(lineno);
			if (found != null) return found;
		}

		return null;
	}

	@Override
	public DefinitionList getDefinitions()
	{
		DefinitionList defs = new DefinitionList(this);
		defs.addAll(composeDefinitions);

		if (invdef != null)
		{
			defs.add(invdef);
		}

		return defs;
	}

	@Override
	public LexNameList getVariableNames()
	{
		// This is only used in VDM++ type inheritance
		return new LexNameList(name);
	}

	@Override
	public NameValuePairList getNamedValues(Context ctxt)
	{
		NameValuePairList nvl = new NameValuePairList();

		if (invdef != null)
		{
			FunctionValue invfunc =	new FunctionValue(invdef, null, null, ctxt);
			nvl.add(new NameValuePair(invdef.name, invfunc));
		}

		return nvl;
	}

	private ExplicitFunctionDefinition getInvDefinition()
	{
		LexLocation loc = invPattern.location;
		PatternList params = new PatternList();
		params.add(invPattern);

		List<PatternList> parameters = new Vector<PatternList>();
		parameters.add(params);

		TypeList ptypes = new TypeList();

		if (type instanceof RecordType)
		{
			// Records are inv_R: R +> bool
			ptypes.add(new UnresolvedType(name));
		}
		else
		{
			// Named types are inv_T: x +> bool, for T = x
			NamedType nt = (NamedType)type;
			ptypes.add(nt.type);
		}

		FunctionType ftype =
			new FunctionType(loc, false, ptypes, new BooleanType(loc));

		ExplicitFunctionDefinition def = new ExplicitFunctionDefinition(
			name.getInvName(loc),
			NameScope.GLOBAL, null, ftype, parameters, invExpression,
			null, null, true, null);

		def.setAccessSpecifier(accessSpecifier);	// Same as type's
		def.classDefinition = classDefinition;
		ftype.definitions = new DefinitionList(def);
		return def;
	}

	@Override
	public boolean isRuntime()
	{
		return false;	// Though the inv definition is, of course
	}

	@Override
	public boolean isTypeDefinition()
	{
		return true;
	}

	@Override
	public ProofObligationList getProofObligations(POContextStack ctxt)
	{
		ProofObligationList list = new ProofObligationList();

		if (invdef != null)
		{
			list.addAll(invdef.getProofObligations(ctxt));
		}

		return list;
	}

	@Override
	public String kind()
	{
		return "type";
	}
}
