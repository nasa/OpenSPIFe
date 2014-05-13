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
package gov.nasa.ensemble.core.plan.advisor.view.preferences;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.plan.advisor.view.Activator;
import gov.nasa.ensemble.core.plan.advisor.view.PlanAdvisorColumnSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

public class PlanAdvisorViewPreferences {

	public static List<PlanAdvisorColumnSpecification> getVisibleColumns() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String visibleColumnsString = store.getString(PlanAdvisorViewPreferencePage.P_VISIBLE_COLUMNS);
		return getColumnListFromPreference(visibleColumnsString);
	}

	public static List<PlanAdvisorColumnSpecification> getGroupByColumns() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String groupByString = store.getString(PlanAdvisorViewPreferencePage.P_GROUP_BY_COLUMNS);
		return getColumnListFromPreference(groupByString);
	}

	public static PlanAdvisorColumnSpecification getSortColumn() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String sortColumnString = store.getString(PlanAdvisorViewPreferencePage.P_SORT_COLUMN);
		return getColumnWithHeaderText(sortColumnString);
	}
	
	public static void setVisibleColumns(List<PlanAdvisorColumnSpecification> specs) {
		String visibleColumnsString = getPreferenceFromColumnList(specs);
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setValue(PlanAdvisorViewPreferencePage.P_VISIBLE_COLUMNS, visibleColumnsString);
	}
	
	/*
	 * Utility methods
	 */
	
	private static List<PlanAdvisorColumnSpecification> getColumnListFromPreference(String preferenceString) {
		StringTokenizer tokenizer = new StringTokenizer(preferenceString, ",");
		List<PlanAdvisorColumnSpecification> columns = new ArrayList<PlanAdvisorColumnSpecification>();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if ((token == null) || (token.length() == 0)) {
				String warning = "ignoring missing column in plan advisor preference";
				Logger.getLogger(PlanAdvisorViewPreferences.class).warn(warning);
				continue;
			}
			PlanAdvisorColumnSpecification column = getColumnWithHeaderText(token);
			if (column == null) {
				String warning = "no column named '" + token + "' in plan advisor preference";
				Logger.getLogger(PlanAdvisorViewPreferences.class).warn(warning);
				continue;
			}
			columns.add(column);
		}
		return columns;
	}

	private static String getPreferenceFromColumnList(List<PlanAdvisorColumnSpecification> specs) {
		StringBuilder preference = new StringBuilder();
		for (PlanAdvisorColumnSpecification spec : specs) {
			if (preference.length() != 0) {
				preference.append(",");
			}
			preference.append(spec.getHeaderText());
		}
		return preference.toString();
	}

	private static PlanAdvisorColumnSpecification getColumnWithHeaderText(String headerText) {
		for (PlanAdvisorColumnSpecification spec : PlanAdvisorColumnSpecification.values()) {
			if (CommonUtils.equals(headerText, spec.getHeaderText())) {
				return spec;
			}
		}
		return null;
	}

}
