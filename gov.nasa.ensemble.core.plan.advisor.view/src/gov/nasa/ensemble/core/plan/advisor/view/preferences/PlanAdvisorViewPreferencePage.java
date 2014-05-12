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

import gov.nasa.ensemble.common.ui.PickListFieldEditor;
import gov.nasa.ensemble.common.ui.preferences.AbstractPreferencePage;
import gov.nasa.ensemble.core.plan.advisor.view.Activator;
import gov.nasa.ensemble.core.plan.advisor.view.PlanAdvisorColumnSpecification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.preference.ComboFieldEditor;

public class PlanAdvisorViewPreferencePage extends AbstractPreferencePage {

	public static final String ID = "PlanAdvisorViewPreferencePage"; // must match plugin.xml
	
	private static final String P_DOMAIN = "gov.nasa.ensemble.core.plan.advisor.view";
	public static final String P_VISIBLE_COLUMNS = P_DOMAIN + ".visibleColumns";
	public static final String P_GROUP_BY_COLUMNS = P_DOMAIN + ".groupByColumns"; 
	public static final String P_SORT_COLUMN = P_DOMAIN + ".sortColumn";

	public PlanAdvisorViewPreferencePage() {
		super(Activator.getDefault().getPreferenceStore());
		setDescription("Plan Advisor View");
	}
	
	@Override
	protected void createFieldEditors() {
		String[][] entryNamesAndValues = getEntryNamesAndValues();
		addField(new ComboFieldEditor(P_SORT_COLUMN, "Default &Sort Column", entryNamesAndValues, getFieldEditorParent()));
		
		Collection<String> groupableHeaderTexts = getGroupableHeaderTexts();
		PickListFieldEditor groupByEditor = new PickListFieldEditor(P_GROUP_BY_COLUMNS,
				"Grouping Columns",
				getFieldEditorParent(),
				groupableHeaderTexts, 
				true);
		addField(groupByEditor);
		
	}


	private String[][] getEntryNamesAndValues() {
		PlanAdvisorColumnSpecification[] values = PlanAdvisorColumnSpecification.values();
		String entryNamesAndValues[][] = new String[values.length][2];
		int i = 0; 
		for (PlanAdvisorColumnSpecification spec : values) {
			entryNamesAndValues[i][0] = spec.getHeaderText();
			entryNamesAndValues[i][1] = spec.getHeaderText();
			i++;
		}
		return entryNamesAndValues;
	}

	private Collection<String> getGroupableHeaderTexts() {
		PlanAdvisorColumnSpecification[] values = PlanAdvisorColumnSpecification.values();
		List<String> groupableHeaderTexts = new ArrayList<String>(values.length);
		for (PlanAdvisorColumnSpecification spec : values) {
			if (spec.isGroupable()) {
				groupableHeaderTexts.add(spec.getHeaderText());
			}
		}
		return groupableHeaderTexts;
	}

}
