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

import gov.nasa.arc.spife.core.plan.rules.view.RuleIcons;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.advisor.RuleUtils;
import gov.nasa.ensemble.dictionary.ERule;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

public class PlanRuleWaivedCellLabelProvider extends CellLabelProvider implements IPlanRulesSortableColumn {

	private EPlanElement element;

	public void setPlanElement(EPlanElement element) {
		this.element = element;
	}
	
	@Override
	public void update(ViewerCell cell) {
		String text = "";
		Image image = null;
		TreeItem item = (TreeItem)cell.getItem();
		if (item.getGrayed()) {
			text = "mixed";
			image = null;
		} else if (item.getChecked()) {
			text = "active";
			image = RuleIcons.RULE_ENFORCED.getImage();
		} else {
			text = "waived";
			image = RuleIcons.RULE_WAIVED.getImage();
		}
		cell.setText(text);
		cell.setImage(image);
	}

	@Override
	public int compare(ERule o1, ERule o2) {
		String value1 = String.valueOf(isEnabled(o1));
		String value2 = String.valueOf(isEnabled(o2));
		return String.CASE_INSENSITIVE_ORDER.compare(value1, value2);
	}
	
	private boolean isEnabled(ERule rule) {
		return RuleUtils.isEnabled(element, rule);
	}

}
