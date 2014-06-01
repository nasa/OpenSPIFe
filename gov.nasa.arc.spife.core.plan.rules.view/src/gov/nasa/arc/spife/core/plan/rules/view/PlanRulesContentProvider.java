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

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.dictionary.ERule;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PlanRulesContentProvider implements ITreeContentProvider {

	private static final Object[] NO_CHILDREN = new Object[] {};
	private EPlanElement currentPlanElement = null;
	private final RuleGroup rootGroup = new RuleGroup("All Rules");
	private final Object[] rootObjects = new Object[] { rootGroup };
	private final Map<ERule, RuleGroup> ruleToGroup = new HashMap<ERule, RuleGroup>();
	
	public PlanRulesContentProvider() {
		List<ERule> rules = ActivityDictionary.getInstance().getDefinitions(ERule.class);
		for (ERule rule : rules) {
			List<String> categoryPath = rule.getPath();
			RuleGroup group = rootGroup;
			int i = categoryPath.size() - 1;
			for (String category : categoryPath) {
				if (i-- <= 0) {
					break;
				}
				group = group.getGroupForLabel(category);
			}
			group.getRules().add(rule);
			ruleToGroup.put(rule, group);
		}
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof EPlanElement) {
			currentPlanElement = (EPlanElement) newInput;
		}
	}

	@Override
	public void dispose() {
		if (currentPlanElement != null) {
			currentPlanElement = null;
		}
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof RuleGroup) {
			RuleGroup group = (RuleGroup)parentElement;
			Collection<RuleGroup> groups = group.getGroups().values();
			List<ERule> rules = group.getRules();
			Object[] children = new Object[groups.size() + rules.size()];
			int i = 0;
			for (RuleGroup child : groups) {
				children[i++] = child;
			}
			for (ERule child : rules) {
				children[i++] = child;
			}
			return children;
		}
		return NO_CHILDREN;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof RuleGroup) {
			RuleGroup group = (RuleGroup)element;
			return group.getParent();
		}
		if (element instanceof ERule) {
			ERule rule = (ERule) element;
			RuleGroup group = ruleToGroup.get(rule);
			return group;
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof RuleGroup) {
			RuleGroup group = (RuleGroup)element;
			if (!group.getGroups().isEmpty()) {
				return true;
			}
			if (!group.getRules().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement == currentPlanElement) {
			return rootObjects;
		}
		return NO_CHILDREN;
	}

}
