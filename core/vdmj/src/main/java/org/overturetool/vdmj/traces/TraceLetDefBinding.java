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

package org.overturetool.vdmj.traces;

import java.util.List;


import org.overturetool.vdmj.definitions.Definition;
import org.overturetool.vdmj.definitions.DefinitionList;
import org.overturetool.vdmj.definitions.ValueDefinition;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.typechecker.FlatCheckedEnvironment;
import org.overturetool.vdmj.typechecker.NameScope;

/**
 * A class representing a let-definition trace binding.
 */

public class TraceLetDefBinding extends TraceDefinition
{
    private static final long serialVersionUID = 1L;
	public final DefinitionList localDefs;
	public final TraceDefinition body;

	public TraceLetDefBinding(
		LexLocation location, List<ValueDefinition> localDefs, TraceDefinition body)
	{
		super(location);
		this.localDefs = new DefinitionList();
		this.localDefs.addAll(localDefs);
		this.body = body;
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder("let ");

		for (Definition d: localDefs)
		{
			result.append(d.toString());
			result.append(" ");
		}

		result.append("in ");
		result.append(body);

		return result.toString();
	}

	@Override
	public void typeCheck(Environment base, NameScope scope)
	{
		Environment local = base;

		for (Definition d: localDefs)
		{
			d.typeResolve(base);
			d.typeCheck(local, scope);
			local = new FlatCheckedEnvironment(d, local, scope);
		}

		body.typeCheck(local, scope);
		local.unusedCheck(base);
	}

	@Override
	public TraceNode expand(Context ctxt)
	{
		Context evalContext = new Context(location, "TRACE", ctxt);

		for (Definition d: localDefs)
		{
			evalContext.putList(d.getNamedValues(evalContext));
		}

		TraceNode node = body.expand(evalContext);
		node.addVariables(new TraceVariableList(evalContext, localDefs));
		return node;
	}
}
