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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;

public class PlanEditorModelRegistry {

	private static final Map<IEditorInput, PlanEditorModel> inputMap = new HashMap<IEditorInput, PlanEditorModel>();
	private static final Map<PlanEditorModel, Set<IEditorPart>> editorMap = new HashMap<PlanEditorModel, Set<IEditorPart>>();
	private static final Map<EditingDomain, PlanEditorModel> editingDomainMap = new HashMap<EditingDomain, PlanEditorModel>();

	public static PlanEditorModel createPlanEditorModel(IEditorInput input) throws Exception {
		ResourceSet resourceSet = createTransactionalResourceSet(input);
		URI uri = EditUIUtil.getURI(input);
		EPlan plan = EPlanUtils.loadPlanIntoResourceSetWithErrorChecking(resourceSet, uri, null);
		PlanEditorModel model = new PlanEditorModel(plan);
		PlanEditorModelRegistry.registerPlanEditorModel(input, model);
		return model;
	}

	private static ResourceSet createTransactionalResourceSet(IEditorInput input) {
		ResourceSet resourceSet = TransactionUtils.createTransactionResourceSet(false);
		IFile file = CommonUtils.getAdapter(input, IFile.class);
		if (file != null) {
			IProject project = file.getProject();
			if (project != null) {
				resourceSet.setURIConverter(new ProjectURIConverter(project));
			}
		}
		return resourceSet;
	}
	
	/**
	 * Return the current plan editor input in the default workbench
	 * @return
	 */
	public static PlanEditorModel getCurrent() {
		if (PlatformUI.isWorkbenchRunning()) {
			return getCurrent(PlatformUI.getWorkbench());
		}
		return null;
	}
	
	/**
	 * Return the current plan editor input in the specified workbench
	 * @param workbench
	 * @return
	 */
	public static PlanEditorModel getCurrent(IWorkbench workbench) {
		if (workbench != null) {
			return PlanEditorModelRegistry.getCurrent(workbench.getActiveWorkbenchWindow());
		}
		return null;
	}
	
	/**
	 * Return the current plan editor input in the specified workbench window
	 * @param window
	 * @return
	 */
	public static PlanEditorModel getCurrent(IWorkbenchWindow window) {
		if (window != null) {
			IEditorPart editPart = EditorPartUtils.getCurrent(window);
			if (editPart != null) {
				return PlanEditorModelRegistry.getPlanEditorModel(editPart.getEditorInput());
			}
		}
		return null;
	}
	
	public static PlanEditorModel getPlanEditorModel(EditingDomain domain) {
		return editingDomainMap.get(domain);
	}
	
	/**
	 * Given an IEditorInput, it returns the associated
	 * PlanEditorModel. This is done for the purposes
	 * of sharing a data model for particular inputs.
	 * Null is returned if none has been found. 
	 * @param input to seek {@link PlanEditorModel} for
	 * @return {@link PlanEditorModel} for the input if it exists, null otherwise
	 */
	public static PlanEditorModel getPlanEditorModel(IEditorInput input) {
		return inputMap.get(input);
	}
	
	/**
	 * Register the {@link PlanEditorModel} so that it may be referenced
	 * by other 
	 * @param input
	 * @param planEditorModel
	 * @throws RegistrationException 
	 */
	public static void registerPlanEditorModel(IEditorInput input, PlanEditorModel planEditorModel) throws RegistrationException {
		if (inputMap.containsKey(input)) {
			throw new RegistrationException("PlanEditorModel already exists for '"+input+"'");
		}
		editingDomainMap.put(planEditorModel.getEditingDomain(), planEditorModel);
		inputMap.put(input, planEditorModel);
	}
	
	public static void unregisterInput(IEditorInput input) {
		if (inputMap.containsKey(input)) {
			PlanEditorModel model = inputMap.get(input);
			Set<IEditorPart> set = editorMap.get(model);
			if (set != null) {
				for (IEditorPart part : set) {
					IEditorSite site = part.getEditorSite();
					if (site != null) {
						IWorkbenchPage page = site.getPage();
						if (page != null) {
							page.closeEditor(part, true);
						}
					}
				}
			}
			editorMap.remove(model);
			inputMap.remove(input);
			editingDomainMap.remove(model.getEditingDomain());
		}
	}
	
	public static void registerEditor(PlanEditorModel planEditorModel, IEditorPart part) {
		Set<IEditorPart> set = editorMap.get(planEditorModel);
		if (set == null) {
			set = new HashSet<IEditorPart>();
			editorMap.put(planEditorModel, set);
		}
		set.add(part);
	}
	
	/**
	 * Remove editor from the list of editors open on this input.
	 * If this was the last editor, dispose the input.
	 * 
	 * Editors should remove themselves from the list when they receive
	 * they receive a setInput and this input was the old input.
	 * 
	 * Editors should also perform a setInput(null) in their dispose()
	 * to ensure that this call is made. 
	 * 
	 * @param editor
	 */
	public static void unregisterEditor(PlanEditorModel planEditorModel, IEditorPart part) {
		Set<IEditorPart> set = editorMap.get(planEditorModel);
		try {
			if (set == null) {
				planEditorModel.dispose();
				inputMap.remove(part.getEditorInput());
			} else {
				set.remove(part);
				if (set.size() == 0) {
					editorMap.remove(planEditorModel);
					inputMap.remove(part.getEditorInput());
					editingDomainMap.remove(planEditorModel.getEditingDomain());
					planEditorModel.dispose();
				}
			}
			System.gc(); // eagerly garbage collect
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			Logger.getLogger(PlanEditorModelRegistry.class).error("removeEditor", t);
		}
	}

	public static class RegistrationException extends Exception {

		private static final long serialVersionUID = 9038127065909091270L;

		public RegistrationException(String message) {
			super(message);
		}
		
	}

	public static boolean isAcceptable(IEditorInput editorInput) {
	    return editorInput instanceof IFileEditorInput
	    		|| editorInput instanceof FileStoreEditorInput;
    }
	
}
