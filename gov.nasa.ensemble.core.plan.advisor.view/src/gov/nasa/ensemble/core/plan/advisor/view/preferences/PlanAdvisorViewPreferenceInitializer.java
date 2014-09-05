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

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.ui.preferences.PropertyPreferenceInitializer;
import gov.nasa.ensemble.core.plan.advisor.view.Activator;
import gov.nasa.ensemble.core.plan.advisor.view.PlanAdvisorColumnSpecification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlanAdvisorViewPreferenceInitializer extends PropertyPreferenceInitializer {

	private static final String GROUP_BY_COLUMNS_PROPERTY = "advisor.groupByColumns";
	private static final List<String> DEFAULT_GROUP_BY_COLUMNS = Arrays.asList(new String[] { PlanAdvisorColumnSpecification.TYPE.getHeaderText(), });
	private static final String HIDDEN_COLUMNS_PROPERTY = "advisor.hideColumns";
	private static final List<String> HIDDEN_COLUMNS = EnsembleProperties.getStringListPropertyValue(HIDDEN_COLUMNS_PROPERTY, Collections.<String> emptyList());

	public PlanAdvisorViewPreferenceInitializer() {
		super(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void initializeDefaultPreferences() {
		setStringList(PlanAdvisorViewPreferencePage.P_VISIBLE_COLUMNS, getAllColumnHeaderTexts());
		List<String> groupByColumns = EnsembleProperties.getStringListPropertyValue(GROUP_BY_COLUMNS_PROPERTY);
		if (groupByColumns == null) {
			groupByColumns = DEFAULT_GROUP_BY_COLUMNS;
		}
		setStringList(PlanAdvisorViewPreferencePage.P_GROUP_BY_COLUMNS, groupByColumns);
		setString(PlanAdvisorViewPreferencePage.P_SORT_COLUMN, PlanAdvisorColumnSpecification.TIME.getHeaderText());
	}

	private List<String> getAllColumnHeaderTexts() {
		PlanAdvisorColumnSpecification[] values = PlanAdvisorColumnSpecification.values();
		List<String> texts = new ArrayList<String>(values.length);
		for (PlanAdvisorColumnSpecification spec : values) {
			String headerText = spec.getHeaderText();
			if (!HIDDEN_COLUMNS.contains(headerText)) {
				texts.add(headerText);
			}
		}
		return texts;
	}

}
