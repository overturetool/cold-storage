package org.overturetool.vdmj.messages.rtlog.validation;

public class SeparateConjecture extends Conjecture {

	public SeparateConjecture(IPropertyTest initializer, IPropertyTest ender,
			int time) {
		super(initializer, ender, time);		
	}

	@Override
	protected boolean validate(ConjectureInstance instance) throws Exception {		
		
		if(instance.hasEnded())
		{
		long timePassed = instance.getEndTime() - instance.getTriggerTime(); 		
		return timePassed >= time;
		}
		throw new Exception("didnt end");
		
	}

}
