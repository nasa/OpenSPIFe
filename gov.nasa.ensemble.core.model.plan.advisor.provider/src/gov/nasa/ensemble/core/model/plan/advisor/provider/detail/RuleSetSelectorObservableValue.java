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
package gov.nasa.ensemble.core.model.plan.advisor.provider.detail;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.ERule;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

public class RuleSetSelectorObservableValue extends AbstractObservableValue {

	private final RulesSelector planRulesSelector;
	private final WidgetListener widgetListener = new WidgetListener();

	public RuleSetSelectorObservableValue(RulesSelector planRulesSelector) {
		this.planRulesSelector = planRulesSelector;
		planRulesSelector.addListener(widgetListener);
		planRulesSelector.getButton().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		});
	}
	
	@Override
	public synchronized void dispose() {
		planRulesSelector.removeListener(widgetListener);
		super.dispose();
	}
	
	@Override
	protected void doSetValue(Object value) {
		if ((value == null) || (value instanceof List)) {
			@SuppressWarnings("unchecked")
			List<String> names = (List<String>)value;
			Set<ERule> set = new LinkedHashSet<ERule>();
			if (names != null) {
				for (String name : names) {
					ERule rule = ActivityDictionary.getInstance().getDefinition(ERule.class, name);
					if (rule != null) {
						set.add(rule);
					} else {
						LogUtil.warn("unknown rule name: " + name);
					}
				}
			}
			planRulesSelector.setWaivedRules(set);
		}
	}
	
	@Override
	protected Object doGetValue() {
		Set<ERule> waivedRules = planRulesSelector.getWaivedRules();
		List<String> names = new ArrayList<String>();
		for (ERule rule : waivedRules) {
			names.add(rule.getName());
		}
		return names;
	}

	@Override
	public Object getValueType() {
		return List.class;
	}

	private final class WidgetListener implements IPropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			Object oldValue = event.getOldValue();
			Object newValue = event.getNewValue();
			fireValueChange(Diffs.createValueDiff(oldValue, newValue));
		}
	}
	
}
