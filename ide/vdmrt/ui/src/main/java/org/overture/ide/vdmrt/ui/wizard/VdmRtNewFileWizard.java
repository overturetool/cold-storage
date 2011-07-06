package org.overture.ide.vdmrt.ui.wizard;

import org.overture.ide.ui.wizard.VdmNewFileWizard;

public class VdmRtNewFileWizard extends VdmNewFileWizard {

	@Override
	protected String getPageDescription() {		
		return "Chose new file name and location";
	}

	@Override
	protected String getPageName() {		
		return "VDM-RT New File Wizard";
	}

	@Override
	protected String getPageTitle() {		
		return "VDM-RT New File Wizard";
	}

	@Override
	protected String getFileExtension()
	{
		return "vdmrt";
	}

}