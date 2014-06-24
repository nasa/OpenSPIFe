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
package gov.nasa.ensemble.core.plan.resources.edge;

import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.edge.impl.TemporalEdgeContributor;
import gov.nasa.ensemble.core.model.plan.temporal.edge.impl.TemporalEdgeManager;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;

public class ProfileTemporalEdgeContributor extends TemporalEdgeContributor {

	@Override
	public void initialize(TemporalEdgeManager manager) {
		EPlan plan = manager.getPlan();
		ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		if (member != null) {
			for (Profile p : member.getResourceProfiles()) {
				addProfileEdges(manager, p);
			}
		}
	}

	@Override
	public boolean isImportant(TemporalEdgeManager manager, Notification notification) {
		Object feature = notification.getFeature();
		return JSciencePackage.Literals.PROFILE__DATA_POINTS == feature
				|| ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES == feature;
	}

	@Override
	public void processNotifications(TemporalEdgeManager manager, List<Notification> notifications) {
		Set<Profile> profilesToUpdate = new HashSet<Profile>();
		for (Notification notification : notifications) {
			Object feature = notification.getFeature();
			if (JSciencePackage.Literals.PROFILE__DATA_POINTS == feature) {
				profilesToUpdate.add((Profile) notification.getNotifier());
			} else if (ProfilePackage.Literals.RESOURCE_PROFILE_MEMBER__RESOURCE_PROFILES == feature) {
				profilesToUpdate.addAll(EMFUtils.getAddedObjects(notification, Profile.class));
				profilesToUpdate.addAll(EMFUtils.getRemovedObjects(notification, Profile.class));
			}
		}
		
		for (Profile profile : profilesToUpdate) {
			removeProfileEdges(manager, profile);
			addProfileEdges(manager, profile);
		}
	}
	
	private void removeProfileEdges(TemporalEdgeManager manager, Profile profile) {
		manager.removeTimes(profile);
	}
	
	private void addProfileEdges(TemporalEdgeManager manager, Profile<?> profile) {
		List<Long> list = new ArrayList<Long>();
		DataPoint[] dataPointsCount = new DataPoint[profile.getDataPoints().size()];
		DataPoint[] dataPoints = profile.getDataPoints().toArray(dataPointsCount);
		for (DataPoint pt : dataPoints) {
			Date date = pt.getDate();
			if (date != null) {
				list.add(date.getTime());
			}
		}
		manager.addTimes(profile, list);
	}

}
