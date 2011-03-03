package org.overturetool.vdmj.messages.rtlog;

import org.overturetool.vdmj.scheduler.CPUResource;
import org.overturetool.vdmj.values.OperationValue;

public class RTOperationMessage extends RTMessage
{
	public static boolean inSystemConstruction = true; //set to false in SystemDefinition when systemInit finishes
	
	public MessageType messageType;
	public OperationValue operationVal;
	public CPUResource from;
	public long threadId;
	public final boolean madeDuringSystemConstruction;
	public Integer objref;
	
	public RTOperationMessage(MessageType messageType, OperationValue operationVal, CPUResource from, long threadId)
	{
		this.messageType = messageType;
		this.operationVal = operationVal;
		this.from = from;
		this.threadId = threadId;
		this.madeDuringSystemConstruction = inSystemConstruction;
	}

	@Override
	String getInnerMessage()
	{
		if (!operationVal.isStatic)
		{
			return "Op"+messageType+" -> id: " + threadId +
			" opname: \"" + operationVal.name + "\"" +
			" objref: " + operationVal.getSelf().objectReference +
			" clnm: \"" + operationVal.getSelf().type.name.name + "\"" +
			" cpunm: " +  from.getNumber() +
			" async: " + operationVal.isAsync;
		}else
		{
			return "Op"+messageType+" -> id: " + threadId +
			" opname: \"" + operationVal.name + "\"" +
			//" objref: nil" +
			" objref: "+ objref +
			" clnm: \"" + operationVal.classdef.name.name + "\"" +
			" cpunm: " +  from.getNumber() +
			" async: " + operationVal.isAsync;
		}
	}
	
	@Override
	public void generateStaticDeploys()
	{
		if (operationVal.isStatic){
			objref = getStaticId(operationVal.classdef.name.name);
		}else
		{
			objref = Integer.valueOf(operationVal.getSelf().objectReference);
		}
	}
	
	public void updateCpu(CPUResource newCpu)
	{
		this.from = newCpu;
	}
	
	public CPUResource getCpu()
	{
		return this.from;
	}
	
	public Integer getObjRef()
	{
		return this.objref;
	}

}
