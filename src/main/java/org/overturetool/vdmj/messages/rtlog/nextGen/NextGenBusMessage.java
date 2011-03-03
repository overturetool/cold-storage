package org.overturetool.vdmj.messages.rtlog.nextGen;

import java.io.Serializable;
import java.util.ArrayList;

public class NextGenBusMessage implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7182675219414522466L;
	public Long id;
	public NextGenBus bus;
	public NextGenCpu fromCpu;
	public NextGenCpu toCpu;
	public NextGenThread callerThread;
	public NextGenOperation operation;
	public Integer size;
	public NextGenObject object;
	private ArrayList<NextGenBusMessageEvent> events = new ArrayList<NextGenBusMessageEvent>();
	
	
	
	public NextGenBusMessage(Long id, NextGenBus bus, NextGenCpu fromCpu, NextGenCpu toCpu,
			NextGenThread callerThread, NextGenOperation operation, int size, NextGenObject object) 
	{
		this.id = id;
		this.bus = bus;
		this.fromCpu = fromCpu;
		this.toCpu = toCpu;
		this.callerThread = callerThread;
		this.operation = operation;
		this.size = size;
		this.object = object;
	}
	
	public void addEvent(NextGenBusMessageEvent e)
	{
		this.events.add(e);
	}
	
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append("BusMessage -> ");
		s.append("id: "); s.append(this.id);
		s.append(" bus: "); s.append(this.bus.id);
		s.append(" fromCpu: "); s.append(this.fromCpu.id);
		s.append(" toCpu: "); s.append(this.toCpu.id);
		s.append(" callerThread: "); s.append(this.callerThread.id);
		s.append(" operation: "); s.append(this.operation!= null ? this.operation.name : "no op");
		s.append(" size: "); s.append(this.size);
		s.append(" object: "); s.append(this.object!=null ? this.object.id : "no object");
				
		return s.toString();
	}

}
