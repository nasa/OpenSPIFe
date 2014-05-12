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

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.core.plan.editor.view.template.actions.TemplatePlanViewDuplicateAction;
import gov.nasa.ensemble.core.plan.editor.view.template.operations.TemplatePlanDuplicateOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;

public class SPIFeTemplatePlanViewDuplicateAction extends TemplatePlanViewDuplicateAction {

	/**
	 * Duplicate the StructuredSelection with a new name and save if successful. For each duplicated selection,
	 * open an editor.
	 * @param structuredSelection the selected plan elements
	 */
	@Override
	public void addDuplicate(final IStructuredSelection structuredSelection) {				
		if(templatePlanView != null && templatePlanView.getCurrentTemplatePlan() != null) {
			Collection<EPlanElement> elements = PlanEditorUtil.emfFromSelection(structuredSelection);
			Map<EPlan, List<EPlanElement>> planDuplicateMap = new HashMap<EPlan, List<EPlanElement>>();
			for(EPlanElement element : elements) {					
				EPlan plan = EPlanUtils.getPlan(element);
				if (plan == null || element.eResource() == null) {
					continue;
				}
				List<EPlanElement> duplicateList = planDuplicateMap.get(plan);
				if (duplicateList == null) {
					duplicateList = new ArrayList<EPlanElement>();
					planDuplicateMap.put(plan, duplicateList);
				}
				duplicateList.add(element);
			}
			for (EPlan templatePlan : planDuplicateMap.keySet()) {
				performDuplicateOperation(templatePlan, planDuplicateMap.get(templatePlan));
			}	
		}
	}

	@Override
	protected void maybeOpenInEditor(TemplatePlanDuplicateOperation operation) {
		// don't open in editor but note as updated on the template plan page
		SPIFeTemplatePlanPage templatePlanPage = (SPIFeTemplatePlanPage)templatePlanView.getCurrentPage();
		templatePlanPage.updatedTemplatePlanResource(operation.getTemplatePlan());
	}
	
}
