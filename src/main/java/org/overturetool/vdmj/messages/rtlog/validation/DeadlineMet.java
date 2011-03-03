package org.overturetool.vdmj.messages.rtlog.validation;

public class DeadlineMet extends Conjecture {

	public DeadlineMet(IPropertyTest initializer, IPropertyTest ender, int time) {
		super(initializer, ender, time);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean validate(ConjectureInstance instance) throws Exception {
		
		if(instance.hasEnded())
		{
		
		long timePassed = instance.getEndTime() - instance.getTriggerTime();
		return timePassed <= time;
		}
		throw new Exception("didnt end");
		
		
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("deadlineMet(");
		s.append(super.toString());
		s.append(")");
		return s.toString();
	}
	
}
