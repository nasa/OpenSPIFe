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
package gov.nasa.arc.spife.ui.lifecycle;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.activityDictionary.provider.DynamicDictonaryAdapterFactory;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;
import gov.nasa.ensemble.dictionary.provider.DefinedObjectItemProviderAdapterFacory;
import gov.nasa.ensemble.emf.ProjectURIConverter;
import gov.nasa.ensemble.emf.resource.IgnorableResource;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.io.IOException;
import java.util.Comparator;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;

public class SpifeProjectUtils {

	public static final String TEMPLATE_PLAN_FILE = "template.plan";
	public static final String SCHEDULE_PLAN_FILE = "schedule.plan";
	
	/**
	 * This method first attempts to return the currently edited plan from the
	 * project, and if not, loads the project and returns the associated plan.
	 * 
	 * Requires the calling of disposeSchedule to avoid memory leaks.
	 * 
	 */
	public static EPlan getOrLoadScheduleFromProject(IProject project) throws IOException {
		if (project == null) {
			return null;
		}
		IFile scheduleFile = project.getFile(SCHEDULE_PLAN_FILE);
		if (Display.getCurrent() != null) {
			IEditorInput input = new FileEditorInput(scheduleFile);
			PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
			if (model != null) {
				return model.getEPlan();
			}
		}
		
		URI uri = EMFUtils.getURI(scheduleFile);
		EditingDomain domain = TransactionUtils.createTransactionEditingDomain(false);
		ResourceSet resourceSet = domain.getResourceSet();
		resourceSet.setURIConverter(new ProjectURIConverter(project));
		if (domain instanceof AdapterFactoryEditingDomain) {
			AdapterFactory f = ((AdapterFactoryEditingDomain)domain).getAdapterFactory();
			if (f instanceof ComposedAdapterFactory) {
				ComposedAdapterFactory factory = (ComposedAdapterFactory)f;
				factory.addAdapterFactory(new DefinedObjectItemProviderAdapterFacory());
				factory.addAdapterFactory(new DynamicDictonaryAdapterFactory());
			}
		}
		Resource resource = resourceSet.getResource(uri, true);
		resource.load(null);
		return (EPlan) resource.getContents().get(0);
	}
	
	/**
	 * This method is intended to be called in conjunction with 
	 * getOrLoadScheduleFromProject since there is some complexity regarding
	 * the loaded state of the plan in an editor.
	 * 
	 * The method effectively does nothing if the plan is in an editor currently.
	 * Otherwise, the plan's editing domain is disposed of if it is a transaction
	 * editing domain.
	 * 
	 */
	public static void disposeSchedule(EPlan plan) {
		Resource eResource = plan.eResource();
		if(eResource != null) {
			EditingDomain domain = EMFUtils.getAnyDomain(plan);
			if (domain != null) {
				PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(domain);
				if (model != null) {
					return;
				}
			}
		}
		TransactionalEditingDomain domain = TransactionUtils.getDomain(plan);
		if (domain != null) {
			domain.dispose();
		}
	}
	
	/**
	 * @return template.plan's EPlan contents, or null if file doesn't exist (use SpifeProjectUtils.doesProjectContainTemplate to avoid that)
	 */
	public static EPlan getTemplateFromProject(IProject project, EPlan schedule, IProgressMonitor monitor) throws Exception {
		EPlan templatePlan = null;
		URI templateUri = EMFUtils.getURI(project.getFile(TEMPLATE_PLAN_FILE));	
		//templatePlan = TemplatePlanUtils.getTemplatePlan(project);		
		if (templatePlan == null) {
			ResourceSet resourceSet = schedule.eResource().getResourceSet();
			templatePlan = getTemplatePlan(resourceSet, templateUri, monitor);
		}
		return templatePlan;
	}
	
	public static boolean doesProjectContainTemplate(IProject project) {
		IFile file = project.getFile(TEMPLATE_PLAN_FILE);
		try {
			IOUtils.closeQuietly(file.getContents());
			return true;
		} catch (CoreException e) {
			return false;
		}
	}

	/**
	 * @return template.plan's EPlan contents, or null if file doesn't exist
	 */
	public static EPlan getTemplatePlan(ResourceSet resourceSet, URI templateUri, IProgressMonitor monitor) throws Exception {
		Resource resource = resourceSet.getResource(templateUri, false);
		if (resource != null) {
			EList<EObject> contents = resource.getContents();
			if (contents==null || contents.isEmpty()) {
				return null;
			} else {
				return (EPlan)contents.get(0);
			}
		} else {
			return EPlanUtils.loadPlanIntoResourceSetWithErrorChecking(resourceSet, templateUri, monitor);
		}
	}

