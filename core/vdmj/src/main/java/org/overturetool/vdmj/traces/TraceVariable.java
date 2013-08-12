/*******************************************************************************
 *
 *	Copyright (c) 2009 Fujitsu Services Ltd.
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

package org.overturetool.vdmj.traces;

import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.values.Value;

public class TraceVariable
{
	public final LexLocation location;
	public final LexNameToken name;
	public final Value value;
	public final Type type;
	public final boolean clone;

	public TraceVariable(LexLocation location, LexNameToken name, Value value, Type type, boolean clone)
	{
		this.location = location;
		this.name = name;
		this.value = value;
		this.type = type;
		this.clone = clone;
	}

	@Override
	public String toString()
	{
		return name + ":" + type + " = " + value;
	}
}
