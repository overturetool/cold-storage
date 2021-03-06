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

package org.overturetool.vdmj.definitions;

import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.lex.LexToken;
import org.overturetool.vdmj.lex.Token;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.typechecker.Pass;
import org.overturetool.vdmj.types.Type;

/**
 * A class to hold an external state definition.
 */

public class ExternalDefinition extends Definition
{
	private static final long serialVersionUID = 1L;
	public final Definition state;
	public final boolean readOnly;
	public final LexNameToken oldname;	// For "wr" only

	public ExternalDefinition(Definition state, LexToken mode)
	{
		super(Pass.DEFS, state.location, state.name, NameScope.STATE);
		this.state = state;
		this.readOnly = mode.is(Token.READ);
		this.oldname = readOnly ? null : state.name.getOldName();
	}

	@Override
	public Definition findName(LexNameToken sought, NameScope scope)
	{
		if (sought.old)
		{
			return (sought.equals(oldname)) ? this : null;
		}

		return (sought.equals(state.name)) ? this : null;
	}

	@Override
	public String toString()
	{
		return (readOnly ? "ext rd " : "ext wr ") + state.name;
	}

	@Override
	public Type getType()
	{
		return state.getType();
	}

	@Override
	public void typeCheck(Environment base, NameScope scope)
	{
		// Nothing to do - state is type checked separately
	}

	@Override
    public void markUsed()
	{
		used = true;
		state.markUsed();
	}

	@Override
    protected boolean isUsed()
	{
		return state.isUsed();
	}

	@Override
	public boolean isUpdatable()
	{
		return true;
	}

	@Override
	public DefinitionList getDefinitions()
	{
		return new DefinitionList(state);
	}

	@Override
	public LexNameList getVariableNames()
	{
		return state.getVariableNames();
	}

	@Override
	public String kind()
	{
		return "external";
	}
}
