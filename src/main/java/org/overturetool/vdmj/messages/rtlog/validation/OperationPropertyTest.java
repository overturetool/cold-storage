package org.overturetool.vdmj.messages.rtlog.validation;

import org.overturetool.vdmj.messages.rtlog.RTMessage.MessageType;
import org.overturetool.vdmj.messages.rtlog.nextGen.INextGenEvent;
import org.overturetool.vdmj.messages.rtlog.nextGen.NextGenOperationEvent;
import org.overturetool.vdmj.messages.rtlog.nextGen.NextGenOperationEvent.OperationEventType;

public class OperationPropertyTest implements IPropertyTest {

	
	private String className;
	private String opName;
	private MessageType type;
	
	public OperationPropertyTest(String opName, String className, MessageType type) {
		this.className = className;
		this.opName = opName;
		this.type = type;
	}
	
	public boolean validate(INextGenEvent e) 
	{				
		if(e instanceof NextGenOperationEvent)
		{
			NextGenOperationEvent event = (NextGenOperationEvent) e;
			if( event.operation.name.equals(opName) 
					&& event.operation.classDef.name.equals(className) 
					&& event.type.equals(type))
			{
				return true;
			}
			else
			{
				return false;
			}			
		}
		else
		{
			return false;
		}				
	}

	public boolean associatedWith(String opname, String classdef) {
		return opname.equals(this.opName) && classdef.equals(this.className);
	}

	public boolean matches(String opname, String classname, MessageType kind) {
		return opname.equals(this.opName) && classname.equals(this.className) && kind.equals(this.type);
	}
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		switch (type) {
		case Activate:
			s.append("#act");
			break;
		case Completed:
			s.append("#fin");
			break;
		case Request:
			s.append("#req");
			break;
		default:
			break;
		}
		s.append("(");
		s.append(className); s.append("`"); s.append(opName);
		s.append(")");
		
		return s.toString();
	}
	
}
