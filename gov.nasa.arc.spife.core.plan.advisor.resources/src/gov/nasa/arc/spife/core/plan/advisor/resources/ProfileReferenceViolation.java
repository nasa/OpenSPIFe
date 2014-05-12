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
package gov.nasa.arc.spife.core.plan.advisor.resources;

import gov.nasa.arc.spife.core.plan.advisor.resources.preferences.ResourcesPreferences;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.resources.profile.ProfileReference;
import gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.jscience.physics.amount.Amount;

public class ProfileReferenceViolation<T extends ProfileReference> extends Violation {
	
	private EPlanElement owner;
	private T reference;
	private Profile profile;
	private Date start;
	private Date end;
	private String description;
	
	public ProfileReferenceViolation(PlanAdvisor advisor, EPlanElement owner, Profile profile, T reference, String description) {
		this(advisor, owner, profile, reference, owner.getMember(TemporalMember.class).getStartTime(), owner.getMember(TemporalMember.class).getEndTime(), description);
	}
	
	public ProfileReferenceViolation(PlanAdvisor advisor, EPlanElement owner, Profile profile, T reference, Date start, Date end, String description) {
		super(advisor);
		this.owner = owner;
		this.reference = reference;
		this.profile = profile;
		this.start = start;
		this.end = end;
		this.description = description;
	}

	@Override
	public String getType() {
		return "Resource Profile";
	}

	@Override
	public String getMarkerType() {
	    return Activator.PLUGIN_ID + ".resourceprofileviolation";
	}

	@Override
	public String getName() {
		String pretty = getPrettyName();
		if (pretty != null) {
			return pretty;
		}
		if (profile != null) {
			pretty = profile.getName();
			if (pretty != null) {
				return pretty;
			}
			return profile.getId();
		} else {
			return reference.getProfileKey();
		}
	}

	public EPlanElement getOwner() {
		return owner;
	}
	
	@Override
	public List<? extends EPlanElement> getElements() {
		List<EPlanElement> elements = new ArrayList<EPlanElement>(1);
		elements.add(getOwner());
		return elements;
	}

	@Override
	public boolean isCurrentlyViolated() {
		if (isObsolete()) {
			return false;
		}
		return ((ProfileConstraintPlanAdvisor)advisor).isCurrentlyViolated(this);
	}

	@Override
	public boolean isObsolete() {
		if (profile==null) return false;
		if (profile.eResource() == null
				|| reference.eContainer() == null
				|| super.isObsolete()) {
			return true;
		}
		if (!ResourcesPreferences.isFindResourceProfileConstraintViolations()) {
			return true;
		}
		return false;
	}

	public Profile getProfile() {
		return profile;
	}

	@Override
	public Date getTime() {
		return getStart();
	}
	
	public Date getStart() {
		return start;
	}
	
	public Date getEnd() {
		return end;
	}

	public Amount<Duration> getDuration() {
		return DateUtils.subtract(getEnd(), getStart());
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public T getProfileReference() {
		return reference;
	}
	
	private String getPrettyName() {
		String result = null;
		if (profile instanceof StructuralFeatureProfile) {
			StructuralFeatureProfile sfProfile  = (StructuralFeatureProfile) profile;
			EObject object = sfProfile.getObject();
			IItemLabelProvider lp = EMFUtils.adapt(object, IItemLabelProvider.class);
			if (lp != null) {
				result = lp.getText(object);
			} else {
				EStructuralFeature f = object.eClass().getEStructuralFeature("name");
				if (f != null) {
					result = (String) object.eGet(f);
				}
			}
			if (result == null || result.trim().length() == 0) {
				return null;
			}
			result += " " + sfProfile.getFeature().getName();
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((profile == null) ? 0 : profile.hashCode());
		result = prime * result
				+ ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProfileReferenceViolation other = (ProfileReferenceViolation) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (profile == null) {
			if (other.profile != null)
				return false;
		} else if (!profile.equals(other.profile))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

	@Override
	public void dispose() {
		super.dispose();
		this.profile = null;
		this.owner = null;
		this.reference = null;
		this.start = null;
		this.end = null;
		this.description = null;
	}
	
}
