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
package org.overture.ide.vdmsl.ui.wizards;

import org.overture.ide.ui.wizard.VdmNewProjectWizard;
import org.overture.ide.vdmsl.core.IVdmSlCoreConstants;



public class VdmSlNewProjectWizard extends VdmNewProjectWizard {	

	@Override
	protected String getNature() {
		return IVdmSlCoreConstants.NATURE;
	}

	@Override
	protected String getPageDescription() {		
		return "Chose location for VDM-Sl project";
	}

	@Override
	protected String getPageName() {		
		return "VDM-Sl Project Location";
	}

	@Override
	protected String getPageTitle() {
		return "VDM-Sl Project";
	}

}
