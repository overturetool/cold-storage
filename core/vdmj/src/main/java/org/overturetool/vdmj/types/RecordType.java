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

import java.util.List;

import org.overturetool.vdmj.definitions.AccessSpecifier;
import org.overturetool.vdmj.definitions.Definition;
import org.overturetool.vdmj.definitions.TypeDefinition;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.util.Utils;
import org.overturetool.vdmj.values.RecordValue;
import org.overturetool.vdmj.values.TupleValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueList;


public class RecordType extends InvariantType
{
	private static final long serialVersionUID = 1L;
	public final LexNameToken name;
	public final List<Field> fields;
	public final boolean composed;	// Created via "compose R of ... end"

	public RecordType(LexNameToken name, List<Field> fields, boolean composed)
	{
		super(name.location);
		this.name = name;
		this.fields = fields;
		this.composed = composed;
	}

	public RecordType(LexLocation location, List<Field> fields)
	{
		super(location);
		this.name = new LexNameToken("?", "?", location);
		this.fields = fields;
		this.composed = false;
	}

	public Field findField(String tag)
	{
		for (Field f: fields)
		{
			if (f.tag.equals(tag))
			{
				return f;
			}
		}

		return null;
	}

	@Override
	public Type isType(String typename)
	{
		if (opaque) return null;

		if (typename.indexOf('`') > 0)
		{
			return (name.getName().equals(typename)) ? this : null;
		}
		else
		{
			// Local typenames aren't qualified with the local module name
			return (name.name.equals(typename)) ? this : null;
		}
	}

	@Override
	public boolean isRecord()
	{
		if (opaque) return false;
		return true;
	}

	@Override
	public boolean isTag()
	{
		return true;
	}

	@Override
	public RecordType getRecord()
	{
		return this;
	}

	@Override
	public void unResolve()
	{
		if (!resolved) return; else { resolved = false; }

		for (Field f: fields)
		{
			f.unResolve();
		}
	}

	private boolean infinite = false;

	@Override
	public Type typeResolve(Environment env, TypeDefinition root)
	{
		if (resolved)
		{
			return this;
		}
		else
		{
			resolved = true;
			infinite = false;
		}

		for (Field f: fields)
		{
			if (root != null)
				root.infinite = false;

			f.typeResolve(env, root);

			if (root != null)
				infinite = infinite || root.infinite;
		}

		if (root != null) root.infinite = infinite;
		return this;
	}

	@Override
	public String toDisplay()
	{
		return name.toString();
	}

	@Override
	public String toDetailedString()
	{
		return "compose " + name + " of " + Utils.listToString(fields) + " end";
	}

	@Override
	public boolean equals(Object other)
	{
		other = deBracket(other);

		if (other instanceof RecordType)
		{
			RecordType rother = (RecordType)other;
			return name.equals(rother.name);	// NB. identical
		}

		return false;
	}

	@Override
	public int compareTo(Type other)
	{
		if (other instanceof RecordType)
		{
			RecordType rt = (RecordType)other;
    		String n1 = name.getExplicit(true).toString();
    		String n2 = rt.name.getExplicit(true).toString();
    		return n1.compareTo(n2);
		}
		else
		{
			return super.compareTo(other);
		}
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}

	@Override
	public ValueList getAllValues(Context ctxt) throws ValueException
	{
		TypeList types = new TypeList();

		for (Field f: fields)
		{
			types.add(f.type);
		}

		ValueList results = new ValueList();

		for (Value v: types.getAllValues(ctxt))
		{
			try
			{
				TupleValue tuple = (TupleValue)v;
				results.add(new RecordValue(this, tuple.values, ctxt));
			}
			catch (ValueException e)
			{
				// Value does not match invariant, so ignore it
			}
		}

		return results;
	}
	
	@Override
	public boolean narrowerThan(AccessSpecifier accessSpecifier)
	{
		if (inNarrower)
		{
			return false;
		}
		else
		{
			inNarrower = true;
		}
		
		boolean result = false;
		
		if (definitions != null)
		{
			for (Definition d: definitions)
			{
				if (d.accessSpecifier.narrowerThan(accessSpecifier))
				{
					result = true;
					break;
				}
			}
		}
		else
		{
			for (Field field: fields)
			{
				if (field.type.narrowerThan(accessSpecifier))
				{
					result = true;
					break;
				}
			}
		}
		
		inNarrower = false;
		return result;
	}
	
	@Override
	public TypeList getComposeTypes()
	{
		if (composed)
		{
			TypeList types = new TypeList(this);

			for (Field f: fields)
			{
				types.addAll(f.type.getComposeTypes());
			}
			
			return types;
		}
		else
		{
			return new TypeList();
		}
	}
}
