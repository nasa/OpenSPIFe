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

import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingOrRemovingReference;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode.DiffType;

import java.util.Comparator;

import org.eclipse.emf.ecore.EObject;

/**
 * Everything except bottom-up (and raw) tree are organized top-down
 * once you get past the first level or two of organization.
 * They all add children under parents the same way, and so
 * can share some code.
 * @author kanef
 *
 */
public abstract class PlanDiffTreePartiallyTopDown extends PlanDiffTree {

	public PlanDiffTreePartiallyTopDown(PlanDiffList differences,
			Comparator<PlanDiffNode> originalOrder) {
		super(differences, originalOrder);
	}

	@Override
	protected PlanDiffModificationNode createNodeDescribing(ChangedByModifyingParameter diff) {
		PlanDiffModificationNode node = new PlanDiffModificationNode(diff);
		PlanDiffNode parent = findOrCreateNodeDescribing(getPlanElement(diff));
		if (parent instanceof PlanDiffObjectNode)
			((PlanDiffObjectNode)parent).setDiffType(DiffType.MODIFY);
		if (parent != null) {
			parent.appendChild(node);
			markParentsAsModified(parent);
		}
		return node;
	}

	@Override
	protected PlanDiffReferenceNode createNodeDescribing(ChangedByAddingOrRemovingReference object) {
		PlanDiffReferenceNode result = new PlanDiffReferenceNode(object);
		PlanDiffNode parentNode = findOrCreateNodeDescribing(object.getOwner());
		if (parentNode != null) {
			parentNode.appendChild(result);
			markParentsAsModified(result);
		}
		return result;
	}

	abstract protected PlanDiffObjectNode createNodeDescribingObjectWithParent(EPlanChild object, EPlanElement parent);

	protected PlanDiffObjectNode createNodeDescribingObjectWithParent(EObject object,
			PlanDiffNode parentNode) {
				PlanDiffObjectNode result = register(new PlanDiffObjectNode(object));
				if (parentNode != null) parentNode.appendChild(result);
				return register(result);
			}

}
