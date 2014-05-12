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
package gov.nasa.ensemble.dictionary.nddl;

import gov.nasa.ensemble.dictionary.ActivityDictionaryContributor;
import gov.nasa.ensemble.dictionary.DictionaryFactory;
import gov.nasa.ensemble.dictionary.EActivityDictionary;
import gov.nasa.ensemble.dictionary.ERule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelGeneratorActivityDictionaryContributor extends ActivityDictionaryContributor {

	@Override
	public void contribute(EActivityDictionary ad) {
		buildRules(ad);
	}

	private void buildRules(EActivityDictionary ad) {
		ParseInterpreter interpreter = ModelGenerator.parseInterpret(ad);
		List<String> claimNames = interpreter.getClaimNames();
		for (String claimName : claimNames) {
			List<String> path = Arrays.asList(new String[] { "Claims", claimName });
			String printName = claimName;
			String name = claimName;
			String shortDescription = claimName + " conflict";
			String hypertextDescription = "Only one activity can claim the " + claimName + " at any given time";
			ERule rule = DictionaryFactory.eINSTANCE.createERule(path, name, printName, shortDescription, hypertextDescription);
			ad.getExtendedDefinitions().add(rule);
		}
		List<String> stateNames = interpreter.getStateNames();
		for (String stateName : stateNames) {
			List<String> path = Arrays.asList(new String[] { "States", stateName });
			String printName = stateName;
			String name = stateName;
			String shortDescription = stateName + " conflict";
			String hypertextDescription = "Only one activity can claim the " + stateName + " at any given time";
			ERule rule = DictionaryFactory.eINSTANCE.createERule(path, name, printName, shortDescription, hypertextDescription);
			ad.getExtendedDefinitions().add(rule);
		}
		HashMap<String, List<String>> exclusiveActsStatesMap = interpreter.getexclusiveActsStatesMap();
		for (Map.Entry<String, List<String>> entry : exclusiveActsStatesMap.entrySet()) {
			String key = entry.getKey();
			List<String> resources = entry.getValue();
			String[] types = key.split("__mx__");
			if (types.length == 2) {
				String type0 = types[0];
				String type1 = types[1];
				for (String resource : resources) {
					List<String> path = Arrays.asList(new String[] { "Mutual Exclusions", resource, type0, type1 });
					String printName = resource + " : " + type0 + " x " + type1;
					String name = key + "__" + resource;
					String explanation = "because they conflict on the resource " + resource;
					String shortDescription = type0 + " and " + type1 + " are mutually exclusive " + explanation + ".";
					String hypertextDescription = "An instance of " + type0 + " and an instance of " + type1
							+ " are overlapping.";
					hypertextDescription += "  They are not allowed to overlap " + explanation + ".";
					ERule rule = DictionaryFactory.eINSTANCE.createERule(path, name, printName, shortDescription, hypertextDescription);
					ad.getExtendedDefinitions().add(rule);
				}
			} else {
				System.err.println("Surprise: " + key);
			}
		}
	}

}
