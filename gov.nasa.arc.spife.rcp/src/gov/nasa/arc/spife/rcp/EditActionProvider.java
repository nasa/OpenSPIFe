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
package gov.nasa.arc.spife.rcp;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.CopyFilesAndFoldersOperation;
import org.eclipse.ui.actions.CopyProjectOperation;
import org.eclipse.ui.actions.DeleteResourceAction;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.eclipse.ui.part.ResourceTransfer;

public class EditActionProvider extends CommonActionProvider {
	private DeleteResourceAction deleteResourceAction;
	private CopyAction copyAction;
	private PasteAction pasteResourceAction;
	private Clipboard clipboard;

	// Must define a no-argument constructor so that instances may be created
	// from the extension.
	public EditActionProvider() {
		//
	}

	@Override
	public void init(ICommonActionExtensionSite anExtensionSite) {
		if (anExtensionSite.getViewSite() instanceof ICommonViewerWorkbenchSite) {
			ICommonViewerWorkbenchSite viewSite = (ICommonViewerWorkbenchSite) anExtensionSite.getViewSite();
			deleteResourceAction = new DeleteResourceAction(viewSite.getSite());
			clipboard = new Clipboard(viewSite.getShell().getDisplay());
			pasteResourceAction = new PasteAction(viewSite.getShell(), clipboard);
			copyAction = new CopyAction(viewSite.getShell(), clipboard, pasteResourceAction);
		}
	}

