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
package org.overture.ide.debug.core.dbgp.internal.breakpoints;

import org.overture.ide.debug.core.dbgp.breakpoints.IDbgpLineBreakpoint;

public class DbgpLineBreakpoint extends DbgpBreakpoint implements
		IDbgpLineBreakpoint {
	private final String fileName;

	private final int lineNumber;

	public DbgpLineBreakpoint(String id, boolean enabled, int hitValue,
			int hitCount, String hitCondition, String fileName, int lineNumber) {
		super(id, enabled, hitValue, hitCount, hitCondition);

		this.fileName = fileName;
		this.lineNumber = lineNumber;
	}

	public String getFilename() {
		return fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}
}
