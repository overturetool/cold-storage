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
package org.overture.ide.vdmpp.ui.wizard;

import org.overture.ide.ui.wizard.VdmNewFileWizard;

public class VdmPpNewClassWizard extends VdmNewFileWizard {

	@Override
	protected String getPageDescription() {		
		return "Chose new VDM++ file name and location";
	}

	@Override
	protected String getPageName() {		
		return "VDM++ New Class Wizard";
	}

	@Override
	protected String getPageTitle() {		
		return "VDM++ New Class Wizard";
	}

	@Override
	protected String getFileExtension()
	{
		return "vdmpp";
	}

	@Override
	protected String getFileTemplate(String fileName)
	{
		String className = fileName;
		return "class " + className + "\n" + "\ttypes\n-- TODO Define types here\n"
				+ "\tvalues\n-- TODO Define values here\n" + "\tinstance variables\n-- TODO Define instance variables here\n"
				+ "\toperations\n-- TODO Define operations here\n" + "\tfunctions\n-- TODO Define functiones here\n" 
				+ "\ttraces\n-- TODO Define Combinatorial Test Traces here\n" + "end "
				+ className;
	}
}
