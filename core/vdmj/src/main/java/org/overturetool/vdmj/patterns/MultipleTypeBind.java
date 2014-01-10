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

import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.pog.POContextStack;
import org.overturetool.vdmj.pog.ProofObligationList;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.typechecker.NameScope;
import org.overturetool.vdmj.typechecker.TypeComparator;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.values.ValueList;

public class MultipleTypeBind extends MultipleBind
{
	private static final long serialVersionUID = 1L;
	public Type type;

	public MultipleTypeBind(PatternList plist, Type type)
	{
		super(plist);
		this.type = type;
	}

	@Override
	public String toString()
	{
		return plist + ":" + type;
	}

	@Override
	public Type typeCheck(Environment base, NameScope scope)
	{
		plist.typeResolve(base);
		type = type.typeResolve(base, null);
		Type ptype = getPossibleType();
		
		TypeComparator.checkComposeTypes(type, base, false);

		if (!TypeComparator.compatible(ptype, type))
		{
			type.report(3265, "At least one bind cannot match this type");
			type.detail2("Binds", ptype, "Type", type);
		}

		return type;
	}

	@Override
	public ValueList getBindValues(Context ctxt) throws ValueException
	{
		return type.getAllValues(ctxt);
	}

	@Override
	public ProofObligationList getProofObligations(POContextStack ctxt)
	{
		return new ProofObligationList();
	}

	@Override
	public ValueList getValues(Context ctxt)
	{
		return new ValueList();
	}

	@Override
	public LexNameList getOldNames()
	{
		return new LexNameList();
	}
}
