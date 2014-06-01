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
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.advisor.AdvisorListener;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationIcons;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.resource.Resource;

public class MarkerManagementListener extends AdvisorListener {

	private EPlan plan;
	private IResource resource;
    private final Map<ViolationTracker, IMarker> violationToMarkerMap = new LinkedHashMap<ViolationTracker, IMarker>();
    private final Map<EPlanElement, Collection<IMarker>> elementToMarkerMap = new HashMap<EPlanElement, Collection<IMarker>>();
    private final Map<IMarker, Violation> markerToViolationMap = new HashMap<IMarker, Violation>();

	public MarkerManagementListener(EPlan plan) {
		this.plan = plan;
		this.resource = CommonUtils.getAdapter(plan, IResource.class);
    }
	
	public Violation getMarkerViolation(IMarker marker) {
		try {
			Object source = marker.getAttribute(IMarker.SOURCE_ID);
			if (source instanceof Violation) {
				return (Violation)source;
			}
			synchronized (markerToViolationMap) {
				return markerToViolationMap.get(marker);
			}
		} catch (CoreException e) {
			LogUtil.error("fetching violation from marker", e);
		}
		return null;
	}
	
	public List<IMarker> getPlanElementMarkers(EPlanElement element) {
		synchronized (elementToMarkerMap) {
			Collection<IMarker> markers = elementToMarkerMap.get(element);
			if (markers == null || markers.isEmpty()) {
				return Collections.emptyList();
			}
			List<IMarker> existingMarkers = new ArrayList<IMarker>();
			for (IMarker marker : markers) {
				if (marker.exists()) {
					existingMarkers.add(marker);
				}
			}
			markers.retainAll(existingMarkers);
			return existingMarkers;
		}
	}

