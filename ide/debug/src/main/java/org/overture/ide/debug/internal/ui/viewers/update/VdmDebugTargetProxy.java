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
package org.overture.ide.debug.internal.ui.viewers.update;

import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.internal.ui.viewers.update.DebugEventHandler;
import org.eclipse.debug.internal.ui.viewers.update.DebugTargetEventHandler;
import org.eclipse.debug.internal.ui.viewers.update.DebugTargetProxy;
import org.eclipse.debug.internal.ui.viewers.update.StackFrameEventHandler;
import org.eclipse.debug.internal.ui.viewers.update.ThreadEventHandler;

@SuppressWarnings("restriction")
public class VdmDebugTargetProxy extends DebugTargetProxy
{

	public VdmDebugTargetProxy(IDebugTarget target)
	{
		super(target);
	}
	
	@Override
	protected DebugEventHandler[] createEventHandlers()
	{
		 ThreadEventHandler threadEventHandler = new VdmThreadEventHandler(this);
			return new DebugEventHandler[] { new DebugTargetEventHandler(this), threadEventHandler,
					new StackFrameEventHandler(this, threadEventHandler) };
	}

}
