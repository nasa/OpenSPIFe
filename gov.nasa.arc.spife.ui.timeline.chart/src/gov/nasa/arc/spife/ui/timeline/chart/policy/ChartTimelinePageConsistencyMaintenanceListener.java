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
package gov.nasa.arc.spife.ui.timeline.chart.policy;

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.chart.ChartEditor;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.emf.transaction.IConsistencyMaintenanceListener;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class ChartTimelinePageConsistencyMaintenanceListener implements IConsistencyMaintenanceListener {
	
	private static final long MILLI_SECOND_DURATION = 1;
	private static final long SECOND_DURATION = 1000 * MILLI_SECOND_DURATION;
	private static final long MINUTE_DURATION = 60 * SECOND_DURATION;
	private static final long HOUR_DURATION = 60 * MINUTE_DURATION;
	private static final long DAY_DURATION = 24 * HOUR_DURATION;
	private static final long WEEK_DURATION = 7 * DAY_DURATION;	
	
	@Override
	public Command createConsistencyMaintenanceCommand(ResourceSetChangeEvent event) {
		// SPF-5020 don't do anything unless we're in a chart editor
		if (!PlatformUI.isWorkbenchRunning()) {
			return null;
		}
		IEditorPart currentEditorPart = EditorPartUtils.getCurrent();
		if(currentEditorPart != null && !(currentEditorPart instanceof ChartEditor)) {
			return null;
		}
		
		Page page = null;
		Set<Plot> addedPlots = new HashSet<Plot>();
		for (Notification notification : event.getNotifications()) {
			Object feature = notification.getFeature();
			if (ChartPackage.Literals.CHART__PLOTS == feature) {
				page = getTimelinePage(notification);
				List<Plot> addedObjects = EMFUtils.getAddedObjects(notification, Plot.class);
				addedPlots.addAll(addedObjects);
			} else if (TimelinePackage.Literals.ETIMELINE__CONTENTS == feature) {
				page = getTimelinePage(notification);
				for (Chart chart : EMFUtils.getAddedObjects(notification, Chart.class)) {
					addedPlots.addAll(chart.getPlots());
				}
			}
		}
		
		if (page == null) {
			return null;
		}
		
		boolean changed = false;
		TemporalExtent extent = page.getExtent();
		TemporalExtent newExtent = null;
		for (Plot plot : addedPlots) {
			TemporalExtent profileExtent = ProfileUtil.getTemporalExtent(plot.getProfile());
			if (newExtent == null) {
				newExtent = profileExtent;
			} else {
				extent = extent.setStart(DateUtils.earliest(extent.getStart(), newExtent.getStart()));
				extent = extent.setEnd(DateUtils.latest(extent.getEnd(), newExtent.getEnd()));
			}
		}
		
		if (newExtent != null) {
			extent = extent.setStart(DateUtils.earliest(extent.getStart(), newExtent.getStart()));
			extent = extent.setEnd(DateUtils.latest(extent.getEnd(), newExtent.getEnd()));
			changed = true;
		}
		
		if (changed) {
			final Date start = extent.getStart();
			final long duration = extent.getDurationMillis();
			// SPF-5020 only freezing in windows, so check with the user if they want to proceed
			boolean isWindows = System.getProperty("os.name").startsWith("Windows");
			boolean okToProceed = true;
			if(isWindows) {
				okToProceed = false;
			}
			if(isWindows && duration > WEEK_DURATION * 13) {
				if(currentEditorPart != null) {
					okToProceed = MessageDialog.openQuestion(currentEditorPart.getEditorSite().getShell(), "Update bounds?"
							, "Warning: Updating the timeline bounds to match this chart may cause your system to become " +
									"unresponsive and you may lose any unsaved work. " +
									"Are you sure you want to proceed?");
				}				
			}
			if(okToProceed) {
				CompoundCommand command = new CompoundCommand(Arrays.asList(new Command[] {
					SetCommand.create(event.getEditingDomain(), page, TimelinePackage.Literals.PAGE__START_TIME, start.getTime()),
					SetCommand.create(event.getEditingDomain(), page, TimelinePackage.Literals.PAGE__DURATION, duration)
				}));
				return command;
			}
		}
		return null;
	}
	
	public Page getTimelinePage(Notification notification) {
		Object notifier = notification.getNotifier();
		while (notifier instanceof EObject) {
			if (notifier instanceof ETimeline) {
				return ((ETimeline)notifier).getPage();
			}
			notifier = ((EObject)notifier).eContainer();
		}
		return null;
	}
	
	
}
