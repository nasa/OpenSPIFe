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

import gov.nasa.ensemble.core.model.plan.diff.api.PlanChange;


public abstract class PlanDiffBadgedNode extends PlanDiffNode {

	protected String name;

	public enum DiffType {ADD, REMOVE, MODIFY, UNCHANGED, MOVE_OUT, MOVE_IN}

	protected DiffType diffType;

	public PlanDiffBadgedNode() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setDiffType(DiffType diffType) {
		this.diffType = diffType;		
	}

	public void setDiffType(PlanChange.DiffType diffType) {
		switch (diffType) {
		case ADD: this.diffType = DiffType.ADD; break;
		case REMOVE: this.diffType = DiffType.REMOVE; break;
		case MODIFY: this.diffType = DiffType.MODIFY; break;
		case UNCHANGED: this.diffType = DiffType.UNCHANGED; break;
		case MOVE: this.diffType = DiffType.MOVE_IN; break; // Special code will set explicitly
		}
	}
	
	public DiffType getDiffType() {
		return diffType;		
	}

	public DiffType getDiffTypeForBadge() {
		return getDiffType();
	}

	public String getCSSclassForIcon() {
		return getCSSclass();
	}
	
	public String getCSSclassForBadge() {
		switch (this.diffType) {
		case ADD:  return "ADD";
		case REMOVE:  return "REMOVE";
		case MODIFY:  return "MODIFY";
		case UNCHANGED:  return "UNCHANGED";
		case MOVE_IN:  return "MOVEIN";
		case MOVE_OUT:  return "MOVEOUT";
		}
		return "UNKNOWN";
	}

}
