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
package org.overture.ide.plugins.combinatorialtesting.views;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.overture.ide.plugins.combinatorialtesting.ITracesConstants;
import org.overture.interpreter.traces.TraceReductionType;

public class TraceOptionsDialog extends Composite
{
	public  boolean isCanceled = false;
	private Button buttonCancel = null;
	private Button buttonOk = null;
	private Button restorePreferences = null;
	private Combo comboReductionType = null;
	private Label label1 = null;
	private Label label2 = null;
	private Label label3 = null;
	//private Text textSeed = null;
	//private Combo comboSubset = null;
	private Spinner subsetSpinner = null;
	private Spinner seedSpinner = null;
	private static TraceOptionsDisplayState displayState = null;
	
	public TraceOptionsDialog(Composite parent, int style) {
		super(parent, style);
		initialize();
		loadDisplayState();
	}

	private void initialize()
	{
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		this.setLayout(gridLayout);
		label1 = new Label(this, SWT.NONE);
		label1.setText("Trace reduction type:");
		createComboReductionType();
		setSize(new Point(421, 224));
		label2 = new Label(this, SWT.NONE);
		label2.setText("Trace filtering seed:");
		//textSeed = new Text(this, SWT.BORDER);
		//textSeed.setText(new Long(seed).toString());
		//textSeed.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		//textSeed.setText(readSeedPref() + "");
		createSeedSpinner();
		label3 = new Label(this, SWT.NONE);
		label3.setText("Subset limitation (%):");
//		comboSubset = new Text(this, SWT.BORDER);
//		comboSubset.setText("1.00000000000");
//		createComboSubset();
		createSubsetSpinner();
		buttonCancel = new Button(this, SWT.NONE);
		buttonCancel.setText("Cancel");
		buttonCancel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		buttonCancel.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
			{
				isCanceled = true;
				getShell().close();
			}
		});
		buttonOk = new Button(this, SWT.NONE);
		buttonOk.setText("Ok");
		buttonOk.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		buttonOk.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
			{
				isCanceled = false;
				subset= subsetSpinner.getSelection() / 100.0F;//Float.parseFloat(comboSubset.getText().replace('%', ' ').trim())/100;
				seed= seedSpinner.getSelection();
				reductionType= TraceReductionType.findValue(comboReductionType.getText());
				getShell().close();
			}
		});
		
		this.addListener(SWT.Dispose, new Listener()
		{
			
			@Override
			public void handleEvent(Event event)
			{
				
				//Note that the 'GUI types' are used, i.e. the spinner uses int etc.
				int subset= subsetSpinner.getSelection();
				int seed= seedSpinner.getSelection();
				String reductionType= comboReductionType.getText();
				
				displayState = new TraceOptionsDisplayState(subset, seed, reductionType);
			}
		});
		
		restorePreferences = new Button(this, SWT.NONE);
		restorePreferences.setText("Restore preferences");
		restorePreferences.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		restorePreferences.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
			{
				selectComboReductionItem(readTraceReductionPrefStr());
				seedSpinner.setSelection(readSeedPref());
				subsetSpinner.setSelection(readSubsetPref());
			}
		});
	}
	
	private void selectComboReductionItem(String toSelect)
	{
		String[] reductions = comboReductionType.getItems();
		if (reductions.length > 0)
		{
			comboReductionType.select(0);
			
			for(int i = 0; i < reductions.length; i++)
			{
				if(reductions[i].equals(toSelect))
				{
					comboReductionType.select(i);
					break;
				}
			}
		}
	}
	
	/**
	 * This method initializes comboReductionType
	 * 
	 */
	private void createComboReductionType()
	{

		comboReductionType = new Combo(this, SWT.READ_ONLY);

		String[] reductions = new String[TraceReductionType.values().length - 1];
		int i = 0;
		for (TraceReductionType r : TraceReductionType.values())
		{
			if(r != TraceReductionType.NONE) //Removed NONE at Nicks request
			{
				reductions[i] = r.getDisplayName();
				i++;
			}
			
		}
	
		comboReductionType.setItems(reductions);
		String reductionStr = readTraceReductionPrefStr();
		selectComboReductionItem(reductionStr);
		comboReductionType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	private IPreferenceStore getPreferenceStore()
	{
		return  new ScopedPreferenceStore(InstanceScope.INSTANCE, ITracesConstants.PLUGIN_ID);
	}
	
	private String readTraceReductionPrefStr()
	{
		int ordinal = getPreferenceStore().getInt(ITracesConstants.TRACE_REDUCTION_TYPE);
		
		return TraceReductionType.values()[ordinal].getDisplayName();
	}
	
	private int readSeedPref()
	{
		return getPreferenceStore().getInt(ITracesConstants.TRACE_SEED);
	}
	
	private int readSubsetPref()
	{
		return getPreferenceStore().getInt(ITracesConstants.TRACE_SUBSET_LIMITATION);
	}
	
	private void createSeedSpinner()
	{
		seedSpinner = new Spinner(this, SWT.None);
		seedSpinner.setMinimum(Integer.MIN_VALUE);
		seedSpinner.setMaximum(Integer.MAX_VALUE);
		seedSpinner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		seedSpinner.setSelection(readSeedPref());
	}
	
	private void createSubsetSpinner()
	{
		subsetSpinner = new Spinner(this, SWT.None);
		subsetSpinner.setMinimum(1);
		subsetSpinner.setMaximum(100);
		subsetSpinner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		subsetSpinner.setSelection(readSubsetPref());
	}
	
//	private void createComboSubset()
//	{
//
//		comboSubset = new Combo(this, SWT.READ_ONLY);
//
//		final Integer division = 1;
//		final Integer total = 100;
//		
//		String[] reductions = new String[total/division];
//
//		for (int i = 0; i < total/division; i++)
//		{
//			reductions[i] = new Long(division*(i+1)).toString()+" %";
//		}
//
//		comboSubset.setItems(reductions);
//		if (reductions.length > 0)
//			comboSubset.select(reductions.length-1);
//		comboSubset.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//	}

	private float subset = -1;
	private long seed = -1;
	private TraceReductionType reductionType;
	public float getSubset()
	{
		return subset;
	}

	public long getSeed()
	{
		return seed;
	}

	public TraceReductionType getTraceReductionType()
	{
		return reductionType;
	}
	
	private void loadDisplayState()
	{
		//The previously typed data have higher priority than what is contained in the preference store
		if(displayState != null)
		{
			subsetSpinner.setSelection(displayState.getSubset());
			seedSpinner.setSelection(displayState.getSeed());
			
			String previousReductionType = displayState.getReductionType();
			final int ITEM_COUNT = comboReductionType.getItemCount();
			for(int i = 0; i < ITEM_COUNT; i++)
			{
				String currentItem = comboReductionType.getItem(i);
				
				if(currentItem.equals(previousReductionType))
				{
					comboReductionType.select(i);
					break;
				}
				
			}
		}
	}
} // @jve:decl-index=0:visual-constraint="10,10"