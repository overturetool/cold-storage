package org.overturetool.vdmj.messages.rtlog.validation;

import java.util.ArrayList;
import java.util.List;


import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.messages.rtlog.RTMessage.MessageType;


public class RTValidationManager {
	
	
	private static RTValidationManager instance = null;
	
	public static synchronized RTValidationManager getInstance()
	{
		if(instance==null)
		{
			instance = new RTValidationManager();
			return instance;
		}
		else
		{
			return instance;
		}
	}

	private List<Conjecture> conjectures = null;
	
	public RTValidationManager() 
	{
		this.conjectures = new ArrayList<Conjecture>();
	}
	
	public void addTestConjectures()
	{
		IPropertyTest initializer = new OperationPropertyTest("UpdateAlgorithm", "HostController", MessageType.Request);
		IPropertyTest ender = new OperationPropertyTest("UpdateAlgorithm", "HostController", MessageType.Completed);
		
		DeadlineMet sp = new DeadlineMet(initializer, ender, 50);
		this.conjectures.add(sp);
	}
	
	public void addConjectures(Conjecture conj)
	{
		this.conjectures.add(conj);
	}
	
	public List<Conjecture> getAssociatedConjectures(LexNameToken name,
			ClassDefinition classdef) 
			{
		
		List<Conjecture> res = new ArrayList<Conjecture>();
		
		for (Conjecture conj : conjectures) 
		{
			if(conj.associatedWith(classdef.getName(),name.name))
			{
				res.add(conj);
			}
		}
		
		return res;
	}
	
	
	
	
}
