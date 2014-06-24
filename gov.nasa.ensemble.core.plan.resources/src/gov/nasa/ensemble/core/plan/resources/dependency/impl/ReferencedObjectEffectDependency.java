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
package gov.nasa.ensemble.core.plan.resources.dependency.impl;

import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.dictionary.EClaimableEffect;
import gov.nasa.ensemble.dictionary.ENumericResourceEffect;
import gov.nasa.ensemble.dictionary.EStateResourceEffect;
import gov.nasa.ensemble.dictionary.Effect;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

public class ReferencedObjectEffectDependency extends ActivityTemporalEffectDependency implements TemporalActivityDependency {
	
	private final EObject object;
	private final EReference reference;
	
	public ReferencedObjectEffectDependency(DependencyMaintenanceSystem dms, EActivity activity, Effect effect, Timepoint timepoint, EReference reference, EObject object) {
		super(dms, activity, effect, timepoint);
		this.object = object;
		this.reference = reference;
	}
	
	@Override
	public String getName() {
		EActivity activity = getActivity();
		Effect effect = getEffect();
		IItemLabelProvider lp = EMFUtils.adapt(object, IItemLabelProvider.class);
		String objectName = "<object>";
		if (lp != null) {
			objectName = lp.getText(object);
		} else {
			EStructuralFeature f = object.eClass().getEStructuralFeature("name");
			if (f != null) {
				objectName = (String) object.eGet(f);
			}
		}
		return activity.getName()+"."+objectName+"."+effect.getDefinition().getName()+"."+getTimepoint();
	}
	
	@Override
	public EObject getObject() {
		return object;
	}

	public EReference getReference() {
		return reference;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean update() {
		if (getEffect() instanceof EStateResourceEffect) {
			return updateStateEffect((EStateResourceEffect) getEffect(), getObject());
		} else if (getEffect() instanceof EClaimableEffect) {
			return updateClaimEffect((EClaimableEffect) getEffect(), object);
		} else if (getEffect() instanceof ENumericResourceEffect) {
			return updateNumericEffect((ENumericResourceEffect) getEffect(), getObject());
		}
		return false;
	}

	@Override
	protected boolean shouldCompute() {
		return containsReference() && super.shouldCompute();
	}

	protected boolean containsReference() {
		Object v = getActivity().getData().eGet(reference);
		if (reference.isMany()) {
			return ((Collection)v).contains(object);
		} else {
			return v == object;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReferencedObjectEffectDependency other = (ReferencedObjectEffectDependency) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}

}
