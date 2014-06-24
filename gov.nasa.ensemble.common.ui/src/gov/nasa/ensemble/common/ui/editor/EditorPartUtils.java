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
package gov.nasa.ensemble.common.ui.editor;

import gov.nasa.ensemble.common.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;

public class EditorPartUtils {

	/**
	 * Return the current plan editor input in the default workbench.
	 * 
	 * @return the current plan editor input in the default workbench.
	 * @deprecated call one of the more specific getCurrent methods,
	 *             or properly get the target editor if you have some
	 *             other association with it 
	 */
	@Deprecated
	public static IEditorPart getCurrent() {
		return getCurrent(PlatformUI.getWorkbench());
	}
	
	/**
	 * Return the current plan editor input in the specified workbench.
	 * 
	 * @param workbench -- defaults to global one if passed a null value
	 * @return the current plan editor input in the specified workbench
	 */
	public static IEditorPart getCurrent(IWorkbench workbench) {
		if (workbench == null) {
			workbench = PlatformUI.getWorkbench();
		}
		return getCurrent(workbench.getActiveWorkbenchWindow());
	}
	
	/**
	 * Return the current plan editor input in the specified workbench window.
	 * 
	 * @param window
	 * @return the current plan editor input in the specified workbench window
	 */
	public static IEditorPart getCurrent(IWorkbenchWindow window) {
		if (window != null) {
			IWorkbenchPage workbenchPage = window.getActivePage();
			if (workbenchPage != null) {
				return workbenchPage.getActiveEditor();
			}
		}
		return null;
	}
	
	/**
	 * Return the current plan editor input in the specified workbench.
	 * 
	 * @param site -- defaults to global workbench if passed a null value
	 * @return the current plan editor input in the specified site's workbench
	 */
	public static IEditorPart getCurrent(IWorkbenchPartSite site) {
		if (site==null) return getCurrent(PlatformUI.getWorkbench());
		else return getCurrent(site.getWorkbenchWindow());
	}
	

	/**
	 * Utility method to get the adapter from the current editor part. The
	 * adapter parameter has been parameterized for added convenience.
	 */
	public static <T> T getAdapter(Class<T> adapter) {
		return getAdapter(getCurrent(), adapter);
	}

	/**
	 * Utility method to get the adapter from the editor part. The
	 * adapter parameter has been parameterized for added convenience. 
	 */
	public static <T> T getAdapter(IEditorPart editor, Class<T> adapter) {
		return CommonUtils.getAdapter(editor, adapter);
	}
	
	public static <T> T getAdapter(IWorkbench workbench, Class<T> adapter) {
		return getAdapter(getCurrent(workbench), adapter);
	}
	
	public static <T> T getAdapter(IWorkbenchWindow window, Class<T> adapter) {
		return getAdapter(getCurrent(window), adapter);
	}
	
	public static IEditorPart getEditor(IEditorInput input) {
		IWorkbenchPage activePage = getActivePage();
		if (activePage != null) {
			return activePage.findEditor(input);
		}
		return null;
	}	

	/**
	 * Return ALL editors.
	 * @return all editors - doesn't matter if active workbench window or active page is null.
	 */
	public static List<IEditorPart> getEditors() {
		List<IEditorPart> editors = new ArrayList<IEditorPart>();
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
		for(IWorkbenchWindow workbenchWindow : workbenchWindows) {
			IWorkbenchPage[] pages = workbenchWindow.getPages();
			for(IWorkbenchPage page : pages) {
				if(page != null) {
					IEditorReference[] editorReferences = page.getEditorReferences();
					for(IEditorReference eReference : editorReferences) {
						IEditorPart editor = eReference.getEditor(false);
						if(editor != null) {
							editors.add(editor);
						}
					}
				}
			}			
		}
		return editors;	
	}
	
	public static IProject getSelectedProject() {
		return getSelectedProject(PlatformUI.getWorkbench(), getCurrentSelection());
	}

	public static IProject getSelectedProject(IWorkbench workbench, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			return getSelectedProject(workbench, ((IStructuredSelection) selection));
		} else {
			return getSelectedProject(workbench, null);
		}
	}
	
	public static IProject getSelectedProject(IWorkbench workbench, IStructuredSelection selection) {
		// Per SPF-6072: Check first for (a) if there's only one project or (b) select based on active workbench window (e.g. an Editor).
		// Otherwise, default project based on the navigator selection.
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject[] projects = root.getProjects();
		if (projects.length == 0)
			return null;
		else if (projects.length == 1)
			return projects[0];
		
    	IEditorPart activeEditor = EditorPartUtils.getCurrent(workbench);
    	if (activeEditor != null) {
    		IEditorInput editorInput = activeEditor.getEditorInput();
    		if (editorInput != null) {
				IFile iFile = (IFile) editorInput.getAdapter(IFile.class);
	    		if (iFile != null) {
	    			return iFile.getProject();
	    		}
    		}
    	} else {
    		if (selection != null && selection.getFirstElement() instanceof IProject)
    			return (IProject) selection.getFirstElement();
    	}
    	return null;
	}
	
	public static IStructuredSelection getCurrentSelection() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench==null || workbench.getActiveWorkbenchWindow()==null) {
			return StructuredSelection.EMPTY;
		}
		IStructuredSelection structuredSelection = StructuredSelection.EMPTY;
		IViewReference[] viewReferences = workbench.getActiveWorkbenchWindow()
				.getActivePage().getViewReferences();
		IViewPart viewPart = null;
		for (IViewReference viewReference : viewReferences) {
			IViewPart view = viewReference.getView(false);
			if (view instanceof CommonViewer) {
				viewPart = view;
			}
		}

		if (viewPart != null) {
			ISelectionProvider selectionProvider = viewPart.getSite()
					.getSelectionProvider();
			ISelection selection = selectionProvider.getSelection();
			if (selection instanceof IStructuredSelection) {
				structuredSelection = (IStructuredSelection) selection;
			}
		}
		return structuredSelection;
	}
	
	/** Tries to find the currently selected project in the currently selected editor or view,
	 * if the editor is some kind of file editor or the view is a navigator.
	 * @param window
	 * @return null if can't find a project, else a project.
	 */
	public static IProject getSelectedProject(IWorkbenchWindow window) {
		ISelection selection = null;
		IWorkbenchPart part = window.getPartService().getActivePart();
		if (part instanceof CommonViewer) {
			selection = ((CommonViewer) part).getSelection();
		} else if (part instanceof CommonNavigator) {
			selection = ((CommonNavigator) part).getCommonViewer().getSelection();
		} else if (part instanceof IEditorPart) {
			IEditorInput editorInput = ((IEditorPart) part).getEditorInput();
			if (editorInput instanceof FileEditorInput) return ((FileEditorInput) editorInput).getFile().getProject();
		}
		if(selection != null && selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			Object firstElement = structuredSelection.getFirstElement();
			if (firstElement instanceof IProject) return ((IProject) firstElement);
			if (firstElement instanceof IFile) return ((IFile) firstElement).getProject();
		}
		return null;
	}
	
	/**
	 * @deprecated get the appropriate page from your local context instead
	 * @return
	 */
	@Deprecated
	public static IWorkbenchPage getActivePage() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchPage activePage = null;
		if (workbench != null) {
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null) {
				activePage = window.getActivePage();
			}
		}
		return activePage;
	}
}
