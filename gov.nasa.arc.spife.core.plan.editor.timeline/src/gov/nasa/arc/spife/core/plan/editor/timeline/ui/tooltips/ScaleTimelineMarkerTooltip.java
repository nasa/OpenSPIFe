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
package gov.nasa.arc.spife.core.plan.editor.timeline.ui.tooltips;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineTool;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.part.ScaleTimelineMarkerEditPart;
import gov.nasa.arc.spife.ui.timeline.service.FileResourceMarkerService;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.Tool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * This abstract class should be used to create tooltip shells for use with
 * ScaleTimelineMarker objects. Use getNewInstance(...) for a new instance of a
 * class implementing this class.
 * @author Eugene Turkov
 *
 */
public abstract class ScaleTimelineMarkerTooltip {

	protected static final int TOOLTIP_WIDTH = 500;
	
	// only one resource manager is needed
	protected static ResourceManager resourceManager;
	
	@SuppressWarnings("unused")
	private static IPartListener partListener = new PartListener();

	// the general composite to which all other widgets will be added
	protected Composite mainComposite;

	protected Composite titleComposite;
	protected Label titleBodySeparatorLabel;
	protected Composite bodyComposite;
	protected Composite suggestionsComposite;
	private WeakHashMap<ViolationTracker, Image> violationTrackerToImageMap;
	protected Shell shell;

	List<EditPart> overlappingEditParts = new ArrayList<EditPart>();

	/*
	 * The marker that has been selected contains a TimelineMarker as a model. This
	 * model is used to gain access to the appropriate ViolationTracker that is
	 * associated with the graphical edit part.
	 */
	protected ViolationTracker violationTracker;

	/*
	 *  The edit part that corresponds to the violationTracker and which is the
	 *  primary focus of this tooltip.
	 */
	protected ScaleTimelineMarkerEditPart scaleTimelineMarkerEditPart;

	/*
	 * so far, only one instance of a group tooltip is supported at any given time.
	 * A group tooltip is one that displays information about multiple violations
	 * simultaneously.
	 */
	protected static GroupScaleTimelineMarkerTooltip groupScaleTimelineMarkerTooltip;

	/*
	 * so far, only one instance of a solitary tooltip is supported at any given time.
	 * A solitary tooltip is one that displays detailed information about a
	 * single violation - this is the type of tooltip that should show up when
	 * a single marker is the only editPart after calling getOverlappingEditParts(...)
	 */
	protected static SolitaryScaleTimelineMarkerTooltip solitaryScaleTimelineMarkerTooltip;

	protected IdentifiableRegistry<EPlanElement> identifiableRegistry;

	protected ScaleTimelineMarkerTooltip(final Display display, int style
			, ScaleTimelineMarkerEditPart scaleTimelineMarkerEditPart
			, ViolationTracker violationTracker)
	{
		this.violationTracker = violationTracker;
		this.scaleTimelineMarkerEditPart = scaleTimelineMarkerEditPart;
		Color color = display.getSystemColor (SWT.COLOR_INFO_BACKGROUND);
		this.shell = new Shell(display, style);
		this.shell.setBackground(color);
		this.shell.setLayout (new FillLayout());
		identifiableRegistry = new IdentifiableRegistry<EPlanElement>();

		mainComposite = getMainComposite();
		titleComposite = getTitleComposite();
		titleBodySeparatorLabel = getTitleBodySeparatorLabel();
		bodyComposite = getBodyComposite();
		suggestionsComposite = getSuggestionsComposite();
		updateTooltipVisibilityForCursorLocation(display);		
				
		this.shell.pack();
	}

