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

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.diff.api.ChangedByAddingOrRemovingReference;
import gov.nasa.ensemble.core.model.plan.diff.top.PlanDiffUtils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

public class ChangedByAddingOrRemovingReferenceImpl implements
		ChangedByAddingOrRemovingReference {
	
	private EActivity newOwner;
	private EActivity oldOwner;
	private ReferenceAndEObject referenceInNewPlan;
	private ReferenceAndEObject referenceInOldPlan;
	private DiffType diffType;

	public ChangedByAddingOrRemovingReferenceImpl(EActivity oldOwner, EActivity newOwner,
			ReferenceAndEObject referenceInOldPlan, ReferenceAndEObject referenceInNewPlan,
			DiffType diffType) {
		this.referenceInOldPlan = referenceInOldPlan;
		this.referenceInNewPlan = referenceInNewPlan;
		this.oldOwner = oldOwner;
		this.newOwner = newOwner;
		this.diffType = diffType;
	}
	
	private ReferenceAndEObject getReference() {
		if (referenceInNewPlan != null) return referenceInNewPlan;
		else return referenceInOldPlan;
	}

	@Override
	public EActivity getOwner() {
		return getNewCopyOfOwner();
	}
	

	@Override
	public EActivity getOldCopyOfOwner() {
		return oldOwner;
	}

	@Override
	public EActivity getNewCopyOfOwner() {
		return newOwner;
	}
	
	@Override
	public String getParameterName() {
		return getReference().getReference().getName();
	}

	@Override
	public String getReferenceName() {
		return PlanDiffUtils.getNameOfReference(getReference());
	}

	@Override
	public String getReferenceTypeName() {
		return getReference().getObject().eClass().getName();
	}

	@Override
	public DiffType getDiffType() {
		return diffType;
	}

	@Override
	public EReference getParameter() {
		return getReference().getReference();
	}

	@Override
	public Object getOldValue() {
		if (referenceInOldPlan==null) {
			return null;
		} else {
			return referenceInOldPlan.getObject();
		}
	}

	@Override
	public Object getNewValue() {
		if (referenceInNewPlan==null) {
			return null;
		} else {
			return referenceInNewPlan.getObject();
		}
	}

	@Override
	public EObject getRelevantPartOf(EPlanElement copyOfOwner) {
		 // FIXME:  may be assuming structural changes are activity-specific
		return copyOfOwner.getData();
	}

}
