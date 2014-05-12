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
import gov.nasa.ensemble.core.model.plan.diff.trees.PlanDiffBadgedNode.DiffType;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

public class PlanDiffDifftypeNode extends PlanDiffNode {
	
	public DiffType getDiffType() {
		return diffType;
	}

	private DiffType diffType;
	
	private static final String parameterNameSingular = "parameter";
	private static final String parameterNamePlural = "parameters";
	private static final String activityNameSingular = "activity";
	private static final String activityNamePlural = "activities";
//	private static final String activityGroupNameSingular = EnsembleProperties.getProperty("ensemble.plan.activityGroupName").toLowerCase();
//	private static final String activityGroupNamePlural = EnsembleProperties.getProperty("ensemble.plan.activityGroupName.plural").toLowerCase();
	private static final String activityGroupNameSingular = EPlanUtils.getActivityGroupDisplayName().toLowerCase();
	private static final String activityGroupNamePlural = EPlanUtils.getActivityGroupDisplayNamePlural().toLowerCase();

	public PlanDiffDifftypeNode(DiffType diffType) {
		super();
		this.diffType = diffType;
	}

	public String getDescription () {
		if (diffType==DiffType.MODIFY) {
			int parameters = getChildren().size();
			String pWord = parameters==1? parameterNameSingular : parameterNamePlural;
			return "Changed:  " + parameters + " " + pWord;
		}
		String prefix = "Other:  ";
		if (diffType==DiffType.ADD)  prefix = "Added";
		if (diffType==DiffType.REMOVE)  prefix = "Deleted";
		if (diffType==DiffType.MOVE_IN || diffType==DiffType.MOVE_OUT)  prefix = "Moved";
		if (diffType==DiffType.UNCHANGED)  prefix = "Unchanged";
		int groups = this.count(EActivityGroup.class);
		int activities = this.count(EActivity.class);
		String gWord = groups==1? activityGroupNameSingular : activityGroupNamePlural;
		String aWord = activities==1? activityNameSingular : activityNamePlural;
		return prefix + ":  "  + groups + " " + gWord
				+ ", "
				+ activities + " total " + aWord;
	}
	

}
