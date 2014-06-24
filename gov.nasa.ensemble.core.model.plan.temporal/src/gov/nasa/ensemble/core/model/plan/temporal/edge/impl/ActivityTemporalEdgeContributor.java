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
package gov.nasa.ensemble.core.model.plan.temporal.edge.impl;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;

public class ActivityTemporalEdgeContributor extends TemporalEdgeContributor {

	@Override
	public void initialize(final TemporalEdgeManager manager) {
		new PlanVisitor(true) {
			@Override
			protected void visit(EPlanElement element) {
			    if (element instanceof EActivity) {
			    	addActivityEdges(manager, (EActivity)element);
			    }
			}
		}.visitAll(manager.getPlan());
	}
	
	@Override
	public boolean isImportant(TemporalEdgeManager manager, Notification notification) {
		Object feature = notification.getFeature();
		if (TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME == feature
				|| TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION == feature
				|| TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME == feature) {
			return ((TemporalMember)notification.getNotifier()).getPlanElement() instanceof EActivity;
		} else if (PlanPackage.Literals.EPLAN_PARENT__CHILDREN == feature
					|| PlanPackage.Literals.EACTIVITY__CHILDREN == feature) {
			return !EPlanUtils.isTemplatePlanHierarchyElement(notification.getNotifier());
		}
		return false;
	}

	@Override
	public void processNotifications(TemporalEdgeManager manager, List<Notification> notifications) {
		for (Notification notification : notifications) {
			Object feature = notification.getFeature();
			// Temporal changes
			//
			if (TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME == feature
					|| TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION == feature
					|| TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME == feature) {
				EActivity eActivity = (EActivity) ((TemporalMember)notification.getNotifier()).getPlanElement();
				removeActivityEdges(manager, eActivity);
				addActivityEdges(manager, eActivity);
			}
			// Hierarchy changes
			//
			if (PlanPackage.Literals.EPLAN_PARENT__CHILDREN == feature
				 || PlanPackage.Literals.EACTIVITY__CHILDREN == feature) {
				for (EActivity activity : EMFUtils.getAddedObjects(notification, EActivity.class)) {
					addActivityEdges(manager, activity);
				}
				for (EActivity activity : EMFUtils.getRemovedObjects(notification, EActivity.class)) {
					removeActivityEdges(manager, activity);
				}
			}
		}
	}

	private void removeActivityEdges(TemporalEdgeManager manager, EActivity activity) {
		manager.removeTimes(activity);
	}

	private void addActivityEdges(TemporalEdgeManager manager, EActivity activity) {
		TemporalMember member = activity.getMember(TemporalMember.class);
		Date start = member == null ? null : member.getStartTime();
		Date end = member == null ? null : member.getEndTime();
		boolean instantaneous = DateUtils.same(start, end);
		List<Long> list = new ArrayList<Long>();
		if (instantaneous && start != null) {
			list.add(start.getTime());
		} else {
			if (start != null) {
				list.add(start.getTime());
			}
			if (end != null) {
				list.add(end.getTime());
			}
		}
		manager.addTimes(activity, list);
	}
	
}
