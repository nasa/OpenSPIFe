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
import gov.nasa.ensemble.core.detail.emf.Activator;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.ui.CommonUIPlugin;
import org.eclipse.emf.common.ui.dialogs.WorkspaceResourceDialog;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;

public class IFileFieldEditor extends ExtendedStringButtonFieldEditor {

	private static final String MUST_SELECT_AN_OUTPUT_FOLDER = "Must select an output folder.";
	private static final String EXTENSION_ERROR_MESSAGE_PREFIX = "File name must end in ";

	/**
	 * List of legal file extension suffixes, or <code>null</code> for system defaults.
	 */
	private String[] extensions = null;

	/**
	 * Indicates whether the path must be absolute; <code>false</code> by default.
	 */
	private boolean enforceAbsolute = false;
	
	private int style = SWT.OPEN;

	private URI baseURI;

	/**
	 * Creates a new file field editor
	 */
	protected IFileFieldEditor() {
		// do nothing
	}

	/**
	 * Creates a file field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public IFileFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, false, parent);
	}

	/**
	 * Creates a file field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param enforceAbsolute
	 *            <code>true</code> if the file path must be absolute, and <code>false</code> otherwise
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public IFileFieldEditor(String name, String labelText, boolean enforceAbsolute, Composite parent) {
		this(name, labelText, enforceAbsolute, VALIDATE_ON_FOCUS_LOST, parent);
	}

	/**
	 * Creates a file field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param enforceAbsolute
	 *            <code>true</code> if the file path must be absolute, and <code>false</code> otherwise
	 * @param validationStrategy
	 *            either {@link StringButtonFieldEditor#VALIDATE_ON_KEY_STROKE} to perform on the fly checking, or {@link StringButtonFieldEditor#VALIDATE_ON_FOCUS_LOST} (the default) to perform
	 *            validation only after the text has been typed in
	 * @param parent
	 *            the parent of the field editor's control.
	 * @since 3.4
	 * @see StringButtonFieldEditor#VALIDATE_ON_KEY_STROKE
	 * @see StringButtonFieldEditor#VALIDATE_ON_FOCUS_LOST
	 */
	public IFileFieldEditor(String name, String labelText, boolean enforceAbsolute, int validationStrategy, Composite parent) {
		init(name, labelText, parent);
		this.enforceAbsolute = enforceAbsolute;
		setErrorMessage(JFaceResources.getString("FileFieldEditor.errorMessage"));//$NON-NLS-1$
		setChangeButtonText(JFaceResources.getString("openBrowse"));//$NON-NLS-1$
		setValidateStrategy(validationStrategy);
		createControl(parent);
	}

	public void setBaseURI(URI baseURI) {
		this.baseURI = baseURI;
	}

	/*
	 * (non-Javadoc) Method declared on StringButtonFieldEditor. Opens the file chooser dialog and returns the selected file.
	 */
	@Override
	protected String changePressed() {
		String text = getTextControl().getText();
		URI oldURI = URI.createURI(text).resolve(baseURI);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		String platformString = oldURI.toPlatformString(true);
		IResource resource = root.getFile(new Path(platformString));
		while ((resource != null) && !resource.exists()) {
			resource = resource.getParent();
		}
		IFile newFile = getFile(resource);
		if (newFile == null) {
			return null;
		}
		URI newURI = EMFUtils.getURI(newFile);
		URI deresolve = newURI.deresolve(baseURI);
		return CommonUtils.decodeUTF8(deresolve.toString());
	}

