package org.overturetool.vdmj.messages.rtlog.validation.definitions;

import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueListener;

public class ConjectureValue implements ValueListener {

	private ConjectureDefinition def;
	private long triggerTime;
	private boolean validated;
	
	private long endTime;
	private boolean isEnded = false;
	
	public ConjectureValue(ConjectureDefinition def, long triggerTime) 
	{
		this.def = def;
		this.triggerTime = triggerTime;
		this.validated = def.startupValue;
	}
	
	public void setEnd(long endTime)
	{
		this.isEnded = true;
		this.endTime = endTime;
		this.validated = this.def.validate(triggerTime, endTime);
		printValidation();
	}
	
	private void printValidation() 
	{
		System.out.print("Conjecture validated: ");
		System.out.print(this.validated + " ");
		System.out.println(def.toString());
	}

	public boolean isValidated()
	{
		return this.validated;
	}
	
	public boolean isEnded()
	{
		return this.isEnded;
	}

	public void changedValue(LexLocation location, Value value, Context ctxt) {
		// TODO Auto-generated method stub
		
	}
	
	
}
