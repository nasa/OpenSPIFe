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
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByMovingChild;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByRemovingElement;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode.DiffType;

import java.util.Comparator;

/**
 * // @see <a href="https://jplis-ahs-003.jpl.nasa.gov:5843/confluence/x/UgRNAQ">design</a>
 * @author kanef
 *
 */

public class PlanDiffTreeTopDown extends PlanDiffTreePartiallyTopDown {
	
	public PlanDiffTreeTopDown(PlanDiffList differences, Comparator<PlanDiffNode> originalOrder) {
		super(differences, originalOrder);
	}

	/** Design calls for top level to be the Activity Groups.*/
	static boolean hidePlanTopLevel = true;
	
	@Override
	public String getDescriptionForUser () {
		return "Plan Hierarchy";
	}

	@Override
	protected PlanDiffObjectNode createNodeDescribingObjectWithParent(EPlanChild object, EPlanElement parent) {
		return createNodeDescribingObjectWithParent(object, nodeForSelfOrRoot(parent));
	}
	
	private PlanDiffNode parentOrRoot(EPlanChild object) {
		return nodeForSelfOrRoot(object.getParent());
	}
	
	private PlanDiffNode nodeForSelfOrRoot(EPlanElement parent) {
		if (hidePlanTopLevel && parent instanceof EPlan) return getRoot();
		PlanDiffNode parentNode = findOrCreateNodeDescribing(parent);
		if (parentNode == null) return getRoot();
		return parentNode;
	}

	@Override
	protected PlanDiffObjectNode createNodeDescribing(EActivity object) {
		return createNodeDescribingObjectWithParent(object, parentOrRoot(object));
	}
	
	@Override
	protected PlanDiffObjectNode createNodeDescribing(EActivityGroup object) {
		return createNodeDescribingObjectWithParent(object, parentOrRoot(object));
	}
	
	@Override
	protected PlanDiffObjectNode createNodeDescribing(EPlan object) {
		if (hidePlanTopLevel)  return null;
		
		PlanDiffObjectNode result = register(new PlanDiffObjectNode(object));
		getRoot().appendChild(result);
		return register(result);
		}
	
	@Override
	protected void addDeletion(ChangedByRemovingElement diff) {
		// For an activity deletion, the top-down tree has to get the parent group
		// from a the diff model, not the plan.  Otherwise, if a group X has a child
		// added or modified and other deleted, X will appear to be listed twice
		// (see SPF-4047).  One turns out to be the object from the new plan (the
		// parent of the added or modified activity) and the other the object from
		// the old plan (the parent of the deleted activity).
		EPlanChild object = diff.getRemovedElement();
		if (object instanceof EActivity) {
			createNodeDescribingObjectWithParent(object, diff.getParent());
			// Then proceed, using the node just created under the correct parent.
		}
		super.addDeletion(diff);
	}
	
	@Override
	public void addMove (ChangedByMovingChild diff) {
		PlanDiffObjectNode oldParentNode = findOrCreateNodeDescribing(diff.getOldParent());
		PlanDiffObjectNode newParentNode = findOrCreateNodeDescribing(diff.getNewParent());
		PlanDiffObjectNode movedOutNode =
			createNodeDescribingObjectWithParent(diff.getOldCopyOfElement(), oldParentNode);
		PlanDiffObjectNode movedInNode =
			createNodeDescribingObjectWithParent(diff.getMovedElement(), newParentNode);
		if (movedOutNode != null) {
			movedOutNode.setDiffType(DiffType.MOVE_OUT);
			establishContextInTree(movedOutNode);
			markParentsAsModified(movedOutNode);
		}
		if (movedInNode != null) {
			movedInNode.setDiffType(DiffType.MOVE_IN);
			establishContextInTree(movedInNode);
			markParentsAsModified(movedInNode);
		}
	}
	
	@Override
	/**
	 * For top-down design:
	 * When a group is added/deleted (but not modified), show all activities under it.
	 * This does not apply to subactivities or parameters or other types of children.
	 * @param node
	 * Deleted as part of SPF-4760.
	 */
	protected void establishContextInTree(final PlanDiffObjectNode node) {
//		EObject object = node.getObject();
//		if (object instanceof EActivityGroup) {
//			EActivityGroup group = (EActivityGroup)object;
//			new PlanVisitor() {
//				@Override
//				protected void visit(EPlanElement element) {
//				    if (element instanceof EActivity) {
//						EActivity activity = (EActivity) element;
//						if (!registered(activity)) {
//							PlanDiffObjectNode childNode = register(createNodeDescribing(activity));
//							childNode.setDiffType(node.getDiffType());
//						}
//				    	
//				    }
//				}
//			}.visitAll(group);
//		}
	}



}
