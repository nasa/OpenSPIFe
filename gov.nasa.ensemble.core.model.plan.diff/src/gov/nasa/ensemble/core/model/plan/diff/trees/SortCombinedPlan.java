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
package gov.nasa.ensemble.core.model.plan.diff.trees;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.diff.api.OldAndNewCopyOfSameThing;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;

/**
 * Used by a PlanDiffNode to sort those of its children that contain plan elements,
 * i.e. those that are PlanDiffObjectNode instances.
 * @author kanef
 *
 */

public class SortCombinedPlan implements Comparator<PlanDiffNode> {
	
	Map<EObject,Integer> ranks = new HashMap<EObject, Integer>();
	EObject lastObjectSeen = null;
	
	public SortCombinedPlan (EPlan plan1, EPlan plan2, PlanDiffList diffModel) {
		ranks.put(null, 0);
		notePlanOrdering(plan2);
		noteEquivalents(diffModel.getCommonElements());
		notePlanOrdering(plan1);
	}
	
	@Override
	public int compare(PlanDiffNode arg1, PlanDiffNode arg2) {
		if (arg1 instanceof PlanDiffObjectNode && arg2 instanceof PlanDiffObjectNode)
			return compare((PlanDiffObjectNode)arg1, (PlanDiffObjectNode)arg2);
		if (arg1 instanceof PlanDiffObjectNode) return +1; // Put Modification Node above Object Node
		if (arg2 instanceof PlanDiffObjectNode) return -1; // e.g. group's color change goes above its children's mods
		return 0;
	}

	public int compare(PlanDiffObjectNode arg1, PlanDiffObjectNode arg2) {
		Integer rank1 = ranks.get(arg1.getObject());
		Integer rank2 = ranks.get(arg2.getObject());
		if (rank1==null) rank1 = -100; // should not occur
		if (rank2==null) rank2 = -100;
		if (rank1 < rank2) return -1;
		if (rank1 > rank2) return +1;
		return 0;
	}

	private void notePlanOrdering (EPlan plan) {
		TreeIterator<EObject> iterator = plan.eAllContents();
		while (iterator.hasNext()) {
			EObject element = iterator.next();
			placeAfter(element, lastObjectSeen);
			lastObjectSeen = element;
		}
	}

	private void noteEquivalents (Collection<OldAndNewCopyOfSameThing> equivalents) {
		for (OldAndNewCopyOfSameThing pair : equivalents) {
			noteEquivalence(pair.getOldCopy(), pair.getNewCopy());
		}
	}	
	
	/**
	 * Use when traversing one of the plans to indicate the order of groups or of activities,
	 * or to indicate that an activity follows its parent group.
	 * Also, indicate the order of parameters, or that a parameter follows the activity containing it.
	 * @param newElement
	 * @param precedingElement
	 */
	private void placeAfter (EObject newElement, EObject precedingElement) {
		ranks.put(newElement, ranks.get(precedingElement)+1);
	}

	/**
	 * Use when a Move model says that these two elements, from the old and new plans,
	 * have been matched against each other.
	 * @param object
	 * @param exisingElement
	 */
	private void noteEquivalence (EObject object, EObject object2) {
		ranks.put(object, ranks.get(object2));
	}

}
