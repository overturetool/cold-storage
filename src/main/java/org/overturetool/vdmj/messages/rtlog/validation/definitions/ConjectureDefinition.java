package org.overturetool.vdmj.messages.rtlog.validation.definitions;

import java.util.ArrayList;
import java.util.List;

import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.messages.rtlog.RTMessage.MessageType;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.scheduler.SystemClock;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueListener;

public abstract class ConjectureDefinition{

	public OperationValidationExpression opExpr;
	public ValueValidationExpression valueExpr;
	public IValidationExpression endingExpr;
	public int interval;
	private List<ConjectureValue> conjectureValues = new ArrayList<ConjectureValue>();
	public boolean startupValue;
	
	
	public ConjectureDefinition(OperationValidationExpression opExpr, 
			IValidationExpression endingExpr, 
			int interval) 
	{
		this.opExpr = opExpr;
		this.endingExpr = endingExpr;
		this.valueExpr = null;
		this.interval = interval;
	}
	
	public ConjectureDefinition(OperationValidationExpression opExpr, 
			ValueValidationExpression valueExpr,
			IValidationExpression endingExpr,
			int interval) 
	{
		this.opExpr = opExpr;
		this.endingExpr = endingExpr;
		this.valueExpr = valueExpr;
		this.interval = interval;
		
		if(valueExpr != null)
		{
			valueExpr.setConjecture(this);
		}
		
		if(endingExpr instanceof ValueValidationExpression)
		{
			((ValueValidationExpression)endingExpr).setConjecture(this);
		}
	}
	
	public ConjectureDefinition(ValueValidationExpression valueExpr,
			IValidationExpression endingExpr,
			int interval) 
	{
		this.opExpr = null;
		this.endingExpr = endingExpr;
		this.valueExpr = valueExpr;
		this.interval = interval;
	}
	
	public ConjectureDefinition(ConjectureDefinition c)
	{
		this.opExpr = c.opExpr;
		this.endingExpr = c.endingExpr;
		this.valueExpr = c.valueExpr;
		this.interval = c.interval;
	}
	
	
	
	abstract public boolean validate(long triggerTime, long endTime);

	public List<String[]> getMonitoredValues()
	{
		List<String[]> res = new ArrayList<String[]>();
		
		if(this.valueExpr!= null)
		{
			res.addAll(this.valueExpr.getMonitoredValues());
		}
		
		if(this.endingExpr instanceof ValueValidationExpression)
		{
			res.addAll(((ValueValidationExpression) this.endingExpr).getMonitoredValues());
		}
		
		return res;
	}

	
	
	public void process(String opname, String classname, MessageType kind,
			long wallTime, long id, int objectReference) {
		
		if(opExpr.matches(opname,classname,kind))
		{
			if(valueExpr != null)
			{
				if(valueExpr.isTrue())
				{
					conjectureValues.add(new ConjectureValue(this, wallTime));
				}
			}
			else
			{			
				conjectureValues.add(new ConjectureValue(this, wallTime));
			}
			
		}
		else
		{
			if(endingExpr instanceof OperationValidationExpression)
			{
				OperationValidationExpression ove = (OperationValidationExpression) endingExpr;
				
				if(ove.matches(opname, classname, kind))
				{
					for (ConjectureValue conj : conjectureValues) 
					{
						if(!conj.isEnded())
						{
							conj.setEnd(wallTime);
						}
					}
				}
			}
		}
		
	}

	public boolean associatedWith(String classname, String opname) {
		
		if(this.opExpr != null)
		{
			if(this.opExpr.isAssociatedWith(opname,classname))
			{
				return true;
			}
		}
		
		if(endingExpr instanceof OperationValidationExpression)
		{
			return ((OperationValidationExpression)this.endingExpr).isAssociatedWith(opname,classname);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		if(opExpr != null)
		{
			s.append(opExpr.toString()); s.append(",");	
		}
		
		if(valueExpr != null)
		{
			s.append(valueExpr.toString()); s.append(",");
		}
		
		s.append(endingExpr.toString()); s.append(",");
		s.append(interval);
		
		return s.toString();
	}

	public void associateVariable(String[] strings, Value v) {
		if(valueExpr != null)
		{
			if(valueExpr.isValueMonitored(strings)){
				valueExpr.associateVariable(strings,v);
			}
			
		}
		
		if(endingExpr instanceof ValueValidationExpression)
		{
			ValueValidationExpression vve = (ValueValidationExpression) endingExpr;
			if(vve.isValueMonitored(strings)){
				vve.associateVariable(strings,v);
			}
		}
		
	}

	public void valueChanged(ValueValidationExpression valueValidationExpression) {
		if(opExpr == null && valueValidationExpression == valueExpr)
		{
			if(valueExpr.isTrue())
			{
				conjectureValues.add(new ConjectureValue(this, SystemClock.getWallTime()));
			}
		}
		else
		{
			if(endingExpr instanceof ValueValidationExpression)
			{
				if(((ValueValidationExpression) endingExpr).isTrue())
				{
					for (ConjectureValue conj : conjectureValues) 
					{
						if(!conj.isEnded())
						{
							conj.setEnd(SystemClock.getWallTime());
						}
					}
				}
			}
		}
		
	}
	
	
	
}
