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
package gov.nasa.ensemble.core.plan.editor.view.template;

import gov.nasa.ensemble.common.Registry;
import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.ide.IDE;

/**
 * Contains several static methods of general use in dealing with templates.
 * It might be more clear if this were declared to be an interface instead of an abstract class.
 */
public  class TemplatePlanUtils {
	/**
	 * Copies the source plan element. The copy will not be a part of any resource/resourceSet or model.
	 * 
	 * @param element the plan element to copy
	 * @return A copy of the original element. The copy will not be a part of any resource/resourceSet or model.
	 */
	public static EPlanElement getCopy(EPlanElement element, boolean keepIds) {
		PlanStructureModifier planStructureModifier = PlanStructureModifier.INSTANCE;
		PlanTransferable source = planStructureModifier.getTransferable(new StructuredSelection(element));
		PlanTransferable result = (PlanTransferable) planStructureModifier.copy(source);
		EPlanElement copy = result.getPlanElements().iterator().next();
		// Guard against calling with a null pointer
		final EObject copyData = copy.getData();
		if (copyData != null) {
			EMFUtils.unresolveProxies(copyData);
		}
		
		if (keepIds) {
			// SPF-6423 A new diffID is generated every time a Template activity is opened
			// fix needed so that the diff id doesn't change...
			CommonMember commonMember = copy.getMember(CommonMember.class);
			if (commonMember != null) {
				CommonMember planElementCommonMember = element.getMember(CommonMember.class);
				String diffID = planElementCommonMember.getDiffID();
				commonMember.setDiffID(diffID);
			}
			// end SPF-6423

			// Keep the persistentId in the copy as well
			copy.setPersistentID(element.getPersistentID());

		}

		return copy;
	}

	/**
	 * Create a new activity
	 * @param name the name of the activity to be created (as recorded in the AD).
	 */
	public static EActivity createNewActivity(String name) {
		EActivityDef def = ActivityDictionary.getInstance().getActivityDef(name);
		final EActivity activity = PlanFactory.getInstance().createActivity(def);
		return activity;
	}

	/**
	 * Launch an editor of the given class on a copy of the given plan element, using the active page of the
	 * current editor as the page in which the editor is opened; the URI of the template plan element as the
	 * editor input and the unique ID of the TemplatePlanElementEditor as the editor extension.
	 * @param element the plan element to open
	 * @param uriRegistry a map from URI to EPlanElement; assure that the copy of the element is the value of
	 * its URI key
	 * @param editorClass the class of the editor to launch
	 */
	public static void openElementInEditor(EPlanElement element, Registry uriRegistry, Class<?> editorClass) {
		final Resource resource = element.eResource();
		if (resource != null) {
			URI resourceURI = resource.getURI();
			String uriFragment = resource.getURIFragment(element);
			// we will use the elementURI as the input for the editor.
			URI elementURI = resourceURI.appendFragment(uriFragment);
			IEditorInput editorInput = new URIEditorInput(elementURI);
			IWorkbenchPage activePage = EditorPartUtils.getActivePage();
			String editorId = ClassIdRegistry.getUniqueID(editorClass);
			try {
				/* we are making a copy because if we use the original any changes will affect the host plan. */
				// this copy will later be added to another model, until then it is "disconnected"
				EPlanElement copy = getCopy(element, true);
				// place a copy of the currently selected element into the registry

				/*
				 * check if the element is already there because if it is, we risk disconnecting an open editor's input with the model
				 */
				if (!uriRegistry.isRegistered(elementURI)) {
					// later, the editor can use the registry to retrieve the model(copy)
					uriRegistry.register(elementURI, copy);
					/*
					 * we want to know when the "copy" changes, so we add a model change listener. the method checks to make sure a listener is not added more than once
					 */
				}

				IDE.openEditor(activePage, editorInput, editorId);
			} catch (PartInitException e) {
				LogUtil.error(e);
			}
		}
	}
	
	/**
	 * Launch a TemplatePlanElementEditor on a copy of the given plan element, using the active page of the
	 * current editor as the page in which the editor is opened; the URI of the template plan element as the
	 * editor input and the unique ID of the TemplatePlanElementEditor as the editor extension.
	 * @param element the element to open; of interest only if it is a model object
	 * @param uriRegistry a map from URI to EPlanElement; assure that the copy of the element is the value of
	 * its URI key
	 */
	public static void openElementInTemplatePlanElementEditor(EPlanElement element, Registry uriRegistry) {
		openElementInEditor(element, uriRegistry, TemplatePlanElementEditor.class);
	}

