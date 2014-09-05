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
package gov.nasa.ensemble.core.plan.advisor.view.fixing;

import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.core.plan.advisor.markers.MarkerViolation;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

public class FixingViolationsWizardSelectionPage extends WizardPage {

	private PlanAdvisorMember advisorMember;
	private Set<EPlanElement> selectedElements;

	public FixingViolationsWizardSelectionPage(ISelection selection, PlanAdvisorMember advisorMember) {
		super("SelectionPage");
		selectedElements = PlanEditorUtil.emfFromSelection(selection);
		if (selectedElements.isEmpty()) {
			setMessage("Fixing violations on the entire plan.");
		} else if (selectedElements.size() == 1) {
			EPlanElement element = selectedElements.iterator().next();
			setMessage("Fixing violations on '" + element.getName() + "'.");
		} else {
			setMessage("Fixing violations on " + selectedElements.size() + " items.");
		}
		this.advisorMember = advisorMember;
	}

	@Override
	public void dispose() {
		super.dispose();
		selectedElements = null;
		advisorMember = null;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new EnsembleComposite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		composite.setLayout(layout);
		Map<String, Integer> typeCounts = new TreeMap<String, Integer>();
		for (ViolationTracker tracker : advisorMember.getViolationTrackers()) {
			Violation violation = tracker.getViolation();
			// the wizard doesn't handle marker violations
			if (!violation.isCurrentlyViolated() || violation instanceof MarkerViolation) {
				continue;
			}
			List<? extends EPlanElement> violationElements = violation.getElements();
			if (!violationElements.isEmpty()) {
				if (Collections.disjoint(violationElements, selectedElements)) {
					continue;
				}
			}
			String type = violation.getType();
			Integer count = typeCounts.get(type);
			if (count == null) {
				count = 1;
			} else {
				count++;
			}
			typeCounts.put(type, count);
		}
		for (Map.Entry<String, Integer> typeCount : typeCounts.entrySet()) {
			String type = typeCount.getKey();
			Integer count = typeCount.getValue();
			Label label = new Label(composite, SWT.WRAP);
			label.setText("Considering " + count(count, type) + ".");
		}
		setControl(composite);
	}

	private static String count(int count, String type) {
		return count + " " + type + " Violation" + (count > 1 ? "s" : "");
	}

	@Override
	public void performHelp() {
		PlatformUI.getWorkbench().getHelpSystem().displayHelp("gov.nasa.arc.spife.core.plan.advisor.fixing.FixingViolationsWizardSelectionsPage");
		super.performHelp();
	}
}
