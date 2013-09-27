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

package org.overture.pog.obligation;

import java.util.List;

import org.overture.ast.expressions.PExp;
import org.overture.ast.util.Utils;

public class FunctionApplyObligation extends ProofObligation
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7146271970744572457L;

	public FunctionApplyObligation(PExp root, List<PExp> args, String prename,
			POContextStack ctxt)
	{
		super(root.getLocation(), POType.FUNC_APPLY, ctxt);
		StringBuilder sb = new StringBuilder();

		if (prename == null)
		{
			sb.append("pre_(");
			sb.append(root);
			sb.append(", ");
			sb.append(Utils.listToString(args));
			sb.append(")");
		} else
		{
			sb.append(prename);
			sb.append(Utils.listToString("(", args, ", ", ")"));
		}

		value = ctxt.getObligation(sb.toString());
	}
}