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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.messages.InternalException;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.scheduler.ControlQueue;
import org.overturetool.vdmj.types.ClassType;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.TypeList;
import org.overturetool.vdmj.util.Utils;


public class ObjectValue extends Value
{
	private static final long serialVersionUID = 1L;

	private static int nextObjectReference = 0;

	public final int objectReference;
	public final ClassType type;
	public final NameValuePairMap members;
	public final List<ObjectValue> superobjects;
	public final List<ControlQueue> guardCQs;

	private CPUValue CPU;
	private Object delegateObject = null;

	public ObjectValue(ClassType type,
		NameValuePairMap members, List<ObjectValue> superobjects, CPUValue cpu)
	{
		this.objectReference = getReference();
		this.type = type;
		this.members = members;
		this.superobjects = superobjects;
		this.CPU = cpu;

		if (Settings.dialect != Dialect.VDM_SL)
		{
			this.guardCQs = new LinkedList<ControlQueue>();
		}
		else
		{
			this.guardCQs = null;
		}

		setSelf(this);
	}

	private static synchronized int getReference()
	{
		return ++nextObjectReference;
	}

	private void setSelf(ObjectValue self)
	{
		for (NameValuePair nvp: members.asList())
 		{
			Value deref = nvp.value.deref();

 			if (deref instanceof OperationValue)
 			{
 				OperationValue ov = (OperationValue)deref;
 				ov.setSelf(self);
 			}
 			else if (deref instanceof FunctionValue)
 			{
 				FunctionValue fv = (FunctionValue)deref;
 				fv.setSelf(self);
 			}
 		}

		for (ObjectValue obj: superobjects)
		{
			obj.setSelf(self);
		}
	}

	@Override
	public ObjectValue objectValue(Context ctxt)
	{
		return this;
	}

	public TypeList getBaseTypes()
	{
		TypeList basetypes = new TypeList();

		if (superobjects.isEmpty())
		{
			basetypes.add(type);
		}
		else
		{
    		for (ObjectValue sup: superobjects)
    		{
    			basetypes.addAll(sup.getBaseTypes());
    		}
		}

		return basetypes;
	}

	@Override
	public Value getUpdatable(ValueListenerList listeners)
	{
		// Object values are not updatable unless their members are explicitly.
		// So this always just returns "this" wrapped in an UpdatableValue, so
		// that objects can be updated as a whole, rather than recursing into
		// the member list.

		return UpdatableValue.factory(this, listeners);
	}

	public OperationValue getThreadOperation(Context ctxt) throws ValueException
	{
		return get(type.classdef.name.getThreadName(), false).operationValue(ctxt);
	}

	public synchronized Value get(LexNameToken field, boolean explicit)
	{
		LexNameToken localname =
			explicit ? field : field.getModifiedName(type.name.name);

		// This is another case where we have to iterate with equals()
		// rather than using the map's hash, because the hash doesn't
		// take account of the TypeComparator looseness when comparing
		// qualified names. Not very efficient... so we try a raw get
		// first.

		Value rv = members.get(localname);

		if (rv == null)
		{
    		for (LexNameToken var: members.keySet())
    		{
    			if (var.equals(localname))
    			{
    				rv = members.get(var);
    				break;
    			}
    		}
		}

		if (rv != null)
		{
			return rv;
		}

		for (ObjectValue svalue: superobjects)
		{
			rv = svalue.get(field, explicit);

			if (rv != null)
			{
				return rv;
			}
		}

		return null;
	}

	public synchronized ValueList getOverloads(LexNameToken field)
	{
		ValueList list = new ValueList();

		// This is another case where we have to iterate with matches()
		// rather than using the map's hash, because the hash includes the
		// overloaded type qualifiers...

		for (LexNameToken var: members.keySet())
		{
			if (var.matches(field))		// Ignore type qualifiers
			{
				list.add(members.get(var));
			}
		}

		if (!list.isEmpty())
		{
			return list;	// Only names from one level
		}

		for (ObjectValue svalue: superobjects)
		{
			list = svalue.getOverloads(field);

			if (!list.isEmpty())
			{
				return list;
			}
		}

		return list;
	}

