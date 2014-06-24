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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanPage;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanURIRegistry;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanUtils;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanView;
import gov.nasa.ensemble.core.plan.editor.view.template.operations.TemplatePlanAddNewOperation;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Action for adding a new Activity to a template plan.
 */
public class TemplatePlanViewAddNewItemAction extends TemplatePlanViewAction {
	
	/** The immutable name of the new Activity, set in the constructor. May be null. */
	private String activityName = null;

	public TemplatePlanViewAddNewItemAction(String activityName) {
		this.activityName = activityName;
	}
	
	/** Return the name of the Activity. */
	public String getActivityName() {
		return activityName;
	}	
	
	/** {@inheritDoc} */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(EditorPlugin.ID, "icons/add.png");
	}

	/** Return the name of the Activity. */
	@Override
	public String getText() {
		return getActivityName();
	}

	/**
	 * {@inheritDoc}
	 * @param action ignored
	 */
	@Override
	public void run(IAction action) {
		run();
	}

	/**
	 * Get the selection from the TemplatePlanView and add a new Activity to it. Save on success and open the
	 * element in an editor.
	 * Note that local variable templatePlanView shadows the protected superclass field of the same name.
	 */
	@Override
	public void run() {
		ISelectionProvider selectionProvider = templatePlanView.getSite().getSelectionProvider();
		ISelection selection = selectionProvider.getSelection();

		TemplatePlanView templatePlanView = (TemplatePlanView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(TemplatePlanView.ID);
		IPage currentPage = templatePlanView.getCurrentPage();
		if(currentPage instanceof TemplatePlanPage) {
			TemplatePlanPage templatePlanPage = (TemplatePlanPage)currentPage;
			TreeViewer treeViewer = templatePlanPage.getTreeViewer();
			final EPlan templatePlan = (EPlan)treeViewer.getInput();
			if(selection instanceof IStructuredSelection) {
				IStructuredSelection structuredSelection = (IStructuredSelection)selection;
				TemplatePlanAddNewOperation operation = createAddNewOperation(templatePlan, structuredSelection);
				try {
					operation.execute();
					if(operation.isSaveNeeded()) {
						TemplatePlanUtils.saveTemplatePlan(templatePlan);
						EPlanElement addedElement = operation.getAddedElement();
						TemplatePlanUtils.openElementInTemplatePlanElementEditor(addedElement, TemplatePlanURIRegistry.INSTANCE);
					}
				} catch (Throwable t) {
					LogUtil.error("error adding element", t);
				}
			}
		}			
	}
	
	private TemplatePlanAddNewOperation createAddNewOperation(EPlan templatePlan, IStructuredSelection selection) {
		TemplatePlanAddNewOperation addNewOperation = null;
		try {
			addNewOperation = MissionExtender.construct(TemplatePlanAddNewOperation.class, templatePlan, selection, activityName);
		} catch (ConstructionException e) {
			LogUtil.error("constructing mission extendable TemplatePlanAddNewOperation", e);
			addNewOperation = new TemplatePlanAddNewOperation(templatePlan, selection, activityName);
		}
		return addNewOperation;
	}
	
	/**
	 * Since change of selection is unimportant, simply trace the change to the console.
	 * @param action ignored
	 * @param selection ignored
	 */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		System.out.println("selection changed");
	}
}
