package org.overturetool.vdmj.messages.rtlog.validation;


public class ConjectureInstance{
	
	private boolean ended = false;
	private long triggerTime;
	private long triggerThread;
	private int triggerObject;
	private long endTime;
	
	
	public ConjectureInstance(long wallTime, long threadid, int objectReference) {
		this.triggerTime = wallTime;
		this.triggerThread = threadid;
		this.triggerObject = objectReference;
	}

	
					
	public boolean hasEnded()
	{
		return ended;
	}
	
	public long getTriggerTime()
	{
		return triggerTime;
	}
	
	public void setEndTime(long time)
	{
		endTime = time;
		ended = true;
	}
	
	public long getEndTime()
	{
		if(ended)
		{
			return endTime;
		}
		else
		{
			return -1;
		}
	}



	public boolean matches(long threadid, int objectReference) {
		return threadid == triggerThread && triggerObject == objectReference;
	}
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Conjuncture Not Verified ->");
		s.append(" threadid: "); s.append(triggerThread);
		s.append(" objectid: "); s.append(triggerObject);
		s.append(" time: "); s.append(triggerTime);
		s.append(" ended: "); s.append(ended);
		
		
		return s.toString();
	}
}
