package org.overture.ide.plugins.traces.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.overture.ide.plugins.traces.ITracesConstants;
import org.overture.ide.plugins.traces.OvertureTracesPlugin;

public class WorkbenchPreferencePageMain  extends FieldEditorPreferencePage implements
IWorkbenchPreferencePage {

	@Override
	protected void createFieldEditors()
	{
		addField(new BooleanFieldEditor(ITracesConstants.REMOTE_DEBUG_PREFERENCE, "Enable remote debug", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ITracesConstants.REMOTE_DEBUG_FIXED_PORT, "Use fixed port for remote debug", getFieldEditorParent()));
	}
	
	@Override
	protected IPreferenceStore doGetPreferenceStore()
	{
		return OvertureTracesPlugin.getDefault().getPreferenceStore();
	}
	
	@Override
	protected void performDefaults()
	{
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(ITracesConstants.REMOTE_DEBUG_PREFERENCE, false);
		store.setDefault(ITracesConstants.REMOTE_DEBUG_FIXED_PORT, false);
		super.performDefaults();
	}

	public void init(IWorkbench workbench)
	{
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(ITracesConstants.REMOTE_DEBUG_PREFERENCE, false);
		store.setDefault(ITracesConstants.REMOTE_DEBUG_FIXED_PORT, false);
	}

}