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

package org.overturetool.vdmj.pog;

import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.definitions.ClassInvariantDefinition;
import org.overturetool.vdmj.definitions.Definition;
import org.overturetool.vdmj.definitions.DefinitionList;
import org.overturetool.vdmj.definitions.ExplicitOperationDefinition;
import org.overturetool.vdmj.definitions.ImplicitOperationDefinition;
import org.overturetool.vdmj.definitions.StateDefinition;
import org.overturetool.vdmj.statements.AssignmentStatement;

public class StateInvariantObligation extends ProofObligation
{
	public StateInvariantObligation(AssignmentStatement ass, POContextStack ctxt)
	{
		super(ass.location, POType.STATE_INVARIANT, ctxt);
		StringBuilder sb = new StringBuilder();
		sb.append("-- After ");
		sb.append(ass);
		sb.append("\n");

		if (ass.classDefinition != null)
		{
			sb.append(invDefs(ass.classDefinition));
		}
		else	// must be because we have a module state invariant
		{
			StateDefinition def = ass.stateDefinition;

			sb.append("let ");
			sb.append(def.invPattern);
			sb.append(" = ");
			sb.append(def.name);
			sb.append(" in ");
			sb.append(def.invExpression);
		}

		value = ctxt.getObligation(sb.toString());
	}

	public StateInvariantObligation(
		ClassInvariantDefinition def,
		POContextStack ctxt)
	{
		super(def.location, POType.STATE_INVARIANT, ctxt);
		StringBuilder sb = new StringBuilder();
		sb.append("-- After instance variable initializers\n");
		sb.append(invDefs(def.classDefinition));

    	value = ctxt.getObligation(sb.toString());
	}

	public StateInvariantObligation(
		ExplicitOperationDefinition def,
		POContextStack ctxt)
	{
		super(def.location, POType.STATE_INVARIANT, ctxt);
		StringBuilder sb = new StringBuilder();
		sb.append("-- After ");
		sb.append(def.name);
		sb.append(" constructor body\n");
		sb.append(invDefs(def.classDefinition));

    	value = ctxt.getObligation(sb.toString());
	}

	public StateInvariantObligation(
		ImplicitOperationDefinition def,
		POContextStack ctxt)
	{
		super(def.location, POType.STATE_INVARIANT, ctxt);
		StringBuilder sb = new StringBuilder();
		sb.append("-- After ");
		sb.append(def.name);
		sb.append(" constructor body\n");
		sb.append(invDefs(def.classDefinition));

    	value = ctxt.getObligation(sb.toString());
	}

	private String invDefs(ClassDefinition def)
	{
		StringBuilder sb = new StringBuilder();
		DefinitionList invdefs = def.getInvDefs();
		String sep = "";

		for (Definition d: invdefs)
		{
			ClassInvariantDefinition cid = (ClassInvariantDefinition)d;
			sb.append(sep);
			sb.append(cid.expression);
			sep = " and ";
		}

    	return sb.toString();
	}
}
