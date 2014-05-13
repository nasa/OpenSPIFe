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

import gov.nasa.ensemble.common.functional.Predicate;
import gov.nasa.ensemble.common.ui.ProjectSelectionDialog;

import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * A field editor for a project name preference. A project selection dialog
 * appears when the user presses the change button.
 */
public class ProjectFieldEditor extends StringButtonFieldEditor {

	private ILabelProvider projectLabelProvider;
	private IProject initialSelection;
	private int style = SWT.OPEN;
	private Predicate<IProject> projectFilter;

	/**
	 * Creates a project field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public ProjectFieldEditor(String name, String labelText, Composite parent) {
		init(name, labelText);
		setErrorMessage(JFaceResources.getString("ProjectFieldEditor.errorMessage"));//$NON-NLS-1$
		setChangeButtonText(JFaceResources.getString("openBrowse"));//$NON-NLS-1$
		setValidateStrategy(VALIDATE_ON_KEY_STROKE);
		createControl(parent);
	}
	
	/*
	 * (non-Javadoc) Method declared on StringButtonFieldEditor. Opens the
	 * project chooser dialog and returns the selected project.
	 */
	@Override
	protected String changePressed() {
		IProject p = getInitialSelectedProject();
		try {
			String text = getTextControl().getText();
			if (text != null) {
				p = ResourcesPlugin.getWorkspace().getRoot().getProject(text);
			}
		} catch (IllegalArgumentException ex) {
			// do nothing
		}
		if (p != null && !p.exists()) {
			p = null;
		}
		p = getProject(p);
		if (p == null) {
			return null;
		}
		return p.getName();
	}
	
	/*
	 * (non-Javadoc) Method declared on StringFieldEditor. Checks whether the
	 * text input field specifies an open, writable project.
	 */
	@Override
	protected boolean checkState() {
		String msg = null;
		String path = getTextControl().getText();
		if (path != null) {
			path = path.trim();
		} else {
			path = "";//$NON-NLS-1$
		}
		if (path.length() == 0) {
			if (!isEmptyStringAllowed()) {
				msg = getErrorMessage();
			}
		} else {
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(path);
			if (!project.isOpen() || project.getResourceAttributes().isReadOnly()) {
				msg = getErrorMessage();
			}
		}

		if (msg != null) { // error
			showErrorMessage(msg);
			return false;
		}

		// OK!
		clearErrorMessage();
		return true;
	}

	/**
	 * Helper to open the project chooser dialog.
	 * 
	 * @param startingProject
	 *            the project to use as a default selection for the dialog.
	 * @return IProject The project the user selected or <code>null</code> if
	 *         they did not select one.
	 */
	private IProject getProject(IProject startingProject) {
		ProjectSelectionDialog dialog = new ProjectSelectionDialog(getShell(), "OK", projectLabelProvider);
		dialog.setProjectFilter(projectFilter);
		dialog.setInitialElementSelections(Collections.singletonList(startingProject));
		dialog.open();
		Object[] result = dialog.getResult();
		if (result != null && result.length != 0)
			return (IProject) result[0];

		return null;
	}

	private IProject getInitialSelectedProject() {
		return initialSelection;
	}

	/**
	 * Set the project chooser type
	 */
	public void setProjectChooserStyle(int style) {
		this.setStyle(style);
	}
	
	public IProject getSelectedProject() {
		String projectName = getStringValue();
		if (projectName != null && !projectName.isEmpty()) {
			return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		}
		return null;
	}


	public void setProjectLabelProvider(ILabelProvider projectLabelProvider) {
		this.projectLabelProvider = projectLabelProvider;
	}

	public void setInitialSelection(IProject initialSelection) {
		this.initialSelection = initialSelection;
		if (getInitialSelectedProject() != null) {
			getTextControl().setText(getInitialSelectedProject().getName());
		}
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public int getStyle() {
		return style;
	}
	
	public void setProjectFilter(Predicate<IProject> projectFilter) {
		this.projectFilter = projectFilter;
	}

}
