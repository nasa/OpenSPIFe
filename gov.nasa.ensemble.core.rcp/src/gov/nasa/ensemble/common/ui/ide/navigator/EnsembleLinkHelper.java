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

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.ILinkHelper;

public class EnsembleLinkHelper implements ILinkHelper {
	
	public static IWorkbenchPage lastPage = null;
	
	@Override
	public void activateEditor(IWorkbenchPage aPage,
			IStructuredSelection aSelection) {
		Iterator iterator = aSelection.iterator();

		/*
		 * The size of the selection will always be 1 so this is 
		 * probably a little bit overkill. But this works and we're going to leave it
		 * in case things change in the future.
		 */
		
		while (iterator.hasNext()) {
			Object element = iterator.next();
			if (element != null) {
				IEditorPart editorPart;
				try {
					editorPart = getEditorPart(element);
					if(editorPart != null) {
						aPage.bringToTop(editorPart);
					}
				} catch (PartInitException e) {
					LogUtil.error(e);
				}
			}
			/*
				Eugene says: this has the effect of taking
				focus away from the navigator so we probably
				don't want to make this call...
				editorPart.setFocus();
			 */
		}
	}

	public static IEditorPart getEditorPart(Object element) throws PartInitException {
		IEditorPart result = null;
		IWorkbench workbench = PlatformUI.getWorkbench();
		
		List<IWorkbenchWindow> workbenchWindows = null;
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		if(workbenchWindow != null) {
			workbenchWindows = Collections.singletonList(workbenchWindow);
		}
		
		else {
			workbenchWindows = Arrays.asList(workbench.getWorkbenchWindows());
		}
		
		lastPage = null;
		for(int i = 0; i < workbenchWindows.size(); i++) {
			workbenchWindow = workbenchWindows.get(i);
			
			IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
			List<IWorkbenchPage> pages = null;
			if(workbenchPage != null) {
				pages = Collections.singletonList(workbenchPage);
			}
			
			else {
				pages = Arrays.asList(workbenchWindow.getPages());
			}
			
			for(int j = 0; j < pages.size(); j++) {
				workbenchPage = pages.get(j);
				IEditorReference[] editorReferences = workbenchPage.getEditorReferences();
				for(IEditorReference editorReference : editorReferences) {
					IEditorInput editorInput = editorReference.getEditorInput();
					IEditorPart editor = editorReference.getEditor(false);
					if(editor != null && editorInput != null) {
						Object adapter = editorInput.getAdapter(IResource.class);
						if(adapter != null && adapter.equals(element)) {
							adapter = editor.getAdapter(IURIEditorInput.class);
							if(adapter == null) {
								result = editor;
								lastPage = workbenchPage;
								return result;
							}
						}
					}
				}			
			}
		}
		
		return result;
	}
	
	@Override
	public IStructuredSelection findSelection(IEditorInput input) {
		Object result = null;
		if(input instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput)input;
			result = fileEditorInput.getFile();
		}
		IStructuredSelection structuredSelection = null;
		if(result != null) {
			structuredSelection = new StructuredSelection(result);
		}
		return structuredSelection;
	}
}