	/**
	 * Due to the nature of "nested tooltips" some mouse exit notifications do not propogate
	 * to the appropriate mouse track listener methods. This method serves to manage tooltip
	 * visibility.
	 * @param display
	 */
	private void updateTooltipVisibilityForCursorLocation(final Display display) {
		display.timerExec(1000, new Runnable() {
			@Override
			@SuppressWarnings("null")
			public void run() {
				boolean cursorContainedInGroupTooltip = false;
				boolean cursorContainedInSolitaryTooltip = false;
				Shell groupShell = null;
				if(groupScaleTimelineMarkerTooltip != null) {
					groupShell = groupScaleTimelineMarkerTooltip.getShell();
				}
				
				/*
				 * Check if the mouse if over the group tooltip
				 */
				if(groupScaleTimelineMarkerTooltip != null && !groupShell.isDisposed()) {
					Point cursorLocation = display.getCursorLocation();
					if(groupShell.getBounds().contains(cursorLocation)) {
						cursorContainedInGroupTooltip = true;
					}	
				}
				
				/*
				 * Check if the mouse is over the solitary tooltip
				 */
				Shell solitaryShell = null;
				if(solitaryScaleTimelineMarkerTooltip != null) {
					solitaryShell = solitaryScaleTimelineMarkerTooltip.getShell();
				}
				if(solitaryScaleTimelineMarkerTooltip != null) {
					solitaryScaleTimelineMarkerTooltip.getShell();
					if(solitaryScaleTimelineMarkerTooltip != null && !solitaryShell.isDisposed()) {
						Point cursorLocation = display.getCursorLocation();
						if(solitaryShell.getBounds().contains(cursorLocation)) {
							cursorContainedInSolitaryTooltip = true;
						}	
					}
				}
				
				/*
				 * if the mouse is over the group tooltip, but not the solitary tooltip
				 * then the solitary tooltip needs to be disposed
				 */				
				if(cursorContainedInGroupTooltip && !cursorContainedInSolitaryTooltip) {
					if(solitaryShell != null && !solitaryShell.isDisposed()) {
						solitaryShell.dispose();
						if(shell.equals(solitaryShell)) {
							// if this shell belongs to the solitary shell, stop the timer
							updateTooltipVisibilityForCursorLocation(display);
						} else {
							solitaryShell.dispose();
						}
					}
				} else if(!cursorContainedInGroupTooltip && !cursorContainedInSolitaryTooltip) {
					/*
					 * if the mouse is over neither, then we should get rid of both and stop all timers
					 */
					if(solitaryShell != null && !solitaryShell.isDisposed()) {
						solitaryShell.dispose();
					}
					if(groupShell != null && !groupShell.isDisposed()) {
						groupShell.dispose();
					}
				}
				
				if((ScaleTimelineMarkerTooltip.this instanceof SolitaryScaleTimelineMarkerTooltip)
						&& cursorContainedInSolitaryTooltip) {
					updateTooltipVisibilityForCursorLocation(display);
				} else if((ScaleTimelineMarkerTooltip.this instanceof GroupScaleTimelineMarkerTooltip)
					&& cursorContainedInGroupTooltip) {
					updateTooltipVisibilityForCursorLocation(display);
				}
			}
		});
	}

	/**
	 * This method should be used to instantiate tooltips which subclass
	 * SaleTimelineMakerTooltip. To create a tooltip for a specific ViolationTracker
	 * , pass in the ViolationTracker parameter, otherwise pass in null to have
	 * all violation trackers (each tracker from a different overlapping editPart)
	 * examined to determine what kind of tooltip should be displayed.
	 * @param scaleTimelineMarkerEditPart the focus edit part
	 * @return an appropriate tooltip
	 * @throws CoreException
	 */
	public static ScaleTimelineMarkerTooltip getInstance(ScaleTimelineMarkerEditPart
			scaleTimelineMarkerEditPart, ViolationTracker violationTracker)
	throws CoreException
	{
		List<ViolationTracker> violationTrackers = ScaleTimelineMarkerTooltip
			.getViolationTrackers(scaleTimelineMarkerEditPart);

		// only one violation
		if(violationTracker != null || violationTrackers.size() == 1) {
			if (solitaryScaleTimelineMarkerTooltip != null
					&& solitaryScaleTimelineMarkerTooltip.shell != null
					&& !solitaryScaleTimelineMarkerTooltip.shell.isDisposed()) {
				solitaryScaleTimelineMarkerTooltip.shell.dispose();
			}

			if(violationTracker == null) {
				violationTracker = violationTrackers.get(0);
			}

			solitaryScaleTimelineMarkerTooltip
				= new SolitaryScaleTimelineMarkerTooltip(WidgetUtils.getDisplay()
					, SWT.TOOL, scaleTimelineMarkerEditPart, violationTracker);

			return solitaryScaleTimelineMarkerTooltip;
		}

		// else, multiple violations
		if (groupScaleTimelineMarkerTooltip != null
				&& !groupScaleTimelineMarkerTooltip.shell.isDisposed()) {
			groupScaleTimelineMarkerTooltip.shell.dispose();
		}

		groupScaleTimelineMarkerTooltip = new GroupScaleTimelineMarkerTooltip(
				WidgetUtils.getDisplay(), SWT.TOOL, scaleTimelineMarkerEditPart);

		return groupScaleTimelineMarkerTooltip;
	}

	public Shell getShell() {
		return shell;
	}

