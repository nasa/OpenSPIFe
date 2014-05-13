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

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Comparator;

import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingNewElement;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByRemovingElement;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode.DiffType;

import org.eclipse.emf.ecore.EObject;


/**
 * // @see <a href="https://jplis-ahs-003.jpl.nasa.gov:5843/confluence/x/UgRNAQ">design</a>
 * @author kanef
 *
 */

public class PlanDiffTreeFlat extends PlanDiffTreePartiallyTopDown {
	/** Clarification from Mel 4:05 8/12/09:
	 * i think i wasn't clear enough in my ui tweaks [SPF-4197]
		i meant for flattened to not display groups
		like flattened does in spife
	 */
	private static boolean hideGroups = true;

	/**
	 * Intended to be called with a chronological or alphabetic comparator,
	 * instead of the usual thing of the original order (a SortCombinedPlan).
	 */
	public PlanDiffTreeFlat(PlanDiffList differences, Comparator<PlanDiffNode> desiredOrder) {
		super(differences, desiredOrder);
	}
	
	@Override
	public String getDescriptionForUser () {
		return "Flat Chronological";
	}

	@Override
	protected PlanDiffObjectNode createNodeDescribing(EActivity object) {
		PlanDiffObjectNode result = register(new PlanDiffObjectNode(object));
		getRoot().appendChild(result);
		return register(result);
	}
	
	@Override
	protected PlanDiffObjectNode createNodeDescribing(EActivityGroup object) {
		if (hideGroups) return null;
		
		PlanDiffObjectNode result = register(new PlanDiffObjectNode(object));
		getRoot().appendChild(result);
		return register(result);
	}
		
	@Override
	protected PlanDiffObjectNode createNodeDescribing(EPlan object) {
		return null;
	}
	
	@Override
	protected void addDeletion (ChangedByRemovingElement diff) {
		// If a group is deleted, then implicitly all the activities under it
		// are also deleted.  Even though we're not supposed to mention groups
		// in this tree, we need to list the implicit deletions.
		// This fixes SPF-4681.
		EObject object = diff.getRemovedElement();
		for (EObject child : object.eContents()) {
			PlanDiffObjectNode node = findOrCreateNodeDescribing(child);
			if (node != null) // no members, just activities
				node.setDiffType(DiffType.REMOVE);
		}
		super.addDeletion(diff);
	}
	
	@Override
	protected void addAddition (ChangedByAddingNewElement diff) {
		// Reverse of addDeletion case.  Fixes unreported bug that is the flip-side of SPF-4681.
		EObject object = diff.getAddedElement();
		for (EObject child : object.eContents()) {
			PlanDiffObjectNode node = findOrCreateNodeDescribing(child);
			if (node != null) // no members, just activities
				node.setDiffType(DiffType.ADD);
		}
		super.addAddition(diff);
	}
	
	@Override
	protected PlanDiffObjectNode createNodeDescribingObjectWithParent(EPlanChild object, EPlanElement parent) {
		return findOrCreateNodeDescribing(object);
	}

	@Override
	protected void establishContextInTree(PlanDiffObjectNode node) {
		// No-op.  No hierarchy of objects in a flat tree.
	}
	
}
