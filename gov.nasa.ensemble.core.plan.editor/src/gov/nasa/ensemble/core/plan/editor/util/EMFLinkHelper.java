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
package gov.nasa.ensemble.core.plan.editor.util;

import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.navigator.ILinkHelper;

public class EMFLinkHelper implements ILinkHelper {

	@Override
	public IStructuredSelection findSelection(IEditorInput anInput) {
		// this should be taken care of by the resource linking
		return null;
	}

	@Override
	public void activateEditor(IWorkbenchPage aPage,
			IStructuredSelection aSelection) {
		EObject selectedObject = (EObject) aSelection.getFirstElement();
		String selectedObjectUriFragment = EcoreUtil.getID(selectedObject);
		List<IEditorPart> editors = EditorPartUtils.getEditors();
		IEditorPart bestSoFar = null;
		//int lowestScoreSoFar = Integer.MAX_VALUE;
		for (IEditorPart editor : editors) {
			Object adapter = editor.getAdapter(EObject.class);
			if(adapter instanceof Resource) {
				Resource eResource = (Resource) adapter;
				
				EObject foundObject = eResource.getEObject(selectedObjectUriFragment);
				if(foundObject != null) {
					bestSoFar = editor;
				}
			} else if(adapter instanceof EObject) {
				EObject eObject = (EObject)adapter;
				Resource eResource = eObject.eResource();
				EObject foundObject = eResource.getEObject(selectedObjectUriFragment);
				if(foundObject != null) {
					bestSoFar = editor;
				}
			}
		}

		if (bestSoFar != null) {
			aPage.bringToTop(bestSoFar);
		}
	}
}