	/**
	 * Here we want to retrieve the image of a particular violation, however,
	 * the violation tracker in question may not be associated with the current host.
	 *
	 * This method is required when building a tooltip that contains information
	 * about multiple violations.
	 *
	 * @param violationTracker
	 * @return
	 * @throws CoreException
	 */
	protected Image getImage(ViolationTracker violationTracker)
	{
		try
		{
			if(violationTrackerToImageMap == null) {
				violationTrackerToImageMap = new WeakHashMap<ViolationTracker, Image>();
			}

			Image image = violationTrackerToImageMap.get(violationTracker);
			if(image != null)
				return image;

			// get all the edit parts that overlap with the current host edit part
			if (overlappingEditParts == null || overlappingEditParts.size() == 0) {
				overlappingEditParts = ScaleTimelineMarkerTooltip
				.getOverlappingEditParts(this.scaleTimelineMarkerEditPart);
			}
			
			PlanAdvisorMember planAdvisorMember = getPlanAdvisorMember(scaleTimelineMarkerEditPart);

			// iterate through the existing edit parts to find the one associated
			// with the given violation tracker
			for (EditPart editPart : overlappingEditParts) {
				Object model = editPart.getModel();
				if (model instanceof TimelineMarker) {
					TimelineMarker timelineMarker = (TimelineMarker) model;
					IMarker marker = getMarker(timelineMarker, scaleTimelineMarkerEditPart.getViewer());
					if (marker == null || !marker.exists()) {
						continue;
					}
					Violation violation = planAdvisorMember.getMarkerViolation(marker);
					if (violation != null && violation.equals(violationTracker.getViolation())) {

						// here we have found the marker for the current violation tracker
						// so we can now extract the image information
						String markerPluginId = String.valueOf(marker
								.getAttribute(MarkerConstants.PLUGIN_ID));
						String imageDescriptorPath = String
						.valueOf(marker
								.getAttribute(MarkerConstants.IMAGE_DESCRIPTOR_PATH));
						ImageDescriptor imageDescriptor = AbstractUIPlugin
						.imageDescriptorFromPlugin(markerPluginId,
								imageDescriptorPath);
						image = (Image) getResourceManager().get(
								imageDescriptor);

						// store the relationship to avoid looking up the image again
						violationTrackerToImageMap.put(violationTracker, image);
						return image;
					}
				}
			}
		}

		catch(CoreException e)
		{
			Logger.getLogger(ScaleTimelineMarkerTooltip.class)
			.error("getting image for violation tracker", e);
		}

		return null;
	}

	/**
	 * The best way to think about this method is to imaging that you are shooting
	 * an arrow through a violation indicator on the timeline. When that arrow gets
	 * pinned to the timeline, it will be going through one or more violations
	 * (or even no violations if you didn't shoot at a violation). To find out which
	 * violations you "pinned," use this method.
	 *
	 * @param scaleTimelineMarkerEditPart
	 * @return the overlapping edit parts
	 */
	public static List<EditPart> getOverlappingEditParts(ScaleTimelineMarkerEditPart
			scaleTimelineMarkerEditPart)
	{
		Tool tool = scaleTimelineMarkerEditPart.getViewer().getEditDomain().getActiveTool();
		List<EditPart> editParts = null;
		if(tool instanceof TimelineTool) {
			TimelineTool timelineTool = (TimelineTool)tool;

			editParts = timelineTool.getEditPartsForRequest
				(new Request(TimelineConstants.REQ_ERASE_TOOLTIP_FEEDBACK));
		}

		/*
		 * there is a case where a child tooltip is displayed from a parent
		 * and as such the above timeline tool method to get the edit parts fails
		 * because it uses the current viewer for the tool (the current viewer is
		 * null in this case).
		 */
		if(editParts == null) {
			editParts = new ArrayList<EditPart>();
			editParts.add(scaleTimelineMarkerEditPart);
		}
		return editParts;
	}

	/**
	 * Returns all of the violation trackers at the current selection
	 * tool's location. the static designation is requred so that this abstract
	 * superclass can automatically determine what type of tooltip it should create
	 * when the "getInstance(...)" method is called. Note that this method also
	 * utilizes the TimelineTool to determine the click-through point to see which
	 * editPart objects are overlapping.
	 *
	 * @return a list of violation trackers for the given editPart and any associated
	 * overlapping edit parts.
	 */
	protected static List<ViolationTracker> getViolationTrackers(ScaleTimelineMarkerEditPart
			scaleTimelineMarkerEditPart)
	{

		PlanAdvisorMember planAdvisorMember = getPlanAdvisorMember(scaleTimelineMarkerEditPart);
		List<ViolationTracker> allViolationTrackers = planAdvisorMember.getViolationTrackers();

		/*
		 *  go through all the edit parts and find the ones that have a matching
		 *  IMarker and violationTracker pair.
		 */
		List<EditPart> editParts = 
				ScaleTimelineMarkerTooltip.getOverlappingEditParts(scaleTimelineMarkerEditPart);
		List<ViolationTracker> violationTrackers = new ArrayList<ViolationTracker>();
		for (EditPart editPart : editParts) {
			Object model = editPart.getModel();
			IMarker marker = getMarker(model, scaleTimelineMarkerEditPart.getViewer());
			if (marker != null) {
				/*
				 * found an IMarker for a given TimelineMarker. The viewer is
				 * necessary to get the FileResourceMarkerService, which
				 * contains information about IMarker to TimelineMarker mappings.
				 */
				Violation violation = planAdvisorMember.getMarkerViolation(marker);
				if (violation == null) {
					continue;
				}
				// check if the sourceId matches the violationId, just to be safe
				for (ViolationTracker violationTracker : allViolationTrackers) {
					Violation trackerViolation = violationTracker.getViolation();
					if (violation.equals(trackerViolation)) {
						violationTrackers.add(violationTracker);
					}
				}
			}
		}
		return violationTrackers;
	}
	
