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

import gov.nasa.ensemble.common.ui.color.ColorMap;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class ChartFigure extends RectangleFigure {
	
	private static final Rectangle CLIP_RECTANGLE = new Rectangle();
	private static final Color DESIGN_BOUNDARY_COLOR = ColorMap.RGB_INSTANCE.getColor(new RGB(252, 222, 224));
	public static final float DESIGN_BOUNDARY_PERCENTAGE = 0.05f;

	@Override
	public void paintFigure(Graphics g) {
		Rectangle clip = g.getClip(CLIP_RECTANGLE);
		Rectangle r = getBounds().getCopy();
		
		int x1 = Math.max(r.x, clip.x);
		int y1 = r.y;
		int w = Math.min(r.width, clip.width);
		int h = (int)(r.height * DESIGN_BOUNDARY_PERCENTAGE);
		
		g.pushState();
		g.setBackgroundColor(DESIGN_BOUNDARY_COLOR);
		
		g.fillRectangle(x1, y1, w, h);
		g.fillRectangle(x1, y1 + r.height - h, w, h);
		
		g.setForegroundColor(ColorConstants.red);
		g.setLineStyle(SWT.LINE_DASH);
		g.setLineWidth(1);
		g.drawLine(x1, y1 + h, x1 + w, y1 + h);
		g.drawLine(x1, y1 + r.height - h, x1 + w, y1 + r.height - h);
		
		g.popState();
	}
	
}
