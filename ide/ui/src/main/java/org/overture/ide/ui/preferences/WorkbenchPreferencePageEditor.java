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
package org.overture.ide.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.overture.ide.ui.IVdmUiConstants;
import org.overture.ide.ui.VdmUIPlugin;

public class WorkbenchPreferencePageEditor  extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {

	@Override
	protected void createFieldEditors()
	{
		addField(new BooleanFieldEditor(IVdmUiConstants.ENABLE_EDITOR_RECONFILER, "Syntax checking while you type", getFieldEditorParent()));
		addField(new BooleanFieldEditor(IVdmUiConstants.ENABLE_EDITOR_FAST_RECONFILER, "Enable fast reconcile", getFieldEditorParent()));
	}
	
	@Override
	protected IPreferenceStore doGetPreferenceStore()
	{
		return VdmUIPlugin.getDefault().getPreferenceStore();
	}
	
	@Override
	protected void performDefaults()
	{
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(IVdmUiConstants.ENABLE_EDITOR_RECONFILER, true);
		store.setDefault(IVdmUiConstants.ENABLE_EDITOR_FAST_RECONFILER, false);
		super.performDefaults();
	}

	public void init(IWorkbench workbench)
	{
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(IVdmUiConstants.ENABLE_EDITOR_RECONFILER, true);
		store.setDefault(IVdmUiConstants.ENABLE_EDITOR_FAST_RECONFILER, false);
	}

}
