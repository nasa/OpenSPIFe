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
package gov.nasa.arc.spife.core.plan.rules.view;

import gov.nasa.ensemble.dictionary.ERule;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RuleGroup {

	private final String label;
	private final RuleGroup parent;
	private final Map<String,RuleGroup> groups = new LinkedHashMap<String, RuleGroup>();
	private final List<ERule> rules = new ArrayList<ERule>();

	public RuleGroup(String label) {
		this(label, null);
	}

	public RuleGroup(String label, RuleGroup parent) {
		this.label = label;
		this.parent = parent;
	}

	public String getLabel() {
		return label;
	}
	
	/**
	 * Return the group that contains this group, or null if none does
	 * @return
	 */
	public RuleGroup getParent() {
		return parent;
	}
	
	/**
	 * Return all the subgroups of this group
	 * @return
	 */
	public Map<String, RuleGroup> getGroups() {
		return groups;
	}
	
	/**
	 * Returns a list of the rules that are immediately contained by this group
	 * @return
	 */
	public List<ERule> getRules() {
		return rules;
	}

	/**
	 * Return the subgroup for this label, creating it if necessary. 
	 * @param label
	 * @return
	 */
	public RuleGroup getGroupForLabel(String label) {
		RuleGroup group = groups.get(label);
		if (group == null) {
			group = new RuleGroup(label, this);
			groups.put(label, group);
		}
		return group;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("RuleGroup");
		builder.append("[");
		builder.append("label=");
		builder.append(getLabel());
		builder.append("[");
		return builder.toString();
	}
	
}
