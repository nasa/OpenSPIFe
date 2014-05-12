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
package gov.nasa.ensemble.core.detail.emf.util;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.preferences.ExtendedStringButtonFieldEditor;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.ui.CommonUIPlugin;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.model.WorkbenchContentProvider;

public class IDirectoryFieldEditor extends ExtendedStringButtonFieldEditor {

	private URI baseURI;

	/**
	 * Creates a new directory field editor
	 */
	protected IDirectoryFieldEditor() {
		// do nothing
	}

	/**
	 * Creates a directory field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public IDirectoryFieldEditor(String name, String labelText, Composite parent) {
		init(name, labelText, parent);
		setErrorMessage(JFaceResources.getString("DirectoryFieldEditor.errorMessage"));//$NON-NLS-1$
		setChangeButtonText(JFaceResources.getString("openBrowse"));//$NON-NLS-1$
		setValidateStrategy(VALIDATE_ON_FOCUS_LOST);
		createControl(parent);
	}

	public void setBaseURI(URI baseURI) {
		this.baseURI = baseURI;
	}

	/*
	 * (non-Javadoc) Method declared on StringButtonFieldEditor. Opens the directory chooser dialog and returns the selected directory.
	 */
	@Override
	protected String changePressed() {
		String text = getTextControl().getText();
		URI oldURI = URI.createURI(text).resolve(baseURI);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		String platformString = oldURI.toPlatformString(true);
		IResource resource = root.getFolder(new Path(platformString));
		while ((resource != null) && !resource.exists()) {
			resource = resource.getParent();
		}
		IContainer newContainer = getDirectory(resource);
		if (newContainer == null) {
			return null;
		}
		URI newURI = EMFUtils.getURI(newContainer);
		URI deresolve = newURI.deresolve(baseURI);
		return CommonUtils.decodeUTF8(deresolve.toString());
	}

	/*
	 * (non-Javadoc) Method declared on StringFieldEditor. Checks whether the text input field contains a valid directory.
	 */
	@Override
	protected boolean doCheckState() {
		String fileName = getTextControl().getText();
		fileName = fileName.trim();
		if (fileName.length() == 0 && isEmptyStringAllowed()) {
			return true;
		}
		File file = new File(fileName);
		return file.isDirectory();
	}

	/**
	 * Helper that opens the directory chooser dialog.
	 * 
	 * @param startingDirectory
	 *            The directory the dialog will open in.
	 * @return File File or <code>null</code>.
	 * 
	 */
	private IContainer getDirectory(Object resource) {
		String project = (baseURI != null ? baseURI.segment(1) : null);
		Shell shell = getShell();
		WorkspaceLabelProvider labelProvider = new WorkspaceLabelProvider();
		WorkbenchContentProvider contentProvider = new WorkbenchContentProvider();
		WorkspaceResourceDialog dialog = new WorkspaceResourceDialog(shell, labelProvider, contentProvider);
		dialog.setAllowMultiple(false);
		String decodedProject = CommonUtils.decodeUTF8(project);
		dialog.setTitle(project != null ? decodedProject : CommonUIPlugin.INSTANCE.getString("_UI_FileSelection_title"));
		//$NON-NLS-1$
		dialog.setMessage(null);
		dialog.setShowNewFolderControl(true);
		dialog.addFilter(dialog.createDefaultViewerFilter(false));
		dialog.addFilter(new DirectoryPatternFilter());
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		Object input = (project != null ? root.getProject(decodedProject) : null);
		if (input == null) {
			input = root;
		}

		dialog.setInput(input);
		dialog.setInitialSelection(resource);
		if (dialog.open() == Window.OK) {
			IContainer[] containers = dialog.getSelectedContainers();
			if (containers.length != 0) {
				IContainer container = containers[0];
				getTextControl().setFocus();
				return container;
			}
		}
		return null;
	}

	private class DirectoryPatternFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			IResource resource = null;
			if (element instanceof IResource) {
				resource = (IResource) element;
			} else if (element instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) element;
				resource = (IResource) adaptable.getAdapter(IResource.class);
			}
			if (resource != null && !resource.isDerived()) {
				if (resource.getType() != IResource.FILE && !resource.getName().startsWith("."))
					return true;
			}
			return false;
		}
	}

}