	@Override
	public void fillActionBars(IActionBars actionBars) {
		// if (openFileAction.isEnabled()) {
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();
		// if (selection.size() == 1 && selection.getFirstElement() instanceof
		// IFile) {
		deleteResourceAction.selectionChanged(selection);
		copyAction.selectionChanged(selection);

		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), deleteResourceAction);
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), pasteResourceAction);
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copyAction);
		// }
		// }
	}

	@Override
	public void fillContextMenu(IMenuManager menu) {
		if (getContext().getSelection().isEmpty()) {
			return;
		}

		// if (openFileAction.isEnabled()) {
		IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

		deleteResourceAction.selectionChanged(selection);
		pasteResourceAction.selectionChanged(selection);
		copyAction.selectionChanged(selection);
		menu.add(new Separator());
		menu.appendToGroup("group.edit", copyAction);
		menu.appendToGroup("group.edit", pasteResourceAction);
		menu.appendToGroup("group.edit", deleteResourceAction);
		// }
	}

	@Override
	public void dispose() {
		if (clipboard != null) {
			clipboard.dispose();
			clipboard = null;
		}
		super.dispose();
	}

	class PasteAction extends SelectionListenerAction {

		/**
		 * The id of this action.
		 */
		public static final String ID = PlatformUI.PLUGIN_ID + ".PasteAction";//$NON-NLS-1$

		/**
		 * The shell in which to show any dialogs.
		 */
		private Shell shell;

		/**
		 * System clipboard
		 */
		private Clipboard clipboard;

		/**
		 * Creates a new action.
		 * 
		 * @param shell
		 *            the shell for any dialogs
		 * @param clipboard
		 *            the clipboard
		 */
		public PasteAction(Shell shell, Clipboard clipboard) {
			super("Paste");
			Assert.isNotNull(shell);
			Assert.isNotNull(clipboard);
			this.shell = shell;
			this.clipboard = clipboard;
			setToolTipText("Paste");
			setId(PasteAction.ID);
		}

		/**
		 * Returns the actual target of the paste action. Returns null if no valid target is selected.
		 * 
		 * @return the actual target of the paste action
		 */
		private IResource getTarget() {
			List<?> selectedResources = getSelectedResources();

			for (int i = 0; i < selectedResources.size(); i++) {
				IResource resource = (IResource) selectedResources.get(i);

				if (resource instanceof IProject && !((IProject) resource).isOpen()) {
					return null;
				}
				if (resource.getType() == IResource.FILE) {
					resource = resource.getParent();
				}
				if (resource != null) {
					return resource;
				}
			}
			return null;
		}

		/**
		 * Returns whether any of the given resources are linked resources.
		 * 
		 * @param resources
		 *            resource to check for linked type. may be null
		 * @return true=one or more resources are linked. false=none of the resources are linked
		 */
		private boolean isLinked(IResource[] resources) {
			for (int i = 0; i < resources.length; i++) {
				if (resources[i].isLinked()) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Implementation of method defined on <code>IAction</code>.
		 */
		@Override
		public void run() {
			// try a resource transfer
			ResourceTransfer resTransfer = ResourceTransfer.getInstance();
			IResource[] resourceData = (IResource[]) clipboard.getContents(resTransfer);

			if (resourceData != null && resourceData.length > 0) {
				if (resourceData[0].getType() == IResource.PROJECT) {

					// enablement checks for all projects
					for (int i = 0; i < resourceData.length; i++) {
						CopyProjectOperation operation = new CopyProjectOperation(this.shell);
						operation.copyProject((IProject) resourceData[i]);
					}
				} else {
					// enablement should ensure that we always have access to a
					// container
					IContainer container = getContainer();

					CopyFilesAndFoldersOperation operation = new CopyFilesAndFoldersOperation(this.shell);
					operation.copyResources(resourceData, container);
				}
				return;
			}

			// try a file transfer
			FileTransfer fileTransfer = FileTransfer.getInstance();
			String[] fileData = (String[]) clipboard.getContents(fileTransfer);

			if (fileData != null) {
				// enablement should ensure that we always have access to a
				// container
				IContainer container = getContainer();

				CopyFilesAndFoldersOperation operation = new CopyFilesAndFoldersOperation(this.shell);
				operation.copyFiles(fileData, container);
			}
		}

		/**
		 * Returns the container to hold the pasted resources.
		 */
		private IContainer getContainer() {
			List<?> selection = getSelectedResources();
			if (selection.get(0) instanceof IFile) {
				return ((IFile) selection.get(0)).getParent();
			} else {
				return (IContainer) selection.get(0);
			}
		}

		/**
		 * The <code>PasteAction</code> implementation of this <code>SelectionListenerAction</code> method enables this action if a resource compatible with what is on the clipboard is selected.
		 * 
		 * -Clipboard must have IResource or java.io.File -Projects can always be pasted if they are open -Workspace folder may not be copied into itself -Files and folders may be pasted to a single
		 * selected folder in open project or multiple selected files in the same folder
		 */
		@Override
		protected boolean updateSelection(IStructuredSelection selection) {
			if (!super.updateSelection(selection)) {
				return false;
			}

			final IResource[][] clipboardData = new IResource[1][];
			shell.getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					// clipboard must have resources or files
					ResourceTransfer resTransfer = ResourceTransfer.getInstance();
					clipboardData[0] = (IResource[]) clipboard.getContents(resTransfer);
				}
			});
			IResource[] resourceData = clipboardData[0];
			boolean isProjectRes = resourceData != null && resourceData.length > 0 && resourceData[0].getType() == IResource.PROJECT;

			if (isProjectRes && resourceData != null) {
				for (int i = 0; i < resourceData.length; i++) {
					// make sure all resource data are open projects
					// can paste open projects regardless of selection
					if (resourceData[i].getType() != IResource.PROJECT || ((IProject) resourceData[i]).isOpen() == false) {
						return false;
					}
				}
				return true;
			}

			if (getSelectedNonResources().size() > 0) {
				return false;
			}

			IResource targetResource = getTarget();
			// targetResource is null if no valid target is selected (e.g., open
			// project)
			// or selection is empty
			if (targetResource == null) {
				return false;
			}

			// can paste files and folders to a single selection (file, folder,
			// open project) or multiple file selection with the same parent
			List<?> selectedResources = getSelectedResources();
			if (selectedResources.size() > 1) {
				for (int i = 0; i < selectedResources.size(); i++) {
					IResource resource = (IResource) selectedResources.get(i);
					if (resource.getType() != IResource.FILE) {
						return false;
					}
					if (!targetResource.equals(resource.getParent())) {
						return false;
					}
				}
			}
			if (resourceData != null) {
				// linked resources can only be pasted into projects
				if (isLinked(resourceData) && targetResource.getType() != IResource.PROJECT && targetResource.getType() != IResource.FOLDER) {
					return false;
				}

				if (targetResource.getType() == IResource.FOLDER) {
					// don't try to copy folder to self
					for (int i = 0; i < resourceData.length; i++) {
						if (targetResource.equals(resourceData[i])) {
							return false;
						}
					}
				}
				return true;
			}
			TransferData[] transfers = clipboard.getAvailableTypes();
			FileTransfer fileTransfer = FileTransfer.getInstance();
			for (int i = 0; i < transfers.length; i++) {
				if (fileTransfer.isSupportedType(transfers[i])) {
					return true;
				}
			}
			return false;
		}
	}

	class CopyAction extends SelectionListenerAction {

		/**
		 * The id of this action.
		 */
		public static final String ID = PlatformUI.PLUGIN_ID + ".CopyAction"; //$NON-NLS-1$

		/**
		 * The shell in which to show any dialogs.
		 */
		private Shell shell;

		/**
		 * System clipboard
		 */
		private Clipboard clipboard;

		/**
		 * Associated paste action. May be <code>null</code>
		 */
		private PasteAction pasteAction;

		/**
		 * Creates a new action.
		 * 
		 * @param shell
		 *            the shell for any dialogs
		 * @param clipboard
		 *            a platform clipboard
		 */
		public CopyAction(Shell shell, Clipboard clipboard) {
			super("Copy");
			Assert.isNotNull(shell);
			Assert.isNotNull(clipboard);
			this.shell = shell;
			this.clipboard = clipboard;
			setToolTipText("Copy");
			setId(CopyAction.ID);
			PlatformUI.getWorkbench().getHelpSystem().setHelp(this, "copy_action_context");
		}

		/**
		 * Creates a new action.
		 * 
		 * @param shell
		 *            the shell for any dialogs
		 * @param clipboard
		 *            a platform clipboard
		 * @param pasteAction
		 *            a paste action
		 * 
		 * @since 2.0
		 */
		public CopyAction(Shell shell, Clipboard clipboard, PasteAction pasteAction) {
			this(shell, clipboard);
			this.pasteAction = pasteAction;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.action.Action#run()
		 */
		@Override
		public void run() {
			/**
			 * The <code>CopyAction</code> implementation of this method defined on <code>IAction</code> copies the selected resources to the clipboard.
			 */
			List<?> selectedResources = getSelectedResources();
			IResource[] resources = (IResource[]) selectedResources.toArray(new IResource[selectedResources.size()]);

			// Get the file names and a string representation
			final int length = resources.length;
			int actualLength = 0;
			String[] fileNames = new String[length];
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < length; i++) {
				IPath location = resources[i].getLocation();
				// location may be null. See bug 29491.
				if (location != null) {
					fileNames[actualLength++] = location.toOSString();
				}
				if (i > 0) {
					buf.append("\n"); //$NON-NLS-1$
				}
				buf.append(resources[i].getName());
			}
			// was one or more of the locations null?
			if (actualLength < length) {
				String[] tempFileNames = fileNames;
				fileNames = new String[actualLength];
				for (int i = 0; i < actualLength; i++) {
					fileNames[i] = tempFileNames[i];
				}
			}
			setClipboard(resources, fileNames, buf.toString());

			// update the enablement of the paste action
			// workaround since the clipboard does not suppot callbacks
			if (pasteAction != null && pasteAction.getStructuredSelection() != null) {
				pasteAction.selectionChanged(pasteAction.getStructuredSelection());
			}
		}

		/**
		 * Set the clipboard contents. Prompt to retry if clipboard is busy.
		 * 
		 * @param resources
		 *            the resources to copy to the clipboard
		 * @param fileNames
		 *            file names of the resources to copy to the clipboard
		 * @param names
		 *            string representation of all names
		 */
		private void setClipboard(IResource[] resources, String[] fileNames, String names) {
			try {
				// set the clipboard contents
				if (fileNames.length > 0) {
					clipboard.setContents(new Object[] { resources, fileNames, names }, new Transfer[] { ResourceTransfer.getInstance(), FileTransfer.getInstance(), TextTransfer.getInstance() });
				} else {
					clipboard.setContents(new Object[] { resources, names }, new Transfer[] { ResourceTransfer.getInstance(), TextTransfer.getInstance() });
				}
			} catch (SWTError e) {
				if (e.code != DND.ERROR_CANNOT_SET_CLIPBOARD) {
					throw e;
				}
				if (MessageDialog.openQuestion(shell, "Problem Copying to Clipboard", "There was a problem when accessing the system clipboard. Retry?")) {
					setClipboard(resources, fileNames, names);
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.ui.actions.BaseSelectionListenerAction#updateSelection (org.eclipse.jface.viewers.IStructuredSelection)
		 */
		@Override
		protected boolean updateSelection(IStructuredSelection selection) {

			/**
			 * The <code>CopyAction</code> implementation of this <code>SelectionListenerAction</code> method enables this action if one or more resources of compatible types are selected.
			 */

			if (!super.updateSelection(selection)) {
				return false;
			}

			if (getSelectedNonResources().size() > 0) {
				return false;
			}

			List<?> selectedResources = getSelectedResources();
			if (selectedResources.size() == 0) {
				return false;
			}

			boolean projSelected = selectionIsOfType(IResource.PROJECT);
			boolean fileFoldersSelected = selectionIsOfType(IResource.FILE | IResource.FOLDER);
			if (!projSelected && !fileFoldersSelected) {
				return false;
			}

			// selection must be homogeneous
			if (projSelected && fileFoldersSelected) {
				return false;
			}

			// must have a common parent
			IContainer firstParent = ((IResource) selectedResources.get(0)).getParent();
			if (firstParent == null) {
				return false;
			}

			Iterator<?> resourcesEnum = selectedResources.iterator();
			while (resourcesEnum.hasNext()) {
				IResource currentResource = (IResource) resourcesEnum.next();
				if (!currentResource.getParent().equals(firstParent)) {
					return false;
				}
			}

			return true;
		}

	}
}
