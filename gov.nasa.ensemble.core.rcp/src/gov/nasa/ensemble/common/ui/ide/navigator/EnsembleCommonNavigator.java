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
package gov.nasa.ensemble.common.ui.ide.navigator;

import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.ICommonActionConstants;

/**
 * To add "Link with Editor" support, you must use the org.eclipse.ui.navigator.viewer
 * extension point. You will need a viewerContentBinding; inside of it, you would need
 * to specify an include of type contextExtension where the pattern should match
 * gov.nasa.ensemble.common.ui.ide.navigator.EnsembleLinkHelper
 * 
 * If you wish specify different functionality for the ensemble link helper, simply
 * extend the EnsembleLinkHelper class in your plugin and override the appropriate methods.
 * 
 * To modify the behavior when clicking editors, you could override the getPartListener()
 * method.
 * @author Eugene Turkov
 *
 */
public class EnsembleCommonNavigator extends CommonNavigator {
	
	private CommonViewer viewer;
	
	@Override
	protected CommonViewer createCommonViewer(Composite parent) {
		viewer = super.createCommonViewer(parent);
		WidgetUtils.runInDisplayThread(viewer.getControl(), new Runnable() {
			@Override
			public void run() {
				viewer.refresh();
			}
		});
		return viewer;
	}
	
	@Override
	protected IAdaptable getInitialInput() {
		return ResourcesPlugin.getWorkspace().getRoot();			
	}

	/**
	 * An editor has been activated.  Sets the selection in this navigator
	 * to be the editor's input, if linking is enabled.
	 * 
	 * @param editor the active editor
	 * @since 2.0
	 */
	protected void editorActivated(IEditorPart editor) {
		if (!isLinkingEnabled()) {
			return;
		}
		IStructuredSelection structuredSelection = null;
		/*
		 * If an editor is editing a component of an IFile, but not the IFile directly, then
		 * the object returned by getAdapter(IEditorInput.class) should return the object being edited.
		 */
		Object adapter = editor.getAdapter(IEditorInput.class);
		if (adapter == null) {
			// select the resource being edited.
			IEditorInput editorInput = editor.getEditorInput();
			adapter = editorInput.getAdapter(IResource.class);
		}
		if (adapter == null) {
			// the editor input may be outside the workspace (e.g., editing an equipment model)
			return;
		}
		structuredSelection = new StructuredSelection(adapter);
		ISelection selection = this.getSite().getSelectionProvider().getSelection();
		if(!selection.equals(structuredSelection)) {
			this.selectReveal(structuredSelection);
		}
	}
	
	@Override
	public void selectReveal(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection)selection;
			List<Object> itemsToSelect = new ArrayList<Object>();
			List<?> list = structuredSelection.toList();
			for (Object object : list) {
				if (object instanceof IResource) {
					IResource resource = (IResource)object;
					while (resource != null) {
						if (resource.exists()) {
							itemsToSelect.add(resource);
							break;
						}
						resource = resource.getParent();
					}
				} else {
					itemsToSelect.add(object);
				}
			}
			if (!itemsToSelect.isEmpty()) {
				super.selectReveal(new StructuredSelection(itemsToSelect));
			}
		}
	}	
	
	/**
	 * Utility method to get all open views that are instances of CommonNavigator
	 * @param clazz the class type which extends EnsembleCommonNavigator
	 * @return the list of viewers that are of type EnsembleCommonNavigator
	 */
	public static List<EnsembleCommonNavigator> getExistingInstances(Class<? extends EnsembleCommonNavigator> clazz) {
		List<EnsembleCommonNavigator> commonNavigators = new ArrayList<EnsembleCommonNavigator>();
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow[] workbenchWindows = workbench.getWorkbenchWindows();
		for(IWorkbenchWindow workbenchWindow : workbenchWindows) {
			IWorkbenchPage[] pages = workbenchWindow.getPages();
			for(IWorkbenchPage page : pages) {
				IViewReference[] viewReferences = page.getViewReferences();							
				for(IViewReference viewReference : viewReferences) {
					IViewPart view = viewReference.getView(false);
						if(view != null) {
							boolean assignableFrom = clazz.isAssignableFrom(view.getClass());
							if(assignableFrom) {
								commonNavigators.add((EnsembleCommonNavigator)view);
							}
					}
				}
			}
		}
		
		return commonNavigators;
	}

	@Override
	protected void handleDoubleClick(DoubleClickEvent anEvent) {
		IAction openHandler = getViewSite().getActionBars().getGlobalActionHandler(ICommonActionConstants.OPEN);
		if(openHandler == null) {
			super.handleDoubleClick(anEvent);
		} else {
			openHandler.run();
		}
	}			
}
