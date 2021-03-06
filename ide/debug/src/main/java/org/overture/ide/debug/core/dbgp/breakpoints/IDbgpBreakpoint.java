/*******************************************************************************
 * Copyright (c) 2009, 2011 Overture Team and others.
 *
 * Overture is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Overture is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Overture.  If not, see <http://www.gnu.org/licenses/>.
 * 	
 * The Overture Tool web-site: http://overturetool.org/
 *******************************************************************************/
package org.overture.ide.debug.core.dbgp.breakpoints;

public interface IDbgpBreakpoint {
	final int HIT_CONDITION_GREATER_OR_EQUAL = 0;

	final int HIT_CONDITION_EQUAL = 1;

	final int HIT_CONDITION_MULTIPLE = 2;
	
	final int HIT_NOT_SET = -1;

	String getId();

	boolean isEnabled();

	// -1 if not available
	int getHitCount();

	// -1 if not set
	int getHitValue();

	// -1 if not set
	int getHitCondition();
}
