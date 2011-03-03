package org.overturetool.vdmj.messages.rtlog;

import org.overturetool.vdmj.scheduler.MessagePacket;

public abstract class RTBusMessage extends RTMessage
{
	public MessagePacket message;

	public RTBusMessage(MessagePacket message)
	{
		this.message = message;
	}
	
}
