package org.overturetool.vdmj.messages.rtlog.nextGen;

import java.io.Serializable;
import java.util.ArrayList;

public class NextGenOperation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7326412415346555552L;

	public String name;
	public NextGenClassDefinition classDef;
	public boolean isAsync;
	public boolean isStatic;
	public ArrayList<NextGenOperationEvent> events = new ArrayList<NextGenOperationEvent>();
	
	
	
	public NextGenOperation(String name,
			NextGenClassDefinition classDef, boolean isAsync, boolean isStatic) 
	{		
		this.name = name;
		this.classDef = classDef;
		this.isAsync = isAsync;
		this.isStatic = isStatic;		
	}
	
	public void addEvent(NextGenOperationEvent e)
	{
		this.events.add(e);
	}
	
	@Override
	public String toString() 
	{
		StringBuffer s = new StringBuffer();
		
		s.append("Operation -> ");
		s.append("name: "); s.append(this.name);
		s.append(" clnm "); s.append(this.classDef.name);
		s.append(" isAsync: "); s.append(this.isAsync);
		s.append(" isStatic: "); s.append(this.isStatic);
			
		return s.toString();
	}

}
