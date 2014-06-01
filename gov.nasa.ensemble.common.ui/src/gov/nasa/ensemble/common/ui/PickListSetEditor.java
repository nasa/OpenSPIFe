/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.CommonUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class PickListSetEditor extends FieldEditor {
	
	private static final String ITEM_NEW = "New...";
	private Combo combo = null;
	private Map<String, PickListSet> setsByName = new HashMap<String, PickListSet>();
	private PickListFieldEditor pickListEditor = null;
	private int lastSelection = -1;
	
	public static final String P_SET_KEYS = "setKeys";
	public static final String P_SELECTED_SET = "selectedSet";
	
	private static final Logger trace = Logger.getLogger(PickListSetEditor.class);
	
	public PickListSetEditor(String preferenceName, String labelText, Composite parent) {
		init(preferenceName, labelText);
        createControl(parent);
	}
	
	public void setPickListEditor(PickListFieldEditor pickListEditor) {
		this.pickListEditor = pickListEditor;
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		// no implementation
	}

	@Override
	protected void doFillIntoGrid(final Composite parent, int numColumns) {
		Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        composite.setLayout(layout);
        
        GridData gd;
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        gd.horizontalAlignment = GridData.FILL;
        gd.verticalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        composite.setLayoutData(gd);
        
        Label label = new Label(composite, SWT.NONE);
        label.setText("Configured Sets");
        
        combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.addSelectionListener(new SelectionListener() {
        	@Override
			public void widgetDefaultSelected(SelectionEvent e) {widgetSelected(e);}
			@Override
			public void widgetSelected(SelectionEvent e) 		{
				int selection = combo.getSelectionIndex();
				String key = combo.getItem(selection);
				if (ITEM_NEW.equals(key)) {
					if (!createNewPickListSet()) {
						combo.select(lastSelection);
					}
				} else {
					PickListSet set = setsByName.get(key);
					if (set != null && PickListSetEditor.this.pickListEditor != null) {
						pickListEditor.doLoad(set.getKeysString());
					}
				}
				lastSelection = selection;
			}
			private boolean createNewPickListSet() {
				InputDialog dialog = new InputDialog(
					parent.getShell(), 
					"Configuration title", 
					"Select new name", "", 
					new IInputValidator() {
						@Override
						public String isValid(String newText) {
							if (newText == null || newText.length() == 0) {
								return "Name must be of non-zero length";
							}
							
							for (String text : combo.getItems()) {
								if (text.equals(newText)) {
									return "Name must be unique";
								}
							}
							return null;
						}
					}
				);
				dialog.setBlockOnOpen(true);
				int returnCode = dialog.open();
				if (returnCode != Window.OK) {
					return false;
				}
				
				List<String> items = Arrays.asList(pickListEditor.getSelectedListControl().getItems());
				PickListSet set = new PickListSet(dialog.getValue(), items);
				setsByName.put(set.getName(), set);
				
				int index = combo.getItemCount() - 1;
				combo.add(set.getName(), combo.getItemCount() - 1);
				combo.select(index);
				return true;
			}
        });
	}
	
	@Override
	protected void doLoad() {
		doLoad(getPreferenceStore().getString(getPreferenceName()));
	}

	@Override
	protected void doLoadDefault() {
        doLoad(getPreferenceStore().getDefaultString(getPreferenceName()));        
	}
    
    protected void doLoad(String s) {
    	StringReader r = new StringReader(s);
    	IDialogSettings settings = new DialogSettings(getPreferenceName());
    	try {
			settings.load(r);
		} catch (IOException e) {
			trace.error(e.getMessage(), e);
		}
    	
		combo.removeAll();
		
		int index = -1;
		String selection = settings.get(P_SELECTED_SET);
		String list[] = settings.getArray(P_SET_KEYS);
		for (int i=0; list != null && i<list.length; i++) {
			String key = list[i];

			IDialogSettings section = settings.getSection(key);
			if (section != null) {
				PickListSet set = new PickListSet(section);
				setsByName.put(key, set);
				
				combo.add(key);
				if (CommonUtils.equals(selection, key)) {
					index = i;
				}
			}
		}
		combo.add(ITEM_NEW);
		
		if (index != -1) {
			combo.select(index);
			lastSelection = index;
		}
    }
    
	@Override
	protected void doStore() {
		try {
			int index = combo.getSelectionIndex();
			
			IDialogSettings settings = new DialogSettings(getPreferenceName());
			settings.put(P_SET_KEYS, combo.getItems());
			settings.put(P_SELECTED_SET, combo.getItem(index));
			for(int i=0; i<combo.getItemCount(); i++) {
				String key = combo.getItem(i);
				if (key.equals(ITEM_NEW)) {
					continue;
				}
				
				PickListSet set = setsByName.get(key);
				if (i == index) {
					set = new PickListSet(key, Arrays.asList(pickListEditor.getSelectedListControl().getItems()));
					setsByName.put(key, set);
				}
				settings.addSection(set.getSettings());
			}
			StringWriter w = new StringWriter();
			settings.save(w);
			getPreferenceStore().putValue(getPreferenceName(), w.toString());
		} catch(Exception e) {
			trace.error(e.getMessage(), e);
		}
	}
	
	@Override
	public int getNumberOfControls() {
		return 1;
	}
    
	public static IDialogSettings buildSettings(String settingsName, List<PickListSet> list) {
		IDialogSettings settings = new DialogSettings(settingsName);
		List<String> keys = new ArrayList<String>();
		for (PickListSet set : list) {
			settings.addSection(set.getSettings());
			keys.add(set.getName());
		}
		settings.put(P_SET_KEYS, keys.toArray(new String[0]));
		return settings;
	}

}
