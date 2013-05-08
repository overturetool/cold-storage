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
import org.overturetool.vdmj.typechecker.TypeComparator;
import org.overturetool.vdmj.types.BooleanType;
import org.overturetool.vdmj.types.SetType;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.TypeList;
import org.overturetool.vdmj.values.BooleanValue;
import org.overturetool.vdmj.values.Value;

public class InSetExpression extends BinaryExpression
{
	private static final long serialVersionUID = 1L;

	public InSetExpression(Expression left, LexToken op, Expression right)
	{
		super(left, op, right);
	}

	@Override
	public Type typeCheck(Environment env, TypeList qualifiers, NameScope scope)
	{
		ltype = left.typeCheck(env, null, scope);
		rtype = right.typeCheck(env, null, scope);

		if (!rtype.isSet())
		{
			report(3110, "Argument of 'in set' is not a set");
			detail("Actual", rtype);
		}
		else
		{
			SetType stype = rtype.getSet();
			
			if (!TypeComparator.compatible(stype.setof, ltype))
			{
				report(3319, "'in set' expression is always false");
				detail2("Element", ltype, "Set", stype);
			}
		}

		return new BooleanType(location);
	}

	@Override
	public Value eval(Context ctxt)
	{
		// breakpoint.check(location, ctxt);
		location.hit();		// Mark as covered

		Value elem = left.eval(ctxt);
		Value set = right.eval(ctxt);

		try
		{
			return new BooleanValue(set.setValue(ctxt).contains(elem));
        }
        catch (ValueException e)
        {
        	return abort(e);
        }
	}

	@Override
	public String kind()
	{
		return "set membership";
	}
}
