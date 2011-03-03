package org.overturetool.vdmj.messages.rtlog.validation;

import java.util.ArrayList;

import org.overturetool.vdmj.messages.rtlog.RTMessage.MessageType;
import org.overturetool.vdmj.messages.rtlog.nextGen.INextGenEvent;

public abstract class Conjecture {
	
	protected int time;
	private IPropertyTest initializer = null;
	private IPropertyTest ender = null;
	private ArrayList<ConjectureInstance> conjectureInstances = null;
		
	
	public Conjecture(IPropertyTest initializer, IPropertyTest ender, int time) 
	{
		this.initializer = initializer;
		this.ender = ender;
		this.time = time;
		this.conjectureInstances = new ArrayList<ConjectureInstance>();
	}

	
	
	protected abstract boolean validate(ConjectureInstance instance) throws Exception;
	protected abstract boolean getInitialState();

	public boolean associatedWith(String classdef , String opname ) {
		return initializer.associatedWith(opname, classdef) || ender.associatedWith(opname, classdef);
		
	}

	/**
	 * 
	 * @param opname
	 * @param classname
	 * @param kind 
	 * @param wallTime
	 * @param threadid
	 * @param objectReference
	 */
	public void process(String opname, String classname, MessageType kind, long wallTime, long threadid,
			int objectReference) 
	{
		
		if(initializer.matches(opname,classname,kind))
		{
			conjectureInstances.add(new ConjectureInstance(wallTime, threadid,this));
		}
		else
		{
			if(ender.matches(opname, classname, kind))
			{
				for (ConjectureInstance conjInstance : conjectureInstances) {
					if(!conjInstance.hasEnded())
					{						
						conjInstance.setEnding(wallTime,threadid);
						try {
							if(conjInstance.validate())
							{
								System.out.print("Conjecture validated: ");
								System.out.println(conjInstance.toString());								
								return;
							}
							else
							{
								System.out.print("Conjecture NOT validated: ");
								System.out.println(conjInstance.toString());								
								return;
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		
		s.append(initializer.toString()); s.append(",");
		s.append(ender.toString()); s.append(",");
		s.append(time);
		
		return s.toString();
	}

}
