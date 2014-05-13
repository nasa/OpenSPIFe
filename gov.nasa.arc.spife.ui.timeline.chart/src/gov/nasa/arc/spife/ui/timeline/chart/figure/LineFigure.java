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
package gov.nasa.arc.spife.ui.timeline.chart.figure;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

public class LineFigure extends Polyline {

	public static final Rectangle CLIP_RECTANGLE = new Rectangle();

	private boolean drawShadow = false;
	private int[] intArray = null;
	private PrecisionPointList norms;

	public LineFigure() {
		addFigureListener(new FigureListener() {
			@Override
			public void figureMoved(IFigure source) {
				if (norms != null) {
					setNormalizedPointList(norms);
				}
			}
		});
	}
	
	public void setNormalizedPointList(PrecisionPointList norms) {
		this.norms = norms;
		// dimensions
		Dimension parentSize = getParent().getSize();
		int parentY = (int) (parentSize.height * ChartFigure.DESIGN_BOUNDARY_PERCENTAGE);
		int parentHeight = (int) (parentSize.height - 2*parentSize.height*ChartFigure.DESIGN_BOUNDARY_PERCENTAGE);
		PointList pointList = new PointList();
		int length = norms.size();
		int previous_x = Integer.MIN_VALUE;
		int previous_y = Integer.MIN_VALUE;
		for (int i=0; i<length; i++) {
			PrecisionPoint pt = (PrecisionPoint) norms.getPoint(i);
			int x = Math.max(0, (int)pt.preciseX());
			int y = (int)((parentHeight * (1.0 - pt.preciseY())) + parentY);
			if ((x != previous_x) || (y != previous_y)) {
				pointList.addPoint(x, y);
				previous_x = x;
				previous_y = y;
			}
		}
		setPoints(pointList);
		intArray = pointList.toIntArray();
	}
	
	public PrecisionPointList getNormalizedPointList() {
		return (PrecisionPointList) getPoints();
	}

	@Override
	protected void outlineShape(Graphics g) {
		Point location = getParent().getBounds().getLocation();
		g.translate(location.x, location.y);
		
		int[] array = clipArray(g, intArray);
		int w = getLineWidth();
		int o = -1*w/2;
		g.setLineWidth(w);
		
		if (drawShadow) {
			g.translate(w, w);
			g.setForegroundColor(ColorConstants.lightGray);
			g.drawPolyline(array);
			
			g.translate(o,o);
			g.setForegroundColor(ColorConstants.gray);
			g.drawPolyline(array);
		}
			
		g.translate(o,o);
		g.setForegroundColor(getForegroundColor());
		g.drawPolyline(array);
		
//		int length = pointList.size();
//		for (int i=0; i<length; i++) {
//			Point pt = pointList.getPoint(i);
//			g.drawRectangle(pt.x-2, pt.y-2, 5, 5);
//		}
	}

	private int[] clipArray(Graphics g, int[] inputArray) {
		g.getClip(CLIP_RECTANGLE);
		int left = CLIP_RECTANGLE.x;
		int bottom = CLIP_RECTANGLE.y;
		int top = bottom + CLIP_RECTANGLE.height;
		int right = left + CLIP_RECTANGLE.width;
		int startIndex = 0;
		for (int i = 0 ; i < inputArray.length ; i += 2) {
			int x = inputArray[i];
			int y = inputArray[i+1];
			if (x >= left) {
				if ((y >= bottom) && (y <= top)) {
					startIndex = i;
					break;
				}
			}
		}
		if (startIndex > 0) {
			startIndex -= 2; // get one point outside the clip region
		}
		int stopIndex = inputArray.length - 2;
		for (int i = inputArray.length - 2 ; i >= 0 ; i -= 2) {
			int x = inputArray[i];
			int y = inputArray[i+1];
			if (x <= right) {
				if ((y >= bottom) && (y <= top)) {
					stopIndex = i;
					break;
				}
			}
		}
		if (stopIndex < inputArray.length - 2) {
			stopIndex += 2; // get one point outside the clip region
		}
		if ((startIndex == 0) && (stopIndex >= inputArray.length - 2)) {
			return inputArray;
		}
		int count = stopIndex - startIndex + 2;
		if (count <= 0) {
			return new int[0];
		}
		int clippedArray[] = new int[count];
		System.arraycopy(inputArray, startIndex, clippedArray, 0, count);
		return clippedArray;
	}
	
}
