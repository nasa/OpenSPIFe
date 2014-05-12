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
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingNewElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingOrRemovingReference;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByMovingChild;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByRemovingElement;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode.DiffType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EObject;

/**
 * A tree structure that is a layer intermediate between the DiffModel and the GUI.
 * It can be turned into HTML for now, and someday into an Eclipse view
 * that may look very much like the HTML.
 * Its subclasses implement different designs for organizing a tree.
 * By default, this superclass turns selected parts of the diff model
 * into calls to <ul>
 * <li> createNodeDescribing(EActivity)
 * <li> createNodeDescribing(EActivityGroup) 
 * <li> createNodeDescribing(EPlan) 
 * <li> createNodeDescribing("Parameter", name) 
 * <li> createNodeDescribing("Subsystem", name) 
 * </ul>
 * and provides default implementations for them.
 * Subclasses may call <ul>
 * <li> findOrCreateNodeDescribing(EObject object)
 * <li> findNodeDescribing(EObject object)
 * <li> findNodeDescribing("Parameter", name)
 * <li> findNodeDescribing("Subsystem", name)
 * </ul>
 * to create nodes needed to organize the tree the way each subclass wants.
 * @author kanef
 *
 */

public abstract class PlanDiffTree extends AbstractDiffTree {

	protected PlanDiffList differenceInfo;
	protected Comparator<PlanDiffNode> originalOrder;
	
	private Map<Object, PlanDiffNode> existingNodes = new HashMap<Object, PlanDiffNode>();
	private boolean initialized = false;
	protected boolean warnOnDuplicateRegistration = true;

	public PlanDiffTree(PlanDiffList differenceInfo, Comparator<PlanDiffNode> originalOrder) {
		super();
		this.differenceInfo = differenceInfo;
		this.originalOrder = originalOrder;
		root = new PlanDiffRootNode();
	}

	@Override
	protected void init () {
		if (!initialized) {
			initialized = true;
			add(differenceInfo);
			root.restoreOrder(originalOrder);
		}
	}
	
	@Override
	protected void add (PlanDiffList diffList) {
		for (ChangedByAddingNewElement diff : diffList.getAdditions()) addAddition(diff);
		for (ChangedByRemovingElement diff : diffList.getDeletions()) addDeletion(diff);
		for (ChangedByMovingChild diff : diffList.getMoves()) addMove(diff);
		for (ChangedByModifyingParameter diff : diffList.getParameterChanges()) addModification(diff);
		for (ChangedByAddingOrRemovingReference diff : diffList.getReferenceChanges()) addReferenceChange(diff);
		
		if (includeUnchanged()) {
			for (EActivityGroup group : diffList.getUnchangedActivityGroups()) {
				addUnchanged(group);
			}
			for (EActivity activity : diffList.getUnchangedActivities()) {
				addUnchanged(activity);
			}
		}
	}

	private void addUnchanged(EPlanElement planElement) {
		PlanDiffObjectNode node = findOrCreateNodeDescribing(planElement);
		if (node != null) {
			establishContextInTree(node);
		}
	}

	protected void addAddition (ChangedByAddingNewElement diff) {
		PlanDiffObjectNode node = findOrCreateNodeDescribing(diff.getAddedElement());
		if (node != null) {
			node.setDiffType(DiffType.ADD);
			establishContextInTree(node);
			markParentsAsModified(node);
		}
	}

	protected void addDeletion (ChangedByRemovingElement diff) {
		PlanDiffObjectNode node = findOrCreateNodeDescribing(diff.getRemovedElement());
		if (node != null) {
			node.setDiffType(DiffType.REMOVE);
			establishContextInTree(node);
			markParentsAsModified(node);
		}
	}
	
	protected void addModification (ChangedByModifyingParameter diff) {
		createNodeDescribing(diff);
	}
	
	protected void addReferenceChange(ChangedByAddingOrRemovingReference diff) {
		createNodeDescribing(diff);
	}
	
	//protected abstract void establishContextInTree(PlanDiffReferenceNode node);


	protected void addMove (ChangedByMovingChild diff) {
		PlanDiffObjectNode oldNode = findOrCreateNodeDescribing(diff.getOldCopyOfElement());
		PlanDiffObjectNode newNode = findOrCreateNodeDescribing(diff.getMovedElement());
		if (oldNode != null) {
			oldNode.setDiffType(DiffType.MOVE_OUT);
			establishContextInTree(oldNode);
			markParentsAsModified(oldNode);
		}
		if (newNode != null) {
			newNode.setDiffType(DiffType.MOVE_IN);
			establishContextInTree(newNode);
			markParentsAsModified(newNode);
		}
	}

