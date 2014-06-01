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
package gov.nasa.arc.spife.ui.timeline.service;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineService;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarkerManager;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ERGB;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.common.ui.editor.MarkerUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.measure.quantity.Duration;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jscience.physics.amount.Amount;

public class FileResourceMarkerService extends TimelineService
	implements IResourceChangeListener, IPropertyChangeListener, TimelineConstants
{

	private class FileResourceTimelineMarker extends TimelineMarker {

		private final IMarker fileMarker;

		public FileResourceTimelineMarker(IMarker fileMarker) {
			this.fileMarker = fileMarker;
		}
		
		public IMarker getFileMarker() {
			return fileMarker;
		}
		
	}

	private final Map<IMarker, TimelineMarker> markerToTimelineMarkerMap
		= new ConcurrentHashMap<IMarker, TimelineMarker>();

	private final ColorMap<String> colorMap = new ColorMap<String>();

	public FileResourceMarkerService() {
		super();
	}

	protected IResource getTimelineResource() {
		return CommonUtils.getAdapter(getTimeline(), IResource.class);
	}

	@Override
	public void activate() {
		getTimeline().getWorkspaceResourceService().addResourceChangeListener(this);
		TIMELINE_PREFERENCES.addPropertyChangeListener(this);
		IResource resource = getTimelineResource();
		if (resource == null) {
			super.activate();
			return;
		}
		try {
			if (resource.exists()) {
				IMarker[] markers = resource.findMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
				for (IMarker marker : markers) {
					initializeTimelineMarker(marker);
				}
			}
		} catch (CoreException e) {
			LogUtil.error("fetching markers from " + resource, e);
		}
		boolean showViolations = resource.exists() && TIMELINE_PREFERENCES.getBoolean(VIOLATIONS_VISIBLE);
		setShowViolations(showViolations);
		super.activate();
	}

	@Override
	public void deactivate() {
		getTimeline().getWorkspaceResourceService().removeResourceChangeListener(this);
		TIMELINE_PREFERENCES.removePropertyChangeListener(this);
		setShowViolations(false);
		colorMap.dispose();
		super.deactivate();
	}

	private void setShowViolations(boolean showViolations) {
		try {
			IResource resource = getTimelineResource();
			if (resource != null && resource.exists() && !showViolations) {
				removeAllViolations();
			} else {
				addAllViolations();
			}
		} catch (CoreException coreException) {
			LogUtil.error(coreException);
		}
	}

	private void removeAllViolations() throws CoreException {
		IResource resource = getTimelineResource();
		if(resource != null && resource.exists()) {
			TimelineMarkerManager markerManager = getTimeline().getTimelineMarkerManager();
			boolean includeSubtypes = true;
			int depth = 0;
			IMarker[] violationMarkers = resource.findMarkers(IMarker.PROBLEM, includeSubtypes, depth);
			for (IMarker marker : violationMarkers) {
				TimelineMarker timelineMarker = markerToTimelineMarkerMap.get(marker);
				if (timelineMarker != null) {
					markerManager.removeMarker(timelineMarker);
				}
			}
		}
	}

	private void addAllViolations() throws CoreException {
		IResource resource = getTimelineResource();
		if (resource != null && resource.exists()) {
			TimelineMarkerManager markerManager = getTimeline().getTimelineMarkerManager();
			boolean includeSubtypes = true;
			int depth = 0;
			IMarker[] violationMarkers = resource.findMarkers(IMarker.PROBLEM, includeSubtypes, depth);
			for (IMarker marker : violationMarkers) {
				TimelineMarker timelineMarker = markerToTimelineMarkerMap.get(marker);
				if (timelineMarker != null) {
					markerManager.addMarker(timelineMarker);
				}
			}
		}
	}

	/**
	 * Use this method to add a new overlay given a new marker.
	 *
	 * @param markerDelta the new marker update.
	 * @throws CoreException
	 */
	private void initializeTimelineMarker(IMarker marker)
	throws CoreException
	{
		TemporalExtent temporalExtent = getTemporalExtent(marker);
		if (temporalExtent == null) {
			return;
		}
		Color color = getColor(marker);
		if (color == null) {
			return;
		}
		Object tooltip = marker.getAttribute(MarkerConstants.TOOLTIP_TEXT);
		TimelineMarker timelineMarker = new FileResourceTimelineMarker(marker);
		timelineMarker.setTooltip(tooltip instanceof String ? (String) tooltip : "tooltip");
		timelineMarker.setColor(color);
		timelineMarker.setTemporalExtent(temporalExtent);

		updateImageDescriptor(marker, timelineMarker);

		markerToTimelineMarkerMap.put(marker, timelineMarker);

		boolean violationsVisible = TIMELINE_PREFERENCES.getBoolean(VIOLATIONS_VISIBLE);
		boolean markerIsOverlayMarker = marker.getType().equals(MarkerConstants.OVERLAY_MARKER);

		if (markerIsOverlayMarker || violationsVisible) {
			getTimeline().getTimelineMarkerManager().addMarker(timelineMarker);
		}
	}

	private static void updateImageDescriptor(IMarker marker, TimelineMarker timelineMarker)
	throws CoreException
	{
		final Object markerPluginId = marker.getAttribute(MarkerConstants.PLUGIN_ID);
		final Object imageDescriptorPath = marker.getAttribute(MarkerConstants.IMAGE_DESCRIPTOR_PATH);
		ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin
			(String.valueOf(markerPluginId), String.valueOf(imageDescriptorPath));

		if(imageDescriptor == null)
		{
			IWorkbenchAdapter adapter = (IWorkbenchAdapter) Platform.getAdapterManager().getAdapter(marker, IWorkbenchAdapter.class);
			if (adapter != null) {
				timelineMarker.setImageDescriptor(adapter.getImageDescriptor(marker));
			}
		} else {
			timelineMarker.setImageDescriptor(imageDescriptor);
		}
	}

	/**
	 * Given a marker, update the graphical overlay component.
	 *
	 * @param marker a marker containing all of the important overlay data.
	 * @throws CoreException
	 */
	private void updateTimelineMarker(IMarker marker)
	throws CoreException
	{
		TimelineMarker timelineMarker = markerToTimelineMarkerMap.get(marker);
		TemporalExtent temporalExtent = getTemporalExtent(marker);
		timelineMarker.setTemporalExtent(temporalExtent);
		Color oldColor = timelineMarker.getColor();
		Color newColor = getColor(marker);
		if((oldColor != null && !oldColor.equals(newColor))
				|| (oldColor == null && newColor != null)) {
			// color has changed..
			timelineMarker.setColor(getColor(marker));
			getTimeline().getTimelineMarkerManager().makerChanged(timelineMarker);
			updateImageDescriptor(marker, timelineMarker);
		}

	}

	private TemporalExtent getTemporalExtent(IMarker marker)
	throws CoreException
	{
		Date startDate = MarkerUtils.getDate(marker, MarkerConstants.START_TIME);
		if (startDate == null)
			return null;
		Amount<Duration> duration = (Amount<Duration>) marker.getAttribute(MarkerConstants.DURATION);
		if (duration == null) {
			duration = DateUtils.ZERO_DURATION;
		}
		return new TemporalExtent(startDate, duration);
	}

	private Color getColor(IMarker marker)
	throws CoreException
	{
		if (marker.isSubtypeOf(IMarker.PROBLEM)) {
			Integer severity = (Integer) marker.getAttribute(IMarker.SEVERITY);
			switch (severity) {
			case IMarker.SEVERITY_ERROR:
				return ColorConstants.red;
			case IMarker.SEVERITY_WARNING:
				return ColorConstants.yellow;
			case MarkerConstants.SEVERITY_WAIVED:
				return ColorConstants.gray;
			case IMarker.SEVERITY_INFO:
			case MarkerConstants.SEVERITY_FIXED:
				return null;
			}
		}
		RGB rgb = null;
		Object rgbObject = marker.getAttribute(MarkerConstants.COLOR);
		if (rgbObject instanceof RGB) {
			rgb = (RGB) rgbObject;
		} else if (rgbObject instanceof ERGB) {
			ERGB eRGB = (ERGB)rgbObject;
			rgb = new RGB(eRGB.red, eRGB.green, eRGB.blue);
		} else {
			return null;
		}
		Color color = colorMap.getCachedColor(rgb.toString());
		if (color == null || color.isDisposed()) {
			color = new Color(null, rgb);
			colorMap.assignColor(rgb.toString(), color);
		}
		return color;
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResource resource = getTimelineResource();
		if (resource == null) {
			return;
		}
		
		try  {
			final boolean includeSubTypes = true;
			IMarkerDelta[] markerDeltas = event.findMarkerDeltas(IMarker.MARKER, includeSubTypes);
			for (IMarkerDelta delta : markerDeltas) {
				IResource markerResource = delta.getResource();
				if (CommonUtils.equals(resource, markerResource)) {
					final IMarker marker = delta.getMarker();
					if (delta.getKind() == IResourceDelta.ADDED) {
						initializeTimelineMarker(marker);
					} else if (delta.getKind() == IResourceDelta.CHANGED) {
						// add a time line marker
						final TimelineMarker timelineMarker = markerToTimelineMarkerMap.get(marker);
						if (timelineMarker != null) {
							updateTimelineMarker(marker);
						} else {
							initializeTimelineMarker(marker);
						}
					} else if (delta.getKind() == IResourceDelta.REMOVED) {
						TimelineMarker timelineMarker = markerToTimelineMarkerMap.remove(marker);
						if (timelineMarker != null) {
							getTimeline().getTimelineMarkerManager().removeMarker(timelineMarker);
						}
					}
				}
			}
		} catch (CoreException coreException) {
			LogUtil.error(coreException);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event)
	{
		Object property = event.getProperty();
		if(property.equals(VIOLATIONS_VISIBLE)) {
			setShowViolations(TIMELINE_PREFERENCES.getBoolean(VIOLATIONS_VISIBLE));
		}
	}

	public IMarker getMarker(TimelineMarker timelineMarker)
	{
		if (timelineMarker instanceof FileResourceTimelineMarker) {
			FileResourceTimelineMarker marker = (FileResourceTimelineMarker) timelineMarker;
			return marker.getFileMarker();
		}
		return null;
	}
}
