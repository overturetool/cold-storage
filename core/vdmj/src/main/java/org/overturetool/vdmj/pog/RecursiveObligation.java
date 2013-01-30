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

import org.overturetool.vdmj.definitions.ExplicitFunctionDefinition;
import org.overturetool.vdmj.definitions.ImplicitFunctionDefinition;
import org.overturetool.vdmj.expressions.ApplyExpression;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.patterns.PatternList;
import org.overturetool.vdmj.types.PatternListTypePair;
import org.overturetool.vdmj.util.Utils;

public class RecursiveObligation extends ProofObligation
{
	public RecursiveObligation(
		ExplicitFunctionDefinition def, ApplyExpression apply, POContextStack ctxt)
	{
		super(apply.location, POType.RECURSIVE, ctxt);
		StringBuilder sb = new StringBuilder();

		sb.append(def.measure.getName());
		
		if (def.typeParams != null)
		{
			sb.append("[");
			
			for (LexNameToken type: def.typeParams)
			{
				sb.append("@");
				sb.append(type);
			}
			
			sb.append("]");
		}
		
		String sep = "";
		sb.append("(");
		
		for (PatternList plist: def.paramPatternList)
		{
			 sb.append(sep);
			 sb.append(Utils.listToString(plist));
			 sep = ", ";
		}

		sb.append(")");
		sb.append(def.measureLexical > 0 ? " LEX" + def.measureLexical + "> " : " > ");
		sb.append(apply.getMeasureApply(def.measure));

		value = ctxt.getObligation(sb.toString());
	}

	public RecursiveObligation(
		ImplicitFunctionDefinition def, ApplyExpression apply, POContextStack ctxt)
	{
		super(def.location, POType.RECURSIVE, ctxt);
		StringBuilder sb = new StringBuilder();

		sb.append(def.measure);
		sb.append("(");

		for (PatternListTypePair pltp: def.parameterPatterns)
		{
			sb.append(pltp.patterns);
		}

		sb.append(")");
		sb.append(def.measureLexical > 0 ? " LEX" + def.measureLexical + "> " : " > ");
		sb.append(def.measure);
		sb.append("(");
		sb.append(apply.args);
		sb.append(")");

		value = ctxt.getObligation(sb.toString());
	}
}
