package org.overturetool.vdmj.messages.rtlog.validation;


public class ConjectureInstance{
	
	private boolean ended = false;
	private long triggerTime;
	private long triggerThread;
	private long endingThread = -1;
	private long endTime;
	private Conjecture conjecture;
	private boolean validated = false;
	
	
	public ConjectureInstance(long wallTime, long threadid, Conjecture conjecture) {
		this.triggerTime = wallTime;
		this.triggerThread = threadid;		
		this.conjecture = conjecture;
		this.validated = conjecture.getInitialState();
	}

	
					
	public boolean hasEnded()
	{
		return ended;
	}
	
	public long getTriggerTime()
	{
		return triggerTime;
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

	
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Conjencture -> "); s.append(conjecture.toString()); 
		s.append(" startThreadId: "); s.append(triggerThread);		
		s.append(" startTime: "); s.append(triggerTime);
		s.append(" ended: "); s.append(ended);
		s.append(" endTime: "); s.append(endTime);
		s.append(" endThreadId: "); s.append(endingThread);
		
		
		return s.toString();
	}



	public void setEnding(long wallTime, long threadid) {
		endTime = wallTime;
		endingThread = threadid;
		ended = true;
		
	}



	public boolean validate() {
		try {
			validated = conjecture.validate(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return validated;
	}
}
