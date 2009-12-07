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

package org.overturetool.vdmj.pog;

import org.overturetool.vdmj.lex.LexLocation;

abstract public class ProofObligation
{
	public final LexLocation location;
	public final POType kind;
	public final String name;
	public String value;
	public POStatus status;

	private int var = 1;

	public ProofObligation(LexLocation location, POType kind, POContextStack ctxt)
	{
		this.location = location;
		this.kind = kind;
		this.name = ctxt.getName();
		this.status = POStatus.UNPROVED;
	}

	public String getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return  name + ": " + kind + " obligation " + location + "\n" + value;
	}

	protected String getVar(String root)
	{
		return root + var++;
	}

	private String[] trivialPatterns =
	{
		"^( *\\(+forall [^\\n]+\\n)*? *\\(forall (\\w+) in set \\(([^&]+)\\) &(.+?)?\\n *\\2 in set \\3\\)+\\n$",
		"^( *\\(+forall [^\\n]+\\n?)*? *\\(+(\\w+) in set \\(([^&]+)\\)+ =>\\n *\\2 in set \\3\\)+\\n$",
		"^( *\\(+forall [^\\n]+\\n)*? *\\(+not \\((.+?) \\= (.+?)\\) =>\\n *\\2 \\<\\> \\3\\)+\\n$"
	};

	public void trivialCheck()
	{
		for (String trivial: trivialPatterns)
		{
			if (value.matches(trivial))
			{
				status = POStatus.TRIVIAL;
				break;
			}
		}
	}
}
