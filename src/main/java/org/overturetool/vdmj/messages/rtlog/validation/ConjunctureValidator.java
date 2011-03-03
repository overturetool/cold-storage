package org.overturetool.vdmj.messages.rtlog.validation;

import java.util.List;

import org.overturetool.vdmj.messages.rtlog.nextGen.INextGenEvent;

public class ConjunctureValidator {

	
	private List<INextGenEvent> eventList = null;
	private Conjecture conj = null;
	
	public ConjunctureValidator(List<INextGenEvent> eventList, Conjecture conj) {
		 this.eventList = eventList;
		 this.conj = conj;
	}

	
	public void validate()
	{
		for (INextGenEvent e : eventList) {
			
			//conj.process(e);
			
		}		
		
	}
	
}
