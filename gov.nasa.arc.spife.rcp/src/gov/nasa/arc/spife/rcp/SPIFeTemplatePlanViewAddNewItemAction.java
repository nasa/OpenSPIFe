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
package gov.nasa.arc.spife.rcp;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanUtils;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanView;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanViewAddAction;
import gov.nasa.ensemble.core.plan.editor.view.template.operations.TemplatePlanAddNewOperation;

import java.util.List;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.part.IPage;

/**
 * Action for adding a new Activity to a template plan.
 */
public class SPIFeTemplatePlanViewAddNewItemAction extends TemplatePlanViewAddAction {
	
	ISelection currentViewerSelection = null;
	// Save last selected plan name instead of the actual plan in case the template views plans get reloaded
	String lastSelectedPlanName = null;
	
	public SPIFeTemplatePlanViewAddNewItemAction(final TemplatePlanView templatePlanView) {
		super("", IAction.AS_PUSH_BUTTON, templatePlanView);
		setEnabled(false);
	}
	
	/**
	 * Get the selection from the TemplatePlanView and add a new Activity to it. Save on success and open the
	 * element in an editor.
	 * Note that local variable templatePlanView shadows the protected superclass field of the same name.
	 */
	@Override
	public void run() {
		IStructuredSelection selection = getCurrentEditorSelection();
		if (selection == null || selection.isEmpty()) {
			return;
		}
		SPIFeTemplatePlanPage templatePlanPage = getCurrentPage();
		List<EPlan> templatePlans = templatePlanPage.getTemplatePlans();
		TreeViewer viewer = templatePlanPage.getTreeViewer();
		AdapterFactoryLabelProvider labelProvider = (AdapterFactoryLabelProvider)viewer.getLabelProvider();
		EPlan selectedPlan = getSelectedTemplatePlan(templatePlans);
		AddTemplateDialog dialog = new AddTemplateDialog(WidgetUtils.getShell(), templatePlans, labelProvider, selectedPlan);
		if (dialog.open() == Window.OK) {
			String templateName = dialog.getTemplateName();
			selectedPlan = dialog.getSelectedPlan();
			lastSelectedPlanName = selectedPlan.getName();
			TemplatePlanAddNewOperation operation = new SPIFeTemplatePlanAddNewOperation(selectedPlan, selection, templateName);
			try {
				operation.execute();
				if (operation.isSaveNeeded()) {
					TemplatePlanUtils.saveTemplatePlan(selectedPlan);
					templatePlanPage.updatedTemplatePlanResource(selectedPlan);
				}
			} catch (Throwable t) {
				LogUtil.error("error adding new template", t);
			}
		}
	}

	private EPlan getSelectedTemplatePlan(List<EPlan> templatePlans) {
		if (currentViewerSelection != null && currentViewerSelection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) currentViewerSelection;
			if (!structuredSelection.isEmpty()) {
				Object firstSelection = structuredSelection.getFirstElement();
				if (firstSelection instanceof EPlan) {
					return (EPlan)firstSelection;
				}
			}
		}
		for (EPlan plan : templatePlans) {
			if (plan.getName().equals(lastSelectedPlanName)) {
				return plan;
			}
		}
		return null;
	}

	private SPIFeTemplatePlanPage getCurrentPage() {
		IPage currentPage = templatePlanView.getCurrentPage();
		if (currentPage instanceof SPIFeTemplatePlanPage) {
			return (SPIFeTemplatePlanPage)currentPage;
		}
		return null;
	}
	
	private IStructuredSelection getCurrentEditorSelection() {
		SPIFeTemplatePlanPage templatePlanPage = getCurrentPage();
		if (templatePlanPage != null) {
			return templatePlanPage.getCurrentEditorSelection();
		}
		return null;
	}
	
	@Override
	public void updateEnablement() {
		boolean shouldBeEnabled = false;
		IStructuredSelection editorSelection = getCurrentEditorSelection();
		if (editorSelection != null && !editorSelection.isEmpty()) {
			Object firstSelection = editorSelection.getFirstElement();
			if (firstSelection instanceof EPlanChild) {
				shouldBeEnabled = true;
			}
		}
		setEnabled(shouldBeEnabled);
	}
	
	@Override
	public boolean shouldBeEnabled(ISelection selection) {
		return false;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		currentViewerSelection = event.getSelection();
	}
	
}