	/** Mark ancestors of this node (but not the node itself) as modified. */
	protected void markParentsAsModified (PlanDiffNode child) {
		PlanDiffNode parent = child.getParent();
		if (parent != null && parent instanceof PlanDiffBadgedNode) {
			PlanDiffBadgedNode badgedParent = (PlanDiffBadgedNode)parent;
			if (badgedParent.getDiffType()==DiffType.UNCHANGED) { // don't override ADDED or DELETED
				badgedParent.setDiffType(DiffType.MODIFY);
				markParentsAsModified(badgedParent);
			}
		}
	}

	
	/**
	 * For top-down design:
	 * When a group is added/deleted (but not modified), show all activities under it.
	 * @param node
	 */
	protected abstract void establishContextInTree(PlanDiffObjectNode node);

	/**
	 * Subclasses can use this to look up or create nodes in the tree.
	 * Examples:<ol>
	 * <li> Top-down tree wants to place an activity under its activity group.  Call with EActivityGroup parent.
	 * <li> Bottom-up tree wants to place a name change under a list of name changes.  Call with "Parameter", "name"
	 * </ol>
	 */
	protected PlanDiffObjectNode findOrCreateNodeDescribing(EObject object) {
		if (existingNodes.containsKey(object))
			return (PlanDiffObjectNode)existingNodes.get(object);
		if (object==null) return null;
		PlanDiffObjectNode result = createNodeDescribing(object);
		existingNodes.put(object, result);
		return result;
	}
	
	protected PlanDiffObjectNode register (PlanDiffObjectNode node) {
		if (warnOnDuplicateRegistration  && existingNodes.containsKey(node.getObject()) && !node.equals(existingNodes.get(node.getObject()))) {
			System.err.println("Duplicate registration of " + node.getObject().getClass().getSimpleName()
					+ " " + (node.getObject() instanceof EPlanElement? ((EPlanElement)node.getObject()).getName() : "")
					+ " #" + node.getObject().hashCode());
		}
		existingNodes.put(node.getObject(), node);
		return node;
	}

	protected boolean registered (EObject object) {
		return existingNodes.containsKey(object);
	}	
		
	protected PlanDiffObjectNode createNodeDescribing(EObject object) {
		PlanDiffObjectNode result = null;
		if (object instanceof EActivity) result = createNodeDescribing((EActivity)object);
		else if (object instanceof EActivityGroup) result = createNodeDescribing((EActivityGroup)object);
		else if (object instanceof EPlan) result = createNodeDescribing((EPlan)object);
		// Note:  Design (https://jplis-ahs-003.jpl.nasa.gov:5843/confluence/x/UgRNAQ) does not call for
		// plan-level parameter changes to be displayed anywhere.
		else if (object instanceof EStructuralFeature) result = createNodeDescribing((EStructuralFeature)object);
		if (result != null) register(result);
		return result;
	}

	abstract protected PlanDiffObjectNode createNodeDescribing(EActivityGroup object);
	abstract protected PlanDiffObjectNode createNodeDescribing(EActivity object);
	abstract protected PlanDiffObjectNode createNodeDescribing(EPlan object);
	abstract protected PlanDiffModificationNode createNodeDescribing(ChangedByModifyingParameter modification);
	abstract protected PlanDiffReferenceNode createNodeDescribing(ChangedByAddingOrRemovingReference diff);
	
	protected PlanDiffParameterNode createNodeDescribing(EStructuralFeature attribute) {
		return new PlanDiffParameterNode(attribute);
	}


	/**
	 * Subclasses can use this to look up or create nodes in the tree.
	 * Examples:<ol>
	 * <li> Top-down tree wants to place an activity under its activity group.  Call with EActivityGroup parent.
	 * <li> Bottom-up tree wants to place a name change under a list of name changes.  Call with "Parameter", "name"
	 * </ol>
	 */
	protected PlanDiffNode findNodeDescribing(EObject object) {
		return existingNodes.get(object);
	}

	/**
	 * Subclasses can use this to look up or create nodes in the tree.
	 * Examples:<ol>
	 * <li> Top-down tree wants to place an activity under its activity group.  Call with EActivityGroup parent.
	 * <li> Bottom-up tree wants to place a name change under a list of name changes.  Call with "Parameter", "name"
	 * </ol>
	 */
	protected PlanDiffNode findNodeDescribing(String type, String name) {
		return existingNodes.get(type + " = " + name);
	}

	public int count (Class objectClass, DiffType type) {
		init();
		int count = 0;
		for (PlanDiffNode node : existingNodes.values()) {
			if (node instanceof PlanDiffObjectNode) {
				PlanDiffObjectNode onode = (PlanDiffObjectNode)node;
				EObject object = onode.getObject();
				if (objectClass.isInstance(object)
						&&
						onode.getDiffType()==type)
					count++;
				}
			}
		return count;
	}
	
	public boolean isEmpty() {
		init();
		for (PlanDiffNode node : existingNodes.values()) {
			if (node instanceof PlanDiffObjectNode) {
				return false;
			}
		}
		return true;
	}
	
	protected EPlanElement getPlanElement(ChangedByModifyingParameter diff) {
		EObject result = diff.getNewCopyOfOwner();
		while (!(result instanceof EPlanElement)) {
			result = result.eContainer();
			if (result==null) return null;
		}
		return (EPlanElement) result;
	}

}
