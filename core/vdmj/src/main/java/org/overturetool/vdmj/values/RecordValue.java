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

package org.overturetool.vdmj.values;

import java.util.Iterator;

import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.types.Field;
import org.overturetool.vdmj.types.RecordType;
import org.overturetool.vdmj.types.Type;


public class RecordValue extends Value
{
	private static final long serialVersionUID = 1L;
	public final RecordType type;
	public final FieldMap fieldmap;
	public final FunctionValue invariant;

	// mk_ expressions
	public RecordValue(RecordType type,	ValueList values, Context ctxt)
		throws ValueException
	{
		this.type = type;
		this.fieldmap = new FieldMap();
		this.invariant = type.getInvariant(ctxt);

		if (values.size() != type.fields.size())
		{
			abort(4078, "Wrong number of fields for " + type.name, ctxt);
		}

		Iterator<Field> fi = type.fields.iterator();

		for (Value v: values)
		{
			Field f = fi.next();
			fieldmap.add(f.tag, v.convertTo(f.type, ctxt), !f.equalityAbstration);
		}
		
		checkInvariant(ctxt);
	}

	// mu_ expressions
	public RecordValue(RecordType type,	FieldMap mapvalues, Context ctxt)
		throws ValueException
	{
		this.type = type;
		this.fieldmap = new FieldMap();
		this.invariant = type.getInvariant(ctxt);

		if (mapvalues.size() != type.fields.size())
		{
			abort(4080, "Wrong number of fields for " + type.name, ctxt);
		}

		Iterator<Field> fi = type.fields.iterator();

		while (fi.hasNext())
		{
			Field f = fi.next();
			Value v = mapvalues.get(f.tag);

			if (v == null)
			{
				abort(4081, "Field not defined: " + f.tag, ctxt);
			}

			fieldmap.add(f.tag, v.convertTo(f.type, ctxt), !f.equalityAbstration);
		}

		checkInvariant(ctxt);
	}

	// Only called by clone()
	private RecordValue(RecordType type, FieldMap mapvalues, FunctionValue invariant)
	{
		this.type = type;
		this.invariant = invariant;
		this.fieldmap = mapvalues;
	}

	// State records - invariant handled separately
	public RecordValue(RecordType type, NameValuePairList mapvalues)
	{
		this.type = type;
		this.invariant = null;
		this.fieldmap = new FieldMap();

		for (NameValuePair nvp: mapvalues)
		{
			Field f = type.findField(nvp.name.name);
			this.fieldmap.add(nvp.name.name, nvp.value, !f.equalityAbstration);
		}
	}

	public void checkInvariant(Context ctxt) throws ValueException
	{
		if (invariant != null && Settings.invchecks)
		{
			// In VDM++ and VDM-RT, we do not want to do thread swaps half way
			// through an invariant check, so we set the atomic flag around the
			// conversion. This also stops VDM-RT from performing "time step"
			// calculations.
	
			ctxt.threadState.setAtomic(true);
			boolean inv = invariant.eval(invariant.location, this, ctxt).boolValue(ctxt);
			ctxt.threadState.setAtomic(false);
	
			if (!inv)
			{
				abort(4079, "Type invariant violated by mk_ arguments", ctxt);
			}
		}
	}

	@Override
	public RecordValue recordValue(Context ctxt)
	{
		return this;
	}

	@Override
	public Value getUpdatable(ValueListenerList listeners)
	{
		InvariantValueListener invl = null;

		if (invariant != null)
		{
			// Add an invariant listener to a new list for children of this value
			// We update the object in the listener once we've created it (below)

			invl = new InvariantValueListener();
			ValueListenerList list = new ValueListenerList(invl);

			if (listeners != null)
			{
				list.addAll(listeners);
			}

			listeners = list;
		}

		FieldMap nm = new FieldMap();

		for (FieldValue fv: fieldmap)
		{
			Value uv = fv.value.getUpdatable(listeners);
			nm.add(fv.name, uv, fv.comparable);
		}

		UpdatableValue uval = UpdatableValue.factory(new RecordValue(type, nm, invariant), listeners);
		
		if (invl != null)
		{
			// Update the listener with the address of the updatable copy
			invl.setValue(uval);
		}

		return uval;
	}

	@Override
	public Value getConstant()
	{
		FieldMap nm = new FieldMap();

		for (FieldValue fv: fieldmap)
		{
			Value uv = fv.value.getConstant();
			nm.add(fv.name, uv, fv.comparable);
		}

		return new RecordValue(type, nm, invariant);
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Value)
		{
			return compareTo((Value)other) == 0;
		}
		else
		{
			return false;
		}
	}

	@Override
	public int compareTo(Value other)
	{
		Value val = other.deref();

		if (val instanceof RecordValue)
		{
			RecordValue ot = (RecordValue)val;

			if (ot.type.equals(type))
			{
				for (Field f: type.fields)
				{
					if (!f.equalityAbstration)
					{
						Value fv = fieldmap.get(f.tag);
						Value ofv = ot.fieldmap.get(f.tag);

						if (fv == null || ofv == null)
						{
							return -1;
						}

						int comp = fv.compareTo(ofv);

						if (comp != 0)
						{
							return comp;
						}
					}
				}

				return 0;
			}
		}

		return -1;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("mk_" + type.name + "(");

		Iterator<Field> fi = type.fields.iterator();

		if (fi.hasNext())
		{
    		String ftag = fi.next().tag;
    		sb.append(fieldmap.get(ftag));

    		while (fi.hasNext())
    		{
    			ftag = fi.next().tag;
    			sb.append(", " + fieldmap.get(ftag));
    		}
		}

		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		return type.name.hashCode() + fieldmap.hashCode();
	}

	@Override
	public String kind()
	{
		return type.toString();
	}

	@Override
	public Value convertValueTo(Type to, Context ctxt) throws ValueException
	{
		if (to.equals(type))
		{
			return this;
		}
		else
		{
			return super.convertValueTo(to, ctxt);
		}
	}

	@Override
	public Object clone()
	{
		return new RecordValue(type, (FieldMap)fieldmap.clone(), invariant);
	}
}
