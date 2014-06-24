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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;

import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByModifyingParameter;
import gov.nasa.ensemble.core.model.plan.diff.api.OldAndNewCopyOfSameThing;

public class ChangedByModifyingMemberAttribute extends
		AbstractChangedByModifyingParameterImpl implements
		ChangedByModifyingParameter {

	private Class<? extends EMember> memberClass;

	public ChangedByModifyingMemberAttribute(OldAndNewCopyOfSameThing oldAndNew,
			Class<? extends EMember> memberClass, EAttribute parameter) {
		this.parameter = parameter;
		this.memberClass = memberClass;
		this.oldAndNew = oldAndNew;
	}

	@Override
	public EObject getRelevantPartOf(EPlanElement copyOfOwner) {
		return copyOfOwner.getMember(memberClass);
	}

}
