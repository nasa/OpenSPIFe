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

import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public class ChartFigureLegendLayer extends Layer {

	public static final String ID = "Chart Data Legend Layer";
	
	private String minText = null;
	private String maxText = null;
	
	public void setMinText(String minText) {
		this.minText = minText;
	}

	public void setMaxText(String maxText) {
		this.maxText = maxText;
	}

	@Override
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);

		Dimension minExtents = minText==null ? Dimension.SINGLETON : FigureUtilities.getTextExtents(minText, getFont());
		Dimension maxExtents = maxText==null ? Dimension.SINGLETON : FigureUtilities.getTextExtents(maxText, getFont());

		int maxWidth = Math.max(minExtents.width, maxExtents.width);
		
		if(minText != null || maxText != null) {
			// float the legend by adjusting for the viewport location
			
			Viewport viewport = TimelineUtils.getViewport(this);
			Point point = viewport.getViewLocation();
						
			int x = bounds.x + point.x;
			if (minText != null) {												
				drawLegend(g, minText, x, bounds.y + bounds.height - minExtents.height - 3, maxWidth);	
			}
			
			if (maxText != null) {			
				drawLegend(g, maxText, x, bounds.y + 1, maxWidth);
			}
			
		}

	}

	private void drawLegend(Graphics g, String text, int x, int y, int width) {
		Dimension extents = FigureUtilities.getTextExtents(text, getFont());
		Rectangle textBorder = new Rectangle(x, y, width+2, extents.height+2);
		try { g.setAlpha(192); } catch (Exception e) {/* fail silently */}
		g.fillRectangle(textBorder);
		try { g.setAlpha(255); } catch (Exception e) {/* fail silently */}
		g.setForegroundColor(ColorConstants.red);
		g.setLineWidth(1);
		g.setLineStyle(SWT.LINE_DOT);
		g.drawRectangle(textBorder);
		g.setForegroundColor(ColorConstants.black);
		g.drawText(text, new Point(textBorder.x + textBorder.width - extents.width - 1, textBorder.y + 1));
	}
	
}