	@Override
	public void violationsAdded(Set<ViolationTracker> violations) {
		final Map<ViolationTracker, Map<String, Object>> violationAttributes = new LinkedHashMap<ViolationTracker, Map<String, Object>>();
		final Map<IMarker, Map<String, Object>> updatedAttributes = new LinkedHashMap<IMarker, Map<String, Object>>();
		for (ViolationTracker violationTracker : violations) {
			Violation violation = violationTracker.getViolation();
			if (violation instanceof MarkerViolation) {
				IMarker marker = ((MarkerViolation)violation).getMarker();
				if (marker.exists()) {
					Map<String, Object> attributes = null;
					try {
						attributes = marker.getAttributes();
					} catch (CoreException e) {
						LogUtil.error("Error getting marker attributes", e);
						continue;
					}
					addViolationAttributes(violation, attributes, marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR));
					synchronized (markerToViolationMap) {
						markerToViolationMap.put(marker, violation);
					}
					addMarkerToViolationElements(violation, marker);
					updatedAttributes.put(marker, attributes);
				}
			} else {
				Map<String, Object> attributes = getAttributes(violationTracker);
				violationAttributes.put(violationTracker, attributes);
			}
		}
		if (!violationAttributes.isEmpty()) {
			WorkspaceJob job = new WorkspaceJob("marker creation") {
				/**
				 * @throws CoreException  
				 */
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					try {
						createMarkers(violationAttributes);
						return Status.OK_STATUS;
					} finally {
						violationAttributes.clear();
					}
				}
				
			};
			job.setRule(resource);
			job.schedule();
		}
		if (!updatedAttributes.isEmpty()) {
			WorkspaceJob job = new WorkspaceJob("marker updating") {
				/**
				 * @throws CoreException  
				 */
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					try {
						updateMarkers(updatedAttributes);
						return Status.OK_STATUS;
					} finally {
						updatedAttributes.clear();
					}
				}
			};
			job.setRule(resource);
			job.schedule();
		}
	}

	private void addMarkerToViolationElements(Violation violation, IMarker marker) {
		synchronized (elementToMarkerMap) {
			for (EPlanElement element : violation.getElements()) {
				addMarkerToPlanElement(element, marker);
			}
		}
	}

	private void addMarkerToPlanElement(EPlanElement element, IMarker marker) {
		Collection<IMarker> elementMarkers = elementToMarkerMap.get(element);
		if (elementMarkers == null) {
			elementMarkers = new ArrayList<IMarker>();
			elementToMarkerMap.put(element, elementMarkers);
		}
		elementMarkers.add(marker);
	}
	
	private void removeMarkerFromViolationElements(Violation violation, IMarker marker) {
		synchronized (elementToMarkerMap) {
			for (EPlanElement element : violation.getElements()) {
				removeMarkerFromPlanElement(element, marker);
			}
		}
	}

	private void removeMarkerFromPlanElement(EPlanElement element, IMarker marker) {
		Collection<IMarker> elementMarkers = elementToMarkerMap.get(element);
		if (elementMarkers != null) {
			elementMarkers.remove(marker);
		}
	}
	
	private void updateViolationElementsMarkers(List<EPlanElement> oldCulprits, List<EPlanElement> newCulprits, IMarker marker) {
		synchronized (elementToMarkerMap) {
			for (EPlanElement element : oldCulprits) {
				if (!newCulprits.contains(element)) {
					removeMarkerFromPlanElement(element, marker);
				}
			}
			for (EPlanElement element : newCulprits) {
				if (!oldCulprits.contains(element)) {
					addMarkerToPlanElement(element, marker);
				}
			}
		}
	}


	@Override
	public void advisorsUpdated() {
		final Map<IMarker, Map<String, Object>> markerAttributes = new LinkedHashMap<IMarker, Map<String, Object>>();
		synchronized (violationToMarkerMap) {
			for (Iterator<Map.Entry<ViolationTracker, IMarker>> it = violationToMarkerMap.entrySet().iterator(); it.hasNext();) {
				Map.Entry<ViolationTracker, IMarker> entry = it.next();
				ViolationTracker violation = entry.getKey();
				if (violation != null) {
					try {
						IMarker marker = entry.getValue();
						if (marker.exists()) {
							Map<String, Object> newAttributes = getAttributes(violation);
							Map<?,?> oldAttributes = marker.getAttributes();
							if (!oldAttributes.equals(newAttributes)) {
								markerAttributes.put(marker, newAttributes);
								Resource resource = plan.eResource();
								List<EPlanElement> oldCulprits = EPlanUtils.getCulprits(marker, resource);
								List<EPlanElement> newCulprits = EPlanUtils.getCulprits(newAttributes, resource);
								if (!oldCulprits.equals(newCulprits)) {
									updateViolationElementsMarkers(oldCulprits, newCulprits, marker);
								}
							}
						} else {
							it.remove();
						}
					} catch (Exception e) {
						LogUtil.error("managing marker updates", e);
					}
				}
			}
		}

		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		/*
		if the resource tree is locked, add a listener to check for POST_CHANGE
		events and see if the tree has become unlocked. Remove the listener
		when it has performed the desired task.
		*/
		if (!markerAttributes.isEmpty()) {
			if (workspace.isTreeLocked()) {
				workspace.addResourceChangeListener(new Listener(markerAttributes, workspace));
			} else {
				WorkspaceJob job = new WorkspaceJob("marker updating") {
					/**
					 * @throws CoreException  
					 */
					@Override
					public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
						updateMarkers(markerAttributes);
						return Status.OK_STATUS;
					}
				};
				job.setRule(resource);
				job.schedule();
			}
		}
	}

	@Override
	public void violationsRemoved(Set<ViolationTracker> violations) {
		final List<IMarker> markers = new ArrayList<IMarker>();
		synchronized (violationToMarkerMap) {
			for (ViolationTracker violationTracker : violations) {
				IMarker marker = violationToMarkerMap.remove(violationTracker);
				if (marker != null) {
					markers.add(marker);
					Violation violation = violationTracker.getViolation();
					if (violation != null) {
						removeMarkerFromViolationElements(violation, marker);
					}
				}
			}
		}
		if (!markers.isEmpty()) {
			WorkspaceJob job = new WorkspaceJob("marker removal") {
				/**
				 * @throws CoreException  
				 */
				@Override
				public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
					removeMarkers(markers);
					return Status.OK_STATUS;
				}
			};
			job.setRule(resource);
			job.schedule();
		}
	}
	
	public void dispose() {
		removeAllViolationMarkers();
		synchronized (violationToMarkerMap) {
			violationToMarkerMap.clear();
		}
		synchronized (elementToMarkerMap) {
			elementToMarkerMap.clear();
		}
		synchronized (markerToViolationMap) {
			markerToViolationMap.clear();
		}
		plan = null;
		resource = null;
	}

	/*
	 * Utility methods
	 */

	private Map<String, Object> getAttributes(ViolationTracker violationTracker) {
		Violation violation = violationTracker.getViolation();
		Map<String, Object> attributes = new LinkedHashMap<String, Object>();
    	attributes.put(IMarker.TRANSIENT, true);
    	attributes.put(IMarker.MESSAGE, violation.getMessage());
    	addViolationAttributes(violation, attributes, IMarker.SEVERITY_ERROR);
		List<? extends EPlanElement> elements = violation.getElements();
		EPlanElement element = (elements.isEmpty() ? null : elements.get(0));
		if ((element != null) && (element.eResource() != null)) {
			attributes.put(IMarker.LOCATION, element.eResource().getURIFragment(element));
			attributes.put(EPlanUtils.CULPRITS, elements);
        }
		if (violation.getTime() != null) {
			attributes.put(MarkerConstants.START_TIME, violation.getTime());
		} else if ((element != null) && element.getMember(TemporalMember.class).getStartTime() != null) {
			attributes.put(MarkerConstants.START_TIME, element.getMember(TemporalMember.class).getStartTime());
		}
		return attributes;
	}

	private void addViolationAttributes(Violation violation, Map<String, Object> attributes, int unfixedSeverity) {
		attributes.put(MarkerConstants.PLUGIN_ID, gov.nasa.ensemble.core.plan.advisor.Activator.PLUGIN_ID);
		attributes.put(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
		if (!(violation instanceof MarkerViolation)) {
			attributes.put(IMarker.SOURCE_ID, violation);
		}
		if (!violation.isCurrentlyViolated()) {
			attributes.put(MarkerConstants.IMAGE_DESCRIPTOR_PATH, ViolationIcons.violation_fixed.getFileName());
			attributes.put(IMarker.SEVERITY, MarkerConstants.SEVERITY_FIXED);
			attributes.put(IMarker.DONE, Boolean.TRUE);
		} else if (violation.isWaivedByInstance() || violation.isOutOfDate()) {
			attributes.put(MarkerConstants.IMAGE_DESCRIPTOR_PATH, ViolationIcons.violation_waived_from_activity.getFileName());
			attributes.put(IMarker.SEVERITY, MarkerConstants.SEVERITY_WAIVED);
			attributes.put(IMarker.DONE, Boolean.TRUE);
		} else if (violation.isWaivedByRule()) {
			attributes.put(MarkerConstants.IMAGE_DESCRIPTOR_PATH, ViolationIcons.violation_waived_from_plan_rule.getFileName());
			attributes.put(IMarker.SEVERITY, MarkerConstants.SEVERITY_WAIVED);
			attributes.put(IMarker.DONE, Boolean.TRUE);
		} else {
			ViolationIcons icon = ((unfixedSeverity == IMarker.SEVERITY_ERROR) 
					? ViolationIcons.violation_unfixed
					: ViolationIcons.warning);
			attributes.put(MarkerConstants.IMAGE_DESCRIPTOR_PATH, icon.getFileName());
			attributes.put(IMarker.SEVERITY, unfixedSeverity);
			attributes.put(IMarker.DONE, Boolean.FALSE);
		}
	}

	private void createMarkers(Map<ViolationTracker, Map<String, Object>> violationAttributes) {
		synchronized (violationToMarkerMap) {
		    for (Map.Entry<ViolationTracker, Map<String, Object>> entry : violationAttributes.entrySet()) {
		    	ViolationTracker violationTracker = entry.getKey();
		    	Violation violation = violationTracker.getViolation();
		    	Map<String, Object> attributes = entry.getValue();
		    	try {
		            String markerId = violation.getMarkerType();
		            if (resource != null && resource.exists()) {
						IMarker marker = resource.createMarker(markerId);
			            marker.setAttributes(attributes);
			            violationToMarkerMap.put(violationTracker, marker);
			            addMarkerToViolationElements(violation, marker);
		            }
		        } catch (CoreException e) {
		        	LogUtil.error("createMarkers", e);
		        }
		    }
		}
    }

	private void updateMarkers(Map<IMarker, Map<String, Object>> markerAttributes) {
	    for (Map.Entry<IMarker, Map<String, Object>> entry : markerAttributes.entrySet()) {
	    	IMarker marker = entry.getKey();
	    	Map<String, Object> attributes = entry.getValue();
		    try {
		    	if(marker.exists()) {
		    		marker.setAttributes(attributes);
		    	} else {
		    		LogUtil.warn("trying to update a marker that no longer exists");
		    	}
	        } catch (CoreException e) {
	        	LogUtil.error("updateMarkers", e);
	        }
	    }
    }

	private void removeMarkers(List<IMarker> markers) {
		for (IMarker marker : markers) {
		    try {
		    	marker.delete();
	        } catch (CoreException e) {
	        	LogUtil.error("removeMarkers", e);
	        }
		}
    }
	
	private void removeAllViolationMarkers() {
		if (resource == null || !resource.exists()) {
			return;
		}
		Set<String> markerTypes = new HashSet<String>();
		try {
			synchronized (violationToMarkerMap) {
				for (IMarker marker : violationToMarkerMap.values()) {
					if(marker.exists()) {
						Object source = marker.getAttribute(IMarker.SOURCE_ID);
						if (source instanceof Violation) {
							((Violation)source).dispose();
						}
						Object culprits = marker.getAttribute(EPlanUtils.CULPRITS);
						if (culprits instanceof List && !((List)culprits).isEmpty()) {
							try {
								((List)culprits).clear();
							} catch (UnsupportedOperationException e) {
								// the culprits list is unmodifiable
							}
						}
						markerTypes.add(marker.getType());
					}
				}
			}
			for (String markerType : markerTypes) {
				resource.deleteMarkers(markerType, false, IResource.DEPTH_ZERO);
			}
		} catch (CoreException e) {
			LogUtil.error(e);
		}
	}

    private final class Listener implements IResourceChangeListener {
		private final Map<IMarker, Map<String, Object>> markerAttributes;
		private final IWorkspace workspace;
		private boolean treeGotUnlocked;

		private Listener(Map<IMarker, Map<String, Object>> markerAttributes, IWorkspace workspace) {
			this.markerAttributes = markerAttributes;
			this.workspace = workspace;
			treeGotUnlocked = false;
		}

		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
				try {
					if (workspace.isTreeLocked()) {
						// do nothing
						return;
					} else {
						treeGotUnlocked = true;
					}
					WorkspaceJob job = new WorkspaceJob("marker updating") {
						/**
						 * @throws CoreException  
						 */
						@Override
						public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
							updateMarkers(markerAttributes);
							return Status.OK_STATUS;
						}
					};
					job.setRule(resource);
					job.schedule();
				} finally {
					if(treeGotUnlocked) {
						workspace.removeResourceChangeListener(this);
					}
				}
			}
		}
	}

}
