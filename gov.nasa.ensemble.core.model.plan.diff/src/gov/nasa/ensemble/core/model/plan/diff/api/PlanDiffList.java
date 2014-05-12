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
package gov.nasa.ensemble.core.model.plan.diff.api;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.Collection;

/**
 * A list of all changes of the plan.
 * For EMF Diff fans, this is the equivalent of DiffModel.
 * @author kanef
 */

public interface PlanDiffList {
	
	Collection<ChangedByAddingNewElement> getAdditions();
	
	Collection<ChangedByRemovingElement> getDeletions();
	
	Collection<ChangedByMovingChild> getMoves();
	
	Collection<ChangedByModifyingParameter> getParameterChanges();
	
	Collection<ChangedByModifyingParameter> getParameterChangesIgnoringCaseAndWhitespaceDifferences();
	
	Collection<ChangedByAddingOrRemovingReference> getReferenceChanges();

	Collection<ChangedConstraintOrProfile> getConstraintAndProfileChanges();
	
	/**
	 * Gets the elements that exist in both the old and new plan,
	 * i.e. all activities and groups that were neither added nor deleted.
	 * @return Pairs of <oldVersion, newVersion>
	 */
	Collection<OldAndNewCopyOfSameThing> getCommonElements();
	
	/**
	 * Gets the elements that exist in both plans and were not modified.
	 * @return
	 */
	
	Collection<EActivityGroup> getUnchangedActivityGroups();
	Collection<EActivity> getUnchangedActivities();

	EPlanElement findOldCopy(EPlanElement newCopy);
	EPlanElement findNewCopy(EPlanElement oldCopy);
	EPlanElement findOtherCopy(EPlanElement eitherCopy);
	
	abstract public void add(PlanChange change);

}
