package org.overturetool.vdmj.messages.rtlog.nextGen;

import java.io.Serializable;

public class NextGenThreadEvent implements Serializable, INextGenEvent{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -170215356410239548L;

	public enum ThreadEventType
	{
		CREATE,
		SWAP,
		KILL		
	}
	
	public NextGenThread thread;
	public long time;
	public ThreadEventType type;
	
	public NextGenThreadEvent(NextGenThread thread, long time, ThreadEventType t) 
	{
		this.thread = thread;
		this.time = time;
		this.type = t;		
	}
	
	@Override
	public String toString() 
	{
		StringBuffer s = new StringBuffer();
		
		s.append("Thread");		
		switch (this.type) 
		{
		case CREATE:
			s.append("Create");
			s.append(" -> id:"); s.append(this.thread.id);
			s.append(" period: "); s.append(thread.periodic);
			s.append(" objref: "); s.append(this.thread.object==null ? "no object" : this.thread.object.id);
			s.append(" clnm: \""); s.append(this.thread.object==null ? "no object" : this.thread.object.classDef.name);	s.append("\"");	
			s.append(" cpunm: "); s.append(thread.object != null ? thread.object.cpu.id : "0");
			s.append(" time: " ); s.append(time);
			break;
		case KILL: 
			s.append("Kill");
			s.append(" -> id:"); s.append(this.thread.id);
			s.append(" time: " ); s.append(time);
			break;
		default:
			break;
		}
		
		
		
		return s.toString();
	}

	
	
	public long getTime() {		
		return time;
	}

}
