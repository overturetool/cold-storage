/*******************************************************************************
 *
 *	This file is part of VDMJ.
 *
 *	VDMJ is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	VDMJ is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with VDMJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package org.overturetool.vdmj.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.overturetool.vdmj.values.BUSValue;

public class BUSConnection implements Comparator<BUSValue>
{
	 List<BUSValue> busses;
	 public BUSConnection(){
		 busses = new ArrayList<BUSValue> ();
	 }
	 
	 public void add(BUSValue bus)
	 {
		 if(!busses.contains(bus))
		 {
			 busses.add(bus);
			 
			 if(bus.resource.getSpeed() > busses.get(0).resource.getSpeed())
			 {
				 Collections.sort(busses, this);
			 }
		 }
	 }
	 
	 public void remove(BUSValue bus)
	 {
		 if(busses.contains(bus))
		 {
			 busses.remove(bus);
			 if(fastest() == bus)
			 {
				 Collections.sort(busses, this);
			 }
		 }
	 }
	 
	 public BUSValue fastest()
	 {
		 if(busses.isEmpty())
		 {
			 return null;
		 }
		 else 
		 {
			 return busses.get(0);
		 }
	 }

	 //highest first
	public int compare(BUSValue o1, BUSValue o2) {
		return (int) (o2.resource.getSpeed() - o1.resource.getSpeed());
	}
}