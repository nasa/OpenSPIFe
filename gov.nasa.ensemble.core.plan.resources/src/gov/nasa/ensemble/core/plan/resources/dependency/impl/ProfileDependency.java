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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.util.EList;

public abstract class ProfileDependency extends DependencyImpl {

	protected static final Profile NO_PROFILE = JSCIENCE_FACTORY.createProfile();

	private final EPlan plan;
	private Profile resourceProfile = null;

	public ProfileDependency(EPlan plan) {
		super();
		this.plan = plan;
	}
	
	public EPlan getPlan() {
		return plan;
	}

	@Override
	public String getName() {
		Profile profile = getResourceProfile();
		String name = (profile == null || profile == NO_PROFILE) ? "UNKNOWN" : profile.getId();
		return getPlan().getName()+"."+name+".profile";
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		Profile profile = getResourceProfile();
		if (profile != null) {
			profile.setValid(false);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void applyValue() {
		Profile profile = getResourceProfile();
		if(profile == null) {
			LogUtil.warnOnce("profile is null: "+getName());
		} else {
			EList oldDataPoints = profile.getDataPoints();
			Collection<DataPoint<?>> dataPoints = (Collection<DataPoint<?>>)getValue();
			if (!CommonUtils.equals(oldDataPoints, dataPoints)) {
				profile.setInterpolation(INTERPOLATION.STEP);
				Collection value = dataPoints == null ? Collections.EMPTY_LIST : dataPoints;
				profile.setDataPoints(value);
			}
			profile.setValid(true);
		}
		super.applyValue();
	}
	
	protected List<Dependency> getDataPointDependencies() {
		List<Dependency> dependencies = new ArrayList<Dependency>();
		for (Dependency dependency : getPrevious()) {
			Object dependencyValue = dependency.getValue();
			if (dependencyValue instanceof DataPoint) {
				if (dependency instanceof TemporalActivityDependency) {
					TemporalActivityDependency pdpd = (TemporalActivityDependency)dependency;
					if (shouldCompute(pdpd)) {
						dependencies.add(pdpd);
					}
				} else {
					dependencies.add(dependency);
				}
			}
		}
		Collections.sort(dependencies, DataPointContributorComparator.INSTANCE);
		return dependencies;
	}
	
	@SuppressWarnings("unchecked")
	protected boolean shouldCompute(TemporalActivityDependency dependency) {
		if (dependency.getValue() == null) {
			return false;
		}
		
		if(dependency.getDate() == null) {
			LogUtil.warnOnce("Dependency has null value: " + dependency);
			return false;
		}
		
		if (Timepoint.START == dependency.getTimepoint()) {
			return true;
		}
		
		return ResourceUtils.hasUpdatedDuration(dependency);
	}

	protected <T> DataPoint<T> getInitialDataPoint(PlanResourceDependency d, List<Dependency> dataPointDependencies, T initialValue) {
		if (initialValue == null) {
			return null;
		}
		TemporalMember m = d.getPlan().getMember(TemporalMember.class, true);
		Date startDate = m.getStartTime();
		if (!dataPointDependencies.isEmpty()) {
			Dependency firstNode = dataPointDependencies.get(0);
			DataPoint firstDataPoint = (DataPoint)firstNode.getValue();
			Date firstDataPointDate = firstDataPoint.getDate();
			if (firstDataPointDate != null) {
				firstDataPointDate = DateUtils.subtract(firstDataPointDate, 1);
				startDate = DateUtils.earliest(startDate, firstDataPointDate);
			}
		}
		if (startDate != null) {
			return JSCIENCE_FACTORY.createEDataPoint(startDate, initialValue);
		}
		return null;
	}

	public Profile getResourceProfile() {
		if (resourceProfile == NO_PROFILE) {
			return null;
		}
		
		if (resourceProfile == null) {
			resourceProfile = findResourceProfile();
			if (resourceProfile == null) {
				resourceProfile = NO_PROFILE;
				return null;
			}
		}
		return resourceProfile;
	}

	protected abstract Profile findResourceProfile();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((plan == null) ? 0 : plan.hashCode());
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
		ProfileDependency other = (ProfileDependency) obj;
		if (plan == null) {
			if (other.plan != null)
				return false;
		} else if (!plan.equals(other.plan))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getName();
	}

}
