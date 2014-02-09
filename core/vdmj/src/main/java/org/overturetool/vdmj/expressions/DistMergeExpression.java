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

import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.pog.MapSetOfCompatibleObligation;
import org.overturetool.vdmj.pog.POContextStack;
import org.overturetool.vdmj.pog.ProofObligationList;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.types.MapType;
import org.overturetool.vdmj.types.SetType;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.TypeList;
import org.overturetool.vdmj.values.MapValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueMap;
import org.overturetool.vdmj.values.ValueSet;

public class DistMergeExpression extends UnaryExpression
{
	private static final long serialVersionUID = 1L;

	public DistMergeExpression(LexLocation location, Expression exp)
	{
		super(location, exp);
	}

	@Override
	public String toString()
	{
		return "(merge " + exp + ")";
	}

	@Override
	public Type typeCheck(Environment env, TypeList qualifiers, NameScope scope)
	{
		Type arg = exp.typeCheck(env, null, scope);

		if (arg.isSet())
		{
			SetType set = arg.getSet();

			if (!set.empty && set.setof.isMap())
			{
				return set.setof;
			}
		}

		report(3077, "Merge argument is not a set of maps");
		return new MapType(location);	// Unknown types
	}

	@Override
	public Value eval(Context ctxt)
	{
		breakpoint.check(location, ctxt);

		try
		{
    		ValueSet setmap = exp.eval(ctxt).setValue(ctxt);
    		ValueMap result = new ValueMap();

    		for (Value v: setmap)
    		{
    			ValueMap m = v.mapValue(ctxt);
    			
    			for (Value k: m.keySet())
    			{
    				Value rng = m.get(k);
    				Value old = result.put(k, rng);

    				if (old != null && !old.equals(rng))
    				{
    					abort(4021, "Duplicate map keys have different values: " + k, ctxt);
    				}
    			}
    		}

    		return new MapValue(result);
        }
        catch (ValueException e)
        {
        	return abort(e);
        }
    }

	@Override
	public ProofObligationList getProofObligations(POContextStack ctxt)
	{
		ProofObligationList obligations = new ProofObligationList();
		obligations.add(new MapSetOfCompatibleObligation(exp, ctxt));
		return obligations;
	}

	@Override
	public String kind()
	{
		return "dmerge";
	}
}