	public static SortedMap<String, String> getIdMap(EPlan schedule, URI uri) {
		return getIdMap(schedule, uri, null);
	}
	
	public static SortedMap<String, String> getIdMap(EPlan schedule, URI uri, Comparator<String> comparator) {
		SortedMap<String, String> idMap = new TreeMap<String, String>(comparator);
		EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(schedule);
		if(domain != null) {
			ResourceSet resourceSet = domain.getResourceSet();
			try {
				Resource resource = resourceSet.getResource(uri, true);
				EList<EObject> crewMembers = resource.getContents();
				for (EObject crewmember : crewMembers) {
					IItemLabelProvider adapter = EMFUtils.adapt(crewmember, IItemLabelProvider.class);
					String name =  adapter.getText(crewmember);
					String id = EcoreUtil.getID(crewmember);
					idMap.put(name, id);
				}
			} catch (WrappedException e) {
				LogUtil.error(e.getCause().getMessage());
			}
		}
		else {
			LogUtil.warn("getIdMap(): The IDs were not populated. Domain was null.");
		}
		return idMap;
	}
	
	/**
	 * Method to write an empty plan. This call should be made from within a workspace modify operation.
	 * @param root
	 * @param isTemplatePlan
	 * @return reference to a plan that has already been written(saved)
	 */
	public static EPlan writeNewEmptyPlan(IContainer root, final boolean isTemplatePlan) throws IOException {
		EPlan plan = null;
		IFile templatePlanFile = root.getFile(new Path(TEMPLATE_PLAN_FILE));
		if (!templatePlanFile.exists()) {
			URI uri = EMFUtils.getURI(templatePlanFile);
			Date date = new Date(System.currentTimeMillis());
			final EPlan templatePlan = TemporalUtils.createTemporalPlan(templatePlanFile.getName(), uri, date, date);
			TransactionUtils.writing(templatePlan, new Runnable() {				
				@Override
				public void run() {
					templatePlan.setTemplate(isTemplatePlan);					
				}
			});			
			savePlan(templatePlan, templatePlanFile);
		}
		return plan;
	}
	
	/**
	 * Method to write a specific empty template plan. This call should be made from within a workspace modify operation.
	 * @param templatePlanFile
	 * @param isTemplatePlan
	 * @return reference to a plan that has already been written(saved)
	 */
	public static EPlan createNewEmptyTemplatePlan(IContainer root, String fileName) throws IOException {
		EPlan plan = null;
		IFile templatePlanFile = root.getFile(new Path(fileName));
		if (!templatePlanFile.exists()) {
			URI uri = EMFUtils.getURI(templatePlanFile);
			Date date = new Date(System.currentTimeMillis());
			final EPlan templatePlan = TemporalUtils.createTemporalPlan(templatePlanFile.getName(), uri, date, date);
			TransactionUtils.writing(templatePlan, new Runnable() {				
				@Override
				public void run() {
					templatePlan.setTemplate(true);					
				}
			});			
			savePlan(templatePlan, templatePlanFile);
		}
		return plan;
	}
	
	public static void writeTemplateFile(EPlan template, IContainer root) throws IOException {
		savePlan(template, root.getFile(new Path(TEMPLATE_PLAN_FILE)));
	}
	
	public static void savePlan(EPlan plan, IFile file) throws IOException {
		Resource planResource = plan.eResource();
		if (planResource == null) {
			URI uri = EMFUtils.getURI(file);
			ResourceSet resourceSet = EMFUtils.createResourceSet();
			Resource resource = resourceSet.createResource(uri);
			resource.getContents().add(plan);
			resource.save(null);
		} else {
			try {
				planResource.save(null);
			} catch (Exception e) {
				throw new IllegalStateException("saving plan to " + planResource.getURI(), e);
			}
			ResourceSet resourceSet = planResource.getResourceSet();
			for (Resource resource : resourceSet.getResources()) {
				if (planResource != resource && !(resource instanceof IgnorableResource)) {
					try {
						resource.save(null);
						break;
					} catch (Exception e) {
						LogUtil.error("saving resource "+resource.getURI()+" not saved: " + e.getMessage());
					}
				}
			}
		}
	}

}
