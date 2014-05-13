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
package gov.nasa.ensemble.core.activityDictionary.events;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.collections.AutoSetMap;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EStateRequirement;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Used for identifying activities involved in LASS's Snap To Orbit
 * and similar features contemplated for MSLICE.
 * @since SPF-7929
 *
 */
public class EventSubsetOfDictionary {

	private ActivityDictionary dictionary;
	private static String identifyingArgName = EnsembleProperties.getProperty("lass.integrate.ad.argument.for.events", "orbit");

	public static String getIdentifyingArgName() {
		return identifyingArgName;
	}

	public static void setIdentifyingArgName(String identifyingArgName) {
		EventSubsetOfDictionary.identifyingArgName = identifyingArgName;
	}

	public EventSubsetOfDictionary() {
		this(ActivityDictionary.getInstance());
	}
	
	public EventSubsetOfDictionary(ActivityDictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	public Map<String, Set<EActivityDef>> getProfileEventMapping() {
		return getProfileEventMapping(eventDefinitions());
	}

	public List<EActivityDef> eventDefinitions() {
		return activitiesThatTakeArgumentNamed(identifyingArgName);
	}
	
	public List<EActivityDef> activitiesThatTakeArgumentNamed(String argName) {
		List<EActivityDef> result = new LinkedList<EActivityDef>();
		List<EActivityDef> allDefinitions = dictionary.getActivityDefs();
		for (EActivityDef def : allDefinitions) {
			EStructuralFeature feature = def.getEStructuralFeature(argName);
			if (feature != null) {
				result.add(def);
			}
		}
		return result;
	}

	public Map<String, Set<EActivityDef>> getProfileEventMapping(Collection<EActivityDef> eventdefs) {
		AutoSetMap<String, EActivityDef> result = new AutoSetMap<String, EActivityDef>(String.class);
		for (EActivityDef def : eventdefs) {
			List<EStateRequirement> stateRequirements = def.getStateRequirements();
			for (EStateRequirement requirement : stateRequirements) {
				String profileName = requirement.getName();
				result.get(profileName).add(def);
				LogUtil.warn("ConditionImportOperation:  Added " + def.getName() + " to " + profileName);
			}
			if (stateRequirements.isEmpty()) {
				LogUtil.warn("Activity Def " + def.getName() + " has no state requirements.");
			}
			if (stateRequirements.size() > 1) {
				LogUtil.warn("Activity Def " + def.getName() + " with more than one state requirement is unlikely to be auto-created/updated to satisfy all requirements.");
			}
		}
		return result;
	}

}
