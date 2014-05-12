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
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingOrRemovingReference;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode.DiffType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * // @see <a href="https://jplis-ahs-003.jpl.nasa.gov:5843/confluence/x/UgRNAQ">design</a>
 * @author kanef
 *
 */

public class PlanDiffTreeBottomUp extends PlanDiffTree {
	
	public PlanDiffTreeBottomUp(PlanDiffList differences, Comparator<PlanDiffNode> originalOrder) {
		super(differences, originalOrder);
		warnOnDuplicateRegistration = false; // Same activity may appear under multiple changes
	}

	private Map<DiffType, PlanDiffDifftypeNode> topLevelNodes;
	private Map<String, PlanDiffReferenceNode> existingReferenceNodes = new HashMap<String, PlanDiffReferenceNode>();
	
	@Override
	public String getDescriptionForUser () {
		return "Changes";
	}
	
	@Override
	protected PlanDiffObjectNode createNodeDescribing(EActivity object) {
		PlanDiffObjectNode result = register(new PlanDiffObjectNode(object));
		PlanDiffObjectNode parentNode = findOrCreateNodeDescribing(object.getParent());
		if (parentNode != null) parentNode.appendChild(result);
		return result;
	}
	
	
	@Override
	protected PlanDiffObjectNode createNodeDescribing(EActivityGroup object) {
		return register(new PlanDiffObjectNode(object));
	}
	
	@Override
	protected PlanDiffObjectNode createNodeDescribing(EPlan object) {
		return null;
	}
	
	@Override
	protected PlanDiffModificationNode createNodeDescribing(ChangedByModifyingParameter diff) {
		EPlanElement planElement = getPlanElement(diff);
		if (planElement == null || planElement instanceof EPlan) return null; // by design, don't show plan name change, etc.
		if (diff.getParameter()==null) return null;
		PlanDiffModificationNode parentDiff = new PlanDiffModificationNode(diff);
		PlanDiffDifftypeNode grandparentDifftype = findOrCreateTopLevelNode(DiffType.MODIFY);
		PlanDiffNode grandparentParam = findOrCreateNodeDescribing(diff.getParameter());
		PlanDiffObjectNode childOwner = new PlanDiffObjectNode(planElement); // never reuse; do not register
		childOwner.setDiffType(DiffType.MODIFY);
		childOwner.appendChild(parentDiff);
		grandparentParam.appendChild(childOwner);
		grandparentDifftype.appendChild(grandparentParam);
		return parentDiff;
	}
	
	@Override
	protected PlanDiffReferenceNode createNodeDescribing(ChangedByAddingOrRemovingReference diff) {
		EPlanElement planElement = diff.getOwner();
		PlanDiffReferenceNode parentDiff = findOrCreateReferenceNode(diff);
		PlanDiffDifftypeNode grandparentDifftype = findOrCreateTopLevelNode(DiffType.MODIFY);
		PlanDiffObjectNode childOwner = new PlanDiffObjectNode(planElement); // never reuse; do not register
		childOwner.setDiffType(diff.getDiffType());
		parentDiff.appendChild(childOwner);
		grandparentDifftype.appendChild(parentDiff);
		return parentDiff;
	}

	private PlanDiffReferenceNode findOrCreateReferenceNode(
			ChangedByAddingOrRemovingReference diff) {
		String key = diff.getReferenceTypeName()+"&&==&&"+diff.getReferenceName();
		PlanDiffReferenceNode node = existingReferenceNodes.get(key);
		if (node==null) {
			node = new PlanDiffReferenceNode(diff);
			node.setDiffType(DiffType.MODIFY);
			existingReferenceNodes.put(key, node);
		}
		return node;
	}

	@Override
	protected void establishContextInTree(PlanDiffObjectNode node) {
		DiffType diffType = node.getDiffType();
		PlanDiffDifftypeNode parent = findOrCreateTopLevelNode(diffType);
		// Unless it's already listed under its parent (often the case for Unchanged)
		PlanDiffNode existingNode = findNodeDescribing(node.getObject());
		if (existingNode==null || !existingNode.isDescendantOf(parent)) {
			parent.appendChild(node);
		}
	}

	private PlanDiffDifftypeNode findOrCreateTopLevelNode(DiffType diffType) {
		if (topLevelNodes==null) {
			topLevelNodes = new HashMap<DiffType, PlanDiffDifftypeNode>();
		}
		PlanDiffDifftypeNode node = topLevelNodes.get(diffType);
		if (node == null) {
			node = new PlanDiffDifftypeNode(diffType);
			topLevelNodes.put(diffType, node);
			getRoot().appendChild(node);
		}
		return node;
	}

}

