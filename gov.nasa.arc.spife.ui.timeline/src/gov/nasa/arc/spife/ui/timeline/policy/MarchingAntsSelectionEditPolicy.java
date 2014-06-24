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
package gov.nasa.arc.spife.ui.timeline.policy;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.figure.BarFigure;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.color.ColorUtils;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class MarchingAntsSelectionEditPolicy extends SelectionHandlesEditPolicy {

	private static final int DEFAULT_LINE_THICKNESS = 3;
	
	private int lineThickness = DEFAULT_LINE_THICKNESS;
	private int margin = 1;
	protected int maxMarchSteps = DEFAULT_LINE_THICKNESS*2;
	
	public MarchingAntsSelectionEditPolicy() {
		this(DEFAULT_LINE_THICKNESS);
	}
	
	public MarchingAntsSelectionEditPolicy(int lineWidth) {
		this.lineThickness = lineWidth;
		this.margin = (int) Math.ceil(lineWidth/2.0);
	}
	
	@Override
	protected void addSelectionHandles() {
		removeSelectionHandles();
		IFigure layer = getLayer(TimelineConstants.LAYER_FEEDBACK_DATA);
		handles = createSelectionHandles();
		for (int i = 0; i < handles.size(); i++)
			layer.add((IFigure)handles.get(i));
	}
	
	@Override
	protected List createSelectionHandles() {
		return Collections.singletonList(new MarqueeRectangleFigure((GraphicalEditPart) getHost()));
	}
	
	/**
	 * removes the selection handles from the selection layer.
	 */
	@Override
	protected void removeSelectionHandles() {
		if (handles == null)
			return;
		IFigure layer = getLayer(TimelineConstants.LAYER_FEEDBACK_DATA);
		for (int i = 0; i < handles.size(); i++)
			layer.remove((IFigure)handles.get(i));
		handles = null;
	}
	
	/**
	 * Class snagged from GEF Tool Selection Marquee. Adapted with a larger
	 * line weight, and a black background (white foreground) 
	 */
	private class MarqueeRectangleFigure extends MoveHandle {

		private static final int DELAY_BETWEEN_STEPS = 110; // animation delay in millisecond
		private int marchStep = 0;
		private boolean schedulePaint = true;
		private PointList[][] cachedEdges = null;
		
		private RGB borderRGB;
		private Color borderColor;
		private Runnable paintRunnable = null;
		private LineAttributes cachedLineAttributes;

		public MarqueeRectangleFigure(GraphicalEditPart owner) {
			super(owner);
		}
		
		@Override
		protected void initialize() {
			// no initialization
		}

		/**
		 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
		 */
		@Override
		protected void paintFigure(Graphics graphics) {
			graphics.pushState();
			IFigure hostFigure = getHostFigure();
			IFigure rootFigure = hostFigure;
			while (rootFigure.getParent() != null) {
				rootFigure = rootFigure.getParent();
			}			
			if (rootFigure.getClippingStrategy()==null) {
				rootFigure.setClippingStrategy(new MemorySavingClippingStrategy());
			}
			Color figureColor = null;
			if (hostFigure instanceof BarFigure) {
				figureColor = ((BarFigure)hostFigure).getNormalColorPalette();
			} else {
				figureColor = ColorConstants.white;
			}
			RGB inverseColor = ColorUtils.inverseColor(figureColor.getRGB());
			//current borderRGB exists and is not the correct one
			if (borderRGB != null && !borderRGB.equals(inverseColor)) {
				// make it correct, get rid of the old color
				// borderColor.dispose();
				borderColor = null;
				borderRGB = null;
			}
			if (borderRGB == null) {
				borderRGB = inverseColor;				
				borderColor = ColorMap.RGB_INSTANCE.getColor(inverseColor);				
			}
			graphics.setForegroundColor(borderColor);
			graphics.setBackgroundColor(ColorConstants.white);
			graphics.setLineAttributes(getCachedLineAttributes());
			for (PointList edges : getCachedEdges(marchStep)) {
				graphics.drawPolyline(edges);
			}
			if (schedulePaint) {
				if (paintRunnable == null) {
					paintRunnable = new Runnable() {
						@Override
						public void run() {
							marchStep = (marchStep+1)%maxMarchSteps;
							schedulePaint = true;
							repaint();
						}
					};
				}
				Display.getCurrent().timerExec(DELAY_BETWEEN_STEPS, paintRunnable);
			}
			schedulePaint = false;
			graphics.popState();
		}
		
		private LineAttributes getCachedLineAttributes() {
			if (cachedLineAttributes == null) {
				cachedLineAttributes = new LineAttributes(lineThickness);
				cachedLineAttributes.style = Graphics.LINE_DOT;
			}
			return cachedLineAttributes;
		}

		@Override
		public void ancestorMoved(IFigure ancestor) {
			super.ancestorMoved(ancestor);
			cachedEdges = null;
		}

		/**
		 * @param marchStep How far the march has gone, aka which animation frame, aka the offset in pixels.
		 * @return two point lists, defining the sides of the rectangle of ants
		 */
		private PointList[] getCachedEdges(int marchStep) {
			// Bounds will change on drag or zoom.
			// No need to make a copy and cause lots of GC, but note that 
			if (cachedEdges == null) {
				cachedEdges = new PointList[maxMarchSteps][2];
				for (int step=0; step < maxMarchSteps; step++) {
					cachedEdges[step] = getEdges(step);
				}
			}	
			return cachedEdges[marchStep%maxMarchSteps];
		}
		
		/**
		 * @param marchStep How far the march has gone, aka which animation frame, aka the offset in pixels.
		 * @param bounds 
		 * @return two point lists, defining the sides of the rectangle of ants
		 */
		private PointList[] getEdges(int marchStep) {

			PointList topAndRightEdges = new PointList();
			PointList bottomAndLeftEdges = new PointList();

			// Top ants head left to right, right-edge ants head down.  (Clockwise.)
			topAndRightEdges.addPoint(marchStep				   , margin);
			topAndRightEdges.addPoint(bounds.width - margin + 1, margin);
			topAndRightEdges.addPoint(bounds.width - margin + 1, bounds.height - margin);
			topAndRightEdges.translate(bounds.x-1, bounds.y-1);
			
			// Left ants head up, bottom ants head right to left.  (Clockwise.)
			int reverse = (-marchStep)%maxMarchSteps;
			bottomAndLeftEdges.addPoint(margin 					, reverse);
			bottomAndLeftEdges.addPoint(margin 					, bounds.height - margin);
			bottomAndLeftEdges.addPoint(bounds.width - margin + 1, bounds.height - margin);
			bottomAndLeftEdges.translate(bounds.x-1, bounds.y-1);
			
			return new PointList[]{topAndRightEdges, bottomAndLeftEdges};
		}

	}
	
	
}


