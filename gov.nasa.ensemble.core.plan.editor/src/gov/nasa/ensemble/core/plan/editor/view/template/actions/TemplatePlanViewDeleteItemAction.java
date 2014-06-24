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
package gov.nasa.ensemble.core.plan.editor.view.template.actions;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

/**
 * Action for deleting selected activities from a  template plan.
 */
public class TemplatePlanViewDeleteItemAction extends TemplatePlanViewAction {
	
	/**
	 * Present a confirmer message dialog. If the user accepts, delete all elements of the StructuredSelection.
	 * @param action ignored
	 */
	@Override
	public void run(IAction action) {		
		if(templatePlanView != null && templatePlanView.getCurrentTemplatePlan() != null) {
			Shell shell = templatePlanView.getSite().getShell();
			int size = structuredSelection.size();
			String templateWord = "template";
			if(size > 1) {
				templateWord += "s";
			}	
			String message = "You are about to delete " + size + " " + templateWord + ".\n" + 
			getAdditionalConfirmMessage(structuredSelection) + "This action is not undoable. Would you like to continue?";
			String title = "Deleting Template";
			boolean confirmed = MessageDialog.openConfirm(shell, title, message);
			if(confirmed) {
				delete(structuredSelection);
			}			
		}
	}

	protected String getAdditionalConfirmMessage(IStructuredSelection structuredSelection) {
		return "";
	}
	
	/**
	 * Delete from the model each element in the StructuredSelection and save the resource.
	 * @param structuredSelection
	 */
	public void delete(final IStructuredSelection structuredSelection) {
		final EPlan templatePlan = templatePlanView.getCurrentTemplatePlan();
		TransactionUtils.writing(templatePlan, new Runnable() {
			@Override
			public void run() {
				Set<Resource> resources = new HashSet<Resource>();			
				List list = structuredSelection.toList();
				for(Object object : list) {					
					if(object instanceof EObject) {
						EObject eObject = (EObject)object;
						if (eObject.eResource() == null)
							continue;
						closeOpenEditor(eObject);
						List<EReference> containmentReferences = new ArrayList<EReference>(eObject.eClass().getEAllContainments());
						for (EReference eReference : containmentReferences) {
							Object target = eObject.eGet(eReference, false);
							if (eReference.isMany()) {
								if (target instanceof InternalEList) {
									InternalEList internalEList = (InternalEList) target;
									for (Object value : internalEList) {
										if (value instanceof InternalEObject) {
											InternalEObject internalEObject = (InternalEObject) value;
											if (internalEObject.eResource() != eObject.eResource()) {
												resources.add(internalEObject.eResource());
												EcoreUtil.delete(internalEObject);
											}
										}
									}
								}
								
							} else {
								if (target instanceof InternalEObject) {
									InternalEObject internalEObject = (InternalEObject) target;
									if (internalEObject.eResource() != eObject.eResource()) {
										resources.add(internalEObject.eResource());
										EcoreUtil.delete(internalEObject);
									}
								}
							}
						}
						
						performAdditionalCleanup(eObject);
						
						EcoreUtil.delete(eObject);
					}					
				}
			
				TemplatePlanUtils.saveTemplatePlan(templatePlan);
				for(Resource resource : resources) {
					TemplatePlanUtils.saveTemplateResource(resource);
				}
			}
		});				
	}	
	
	protected void closeOpenEditor(EObject eObject) {
		URI uri = eObject.eResource().getURI().appendFragment(eObject.eResource().getURIFragment(eObject));
		URIEditorInput uriEditorInput = new URIEditorInput(uri);
		IWorkbenchPage activePage = templatePlanView.getSite().getPage();
		IEditorPart editorPart = activePage.findEditor(uriEditorInput);
		activePage.closeEditor(editorPart, false);
	}
	
	protected void performAdditionalCleanup(EObject eObject) {
		// do nothing
	}
	
}
