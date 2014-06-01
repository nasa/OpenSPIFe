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
package gov.nasa.ensemble.core.model.plan.diff.impl;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedConstraintOrProfile;
import gov.nasa.ensemble.core.model.plan.diff.api.OldAndNewCopyOfSameThing;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;

import org.eclipse.emf.ecore.EObject;

public class ChangedConstraintOrProfileImpl implements ChangedConstraintOrProfile {
	
	private OldAndNewCopyOfSameThing oldAndNew;
	private DiffType diffType;
	private EObject object;
	
	public ChangedConstraintOrProfileImpl(OldAndNewCopyOfSameThing oldAndNew, DiffType diffType, EObject object) {
		super();
		this.oldAndNew = oldAndNew;
		this.diffType = diffType;
		this.object = object;
	}
	
	@Override
	public DiffType getDiffType() {
		return diffType;
	}
	@Override
	public EObject getObject() {
		return object;
	}
	
	@Override
	public OldAndNewCopyOfSameThing getOldAndNew() {
		return oldAndNew;
	}
	
	@Override
	public EPlanElement getNewCopyOfOwner() {
		return oldAndNew.getNewCopy();
	}

	@Override
	public EPlanElement getOldCopyOfOwner() {
		return oldAndNew.getOldCopy();
	}
	
	@Override
	public int hashCode () {
		return object.hashCode() + diffType.ordinal();
	}
	
	@Override
	public boolean equals (Object x) {
		if (!(x instanceof ChangedConstraintOrProfile)) return false;
		ChangedConstraintOrProfile other = (ChangedConstraintOrProfile) x;
		return other.getObject() != null && other.getDiffType() != null
			&& PlanDiffUtils.deepEquals(other.getObject(), object)
			&& other.getDiffType().equals(diffType);
	}
	
}
