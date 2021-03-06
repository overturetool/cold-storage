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
package org.overture.ide.debug.core.model;

import org.overture.ide.debug.core.IDebugOptions;

public class DefaultDebugOptions implements IDebugOptions {

	private static IDebugOptions defaultInstance = null;

	public static IDebugOptions getDefaultInstance() {
		if (defaultInstance == null) {
			defaultInstance = new DefaultDebugOptions();
		}
		return defaultInstance;
	}

	protected DefaultDebugOptions() {
		// empty
	}

	public boolean get(BooleanOption option) {
		return option.getDefaultValue();
	}

	public int get(IntegerOption option) {
		return option.getDefaultValue();
	}

	public String get(StringOption option) {
		return option.getDefaultValue();
	}

	public IVdmStackFrame[] filterStackLevels(IVdmStackFrame[] frames) {
		return (IVdmStackFrame[]) frames.clone();
	}

	public boolean isValidStack(IVdmStackFrame[] frames) {
		return true;
	}

}
