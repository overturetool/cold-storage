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

package org.overturetool.vdmj.patterns;

import java.util.Vector;

import org.overturetool.vdmj.expressions.ExpressionList;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.PatternMatchException;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.TypeSet;
import org.overturetool.vdmj.types.UnknownType;
import org.overturetool.vdmj.util.Utils;
import org.overturetool.vdmj.values.NameValuePairList;
import org.overturetool.vdmj.values.Value;


@SuppressWarnings("serial")
public class PatternList extends Vector<Pattern>
{
	@Override
	public String toString()
	{
		return Utils.listToString(this);
	}

	public NameValuePairList getNamedValues(Value value, Context ctxt)
		throws PatternMatchException
	{
		NameValuePairList list = new NameValuePairList();

		for (Pattern p: this)
		{
			list.addAll(p.getNamedValues(value, ctxt));
		}

		return list;
	}

	public void unResolve()
	{
		for (Pattern p: this)
		{
			p.unResolve();
		}
	}

	public void typeResolve(Environment env)
	{
		for (Pattern p: this)
		{
			p.typeResolve(env);
		}
	}

	public Type getPossibleType(LexLocation location)
	{
		switch (size())
		{
			case 0:
				return new UnknownType(location);

			case 1:
				return get(0).getPossibleType();

			default:
        		TypeSet list = new TypeSet();

        		for (Pattern p: this)
        		{
        			list.add(p.getPossibleType());
        		}

        		return list.getType(location);		// NB. a union of types
		}
	}

	public ExpressionList getMatchingExpressionList()
	{
		ExpressionList list = new ExpressionList();

		for (Pattern p: this)
		{
			list.add(p.getMatchingExpression());
		}

		return list;
	}

	public boolean isConstrained()
	{
		for (Pattern p: this)
		{
			if (p.isConstrained()) return true;		// NB. OR
		}

		return false;
	}

	public boolean isSimple()
	{
		for (Pattern p: this)
		{
			if (!p.isSimple()) return false;		// NB. AND
		}

		return true;
	}

	public boolean alwaysMatches()
	{
		for (Pattern p: this)
		{
			if (!p.alwaysMatches()) return false;	// NB. AND
		}

		return true;
	}
}
