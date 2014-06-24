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
package gov.nasa.ensemble.core.plan.resources;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.ADEffectMember;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.IMember;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.dependency.DependencyPackage;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.DependencyMaintenanceSystem;
import gov.nasa.ensemble.emf.transaction.ThreadedNotificationDelegate;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.RunnableWithResult;

public class ResourceUpdater extends ThreadedNotificationDelegate implements IMember {

	private static final Logger trace = Logger.getLogger(ResourceUpdater.class);

	private DependencyMaintenanceSystem dms;
	
	private EPlan plan;

	boolean isIntialization = false;

	private boolean queueEnabled = false;
	private Set<Dependency> queuedDependencies = new LinkedHashSet<Dependency>();
	private ResourceUpdaterQueuePredicate queuePredicate = ResourceUpdaterQueuePredicate.createInstance();
	
	private Map<Dependency, Integer> updateByNodeCount = new HashMap<Dependency, Integer>();
	
	protected ResourceUpdater(EPlan plan) {
		super(ResourceUpdater.class.getSimpleName()+":"+plan.getName(), 500);
		this.plan = plan;
		if (plan.eResource() == null) {
			LogUtil.error("missing resource for plan: " + plan.getName());
		}
	}
	
	public boolean isQueueEnabled() {
		return queueEnabled;
	}

	public void setQueueEnabled(boolean queueEnabled) {
		this.queueEnabled = queueEnabled;
		if (!queueEnabled && !queuedDependencies.isEmpty()) {
			TransactionUtils.writing(plan, new Runnable() {
				@Override
				public void run() {
					Set<Dependency> dependencies = new LinkedHashSet<Dependency>(queuedDependencies);
					queuedDependencies.clear();
					update(dependencies);
				}
			});
		}
	}
	
	public ResourceUpdaterQueuePredicate getQueuePredicate() {
		return queuePredicate;
	}

	public void setQueuePredicate(ResourceUpdaterQueuePredicate queuePredicate) {
		this.queuePredicate = queuePredicate;
	}

	public static void recompute(final EPlan ePlan) {
		ResourceUpdaterFactory.setTestingResourceUpdater(true);
		final ResourceUpdater updater;
		try {
			updater = WrapperUtils.getMember(ePlan, ResourceUpdater.class);
		} finally {
			ResourceUpdaterFactory.setTestingResourceUpdater(false);
		}
		updater.joinInitialization();
		TransactionUtils.writing(ePlan, new Runnable() {
			@Override
			public void run() {
				updater.update(updater.dms.getGraph().getDependencies()); 
			}
		});
		try {
			updater.waitUntilFinished();
		} catch (InterruptedException e) {
			LogUtil.error(e);
		}
	}
	
	@Override
	public void dispose() {
		quit();
		if (dms != null) dms.dispose();
		plan = null;
	}
	
	@Override
	protected void initialize() {
		isIntialization = true;
		this.dms = new DependencyMaintenanceSystem(plan);
		final List<Dependency> rootDependencies = this.dms.getRootNodes();
		TransactionUtils.writing(EMFUtils.getAnyDomain(plan), new Runnable() {
			@Override
			public void run() {
				//
				// We want to clear all previously computed effects (SPF-8487 & SPF-8615)
				new PlanVisitor(true) {
					@Override
					protected void visit(EPlanElement element) {
						ADEffectMember m  = element.getMember(ADEffectMember.class);
						if (m != null) {
							m.getEffects().clear();
						}
					}
				}.visitAll(plan);
				update(rootDependencies);
			}
		});
		isIntialization = false;
	}
	
