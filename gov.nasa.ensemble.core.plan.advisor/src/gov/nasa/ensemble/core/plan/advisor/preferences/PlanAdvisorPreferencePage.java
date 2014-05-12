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
package gov.nasa.ensemble.core.plan.advisor.preferences;

import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;
import gov.nasa.ensemble.core.plan.advisor.Activator;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class PlanAdvisorPreferencePage extends AbstractPreferencePage {

	public static final String P_FIX_VIOLATIONS_WIZARD = "__PLAN_ADVISOR_FIX_VIOLATIONS_WIZARD";
	public static final String P_AUTOMATICALLY_START_FIXING = "__PLAN_ADVISOR_AUTOMATICALLY_START_FIXING";
	public static final String P_ASPEN_FEEDBACK = "__PLAN_ADVISOR_ASPEN_FEEDBACK";

	private BooleanFieldEditor fixViolationWizard;
	private BooleanFieldEditor autoStartFixing;
	private Button fixViolationsCheckbox;
	private BooleanFieldEditor aspenFeedback;
	
	public PlanAdvisorPreferencePage() {
		super(Activator.getDefault().getPreferenceStore());
		setDescription("Advisor Preference Page");
	}

	@Override
	protected void createFieldEditors() {
		
		fixViolationWizard = new BooleanFieldEditor(
			P_FIX_VIOLATIONS_WIZARD,
			"Fix violations with a &wizard",
			getFieldEditorParent()) {
			@Override
			protected Button getChangeControl(Composite parent) {
				fixViolationsCheckbox = super.getChangeControl(parent);
				return fixViolationsCheckbox;
			}
			@Override
			protected void doLoad() {
				super.doLoad();
				updateAutoStartFixing(fixViolationsCheckbox.getSelection());
			}
			@Override
			protected void doLoadDefault() {
				super.doLoadDefault();
				updateAutoStartFixing(fixViolationsCheckbox.getSelection());
			}
		};
		addField(fixViolationWizard);

		autoStartFixing = new BooleanFieldEditor(
			P_AUTOMATICALLY_START_FIXING,
			"&Automatically start fixing when wizard comes up",
			getFieldEditorParent());
		addField(autoStartFixing);
		
		aspenFeedback = new BooleanFieldEditor(
				P_ASPEN_FEEDBACK,
				"Activity Drag &Feedback",
				getFieldEditorParent());
		addField(aspenFeedback);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if ((event.getProperty() == FieldEditor.VALUE)
			&& (event.getSource() == fixViolationWizard)
			&& (event.getNewValue() instanceof Boolean)) {
			Boolean bool = (Boolean) event.getNewValue();
			updateAutoStartFixing(bool.booleanValue());
		}
		super.propertyChange(event);
	}
	
	private void updateAutoStartFixing(boolean booleanValue) {
		autoStartFixing.setEnabled(booleanValue, getFieldEditorParent());
	}

}
