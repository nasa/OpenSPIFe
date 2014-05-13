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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.common.ui.editor.MarkerUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationKey;
import gov.nasa.ensemble.core.plan.advisor.ViolationSeverity;
import gov.nasa.ensemble.core.plan.advisor.fixing.MarkerResolutionOperation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IMarkerHelpRegistry;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.ide.IDE;

public class MarkerViolation extends Violation {

	private static final Map<String, String> MARKER_TYPE_LABELS = new HashMap<String, String>();
	private static final Logger trace = Logger.getLogger(MarkerViolation.class);
	
	static {
		try {
			initializeMarkerTypeNames();
		} catch(Exception e) {
			trace.error(e.getMessage(), e);
		}
	}
	
	private static void initializeMarkerTypeNames() {
		IExtensionPoint point = Platform.getExtensionRegistry().getExtensionPoint(ResourcesPlugin.PI_RESOURCES, ResourcesPlugin.PT_MARKERS);
		if (point != null) {
			IExtension[] extensions = point.getExtensions();
			for (int i = 0; i < extensions.length; ++i) {
				IExtension ext = extensions[i];
				String id = ext.getUniqueIdentifier();
				String label = ext.getLabel();
				if (label.equals("")) {
					if (id.equals(IMarker.PROBLEM)) {
						label = "Problem";
					} else {
						label = id;
					}
				}
				MARKER_TYPE_LABELS.put(id, label);
			}
		}
	}
	
	private IMarker marker;
	private final String markerType;
	private final String markerName;
	private final String markerMessage;
	private final Map markerAttributes;
	
	@Override
	public void dispose() {
		super.dispose();
		try {
			marker.delete();
		} catch (CoreException e) {
			//silent
		}
		marker = null;
		markerAttributes.clear();
	}

	public MarkerViolation(PlanAdvisor advisor, IMarker marker) {
		super(advisor);
		this.marker = marker;
		String markerType = null;
		String markerName = null;
		String markerMessage = null;
		Map markerAttributes = null;
		try {
			markerType = marker.getType();
			markerName = (String)marker.getAttribute(MarkerConstants.NAME);
			markerMessage = String.valueOf(marker.getAttribute(IMarker.MESSAGE));
			// Make a copy of the marker attributes map for equality comparison
			// getAttributes returns an instance of MarkerAttributeMap, whose hashcode method
			// only takes into account the map keys
			Map<String, Object> attributes = marker.getAttributes();
			if (attributes != null) {
				markerAttributes = new HashMap(attributes);
				// Remove line number as it is not essential to the marker violation identity
				markerAttributes.remove(IMarker.LINE_NUMBER);
				// Remove from map copy any attributes added by MarkerManagementListener.addViolationAttributes
				markerAttributes.remove(MarkerConstants.PLUGIN_ID);
				markerAttributes.remove(IMarker.PRIORITY);
				markerAttributes.remove(MarkerConstants.IMAGE_DESCRIPTOR_PATH);
				markerAttributes.remove(IMarker.DONE);
			}
		} catch (CoreException e) {
			if (markerType == null) {
				markerType = super.getType();
			}
			if (markerMessage == null) {
				markerMessage = super.getDescription();
			}
		}
		this.markerType = markerType;
		this.markerName = markerName;
		this.markerMessage = markerMessage;
		this.markerAttributes = markerAttributes;
	}

	public IMarker getMarker() {
		return marker;
	}	
	
	@Override
	public String getType() {
		String label = MARKER_TYPE_LABELS.get(markerType);
		return label==null?markerType:label;
	}

	@Override
	public String getMarkerType() {
		String result = marker.getClass().getSimpleName();
		if (markerType == IMarker.BOOKMARK) {
			result = "Bookmark";
		} else if (markerType == IMarker.PROBLEM) {
			result = "Problem";
		} else if (markerType == IMarker.MARKER) {
			result = "Marker";
		}
		return result;
	}

	@Override
	public String getName() {
		String result = markerName;
		if (result == null) {
			result = getMarkerType();
		}
		return result;
	}

	@Override
	public String getDescription() {
		return markerMessage;
	}
	
	@Override
	public Date getTime() {
		if (marker.exists()) {
			Date startTime = MarkerUtils.getDate(marker, MarkerConstants.START_TIME);
			if (startTime != null) {
				return startTime;
			}
		}
		return super.getTime();
	}
	
	@Override
	public ViolationSeverity getSeverity() {
		int markerSeverity = marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
		switch (markerSeverity) {
		case IMarker.SEVERITY_ERROR:
			return ViolationSeverity.ERROR;
		case IMarker.SEVERITY_WARNING:
			return ViolationSeverity.WARNING;
		default:
			return ViolationSeverity.INFO;
		}
	}
	
	@Override
	public Set<Suggestion> getSuggestions() {
		Set<Suggestion> suggestions = new LinkedHashSet<Suggestion>();
		IMarkerHelpRegistry registry = IDE.getMarkerHelpRegistry();
		IMarker marker = getMarker();
		EPlan plan = ((MarkerPlanAdvisor)getAdvisor()).getPlan();
		if (registry.hasResolutions(marker)) {
			IMarkerResolution[] resolutions = registry.getResolutions(marker);
			for (IMarkerResolution resolution : resolutions) {
				String description = resolution.getLabel() + " for " + getDescription() + " at " + getPrintString(ViolationKey.TIME);
				IUndoableOperation operation = new MarkerResolutionOperation(this, resolution, plan);
				Suggestion suggestion = new Suggestion(null, description, operation);
				suggestions.add(suggestion);
			}
		}
		return suggestions;
	}

	@Override
	public List<? extends EPlanElement> getElements() {
		String location = marker.getAttribute(IMarker.LOCATION, null);
		if (location != null) {
			EPlan plan = ((MarkerPlanAdvisor)getAdvisor()).getPlan();
			EObject object = plan.eResource().getEObject(location);
			if (object instanceof EPlanElement && !(object instanceof EPlan)) {
				List<EPlanElement> elements = new ArrayList<EPlanElement>(1);
				elements.add((EPlanElement)object);
				return elements;
			}
		}
		return super.getElements();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MarkerViolation) {
			MarkerViolation violation = (MarkerViolation) obj;
			return CommonUtils.equals(marker.getResource(), violation.marker.getResource()) 
				&& CommonUtils.equals(markerAttributes, violation.markerAttributes);
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		IResource resource = marker.getResource();
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((markerAttributes == null) ? 0 : markerAttributes.hashCode());
		return result;
	}

	@Override
	public boolean isObsolete() {
		IResource resource = marker.getResource();
		return (resource == null || !resource.exists());
	}
	
	@Override
	public boolean isCurrentlyViolated() {
		return marker.exists();
	}
	
//	@Override
//	public boolean isWaivedByInstance() {
//		return (markerType != IMarker.PROBLEM);
//	}
	
	@Override
	public boolean isFixable() {
		return true;
	}
	
	@Override
	public boolean isSelectedToFix(Set<Object> selectedObjects) {
		return selectedObjects.contains(this);
	}
	
}
