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
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanURIRegistry;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanUtils;
import gov.nasa.ensemble.core.plan.editor.view.template.operations.TemplatePlanDuplicateOperation;

import java.util.Collection;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Action for duplicating a template plan.
 */
public class TemplatePlanViewDuplicateAction extends TemplatePlanViewAction {
	
	/** {@inheritDoc} */
	@Override
	public void run(IAction action) {
		addDuplicate(structuredSelection);
	}
	
	/**
	 * Duplicate the StructuredSelection with a new name and save if successful. For each duplicated selection,
	 * open an editor.
	 * @param structuredSelection the selected plan elements
	 */
	public void addDuplicate(final IStructuredSelection structuredSelection) {				
		if(templatePlanView != null && templatePlanView.getCurrentTemplatePlan() != null) {
			final EPlan templatePlan = templatePlanView.getCurrentTemplatePlan();
			Collection<EPlanElement> elements = PlanEditorUtil.emfFromSelection(structuredSelection);
			performDuplicateOperation(templatePlan, elements);
		}		
	}

	protected void performDuplicateOperation(final EPlan templatePlan, Collection<EPlanElement> elements) {
		TemplatePlanDuplicateOperation operation = new TemplatePlanDuplicateOperation("template plan duplicate operation", templatePlan, elements);
		try {
			operation.execute();
			if(operation.isSaveNeeded()) {
				TemplatePlanUtils.saveTemplatePlan(templatePlan);
				maybeOpenInEditor(operation);
			}
		} catch (Throwable t) {
			LogUtil.error("error adding element", t);
		}
	}

	protected void maybeOpenInEditor(TemplatePlanDuplicateOperation operation) {
		Collection<EPlanElement> duplicates = operation.getDuplicates();
		for (EPlanElement duplicate : duplicates) {
			TemplatePlanUtils.openElementInTemplatePlanElementEditor(duplicate, TemplatePlanURIRegistry.INSTANCE);
		}
	}
		
}
