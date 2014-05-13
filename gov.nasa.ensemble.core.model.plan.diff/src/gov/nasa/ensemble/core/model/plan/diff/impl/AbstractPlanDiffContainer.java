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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingNewElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingOrRemovingReference;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByMovingChild;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByRemovingElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedConstraintOrProfile;
import gov.nasa.ensemble.core.model.plan.diff.api.OldAndNewCopyOfSameThing;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanChange;
import gov.nasa.ensemble.core.model.plan.diff.api.PlanDiffList;

public abstract class AbstractPlanDiffContainer implements PlanDiffList {

	protected Collection<OldAndNewCopyOfSameThing> commonElements;
	protected Collection<EActivityGroup> unchangedActivityGroups = new ArrayList<EActivityGroup>();
	protected Collection<EActivity> unchangedActivities = new ArrayList<EActivity>();
	protected Collection<ChangedByAddingNewElement> additions = new ArrayList<ChangedByAddingNewElement>();
	protected Collection<ChangedByRemovingElement> deletions = new ArrayList<ChangedByRemovingElement>();
	protected Collection<ChangedByMovingChild> moves = new ArrayList<ChangedByMovingChild>();
	protected Collection<ChangedByModifyingParameter> parameterChanges = new ArrayList<ChangedByModifyingParameter>();
	protected Collection<ChangedByAddingOrRemovingReference> referenceChanges = new ArrayList<ChangedByAddingOrRemovingReference>();
	protected Collection<ChangedConstraintOrProfile> constraintAndProfileChanges = new ArrayList<ChangedConstraintOrProfile>();

	public AbstractPlanDiffContainer() {
		super();
	}

	public Collection<OldAndNewCopyOfSameThing> getCommonElements() {
		return Collections.unmodifiableCollection(commonElements);
	}

	public Collection<ChangedByAddingNewElement> getAdditions() {
		return Collections.unmodifiableCollection(additions);
	}

	public Collection<ChangedByRemovingElement> getDeletions() {
		return Collections.unmodifiableCollection(deletions);
	}

	public Collection<ChangedByModifyingParameter> getParameterChanges() {
		return Collections.unmodifiableCollection(parameterChanges);
	}
	
	public Collection<ChangedByModifyingParameter> getParameterChangesIgnoringCaseAndWhitespaceDifferences() {
		Collection<ChangedByModifyingParameter> changes = new ArrayList(parameterChanges.size());
		for (ChangedByModifyingParameter change : parameterChanges) {
			if (!change.isOnlyChangeToStringCaseAndWhitespace()) {
				changes.add(change);
			}
		}
		return Collections.unmodifiableCollection(changes);
	}

	public Collection<ChangedByMovingChild> getMoves() {
		return Collections.unmodifiableCollection(moves);
	}

	public Collection<ChangedByAddingOrRemovingReference> getReferenceChanges() {
		return Collections.unmodifiableCollection(referenceChanges);
	}

	public Collection<ChangedConstraintOrProfile> getConstraintAndProfileChanges() {
		return Collections.unmodifiableCollection(constraintAndProfileChanges);
	}

	public Collection<EActivity> getUnchangedActivities() {
		return Collections.unmodifiableCollection(unchangedActivities);
	}

	public Collection<EActivityGroup> getUnchangedActivityGroups() {
		return Collections.unmodifiableCollection(unchangedActivityGroups);
	}

	@Override
	public void add(PlanChange change) {
		if (change instanceof ChangedByAddingNewElement) {
			additions.add((ChangedByAddingNewElement) change);
		}
		if (change instanceof ChangedByRemovingElement) {
			deletions.add((ChangedByRemovingElement) change);
		}
		if (change instanceof ChangedByMovingChild) {
			moves.add((ChangedByMovingChild) change);
		}
		if (change instanceof ChangedByModifyingParameter) {
			parameterChanges.add((ChangedByModifyingParameter) change);
		}
		if (change instanceof ChangedByAddingOrRemovingReference) {
			referenceChanges.add((ChangedByAddingOrRemovingReference) change);
		}
		if (change instanceof ChangedConstraintOrProfile) {
			constraintAndProfileChanges.add((ChangedConstraintOrProfile) change);
		}
	}

	@Override
	abstract public EPlanElement findOldCopy(EPlanElement newCopy);
	@Override
	abstract public EPlanElement findNewCopy(EPlanElement oldCopy);
	@Override
	abstract public EPlanElement findOtherCopy(EPlanElement eitherCopy);

}
