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
package gov.nasa.ensemble.common.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public abstract class AbstractPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private List<Group> groups;

	public AbstractPreferencePage(IPreferenceStore store) {
		super(GRID);
		setPreferenceStore(store);
	}

	@Override
	public void init(IWorkbench workbench) {
		// no implementation
	}

	public Group createGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 0;
        group.setLayout(layout);
        group.setFont(parent.getFont());
        if (groups == null) {
        	groups = new ArrayList<Group>();
        }
        groups.add(group);
        return group;
	}

	@Override
	protected void adjustGridLayout() {
		super.adjustGridLayout();
		if (groups != null) {
			int numColumns = ((GridLayout) getFieldEditorParent().getLayout()).numColumns;
			for (Group group : groups) {
				GridData groupLayoutData = (GridData) group.getLayoutData();
				groupLayoutData.horizontalSpan = numColumns;
				GridLayout groupLayout = (GridLayout) group.getLayout();
				groupLayout.numColumns = numColumns;
				groupLayout.marginHeight = 5;
				groupLayout.marginWidth = 5;
			}
	    }
	}

	@Override
	protected abstract void createFieldEditors();

	protected void indent(BooleanFieldEditor editor, Composite parent) {
		Control control = editor.getDescriptionControl(parent);
		GridData layoutData = (GridData)control.getLayoutData();
		if (layoutData == null) {
			control.setLayoutData(layoutData = new GridData());
		}
		layoutData.horizontalIndent = 20;
	}

}
