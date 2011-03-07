package org.overturetool.vdmj.messages.rtlog.validation.definitions;

import java.util.ArrayList;
import java.util.List;

import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.runtime.Context;
import org.overturetool.vdmj.values.UpdatableValue;
import org.overturetool.vdmj.values.Value;
import org.overturetool.vdmj.values.ValueListener;
import org.overturetool.vdmj.values.ValueListenerList;

public class ValueValidationExpression implements IValidationExpression, ValueListener {

	private String[] leftName;
	private String[] rightName;
	private String binaryOp;
	public Value leftValue;
	public Value rightValue;
	
	public ValueValidationExpression(String[] leftName, String binaryOp, String[] rightName) {
		this.leftName = leftName;
		this.binaryOp = binaryOp;
		this.rightName = rightName;
	}
		
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append("ValueValidationExpression: ");
		s.append(this.leftName);
		s.append(" ");
		s.append(this.binaryOp);
		s.append(" ");
		s.append(this.rightName);
		
		return s.toString();
	}


	public List<String[]> getMonitoredValues()
	{
		ArrayList<String[]> res = new ArrayList<String[]>();
		res.add(leftName);
		res.add(rightName);
		return res;
				
	}
	
	
	public boolean isAssociatedWith(LexLocation name) {
		
		return false;
	}


	public boolean isTrue() {
		System.out.println("ValueValidationExpression: Testing if expression is true");
		return false;
	}


	public void associateVariable(String[] strings, Value v) {
		if(v instanceof UpdatableValue)
		{
			UpdatableValue uv = (UpdatableValue) v;
			if(uv.listeners != null)
			{
				uv.listeners.add((ValueListener) this);
			}
			else
			{
				uv.listeners = new ValueListenerList(this);
			}
		}
		
	}

//-h localhost -p 10000 -k 1 -w -q -vdmrt -r classic -c MacRoman -e64 bmV3IFdvcmxkKCkuUnVuU2NlbmFyaW8xKCk= -default64 V29ybGQ= -consoleName LaunchConfigurationExpression -log file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/generated/logs/New_configuration/2011_03_07_23_38_20.logrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/EnvTask.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/InsertAdress.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/Test.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/TransmitTMC.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/VolumeKnob.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/World.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/lib/IO.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/lib/MATH.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/lib/VDMUtil.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/mmi.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/navigation.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/radhavsys.vdmrt file:/Users/ari/Documents/work/vdmWorkspace/CarNaviRadioRT/radio.vdmrt
	public boolean isValueMonitored(String[] strings) {
		return isStringsEqual(strings, leftName) || isStringsEqual(strings, rightName);
	}

	
	private boolean isStringsEqual(String[] strings1,String[] strings2)
	{
		if(strings1.length == strings2.length)
		{
			for (int i = 0; i < strings2.length; i++) {
				if(!strings1[i].equals(strings2[i]))
				{
					return false;
				}
			}
			return true;
		}
		else
		{
			return false;
		}
		
	}


	public void changedValue(LexLocation location, Value value, Context ctxt) {
		System.out.println(location + value.toString());
		
	}
}
