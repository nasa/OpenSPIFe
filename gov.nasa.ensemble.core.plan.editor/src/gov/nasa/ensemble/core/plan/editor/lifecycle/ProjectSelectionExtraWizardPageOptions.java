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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.ui.preferences.ProjectFieldEditor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class ProjectSelectionExtraWizardPageOptions extends ExtraWizardPageOptions {
		
	private final IProject initialProject;
	private final ILabelProvider projectLableProvider;
	private ProjectFieldEditor projectFieldEditor;
	
	public ProjectSelectionExtraWizardPageOptions() {
		this(null, null);
	}
	
	public ProjectSelectionExtraWizardPageOptions(IProject initialProject, ILabelProvider projectLableProvider) {
		this.initialProject = initialProject;
		if (projectLableProvider == null) {
			projectLableProvider = new WorkbenchLabelProvider();
		}
		this.projectLableProvider = projectLableProvider;
	}

	@Override
	public void createControl(Composite parent, String type) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		projectFieldEditor = new ProjectFieldEditor("project", "Into Project:", composite);
		projectFieldEditor.setProjectLabelProvider(projectLableProvider);
		projectFieldEditor.setInitialSelection(initialProject);
	}
	
	public IProject getProject() {
		try {
			if (projectFieldEditor != null && projectFieldEditor.getStringValue() != null && !projectFieldEditor.getStringValue().equals("")) {
				return ResourcesPlugin.getWorkspace().getRoot().getProject(projectFieldEditor.getStringValue());
			}
		}
		catch (IllegalArgumentException ex) {
			// do nothing
		}
		return null;
	}
		
}
