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
package org.overture.ide.plugins.csk.internal;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.overture.ide.plugins.csk.Activator;
import org.overture.ide.plugins.csk.ICskConstants;

public class WorkbenchPreferencePageCsk extends PreferencePage
		implements IWorkbenchPreferencePage
{
	FileFieldEditor vdmPath = null;
	FileFieldEditor vppPath = null;
	FileFieldEditor vicePath = null;
	
	DirectoryFieldEditor vdmPathMac = null;
	DirectoryFieldEditor vppPathMac = null;
	DirectoryFieldEditor vicePathMac = null;
	
	
	@Override
	protected Control createContents(Composite parent) {
		Composite top = new Composite(parent, SWT.LEFT);

		// Sets the layout data for the top composite's 
		// place in its parent's layout.
		top.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Sets the layout for the top composite's 
		// children to populate.
		top.setLayout(new GridLayout());
			
		if(Platform.getOS().equalsIgnoreCase(Platform.OS_MACOSX)){
			vdmPathMac = new DirectoryFieldEditor(ICskConstants.VSLGDE_PATH, "Path to VDM Tools for VDM-SL (vdmgde):", top);
			vdmPathMac.setPreferencePage(this);
			vdmPathMac.setPreferenceStore(getPreferenceStore());
			vdmPathMac.load();
			
			vppPathMac = new DirectoryFieldEditor(ICskConstants.VPPGDE_PATH, "Path to VDM Tools for VDM-PP (vppgde):", top);
			vppPathMac.setPreferencePage(this);
			vppPathMac.setPreferenceStore(getPreferenceStore());
			vppPathMac.load();
			
			vicePathMac = new DirectoryFieldEditor(ICskConstants.VRTGDE_PATH, "Path to VDM Tools for VICE  (vicegde):", top);
			vicePathMac.setPreferencePage(this);
			vicePathMac.setPreferenceStore(getPreferenceStore());
			vicePathMac.load();
			Label listLabel = new Label(top, SWT.BOLD);
			listLabel.setText("NOTE: select the \"bin\" folder just above \"vxxgde\"");
		}
		else
		{
			vdmPath = new FileFieldEditor(ICskConstants.VSLGDE_PATH, "Path to VDM Tools for VDM-SL (vdmgde):", top);
			vdmPath.setPreferencePage(this);
			vdmPath.setPreferenceStore(getPreferenceStore());
			vdmPath.load();
			
			vppPath = new FileFieldEditor(ICskConstants.VPPGDE_PATH, "Path to VDM Tools for VDM-PP (vppgde):", top);
			vppPath.setPreferencePage(this);
			vppPath.setPreferenceStore(getPreferenceStore());
			vppPath.load();
			
			vicePath = new FileFieldEditor(ICskConstants.VRTGDE_PATH, "Path to VDM Tools for VICE  (vicegde):", top);
			vicePath.setPreferencePage(this);
			vicePath.setPreferenceStore(getPreferenceStore());
			vicePath.load();
		}
		
		return top;
	}
	
	

//	@Override
//	protected void createFieldEditors()
//	{
//		if (!Platform.getOS().equalsIgnoreCase(Platform.OS_MACOSX))
//		{
//			addField(new FileFieldEditor(ICskConstants.VPPGDE_PATH, "Path to VDM Tools for VDM-PP (vppgde):", getFieldEditorParent()));
//			addField(new FileFieldEditor(ICskConstants.VRTGDE_PATH, "Path to VDM Tools for VICE  (vicegde):", getFieldEditorParent()));
//			addField(new FileFieldEditor(ICskConstants.VSLGDE_PATH, "Path to VDM Tools for VDM-SL (vdmgde):", getFieldEditorParent()));
//		} else
//		{
//			addField(new DirectoryFieldEditor(ICskConstants.VPPGDE_PATH, "Path to VDM Tools for VDM-PP (vppgde):", getFieldEditorParent()));
//			addField(new DirectoryFieldEditor(ICskConstants.VRTGDE_PATH, "Path to VDM Tools for VICE  (vicegde):", getFieldEditorParent()));
//			addField(new DirectoryFieldEditor(ICskConstants.VSLGDE_PATH, "Path to VDM Tools for VDM-SL (vdmgde):", getFieldEditorParent()));
//		}
//	}

	
	
	@Override
	protected IPreferenceStore doGetPreferenceStore()
	{
		return Activator.getDefault().getPreferenceStore();
	}

	@Override
	protected void performDefaults()
	{
		if(Platform.getOS().equalsIgnoreCase(Platform.OS_MACOSX)){
			vdmPathMac.loadDefault();
			vppPathMac.loadDefault();
			vicePathMac.loadDefault();
		}
		else
		{
			vdmPath.loadDefault();
			vppPath.loadDefault();
			vicePath.loadDefault();
		}
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(ICskConstants.VPPGDE_PATH, ICskConstants.DEFAULT_VPPGDE_PATH);
		super.performDefaults();
	}


	@Override
	public boolean performOk() {
		if(Platform.getOS().equalsIgnoreCase(Platform.OS_MACOSX)){
			vdmPathMac.store();
			vppPathMac.store();
			vicePathMac.store();
		}
		else
		{
			vdmPath.store();
			vppPath.store();
			vicePath.store();
		}
		return super.performOk();
	}

	public void init(IWorkbench workbench) {
		// TODO Auto-generated method stub
		
	}

	



	

}
