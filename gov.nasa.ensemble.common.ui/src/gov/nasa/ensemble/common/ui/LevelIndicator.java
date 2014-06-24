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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.ui.color.ColorConstants;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class LevelIndicator extends Canvas 
	implements PaintListener, DisposeListener {

	public static final int LEVEL_WIDTH = 40;
	public static final int LEVEL_HEIGHT = 12;
	
	private int min;
	private int max;

	private int level = 0;
	private int highLevel = 0;
	
	private static final Color black = ColorConstants.black;
	private static Color red;
	private static Color green;
	
	private Point size;
	private boolean maxxedOut;
	private boolean hasBeenMaxxedOut;
	
	public LevelIndicator(Composite parent, int style, int minLevel, int maxLevel) {
		super(parent, style);
		max = maxLevel;
		min = minLevel;
		addPaintListener(this);
		addDisposeListener(this);
		red = new Color(parent.getDisplay(), 255, 100 ,0);
		green = new Color(parent.getDisplay(), 0, 255 ,100);
		size = new Point(LEVEL_WIDTH+2, LEVEL_HEIGHT);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				reset();
			}
		});
	}
	
	public void reset() {
		if (Display.getCurrent() != null) {
			//reset high watermark to current value
			hasBeenMaxxedOut = false;
			highLevel = min;
			setLevel(getLevel());
		} else {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					reset();
				}
			});
		}
	}

	public void repaint() {
		if (Display.getCurrent() != null) {
			//reset high watermark to current value
			setLevel(getLevel());
		} else {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					repaint();
				}
			});
		}
	}
	@Override
	public void paintControl(PaintEvent e) {
		Color oldForegroundColor = e.gc.getForeground();
		Color oldBackgroundColor = e.gc.getBackground();

		maxxedOut = level >= max;
		hasBeenMaxxedOut = hasBeenMaxxedOut || maxxedOut; 
//		if (hasBeenMaxxedOut)
//			e.gc.setForeground(red);
//		else
//			e.gc.setForeground(black);
//		
//		if (maxxedOut)
//			e.gc.setBackground(red);
//		else
		e.gc.setBackground(green);
		
		int barWidth = Math.min((int)(((float)(level-min)/(float)(max-min))*LEVEL_WIDTH)-1, LEVEL_WIDTH-1);

		e.gc.fillRectangle(0, 0, barWidth, LEVEL_HEIGHT-1);
		e.gc.drawRectangle(0, 0, LEVEL_WIDTH-1, LEVEL_HEIGHT-1);
		
		int maxLevelX = (int)(((float)(highLevel-min)/(float)(max-min)) * LEVEL_WIDTH)-1;

		if (hasBeenMaxxedOut) {
			maxLevelX = LEVEL_WIDTH+1;
		}
		e.gc.setForeground(red);
		e.gc.drawLine(maxLevelX, 0, maxLevelX, LEVEL_HEIGHT-1);
		
		e.gc.setForeground(oldForegroundColor);
		e.gc.setBackground(oldBackgroundColor);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return size;
	}
	
	@Override
	public void widgetDisposed(DisposeEvent e) {
		black.dispose();
		red.dispose();
		green.dispose();
	}

	/**
	 * Set the level value and update the highLevel if the new level is larger
	 * then the previous one. This also causes the Level Indicator to redraw.
	 * 
	 * @param level The new level.
	 */
	public void setLevel(int level) {
		if (!isDisposed()) {
			this.level = level;
			if (level > highLevel)
				highLevel = level;
			setToolTipText("Current: " + level + ", High: " + highLevel	+ ", Max: " + max);
			this.redraw();
		}		
	}
	
	public int getLevel() {
		return level;
	}
	
	public int getMinLevel() {
		return min;
	}
	
	public int getMaxLevel() {
		return max;
	}

	public void setMaxLevel(int max) {
		this.max = max;
		repaint();
	}

	public void setMinLevel(int min) {
		this.min = min;
		repaint();
	}
}
