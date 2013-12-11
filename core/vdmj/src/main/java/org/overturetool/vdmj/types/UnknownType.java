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

import java.util.Vector;

import org.overturetool.vdmj.definitions.AccessSpecifier;
import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.lex.LexLocation;


public class UnknownType extends Type
{
	private static final long serialVersionUID = 1L;

	public UnknownType(LexLocation location)
	{
		super(location);
	}

	@Override
	public Type isType(String typename)
	{
		return null;	// Isn't any particular type?
	}

	@Override
	public boolean narrowerThan(AccessSpecifier accessSpecifier)
	{
		return false;
	}

	@Override
	public boolean isType(Class<? extends Type> typeclass)
	{
		return true;
	}

	@Override
	public boolean isUnknown()
	{
		return true;
	}

	@Override
	public boolean isSeq()
	{
		return true;
	}

	@Override
	public boolean isSet()
	{
		return true;
	}

	@Override
	public boolean isMap()
	{
		return true;
	}

	@Override
	public boolean isRecord()
	{
		return true;
	}

	@Override
	public boolean isTag()
	{
		return true;
	}

	@Override
	public boolean isClass()
	{
		return true;		// Too much trouble?
	}

	@Override
	public boolean isNumeric()
	{
		return true;
	}

	@Override
	public boolean isProduct()
	{
		return true;
	}

	@Override
	public boolean isProduct(int n)
	{
		return true;
	}

	@Override
	public boolean isFunction()
	{
		return true;
	}

	@Override
	public boolean isOperation()
	{
		return true;
	}

	@Override
	public SeqType getSeq()
	{
		return new SeqType(location, new UnknownType(location));	// seq of ?
	}

	@Override
	public SetType getSet()
	{
		return new SetType(location, new UnknownType(location));	// set of ?
	}

	@Override
	public MapType getMap()
	{
		return new MapType(location);	// Unknown |-> Unknown
	}

	@Override
	public RecordType getRecord()
	{
		return new RecordType(location, new Vector<Field>());
	}

	@Override
	public ClassType getClassType()
	{
		return new ClassType(location, new ClassDefinition());
	}

	@Override
	public NumericType getNumeric()
	{
		return new RealType(location);
	}

	@Override
	public ProductType getProduct()
	{
		return new ProductType(location, new TypeList());
	}

	@Override
	public ProductType getProduct(int n)
	{
		TypeList tl = new TypeList();

		for (int i=0; i<n; i++)
		{
			tl.add(new UnknownType(location));
		}

		return new ProductType(location, tl);
	}

	@Override
	public FunctionType getFunction()
	{
		return new FunctionType(
			location, true, new TypeList(), new UnknownType(location));
	}

	@Override
	public OperationType getOperation()
	{
		return new OperationType(
			location, new TypeList(), new UnknownType(location));
	}

	@Override
	public boolean equals(Object other)
	{
		return true;	// Assume OK to avoid error explosions
	}

	@Override
	public String toDisplay()
	{
		return "?";
	}
}
