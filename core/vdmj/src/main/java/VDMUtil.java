/*******************************************************************************
 *
 *	Copyright (C) 2008, 2009 Fujitsu Services Ltd.
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

// This must be in the default package to work with VDMJ's native delegation.

import org.overturetool.vdmj.expressions.Expression;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.syntax.ExpressionReader;
import org.overturetool.vdmj.values.BooleanValue;
import org.overturetool.vdmj.values.CharacterValue;
import org.overturetool.vdmj.values.NilValue;
import org.overturetool.vdmj.values.ObjectValue;
import org.overturetool.vdmj.values.SeqValue;
import org.overturetool.vdmj.values.TupleValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueList;
import org.overturetool.vdmj.values.ValueSet;

public class VDMUtil
{
	public static Value set2seq(Value arg) throws ValueException
	{
		ValueSet set = arg.setValue(null);
		ValueList list = new ValueList();
		list.addAll(set);
		return new SeqValue(list);
	}

	public static Value val2seq_of_char(Value arg)
	{
		return new SeqValue(arg.toString());
	}

	public static Value seq_of_char2val(Value arg)
	{
		ValueList result = new ValueList();

		try
		{
			SeqValue seq = (SeqValue) arg;
			StringBuilder expression = new StringBuilder();
			
			for (Value v: seq.values)
			{
				CharacterValue ch = (CharacterValue) v;
				expression.append(ch.unicode);
			}
			
			LexTokenReader ltr = new LexTokenReader(expression.toString(), Dialect.VDM_PP);
			ExpressionReader reader = new ExpressionReader(ltr);
			reader.setCurrentModule("VDMUtil");
			Expression exp = reader.readExpression();
			result.add(new BooleanValue(true));
			Context ctxt = new Context(null, "seq_of_char2val", null);
			ctxt.setThreadState(null, null);
			result.add(exp.eval(ctxt));
		}
		catch (Exception e)
		{
			result = new ValueList();
			result.add(new BooleanValue(false));
			result.add(new NilValue());
		}

		return new TupleValue(result);
	}
	
	public static Value classname(Value arg)
	{
		Value a = arg.deref();
		
		if (a instanceof ObjectValue)
		{
			ObjectValue obj = (ObjectValue)a;
			return new SeqValue(obj.type.name.name);
		}
		else
		{
			return new NilValue();
		}
	}
}
