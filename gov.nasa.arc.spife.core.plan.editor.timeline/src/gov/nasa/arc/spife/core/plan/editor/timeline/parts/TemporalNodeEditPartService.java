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
package gov.nasa.arc.spife.core.plan.editor.timeline.parts;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineService;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;

public class TemporalNodeEditPartService extends PlanTimelineService {

	private Listener listener = new Listener();
	private TransactionalEditingDomain domain;
	
	@Override
	public void activate() {
		PlanTimeline timeline = getTimeline();
		domain = timeline.getEditingDomain();
		if (domain != null) {
			domain.addResourceSetListener(listener);
		}
		super.activate();
	}

	@Override
	public void deactivate() {
		super.deactivate();
		if (domain != null) {
			domain.removeResourceSetListener(listener);
			domain = null;
		}
	}

	public class Listener extends PostCommitListener {

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			Set<EPlanElement> planElements = new HashSet<EPlanElement>();
			for (Notification notification : event.getNotifications()) {
				Object notifier = notification.getNotifier();
				EPlanElement pe = null;
				if (notifier instanceof EPlanElement) {
					pe = (EPlanElement) notifier;
				} else if (notifier instanceof EMember) {
					pe = ((EMember)notifier).getPlanElement();
				} else {
					continue;
				}
				
				//
				// child bound updates
				if (isChildBoundChange(notification)) {
					buildPlanElements(pe, planElements);
				}
			}
			
			final Set<EditPart> parts = new HashSet<EditPart>();
			PlanTimeline timeline = getTimeline();
			List<TimelineViewer> viewers = timeline.getTimelineViewers();
			for (TimelineViewer v : viewers) {
				parts.addAll(v.getEditParts(planElements));
			}
			if (!parts.isEmpty()) {
				WidgetUtils.runInDisplayThread(timeline.getControl(), new Runnable() {
					@Override
					public void run() {
						for (EditPart ep : parts) {
							if (ep instanceof TemporalNodeEditPart) {
								((TemporalNodeEditPart)ep).updateChildBounds();
							}
						}
					}
				});
			}
		}

		private void buildPlanElements(EPlanElement pe, Set<EPlanElement> planElements) {
			if (planElements.add(pe)) {
				if (pe instanceof EPlanChild) {
					buildPlanElements(((EPlanChild)pe).getParent(), planElements);
				}
			}
		}

		private boolean isChildBoundChange(Notification notification) {
			Object notifier = notification.getNotifier();
			Object feature = notification.getFeature();
			return Notification.REMOVE == notification.getEventType()
					|| Notification.REMOVE_MANY == notification.getEventType()
					|| notifier instanceof TemporalMember
					|| PlanPackage.Literals.COMMON_MEMBER__VISIBLE == feature;
		}
		
	}
	
}
