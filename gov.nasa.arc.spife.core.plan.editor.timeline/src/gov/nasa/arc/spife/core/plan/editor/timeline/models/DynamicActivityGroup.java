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
package gov.nasa.arc.spife.core.plan.editor.timeline.models;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.impl.EActivityGroupImpl;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.SafeAdapter;

import java.util.Date;

import javax.measure.unit.SI;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.jscience.physics.amount.Amount;

public class DynamicActivityGroup extends EActivityGroupImpl {
	
	private final EPlan ePlan;
	private final Object value;
	
	private final Adapter activityAdapter = new ActivityAdapter();
	private final Adapter bubbleUpAdapter = new BubbleUpAdapter();
	
	private EList<EPlanChild> activities = new BasicEList<EPlanChild>();
	
	public DynamicActivityGroup(EPlan ePlan, Object value) {
		super();
		this.ePlan = ePlan;
		this.value = value;
		eAdapters().add(activityAdapter);
	}

	/**
	 * Override in order to not change eContainters
	 */
	@Override
	public EList<EPlanChild> getChildren() {
		return activities;
	}

	public Object getValue() {
		return value;
	}
	
	@Override
	public EPlan getParent() {
		return ePlan;
	}

	private class ActivityAdapter extends SafeAdapter {

		@Override
		@SuppressWarnings("unchecked")
		protected void handleNotify(Notification notification) {
			Object notifier = notification.getNotifier();
			Object feature = notification.getFeature();
			if (PlanPackage.Literals.EPLAN_PARENT__CHILDREN == feature) {
				for (EActivity eActivity : EPlanUtils.getActivitiesRemoved(notification)) {
					eActivity.eAdapters().remove(this);
					eActivity.eAdapters().remove(bubbleUpAdapter);
				}
				for (EActivity eActivity : EPlanUtils.getActivitiesAdded(notification)) {
					eActivity.eAdapters().add(this);
					eActivity.eAdapters().add(bubbleUpAdapter);
				}
			} else if (notifier instanceof TemporalMember
						&& ((TemporalMember)notifier).getPlanElement() instanceof EActivity) {
				updateTemporalMember();
			}
		}

		private void updateTemporalMember() {
			TemporalMember temporalMember = getMember(TemporalMember.class);
			Date earliest = null;
			Date latest = null;
			for (EPlanChild eActivity : getChildren()) {
				TemporalMember member = eActivity.getMember(TemporalMember.class);
				Boolean scheduled = member.getScheduled();
				if ((scheduled != null) && !scheduled.booleanValue()) {
					continue; // skip unscheduled items
				}
				Date currentStartTime = member.getStartTime();
				if (currentStartTime != null) {
					if (earliest == null) {
						earliest = currentStartTime;
					} else { 
						earliest = DateUtils.earliest(earliest, currentStartTime);
					}
				}
				Date currentEndTime = member.getEndTime();
				if (latest == null) {
					latest = currentEndTime;
				} else { 
					latest = DateUtils.latest(latest, currentEndTime);
				}
			}
			if (earliest != null) {
				temporalMember.setStartTime(earliest);
			}
			if ((latest != null) && (earliest != null)) {
				temporalMember.setDuration(Amount.valueOf(DateUtils.subtract(latest, earliest), SI.MILLI(SI.SECOND)));
			}
		}
		
	}
	
	private class BubbleUpAdapter extends SafeAdapter {

		@Override
		protected void handleNotify(Notification notification) {
			DynamicActivityGroup.this.eNotify(notification);
		}
		
	}
	
}
