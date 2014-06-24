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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationKey;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.core.plan.advisor.view.preferences.PlanAdvisorViewPreferencePage;
import gov.nasa.ensemble.core.plan.advisor.view.preferences.PlanAdvisorViewPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreePathContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;

class ViolationContentProvider implements ITreePathContentProvider {
	
	private boolean flatten = false;
	private ViolationKey[] groupingOrder = new ViolationKey[] {
		ViolationKey.TYPE,
	};
	private Map<ViolationKey, Map<String, PlanAdvisorGroup>> groupMap;
	private final IPropertyChangeListener preferenceListener = new PlanAdvisorViewPreferencePropertyChangeListener();

	private Viewer viewer = null;
	private PlanAdvisorMember planAdvisorMember = null;
	
	public ViolationContentProvider() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.addPropertyChangeListener(preferenceListener);
		updateGroupingOrder();
	}

	@Override
	public synchronized void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
		if (newInput instanceof EPlan) {
			EPlan plan = (EPlan)newInput;
			planAdvisorMember = PlanAdvisorMember.get(plan);
		}
		createGroupMap();
	}
	
	@Override
	public void dispose() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.removePropertyChangeListener(preferenceListener);
		viewer = null;
		planAdvisorMember = null;
	}
	
	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(null);
	}
	
	@Override
	public synchronized Object[] getChildren(TreePath parentPath) {
		if (flatten) {
			if (parentPath == null) {
				return planAdvisorMember.getViolationTrackers().toArray();
			}
			return new Object[0];
		}
		int count = ((parentPath == null) ? 0 : parentPath.getSegmentCount());
		List<ViolationTracker> trackers = planAdvisorMember.getViolationTrackers();
		if (parentPath != null) {
			List<PlanAdvisorGroup> groups = getGroupsFromPath(parentPath);
			ListIterator<ViolationTracker> trackerIterator = trackers.listIterator(); 
			while (trackerIterator.hasNext()) {
				Violation violation = trackerIterator.next().getViolation();
				for (int i = 0 ; i < groups.size() ; i++) {
					PlanAdvisorGroup group = groups.get(i);
					ViolationKey key = group.getKey();
					String value = violation.getPrintString(key);
					if (!CommonUtils.equals(group.getValue(), value)) {
						trackerIterator.remove();
						break;
					}
				}
			}
		}
		if (count >= groupingOrder.length) {
			return trackers.toArray();
		}
		return groupsFor(groupingOrder[count], trackers);
	}

	@Override
	public synchronized boolean hasChildren(TreePath path) {
		if (flatten) {
			return false;
		}
		return (path == null) || (path.getSegmentCount() <= groupingOrder.length);
	}

	@Override
	public TreePath[] getParents(Object element) {
		List<Object> segments = new ArrayList<Object>();
		Object segment = getSegment(element);
		if (segment != null) {
			segments.add(segment);
			// should find parent groups here
		}
		if (segments.isEmpty()) {
			return new TreePath[0];
		}
		return new TreePath[] { new TreePath(segments.toArray()) };
	}

	private Object getSegment(Object element) {
		for (Map<String, PlanAdvisorGroup> map : groupMap.values()) {
			for (PlanAdvisorGroup group : map.values()) {
				if (group.getViolationTrackers().contains(element)) {
					return group;
				}
			}
		}
		return null;
	}

	public boolean isFlatten() {
		return flatten;
	}
	
	public void toggleFlatten() {
		flatten = !flatten;
	}

	/*
	 * Utility methods
	 */
	
	private Object[] groupsFor(ViolationKey key, List<ViolationTracker> trackers) {
		SortedMap<String, LinkedHashSet<ViolationTracker>> valueViolationsMap = new TreeMap<String, LinkedHashSet<ViolationTracker>>();
		for (ViolationTracker tracker : trackers) {
			String value = tracker.getViolation().getPrintString(key);
			LinkedHashSet<ViolationTracker> valueViolations = valueViolationsMap.get(value);
			if (valueViolations == null) {
				valueViolations = new LinkedHashSet<ViolationTracker>();
				valueViolationsMap.put(value, valueViolations);
			}
			valueViolations.add(tracker);
		}
		PlanAdvisorGroup[] groups = new PlanAdvisorGroup[valueViolationsMap.size()];
		int i = 0;
		for (Map.Entry<String, LinkedHashSet<ViolationTracker>> entry : valueViolationsMap.entrySet()) {
			PlanAdvisorGroup group = findGroup(key, entry.getKey());
			group.setViolationTrackers(entry.getValue());
			groups[i++] = group;
		}
		Map<String, PlanAdvisorGroup> valueMap = groupMap.get(key);
		valueMap.keySet().retainAll(valueViolationsMap.keySet()); // throw away groups that we don't need any longer
		return groups;
	}

	private PlanAdvisorGroup findGroup(ViolationKey key, String value) {
		Map<String, PlanAdvisorGroup> valueMap = groupMap.get(key);
		PlanAdvisorGroup group = valueMap.get(value);
		if (group == null) {
			group = new PlanAdvisorGroup(key, value);
			valueMap.put(value, group);
		}
		return group;
	}

	private static List<PlanAdvisorGroup> getGroupsFromPath(TreePath parentPath) {
		List<PlanAdvisorGroup> groups = new ArrayList<PlanAdvisorGroup>(parentPath.getSegmentCount());
		for (int i = 0 ; i < parentPath.getSegmentCount() ; i++) {
			Object segment = parentPath.getSegment(i);
			if (segment instanceof PlanAdvisorGroup) {
				PlanAdvisorGroup group = (PlanAdvisorGroup) segment;
				groups.add(group);
			} else {
				// Shouldn't be here
			}
		}
		return groups;
	}

	private void createGroupMap() {
		Map<ViolationKey, Map<String, PlanAdvisorGroup>> map = new HashMap<ViolationKey, Map<String, PlanAdvisorGroup>>();
		for (ViolationKey key : ViolationKey.values()) {
			map.put(key, new LinkedHashMap<String, PlanAdvisorGroup>());
		}
		groupMap = map;
	}

	private synchronized void updateGroupingOrder() {
		List<ViolationKey> keys = new ArrayList<ViolationKey>();
		for (PlanAdvisorColumnSpecification spec : PlanAdvisorViewPreferences.getGroupByColumns()) {
			keys.add(spec.getViolationKey());
		}
		groupingOrder = keys.toArray(new ViolationKey[keys.size()]);
		createGroupMap();
	}
	
	private class PlanAdvisorViewPreferencePropertyChangeListener implements IPropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (CommonUtils.equals(event.getProperty(), PlanAdvisorViewPreferencePage.P_GROUP_BY_COLUMNS)) {
				updateGroupingOrder();
				Viewer v = viewer;
				if (v != null) {
					v.refresh();
				}
			}
		}
	}

}
