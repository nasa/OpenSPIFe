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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationIcons;
import gov.nasa.ensemble.core.plan.advisor.ViolationKey;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;

import java.util.LinkedHashSet;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class ViolationColumnLabelProvider extends ColumnLabelProvider {

	private static final FontData systemFontData = Display.getDefault().getSystemFont().getFontData()[0];
	private static final Font waivedFont = new Font(null, systemFontData.getName(), systemFontData.getHeight(), SWT.ITALIC);

	private final ViolationKey key;

	public ViolationColumnLabelProvider(ViolationKey key) {
		this.key = key;
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof ViolationTracker) {
			Violation violation = ((ViolationTracker)element).getViolation();
			return violation.getPrintString(key);
		} else if (element instanceof PlanAdvisorGroup) {
			PlanAdvisorGroup group = (PlanAdvisorGroup) element;
			switch (key) {
			case NAME:
			 {
				String value = group.getValue();
				int unfixed = 0, fixed = 0;
				for (ViolationTracker tracker : group.getViolationTrackers()) {
					if (tracker==null) continue;
					Violation violation = tracker.getViolation();
					if (violation==null) continue;
					if (violation.isCurrentlyViolated()) {
						unfixed++;
					} else {
						fixed++;
					}
				}
				return value + " (" + unfixed + "/" + (fixed + unfixed) + ")";
			}
			default: return group.getPrintString(key);
			}
		}
		return super.getText(element);
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof ViolationTracker) {
			Violation violation = ((ViolationTracker)element).getViolation();
			if (key == ViolationKey.NAME) {
				if (violation.isOutOfDate()) {
					return ViolationIcons.violation_waived_from_activity.getImage();
				}
				if (!violation.isCurrentlyViolated()) {
					return ViolationIcons.violation_fixed.getImage();
				}
				if (violation.isWaivedByInstance()) {
					return ViolationIcons.violation_waived_from_activity.getImage();
				}
				if (violation.isWaivedByRule()) {
					return ViolationIcons.violation_waived_from_plan_rule.getImage();
				}
				switch (violation.getSeverity()) {
				case ERROR:
					return ViolationIcons.violation_unfixed.getImage();
				case WARNING:
					return ViolationIcons.warning.getImage();
				case INFO:
					return null;
				}
			}
		}
		return super.getImage(element);
	}
	
	@Override
	public Font getFont(Object element) {
		if (element instanceof ViolationTracker) {
			Violation violation = ((ViolationTracker)element).getViolation();
			if (violation.isWaivedByInstance() || violation.isWaivedByRule() || violation.isOutOfDate()) {
				return waivedFont;
			}
		} else if (element instanceof PlanAdvisorGroup) {
			PlanAdvisorGroup group = (PlanAdvisorGroup) element;
			if (key == ViolationKey.NAME) {
				boolean allWaived = true;
				LinkedHashSet<ViolationTracker> violationTrackers = group.getViolationTrackers();
				for (ViolationTracker violationTracker : violationTrackers) {
					Violation violation = violationTracker.getViolation();
					if (!violation.isWaivedByRule() && !violation.isWaivedByInstance()) {
						allWaived = false;
						break;
					}
				}
				if (!violationTrackers.isEmpty() && allWaived) {
					return waivedFont;
				}
			}
		}
		return super.getFont(element);
	}
	
}
