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
package gov.nasa.arc.spife.core.plan.advisor.resources.preferences;

import gov.nasa.arc.spife.core.plan.advisor.resources.Activator;
import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class ResourcesPreferencePage extends AbstractPreferencePage {

	private static final String P_DOMAIN = "gov.nasa.arc.spife.core.plan.advisor.resources";
	/* package */ static final String P_FIND_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS = P_DOMAIN + ".findresourceprofileconstraintviolations"; // Whether or not to find resource profile constraint violations
	/* package */ static final String P_FIND_CLAIMABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS = P_FIND_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS + ".claimable"; // Whether or not to find claimable resource profile constraint violations
	/* package */ static final String P_FIND_SHARABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS = P_FIND_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS + ".sharable"; // Whether or not to find sharable resource profile constraint violations

	/* package */ static final String P_FIND_NUMERIC_ACTIVITY_REQUIREMENT_VIOLATIONS = P_DOMAIN + ".findnumericactivityrequirementviolations"; // Whether or not to find activity requirement violations
	/* package */ static final String P_FIND_STATE_ACTIVITY_REQUIREMENT_VIOLATIONS = P_DOMAIN + ".findstateactivityrequirementviolations"; // Whether or not to find activity requirement violations
	
	private Group profileConstraintsGroup;
	private BooleanFieldEditor findResourceProfileConstraintViolationsEditor;
	private BooleanFieldEditor findClaimableResourceProfileConstraintViolationsEditor;
	private BooleanFieldEditor findSharableResourceProfileConstraintViolationsEditor;
	
	private Group activityRequirementsGroup;
	@SuppressWarnings("unused")
	
	public ResourcesPreferencePage() {
		super(Activator.getDefault().getPreferenceStore());
		setDescription("Resources Preference Page");
	}

	@Override
	protected void createFieldEditors() {
		Composite parent = getFieldEditorParent();
		
		profileConstraintsGroup = createGroup(parent);
		profileConstraintsGroup.setText("Resource Profile Constraints");
		addField(findResourceProfileConstraintViolationsEditor = new BooleanFieldEditor(
			P_FIND_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS,
			"Report resource profile constraint violations (re-open plans to refresh all violations)",
			profileConstraintsGroup));
		addField(findClaimableResourceProfileConstraintViolationsEditor = new BooleanFieldEditor(
			P_FIND_CLAIMABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS,
			"Report claimable resource profile constraint violations (re-open plans to refresh all violations)",
			profileConstraintsGroup));
		indent(findClaimableResourceProfileConstraintViolationsEditor, profileConstraintsGroup);
		addField(findSharableResourceProfileConstraintViolationsEditor = new BooleanFieldEditor(
				P_FIND_SHARABLE_RESOURCE_PROFILE_CONSTRAINT_VIOLATIONS,
				"Report sharable resource profile constraint violations (re-open plans to refresh all violations)",
				profileConstraintsGroup));
		indent(findSharableResourceProfileConstraintViolationsEditor, profileConstraintsGroup);
		
		activityRequirementsGroup = createGroup(parent);
		activityRequirementsGroup.setText("Activity Requirements");
		addField(new BooleanFieldEditor(
				P_FIND_NUMERIC_ACTIVITY_REQUIREMENT_VIOLATIONS,
				"Report numeric activity requirement violations (re-open plans to refresh all violations)",
				activityRequirementsGroup));
		addField(new BooleanFieldEditor(
				P_FIND_STATE_ACTIVITY_REQUIREMENT_VIOLATIONS,
				"Report state activity requirement violations (re-open plans to refresh all violations)",
				activityRequirementsGroup));
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		setGroupEnablement();
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		setGroupEnablement();
	}

	private void setGroupEnablement() {
		Boolean resourceProfileConstraints = findResourceProfileConstraintViolationsEditor.getBooleanValue();
		findClaimableResourceProfileConstraintViolationsEditor.setEnabled(resourceProfileConstraints, profileConstraintsGroup);
		findSharableResourceProfileConstraintViolationsEditor.setEnabled(resourceProfileConstraints, profileConstraintsGroup); 
	}

}
