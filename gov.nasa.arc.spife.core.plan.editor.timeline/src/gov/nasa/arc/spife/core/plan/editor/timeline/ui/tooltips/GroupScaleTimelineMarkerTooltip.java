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

import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.arc.spife.ui.timeline.part.ScaleTimelineMarkerEditPart;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.editor.MarkerConstants;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Class for displaying information about multiple timeline markers at once
 * @author Eugene Turkov
 *
 */
public class GroupScaleTimelineMarkerTooltip extends ScaleTimelineMarkerTooltip {
	
	/*
	 * Here we keep track of the number of different types of images given the
	 * available violations. Since only one violation is associated with each
	 * editPart, we use a utility method to determine the other violations that
	 * graphically overlap with this one.
	 */
	private HashMap<Image, Integer> imageToCountMap;
	
	private List<ViolationTracker> violationTrackers;
	private MouseMoveListener mouseMoveListener;
	private Composite violationsComposite;
	
	protected GroupScaleTimelineMarkerTooltip(Display display, int style,
			ScaleTimelineMarkerEditPart scaleTimelineMarkerEditPart) {
		super(display, style, scaleTimelineMarkerEditPart, null);
		// passing in null as the violationTracker
		
		// add a mouse move listener, to clear out a child violation when applicable
		mouseMoveListener = new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				if (solitaryScaleTimelineMarkerTooltip != null
						&& !solitaryScaleTimelineMarkerTooltip.shell.isDisposed()) {
					Composite localComposite = GroupScaleTimelineMarkerTooltip.this.getMainComposite();
					Point displayPoint = localComposite.toDisplay(e.x, e.y);
					displayPoint = solitaryScaleTimelineMarkerTooltip.shell.toControl(displayPoint);
					if (!solitaryScaleTimelineMarkerTooltip.shell.getBounds().contains(displayPoint)) {
						// navigated off a child, onto the parent
						solitaryScaleTimelineMarkerTooltip.shell.dispose();
					}
				}
			}
		};

		this.mainComposite.addMouseMoveListener(mouseMoveListener);
		Control[] controls = violationsComposite.getChildren();
		for(Control control : controls) {
			control.addMouseMoveListener(mouseMoveListener);
		}
	}
	
	@Override
	protected Composite getTitleComposite() {
		titleComposite = new Composite(mainComposite, SWT.NONE);

		violationTrackers
			= super.getViolationTrackers(scaleTimelineMarkerEditPart);
		
		List<Image> images = getAssociatedScaleTimelineMarkerImages(super
				.getOverlappingEditParts(scaleTimelineMarkerEditPart));
		
		// pairs of image and text, followed by a text field indicating total
		// for example: <icon0>:N <icon1>:M Total:X
		GridLayout layout = new GridLayout(images.size() * 2 + 1, false);
		
		layout.horizontalSpacing = 0;
		layout.marginHeight = 0;
		titleComposite.setLayout(layout);
		titleComposite.setBackground(shell.getBackground());
		
		for(Image image : images)
		{
			Label imageLabel = new Label(titleComposite, SWT.NONE);
			imageLabel.setBackground(shell.getBackground());
			imageLabel.setImage(image);

			Label imageLabelText = new Label(titleComposite, SWT.NONE);
			imageLabelText.setBackground(shell.getBackground());

			String imageLabelTextString = ":"
				+ getImageToCountMap().get(image).toString() + " ";

			imageLabelText.setText(imageLabelTextString);
		}
		
		Label textLabel = new Label(titleComposite, SWT.NONE);
		textLabel.setBackground(shell.getBackground());
		textLabel.setText(" Total: " + violationTrackers.size());		
		textLabel.setFont(FontUtils.getSystemBoldFont());
		
		return titleComposite;
	}

	@Override
	protected Composite getBodyComposite() {
		violationsComposite = new Composite(mainComposite, SWT.NONE);
		violationsComposite.setBackground(shell.getBackground());
		int imageTextPair = 2; // each violation has an icon and text
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = imageTextPair;
		violationsComposite.setLayout(layout);
		
		FormToolkit toolkit = new FormToolkit(mainComposite.getDisplay());
		for(ViolationTracker violationTracker : violationTrackers) {
			Image image = this.getImage(violationTracker);
			Label imageLabel = toolkit.createLabel(violationsComposite, "");
			imageLabel.setBackground(shell.getBackground());
			imageLabel.setImage(image);
			ViolationLabelMouseListener violationImageLabelMouseListener
				= new ViolationLabelMouseListener(violationTracker, imageLabel);
			imageLabel.addMouseListener(violationImageLabelMouseListener);
			String violationText = StringEscapeUtils.unescapeXml( 
					violationTracker.getViolation()
					.getFormText(null, identifiableRegistry)).replaceAll("&", "&&");
			violationText = violationText.replaceAll("<[^<]*>", "");
			violationText = violationText.replaceAll("&quot;", "\"");
			final Hyperlink link = toolkit.createHyperlink(violationsComposite, violationText, SWT.WRAP);
			link.setBackground(shell.getBackground());
			link.setForeground(ColorConstants.blue);
			TableWrapData layoutData = new TableWrapData(TableWrapData.FILL);
			if (image == null) {
				layoutData.colspan = 2;
			}
			layoutData.maxWidth = TOOLTIP_WIDTH;
			link.setLayoutData(layoutData);
			final ViolationTracker linkTracker = violationTracker;
			link.addHyperlinkListener(new HyperlinkAdapter() {

				@Override
				public void linkEntered(HyperlinkEvent e) {
					link.setFocus();
				}

				@Override
				public void linkActivated(HyperlinkEvent e) {
					try
					{
						ScaleTimelineMarkerTooltip childTooltip
							= solitaryScaleTimelineMarkerTooltip;
						
						if(childTooltip != null) {
							if(!childTooltip.shell.isDisposed())
								childTooltip.shell.dispose();
						}
						
						childTooltip
							= ScaleTimelineMarkerTooltip
								.getInstance(scaleTimelineMarkerEditPart
								, linkTracker);
					
						Point point = link.toDisplay(0, 0);
						childTooltip.shell.setLocation(point);
						childTooltip.shell.setVisible(true);
					}
					
					catch(CoreException coreException)
					{
						Logger.getLogger(GroupScaleTimelineMarkerTooltip.class)
						.error("trying to show tooltip for selected violation"
								, coreException);
					}
				}
				
			});
		}
		
		if(violationTrackers.size() == 0) {
			IMarker marker = getMarker(scaleTimelineMarkerEditPart.getModel()
					, scaleTimelineMarkerEditPart.getViewer());
			final Label label = new Label(violationsComposite, SWT.WRAP);
			TableWrapData layoutData = new TableWrapData(TableWrapData.FILL);
			layoutData.maxWidth = TOOLTIP_WIDTH;
			layoutData.colspan = imageTextPair;
			label.setLayoutData(layoutData);
			try {
				Object attribute = marker.getAttribute(IMarker.MESSAGE);
				label.setText(attribute.toString());
				label.setBackground(shell.getBackground());
			} catch (CoreException e) {
				LogUtil.error(e);
			}
		}
		
		return violationsComposite;		
	}

	@Override
	protected Composite getSuggestionsComposite() {
		// no suggestions yet for multiple markers
		return null;
	}
	
	private class ViolationLabelMouseListener extends MouseAdapter
	{
		private ViolationTracker violationTracker;
		private Label label;
		public ViolationLabelMouseListener(ViolationTracker violationTracker
				, Label label)
		{
			this.violationTracker = violationTracker;
			this.label = label;
		}
		
		@Override
		public void mouseUp(MouseEvent e) {
			try
			{
				ScaleTimelineMarkerTooltip childTooltip
					= solitaryScaleTimelineMarkerTooltip;
				
				if(childTooltip != null) {
					if(!childTooltip.shell.isDisposed())
						childTooltip.shell.dispose();
				}
				
				childTooltip
					= ScaleTimelineMarkerTooltip
						.getInstance(scaleTimelineMarkerEditPart
						, violationTracker);
			
				Point point = label.toDisplay(e.x, e.y);
				childTooltip.shell.setLocation(point);
				childTooltip.shell.setVisible(true);
			}
			
			catch(CoreException coreException)
			{
				Logger.getLogger(GroupScaleTimelineMarkerTooltip.class)
				.error("trying to show tooltip for selected violation"
						, coreException);
			}
		}		
	}
	
	protected HashMap<Image, Integer> getImageToCountMap()
	{
		if(this.imageToCountMap == null)
		{
			List<EditPart> editParts = ScaleTimelineMarkerTooltip
				.getOverlappingEditParts(scaleTimelineMarkerEditPart);
			
			getAssociatedScaleTimelineMarkerImages(editParts);
		}
		
		return imageToCountMap;
	}
	
	/**
	 * Given the list of edit part objects, this method should return the images
	 * associated with those edit parts that represent instances of
	 * ScaleTimelineMarkerHeaderEditPart
	 * 
	 * During operation, this method also counts how many images of the same
	 * type are present in the edit part list, and keeps track of the information.
	 * 
	 * @param editParts
	 * @return a list of images associated with the edit parts
	 * @throws CoreException
	 */
	protected List<Image> getAssociatedScaleTimelineMarkerImages(List<EditPart> editParts)
	{
		if(imageToCountMap == null)
			imageToCountMap = new HashMap<Image, Integer>();
		
		imageToCountMap.clear();
		
		List<Image> images = new ArrayList<Image>();
		try
		{
			for(EditPart editPart : editParts)
			{
				Object object = editPart.getModel();
				if(object instanceof TimelineMarker)
				{
					TimelineMarker timelineMarker = (TimelineMarker)object;
					IMarker marker = getMarker(timelineMarker
							, ((ScaleTimelineMarkerEditPart)editPart).getViewer());
					
					String markerPluginId;
	
					markerPluginId = String.valueOf(marker
							.getAttribute(MarkerConstants.PLUGIN_ID));
	
					String imageDescriptorPath = null;
					Object path = marker.getAttribute(MarkerConstants.IMAGE_DESCRIPTOR_PATH);
					if(path != null) {
						imageDescriptorPath = String.valueOf(path);
					} 
					
					ImageDescriptor imageDescriptor = null;
					if(imageDescriptorPath != null) {
						imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(markerPluginId, imageDescriptorPath);
					}
					
					if(imageDescriptor == null) {
						IWorkbenchAdapter adapter = (IWorkbenchAdapter) Platform.getAdapterManager().getAdapter(marker, IWorkbenchAdapter.class);
						if (adapter != null) {
							imageDescriptor = adapter.getImageDescriptor(marker);
						}
					}
					Image image = (Image) getResourceManager().get(imageDescriptor);
					
					Integer integer = imageToCountMap.get(image);
					if(integer == null) {
						imageToCountMap.put(image, 1);
					}
					
					else {
						imageToCountMap.put(image, imageToCountMap.get(image) + 1);
					}
					
					if(!images.contains(image)) {
						images.add(image);
					}
				}
			}
		}
		
		catch(CoreException e)
		{
			Logger.getLogger(ScaleTimelineMarkerTooltip.class).error(
					"getting associated scale timeline marker images", e);
			images = null;
		}
		
		return images;
	}
}
