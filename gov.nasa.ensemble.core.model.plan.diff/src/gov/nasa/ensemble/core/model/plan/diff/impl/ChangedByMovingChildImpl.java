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

import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.EPlanParent;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByMovingChild;
import gov.nasa.ensemble.core.model.plan.diff.api.OldAndNewCopyOfSameThing;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;

public class ChangedByMovingChildImpl implements ChangedByMovingChild {

	private OldAndNewCopyOfSameThing oldAndNew;
	private PlanDiffList diffInfo;

	public ChangedByMovingChildImpl(OldAndNewCopyOfSameThing oldAndNew,
			PlanDiffListImpl diffInfo) {
		this.oldAndNew = oldAndNew;
		this.diffInfo = diffInfo;
	}

	@Override
	public EPlanChild getMovedElement() {
		return (EPlanChild) oldAndNew.getNewCopy();
	}

	@Override
	public EPlanParent getNewParent() {
		return (EPlanParent) getMovedElement().getParent();
	}

	@Override
	public EPlanChild getOldCopyOfElement() {
		return (EPlanChild) oldAndNew.getOldCopy();
	}

	@Override
	public EPlanParent getOldParent() {
		EPlanElement oldCopyOfParent = getOldCopyOfElement().getParent();
		EPlanElement newCopyOfParent = diffInfo.findNewCopy(oldCopyOfParent);
		if (newCopyOfParent == null) {
			return (EPlanParent) oldCopyOfParent;
		} else {
			return (EPlanParent) newCopyOfParent;
		}
	}

	@Override
	public DiffType getDiffType() {
		return DiffType.MOVE;
	}

}
