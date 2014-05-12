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
package gov.nasa.ensemble.core.model.plan.diff.test;

import gov.nasa.ensemble.core.model.plan.diff.trees.AbstractDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffDifftypeNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffModificationNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNameNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffObjectNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffReferenceNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode.DiffType;


/** An ad hoc syntax for expressing expected test results from PlanDiff testing.
 * Symbols denote names.
 * <ul>
 * <li> "( )" denotes children.
 * <li> "=" denotes unchanged.
 * <li> "+" denotes something added, e.g. "+A (+x +y)" means Group named A and children named x and y were added.
 * <li> "-" denotes deletion
 * <li> "Δ" (delta) denotes a change, e.g. "ΔB (+x =y)" means B was changed by adding child x. 
 * <li> "“string”" denotes a name-fragment node in the By Name tree.
 * <li> "⤷" and "⤹" (curved arrows) denote a child moved in or out respectively, e.g. "ΔA (=y ⤹x) ΔB (⤷x =z)" means x moved from A to B. 
 * </ul>
 */
public class TersePlanDiffSummaryNotationForTesting {
	
	public static String getSortedNames (AbstractDiffTree tree) {
		String result = "";
		for (PlanDiffNode child : tree.getRoot().getChildren())
			result += getDiffs(child, false) + " ";
		return result.trim();
	}

	public static String getChangesOnly (AbstractDiffTree tree) {
		String result = "";
		for (PlanDiffNode child : tree.getRoot().getChildren())
			if (!child.isUnchanged()) {
				result += getDiffs(child, true) + " ";
			}
		return result.trim();
	}

	private static String getDiffs (PlanDiffNode node, boolean hideUnchanged) {
		String result = "";
		if (node instanceof PlanDiffObjectNode) {
			PlanDiffBadgedNode diff = ((PlanDiffBadgedNode)node);
			DiffType type = diff.getDiffTypeForBadge();
			if (hideUnchanged && type==DiffType.UNCHANGED) return "";
			result = symbolForDiffType(type) + diff.getName();
		}
		else if (node instanceof PlanDiffDifftypeNode) {
			PlanDiffDifftypeNode diff = ((PlanDiffDifftypeNode)node);
			DiffType type = diff.getDiffType();
			if (hideUnchanged && type==DiffType.UNCHANGED) return "";
			result = "[" + symbolForDiffType(type) + "]";
		}
		else if (node instanceof PlanDiffNameNode) {
			PlanDiffNameNode diff = ((PlanDiffNameNode)node);
			result = "“" + diff.getName() + "”";
		}
		else if (node instanceof PlanDiffModificationNode) {
			result = ""; // could do "Δ[" + ((PlanDiffModificationNode)node).getDiff().getParameter().getName() + "]";
			// but that would make tests too hard to read
			// and too dependent on parameter-name details like suggestedStartTime
		}
		else if (node instanceof PlanDiffReferenceNode) {
			PlanDiffReferenceNode nnode = ((PlanDiffReferenceNode)node);
			result = symbolForDiffType(nnode.getDiffType()) + nnode.getName();
		}
		else result = "?" + node.getClass().getSimpleName() + "?";
		String sub = "";
		if (node.hasChildren()) {
			for (PlanDiffNode child : node.getChildren())
				sub += getDiffs(child, hideUnchanged) + " ";
		}
		sub = sub.trim();
		if (sub.length() > 0) {
			result += " (" + sub + ")";
		}
		return result.trim();
	}
	

	private static String symbolForDiffType(DiffType type) {
		if (type==DiffType.ADD) return "+";
		else if (type==DiffType.REMOVE) return "-";
		else if (type==DiffType.MODIFY) return "Δ";
		else if (type==DiffType.UNCHANGED) return "=";
		else if (type==DiffType.MOVE_OUT) return "⤹";
		else if (type==DiffType.MOVE_IN) return "⤷";
		else return "?";
	}

}
