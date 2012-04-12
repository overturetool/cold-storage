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

package org.overturetool.vdmj.definitions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.overturetool.vdmj.debug.DBGPReader;
import org.overturetool.vdmj.expressions.UndefinedExpression;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexException;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameList;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.messages.RTLogger;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.runtime.ContextException;
import org.overturetool.vdmj.runtime.Interpreter;
import org.overturetool.vdmj.runtime.StateContext;
import org.overturetool.vdmj.runtime.ValueException;
import org.overturetool.vdmj.scheduler.BUSResource;
import org.overturetool.vdmj.scheduler.BusThread;
import org.overturetool.vdmj.scheduler.ISchedulableThread;
import org.overturetool.vdmj.scheduler.ResourceScheduler;
import org.overturetool.vdmj.scheduler.SchedulingPolicy;
import org.overturetool.vdmj.syntax.DefinitionReader;
import org.overturetool.vdmj.syntax.ParserException;
import org.overturetool.vdmj.typechecker.Environment;
import org.overturetool.vdmj.types.ClassType;
import org.overturetool.vdmj.types.Type;
import org.overturetool.vdmj.types.UndefinedType;
import org.overturetool.vdmj.types.UnresolvedType;
import org.overturetool.vdmj.values.BUSValue;
import org.overturetool.vdmj.values.CPUValue;
import org.overturetool.vdmj.values.ObjectValue;
import org.overturetool.vdmj.values.QuoteValue;
import org.overturetool.vdmj.values.RealValue;
import org.overturetool.vdmj.values.SetValue;
import org.overturetool.vdmj.values.UpdatableValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueList;
import org.overturetool.vdmj.values.ValueSet;
import org.overturetool.vdmj.values.VoidValue;

public class SystemDefinition extends ClassDefinition
{
	private static final long serialVersionUID = 1L;

	private static Context systemContext = null;

	public static String systemClassName = "undefined";
	
	private static int addedConstituentCount = 1;
	private static int addedBusCount = 1;
	
	public SystemDefinition(LexNameToken className, DefinitionList members) throws ParserException, LexException
	{
		super(className, new LexNameList(), operationDefs(className.name ,members));
		systemClassName = className.name;
	}
	
	private static String defs =
		"operations " +
		"public static connect: Constituent * Channel ==> () " +
		"	connect(constituent, channel) == is not yet specified; " +
		"public static disconnect: Constituent * Channel ==> () " +
		"	disconnect(constituent, channel) == is not yet specified; " +
		"public static migrate: ? * ?  ==> () " +
		"	migrate(obj, cpu) == is not yet specified;" +
		"public static addConstituent : real ==> Constituent " +
			"addConstituent(speed) == is not yet specified;" +
		"public static removeConstituent : Constituent ==> () " +
			"removeConstituent(constituent) == is not yet specified;" +
		"public static addChannel : real * set of Constituent  ==> Channel " +
		"	addChannel(speed, constituents) == is not yet specified;" + 
		"public static removeChannel : Channel ==> () " +
		"	removeChannel(channel) == is not yet specified;";


	private static DefinitionList operationDefs(String className ,DefinitionList members)
	throws ParserException, LexException
	{
		LexTokenReader ltr = new LexTokenReader(defs, Dialect.VDM_PP);
		DefinitionReader dr = new DefinitionReader(ltr); 
		dr.setCurrentModule(className);
		members.addAll(dr.readDefinitions());
		return members;
	}
	
	@Override
	public void implicitDefinitions(Environment publicClasses)
	{
		super.implicitDefinitions(publicClasses);

		for (Definition d: definitions)
		{
			if (d instanceof InstanceVariableDefinition)
			{
				InstanceVariableDefinition iv = (InstanceVariableDefinition)d;

				if (iv.type instanceof UnresolvedType &&
					iv.expression instanceof UndefinedExpression)
				{
					UnresolvedType ut = (UnresolvedType)iv.type;

					if (ut.typename.getName().equals("BUS"))
					{
						d.warning(5014, "Uninitialized BUS ignored");
					}
				}
			}
			else if (d instanceof ExplicitOperationDefinition)
			{
				ExplicitOperationDefinition edef = (ExplicitOperationDefinition)d;

				if(edef.name.name.equals("connect") || edef.name.name.equals("disconnect") || 
						edef.name.name.equals("migrate") || edef.name.name.equals("addConstituent") || 
						edef.name.name.equals("removeConstituent") || edef.name.name.equals("addChannel") || 
						edef.name.name.equals("removeChannel"))
				{
					continue;
				}
				
				if (!edef.name.name.equals(name.name) ||
					!edef.parameterPatterns.isEmpty())
				{
					d.report(3285, "System class can only define a default constructor");
				}
			}
			else if (d instanceof ImplicitOperationDefinition)
			{
				ImplicitOperationDefinition idef = (ImplicitOperationDefinition)d;

				if (!d.name.name.equals(name.name))
				{
					d.report(3285, "System class can only define a default constructor");
				}

				if (idef.body == null)
				{
					d.report(3283, "System class constructor cannot be implicit");
				}
			}
			else
			{
				d.report(3284, "System class can only define instance variables and a constructor");
			}
		}
	}

