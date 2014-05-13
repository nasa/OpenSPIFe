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
package gov.nasa.arc.spife.core.plan.resources.editor.timeline.marker;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineService;
import gov.nasa.arc.spife.core.plan.resources.editor.timeline.TimelinePlugin;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.arc.spife.ui.timeline.util.EMFCommandWrapper;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.MemberPackage;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ResourceConditionTimelineService extends PlanTimelineService {
	
	private Listener listener = new Listener();
	
	@Override
	public void activate() {
		super.activate();
		ResourceConditionsMember member = getResourceConditionsMember();
		//
		// Add the listeners
		member.eAdapters().add(listener);
		//
		// Initialize the markers
		final List<Conditions> conditionsList = member.getConditions();
		PlanTimeline timeline = getTimeline();
		TimelineMarkerManager markerManager = CommonUtils.getAdapter(timeline, TimelineMarkerManager.class);
		for (Conditions conditions : conditionsList) {
			ResourceConditionsTimelineMarker marker = new ResourceConditionsTimelineMarker(conditions);
			markerManager.addMarker(marker);
		}
	}

	@Override
	public void deactivate() {
		ResourceConditionsMember member = getResourceConditionsMember();
		member.eAdapters().remove(listener);
		super.deactivate();
	}

	private ResourceConditionsMember getResourceConditionsMember() {
		PlanTimeline timeline = getTimeline();
		EPlan plan = timeline.getPlan();
		ResourceConditionsMember member = plan.getMember(ResourceConditionsMember.class);
		return member;
	}
	
	public class ResourceConditionsTimelineMarker extends TimelineMarker {

		private final ImageDescriptor IMAGE = AbstractUIPlugin.imageDescriptorFromPlugin(TimelinePlugin.PLUGIN_ID, "icons/resource_conditions_marker.png");
		
		private final Conditions conditions;
		private final Adapter adapter = new Adapter();
		
		public ResourceConditionsTimelineMarker(Conditions conditions) {
			super();
			setLineStyle(SWT.LINE_DASHDOTDOT);
			setColor(ColorConstants.blue);
			setImageDescriptor(IMAGE);
			setTemporalExtent(new TemporalExtent(conditions.getTime(), DateUtils.ZERO_DURATION));
			this.conditions = conditions;
			this.conditions.eAdapters().add(adapter);
		}
		
		public Conditions getConditions() {
			return conditions;
		}

		@Override
		public void dispose() {
			super.dispose();
			this.conditions.eAdapters().remove(adapter);
		}

		public PlanTimeline getTimeline() {
			return ResourceConditionTimelineService.this.getTimeline();
		}
		
		@Override
		public boolean isSelectable() {
			return true;
		}
		
		@Override
		public boolean understandsRequest(Request request) {
			if (RequestConstants.REQ_DELETE == request.getType()) {
				return true;
			}
			return super.understandsRequest(request);
		}
		
		@Override
		public Command getCommand(Request request) {
			if (RequestConstants.REQ_DELETE == request.getType()) {
				EditingDomain domain = EMFUtils.getAnyDomain(conditions);
				final org.eclipse.emf.common.command.Command emfCommand = RemoveCommand.create(domain, conditions);
				return new EMFCommandWrapper(domain, emfCommand);
			}
			return super.getCommand(request);
		}
		
		private class Adapter extends AdapterImpl {
			
			@Override
			public void notifyChanged(Notification notification) {
				if (MemberPackage.Literals.CONDITIONS__TIME == notification.getFeature()) {
					Conditions conditions = (Conditions) notification.getNotifier();
					setTemporalExtent(new TemporalExtent(conditions.getTime(), DateUtils.ZERO_DURATION));
				}
			}
			
		}
		
	}
	
	private class Listener extends AdapterImpl {
		
		@Override
		public void notifyChanged(Notification notification) {
			Object feature = notification.getFeature();
			if (MemberPackage.Literals.RESOURCE_CONDITIONS_MEMBER__CONDITIONS == feature) {
				List<Conditions> added = EMFUtils.getAddedObjects(notification, Conditions.class);
				PlanTimeline timeline = getTimeline();
				TimelineMarkerManager markerManager = CommonUtils.getAdapter(timeline, TimelineMarkerManager.class);
				for (Conditions condition : added) {
					markerManager.addMarker(new ResourceConditionsTimelineMarker(condition));
				}
				
				List<Conditions> removed = EMFUtils.getRemovedObjects(notification, Conditions.class);
				for (Conditions condition : removed) {
					Set<TimelineMarker> markers = new HashSet<TimelineMarker>(markerManager.getTimelineMarkers());
					for (TimelineMarker marker : markers) {
						if (marker instanceof ResourceConditionsTimelineMarker
								&& ((ResourceConditionsTimelineMarker)marker).getConditions() == condition) {
							markerManager.removeMarker(marker);
						}
					}
				}
			}
		}
		
	}
	
}
