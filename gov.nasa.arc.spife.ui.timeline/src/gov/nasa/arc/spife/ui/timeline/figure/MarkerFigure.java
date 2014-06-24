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
package gov.nasa.arc.spife.ui.timeline.figure;

import gov.nasa.arc.spife.ui.timeline.model.TimelineMarker;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.color.ColorUtils;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class MarkerFigure extends RectangleFigure {

	protected TimelineMarker timelineMarker;
	
	public MarkerFigure(TimelineMarker timelineMarker) {
		this.timelineMarker = timelineMarker;
	}
	
	@Override
	public void fillShape(Graphics g) {
		if (timelineMarker.isInstantaneous()) {
			return;
		}
		// Alpha will have no effect on instantaneous markers
		if(timelineMarker.getLineStyle() == SWT.LINE_SOLID) { 
			g.setAlpha(125);
		}
		super.fillShape(g);
	}

	@Override
	protected void outlineShape(Graphics g) {
		if (timelineMarker.isInstantaneous()) {
			Rectangle r = getBounds();
			g.setLineStyle(timelineMarker.getLineStyle());
			g.setLineWidth(3);
			g.drawLine(r.getTopLeft(), r.getBottomLeft());
		} else if(timelineMarker.getLineStyle() == SWT.LINE_SOLID) {
			g.setLineStyle(timelineMarker.getLineStyle());
		} else {
			g.setAlpha(255);
			g.setLineStyle(timelineMarker.getLineStyle());
			Rectangle r = getBounds();
			int x = r.x + lineWidth / 2;
			int y = r.y + lineWidth / 2;
			int w = r.width - Math.max(1, lineWidth);
			int h = r.height - Math.max(1, lineWidth);
			g.drawLine(x, y, x, y + h);
			g.drawLine(x + w, y, x + w, y + h);
		}
	}
	
	@Override
	public void setBackgroundColor(Color bg) {
		Color resultColor = bg;
		if (bg != null) {
			RGB rgb = bg.getRGB();
			if(((rgb.red * 299) + (rgb.green * 587) + (rgb.blue * 114)) / 1000 > 179){
				RGB darkerColor = ColorUtils.darkerColor(rgb, 0.7);
				resultColor = ColorMap.RGB_INSTANCE.getColor(darkerColor);
			}
		}
		super.setBackgroundColor(resultColor);
	}

	@Override
	public void setForegroundColor(Color fg) {
		Color resultColor = fg;
		if (fg != null) {
			RGB rgb = fg.getRGB();
			if(((rgb.red * 299) + (rgb.green * 587) + (rgb.blue * 114)) / 1000 > 179){
				RGB darkerColor = ColorUtils.darkerColor(rgb, 0.7);
				resultColor = ColorMap.RGB_INSTANCE.getColor(darkerColor);
			}
		}
		super.setForegroundColor(resultColor);
	}	

}
