package org.overturetool.vdmj.messages.rtlog.validation;

import org.overturetool.vdmj.messages.rtlog.RTMessage.MessageType;
import org.overturetool.vdmj.messages.rtlog.nextGen.INextGenEvent;

public interface IPropertyTest {

	
	public boolean validate(INextGenEvent e);

	public boolean associatedWith(String opname, String classdef);

	public boolean matches(String opname, String classname, MessageType kind);	
	
}
