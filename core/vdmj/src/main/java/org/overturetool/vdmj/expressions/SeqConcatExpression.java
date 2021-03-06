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

package org.overturetool.vdmj.expressions;

import org.overturetool.vdmj.lex.LexToken;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.types.Seq1Type;
import org.overturetool.vdmj.types.SeqType;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.TypeList;
import org.overturetool.vdmj.types.TypeSet;
import org.overturetool.vdmj.types.UnknownType;
import org.overturetool.vdmj.values.SeqValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueList;

public class SeqConcatExpression extends BinaryExpression
{
	private static final long serialVersionUID = 1L;

	public SeqConcatExpression(Expression left, LexToken op, Expression right)
	{
		super(left, op, right);
	}

	@Override
	public final Type typeCheck(Environment env, TypeList qualifiers, NameScope scope)
	{
		ltype = left.typeCheck(env, null, scope);
		rtype = right.typeCheck(env, null, scope);

		if (!ltype.isSeq())
		{
			report(3157, "Left hand of '^' is not a sequence");
			ltype = new SeqType(location, new UnknownType(location));
		}

		if (!rtype.isSeq())
		{
			report(3158, "Right hand of '^' is not a sequence");
			rtype = new SeqType(location, new UnknownType(location));
		}

		Type lof = ltype.getSeq();
		Type rof = rtype.getSeq();
		boolean seq1 = (lof instanceof Seq1Type) || (rof instanceof Seq1Type);
		
		lof = ((SeqType)lof).seqof;
		rof = ((SeqType)rof).seqof;
		TypeSet ts = new TypeSet(lof, rof);
		
		return seq1 ?
			new Seq1Type(location, ts.getType(location)) :
			new SeqType(location, ts.getType(location));
	}

	@Override
	public Value eval(Context ctxt)
	{
		// breakpoint.check(location, ctxt);
		location.hit();		// Mark as covered

		try
		{
    		Value lv = left.eval(ctxt);
    		Value rv = right.eval(ctxt);

    		ValueList result = new ValueList();
    		result.addAll(lv.seqValue(ctxt));
    		result.addAll(rv.seqValue(ctxt));

    		return new SeqValue(result);
		}
		catch (ValueException e)
		{
			return abort(e);
		}
	}

	@Override
	public String kind()
	{
		return "concatenation";
	}
}