	public NameValuePairMap getMemberValues()
	{
		NameValuePairMap nvpm = new NameValuePairMap();

		for (ObjectValue svalue: superobjects)
		{
			nvpm.putAll(svalue.getMemberValues());
		}

		nvpm.putAll(members);
		return nvpm;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Value)
		{
			Value val = ((Value)other).deref();

    		if (val instanceof ObjectValue)
    		{
    			return val == this;		// Direct object comparison?
    		}
		}

		return false;
	}

	private boolean inToString = false;

	@Override
	public String toString()
	{
		if (inToString)
		{
			return "{#" + objectReference + " recursive}";
		}

		inToString = true;
		StringBuilder sb = new StringBuilder();
		sb.append(type.toString());
		sb.append("{#" + objectReference);

		for (LexNameToken name: members.keySet())
		{
			Value ov = members.get(name);
			Value v = ov.deref();

			if (!(v instanceof FunctionValue) &&
				!(v instanceof OperationValue))
			{
				sb.append(", ");
				sb.append(name.name);

				if (ov instanceof UpdatableValue)
				{
					sb.append(":=");
				}
				else
				{
					sb.append("=");
				}

				sb.append(v.toString());
			}
		}

		if (!superobjects.isEmpty())
		{
			sb.append(", ");
			sb.append(Utils.listToString(superobjects));
		}

		sb.append("}");
		inToString = false;
		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		// return type.hashCode() + members.hashCode() + superobjects.hashCode();
		return type.hashCode() + objectReference + superobjects.hashCode();
	}

	@Override
	public String kind()
	{
		return type.toString();
	}

	@Override
	public Value convertValueTo(Type to, Context ctxt) throws ValueException
	{
		Value conv = convertToHierarchy(to);

		if (conv != null)
		{
			return conv;
		}

		// This will fail...
		return super.convertValueTo(to, ctxt);
	}

	private Value convertToHierarchy(Type to)
	{
		if (to.equals(type))
		{
			return this;
		}

		for (ObjectValue svalue: superobjects)
		{
			Value conv = svalue.convertToHierarchy(to);

			if (conv != null)
			{
				return this;	// NB. not the subtype
			}
		}

		return null;
	}

	@Override
	public Object clone()
	{
		return deepCopy(); 	// shallowCopy(); ?
	}

	private ObjectValue mycopy = null;

	@Override
	public ObjectValue shallowCopy()
	{
		mycopy = new ObjectValue(type,
					new NameValuePairMap(), new Vector<ObjectValue>(), CPU);

		List<ObjectValue> supers = mycopy.superobjects;
		NameValuePairMap memcopy = mycopy.members;

   		for (ObjectValue sobj: superobjects)
   		{
   			supers.add(	// Type skeleton only...
   				new ObjectValue(sobj.type,
   					new NameValuePairMap(), new Vector<ObjectValue>(), sobj.CPU));
   		}

		for (LexNameToken name: members.keySet())
		{
			Value mv = members.get(name);

			if (mv.deref() instanceof ObjectValue)
			{
				ObjectValue om = (ObjectValue)mv.deref();

				if (om.mycopy != null)	// Currently copying it
				{
					memcopy.put(name, om.mycopy);
				}
				else
				{
					memcopy.put(name, om.shallowCopy());
				}
			}
			else
			{
				memcopy.put(name, (Value)mv.clone());
			}
		}

		ObjectValue rv = mycopy;
		mycopy = null;
		return rv;
	}

	@Override
	public ObjectValue deepCopy()
	{
		try
		{
			// This is slow, but it has the advantage that Value copies,
			// such as the parent and subclass copies of the same
			// variable, are preserved as the same variable rather than
			// being split, as they are in naive object copies.

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(this);
			oos.close();

			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(is);
			return (ObjectValue)ois.readObject();
		}
		catch (Exception e)
		{
			throw new InternalException(5, "Illegal clone: " + e);
		}
	}

	public synchronized void setCPU(CPUValue cpu)
	{
		CPU = cpu;
	}

	public synchronized CPUValue getCPU()
	{
		return CPU == null ? CPUValue.vCPU : CPU;
	}

	public boolean hasDelegate()
	{
		if (type.classdef.hasDelegate())
		{
			if (delegateObject == null)
			{
				delegateObject = type.classdef.newInstance();
			}

			return true;
		}

		return false;
	}

	public Value invokeDelegate(Context ctxt)
	{
		return type.classdef.invokeDelegate(delegateObject, ctxt);
	}
}
