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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.activityDictionary.resources.ModeledResourceDef;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.ESummaryResourceDef;
import gov.nasa.ensemble.dictionary.ObjectDef;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;

public class ResourceMergeColumnProvider implements IMergeColumnProvider {

	private static final String COLUMN_PROVIDER_NAME = "Effects";

	@Override
	public List<? extends AbstractMergeColumn<?>> getColumns() {
		List<AbstractMergeColumn<?>> columns = new ArrayList<AbstractMergeColumn<?>>();
		Iterable<EResourceDef> definitions = getResourceDefs();
		for (EResourceDef def : definitions) {
			if (!(def instanceof ModeledResourceDef)) {
				columns.add(new ResourceColumn(this, def));
			}
		}
		return columns;
	}

	@Override
	public String getName() {
		return COLUMN_PROVIDER_NAME;
	}

	public static Set<EResourceDef> getResourceDefs() {
		ActivityDictionary AD = ActivityDictionary.getInstance();
		Set<EResourceDef> definitions = new LinkedHashSet<EResourceDef>();
		definitions.addAll(AD.getDefinitions(ENumericResourceDef.class));
		definitions.addAll(AD.getDefinitions(ESummaryResourceDef.class));
		for (ObjectDef def : AD.getDefinitions(ObjectDef.class)) {
			for (EStructuralFeature feature : def.getEStructuralFeatures()) {
				if ((feature instanceof ENumericResourceDef) || (feature instanceof ESummaryResourceDef)) {
					EResourceDef resourceDef = (EResourceDef)feature;
					definitions.add(resourceDef);
				}
			}
		}
		return definitions;
	}

}
