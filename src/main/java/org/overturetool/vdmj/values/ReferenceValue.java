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

import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.types.Type;

abstract public class ReferenceValue extends Value
{
	private static final long serialVersionUID = 1L;
	public Value value;

	public ReferenceValue(Value value)
	{
		this.value = value.deref();		// In case we're reffing a ref
	}

	public ReferenceValue()
	{
		this.value = new UndefinedValue();
	}

	@Override
	public Value convertValueTo(Type to, Context ctxt) throws ValueException
	{
		return value.convertValueTo(to, ctxt);
	}

	@Override
	public boolean isType(Class<? extends Value> valueclass)
	{
		return valueclass.isInstance(this.value);
	}

	@Override
	public Value deref()
	{
		return value.deref();
	}

	@Override
	public boolean isUndefined()
	{
		return value.isUndefined();
	}

	@Override
	public boolean isVoid()
	{
		return value.isVoid();
	}

	@Override
	public double realValue(Context ctxt) throws ValueException
	{
		return value.realValue(ctxt);
	}

	@Override
	public long intValue(Context ctxt) throws ValueException
	{
		return value.intValue(ctxt);
	}

	@Override
	public long natValue(Context ctxt) throws ValueException
	{
		return value.nat1Value(ctxt);
	}

	@Override
	public long nat1Value(Context ctxt) throws ValueException
	{
		return value.nat1Value(ctxt);
	}

	@Override
	public boolean boolValue(Context ctxt) throws ValueException
	{
		return value.boolValue(ctxt);
	}

	@Override
	public char charValue(Context ctxt) throws ValueException
	{
		return value.charValue(ctxt);
	}

	@Override
	public ValueList tupleValue(Context ctxt) throws ValueException
	{
		return value.tupleValue(ctxt);
	}

	@Override
	public RecordValue recordValue(Context ctxt) throws ValueException
	{
		return value.recordValue(ctxt);
	}

	@Override
	public ObjectValue objectValue(Context ctxt) throws ValueException
	{
		return value.objectValue(ctxt);
	}

	@Override
	public String quoteValue(Context ctxt) throws ValueException
	{
		return value.quoteValue(ctxt);
	}

	@Override
	public ValueList seqValue(Context ctxt) throws ValueException
	{
		return value.seqValue(ctxt);
	}

	@Override
	public ValueSet setValue(Context ctxt) throws ValueException
	{
		return value.setValue(ctxt);
	}

	@Override
	public String stringValue(Context ctxt) throws ValueException
	{
		return value.stringValue(ctxt);
	}

	@Override
	public ValueMap mapValue(Context ctxt) throws ValueException
	{
		return value.mapValue(ctxt);
	}

	@Override
	public FunctionValue functionValue(Context ctxt) throws ValueException
	{
		return value.functionValue(ctxt);
	}

	@Override
	public OperationValue operationValue(Context ctxt) throws ValueException
	{
		return value.operationValue(ctxt);
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Value)
		{
			Value val = ((Value)other).deref();

    		if (val instanceof ReferenceValue)
    		{
    			ReferenceValue rvo = (ReferenceValue)val;
    			return value.equals(rvo.value);
    		}
    		else
    		{
    			return value.equals(other);
    		}
		}

		return false;
	}

	@Override
	public String kind()
	{
		return value.kind();
	}

	@Override
	public int hashCode()
	{
		return value.hashCode();
	}

	@Override
	public String toString()
	{
		return value.toString();
	}
}