	/*
	 * (non-Javadoc) Method declared on StringFieldEditor. Checks whether the text input field specifies an existing file.
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
			File file = new File(path);
			if (file.isFile()) {
				if (enforceAbsolute && !file.isAbsolute()) {
					msg = JFaceResources.getString("FileFieldEditor.errorMessage2");//$NON-NLS-1$
				}
			} else {
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
	 * Helper to open the file chooser dialog.
	 * 
	 * @param startingDirectory
	 *            the directory to open the dialog on.
	 * @return File The File the user selected or <code>null</code> if they do not.
	 */
	private IFile getFile(IResource resource) {
		String project = (baseURI != null ? baseURI.segment(1) : null);
		Shell shell = getShell();
		WorkspaceLabelProvider labelProvider = new WorkspaceLabelProvider();
		WorkbenchContentProvider contentProvider = new WorkbenchContentProvider();
		final WorkspaceResourceDialog dialog = new WorkspaceResourceDialog(shell, labelProvider, contentProvider) {

			@Override
			protected void fileTextModified(String text) {
				super.fileTextModified(text);
				updateOKStatus();
			}
			
		};
		dialog.setAllowMultiple(false);
		String decodedProject = CommonUtils.decodeUTF8(project);
		dialog.setTitle(project != null ? decodedProject : CommonUIPlugin.INSTANCE.getString("_UI_FileSelection_title"));//$NON-NLS-1$
		dialog.setMessage(null);
		dialog.setShowNewFolderControl(true);
		if (style == SWT.SAVE) {
			dialog.setShowFileControl(true);
		}
		dialog.addFilter(dialog.createDefaultViewerFilter(true));
		dialog.addFilter(new FilePatternFilter());
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		Object input = (project != null ? root.getProject(decodedProject) : null);
		if (input == null) {
			input = root;
		}
		dialog.setValidator(new ISelectionStatusValidator() {
			@Override
			public IStatus validate(Object[] selection) {
				if (selection.length == 0) {
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, MUST_SELECT_AN_OUTPUT_FOLDER);
				} else if (selection.length > 0) {
					Object s = selection[0];
					if (s instanceof IFile) {
						return hasValidExtension(((IFile) s).getName());
					}
				}
				return hasValidExtension(dialog.getFileText());
			}
		});
		
		dialog.setInput(input);
		dialog.setInitialSelection(resource);
		if (dialog.open() == Window.OK) {
			IFile file = getFileFromDialog(dialog);
			if (file != null) {
				getTextControl().setFocus();
				return file;
			}
		}
		return null;
	}
	
	private Status hasValidExtension(String name) {
		if (extensions == null || (extensions != null && extensions.length == 0)) return new Status(IStatus.OK, Activator.PLUGIN_ID, "");
		if (name != null) {
			int index = name.trim().lastIndexOf(".");
			if (index >= 0) {
				String currentExtension = "*" + name.substring(index);
				for (String e : extensions) {
					if (currentExtension.equalsIgnoreCase(e)) {
						return new Status(IStatus.OK, Activator.PLUGIN_ID, "");
					}
				}
			}
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(EXTENSION_ERROR_MESSAGE_PREFIX);
		for (int i = 0; i < extensions.length; i++) {
			buffer.append(extensions[i]);
			if (i != extensions.length-1) {
				buffer.append(",");
			}
		}
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID, buffer.toString());
	}

	private IFile getFileFromDialog(WorkspaceResourceDialog dialog) {
		String fileName = dialog.getFileText();
		IContainer container = null;
		IFile[] files = dialog.getSelectedFiles();
		if (files.length != 0) {
			IFile file = files[0];
			if (fileName.isEmpty() || file.getName().equals(fileName)) {
				return file;
			}
			container = file.getParent();
		} else {
			IContainer[] containers = dialog.getSelectedContainers();
			if (containers.length != 0) {
				container = containers[0];
			}
		}
		if (container != null && !fileName.isEmpty()) {
			return container.getFile(new Path(fileName));
		}
		return null;
	}

	/**
	 * Sets this file field editor's file extension filter.
	 * 
	 * @param extensions
	 *            a list of file extension, or <code>null</code> to set the filter to the system's default value
	 */
	public void setFileExtensions(String[] extensions) {
		this.extensions = extensions;
	}
	
	/**
	 * Set the file chooser type
	 */
	public void setFileChooserStyle(int style) {
		this.style = style;
	}
	   

	private class FilePatternFilter extends ViewerFilter {
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			IResource resource = null;
			if (element instanceof IResource) {
				resource = (IResource) element;
			} else if (element instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) element;
				resource = (IResource) adaptable.getAdapter(IResource.class);
			}
			if (resource != null && resource.getName().startsWith("."))
				return false;
			if ((extensions == null || extensions.length == 0))
				return true;
			if (resource != null && !resource.isDerived()) {
				if (resource.getType() != IResource.FILE)
					return true;
				String extension = resource.getFileExtension();
				if (extension == null)
					return true;
				for (int i = 0; i < extensions.length; i++) {
					if (extension.equalsIgnoreCase(extensions[i].substring(extensions[i].lastIndexOf(".") + 1)))
						return true;
				}
			}
			return false;
		}
	}
}
