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

import java.util.List;
import java.util.Vector;

import org.overturetool.vdmj.definitions.DefinitionList;
import org.overturetool.vdmj.definitions.LocalDefinition;
import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.expressions.VariableExpression;
import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.UnknownType;
import org.overturetool.vdmj.values.NameValuePair;
import org.overturetool.vdmj.values.NameValuePairList;
import org.overturetool.vdmj.values.Value;

public class IdentifierPattern extends Pattern
{
	private static final long serialVersionUID = 1L;
	public final LexNameToken name;
	private boolean constrained;

	public IdentifierPattern(LexNameToken token)
	{
		super(token.location);
		this.name = token;
	}

	@Override
	public int getLength()
	{
		return ANY;	// Special value meaning "any length"
	}

	@Override
	public String toString()
	{
		return name.toString();
	}

	@Override
	public DefinitionList getAllDefinitions(Type ptype, NameScope scope)
	{
		DefinitionList defs = new DefinitionList();
		defs.add(new LocalDefinition(location, name, scope, ptype));
		return defs;
	}

	@Override
	public LexNameList getAllVariableNames()
	{
		LexNameList list = new LexNameList();
		list.add(name);
		return list;
	}

	@Override
	public List<NameValuePairList> getAllNamedValues(Value expval, Context ctxt)
	{
		List<NameValuePairList> result = new Vector<NameValuePairList>();
		NameValuePairList list = new NameValuePairList();
		list.add(new NameValuePair(name, expval));
		result.add(list);
		return result;
	}

	@Override
	public Type getPossibleType()
	{
		return new UnknownType(location);
	}

	@Override
	public Expression getMatchingExpression()
	{
		return new VariableExpression(name);
	}

	@Override
	public boolean isConstrained()
	{
		return constrained;	// The variable may be constrained to be the same as another occurrence
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}

	@Override
	public boolean alwaysMatches()
	{
		return true;
	}

	public void setConstrained(boolean c)
	{
		constrained = c;
	}

	@Override
	public List<IdentifierPattern> findIdentifiers()
	{
		List<IdentifierPattern> list = new Vector<IdentifierPattern>();
		list.add(this);
		return list;
	}
}