	public void systemInit(ResourceScheduler scheduler, DBGPReader dbgp)
	{
		systemContext = new StateContext(location, "RT system environment");
		systemContext.setThreadState(dbgp, CPUValue.vCPU);

		try
		{
			// First go through the definitions, looking for CPUs to decl
			// before we can deploy to them in the constructor. We have to
			// predict the CPU numbers at this point.

			DefinitionList cpudefs = new DefinitionList();
			int cpuNumber = 1;
			CPUClassDefinition instance = null;

			for (Definition d: definitions)
			{
				Type t = d.getType();

				if (t instanceof ClassType)
				{
					InstanceVariableDefinition ivd = (InstanceVariableDefinition)d;
					ClassType ct = (ClassType)t;

					if (ct.classdef instanceof CPUClassDefinition)
					{
						cpudefs.add(d);
						instance = (CPUClassDefinition)ct.classdef;

	    				RTLogger.log(
	    					"CPUdecl -> id: " + (cpuNumber++) +
	    					" expl: " + !(ivd.expType instanceof UndefinedType) +
	    					" sys: \"" + name.name + "\"" +
	    					" name: \"" + d.name.name + "\"");
					}
				}
			}

			// Run the constructor to do any deploys etc.

			ObjectValue system = makeNewInstance(null, new ValueList(),
					systemContext, new HashMap<LexNameToken, ObjectValue>());

			// Do CPUs first so that default BUSses can connect all CPUs.

			ValueSet cpus = new ValueSet();

			for (Definition d: cpudefs)
			{
    			UpdatableValue v = (UpdatableValue)system.members.get(d.name);
    			CPUValue cpu = null;

    			if (v.isUndefined())
    			{
    				ValueList args = new ValueList();

    				args.add(new QuoteValue("FCFS"));	// Default policy
    				args.add(new RealValue(0));			// Default speed

    				cpu = (CPUValue)instance.newInstance(null, args, systemContext);
    				v.set(location, cpu, systemContext);
    			}
    			else
    			{
    				cpu = (CPUValue)v.deref();
    			}

    			// Set the name and scheduler for the CPU resource, and
    			// associate the resource with the scheduler.

    			cpu.setup(scheduler, d.name.name);
    			cpus.add(cpu);
			}

			// We can create vBUS now that all the CPUs have been created
			// This must be first, to ensure it's bus number 0.

			BUSValue.vBUS = BUSClassDefinition.makeVirtualBUS(cpus);
			BUSValue.vBUS.setup(scheduler, "vBUS");

			for (Definition d: definitions)
			{
				Type t = d.getType();

				if (t instanceof ClassType)
				{
					ClassType ct = (ClassType)t;

					if (ct.classdef instanceof BUSClassDefinition)
					{
						UpdatableValue v = (UpdatableValue)system.members.get(d.name);
	    				BUSValue bus = null;

						if (!v.isUndefined())
						{
							bus = (BUSValue)v.deref();

							// Set the name and scheduler for the BUS resource, and
							// associate the resource with the scheduler.

							bus.setup(scheduler, d.name.name);
						}
					}
				}
			}

			// For efficiency, we create a 2D array of CPU-to-CPU bus links
			BUSValue.createMap(systemContext, cpus);
		}
		catch (ContextException e)
		{
			throw e;
		}
		catch (ValueException e)
		{
			throw new ContextException(e, location);
		}
    	catch (Exception e)
    	{
    		throw new ContextException(
    			4135, "Cannot instantiate a system class", location, systemContext);
    	}
	}

	@Override
	public ObjectValue newInstance(
		Definition ctorDefinition, ValueList argvals, Context ctxt)
	{
		abort(4135, "Cannot instantiate system class " + name, ctxt);
		return null;
	}
	
	public static Value connect(Context ctxt)
	{
		try
		{
    		CPUValue cpu = (CPUValue)ctxt.lookup(new LexNameToken(systemClassName, "constituent", new LexLocation()));
    		BUSValue channel  = (BUSValue)ctxt.check(new LexNameToken(systemClassName, "channel", new LexLocation()));;
    		
    		if(channel.isTerminated())
    		{
    			throw new ContextException(4136, "Channel has been removed from the system.", ctxt.location, ctxt); //TODO error code?
    		}
    		
    		BUSValue.connectCPUToBUS(cpu, channel);

  			return new VoidValue();
		}
		catch (Exception e)
		{
			throw new ContextException(9999, "Cannot connect to Channel: " + e.toString(), ctxt.location, ctxt); //TODO error code?
		}
	}
	
	public static Value disconnect(Context ctxt)
	{
		CPUValue constituent = (CPUValue)ctxt.lookup(new LexNameToken(systemClassName, "constituent", new LexLocation()));
		BUSValue bus  = (BUSValue)ctxt.check(new LexNameToken(systemClassName, "channel", new LexLocation()));;
		
		if(bus.isTerminated())
		{
			throw new ContextException(4136, "Cannot disconnect from channel. Channel has been removed from the system.", ctxt.location, ctxt); //TODO error code?
		}
		BUSValue.disconnectCPUFromBUS(constituent.resource, bus);

		return new VoidValue();
	}
	
