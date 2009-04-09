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

import org.overturetool.vdmj.definitions.DefinitionList;
import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.expressions.QuoteLiteralExpression;
import org.overturetool.vdmj.lex.LexQuoteToken;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.PatternMatchException;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.types.QuoteType;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.values.NameValuePairList;
import org.overturetool.vdmj.values.Value;

public class QuotePattern extends Pattern
{
	private static final long serialVersionUID = 1L;
	public final LexQuoteToken value;

	public QuotePattern(LexQuoteToken token)
	{
		super(token.location);
		this.value = token;
	}

	@Override
	public String toString()
	{
		return value.toString();
	}

	@Override
	public DefinitionList getDefinitions(Type type, NameScope scope)
	{
		return new DefinitionList();
	}

	@Override
	public NameValuePairList getNamedValues(Value expval, Context ctxt)
		throws PatternMatchException
	{
		NameValuePairList result = new NameValuePairList();

		try
		{
			if (!expval.quoteValue(ctxt).equals(value.value))
			{
				patternFail(4112, "Quote pattern match failed");
			}
		}
		catch (ValueException e)
		{
			patternFail(e);
		}

		return result;
	}

	@Override
	public Type getPossibleType()
	{
		return new QuoteType(value);
	}

	@Override
	public Expression getMatchingExpression()
	{
		return new QuoteLiteralExpression(value);
	}
}
