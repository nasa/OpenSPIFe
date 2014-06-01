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

import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Comparator;

import org.eclipse.emf.ecore.EObject;

public class AlphabeticalOrder implements Comparator<PlanDiffNode> {

	@Override
	public int compare(PlanDiffNode arg1, PlanDiffNode arg2) {
		if (arg1 instanceof PlanDiffReferenceNode && arg2 instanceof PlanDiffReferenceNode)
			return compare((PlanDiffReferenceNode)arg1, (PlanDiffReferenceNode)arg2);
		if (arg1 instanceof PlanDiffObjectNode && arg2 instanceof PlanDiffObjectNode)
			return compare((PlanDiffObjectNode)arg1, (PlanDiffObjectNode)arg2);
		if (arg1 instanceof PlanDiffNameNode && arg2 instanceof PlanDiffNameNode)
			return compare((PlanDiffNameNode)arg1, (PlanDiffNameNode)arg2);
		return 0; // shouldn't happen
	}

	public int compare(PlanDiffNameNode arg1, PlanDiffNameNode arg2) {
		return compare(arg1.getName(), arg2.getName());
	}
	
	private int compare(String name1, String name2) {
		if (name1==name2) return 0; // two nulls (or identical string pointer)
		if (name1==null) return -1;
		if (name2==null) return +1;
		return name1.compareTo(name2);
	}

	public int compare(PlanDiffObjectNode arg1, PlanDiffObjectNode arg2) {
		return compare(arg1.getObject(), arg2.getObject());
	}

	public int compare(PlanDiffReferenceNode arg1, PlanDiffReferenceNode arg2) {
		return compare(arg1.getName(), arg2.getName());
	}

	public int compare(EObject arg1, EObject arg2) {
		if (arg1 instanceof EPlanElement
				&& arg2 instanceof EPlanElement)
		return compare((EPlanElement)arg1, (EPlanElement)arg2);
		else return 0; // not expected to happen
	}
	
	public int compare(EPlanElement arg1, EPlanElement arg2) {
		String name1 = arg1.getName();
		String name2 = arg2.getName();
		int order = name1.compareTo(name2);
		if (order != 0) return order;
		// For objects whose sort keys are equal (e.g. start time),
		// we need to produce a consistent result each time
		// to facilitate automated testing, even if the choice is arbitrary.
		return getTiebreaker(arg1).compareTo(getTiebreaker(arg2));
	}
	
	private String getTiebreaker(EPlanElement element) {
		String diffID = element.getMember(CommonMember.class).getDiffID();
		if (diffID==null) return "?";
		return diffID;
	}

}
