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
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PlanDiffTreeByName extends PlanDiffTreePartiallyTopDown {

	private Map<String, PlanDiffNameNode> existingNameNodes = new HashMap<String, PlanDiffNameNode>();

	public PlanDiffTreeByName(PlanDiffList differences, Comparator<PlanDiffNode> originalOrder) {
		super(differences, originalOrder);
	}

	@Override
	public String getDescriptionForUser() {
		return "by Name";
	}


	@Override
	protected PlanDiffObjectNode createNodeDescribing(EActivity object) {
		return createNodeDescribingObject(object);
	}

	@Override
	protected PlanDiffObjectNode createNodeDescribing(EActivityGroup object) {
		return createNodeDescribingObject(object);
	}

	@Override
	protected PlanDiffModificationNode createNodeDescribing(ChangedByModifyingParameter diff) {
		return super.createNodeDescribing(diff);
	}
	
	@Override
	protected PlanDiffObjectNode createNodeDescribing(EPlan object) {
		return null;
	}
	
	protected PlanDiffObjectNode createNodeDescribingObject(EPlanElement object) {
		PlanDiffObjectNode result = register(new PlanDiffObjectNode(object));
		PlanDiffNameNode parentNode = findOrCreateNameNode(PlanDiffUtils.getObjectName(object));
		parentNode.appendChild(result);
		return register(result);
	}

	private PlanDiffNameNode findOrCreateNameNode(String name) {
		PlanDiffNameNode result = getExistingNodeNames().get(name);
		if (result == null) {
			result = new PlanDiffNameNode(name);
		}
		PlanDiffNode parentNode = getRoot();
		String prefix = prefix(name);
			if (!prefix.equals(name)) {
				parentNode = findOrCreateNameNode(prefix);
			}
		parentNode.appendChild(result);
		return register(result);
	}

	private String prefix(String name) {
		for (int i = 0; i < name.length(); i++) {
			if (",-_; ".indexOf(name.charAt(i)) != -1) {
				return name.substring(0, i);
			}
		}
		return name;
	}

	private PlanDiffNameNode register(PlanDiffNameNode node) {
		if (!getExistingNodeNames().containsKey(node.getName())) {
			getExistingNodeNames().put(node.getName(), node);
		}
		return node;
	}

	private Map<String, PlanDiffNameNode> getExistingNodeNames() {
		if (existingNameNodes==null) {
			existingNameNodes = new HashMap<String, PlanDiffNameNode>();
		}
		return existingNameNodes;
	}

	@Override
	protected void establishContextInTree(PlanDiffObjectNode node) {
		// No-op.  Done at creation time.		
	}


	@Override
	protected PlanDiffObjectNode createNodeDescribingObjectWithParent(EPlanChild object, EPlanElement parent) {
		return createNodeDescribingObjectWithParent(object, nodeForParentOrName(object));
	}
	
	private PlanDiffNode nodeForParentOrName(EPlanChild object) {
		EPlanElement parent = object.getParent();
		PlanDiffNode parentNode = findOrCreateNodeDescribing(parent);
		if (parentNode == null) return findOrCreateNameNode(PlanDiffUtils.getObjectName(object));
		return parentNode;
	}

}
