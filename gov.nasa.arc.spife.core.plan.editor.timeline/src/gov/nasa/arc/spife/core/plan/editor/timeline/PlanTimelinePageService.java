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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.emf.transaction.PreCommitListener;
import gov.nasa.ensemble.emf.transaction.RunnableRecordingCommand;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;
import java.util.List;

import javax.measure.unit.NonSI;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.jscience.physics.amount.Amount;

public class PlanTimelinePageService extends PlanTimelineService {

	private ResourceSetListener listener = new Listener();
	private TransactionalEditingDomain domain;

	@Override
	public void setTimeline(PlanTimeline timeline) {
		super.setTimeline(timeline);
		refreshPage();
	}

	@Override
	public void activate() {
		EPlan plan = getTimeline().getPlan();
		domain = TransactionUtils.getDomain(plan);
		domain.addResourceSetListener(listener);
		super.activate();
	}

	@Override
	public void deactivate() {
		if (domain != null) {
			domain.removeResourceSetListener(listener);
		}
		super.deactivate();
	}
	
	private void refreshPage() {
		final TemporalExtent extent = getPlanExtent();
		final Page page = getTimeline().getPage();
		TransactionUtils.writing(page, new Runnable() {
			@Override
			public void run() {
				page.setStartTime(extent.getStart());
				page.setDuration(extent.getDuration());
			}
		});
	}
	
	private TemporalExtent getPlanExtent() {
		EPlan plan = getTimeline().getPlan();
		if (plan == null) {
			return new TemporalExtent(new Date(), Amount.valueOf(1, NonSI.DAY));
		}
		TemporalMember member = plan.getMember(TemporalMember.class);
		if (member == null) {
			return new TemporalExtent(new Date(), Amount.valueOf(1, NonSI.DAY));
		}
		PlanTemporalMember ptm = plan.getMember(PlanTemporalMember.class);
		Date start = member.getStartTime();
		Date startBoundary = ptm == null ? null : ptm.getStartBoundary();
		if (startBoundary != null) {
			// SPF-1157 -- Just use the startBoundary
//			start = DateUtils.earliest(startBoundary, start);
			start = startBoundary;
		}
		Date end = member.getEndTime();
		Date endBoundary = ptm == null ? null : ptm.getEndBoundary();
		if (endBoundary != null) {
			// SPF-1157 -- Just use the endBoundary
//			end =  DateUtils.latest(endBoundary, end);
			end = endBoundary;
		}
		if (start == null) {
			start = new Date();
		}
		if (end == null) {
			end = DateUtils.add(start, Amount.valueOf(1, NonSI.DAY));
		}
		return new TemporalExtent(start, end);
	}

	private class Listener extends PreCommitListener {

		@Override
		public Command transactionAboutToCommit(ResourceSetChangeEvent event) {
			EPlan plan = getTimeline().getPlan();
			List<Notification> notifications = event.getNotifications();
			for (Notification notification : notifications) {
				Object notifier = notification.getNotifier();
				if (notifier instanceof PlanTemporalMember) {
					PlanTemporalMember member = (PlanTemporalMember) notifier;
					if (member.getPlanElement() == plan) {
						Object feature = notification.getFeature();
						if (TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME == feature
								|| TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME == feature
								|| TemporalPackage.Literals.PLAN_TEMPORAL_MEMBER__START_BOUNDARY == feature
								|| TemporalPackage.Literals.PLAN_TEMPORAL_MEMBER__END_BOUNDARY == feature) {
							return new RunnableRecordingCommand("refresh timeline page", new Runnable() {
								@Override
								public void run() {
									refreshPage();
								}
							});
						}
					}
				}
			}
			return null;
		}
		
	}
	
}
