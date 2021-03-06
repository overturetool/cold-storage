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

package org.overturetool.vdmj.types;

import org.overturetool.vdmj.lex.LexLocation;

public abstract class NumericType extends BasicType
{
	private static final long serialVersionUID = 1L;

	public NumericType(LexLocation location)
	{
		super(location);
	}

	public abstract int getWeight();

	@Override
	public boolean isNumeric()
	{
		return true;
	}

	@Override
	public NumericType getNumeric()
	{
		return this;
	}
	
	public static NumericType typeOf(long iv, LexLocation location)
	{
		if (iv > 0)
		{
			return new NaturalOneType(location);
		}
		else if (iv >= 0)
		{
			return new NaturalType(location);
		}
		else
		{
			return new IntegerType(location);
		}
	}
}