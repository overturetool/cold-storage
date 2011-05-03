package org.overturetool.vdmj.messages.rtlog.validation;

import java.util.ArrayList;
import java.util.List;


import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.messages.rtlog.RTMessage.MessageType;
import org.overturetool.vdmj.messages.rtlog.validation.definitions.ConjectureDefinition;
import org.overturetool.vdmj.messages.rtlog.validation.definitions.DeadlineMet;
import org.overturetool.vdmj.messages.rtlog.validation.definitions.OperationValidationExpression;
import org.overturetool.vdmj.messages.rtlog.validation.definitions.Separate;
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
		// C1 conjuction
		OperationValidationExpression c1init = new OperationValidationExpression("AdjustVolumeUp",  "Radio",  MessageType.Completed);
		OperationValidationExpression c1ender = new OperationValidationExpression("UpdateScreen",  "MMI",  MessageType.Completed);
		
		
		DeadlineMet c1 = new DeadlineMet("C1",c1init,null, c1ender, 35000000);
		
		
		// C2 conjuction
		OperationValidationExpression c2init = new OperationValidationExpression("UpdateScreen",  "MMI",  MessageType.Completed);
		OperationValidationExpression c2end = new OperationValidationExpression("UpdateScreen",  "MMI",  MessageType.Completed);
		
		Separate c2 = new Separate("C2",c2init, null, c2end, 500000000);
		
		
		// C3 
		OperationValidationExpression c3init = new OperationValidationExpression("HandleKeyPressUp", "MMI", MessageType.Activate);
		OperationValidationExpression c3end = new OperationValidationExpression("AdjustVolumeUp", "Radio", MessageType.Completed);
		ValueValidationExpression c3value = new ValueValidationExpression(new String[]{"RadNavSys","radio","volume"}, BinaryOps.LESS , new String[]{"RadNavSys","radio","max"} );
		
		
		DeadlineMet c3 = new DeadlineMet("C3",c3init, c3value, c3end, 100000000);
		
		//Separate sp = new Separate(initializer, ender, 1000000000);
		
		this.conjectures.add(c1);
		this.conjectures.add(c2);
		this.conjectures.add(c3);
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
	
	public void printLogFormat()
	{
		for (ConjectureDefinition c : conjectures) {
			c.printLogFormat();
		}
	}
	
	
}
