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
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.color.ColorUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class HeatMapFigure extends RectangleFigure {

	private List<HeatMapFigureData> data = null;
	private Map<String, Dimension> textExtentsCache = new HashMap<String, Dimension>();

	public List<HeatMapFigureData> getData() {
		return data;
	}

	public void setData(List<HeatMapFigureData> data) {
		this.data = data;
	}

	@Override
	public void paintFigure(Graphics graphics) {
		if (data == null) {
			return;
		}

		int fontHeight = FigureUtilities.getFontMetrics(getFont()).getHeight();
		Rectangle b = getBounds();
		Rectangle clip = TimelineUtils.getViewport(this).getClientArea();
		for (HeatMapFigureData d : data) {
			int x = d.getX();
			int width = d.getWidth();
			String text = d.getText();
			RGB rgb = d.getRgb();
			int alpha = d.getAlpha();
			
			Rectangle r = new Rectangle(b.x + x, b.y + 1, width, b.height - 3);
			if (clip.intersects(r)) {
				graphics.pushState();
				graphics.setAlpha(alpha);
				if (rgb != null) {
					Color color = ColorMap.RGB_INSTANCE.getColor(rgb);
					boolean brightColor = ColorUtils.isBrightColor(color.getRGB());
					graphics.setForegroundColor(ColorConstants.black);
					graphics.setBackgroundColor(color);
					graphics.fillRectangle(r);
					
					if (d.isShowOutline())
						graphics.drawRectangle(r);
					
					if (text != null) {
						graphics.setForegroundColor(brightColor ? ColorConstants.black : ColorConstants.white);
						Dimension extents = getTextExtents(text);
						int textWidth = extents.width;
						x = r.x + (r.width - textWidth)/2;
						int l_bound = clip.x;
						if (x < l_bound) {
							x = l_bound;
						}
						int r_bound = Math.max(b.x + d.getX(), clip.getRight().x - textWidth);
						if (x > r_bound) {
							x = r_bound;
						}
						Rectangle textClip = new Rectangle(b.x + d.getX(), b.y, width, b.height);
						graphics.setClip(textClip);
						int y = r.y + (r.height - fontHeight)/2;
						graphics.drawText(text, x, y);
					}
				}
				graphics.popState();
			}
		}
	}

	private Dimension getTextExtents(String text) {
		Dimension extents = textExtentsCache.get(text);
		if (extents == null) {
			extents = FigureUtilities.getTextExtents(text, getFont());
			textExtentsCache.put(text, extents);
		}
		return extents;
	}
	
}
