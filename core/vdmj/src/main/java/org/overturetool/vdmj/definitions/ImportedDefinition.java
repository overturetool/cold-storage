/*******************************************************************************
 *
 *	Copyright (C) 2008 Fujitsu Services Ltd.
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

import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.typechecker.Pass;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.values.NameValuePair;
import org.overturetool.vdmj.values.NameValuePairList;

/**
 * A class to hold an imported definition.
 */

public class ImportedDefinition extends Definition
{
	private static final long serialVersionUID = 1L;
	public final Definition def;

	public ImportedDefinition(LexLocation location, Definition def)
	{
		super(Pass.DEFS, location, def.name, def.nameScope);
		this.def = def;
	}

	@Override
	public String toString()
	{
		return def.toString();
	}

	@Override
	public Type getType()
	{
		return def.getType();
	}

	@Override
	public void typeResolve(Environment env)
	{
		def.typeResolve(env);
	}

	@Override
	public void typeCheck(Environment base, NameScope scope)
	{
		def.typeCheck(base, scope);
	}

	@Override
    public void markUsed()
	{
		used = true;
		def.markUsed();
	}

//	@Override
//    protected boolean isUsed()
//	{
//		return def.isUsed();
//	}

	@Override
	public DefinitionList getDefinitions()
	{
		return new DefinitionList(def);
	}

	@Override
	public LexNameList getVariableNames()
	{
		return def.getVariableNames();
	}

	@Override
	public Definition findType(LexNameToken sought, String fromModule)
	{
		// We can only find an import if it is being sought from the module that
		// imports it.

		if (fromModule != null && !location.module.equals(fromModule))
		{
			return null;	// Someone else's import
		}

		Definition d = def.findType(sought, fromModule);

		if (d != null)
		{
			markUsed();
		}

		return d;
	}

	@Override
	public Definition findName(LexNameToken sought, NameScope scope)
	{
		Definition d = def.findName(sought, scope);

		if (d != null)
		{
			markUsed();
		}

		return d;
	}

	@Override
	public NameValuePairList getNamedValues(Context ctxt)
	{
		NameValuePairList renamed = new NameValuePairList();

		for (NameValuePair nv: def.getNamedValues(ctxt))
		{
			if (nv.name.equals(def.name))	// NB. excludes pre/post/inv functions
			{
				renamed.add(new NameValuePair(name, nv.value));
			}
		}

		return renamed;
	}

	@Override
	public String kind()
	{
		return "import";
	}

	@Override
	public boolean isFunction()
	{
		return def.isFunction();
	}

	@Override
	public boolean isOperation()
	{
		return def.isOperation();
	}

	@Override
	public boolean isCallableOperation()
	{
		return def.isCallableOperation();
	}

	@Override
	public boolean isInstanceVariable()
	{
		return def.isInstanceVariable();
	}

	@Override
	public boolean isTypeDefinition()
	{
		return def.isTypeDefinition();
	}

	@Override
	public boolean isValueDefinition()
	{
		return def.isValueDefinition();
	}

	@Override
	public boolean isRuntime()
	{
		return def.isRuntime();
	}

	@Override
	public boolean isUpdatable()
	{
		return def.isUpdatable();
	}

	@Override
	public Definition deref()
	{
		return def.deref();
	}
}