	private static PlanAdvisorMember getPlanAdvisorMember(ScaleTimelineMarkerEditPart scaleTimelineMarkerEditPart) {
		TimelineViewer timelineViewer = scaleTimelineMarkerEditPart.getViewer();
		PlanTimeline planTimeline = (PlanTimeline) timelineViewer.getTimeline();
		EPlan plan = planTimeline.getPlan();
		return PlanAdvisorMember.get(plan);
	}

	protected abstract Composite getTitleComposite();
	protected abstract Composite getBodyComposite();
	protected abstract Composite getSuggestionsComposite();

	protected Label getTitleBodySeparatorLabel()
	{
		if(titleBodySeparatorLabel == null) {
			titleBodySeparatorLabel = new Label(mainComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
			titleBodySeparatorLabel.setLayoutData(new TableWrapData(TableWrapData.FILL));
		}

		return titleBodySeparatorLabel;
	}

	protected Composite getMainComposite() {
		if(this.mainComposite != null)
			return this.mainComposite;

		mainComposite = new Composite(shell, SWT.NONE);

		mainComposite.addMouseTrackListener(new MouseTrackAdapter() {
			@Override
			public void mouseExit(MouseEvent e) {
				// mouse leaves the tooltip, dispose the tooltip
				if (!mainComposite.getBounds().intersects(e.x, e.y, 1, 1)) {
					shell.setVisible(false);
				}

				// check if parent should be disposed
				if (groupScaleTimelineMarkerTooltip != null
						&& !groupScaleTimelineMarkerTooltip.shell.isDisposed()
						&& !shell.isVisible()) {

					Point point = new Point(e.x, e.y);
					point = shell.toDisplay(point);
					point = groupScaleTimelineMarkerTooltip.getMainComposite().toControl(point);
					Rectangle bounds = groupScaleTimelineMarkerTooltip.getMainComposite().getBounds();
					if (!bounds.intersects(point.x, point.y, 1, 1)) {
						// child tooltip disposed and mouse no longer over tooltip
						groupScaleTimelineMarkerTooltip.shell.dispose();
					}
				}
				if (!shell.isDisposed() && !shell.isVisible()) {
					shell.dispose();
				}
			}
		});

		mainComposite.setBackground(shell.getBackground());
		mainComposite.setLayout(new TableWrapLayout());

		return mainComposite;
	}

	protected static IMarker getMarker(Object model
			, TimelineViewer timelineViewer)
	{
		if(model instanceof TimelineMarker)
		{
			TimelineMarker timelineMarker = (TimelineMarker)model;
			PlanTimeline planTimeline = (PlanTimeline) timelineViewer.getTimeline();
			FileResourceMarkerService fileResourceMarkerService
				= planTimeline.getFileResourceMarkerService();
			return fileResourceMarkerService.getMarker(timelineMarker);
		}

		return null;
	}

	protected synchronized ResourceManager getResourceManager() {
		if (resourceManager == null) {
			resourceManager = new LocalResourceManager(JFaceResources.getResources());
		}
		return resourceManager;
	}

	private static class PartListener implements IPartListener {

		public PartListener() {
			super();
			IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();			
			activePage.addPartListener(this);
		}

		@Override
		public void partClosed(IWorkbenchPart part) {
			resourceManager = null;
			solitaryScaleTimelineMarkerTooltip = null;
			groupScaleTimelineMarkerTooltip = null;
		}

		@Override
		public void partActivated(IWorkbenchPart part) 		{ /* unimplemented */ }
		@Override
		public void partBroughtToTop(IWorkbenchPart part) 	{ /* unimplemented */ }
		@Override
		public void partDeactivated(IWorkbenchPart part) 	{ /* unimplemented */ }
		@Override
		public void partOpened(IWorkbenchPart part) 		{ /* unimplemented */ }	
	}	
}
