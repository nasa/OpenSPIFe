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
package gov.nasa.ensemble.core.plan.editor.view.template.operations;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * An operation for adding a newly-created activity as a child of a template plan.
 */
public class TemplatePlanAddNewOperation extends TemplatePlanOperation implements MissionExtendable {
	
	/** Passed into the constructor. May be null. */
	protected IStructuredSelection structuredSelection;
	
	/** Passed into the constructor. May be null. */
	protected String activityName;
	
	/** Set by execute() to be the plan element added as a child of the template plan. */
	protected EPlanChild addedElement;
	
	/**
	 * Decorate by storing the structured selection and the activity name.
	 * @param templatePlan the template plan being operated on
	 * @param structuredSelection the selection
	 * @param activityName
	 */
	public TemplatePlanAddNewOperation(EPlan templatePlan, IStructuredSelection structuredSelection, String activityName) {
		super("add new item to template plan", templatePlan);
		this.structuredSelection = structuredSelection;
		this.activityName = activityName;
	}

	/**
	 * Execute the add-new operation. If the template plan is not null, create a new activity with the name
	 * passed into the constructor and make it one of the template plan's children. If successful, the new
	 * activity is assigned to the addedElement property and the template plan is flagged as dirty.
	 */
	@Override
	public void execute() throws Throwable {
		final EPlan templatePlan = getTemplatePlan();
		if(templatePlan != null) {
			final EActivity activity = TemplatePlanUtils.createNewActivity(activityName);
	   		List<? extends EObject> folders = getAllFolderModels(structuredSelection);
	   		makeTemplateReferenceAllTheFolders(activity, folders);
			TransactionUtils.writing(templatePlan, new Runnable() {
				@Override
				public void run() {
					templatePlan.getChildren().add(activity);
					addedElement = activity;
					needsSave = true;
				}
			});
		}
	}
	
	/**
	 * Overridable method to return a list of EObjects for the models of folders in the template view
	 *  
	 * @param structuredSelection the selection when the add new operation is invoked
	 * @return a list of folder model EObjects
	 */
	protected List<? extends EObject> getAllFolderModels(IStructuredSelection structuredSelection) {
		return TemplatePlanUtils.getAllFolderModels(structuredSelection);
	}
	
	/**
	 * Overridable method for associating the newly created EPlanChild with EObjects representing folders
	 * in the template view
	 * 
	 * @param template the newly created template plan EPlanChild
	 * @param folders a list of folder model EObjects
	 */
	protected void makeTemplateReferenceAllTheFolders(EPlanChild template, List<? extends EObject> folders) {
		if (template instanceof EActivity) {
			TemplatePlanUtils.makeActivityReferenceAllTheFolders((EActivity)template, folders);
		}
	}

	/** Return a null string reference. */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Return the new plan element, if any, that execute() added to the template plan.
	 */
	public EPlanChild getAddedElement() {
		return addedElement;
	}
	
}
