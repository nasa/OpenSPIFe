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
package gov.nasa.ensemble.core.plan.advisor.markers;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.advisor.Advice;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.core.plan.advisor.fixing.IPlanMarkerResolution;
import gov.nasa.ensemble.core.plan.advisor.fixing.ViolationFixes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.NotificationImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMarkerHelpRegistry;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.dialogs.MarkerResolutionSelectionDialog;
import org.eclipse.ui.ide.IDE;

public class MarkerPlanAdvisor extends PlanAdvisor implements IResourceChangeListener {

	private final IProject project;
	private final EPlan plan;
	private MarkerFilter filter;
	
	public MarkerPlanAdvisor(PlanAdvisorMember planAdvisorMember) {
		super("Markers", planAdvisorMember);
		plan = planAdvisorMember.getPlan();
		IResource resource = (IResource) plan.getAdapter(IResource.class);
		project = resource == null ? null : resource.getProject();
		try {
			filter = MissionExtender.construct(MarkerFilter.class);
		} catch (ConstructionException e) {
			// no filter
		}
	}

	public EPlan getPlan() {
		return plan;
	}
	
	@Override
	public void dispose() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(this);
	}

	@Override
	protected List<? extends Advice> initialize() {
		if (project == null || !project.exists()) {
			return Collections.emptyList();
		}
		// add resource listener to resource
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(this);

		// find existing markers and convert to violations
		List<MarkerViolation> violations = new ArrayList<MarkerViolation>();
		try {
			IMarker[] markers = project.findMarkers(null, true, IResource.DEPTH_INFINITE);
			for (IMarker iMarker : markers) {
				boolean isTransient = iMarker.getAttribute(IMarker.TRANSIENT, false);
				if(!isTransient && filter(iMarker)) {
					MarkerViolation markerViolation = new MarkerViolation(this, iMarker);
					violations.add(markerViolation);
				}
			}
		} catch (CoreException e) {
			LogUtil.error(e);
		}

		return violations;
	}

	private boolean filter(IMarker marker) {
		boolean result = true;
		if(filter != null) {
			result = filter.accept(marker, plan);
		}
		
		try {
			result = result && marker.isSubtypeOf(IMarker.PROBLEM);
		} catch (CoreException e) {
			LogUtil.error(e);
		}
		return result;
	}

	@Override
	protected boolean affectsViolations(Notification notification) {
		return (notification instanceof MarkerNotification);
	}

	@Override
	protected List<? extends Advice> check(List<Notification> notifications) {
		if (notifications.size() == 1) {
			Notification notification = notifications.get(0);
			if (notification instanceof MarkerNotification) {
				return (List<Violation>)notification.getNewValue();
			}
			return Collections.emptyList();
		}
		LinkedHashSet<Violation> violations = new LinkedHashSet<Violation>();
		for (Notification notification : notifications) {
			if (notification instanceof MarkerNotification) {
				List<Violation> list = (List<Violation>)notification.getNewValue();
				violations.addAll(list);
			}
		}
		return new ArrayList<Advice>(violations);
	}
	
	@Override
	public ViolationFixes fixViolations(ISelection selection) {
		ViolationFixes fixes = null;
		IMarkerHelpRegistry registry = IDE.getMarkerHelpRegistry();
		if (selection instanceof IStructuredSelection && !selection.isEmpty()) {
			List list = ((IStructuredSelection)selection).toList();
			for (Object object : list) {
				if (object instanceof ViolationTracker) {
					Violation violation = ((ViolationTracker)object).getViolation();
					if (violation instanceof MarkerViolation && violation.isCurrentlyViolated()) {
						fixMarkerViolation(registry, (MarkerViolation)violation, true);
					}
				}
			}
		} else {
			// try to fix all marker violations
			for (ViolationTracker tracker : planAdvisorMember.getViolationTrackers()) {
				Violation violation = tracker.getViolation();
				if (violation instanceof MarkerViolation && violation.isCurrentlyViolated()) {
					fixMarkerViolation(registry, (MarkerViolation)violation, false);
				}
			}
		}
		return fixes;
	}

	private void fixMarkerViolation(IMarkerHelpRegistry registry, final MarkerViolation markerViolation, boolean reportNoFix) {
		IMarker marker = markerViolation.getMarker();
		if (registry.hasResolutions(marker)) {
			IMarkerResolution[] resolutions = registry.getResolutions(marker);
			switch (resolutions.length)  {
			case 0:
				if (reportNoFix) {
					reportNoFix(markerViolation);
				}
				break;
			case 1:
				IMarkerResolution resolution = resolutions[0];
				if (resolution instanceof IPlanMarkerResolution) {
					((IPlanMarkerResolution)resolution).run(marker, plan);
				} else {
					resolution.run(marker);
				}
				break;
			default:
				chooseResolution(marker, resolutions);
			}
		} else if (reportNoFix) {
			reportNoFix(markerViolation);
		}
	}

	private void reportNoFix(final MarkerViolation markerViolation) {
		Display display = WidgetUtils.getDisplay();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation (
						WidgetUtils.getShell(),
						"No fix found",
						"No available fix suggestions for " + markerViolation.getDescription());
			}
		});
	}
	
	private void chooseResolution(IMarker marker, IMarkerResolution[] resolutions) {
		final MarkerResolutionSelectionDialog dialog = new MarkerResolutionSelectionDialog(WidgetUtils.getShell(), resolutions);
		Display display = WidgetUtils.getDisplay();
		final int[] returnCode = new int[1];
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				returnCode[0] = dialog.open();
			}
		});
		if (returnCode[0] != Window.OK) {
			return;
		}
        Object[] result = dialog.getResult();
        if (result != null && result.length > 0) {
			((IMarkerResolution) result[0]).run(marker);
		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (planAdvisorMember == null) {
			// if PlanAdvisor.cleanup() has been called
			return;
		}
		IMarkerDelta[] markerDeltas = event.findMarkerDeltas(null, true);
		List<MarkerViolation> violations = new ArrayList<MarkerViolation>();
		boolean needUpdate = false;
		for (IMarkerDelta markerDelta : markerDeltas) {
			IResource resource = markerDelta.getResource();
			if (resource == null || !resource.exists() || !this.project.equals(resource.getProject())) {
				continue;
			}
			if ((markerDelta.getKind() == IResourceDelta.ADDED) ||
				(markerDelta.getKind() == IResourceDelta.CHANGED)) {
				IMarker marker = markerDelta.getMarker();
				if (!isViolationMarker(marker) && filter(marker)) {
					violations.add(new MarkerViolation(this, marker));
				}
			}
			if (markerDelta.getKind() == IResourceDelta.REMOVED) {
				needUpdate = true;
			}
		}
		if (!violations.isEmpty() || needUpdate) {
			List<Notification> notifications = Collections.<Notification>singletonList(new MarkerNotification(violations));
			if (planAdvisorMember != null) {
				planAdvisorMember.enqueue(notifications);
			}
		}
	}

	/**
	 * Returns true if the marker was created by the MarkerManagementListener
	 * as a marker for PlanAdvisor Violation.
	 * 
	 * @param marker
	 * @return
	 */
	private boolean isViolationMarker(IMarker marker) {
		boolean violationMarker = false;
		try {
			if (marker.getAttribute(MarkerConstants.PLUGIN_ID) != null) {
				violationMarker = true;
			}
		} catch (CoreException e) {
			LogUtil.error(e);
		}
		return violationMarker;
	}
	
	private static class MarkerNotification extends NotificationImpl {

		public MarkerNotification(List<MarkerViolation> violations) {
			super(Notification.ADD_MANY, null, violations);
		}

	}

}
