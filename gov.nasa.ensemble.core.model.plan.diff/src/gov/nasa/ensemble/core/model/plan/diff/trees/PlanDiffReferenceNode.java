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

import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingOrRemovingReference;

public class PlanDiffReferenceNode extends PlanDiffBadgedNode {
	
	private ChangedByAddingOrRemovingReference diff;
	
	public PlanDiffReferenceNode(ChangedByAddingOrRemovingReference diff) {
		this.diff = diff; 
		this.name = diff.getReferenceName();
		setDiffType(diff.getDiffType());
	}
	
	@Override
	public String getName() {
		// + (getDiffType().equals(DiffType.REMOVE)? " is no longer needed" : " is now needed");
		return name;
	}
	
	@Override
	public String getCSSclass() {
		return diff.getReferenceTypeName() + " Reference";
	}
	
	@Override
	public String getCSSclassForIcon() {
		return diff.getReferenceTypeName() + "-icon Reference-icon";
	}


	@Override
	public boolean isDeleted() {
		// Removing the use of a resource is not considered a deletion.
		// per design in SPF-4458, 1/27/10.
		return false;
	}
	
	@Override
	public DiffType getDiffType() {
		// Adding and removing the use of a resource is considered a "modification".
		// for all purposes except the badge displayed.
		// per design in SPF-4458, 1/27/10.
		return diffType;
	}

	@Override
	public DiffType getDiffTypeForBadge() {
		return super.getDiffType();
	}


}
