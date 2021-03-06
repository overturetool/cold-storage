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

package org.overturetool.vdmj.typechecker;

import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.definitions.Definition;
import org.overturetool.vdmj.definitions.DefinitionList;
import org.overturetool.vdmj.definitions.DefinitionSet;
import org.overturetool.vdmj.definitions.StateDefinition;
import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;

/**
 * The parent class of all type checking environments.
 */

abstract public class Environment
{
	/** The environment chain. */
	protected final Environment outer;

	/** The enclosing func/op definition at this point, or null. */
	private Definition enclosingDefinition = null;

	/**
	 * Create an environment linking to the given outer chain.
	 * @param outer
	 */

	public Environment(Environment outer)
	{
		this.outer = outer;
	}

	/**
	 * Check whether the list of definitions passed contains any duplicates,
	 * or whether any names in the list hide the same name further down the
	 * environment chain.
	 *
	 * @param list	The list of definitions to check.
	 */

	protected void dupHideCheck(DefinitionList list, NameScope scope)
	{
		LexNameList allnames = list.getVariableNames();

		for (LexNameToken n1: allnames)
		{
			LexNameList done = new LexNameList();

			for (LexNameToken n2: allnames)
			{
				if (n1 != n2 && n1.equals(n2) && !done.contains(n1))
				{
					TypeChecker.warning(5007, "Duplicate definition: " + n1, n1.location);
					done.add(n1);
				}
			}

			if (outer != null)
			{
				// We search for any scoped name (ie. the first), but then check
				// the scope matches what we can see. If we pass scope to findName
				// it throws errors if the name does not match the scope.

				Definition def = outer.findName(n1, NameScope.NAMESANDSTATE);

				if (def != null && def.location != n1.location &&
					def.nameScope.matches(scope))
				{
					// Reduce clutter for names in the same module/class
					String message = null;

					if (def.location.file.equals(n1.location.file))
					{
						message = def.name + " " + def.location.toShortString() +
							" hidden by " +	n1.name;
					}
					else
					{
						message = def.name + " " + def.location +
							" hidden by " + n1.name;
					}

					TypeChecker.warning(5008, message, n1.location);
				}
			}
		}
	}

	public Definition getEnclosingDefinition()
	{
		if (enclosingDefinition != null)
		{
			return enclosingDefinition;
		}

		return outer == null ? null : outer.getEnclosingDefinition();
	}

	public void setEnclosingDefinition(Definition def)
	{
		enclosingDefinition = def;
	}

	/** Find a name in the environment of the given scope. */
	abstract public Definition findName(LexNameToken name, NameScope scope);

	/** Find a type in the environment. */
	abstract public Definition findType(LexNameToken name, String fromModule);

	/** Find the state defined in the environment, if any. */
	abstract public StateDefinition findStateDefinition();

	/** Find the enclosing class definition, if any. */
	abstract public ClassDefinition findClassDefinition();

	/** True if the calling context is a static function or operation. */
	abstract public boolean isStatic();

	/** Check whether any definitions in the environment were unused. */
	abstract public void unusedCheck();

	/** True if this is a VDM++ environment. */
	abstract public boolean isVDMPP();

	/** True if this is a VDM-RT "system" environment. */
	abstract public boolean isSystem();

	/** Find functions and operations of the given basic name. */
	abstract public DefinitionSet findMatches(LexNameToken name);

	/** Mark all definitions, at this level, used. */
	public void markUsed()
	{
		// Nothing, by default. Implemented in flat environments.
	}

	/** Add details to a TC error with alternative fn/op name possibilities. */
	public void listAlternatives(LexNameToken name)
	{
		for (Definition possible: findMatches(name))
		{
			if (possible.isFunctionOrOperation())
			{
				TypeChecker.detail("Possible", possible.name);
			}
		}
	}

	/** Unravelling unused check. */
	public void unusedCheck(Environment downTo)
	{
		Environment p = this;

		while (p != null && p != downTo)
		{
			p.unusedCheck();
			p = p.outer;
		}
	}
}
