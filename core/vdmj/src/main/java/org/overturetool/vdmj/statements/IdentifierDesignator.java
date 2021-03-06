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

package org.overturetool.vdmj.statements;

import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.definitions.Definition;
import org.overturetool.vdmj.definitions.DefinitionSet;
import org.overturetool.vdmj.definitions.ExternalDefinition;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.types.FunctionType;
import org.overturetool.vdmj.types.OperationType;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.UnknownType;
import org.overturetool.vdmj.values.Value;

public class IdentifierDesignator extends StateDesignator
{
	private static final long serialVersionUID = 1L;
	public final LexNameToken name;

	public IdentifierDesignator(LexNameToken name)
	{
		super(name.location);
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name.getName();
	}

	@Override
	public Type typeCheck(Environment env)
	{
		if (env.isVDMPP())
		{
			// We generate an explicit name because accessing a variable
			// by name in VDM++ does not "inherit" values from a superclass.

			LexNameToken exname = name.getExplicit(true);
			Definition def = env.findName(exname, NameScope.STATE);

			if (def == null)
			{
				DefinitionSet matches = env.findMatches(exname);
				
				if (!matches.isEmpty())
				{
					Definition match = matches.iterator().next();	// Just take first
					
					if (match.isFunction())
					{
						report(3247, "Function apply not allowed in state designator");
					}
					else
					{
						report(3247, "Operation call not allowed in state designator");
					}
					
					return match.getType();
				}
				else
				{
					report(3247, "Symbol '" + name + "' is not an updatable variable");
				}
				
				return new UnknownType(location);
			}
			else if (!def.isUpdatable())
			{
				report(3301, "Variable '" + name + "' in scope is not updatable");
				return new UnknownType(location);
			}
			else if (def.classDefinition != null)
			{
    			if (!ClassDefinition.isAccessible(env, def, true))
    			{
    				report(3180, "Inaccessible member '" + name + "' of class " +
    					def.classDefinition.name.name);
    				return new UnknownType(location);
    			}
    			else if (!def.isStatic() && env.isStatic())
    			{
    				report(3181, "Cannot access " + name + " from a static context");
    				return new UnknownType(location);
    			}
			}

			return def.getType();
		}
		else
		{
			Definition def = env.findName(name, NameScope.STATE);

			if (def == null)
			{
				report(3247, "Unknown state variable '" + name + "' in assignment");
				return new UnknownType(name.location);
			}
			else if (def.isFunction())
			{
				report(3247, "Function apply not allowed in state designator");
				return new UnknownType(name.location);
			}
			else if (def.isOperation())
			{
				report(3247, "Operation call not allowed in state designator");
				return new UnknownType(name.location);
			}
			else if (!def.isUpdatable())
			{
				report(3301, "Variable '" + name + "' in scope is not updatable");
				return new UnknownType(name.location);
			}
			else if (def instanceof ExternalDefinition)
			{
				ExternalDefinition d = (ExternalDefinition)def;

				if (d.readOnly)
				{
					report(3248, "Cannot assign to 'ext rd' state " + name);
				}
			}
			// else just state access in (say) an explicit operation

			return def.getType();
		}
	}

	@Override
	public Value eval(Context ctxt)
	{
		// We lookup the name in a context comprising only state...
		// return ctxt.getUpdateable().lookup(name.getExplicit(true));
		return ctxt.lookup(name.getExplicit(true));
	}

	@Override
	public Definition targetDefinition(Environment env)
	{
		return env.findName(name, NameScope.STATE);
	}
}
