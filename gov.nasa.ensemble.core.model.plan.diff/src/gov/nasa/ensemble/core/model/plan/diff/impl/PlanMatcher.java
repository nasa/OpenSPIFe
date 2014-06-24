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
package gov.nasa.ensemble.core.model.plan.diff.impl;

import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.OldAndNewCopyOfSameThing;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This plan matcher is much less ambitious than EMF Diff's GenericMatchEngine:
 * <ol>
 * <li> Non-heuristic, relies entirely on diffId
 * <li> No MatchModel, just a pairing of what matches and doesn't match.
 * <li> Discriminates between EPlanElements and other EObjects. 
 *    Can't move a parameter from one activity to another!
 *    </ol>
 * @author kanef
 *
 */
public class PlanMatcher {
	
	protected final EPlan oldPlan;
	protected final EPlan newPlan;
	// Maps from diffId to plan element
	private Map<String, EPlanChild> onlyInOldPlan = new HashMap<String, EPlanChild>();
	private Map<String, EPlanChild> onlyInNewPlan = new HashMap<String, EPlanChild>();
	private Map<String, OldAndNewCopyOfSameThing> inBothPlans = new HashMap<String, OldAndNewCopyOfSameThing>();
	
	private Collection<EPlanChild> addedElements;
	private Collection<EPlanChild> deletedElements;
	private Collection<OldAndNewCopyOfSameThing> commonElements;
	
	public PlanMatcher (EPlan oldPlan, EPlan newPlan) {
		this.oldPlan = oldPlan;
		this.newPlan = newPlan;
	}
	
	public void match() {
		initializeMap(onlyInOldPlan, oldPlan);
		initializeMap(onlyInNewPlan, newPlan);
		moveAroundInMaps();
		addedElements = new ArrayList(onlyInNewPlan.values());
		deletedElements = new ArrayList(onlyInOldPlan.values());
		commonElements = new ArrayList(inBothPlans.values());
	}


	public Collection<EPlanChild> getAddedElements() {
		return addedElements;
	}


	public Collection<EPlanChild> getDeletedElements() {
		return deletedElements;
	}


	public Collection<OldAndNewCopyOfSameThing> getCommonElements() {
		return commonElements;
	}

	protected void initializeMap(final Map<String, EPlanChild> map, EPlan plan) {
		new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				if (element instanceof EPlanChild) {
					map.put(getId(element), (EPlanChild) element);
				}
			}
		}.visitAll(plan);
	}

	protected String getId(EPlanElement element) {
		return element.getMember(CommonMember.class).getDiffID();
	}


	private void moveAroundInMaps() {
		for (String id : onlyInOldPlan.keySet()) {
			if (onlyInNewPlan.containsKey(id)) {
				EPlanChild oldCopy = onlyInOldPlan.get(id);
				EPlanChild newCopy = onlyInNewPlan.get(id);
				inBothPlans.put(id, new OldAndNewCopyOfSameThingImpl(oldCopy, newCopy));
			}
		}
		// Second pass, to avoid java.util.ConcurrentModificationException
		for (String id : inBothPlans.keySet()) {
			onlyInOldPlan.remove(id);
			onlyInNewPlan.remove(id);
		}
	}
	
	public EPlanElement findOldCopy(EPlanElement newCopy) {
		OldAndNewCopyOfSameThing pair = inBothPlans.get(getId(newCopy));
		if (pair==null) return null;
		return pair.getOldCopy();
	}

	public EPlanElement findNewCopy(EPlanElement oldCopy) {
		OldAndNewCopyOfSameThing pair = inBothPlans.get(getId(oldCopy));
		if (pair==null) return null;
		return pair.getNewCopy();
	}

	public boolean equalIDs(EPlanElement a, EPlanElement b) {
		if (a==null || b==null) return false; // ignore broken constraints; see SPF-8389, (one known cause was SPF-8391)
		String id1 = getId(a);
		String id2 = getId(b);
		return (id1 != null && id2 != null && id1.equals(id2));
	}

}
