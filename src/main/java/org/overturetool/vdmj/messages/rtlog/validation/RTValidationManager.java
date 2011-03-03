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
		IPropertyTest initializer = new OperationPropertyTest("AdjustVolume", "Radio", MessageType.Completed);
		IPropertyTest ender = new OperationPropertyTest("UpdateScreen", "MMI", MessageType.Completed);
		
		DeadlineMet dm = new DeadlineMet(initializer, ender, 1000000000);
		Separate sp = new Separate(initializer, ender, 1000000000);
		
		this.conjectures.add(dm);
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
