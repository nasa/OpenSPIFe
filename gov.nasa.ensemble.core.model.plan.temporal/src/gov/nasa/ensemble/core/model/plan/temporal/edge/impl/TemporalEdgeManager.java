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
package gov.nasa.ensemble.core.model.plan.temporal.edge.impl;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.emf.resource.IgnorableResource;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;

public class TemporalEdgeManager extends ETemporalEdgeManagerImpl {

	private boolean initializedOrInitializing = false;
	
	private final Map<EObject, List<Long>> objectToTimes = new HashMap<EObject, List<Long>>();
	private final TreeMap<Long, Integer> timeReferences = new TreeMap<Long, Integer>();
	
	private EPlan plan;
	
	private Collection<TemporalEdgeContributor> contributors = ClassRegistry.createInstances(TemporalEdgeContributor.class);
	
	public TemporalEdgeManager(EPlan plan) {
		this.plan = plan;
		EditingDomain domain = EMFUtils.getAnyDomain(plan);
		final ResourceSet resourceSet = domain.getResourceSet();
		URI uri = URI.createURI(TemporalEdgeManager.this.plan.getName()+".edge");
		IgnorableResource resource = new IgnorableResource.Stub(uri);
		resource.getContents().add(TemporalEdgeManager.this);
		resourceSet.getResources().add(resource);
	}
	
	public void dispose() {
		initializedOrInitializing = true; // don't initialize after disposal
		plan = null;
		timeReferences.clear();
		objectToTimes.clear();
	}
	
	public EPlan getPlan() {
		return plan;
	}

	public boolean isImportant(Notification notification) {
		if (initializedOrInitializing) {
			for (TemporalEdgeContributor contributor : contributors) {
				if (contributor.isImportant(this, notification)) {
					return true;
				}
			}
		}
		return false;
	}

	public void processNotifications(List<Notification> notifications) {
		for (TemporalEdgeContributor contributor : contributors) {
			contributor.processNotifications(this, notifications);
		}
	}

	public void removeTimes(EObject owner) {
		synchronized (timeReferences) {
			List<Long> list = objectToTimes.get(owner);
			if (list != null) {
				objectToTimes.remove(owner);
				for (Long edge : list) {
					Integer count = timeReferences.get(edge);
					if (count == null || count < 2) {
						timeReferences.remove(edge);
					} else {
						timeReferences.put(edge, --count);
					}
				}
			}
		}
	}

	public void addTimes(EObject owner, List<Long> list) {
		synchronized (timeReferences) {
			objectToTimes.put(owner, list);
			for (Long time : list) {
				Integer count = timeReferences.get(time);
				if (count == null) {
					count = 0;
				}
				timeReferences.put(time, ++count);
			}
		}
	}

	public List<Long> getSortedTimes(Long startEdge, Long endEdge) {
		synchronized (timeReferences) {
			initialize();
			SortedSet<Long> set = timeReferences.navigableKeySet();
			set = set.tailSet(startEdge);
			set = set.headSet(endEdge);
			return new ArrayList<Long>(set);
		}
	}
	
	private void initialize() {
		if (!initializedOrInitializing) {
			initializedOrInitializing = true;
			for (TemporalEdgeContributor contributor : contributors) {
				contributor.initialize(this);
			}
		}
	}
	
}