	@Override
	protected boolean isImportant(Notification notification) {
		Object feature = notification.getFeature();
		return 
			//
			// New Dependency added to the graph
			DependencyPackage.Literals.GRAPH__DEPENDENCIES == feature
			//
			// Previous dependency added
			|| DependencyPackage.Literals.DEPENDENCY__PREVIOUS == feature
			//
			// Invalidation
			|| (DependencyPackage.Literals.DEPENDENCY__VALID == feature
					&& notification.getNewBooleanValue() == false);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void processNotifications(List<Notification> notifications) {
		if (isIntialization) {
			return;
		}
		final Set<Dependency> dependencies = new LinkedHashSet<Dependency>();
		for (Notification notification : notifications) {
			Object feature = notification.getFeature();
			Object newValue = notification.getNewValue();
			if (DependencyPackage.Literals.GRAPH__DEPENDENCIES == feature) {
				switch (notification.getEventType()) {
				case Notification.ADD:
					dependencies.add((Dependency) newValue);
					break;
				case Notification.ADD_MANY:
					dependencies.addAll((Collection<Dependency>) newValue);
					break;
				case Notification.REMOVE:
				case Notification.REMOVE_MANY:
					// handle below
					break;
				}
			} 
			else if (DependencyPackage.Literals.DEPENDENCY__PREVIOUS == feature) {
				if (Notification.REMOVE_MANY == notification.getEventType()
					|| Notification.REMOVE == notification.getEventType())
				{
					Dependency dependency = (Dependency) notification.getNotifier();
					// SPF-9862 -- Don't add the notifier dependency if it has been removed
					// from the dependency graph and shouldn't be updated in that case
					if (dependency != null) {
						if (dependency.eContainer() != null || dependency.shouldUpdateIfRemoved()) {
							dependencies.add(dependency);
						}
					}
				}
			} 
			else if (DependencyPackage.Literals.DEPENDENCY__VALID == feature) {
				if (!notification.getNewBooleanValue()) {
					dependencies.add((Dependency) notification.getNotifier());
				}
			}
		}
		update(dependencies);
	}

	private Set<Dependency> queue = new LinkedHashSet<Dependency>();
	private Integer update(final Collection<Dependency> nodes) {
		if (nodes.isEmpty()) {
			return 0;
		}
		
		return TransactionUtils.writing(getDependencyMaintenanceSystem().getGraph(), new RunnableWithResult.Impl<Integer>() {
			
			@Override
			public void run() {
				Set<Dependency> visited = new HashSet<Dependency>();
				Set<Dependency> dependencies = new LinkedHashSet<Dependency>(nodes);
				queue.addAll(dependencies);
				
				int updateCount = 0;
				//
				// Update
				int initialSetSize = dependencies.size() / 2;
				//
				// Update
				Dependency dependency = null;
				Iterator<Dependency> iterator = queue.iterator();
				if (iterator.hasNext()) {
					dependency = iterator.next();
					iterator.remove();
				}
				while (dependency != null) {
					if (shouldQueue(dependency)) {
						queuedDependencies.add(dependency);
					} else {
						boolean updated = dependency.update();
						if (!visited.contains(dependency) && isIntialization) {
							updated = true;
						}
						visited.add(dependency);
						logUpdate(dependency, 0, updated);
						if (updated) {
							updateCount++;
							List<Dependency> nextNodes = dependency.getNext();
							for (Dependency d : nextNodes) {
								queue.remove(d);
							}
							queue.addAll(nextNodes);
						}
					}
					iterator = queue.iterator();
					if (iterator.hasNext()) {
						dependency = iterator.next();
						iterator.remove();
					} else {
						dependency = null;
					}
				}
				dependencies.removeAll(queuedDependencies);
				Set<Dependency> applied = new HashSet<Dependency>(initialSetSize);
				for (Dependency d : dependencies) {
					applyValue(d, applied);
				}
				setResult(updateCount);
				if (trace.isDebugEnabled()) {
					debugUpdateCounts();
				}
			}
			
		});
	}

	private void debugUpdateCounts() {
		Set<Entry<Dependency, Integer>> entrySet = updateByNodeCount.entrySet();
		List<Entry<Dependency, Integer>> entryList = new ArrayList<Map.Entry<Dependency,Integer>>(entrySet);
		Collections.sort(entryList, new Comparator<Entry<Dependency, Integer>>() {

			@Override
			public int compare(Entry<Dependency, Integer> e0, Entry<Dependency, Integer> e1) {
				Integer v0 = e0.getValue();
				Integer v1 = e1.getValue();
				return v0.compareTo(v1);
			}
			
		});
		trace.debug("Updates by node");
		for (Entry<Dependency, Integer> e : entryList) {
			trace.debug("\t"+e.getValue()+"\t"+e.getKey());
		}
	}
	
	private boolean shouldQueue(Dependency dependency) {
		if (!queueEnabled)
			return false;
		return queuePredicate.apply(dependency);
	}

	private void applyValue(Dependency dependency, Set<Dependency> visited) {
		if (!visited.add(dependency)) {
			return;
		}
		try {
			dependency.applyValue();
		} catch (Exception e) {
			trace.error("applying value for "+dependency, e);
		}
		List<Dependency> nextDependencies = dependency.getNext();
		int size = nextDependencies.size();
		for (int i=0; i<size; i++) {
			Dependency next = nextDependencies.get(i);
			applyValue(next, visited);
		}
	}

	public DependencyMaintenanceSystem getDependencyMaintenanceSystem() {
		return dms;
	}

	private void logUpdate(Dependency dependency, int t, boolean updated) {
		if (trace.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer();
			for (int i=0; i<t; i++) {
				buffer.append("\t");
			}
			buffer.append(updated + " - " + dependency.toString() + " - " + dependency.getValue());
			trace.debug(buffer.toString());
		}
	}

}
