package org.overturetool.vdmj.messages.rtlog.validation;

import java.util.ArrayList;
import java.util.List;


import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.messages.rtlog.RTMessage.MessageType;
import org.overturetool.vdmj.messages.rtlog.validation.definitions.ConjectureDefinition;
import org.overturetool.vdmj.messages.rtlog.validation.definitions.DeadlineMet;
import org.overturetool.vdmj.messages.rtlog.validation.definitions.OperationValidationExpression;
import org.overturetool.vdmj.messages.rtlog.validation.definitions.ValueValidationExpression;
import org.overturetool.vdmj.messages.rtlog.validation.definitions.ValueValidationExpression.BinaryOps;
import org.overturetool.vdmj.values.NameValuePairList;
import org.overturetool.vdmj.values.Value;


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

	private List<ConjectureDefinition> conjectures = null;
	
	public RTValidationManager() 
	{
		this.conjectures = new ArrayList<ConjectureDefinition>();
	}
	
	public void addTestConjectures()
	{
		OperationValidationExpression init = new OperationValidationExpression("AdjustVolume",  "Radio",  MessageType.Completed);
		OperationValidationExpression ender = new OperationValidationExpression("UpdateScreen",  "MMI",  MessageType.Completed);
		ValueValidationExpression value = new ValueValidationExpression(new String[]{"RadNavSys","radio","volume"}, BinaryOps.LESS , new String[]{"RadNavSys","radio","max"} );
		
		DeadlineMet dm = new DeadlineMet(init,value, ender, 1000000000);
		
		//Separate sp = new Separate(initializer, ender, 1000000000);
		
		this.conjectures.add(dm);
		//this.conjectures.add(sp);
	}
	
	public void addConjectures(ConjectureDefinition conj)
	{
		this.conjectures.add(conj);
	}
	
	public List<ConjectureDefinition> getAssociatedConjectures(LexNameToken name,
			ClassDefinition classdef) 
			{
		
		List<ConjectureDefinition> res = new ArrayList<ConjectureDefinition>();
		
		for (ConjectureDefinition conj : conjectures) 
		{
			if(conj.associatedWith(classdef.getName(),name.name))
			{
				res.add(conj);
			}
		}
		
		return res;
	}

	public void areValuesAssociatedWithConjectures(NameValuePairList nvpl) {
		// TODO Auto-generated method stub
		
	}

	public List<String[]> getMonitoredValues() {
		List<String[]> res = new ArrayList<String[]>();
		
		for (ConjectureDefinition conjectureDefinition : conjectures) {
			res.addAll(conjectureDefinition.getMonitoredValues());
		}
		
		return res;
	}

	public void associateVariableWithRTValidator(String[] strings, Value v) {
		// TODO Auto-generated method stub
		
		for (ConjectureDefinition definition : conjectures) {
			definition.associateVariable(strings,v);
		}
		
	}
	
	
	
	
}
