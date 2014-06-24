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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

abstract public class PlanDiffNode {

	private List<PlanDiffNode> children = new ArrayList<PlanDiffNode>();
	protected PlanDiffNode parent = null;

	public List<PlanDiffNode> getChildren() {
		return children;
	}
	
	public boolean hasChildren() {
		return children != null && !children.isEmpty();
	}
	
	public void appendChild(PlanDiffNode newKid) {
		if (!children.contains(newKid)) {
			children.add(newKid);
			newKid.setParent(this);
		}
	}
	
	public boolean mentionedAbove (Object object) {
		if (mentionedHere(object)) return true;
		if (parent==null) return false;
		return parent.mentionedAbove(object);		
	}
	
	public void restoreOrder(Comparator<PlanDiffNode> originalOrder) {
		Collections.sort(children, originalOrder);
		for (PlanDiffNode child : children) child.restoreOrder(originalOrder);
	}
	
	public PlanDiffNode getParent() {
		return parent;
	}

	public void setParent(PlanDiffNode parent) {
		this.parent = parent;
	}

	public boolean mentionedHere (Object object) {
		return false;
	}
	
	public int count (Class objectClass) {
		int count = 0;
		for (PlanDiffNode node : children) {
			count += node.count(objectClass);
			}
		return count;
	}

	public String getCSSclass() {
		return "PlanDiffNode";
	}

	public boolean isUnscheduled() {
		if (children != null && children.size() > 0) {
			boolean allUnscheduled = true;
			for (PlanDiffNode child : children) {
				allUnscheduled = allUnscheduled && child.isUnscheduled();
			}
			return allUnscheduled;
		}
		else return false;
	}
	
	public boolean isUnchanged() {
		if (children != null && children.size() > 0) {
			boolean allUnchanged = true;
			for (PlanDiffNode child : children) {
				allUnchanged = allUnchanged && child.isUnchanged();
			}
			return allUnchanged;
		}
		else return false;
	}
	
	public boolean isDeleted() {
		if (children != null && children.size() > 0) {
			boolean allDeleted = true;
			for (PlanDiffNode child : children) {
				allDeleted = allDeleted && child.isDeleted();
			}
			return allDeleted;
		}
		else return false;
	}

	public boolean isModified() {
		if (children != null && children.size() > 0) {
			boolean allModified = true;
			for (PlanDiffNode child : children) {
				allModified = allModified && child.isModified();
			}
			return allModified;
		}
		else return false;
	}

	public boolean isDescendantOf(PlanDiffNode ancestor) {
		if (parent==null) return false;
		if (parent.equals(ancestor)) return true;
		return parent.isDescendantOf(ancestor);
	}

}