	public static Value addConstituent(Context ctxt)  {
		
		RealValue speed = (RealValue)ctxt.lookup(new LexNameToken(systemClassName, "speed", new LexLocation()));

		ValueList args = new ValueList();
		args.add(speed);		

		//create new cpu from ConstituentClassDefinition
		CPUValue newCPU = (CPUValue)ConstituentClassDefinition.instance.newInstance(null, args, systemContext);
		newCPU.setup(Interpreter.getInstance().scheduler, "dynamicConstituent" + addedConstituentCount++);
		
		//add to CPU map
		BUSValue.connectCPUToVirtualBUS(newCPU);
	
		return newCPU;
	}
	
	public static Value removeConstituent(Context ctxt) 
	{
		CPUValue constituent = (CPUValue)ctxt.check(new LexNameToken(systemClassName, "constituent", new LexLocation()));;
		
		if(constituent.isTerminated())
		{
			throw new ContextException(9999, "Cannot remove constituent. Constituent has already been removed from the system.", ctxt.location, ctxt); //TODO error code?
		}
		
		//stop execution
		constituent.terminate();
		
		//find all busses
		BUSValue.disconnectCPUFromAllBusses(constituent);
		
		return new VoidValue();
	}
	
	public static Value addChannel(Context ctxt) {
		
		RealValue speed = (RealValue)ctxt.lookup(new LexNameToken(systemClassName, "speed", new LexLocation()));
		SetValue constituents = (SetValue)ctxt.lookup(new LexNameToken(systemClassName, "constituents", new LexLocation()));
		
		ValueList args = new ValueList();
		args.add(speed);		
		args.add(constituents);

		BUSValue newBUS = (BUSValue)ChannelClassDefinition.instance.newInstance(null, args, systemContext);
		newBUS.setup(Interpreter.getInstance().scheduler, "dynamicBus" + addedBusCount++);
		
		//start bus
		new BusThread(newBUS.resource, 0).start();
		
		return newBUS;
	}
	
	public static Value removeChannel(Context ctxt) {
		
		BUSValue channel = (BUSValue)ctxt.lookup(new LexNameToken(systemClassName, "channel", new LexLocation()));
		
		if(channel.isTerminated())
		{
			throw new ContextException(9999, "Cannot remove channel. Channel has already been removed from the system.", ctxt.location, ctxt); //TODO error code?
		}
		
		//stop processing 
		channel.terminate();
		
		//get attached cpus
		BUSResource busRes = channel.resource;
		
		
		for (int i = 0; i < busRes.getCPUs().size(); i++) {
			BUSValue.disconnectCPUFromBUS(busRes.getCPUs().get(i), channel);
		}
		
//		for (Iterator<CPUResource> iterator = busRes.getCPUs().iterator(); iterator.hasNext();) {
//			BUSValue.disconnectCPUFromBUS(iterator.next(), channel);
//			iterator.remove();
//		}
		
//		for(CPUResource c : busRes.getCPUs()){	
//			BUSValue.disconnectCPUFromBUS(c, channel);
//		}
		
		return new VoidValue();
	}
	
	public static Value migrate(Context ctxt)
	{
		ObjectValue obj = (ObjectValue)ctxt.lookup(new LexNameToken(systemClassName, "obj", new LexLocation()));
		CPUValue cpuSource = obj.getCPU();
		CPUValue cpuTarget = (CPUValue)ctxt.check(new LexNameToken(systemClassName, "cpu", new LexLocation()));;
    		
		//ensure bus connection
		BUSValue bus = BUSValue.lookupBUS(cpuSource, cpuTarget);
		if (bus == null)
		{
			throw new ContextException(4140, "No BUS between CPUs " + cpuSource.getName() + " and " + cpuTarget.getName(), ctxt.location, ctxt);
		}
		
		//redeploy
		redeploy(obj, cpuTarget);

		//find the objects threads  
		SchedulingPolicy policy = cpuSource.resource.policy;
		
		//get all transitive objects for the migrating object
		List<ObjectValue> objectTree = new LinkedList<ObjectValue>(); 
		obj.getTransitiveReferences(objectTree); 

		List<ISchedulableThread> migratingThreads = policy.getThreadsFromObjects(objectTree);
		
		//and move
		for(ISchedulableThread migratingTh : migratingThreads)
		{
			long migratingPriority = policy.getPriority(migratingTh);
			
			cpuSource.resource.unregister(migratingTh);
			cpuTarget.resource.register(migratingTh, migratingPriority);
		}
		
		//redirect / forward messages
		BUSValue.migrateMessages(obj, bus);
		
		return new VoidValue();
	}

	public static void redeploy(ObjectValue obj, CPUValue cpuTarget) 
	{
		//relocate object to CPU
		obj.getCPU().undeploy(obj); 	//object will no longer be deployed on the old cpu
		obj.redeploy(cpuTarget);
		cpuTarget.deploy(obj);
	}
}
