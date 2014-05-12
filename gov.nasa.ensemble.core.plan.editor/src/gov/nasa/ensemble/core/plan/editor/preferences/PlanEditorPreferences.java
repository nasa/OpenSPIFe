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
package gov.nasa.ensemble.core.plan.editor.preferences;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.plan.editor.EditorPlugin;
import gov.nasa.ensemble.core.plan.editor.PlanRole;
import gov.nasa.ensemble.core.plan.editor.PlanRoleRegistry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.preference.IPreferenceStore;

public class PlanEditorPreferences {

	public static final IPreferenceStore PREFERENCE_STORE = EditorPlugin.getDefault().getPreferenceStore();
	/* package */ static final boolean DEFAULT_CHECK_CUSTODIAN = false;
	/* package */ static final boolean DEFAULT_CROSS_EDITOR_SELECTIONS = false;
	/* package */ static final boolean DEFAULT_AUTOMATIC_RESOURCE_MODELING = true;
	public static final int DEFAULT_WATERFALL_LARGE_ELEMENT_DURATION_IN_HOURS = 8;

	/**
	 * Returns whether we should check the custodian when saving
	 * @return
	 */
	public static boolean isCheckCustodian() {
		boolean checkCustodian = PREFERENCE_STORE.getBoolean(PlanEditorPreferencePage.P_CHECK_CUSTODIAN);
		// no good way to tell if this is a default-default, unfortunately.
		return checkCustodian;
	}
	
	/**
	 * Returns whether selections should be synchronized between editors
	 * @return
	 */
	public static boolean isCrossEditorSelection() {
		return PREFERENCE_STORE.getBoolean(PlanEditorPreferencePage.P_CROSS_EDITOR_SELECTIONS);
	}
	
	public static URI getTemplatePlanURI() {
		String valueString = PREFERENCE_STORE.getString(PlanEditorPreferencePage.P_TEMPLATE_PLAN_URI);
		if (valueString != null && valueString.trim().length() != 0) {
			try {
				return URI.createURI(valueString);
			} catch (Exception e) {
				LogUtil.error("parsing "+valueString+": "+e.getMessage());
			}
		}
		return null;
	}
	
	public static PlanRole getPlanRole() {
		return PlanRoleRegistry.getPlanRole(PREFERENCE_STORE.getString(PlanEditorPreferencePage.P_PLAN_ROLE));
	}
	
	public static void setPlanRole(String roleName) {
		PREFERENCE_STORE.setValue(PlanEditorPreferencePage.P_PLAN_ROLE, roleName);
	}
	
	public static boolean isAutomaticResourceModeling() {
		return PREFERENCE_STORE.getBoolean(PlanEditorPreferencePage.P_AUTOMATIC_RESOURCE_MODELING);
	}
	
	public static void setAutomaticResourceModeling(boolean value) {
		PREFERENCE_STORE.setValue(PlanEditorPreferencePage.P_AUTOMATIC_RESOURCE_MODELING, value);
	}
	
}
