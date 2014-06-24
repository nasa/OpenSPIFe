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
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.OldAndNewCopyOfSameThing;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;

import org.eclipse.emf.ecore.EStructuralFeature;

public abstract class AbstractChangedByModifyingParameterImpl implements
		ChangedByModifyingParameter {

	protected OldAndNewCopyOfSameThing oldAndNew;
	protected EStructuralFeature parameter;

	@Override
	public DiffType getDiffType() {
		return DiffType.MODIFY;
	}
	
	@Override
	public EStructuralFeature getParameter() {
		return parameter;
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
	public Object getOldValue() {
		return getRelevantPartOf(getOldCopyOfOwner()).eGet(parameter);
	}

	@Override
	public Object getNewValue() {
		return getRelevantPartOf(getNewCopyOfOwner()).eGet(parameter);
	}

	@Override
	public boolean isOnlyChangeToStringCaseAndWhitespace() {
		 Object oldValue = getOldValue();
		 Object newValue = getNewValue();
		 if (oldValue instanceof String && newValue instanceof String) {
			 String oldString = (String)oldValue;
			 String newString = (String)newValue;
			 if (oldString.equalsIgnoreCase(newString)) {
				 return true;
			 } else {
				 return collapseWhitespace(oldString).equalsIgnoreCase(collapseWhitespace(newString));
			 }
		 } else {
			 return false;
		 }
	}

	private String collapseWhitespace(String string) {
		return PlanDiffUtils.collapseWhitespace(string);
	}

}
