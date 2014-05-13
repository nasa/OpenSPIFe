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
package gov.nasa.arc.spife.core.plan.rules.view;

import gov.nasa.arc.spife.core.plan.rules.view.columns.PlanRuleDescriptionCellLabelProvider;
import gov.nasa.arc.spife.core.plan.rules.view.columns.PlanRuleGroupCellLabelProvider;
import gov.nasa.arc.spife.core.plan.rules.view.columns.PlanRulePrintNameCellLabelProvider;
import gov.nasa.arc.spife.core.plan.rules.view.columns.PlanRuleWaivedCellLabelProvider;

import org.eclipse.jface.viewers.CellLabelProvider;

public enum PlanRulesColumnSpecification {

	TREE("", 200, PlanRuleGroupCellLabelProvider.class),
	NAME("Name", 120, PlanRulePrintNameCellLabelProvider.class),
	WAIVED("Enablement", 70, PlanRuleWaivedCellLabelProvider.class),
	DESCRIPTION("Description", 400, PlanRuleDescriptionCellLabelProvider.class),
	;
	
	private final String headerText;
	private final int defaultWidth;
	private final Class<? extends CellLabelProvider> labelProviderClass;

	private PlanRulesColumnSpecification(String headerText, int defaultWidth, Class<? extends CellLabelProvider> labelProviderClass) {
		this.headerText = headerText;
		this.defaultWidth = defaultWidth;
		this.labelProviderClass = labelProviderClass;
	}

	public String getHeaderText() {
		return headerText;
	}

	public int getDefaultWidth() {
		return defaultWidth;
	}

	public Class<? extends CellLabelProvider> getLabelProviderClass() {
		return labelProviderClass;
	}

}
