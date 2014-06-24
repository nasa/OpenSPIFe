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
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.core.plan.resources.profile.StructuralFeatureProfile;
import gov.nasa.ensemble.core.plan.resources.util.ResourceUtils;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemLabelProvider;

public class PlanStateResourceDependency extends PlanResourceDependency<EStateResourceDef> {

	private final EObject object;
	
	public PlanStateResourceDependency(EPlan plan, EStateResourceDef resourceDef) {
		this(plan, resourceDef, null);
	}
	
	public PlanStateResourceDependency(EPlan plan, EStateResourceDef resourceDef, EObject object) {
		super(plan, resourceDef);
		this.object = object;
	}

	@Override
	public String getName() {
		if (object != null) {
			EPlan plan = getPlan();
			EStateResourceDef resourceDef = getResourceDef();
			IItemLabelProvider lp = EMFUtils.adapt(object, IItemLabelProvider.class);
			if (lp != null) {
				return plan.getName()+"."+lp.getText(object)+"."+resourceDef.getName();
			}
			return plan.getName()+"."+resourceDef.getName();
		}
		return super.getName();
	}
	
	@Override
	protected Profile findResourceProfile() {
		if (object != null) {
			EPlan plan = getPlan();
			EResourceDef resourceDef = getResourceDef();
			ResourceProfileMember rpm = WrapperUtils.getMember(plan, ResourceProfileMember.class);
			if (rpm != null) {
				for (Profile p : rpm.getResourceProfiles()) {
					if (p instanceof StructuralFeatureProfile
							&& ((StructuralFeatureProfile)p).getObject() == object
							&& ((StructuralFeatureProfile)p).getFeature() == resourceDef) {
						return p;
					}
				}
			}
			return null;
		}
		return super.findResourceProfile();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean update() {
		List<DataPoint<Object>> dataPoints = new ArrayList<DataPoint<Object>>(getPrevious().size());
		List<DataPoint<Object>> oldValue = (List<DataPoint<Object>>) getValue();
		DataPoint<Object> previousDataPoint = null;
		List<Dependency> dataPointDependencies = getDataPointDependencies();
		DataPoint<Object> initialDataPoint = getInitialDataPoint(dataPointDependencies);
		if (initialDataPoint != null) {
			dataPoints.add(initialDataPoint);
			setValue(dataPoints);
			previousDataPoint = initialDataPoint;
		}
		for (Dependency dependency : dataPointDependencies) {
			DataPoint dataPoint = (DataPoint) dependency.getValue();
			Date date = dataPoint.getDate();
			Object value = dataPoint.getValue();
			EActivity contributor = null;
			if (dependency instanceof ActivityDependency) {
				ActivityDependency ad = (ActivityDependency)dependency;
				contributor = ad.getActivity();
			}
			if (previousDataPoint != null 
				&& CommonUtils.equals(previousDataPoint.getDate(), date)
				&& CommonUtils.equals(previousDataPoint.getValue(), value)) {
				previousDataPoint.setValue(value);
				if (contributor != null) {
					List contributors = previousDataPoint.getContributors();
					contributors.clear();
					contributors.add(contributor);
				}
			} else {
				DataPoint newDataPoint = JScienceFactory.eINSTANCE.createEDataPoint(dataPoint.getDate(), dataPoint.getValue());
				if (contributor != null) {
					List contributors = newDataPoint.getContributors();
					contributors.add(contributor);
				}
				dataPoints.add(newDataPoint);
				setValue(dataPoints);
				previousDataPoint = newDataPoint;
			}
		}
		if (!CommonUtils.equals(oldValue, dataPoints)) {
			setValue(dataPoints);
			return true;
		}
		return false;
	}
	
	private DataPoint<Object> getInitialDataPoint(List<Dependency> dataPointDependencies) {
		EStateResourceDef resourceDef = getResourceDef();
		String valueLiteral = resourceDef.getDefaultValueLiteral();
		if (CommonUtils.isNullOrEmpty(valueLiteral)) {
			return null;
		}
		try {
			Object initialValue = ResourceUtils.evaluateStateValue(resourceDef, valueLiteral);
			if (initialValue != null) {
				DataPoint<Object> dataPoint = getInitialDataPoint(this, dataPointDependencies, initialValue);
				return dataPoint;
			}
		} catch (Exception e) {
			LogUtil.error("evaluating initial value '"+valueLiteral+"' for "+resourceDef.getName());
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((object == null) ? 0 : object.hashCode());
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
		PlanStateResourceDependency other = (PlanStateResourceDependency) obj;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}
	
}