	/**
	 * Save the template plan to its containing resource.
	 * @param templatePlan a template plan; must not be null
	 */
	public static void saveTemplatePlan(final EPlan templatePlan) {
		saveTemplateResource(templatePlan.eResource());
	}
	
	/**
	 * In a separate thread save the resource, which contains a template plan.
	 * @param resource the resource to save
	 */
	public static void saveTemplateResource(final Resource resource) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
					@Override
					protected void execute(IProgressMonitor monitor) {
						try {
							resource.save(null);
						} catch (IOException e) {
							LogUtil.error(e);
						}
					}
				};
				try {
					operation.run(null);
				} catch (InvocationTargetException e) {
					LogUtil.error(e);
				} catch (InterruptedException e) {
					LogUtil.error(e);
				}
			}
		};
		
		Thread thread = new Thread(runnable);
		thread.setName("template plan saver thread");
		thread.start();
	}
	
	public static void saveUpdatedTemplate(Resource templatePlanResource, final EPlanChild original, final EPlanChild updated) {
		TransactionUtils.writing(templatePlanResource, new Runnable() {
			@Override
			public void run() {
				EPlanParent parent = (EPlanParent)original.getParent();
				if(parent != null) {
					EList<EPlanChild> children = parent.getChildren();
					int index = children.indexOf(original);
					EPlanChild oldChild = children.remove(index);
					children.add(index, updated);
					if (oldChild != original) {
						LogUtil.error("holy cow transactions don't work");
					}
				} else {
					// not sure how this case would come about, but in case the parent is null, we can try to use the container
					LogUtil.warn("parent is null, using container to determine insertion index");
					EObject eContainer = original.eContainer();
					EList<EObject> contents = eContainer.eContents();
					int index = contents.indexOf(original);
					EObject oldChild = contents.remove(index);
					contents.add(index, updated);
					if (oldChild != original) {
						LogUtil.error("holy cow transactions don't work");
					}								
				}
			}
		});

		saveTemplateResource(templatePlanResource);
		
	}
	
	public static void saveNewTemplate(Resource templatePlanResource, final EPlan plan, final EPlanChild template) {
		TransactionUtils.writing(templatePlanResource, new Runnable() {
			@Override
			public void run() {
				plan.getChildren().add(template);
			}
		});
		
		saveTemplateResource(templatePlanResource);
		
	}
	
	/**
	 * Make a copy of the plan child and replace its members with URIs to
	 * unresolve all proxies
	 * @param planChild the plan child being copied
	 * @return the new copy of the plan child
	 */
	public static EPlanElement copyUnresolved(EPlanChild planChild) {
		final EPlanElement copy = TemplatePlanUtils.getCopy(planChild, true);
		for (EMember member : copy.getMembers()) {
			EMFUtils.unresolveProxies(member);
		}
		return copy;
	}

	public static void makeActivityReferenceAllTheFolders(EActivity activity, List<? extends EObject> folders) {
		EObject data = activity.getData();
		EClass eClass = data.eClass();
		EStructuralFeature groupsFeature = eClass.getEStructuralFeature("groups");
		if (groupsFeature != null) {
			data.eSet(groupsFeature, folders);
		}
	}

	/**
	 * Returns all folder models contained in the selection. If any non model items are found, an empty collection is returned.
	 * 
	 */
	public static List<DynamicEObjectImpl> getAllFolderModels(IStructuredSelection structuredSelection) {
		List<DynamicEObjectImpl> models = new ArrayList<DynamicEObjectImpl>();
		List list = structuredSelection.toList();
		for (Object object : list) {
			if (object instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) object;
				Object adapter = adaptable.getAdapter(DynamicEObjectImpl.class);
				if (adapter instanceof DynamicEObjectImpl) {
					models.add((DynamicEObjectImpl) adapter);
				} else {
					return Collections.emptyList();
				}
			}
		}
		return models;
	}

	/**
	 * Return the template plan associated wit the current editor's TemplatePlanPage. May be null.
	 */
	public static EPlan getCurrentTemplatePlan() {
		EPlan templatePlan = null;
		IEditorPart current = EditorPartUtils.getCurrent();
		Object adapter = current.getAdapter(TemplatePlanPage.class);
		if (adapter instanceof TemplatePlanPage) {
			TemplatePlanPage templatePlanPage = (TemplatePlanPage) adapter;
			templatePlan = templatePlanPage.getTemplatePlan();
		}
		return templatePlan;
	}

	public static TemplatePlanPage getTemplatePlanPage(IProject project) {
		TemplatePlanPage templatePlanPage = null;
		/*
		 * iterate through all workbench windows; both active and non-active to try to find an editor that is open in the same project as the project parameter and whose editor adapts to
		 * PlanEditorModel.class. Query the editor for the templatePlan since that is the appropriate editor for the given project.
		 */
		List<IEditorPart> editors = EditorPartUtils.getEditors();
		for (IEditorPart editor : editors) {
			Object adapter = editor.getAdapter(PlanEditorModel.class);
			if (adapter instanceof PlanEditorModel) {
				PlanEditorModel planEditorModel = (PlanEditorModel) adapter;
				// get the workspace file for the EPlan
				IFile file = (IFile) planEditorModel.getEPlan().getAdapter(IResource.class);
				IProject planProject = file.getProject();
				if (project.equals(planProject)) {
					adapter = editor.getAdapter(TemplatePlanPage.class);
					if (adapter instanceof TemplatePlanPage) {
						// the current editor supports template plans!
						templatePlanPage = (TemplatePlanPage) adapter;
						break;
					}
				}
			}
		}
		return templatePlanPage;
	}

	public static EPlan getTemplatePlan(IProject project) {
		TemplatePlanPage templatePlanPage = getTemplatePlanPage(project);
		EPlan templatePlan = null;
		if (templatePlanPage != null) {
			templatePlan = templatePlanPage.getTemplatePlan();
		}
		return templatePlan;
	}

	public static void checkTemplatePlanHealth(final EPlan plan) {
		if (plan == null)
			return;

		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor) {
				final TreeIterator<EObject> iter = EcoreUtil.getAllProperContents(plan.eResource(), false);
				while (iter.hasNext()) {
					InternalEObject element = (InternalEObject) iter.next();
					if (element.eProxyURI() == null) {
						// check for unresolved proxies in cross references
						List<EReference> containmentReferences = new ArrayList<EReference>(element.eClass().getEAllContainments());
						for (Iterator iterator = containmentReferences.iterator(); iterator.hasNext();) {
							EReference eReference = (EReference) iterator.next();
							Object value = element.eGet(eReference, false);
							if (eReference.isMany()) {
								if (value instanceof InternalEList) {
									InternalEList internalEList = (InternalEList) value;
									for (Iterator basicIterator = internalEList.basicIterator(); basicIterator.hasNext();) {
										String msg = checkProxy(element, eReference, basicIterator.next());
										if (msg != null) {
											LogUtil.error(msg);
										}
									}
								}
							} else {
								String msg = checkProxy(element, eReference, value);
								if (msg != null) {
									LogUtil.error(msg);
								}
							}
						}
					}
				}
			}
		};
		try {
			operation.run(null);
		} catch (InvocationTargetException e) {
			LogUtil.error(e);
		} catch (InterruptedException e) {
			LogUtil.error(e);
		}
	}

	/**
	 * If the object is a proxy, attempt to resolve it. On failure to resolve, return an
	 * error message string; otherwise return null
	 * @param element ignored
	 * @param ref ignored
	 * @param value the object being checked
	 * @return
	 */
	private static String checkProxy(InternalEObject element, EReference ref, Object value) {
		if (value instanceof InternalEObject) {
			InternalEObject internalEObject = (InternalEObject) value;
			if (internalEObject.eIsProxy()) {
				EObject resolved = element.eResolveProxy(internalEObject);
				String errMsg = null;
				if (resolved == internalEObject) {
					errMsg = "Unresolved proxy";
				} else if (!ref.getEType().isInstance(resolved)) {
					errMsg = "Invalid data";
				}
				if (errMsg != null) {
					return errMsg + " in '" + element.eResource().getURI().toFileString() + "': " + resolved;
				}
			}
		}
		return null;
	}
}
