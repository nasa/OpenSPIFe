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

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.model.Tick;
import gov.nasa.arc.spife.ui.timeline.model.TickManager;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.ui.color.ColorMap;

import java.util.Calendar;
import java.util.SortedSet;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class TickIntervalsFigure extends Figure {

	protected int buffer = 5;

	private boolean disableErrorLogging; // one is enough

	protected TickManager tickManager;

	private Calendar calendar = MissionConstants.getInstance().getMissionCalendar();
	private Color verticalLineColor;
 
	@Override
	protected void paintFigure(Graphics g) {
		if (isOpaque()) {
			Rectangle bounds = new Rectangle(getBounds()).intersect(g.getClip(new Rectangle()));
			g.fillRectangle(bounds);
		}
		
		super.paintBorder(g);
		try {
			g.pushState();
			paintTickIntervals(g);
		} catch (Exception e) {
			if (!disableErrorLogging) {
				LogUtil.error(e);
			}
			disableErrorLogging = true;
		} finally {
			g.popState();
		}
	}
	
	@Override
	protected void paintBorder(Graphics graphics) {
		// no border to paint since the border draws over the ticks
	}

	protected void paintTickIntervals(Graphics g) {
		if (tickManager == null) {
			return;
		}
		Viewport viewport = TimelineUtils.getViewport(this);
		
		int scrollbarPosition = viewport.getHorizontalRangeModel().getValue();
		int optimizeClipLeft = scrollbarPosition - 100;
		int optimizeClipRight = scrollbarPosition + viewport.getBounds().width + 100;
		
		SortedSet<Tick> ticks = tickManager.getTicks(calendar, g);
		
		g.setLineStyle(SWT.LINE_DOT);
		g.setForegroundColor(ColorConstants.black);
		paintIntervals(g, ticks, optimizeClipLeft, optimizeClipRight);
	}
	
	protected void paintIntervals(Graphics g, SortedSet<Tick> ticks, int optimizeClipLeft, int optimizeClipRight) {
		if (tickManager == null) {
			return;
		}

		Rectangle clip = g.getClip(new Rectangle());
		// Set vertical line color
		Color color = getVerticalLineColor();
		g.setForegroundColor(color);
		for(Tick tick: ticks) {
			int x = tick.getPosition();
			if (x > optimizeClipRight) return;
			if (x >= optimizeClipLeft) {
				int x0 = bounds.x+x;
				if (x0 + 50 < clip.x) {
					continue;
				} else if (x0 - 50 > clip.x + clip.width) {
					break;
				}
				g.setLineWidth(tick.isMajor() ? 3 : 1);
				g.drawLine(x0, clip.y - 1, x0, clip.y + clip.height);
			}
		}
	}

	public void setTickManager(TickManager tickManager) {
		this.tickManager = tickManager;
	}
	
	public void setVerticalLineColor(Color verticalLineColor) {
		this.verticalLineColor = verticalLineColor;
		repaint();
		this.requestFocus();
	}
	
	/**
	 * Gets the preference page Vertical Line color
	 * @return
	 */
	protected Color getVerticalLineColor() {
		String rgbString = TimelineConstants.TIMELINE_PREFERENCES.getString(TimelinePreferencePage.P_VERTICAL_LINES_COLOR);
		RGB rgb = StringConverter.asRGB(rgbString);
		verticalLineColor = ColorMap.RGB_INSTANCE.getColor(rgb);
		return verticalLineColor;
	}
}
