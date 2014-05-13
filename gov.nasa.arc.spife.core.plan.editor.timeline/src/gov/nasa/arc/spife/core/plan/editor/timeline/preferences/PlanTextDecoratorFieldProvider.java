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
package gov.nasa.arc.spife.core.plan.editor.timeline.preferences;

import gov.nasa.arc.spife.core.plan.editor.timeline.policies.TemporalNodeDecoratorTextEditPolicy;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.TemporalNodeDecoratorTextEditPolicy.Provider;
import gov.nasa.arc.spife.ui.timeline.preference.TextDecoratorFieldProvider;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;

import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

public class PlanTextDecoratorFieldProvider extends TextDecoratorFieldProvider {

	private static final ParameterDescriptor PARAMETER_DESCRIPTOR = ParameterDescriptor.getInstance();

	public static final String P_DECORATOR_TEXT_KEY_NAME	= "timeline.decorator.text.key.name";
	public static final String P_DECORATOR_TEXT_KEY_NOTES	= "timeline.decorator.text.key.notes";

	@Override
	public String[][] getFieldValues() {
		ActivityDictionary ad = ActivityDictionary.getInstance();
		
		List<? extends EStructuralFeature> attributeDefs = ad.getAttributeDefs();
		
		String values[][] = new String[attributeDefs.size() + TemporalNodeDecoratorTextEditPolicy.PROVIDERS.size() + 3][2];
		values[0][0] = "None";
		values[0][1] = TimelinePreferencePage.P_DECORATOR_TEXT_KEY_NONE;
		
		values[1][0] = PARAMETER_DESCRIPTOR.getDisplayName(PlanPackage.Literals.EPLAN_ELEMENT__NAME);
		values[1][1] = P_DECORATOR_TEXT_KEY_NAME;
		
		values[2][0] = PARAMETER_DESCRIPTOR.getDisplayName(PlanPackage.Literals.COMMON_MEMBER__NOTES);
		values[2][1] = P_DECORATOR_TEXT_KEY_NOTES;
		
		int index = 3;
		for (EStructuralFeature def : attributeDefs) {
			values[index][0] = PARAMETER_DESCRIPTOR.getDisplayName(def);
			values[index][1] = def.getName();
			index++;
		}
		
		for (Provider provider : TemporalNodeDecoratorTextEditPolicy.PROVIDERS) {
			values[index][0] = provider.getDisplayName();
			values[index][1] = provider.getKey();
			index++;
		}
		
		return values;
	}

}
