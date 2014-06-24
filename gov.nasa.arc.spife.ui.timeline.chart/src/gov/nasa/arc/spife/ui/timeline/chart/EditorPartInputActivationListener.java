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
package gov.nasa.arc.spife.ui.timeline.chart;

import gov.nasa.ensemble.common.CommonUtils;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

public class EditorPartInputActivationListener  implements IPartListener, IWindowListener, IResourceChangeListener {

	/** Cache of the active workbench part. */
	private IWorkbenchPart fActivePart;
	/** Indicates whether activation handling is currently be done. */
	private boolean fIsHandlingActivation= false;
	/**
	 * The part service.
	 * @since 3.1
	 */
	private IPartService fPartService;

	private EditorPart fEditor;

	private long fTimestamp = -1;

	@SuppressWarnings("unused")
	private boolean fHasBeenActivated= false;
	
	/**
	 * Creates this activation listener.
	 *
	 * @param editor the part service on which to add the part listener
	 * @since 3.1
	 */
	public EditorPartInputActivationListener(EditorPart editor) {
		fEditor= editor;
		fPartService= editor.getSite().getWorkbenchWindow().getPartService();
		fPartService.addPartListener(this);
		PlatformUI.getWorkbench().addWindowListener(this);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);		
	}

	public void refresh() {
		if (fEditor != null) {
			IEditorInput input = fEditor.getEditorInput();
			IFile file = CommonUtils.getAdapter(input, IFile.class);
			fTimestamp = file.getLocalTimeStamp();
		}
	}
	
	/**
	 * Disposes this activation listener.
	 *
	 * @since 3.1
	 */
	public void dispose() {
		fPartService.removePartListener(this);
		PlatformUI.getWorkbench().removeWindowListener(this);
		fPartService= null;
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		fEditor = null;
		fActivePart = null;
	}

	/*
	 * @see IPartListener#partActivated(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void partActivated(IWorkbenchPart part) {
		fActivePart= part;
		handleActivation();
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) { /* no implementation */ }
	@Override
	public void partClosed(IWorkbenchPart part) { /* no implementation */ }
	@Override
	public void partOpened(IWorkbenchPart part) { /* no implementation */ }

	/*
	 * @see IPartListener#partDeactivated(org.eclipse.ui.IWorkbenchPart)
	 */
	@Override
	public void partDeactivated(IWorkbenchPart part) {
		fActivePart= null;
	}

	/**
	 * Handles the activation triggering a element state check in the editor.
	 */
	private void handleActivation() {
		if (fIsHandlingActivation)
			return;

		if (fEditor != null && fActivePart == fEditor) {
			fIsHandlingActivation= true;
			try {
				sanityCheckState(fEditor.getEditorInput());
			} finally {
				fIsHandlingActivation= false;
			}
		}
		fHasBeenActivated= true;
	}

	/*
	 * @see org.eclipse.ui.IWindowListener#windowActivated(org.eclipse.ui.IWorkbenchWindow)
	 * @since 3.1
	 */
	@Override
	public void windowActivated(IWorkbenchWindow window) {
		if (window == fEditor.getEditorSite().getWorkbenchWindow()) {
			/*
			 * Workaround for problem described in
			 * http://dev.eclipse.org/bugs/show_bug.cgi?id=11731
			 * Will be removed when SWT has solved the problem.
			 */
			window.getShell().getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					handleActivation();
				}
			});
		}
	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) { /* no implementation */ }
	@Override
	public void windowClosed(IWorkbenchWindow window) { /* no implementation */ }
	@Override
	public void windowOpened(IWorkbenchWindow window) { /* no implementation */ }
	
	/**
	 * Checks the state of the given editor input.
	 * @param input the editor input whose state is to be checked
	 * @since 2.0
	 */
	protected void sanityCheckState(IEditorInput input) {
		IFile file = CommonUtils.getAdapter(input, IFile.class);
		long timestamp = file.getLocalTimeStamp();
		if (fTimestamp != timestamp) {
			fTimestamp = timestamp;
			handleEditorInputChanged();
		}
	}
	
	/**
	 * Handles an external change of the editor's input element. Subclasses may
	 * extend.
	 */
	protected void handleEditorInputChanged() {
		@SuppressWarnings("unused")
		Shell shell= fEditor.getSite().getShell();

		IEditorInput editorInput = fEditor.getEditorInput();
		final IFile file = CommonUtils.getAdapter(editorInput, IFile.class);
		if (file == null) {
			return;
		}

//		if (false && file.exists()) {
//			String title = "File Changed";
//			String msg = "The file has been changed on the file system. Do you want to replace the editor contents with these changes?";
//			if (fHasBeenActivated /*&& MessageDialog.openQuestion(shell, title, msg) */) {
//				WidgetUtils.runLaterInDisplayThread(shell, new Runnable() {
//					public void run() {
//						IWorkbench workbench = PlatformUI.getWorkbench();
//						IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
//						final IWorkbenchPage page = window.getActivePage();
//						ForbiddenWorkbenchUtils.activateEmptyEditorWorkbook(page);
//						page.closeEditor(fEditor, true);
//						try {
//							IDE.openEditor(page, file);
//						} catch (PartInitException e) {
//							Logger.getLogger(EditorPartInputActivationListener.class).error("opening editor for "+file, e);
//						}
//					}
//				});
//			}
//		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		processDelta(event.getDelta());
	}

	private void processDelta(IResourceDelta delta) {
		int eventKind = delta.getKind();
		IResource resource = delta.getResource();
		IFile targetFile = CommonUtils.getAdapter(fEditor.getEditorInput(), IFile.class);
		if (IResourceDelta.REMOVED == eventKind && isDescendant(resource, targetFile)) {
			fEditor.getSite().getPage().closeEditor(fEditor, false);
			return;
		}
		//
		// Recurse
		for (IResourceDelta child : delta.getAffectedChildren()) {
			processDelta(child);
		}
	}

	private boolean isDescendant(IResource resource, IFile targetFile) {
		if (resource instanceof IFile) {
			return resource.equals(targetFile);
		}
		if (resource instanceof IContainer) {
			IPath path = resource.getFullPath();
			return path.isPrefixOf(targetFile.getFullPath());
		}
		return false;
	}
	
}
