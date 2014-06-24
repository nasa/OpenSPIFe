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

import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.common.ui.editor.lifecycle.EnsembleWizardPage;
import gov.nasa.ensemble.common.ui.preferences.ProjectFieldEditor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ProjectSelectionPage extends EnsembleWizardPage {

	protected static final String P_PROJECT = "projectSelect";
	protected ExtraWizardPageOptions extraOptions = null;
	private String type;
	protected ProjectFieldEditor editor;
	private String labelText = "Project:";
	private Predicate<IProject> projectFilter;
	private ILabelProvider projectLabelProvider;

	public ProjectSelectionPage() {
		super("project_selection_page");
	}
	
	public void setExtraWizardPageOptions(ExtraWizardPageOptions extraOptions) {
		this.extraOptions = extraOptions;
	}

	public void setProjectType(String type) {
		this.type = type;
	}
	
	public void setProjectFilter(Predicate<IProject> projectFilter) {
		this.projectFilter = projectFilter;
	}
	
	public void setProjectLabelProvider(ILabelProvider projectLabelProvider) {
		this.projectLabelProvider = projectLabelProvider;
	}

	public IProject getSelectedProject() {
		String projectName = editor.getStringValue();
		if (projectName != null && !projectName.isEmpty()) {
			return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		}
		return null;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);

		buildProjectChooser(composite);
		buildControls(composite);

		setControl(composite);
		if (extraOptions != null) {
			extraOptions.createControl(composite, type);
		}
		pageUpdated();
	}

	/**
	 * Allow for implementing classes to create more custom controls.
	 */
	protected void buildControls(Composite parent) {
		// no default implementation
	}

	protected Composite buildProjectChooser(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

	    GridData projectSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
	    composite.setLayoutData(projectSelectionData);

	    GridLayout projectSelectionLayout = new GridLayout();
	    projectSelectionLayout.numColumns = 3;
	    projectSelectionLayout.makeColumnsEqualWidth = false;
	    projectSelectionLayout.marginWidth = 0;
	    projectSelectionLayout.marginHeight = 0;
	    composite.setLayout(projectSelectionLayout);

	    editor = new ProjectFieldEditor(P_PROJECT, labelText, composite); // NON-NLS-1
	    editor.setProjectLabelProvider(projectLabelProvider);
	    editor.setProjectFilter(projectFilter);
	    // //NON-NLS-2
	    editor.setPage(this);

	    return composite;
	}

	@Override
	protected void pageUpdated() {
		clearError(ProjectSelectionPage.class);
		if (extraOptions != null)
			extraOptions.pageUpdated(this);
	}
	
	@Override
	public boolean isPageComplete() {
		return getSelectedProject() != null && super.isPageComplete()
				&& (extraOptions == null || extraOptions.isComplete());
	}
	
}
