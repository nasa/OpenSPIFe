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
package gov.nasa.arc.spife.core.plan.editor.timeline.models;

import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineContentProvider;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public abstract class GroupingTimelineContentProvider extends PlanTimelineContentProvider {
	
	private final Map<Object, Object> wrapperByValue = new HashMap<Object, Object>();
	private final Map<Object, Set<EActivity>> activitiesByWrapper = new HashMap<Object, Set<EActivity>>();
	private final Map<EActivity, List<? extends Object>> valuesByActivity = new HashMap<EActivity, List<? extends Object>>();
	private final Listener listener = new Listener();
	
	private static final Object NULL_VALUE = new Object();
	private TransactionalEditingDomain domain;
	
	public GroupingTimelineContentProvider(EPlan ePlan) {
		super(ePlan);
	}
	
	@Override
	public void activate() {
		domain = TransactionUtils.getDomain(getPlan());
		if (domain != null) {
			domain.addResourceSetListener(listener);
		}
		update();
	}
	
	@Override
	public void dispose() {
		if (domain != null) {
			domain.removeResourceSetListener(listener);
			domain = null;
		}
		clearCache();
	}

	private void clearCache() {
		activitiesByWrapper.clear();
		wrapperByValue.clear();
		valuesByActivity.clear();
	}
	
	public void update() {
		TransactionUtils.reading(getPlan(), new Runnable() {
			@Override
			public void run() {
				clearCache();
				for (Object o : getGroupingValues()) {
					Object wrapper = getWrapperByValue(o);
					getActivitiesByWrapper(wrapper).clear();
					getActivitiesByWrapper(wrapper).addAll(getActivitiesByValue(o));
				}
			}
		});
	}

	@Override
	public Object getParent(Object object) {
		if (activitiesByWrapper.containsKey(object)) {
			return getPlan();
		} else if (object instanceof EPlan) {
			return null;
		}
		return super.getParent(object);
	}

	@Override
	public Collection<?> getChildren(Object object) {
		if (object == getPlan()) {
			Collection<Object> children = new ArrayList<Object>();
			for (Object o : getGroupingValues()) {
				if (o == null) {
					o = NULL_VALUE;
				}
				
				Object wrapper = wrapperByValue.get(o);
				if (wrapper == null) {
					wrapper = getWrapperByValue(o);
					wrapperByValue.put(o, wrapper);
				}
				children.add(wrapper);
			}
			return children;
		} else if (activitiesByWrapper.containsKey(object)) {
			return activitiesByWrapper.get(object);
		}
		return super.getChildren(object);
	}
	
	protected Collection<EActivity> getActivitiesByValue(final Object value) {
		final Collection<EActivity> activities = new LinkedHashSet<EActivity>();
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
			    if (element instanceof EActivity) {
			    	EActivity activity = (EActivity) element;
					List<? extends Object> activityValues = getActivityValues(activity);
					if(activityValues != null) {
						for (Object aValue : activityValues) {
							if (CommonUtils.equals(value, aValue)) {
								activities.add(activity);
							}
						}
					}
			    }
			}
		}.visitAll(getPlan());
		return activities;
	}
	
	/**
	 * Allows implementing classes to override to indicate that the
	 * value wrapper exists with or without activities that populate them.
	 * 
	 *  Specifically, this allows for timeline content providers
	 *  that always show particular values
	 * @return true if the value wrapper exists even when no activities conform to the value, false otherwise
	 */
	protected boolean isFixedValueWrapper(Object wrapper) {
		return false;
	}

	@SuppressWarnings("unchecked")
	protected Collection getGroupingValues() {
		final Set<Object> values = new LinkedHashSet<Object>();
		final PlanVisitor planVisitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
			    if (element instanceof EActivity) {
			    	EActivity activity = (EActivity) element;
			    	List<? extends Object> activityValues  = Collections.emptyList();
					try {
						activityValues = getActivityValues(activity);
					} catch (Exception e) {
						LogUtil.error("getActivityValues implementation", e);
					}
					if(activityValues == null) {
						activityValues = Collections.emptyList();
					}
					valuesByActivity.put(activity, activityValues);
					values.addAll(activityValues);
				}
			}
		};
		// SPF-11648 -- Handle ConcurrentModificationException if there is a concurrent thread applying a patch to the plan
		// SPF-12198 -- The TransactionUtils.reading causes SaveAs with a restricted time range to fail
		// Replacing with a catch and redo approach as the ConcurrentModificationException happens
		// only rarely
