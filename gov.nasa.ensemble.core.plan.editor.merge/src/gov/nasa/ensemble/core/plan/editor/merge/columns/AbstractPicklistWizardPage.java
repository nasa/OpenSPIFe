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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.ui.PickListFieldEditor;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.ui.CommonUIPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/*package*/ abstract class AbstractPicklistWizardPage extends EnsembleWizardPage {

	private PickListFieldEditor pickList;
	protected String preferenceName;
	private String title;

	public AbstractPicklistWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.title = title;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
	
		setControl(container);
	
		pickList = new PickListFieldEditor("", title, container, computeAvailableChoices(), true) {
	
			@Override
			public String getPreferenceName() {
				return preferenceName;
			}
		
			@Override
			protected void doLoad() {
				super.doLoad(getDefaultPickNames());
			}
			
			@Override
			protected void doLoadDefault() {
				super.doLoad(getDefaultPickNames());
			}
		
			@Override
			protected void doLoad(String commaSeparated) {
				if (pickList == null) {
					this.doLoad();
				} else {
					super.doLoad(commaSeparated);
				}
			}
			
			@Override
			protected void doLoad(java.util.List<String> showOnRight) {
				if (pickList == null) {
					this.doLoad();
				} else {
					super.doLoad(showOnRight);
				}
			}
		};
		
		pickList.setPropertyChangeListener(new IPropertyChangeListener() {
	
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				getContainer().updateButtons();
			}
		});
		
		pickList.setPreferenceStore(CommonUIPlugin.getPlugin().getPreferenceStore());
		
	}

	public abstract Collection<String> computeAvailableChoices();

	public List<String> getDefaultPickNames() {
		List<String> result = EnsembleProperties.getStringListPropertyValue(preferenceName);
		if (result==null) return Collections.emptyList();
		return result;
	}

	public String[] getSelectedPickNames() {
		if (pickList==null) return null;
		if (pickList.getSelectedListControl()==null) return null;
		return pickList.getSelectedListControl().getItems();
	}

	public String getPreferenceName() {
		return preferenceName;
	}

	public void setPreferenceName(String preferenceName) {
		this.preferenceName = preferenceName;
		if (pickList != null) { // may be setting this in advance, but if resetting...
			pickList.loadDefault();
		}
	}

}
