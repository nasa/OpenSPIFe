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
package gov.nasa.ensemble.core.plan.editor.lifecycle;

import gov.nasa.ensemble.common.ui.wizard.EnsembleWizard;
import gov.nasa.ensemble.common.ui.wizard.EnsembleWizardErrorPage;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IWorkbench;

public abstract class ActivePlanWizard extends EnsembleWizard {
	
	private static final WizardPage DEFAULT_ERROR_PAGE = new EnsembleWizardErrorPage("Error", "No active plan selected in workspace");
	
	protected IStructuredSelection selection;
	private PlanEditorModel editorModel;
	private EPlan plan;	
	
	public ActivePlanWizard() {
		errorPage = ActivePlanWizard.DEFAULT_ERROR_PAGE;
	}
	
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		editorModel = PlanEditorModelRegistry.getCurrent(workbench);
		if (editorModel != null) {
			plan = editorModel.getEPlan();
		}
	}
	
	/**
	 * Gets the project associated with the plan.
	 * Caution (FIXME):  This makes the assumption that the plan name is the project name,
	 * which I'm not sure always holds, except maybe in Score.
	 */
	protected IProject getProject() {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(plan.getName());
	}

	@Override
	public abstract boolean performFinish();

	@Override
	public void dispose() {
		super.dispose();
		plan = null;
		selection = null;
		editorModel = null;
	}

	/**
	 * Adds the pages give the following plan which is guaranteed to not be null.
	 * It is called from the addPages() method if and only if the active plan is
	 * not null
	 */
	protected abstract void addPages(EPlan plan);

	public final EPlan getPlan() {
		return plan;
	}
	
	protected final PlanEditorModel getPlanEditorModel() {
		return editorModel;
	}

	@Override
	protected boolean checkWizardPreconditions() {
		return getPlan() != null;
	}
	
	@Override
	public final void addContentPages() {
		addPages(plan);
	}
	
}