//		TransactionUtils.reading(getPlan(), new Runnable() {
//			@Override
//			public void run() {
//				planVisitor.visitAll(getPlan());
//			}
//		});
		try {
			planVisitor.visitAll(getPlan());
		} catch (ConcurrentModificationException ex) {
			// Try again
			planVisitor.visitAll(getPlan());
		}
		Comparator<Object> comparator = getGroupingValuesComparator();
		if (comparator != null) {
			List<Object> list = new ArrayList<Object>(values);
			Collections.sort(list, comparator);
			return list;
		}
		return values;
	}

	protected Comparator<Object> getGroupingValuesComparator() {
		return null;
	}
	
	protected abstract List<? extends Object> getActivityValues(EActivity eActivity);
	
	protected abstract boolean isRelevant(EStructuralFeature f);
	
	protected String getValueString(Object value) {
		if (value instanceof EEnumLiteral) {
			return ((EEnumLiteral)value).getLiteral();
		} else if (value instanceof EObject) {
			IItemLabelProvider labeler = EMFUtils.adapt(value, IItemLabelProvider.class);
			if (labeler != null) {
				String text = labeler.getText(value);
				if (text != null) {
					return text;
				}
			}
		} else if (NULL_VALUE == value) {
			return "";
		}
		return value.toString();
	}
	
	private Object getWrapperByValue(Object value) {
		Object eActivityGroup = wrapperByValue.get(value);
		if (eActivityGroup == null) {
			if (value == null) {
				value = NULL_VALUE;
			}
			eActivityGroup = getValueWrapper(value);
			wrapperByValue.put(value, eActivityGroup);
		}
		return eActivityGroup;
	}

	protected Object getValueWrapper(Object value) {
		DynamicActivityGroup dag = new DynamicActivityGroup(getPlan(), value);
		dag.setName(getValueString(value));
		return dag;
	}

	private void activityValueAdded(EActivity activity, Object value, Set<Object> contentForRefresh) {
		if (getPlan() != EPlanUtils.getPlan(activity)) {
			return;
		}
		Object wrapper = getWrapperByValue(value);
		Collection<EActivity> activites = getActivitiesByWrapper(wrapper);
		activites.add(activity);
		if (!isFixedValueWrapper(wrapper) && activites.size() == 1) {
			contentForRefresh.add(getPlan());
		} else {
			contentForRefresh.add(wrapper);
		}
	}

	private void activityValueRemoved(EActivity eActivity, Object value, Set<Object> contentForRefresh) {
		Object wrapper = getWrapperByValue(value);
		Collection<EActivity> activites = getActivitiesByWrapper(wrapper);
		activites.remove(eActivity);
		if (!isFixedValueWrapper(wrapper) && activites.size() == 0) {
			wrapperByValue.remove(value);
			contentForRefresh.add(getPlan());
		} else {
			contentForRefresh.add(wrapper);
		}
	}
	
	private Set<EActivity> getActivitiesByWrapper(Object wrapper) {
		Set<EActivity> activities = activitiesByWrapper.get(wrapper);
		if (activities == null) {
			activities = new LinkedHashSet<EActivity>();
			activitiesByWrapper.put(wrapper, activities);
		}
		return activities;
	}

	private void handleActivityNotification(EActivity activity, Set<Object> contentForRefresh) {
		EPlan plan = EPlanUtils.getPlan(activity);
		if (getPlan() != plan) {
			return;
		}
		List<? extends Object> oldValues = valuesByActivity.get(activity);
		List<? extends Object> newValues = getActivityValues(activity);
		
		if(oldValues == null) {
			oldValues = new ArrayList<Object>();
		}
		
		if(newValues == null) {
			newValues = new ArrayList<Object>();
		}
		
		valuesByActivity.put(activity, newValues);
		Set<Object> added = new LinkedHashSet<Object>(newValues);
		added.removeAll(oldValues);
		Set<Object> removed = new LinkedHashSet<Object>(oldValues);
		removed.removeAll(newValues);
		for (Object removedValue : removed) {
			activityValueRemoved(activity, removedValue, contentForRefresh);
		}
		for (Object addedValue : added) {
			activityValueAdded(activity, addedValue, contentForRefresh);
		}
    }

	public void handleResourceSetChanged(ResourceSetChangeEvent event) {
		Set<Object> contentForRefresh = new LinkedHashSet<Object>();
		Set<EActivity> added = new HashSet<EActivity>();
		Set<EActivity> removed = new HashSet<EActivity>();
		for (Notification notification : event.getNotifications()) {
			Object f = notification.getFeature();
			if (PlanTimelinePackage.Literals.ABSTRACT_PLAN_SECTION__SHOW_UNREFERECED_ROW == f
					|| PlanTimelinePackage.Literals.PLAN_REFERENCED_OBJECT_SECTION__TYPE == f) {
				contentForRefresh.add(getPlan());
				break;
			}
			
			Object notifier = notification.getNotifier();
			if (notifier instanceof EMember) {
				notifier = ((EMember)notifier).getPlanElement();
			} else if (ADParameterUtils.isActivityAttributeOrParameter(notifier)) {
				notifier = ADParameterUtils.getActivityAttributeOrParameter(notifier);
			}
			switch (notification.getEventType()) {
			case Notification.ADD:
			case Notification.ADD_MANY:
			case Notification.REMOVE:
			case Notification.REMOVE_MANY:
			case Notification.SET:
			case Notification.UNSET: 
				if (notifier instanceof EActivity) {
					EActivity activity = (EActivity) notifier;
					EStructuralFeature feature = (EStructuralFeature) f;
					if (isRelevant(feature)) {
						handleActivityNotification(activity, contentForRefresh);
					}
					continue;
				}
			}
			removed.addAll(EPlanUtils.getActivitiesRemoved(notification));
			added.addAll(EPlanUtils.getActivitiesAdded(notification));
		}
		
		for (EActivity eActivity : added) {
			// remove any redundantly detected activities
			if (removed.contains(eActivity)) { 
				continue;
			}
			List<? extends Object> values = getActivityValues(eActivity);
			if (values != null) {
				for (Object value : values) {
					activityValueAdded(eActivity, value, contentForRefresh);
				}
			}
		}
		for (EActivity eActivity : removed) {
			// remove any redundantly detected activities
			if (added.contains(eActivity)) {
				continue;
			}
			List<? extends Object> values = getActivityValues(eActivity);
			if (values != null) {
				for (Object value : values) {
					activityValueRemoved(eActivity, value, contentForRefresh);
				}
			}
		}
		if (!contentForRefresh.isEmpty()) {
			refreshContents(contentForRefresh);
		}
		
	}

	private class Listener extends PostCommitListener {
		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			handleResourceSetChanged(event);
		}

	}
	
}
