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
package gov.nasa.ensemble.core.model.plan.diff.report.viewer;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffEngine;
import gov.nasa.ensemble.core.model.plan.diff.trees.AbstractDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.AlphabeticalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.ChronologicalOrder;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffNode;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTree;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeBottomUp;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeByName;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeFlat;
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffTreeTopDown;
import gov.nasa.ensemble.core.model.plan.diff.trees.SortCombinedPlan;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.compare.ResourceNode;
import org.eclipse.compare.structuremergeviewer.ICompareInput;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PlanDiffContentProvider implements ITreeContentProvider {
	
	EPlan leftPlan;
	EPlan rightPlan;
	List<AbstractDiffTree> diffTrees;
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		leftPlan = null;
		rightPlan = null;
		diffTrees = null;
		if (newInput instanceof ICompareInput) {
			ICompareInput input = (ICompareInput) newInput;
			ResourceNode left = (ResourceNode) input.getLeft();
			ResourceNode right = (ResourceNode) input.getRight();
			try {
				leftPlan = EPlanUtils.loadOnePlan(left.getResource());
				rightPlan = EPlanUtils.loadOnePlan(right.getResource());
				diffTrees = computeDiffTrees(leftPlan, rightPlan);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private List<AbstractDiffTree> computeDiffTrees (EPlan plan1, EPlan plan2) {
		PlanDiffList differences = PlanDiffEngine.findChanges(plan1, plan2);
		if (differences == null) {
			throw new IllegalStateException("Sorry, unable to compare plans.");
		} else {
			List<AbstractDiffTree> trees = new ArrayList<AbstractDiffTree>(2);
			Comparator<PlanDiffNode> originalOrder = new SortCombinedPlan(plan1, plan2, differences);
			Comparator<PlanDiffNode> alphabeticalOrder = new AlphabeticalOrder();
			Comparator<PlanDiffNode> chronologicalOrder = new ChronologicalOrder();
			trees.add(new PlanDiffTreeFlat(differences, chronologicalOrder));
			trees.add(new PlanDiffTreeTopDown(differences, originalOrder));
			trees.add(new PlanDiffTreeBottomUp(differences, originalOrder));
			trees.add(new PlanDiffTreeByName(differences, alphabeticalOrder));
			return trees;
		}
	}
	
	public void dispose() {
		// Nothing needed?
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof PlanDiffNode)
			return ((PlanDiffNode)parentElement).getChildren().toArray();
		if (parentElement instanceof PlanDiffTree)
			return ((AbstractDiffTree)parentElement).getRoot().getChildren().toArray();
		return null;
	}

	public Object getParent(Object element) {
		if (element instanceof PlanDiffNode)
			return ((PlanDiffNode)element).getParent();
		return null;
	}

	public boolean hasChildren(Object parentElement) {
		if (parentElement instanceof PlanDiffNode)
			return !((PlanDiffNode)parentElement).getChildren().isEmpty();
		if (parentElement instanceof PlanDiffTree)
			return !((AbstractDiffTree)parentElement).getRoot().getChildren().isEmpty();
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return diffTrees.toArray();
	}
}
