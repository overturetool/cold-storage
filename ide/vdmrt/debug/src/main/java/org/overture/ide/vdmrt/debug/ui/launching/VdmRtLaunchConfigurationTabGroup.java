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
package org.overture.ide.vdmrt.debug.ui.launching;


import org.overture.ide.debug.ui.launching.AbstractVdmLaunchConfigurationTabGroup;
import org.overture.ide.debug.ui.launching.AbstractVdmMainLaunchConfigurationTab;
import org.overture.ide.debug.ui.launching.VdmRuntimeChecksLaunchConfigurationTab;

public class VdmRtLaunchConfigurationTabGroup extends
		AbstractVdmLaunchConfigurationTabGroup
{

	@Override
	protected AbstractVdmMainLaunchConfigurationTab getMainTab()
	{
		return new VdmRtMainLaunchConfigurationTab();
	}
	
	@Override
	protected VdmRuntimeChecksLaunchConfigurationTab getRuntimeTab()
	{
		return new VdmRtRuntimeChecksLaunchConfigurationTab();
	}

}
