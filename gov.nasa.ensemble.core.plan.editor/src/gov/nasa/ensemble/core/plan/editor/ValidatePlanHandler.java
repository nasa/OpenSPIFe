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
package gov.nasa.ensemble.core.plan.editor;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edit.ui.action.ValidateAction.EclipseResourcesUtil;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ISetSelectionTarget;

public class ValidatePlanHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {
		IWorkbenchPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		if (part != null && part instanceof MultiPagePlanEditor) {
			MultiPagePlanEditor editor = (MultiPagePlanEditor) part;
			validate(editor.getPlan());
		}
		return null;
	}
	
	private void validate(EObject element) {
		Diagnostic diagnostic = null;
		// Validate the contents of the loaded resource.
		//
		for (EObject eObject : element.eResource().getContents()) {
			diagnostic = Diagnostician.INSTANCE.validate(eObject);
//			if (diagnostic.getSeverity() != Diagnostic.OK) {
//				printDiagnostic(diagnostic, "");
//			}
		}
		Viewer viewer = null;
		if (diagnostic != null) {
			EclipseResourcesUtil resourcesUtil = new EclipseResourcesUtil();
			resourcesUtil.deleteMarkers(element.eResource());
			for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
				if (!childDiagnostic.getData().isEmpty()) {
					resourcesUtil.createMarkers(element.eResource(), childDiagnostic);
				}
			}

			// select the first object with problem in the viewer if possible
			//
			if (!diagnostic.getChildren().isEmpty()) {
				List<?> data = (diagnostic.getChildren().get(0)).getData();
				if (!data.isEmpty()) {
					Object part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
					Object e = data.get(0);
					if (part instanceof ISetSelectionTarget) {
						((ISetSelectionTarget) part).selectReveal(new StructuredSelection(e));
					} else if (part instanceof IViewerProvider) {
						viewer = ((IViewerProvider) part).getViewer();
						if (viewer != null) {
							viewer.setSelection(new StructuredSelection(data.get(0)), true);
						}
					} 
//					else if (part instanceof IEditorPart) {
//	
//						IEditorPart editor = ((IEditorPart) part);
//						if (editor != null) {
//							if (e instanceof EObject) {
//								
//							editor.getEditorSite().getSelectionProvider().setSelection(new StructuredSelection(((EObject)e).eContainer()));
//							}
//						}
//					}
				}
			}
		}
		if (viewer == null) {
			Object part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
			if (part instanceof IViewerProvider) {
				viewer = ((IViewerProvider) part).getViewer();
			}
		}

	}

}
