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

import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.common.ui.FontUtils;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;

public class ScaleHeaderFigure extends ScaleFigure {

	private Date cursorTime;
	
	@Override
	public Dimension getPreferredSize(int hint, int hint2) {
		return new Dimension(-1, -1);
	}
	
	public void setCursorTime(Date cursorTime) {
		this.cursorTime = cursorTime;
		repaint();
	}
	
	@Override
	protected void paintFigure(Graphics g) {
		super.paintFigure(g);
		FontMetrics fm = FigureUtilities.getFontMetrics(getFont());
		int h = fm.getHeight();
		int y = h;
		boolean first = true;
		String symbolicFontName = getFont().toString();
		Font boldFont = FontUtils.FONT_REGISTRY_INSTANCE.getBold(symbolicFontName);
		for (DateFormat format : dateFormats) {
			try {
				g.pushState();
				if (first) {
					g.setFont(boldFont);
					first = false;
				}
				String id = DateFormatRegistry.INSTANCE.getDateFormatID(format);
				if (id==null) id = "";
				g.drawText(id, 5, y);
				
				if (cursorTime != null) {
					String date = format.format(cursorTime);
					int width = FigureUtilities.getTextWidth(date, getFont()) + 5;
					g.drawText(date, bounds.width - width, y);
				}
				
				y += h;
			} finally {
				g.popState();
			}
		}
		//boldFont.dispose();
	}
	
}
