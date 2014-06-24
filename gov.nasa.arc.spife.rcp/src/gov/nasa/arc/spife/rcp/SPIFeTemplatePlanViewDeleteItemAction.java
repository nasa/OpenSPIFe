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
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanUtils;
import gov.nasa.ensemble.core.plan.editor.view.template.actions.TemplatePlanViewDeleteItemAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredSelection;

public class SPIFeTemplatePlanViewDeleteItemAction extends TemplatePlanViewDeleteItemAction {

	@Override
	public void delete(IStructuredSelection structuredSelection) {
		Map<EPlan, List<EPlanElement>> planDeletionsMap = new HashMap<EPlan, List<EPlanElement>>();
		SPIFeTemplatePlanPage templatePlanPage = (SPIFeTemplatePlanPage)templatePlanView.getCurrentPage();
		List list = structuredSelection.toList();
		for(Object object : list) {					
			if(object instanceof EPlanElement) {
				EPlanElement element = (EPlanElement)object;
				EPlan plan = EPlanUtils.getPlan(element);
				if (plan == null || element.eResource() == null) {
					continue;
				}
				List<EPlanElement> deletions = planDeletionsMap.get(plan);
				if (deletions == null) {
					deletions = new ArrayList<EPlanElement>();
					planDeletionsMap.put(plan, deletions);
				}
				deletions.add(element);
			}
		}
		for (EPlan plan : planDeletionsMap.keySet()) {
			final EPlan templatePlan = plan;
			final List<EPlanElement> deletions = planDeletionsMap.get(plan);
			PlanTransferable transferable = new PlanTransferable();
			transferable.setPlanElements(deletions);
			PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
			modifier.remove(transferable, modifier.getLocation(transferable));
			TemplatePlanUtils.saveTemplatePlan(templatePlan);
			templatePlanPage.updatedTemplatePlanResource(templatePlan);
		}
		
	}
	
	

}
