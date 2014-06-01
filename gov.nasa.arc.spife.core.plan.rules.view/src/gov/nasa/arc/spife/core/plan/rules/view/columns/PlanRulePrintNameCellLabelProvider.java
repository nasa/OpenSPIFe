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
package gov.nasa.arc.spife.core.plan.rules.view.columns;

import gov.nasa.ensemble.dictionary.ERule;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public class PlanRulePrintNameCellLabelProvider extends CellLabelProvider implements IPlanRulesSortableColumn {

	@Override
	public void update(ViewerCell cell) {
		String text = "";
		Object element = cell.getElement();
		if (element instanceof ERule) {
			ERule rule = (ERule) element;
			text = rule.getPrintName();
		}
		cell.setText(text);
	}

	@Override
	public int compare(ERule o1, ERule o2) {
		String value1 = o1.getPrintName();
		String value2 = o2.getPrintName();
		return String.CASE_INSENSITIVE_ORDER.compare(value1, value2);
	}
	
}
