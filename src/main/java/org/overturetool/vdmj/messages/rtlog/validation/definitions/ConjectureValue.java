package org.overturetool.vdmj.messages.rtlog.validation.definitions;


public class ConjectureValue {

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
		//printValidation();
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

	public long getEndTime()
	{
		return endTime;
	}
	
}
