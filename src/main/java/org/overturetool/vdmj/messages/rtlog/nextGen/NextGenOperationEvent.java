package org.overturetool.vdmj.messages.rtlog.nextGen;

import java.io.Serializable;

public class NextGenOperationEvent implements Serializable, INextGenEvent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2328244727611734686L;

	public enum OperationEventType
	{
		REQUEST,
		ACTIVATE,
		COMPLETE
	}
	
	public NextGenThread thread;
	public String name;
	public long time;
	public NextGenOperation operation;
	public NextGenObject object;
	public OperationEventType type;
	
	public NextGenOperationEvent(NextGenThread thread, String name, long time, NextGenOperation operation, NextGenObject object, OperationEventType type) 
	{
		this.thread = thread;
		this.name = name;
		this.time = time;
		this.operation = operation;
		this.object = object;
		this.type = type;
	}
	
	@Override
	public String toString() 
	{
		StringBuffer s = new StringBuffer();
		
		s.append("Op");
		
		switch (type) {
		case REQUEST:
			s.append("Request");
			break;
		case ACTIVATE:
			s.append("Activate");
			break;
		case COMPLETE:
			s.append("Complete");			
		default:
			break;
		}
		
		s.append(" -> id: "); s.append(this.thread.id); 
		s.append(" opname: \""); s.append(this.name); s.append("\"");
		s.append(" objref: "); s.append(this.object==null ? "noref" : this.object.id );
		s.append(" clnm: \"" ); s.append(this.operation.classDef.name); s.append("\""); 
		s.append(" cpunm: "); s.append(this.object==null ? "0" : this.object.cpu.id);
		s.append(" async: "); s.append(this.operation.isAsync);
		s.append(" time: "); s.append(this.time);
		
		return s.toString();
	}

	public long getTime() {
		return time;
	}
	
}
