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
package gov.nasa.ensemble.core.plan.formula.js;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.Plan;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptablePlan extends ScriptableObject {

	private final EPlan plan;

	private static Set<String> ERROR_MESSAGES = new HashSet<String>(); 
	private static final Map<String, ScriptablePlanContributor> CONTRIBUTORS_MAP = new HashMap<String, ScriptablePlanContributor>(); 
	private static final List<ScriptablePlanContributor> CONTRIBUTORS = ClassRegistry.createInstances(ScriptablePlanContributor.class);
	private static final List<ScriptablePlanContributorDelegate> DELEGATE = ClassRegistry.createInstances(ScriptablePlanContributorDelegate.class);
	
	public ScriptablePlan(EPlan plan) {
		this.plan = plan;
	}
	
	public EPlan getPlan() {
		return plan;
	}

	@Override
	public String getClassName() {
		return Plan.class.getSimpleName();
	}

	@Override
	public Object get(String name, Scriptable start) {
		ScriptablePlanContributor contributor = getPlanContributors().get(name);
		if (contributor == null) {
			for (ScriptablePlanContributorDelegate factory : DELEGATE) {
				contributor = factory.get(name, start);
				if (contributor != null) {
					break;
				}
			}
		}
		
		Object value = contributor != null ? 
						contributor.get(start) 
						: super.get(name, start);
		return JSUtils.unwrap(value, start);
	}
	
	private Map<String, ScriptablePlanContributor> getPlanContributors() {
		if (CONTRIBUTORS_MAP.isEmpty()) {
			for (ScriptablePlanContributor c : CONTRIBUTORS) {
				if (CONTRIBUTORS_MAP.put(c.getPropertyName(), c) != null) {
					String msg = "collision on contributor name: " + c.getPropertyName();
					if (!ERROR_MESSAGES.contains(msg)) {
						ERROR_MESSAGES.add(msg);
						Logger.getLogger(ScriptablePlan.class).warn(msg);
					}
				}
			}
		}
		return CONTRIBUTORS_MAP;
	}

}
