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

import gov.nasa.arc.spife.ui.timeline.model.Tick;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;

public class ScaleFigure extends TickIntervalsFigure {

	private static final int PAD_BETWEEN_LABELS = 2;
	private Color alternatingRowColor;
	private Color tickmarkColor;
	
	public List<DateFormat> dateFormats = new ArrayList<DateFormat>();

	private Map<String, Integer> textExtentsCache = new HashMap<String, Integer>();

	public void setDateFormats(List<DateFormat> dateFormats) {
		this.dateFormats = dateFormats;
		repaint();
	}
	
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		textExtentsCache.clear();
	}
	
	private Color getAlternatingRowColor() {
		if (alternatingRowColor == null) {
			return ColorConstants.lightGray;
		}
		return alternatingRowColor;
	}
	
	private Color getTickmarkColor() {
		if (tickmarkColor == null) {
			return ColorConstants.lightGray;
		}
		return tickmarkColor;
	}
	
	public void setAlternatingRowColor (Color alternatingRowColor) {
		this.alternatingRowColor = alternatingRowColor;
		repaint();
	}

	public void setTickmarkColor (Color tickmarkColor) {
		this.tickmarkColor = tickmarkColor;
		repaint();
	}
	
	@Override
	protected void paintTickIntervals(Graphics g) {
		FontMetrics fm = FigureUtilities.getFontMetrics(getFont());
		int fontHeight = fm.getHeight();
		int y = bounds.y + fontHeight;
		for (int i = 1 ; i < dateFormats.size() ; i++) {
			if (i % 2 == 1) {
				g.setBackgroundColor(getAlternatingRowColor());
				g.fillRectangle(new Rectangle(bounds.x, bounds.y + i*fontHeight + fontHeight, bounds.width, fontHeight));
			}
		}
		if (tickManager == null) { // no tick manager for the ScaleHeaderFigure
			return;
		}
		// The scale tickmark color
		g.setForegroundColor(getTickmarkColor());
		boolean first = true;
		int text_y = y;
		int dateFormatIndex = 0;
		String symbolicFontName = getFont().toString();
		Font boldFont = FontUtils.FONT_REGISTRY_INSTANCE.getBold(symbolicFontName);
		Viewport viewport = TimelineUtils.getViewport(this);
		int scrollbarPosition = viewport.getHorizontalRangeModel().getValue();
		Rectangle clip = viewport.getClientArea();
		final TextLayout layout = new TextLayout(WidgetUtils.getDisplay());
		TextStyle style = null;
		Font font = null;
		Color backgroundColor = null;
		for (DateFormat dateFormat : dateFormats) {
			String previousDateLabel = null;
			if (first) {
				font = boldFont;
				g.setFont(font);
			} else {
				font = getFont();
				g.setFont(font);
			}
			first = false;
			if (dateFormatIndex % 2 == 1) {
				backgroundColor = getAlternatingRowColor();
				g.setBackgroundColor(backgroundColor);
			} else {
				backgroundColor = ColorConstants.white;
				g.setBackgroundColor(backgroundColor);
			}
			int endOfPreviousLabel = -1;
			Calendar calendar = dateFormat.getCalendar();
			SortedSet<Tick> tickList = tickManager.getTicks(calendar, g);
			for (Tick tick : tickList) {
				int tickX = tick.getPosition();
				if (tickX < scrollbarPosition) continue; // optimization
				if (tickX > scrollbarPosition + clip.width) break; // optimization
				tickX += bounds.x;
				if (tickX > endOfPreviousLabel + PAD_BETWEEN_LABELS) { // Skip labeling anything that starts before the end of this text.
					
					g.drawLine(tickX,text_y,tickX,clip.height);
					String dateLabel = tick.getTickDateLabel();
					String label = dateLabel;
					if (previousDateLabel == null // only when we get to midnight
							|| label.equals(previousDateLabel)) {
						label = tick.getTickTimeLabel();
					}
					previousDateLabel = dateLabel;
					int textWidth = getTextWidth(label);
					// Draw the label after clearing any tick marks it would overlap.
					g.fillRectangle(tickX+1, text_y, textWidth, fontHeight);
					// Set the tick measurement number labels to black
				    style = new TextStyle(font, ColorConstants.black, backgroundColor);
				    layout.setText(label);
				    layout.setStyle(style, 0, label.length()-1);
					g.drawTextLayout(layout, tickX+1, text_y);
					// Note where this label ends so that we don't overlap it with subsequent ones.
					endOfPreviousLabel = tickX+1 + textWidth;
				}
			}
			text_y += fontHeight;
			dateFormatIndex++;
		}
		
		SortedSet<Tick> topTicks = tickManager.getTicks(dateFormats.get(0).getCalendar(), g);
		
		if (!topTicks.isEmpty()) {
			Iterator<Tick> iterator = topTicks.iterator();
			Tick previousTick = iterator.next();
			while (iterator.hasNext()) {
				Tick tick = iterator.next(); 
				int previousX = previousTick.getPosition() + bounds.x;
				int currentX = tick.getPosition() + bounds.x;
				int x = 0;
				
				x = previousX + ((currentX - previousX) / 4);
				y = bounds.y + bounds.height;
				g.drawLine(x, y, x, y - fontHeight/2);
				
				x = previousX + ((currentX - previousX) / 2);
				y = bounds.y + bounds.height;
				g.drawLine(x, y, x, y - fontHeight);
				
				x = previousX + (3 * (currentX - previousX) / 4);
				y = bounds.y + bounds.height;
				g.drawLine(x, y, x, y - fontHeight/2);
				
				previousTick = tick;
			}
		}

		if (!topTicks.isEmpty()) {
			Tick previousTick = null;
			for (Tick tick : topTicks) {
				if (!tick.isMajor()) {
					continue;
				}
				if (previousTick != null) {
					paintMajorIntervalBubble(g, previousTick, tick, fontHeight);
				}
				previousTick = tick;
			}
		}
	}
	
	private int getTextWidth(String text) {
		Integer textWidth = textExtentsCache.get(text);
		if (textWidth == null) {
			textWidth = FigureUtilities.getTextExtents(text, getFont()).width;
			textExtentsCache.put(text, textWidth);
		}
		return textWidth;
	}
	
	private void paintMajorIntervalBubble(Graphics g, Tick previousTick, Tick nextTick, int fontHeight) {
		Rectangle clip = TimelineUtils.getViewport(this).getClientArea();
		
		int y = bounds.y;
		
		int x = nextTick.getPosition() + bounds.x;
		int previousX = previousTick.getPosition() + bounds.x;

		g.setForegroundColor(ColorConstants.black);
		g.setLineWidth(3);
		g.drawLine(x,y - fontHeight/2,x,clip.height);
		
		if (previousX != -1) {
			g.setLineWidth(1);
			g.setBackgroundColor(ColorConstants.lightGray);
			g.setForegroundColor(ColorConstants.black);
			
			Rectangle r = new Rectangle(previousX + 1, y, x - previousX - 2, fontHeight);
			g.fillRectangle(r);
			g.drawRectangle(r);
		}

		String text = previousTick.getBubbleText();
		if (text != null) {
			g.setForegroundColor(ColorConstants.black);
			g.setBackgroundColor(ColorConstants.black);
			
			int xInset = 4;
			int fontWidth = getTextWidth(text);
			int text_x = previousX + xInset;
			int r_bound = clip.x + clip.width - fontWidth - fontHeight;
			int l_bound = clip.x + xInset;
			
			if (text_x < l_bound) {
				text_x = Math.min(l_bound, x-fontWidth-fontHeight);
			} else if (text_x > r_bound) {
				text_x = Math.max(r_bound, previousX + fontHeight);
			}
			
			g.drawString(text, text_x, 0);
		}
	}

}
