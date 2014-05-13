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

/*
 * @author Eugene Turkov
 */
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListener;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class OverlayMarkerService extends PlanTimelineService {
	private ResourceSetListener listener = new Listener();
	private IResource resource = null;
	private TransactionalEditingDomain domain;

	private static final Logger logger = Logger.getLogger(OverlayMarkerService.class);

	@Override
	public void activate() {
		EPlan plan = getTimeline().getPlan();
		domain = TransactionUtils.getDomain(plan);
		if (domain != null) {
			domain.addResourceSetListener(listener);
		}
		resource = (IResource) plan.getAdapter(IResource.class);
		addMarkersToWorkspace(plan);
		super.activate();
	}

	@Override
	public void deactivate() {
		if (domain != null) {
			domain.removeResourceSetListener(listener);
			domain = null;
		}
		removeMarkersFromWorkspace();
		super.deactivate();
	}

	private void addMarkersToWorkspace(EPlan plan) {
		Collection<CommonMember> eMembers = EPlanUtils.getMembers(plan, CommonMember.class);
		for (EMember eMember : eMembers) {
			if (eMember instanceof CommonMember) {
				CommonMember commonMember = (CommonMember) eMember;
				if (commonMember.isMarked()) {
					try {
						addOverlayMarkerToResource(resource, commonMember);
					} catch (Exception exception) {
						logger.error(exception);
					}
				}
			}
		}
	}

	private void removeMarkersFromWorkspace() {
		if (resource == null
				|| !resource.exists())
			return;
		try {
			resource.deleteMarkers(MarkerConstants.OVERLAY_MARKER, true, IResource.DEPTH_ONE);
		} catch (CoreException e) {
			LogUtil.error(e);
		}
	}

	public class Listener extends PostCommitListener {

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			try {
				EPlan plan = EPlanUtils.getPlanNotifications(event);
				if (plan != null) {
					List<Notification> notifications = event.getNotifications();
					for (Notification notification : notifications) {
						final int eventType = notification.getEventType();
						// execute code segment when an addition occurs
						if (eventType == Notification.ADD || eventType == Notification.ADD_MANY) {
							addStuff(notification);
						}
						// execute this code segment when a removal occurs
						else if (eventType == Notification.REMOVE || eventType == Notification.REMOVE_MANY) {
							removeStuff(notification);
						}
						// any time changes are being made, execute this code
						// segment also if we are adding a new marker, go in here
						else if (eventType == Notification.SET) {
							Object notifier = notification.getNotifier();
							setStuff(notifier);
						}
					}
				}
			} catch (Throwable e) {
				LogUtil.error(e);
			}
		}
	}

	private void setStuff(final Object notifier) throws CoreException, Exception {
		// either add or remove an overlay
		if (notifier instanceof CommonMember) {
			final CommonMember commonMember = (CommonMember) notifier;
			if (commonMember.isMarked()) {
				IMarker existingMarker = getMarker(resource, commonMember, MarkerConstants.OVERLAY_MARKER);
				if (existingMarker == null) {
					addOverlayMarkerToResource(resource, commonMember);
				} else {
					// this is the case where color changes
					updateOverlayMarker(existingMarker, commonMember);
				}
			} else {
				IMarker existingMarker = getMarker(resource, commonMember, MarkerConstants.OVERLAY_MARKER);
				if (existingMarker != null) {
					removeOverlayMarkerFromResource(resource, commonMember); // could just be existingMarker.delete() ?
				}
			}
		}
		// this is the case where time changes
		else if (notifier instanceof TemporalMember) {
			final TemporalMember temporalMember = (TemporalMember) notifier;
			EPlanElement planElement = temporalMember.getPlanElement();
			if (planElement == null) {
				LogUtil.warn("No plan element for temporal member notifier.");
			} else {
				final CommonMember commonMember = planElement.getMember(CommonMember.class);
				if (commonMember.isMarked()) {
					final IMarker existingMarker = getMarker(resource, commonMember, MarkerConstants.OVERLAY_MARKER);
					updateOverlayMarker(existingMarker, commonMember);
				}
			}
		}
	}

	private void removeStuff(Notification notification) throws CoreException {
		final List<EPlanElement> removedActivitiesAndGroups = getRemovedActivitiesAndGroups(notification);
		for (EPlanElement activityOrGroup : removedActivitiesAndGroups) {
			CommonMember commonMember = activityOrGroup .getMember(CommonMember.class);
			final IMarker existingMarker = getMarker(resource, commonMember, MarkerConstants.OVERLAY_MARKER);
			if (commonMember.isMarked() && existingMarker != null)
				removeOverlayMarkerFromResource(resource, commonMember); // could just be existingMarker.delete() ?
		}
	}

	private void addStuff(Notification notification) throws CoreException, Exception {
		final List<EPlanElement> addedActivitiesAndGroups = getAddedActivitiesAndGroups(notification);
		for (EPlanElement activityOrGroup : addedActivitiesAndGroups) {
			CommonMember commonMember = activityOrGroup .getMember(CommonMember.class);
			final IMarker existingMarker = getMarker(resource, commonMember, MarkerConstants.OVERLAY_MARKER);
			if (commonMember.isMarked() && existingMarker == null) {
				addOverlayMarkerToResource(resource, commonMember);
			}
		}
	}

	/**
	 * This given a notification, this method will return a list of removed
	 * groups and activities (without duplicates) that this notification
	 * contains. If a group is found to have been removed, all of the group's
	 * activities will be added to the list that this method returns. The group
	 * will be added as well.
	 * 
	 * @param notification
	 *            the notification containing EPlanElements
	 * @return all removed activities and groups
	 */
	private static List<EPlanElement> getRemovedActivitiesAndGroups(
			Notification notification) {
		final List<EPlanElement> activitiesAndGroups = new ArrayList<EPlanElement>();
		PlanVisitor visitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				activitiesAndGroups.add(element);
			}
		};
		List<EPlanChild> removedChildren = EMFUtils.getRemovedObjects(
				notification, EPlanChild.class);
		for (EPlanChild removedChild : removedChildren) {
			visitor.visitAll(removedChild);
		}
		return activitiesAndGroups;
	}

	/**
	 * This given a notification, this method will return a list of added groups
	 * and activities (without duplicates) that this notification contains. If a
	 * group is found to have been added, all of the group's activities will be
	 * added to the list that this method returns. The group will be added as
	 * well.
	 * 
	 * @param notification
	 *            the notification containing EPlanElements
	 * @return all added activities and groups
	 */
	private static List<EPlanElement> getAddedActivitiesAndGroups(
			Notification notification) {
		final List<EPlanElement> activitiesAndGroups = new ArrayList<EPlanElement>();
		PlanVisitor visitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				activitiesAndGroups.add(element);
			}
		};
		List<EPlanChild> addedChildren = EMFUtils.getAddedObjects(
				notification, EPlanChild.class);
		for (EPlanChild addChild : addedChildren) {
			visitor.visitAll(addChild);
		}
		return activitiesAndGroups;
	}

	/**
	 * Use this method to get the marker on the resource related to a particular
	 * eMember.
	 * 
	 * @param resource
	 *            the resource which may or may not contain an associated
	 *            overlay marker.
	 * 
	 * @param eMember
	 *            the member which is related to the marker.
	 * @return the marker related to the eMember.
	 * @throws CoreException
	 */
	public static IMarker getMarker(IResource resource, CommonMember commonMember, final String markerId) throws CoreException {
		if ((resource == null) || (commonMember == null)) {
			return null;
		}
		if (!resource.exists()) {
			LogUtil.warnOnce(resource + " does not exist");
			return null;
		}
		final boolean includeSubtypes = false;
		final int depth = 0;
		final IMarker[] markers = resource.findMarkers(markerId, includeSubtypes, depth);
		for (IMarker marker : markers) {
			Object source = marker.getAttribute(IMarker.SOURCE_ID);
			if (commonMember.equals(source)) {
				return marker;
			}
		}
		return null;
	}

	/**
	 * This method should be used to update an existing overlay marker with new
	 * information
	 * 
	 * @param marker
	 *            an overlay marker
	 * @param commonMember
	 *            the member containing the new information.
	 * @throws CoreException
	 * @throws Exception
	 *             if the marker provided is null.
	 */
	private void updateOverlayMarker(IMarker marker, CommonMember commonMember)
			throws CoreException, Exception {
		if (marker == null)
			throw new Exception("Can't update a null marker!");

		EPlanElement planElement = commonMember.getPlanElement();
		TemporalMember temporalMember = planElement.getMember(TemporalMember.class);

		Map<String, Object> attributeToValueMap = new LinkedHashMap<String, Object>();
		attributeToValueMap.put(MarkerConstants.START_TIME, temporalMember.getStartTime());
		attributeToValueMap.put(MarkerConstants.DURATION, temporalMember.getDuration());
		attributeToValueMap.put(MarkerConstants.COLOR, commonMember.getColor());
		attributeToValueMap.put(IMarker.SOURCE_ID, commonMember);
		attributeToValueMap.put(IMarker.MESSAGE, planElement.getName());
		attributeToValueMap.put(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
		attributeToValueMap.put(IMarker.LOCATION, planElement.eResource().getURIFragment(planElement));
		attributeToValueMap.put(MarkerConstants.PLUGIN_ID, TimelinePlugin.ID);
//		attributeToValueMap.put(MarkerConstants.IMAGE_DESCRIPTOR_PATH,
//				MarkerConstants.OVERLAY_MARKER_IMAGE_DESCRIPTOR_PATH);

		marker.setAttributes(attributeToValueMap);
	}

	/**
	 * This method is used to add an overlay marker to the plan resource.
	 * 
	 * @param resource
	 *            the resource to which the overlay marker should be added.
	 * @param eMember
	 *            contains the data to create the marker.
	 */
	private void addOverlayMarkerToResource(IResource resource, CommonMember eMember) throws CoreException, Exception {
		IMarker overlayMarker = resource.createMarker(MarkerConstants.OVERLAY_MARKER);
		updateOverlayMarker(overlayMarker, eMember);
	}

	/**
	 * Calling this method causes an overlay marker to be removed from the
	 * associated eMember.
	 * 
	 * @param resource
	 *            the resource to which the overlay marker belongs.
	 * @param commonMember
	 *            the member to which the overlay marker belongs.
	 */
	private static void removeOverlayMarkerFromResource(IResource resource, CommonMember commonMember) throws CoreException {
		IMarker[] markers = resource.findMarkers(MarkerConstants.OVERLAY_MARKER, false, 0);
		for (IMarker marker : markers) {
			if (commonMember.equals(marker.getAttribute(IMarker.SOURCE_ID))) {
				marker.delete();
				break;
			}
		}
	}

}
