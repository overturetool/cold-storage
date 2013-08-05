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

import org.overture.ast.expressions.AFieldNumberExp;
import org.overture.ast.expressions.AIsExp;
import org.overture.ast.expressions.ANotUnaryExp;
import org.overture.ast.types.AProductType;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.POType;

public class TupleSelectObligation extends ProofObligation
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7776291065628025047L;

	public TupleSelectObligation(
		AFieldNumberExp exp, AProductType type, IPOContextStack ctxt)
	{
		// not is_(exp, type)
		super(exp, POType.TUPLE_SELECT, ctxt);

		ANotUnaryExp notExp = new ANotUnaryExp();
		AIsExp isExp = new AIsExp();
		
		isExp.setTest(exp);
		isExp.setBasicType(type); //Do we need the type definition instead? If so, the visitor must provide it.
		
		notExp.setExp(isExp);
		
//		valuetree.setContext(ctxt.getContextNodeList());
		valuetree.setPredicate(ctxt.getPredWithContext(notExp));		
	}
}