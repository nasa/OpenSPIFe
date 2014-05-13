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
/*
 * Created on Mar 15, 2005
 */
package gov.nasa.arc.spife.ui.timeline.figure;

import gov.nasa.arc.spife.timeline.model.FigureStyle;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.color.ColorUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * This probably should be refactored and split up.  It handles the following:
 * <ul>
 * <li> Drawing a cylindrical-looking (or flat) colored bar.
 * <li> Setting the hue of the shaded cylindrical-looking bar.
 * <li> Setting the texture to plastic or metallic lighting texture.
 * <li> Superimposes an optional text label.
 * </ul>
 */
public class BarFigure extends RectangleFigure{
	
	private static final Color DEFAULT_BRIGHT_COLOR = ColorMap.RGB_INSTANCE.getColor(new RGB(64, 64, 128));
	
	private static final int PATTERN_LINE_SPACING = 7;
	private Palette palette = Palette.NORMAL;
	private boolean renderSimply;
	
	public static enum Palette { NORMAL, MOVING, UNMOVING }
	
	/** 
	 * selected_*, moving_*, and unmoving_* are manually-computed settings
	 * for certain special color codings a bar can be set to, overriding its normal hue.
	 */

	private static final Color moving_bright = ColorMap.RGB_INSTANCE.getColor(new RGB(64, 128, 64)); // green
	private static final Color moving_pale = ColorMap.RGB_INSTANCE.getColor(new RGB(0, 255, 0));
	private static final Color moving_dark = ColorMap.RGB_INSTANCE.getColor(new RGB(0, 64, 0));

	private static final Color unmoving_bright = ColorMap.RGB_INSTANCE.getColor(new RGB(128, 64, 64)); // red
	private static final Color unmoving_pale = ColorMap.RGB_INSTANCE.getColor(new RGB(255, 0, 0));
	private static final Color unmoving_dark = ColorMap.RGB_INSTANCE.getColor(new RGB(64, 0, 0));

	private boolean paintBorder = false;
	
	private int rowElementHeight = TimelineUtils.getRowElementHeight();
	
	/**
	 * <img src="https://ensemble.jpl.nasa.gov/confluence/download/attachments/3769867/BarFigure.normal_bright.png"
	     height="100px" width="auto">
	 * <p>
	 * normal_bright, normal_pale, and normal_dark determine the "cylinder" shading,
	 * which actually is two gradients, like two inclined planes that give the illusion
	 * of a cylinder.  It starts out pure at the top, shades to a pale color,
	 * as if a light were reflecting off plastic, and then shades to a
	 * shadowed version of the color underneath the bar.s
	 * (A different choice of the three colors gives a metallic gleam.)
	 * To change the hue, we compute all three colors and set all three variables.
	 * 
	 */
	private Color normal_bright = null;
	private Color normal_pale = null;
	private Color normal_dark = null;

	private Color bright = normal_bright;
	private Color pale = normal_pale;
	private Color dark = normal_dark;
	
	/** Round the corners of the figure */
	private boolean round = true;
	
	/** Fill policy */
	private FigureStyle figureStyle = FigureStyle.SOLID;
	
	public BarFigure() {
		setNormalColorPalette(DEFAULT_BRIGHT_COLOR);
	}
	
	/**
	 * Tells the color allocator which colors to avoid assigning
	 * (when arbitrarily assigning colors to, e.g., observations).
	 * "Avoid" means it should initially choose colors that are nowhere near
	 * these colors in the color space, and then asymptotically approach them
	 * when it starts running out of colors.
	 * @return list of colors to avoid.
	 */
	public static List<Color> reservedColors() {
		List<Color> result = new ArrayList<Color>();
		result.add(moving_bright);
		result.add(unmoving_bright);
		result.add(ColorConstants.red);
		return result;
	}
	
	public void setPalette(Palette palette) {
		if (this.palette != palette) {
			this.palette = palette;
			updateColors();
			repaint();
		}
	}
	
	public void setPaintBorder(boolean paintBorder) {
		this.paintBorder = paintBorder;
		repaint();
	}

	public void setRound(boolean round) {
		this.round = round;
	}

	public void setRenderSimply(boolean renderSimply) {
		this.renderSimply = renderSimply;
		invalidate();
	}
	
	/**
	 * Visually indicate the texture
	 */
	public void setFigureStyle(FigureStyle figureStyle) {
		if (this.figureStyle != figureStyle) {
			this.figureStyle = figureStyle;
			invalidate();
		}
	}

	/**
	 * Cuts a horz rectangle up into smaller ones.  There is a bug (on linux at least)
	 * where a rectangle that is large can not be rendered.
	 */
	private List<Rectangle> cutRectangleUp(Rectangle rec) {
		List<Rectangle> list = new ArrayList<Rectangle>();
		int n = rec.width / 20000;
		int r = rec.width % 20000;
		if (n == 0) {
			list.add(rec);
		} else {
			for (int i = 0; i < n; i++) {
				list.add(new Rectangle(rec.x + i*20000, rec.y , 20000, rec.height));
			}
			list.add(new Rectangle(rec.x + n*20000, rec.y , r, rec.height));
		}
		return list;
	}

	@Override
	protected void paintBorder(Graphics graphics) {
		if (paintBorder && figureStyle != FigureStyle.IBAR) {
			graphics.drawRectangle(getBounds().getCopy().shrink(1, 1).translate(0, -1));
		}
	}

	@Override
	protected void fillShape(Graphics graphics) {
		graphics.pushState();
		
		Rectangle clip = graphics.getClip(new Rectangle());
		Rectangle bounds = getBounds().getCopy();
		int arcWidth = bounds.height / 2;
		
		Object constraint = getParent().getLayoutManager().getConstraint(this);
		if (constraint instanceof RowDataFigureLayoutData
				&& ((RowDataFigureLayoutData)constraint).instantaneous) {
			fillInstantaneousShape(graphics, bounds.getCopy(), dark);
		} else if (renderSimply) {
			fillShapeSimply(graphics, bounds, arcWidth);
		} else {
			switch (figureStyle) {
			case PATTERN:
				fillShapeAsPattern(graphics, bounds);
				break;
			case IBAR:
				fillShapeAsIBar(graphics, bounds);
				break;
			case SOLID:
			default:
				// fill in the background
				for (Rectangle r : cutRectangleUp(bounds.getCopy())) {
					if (!clip.intersects(r)) {
						continue;
					}									
					
					fillShapeAsGradient(graphics, r, arcWidth);																	
				}
				break;
			}
		}
		graphics.popState();
	}
	
	public int getRowElementHeight() {
		return rowElementHeight;
	}

	public void setRowElementHeight(int rowElementHeight) {
		this.rowElementHeight = rowElementHeight;
		repaint();
	}

	@Override
	public Dimension getMinimumSize(int hint, int hint2) {
		return new Dimension(rowElementHeight, rowElementHeight);
	}

	private void fillShapeSimply(Graphics graphics, Rectangle bounds, int arcWidth) {
		Rectangle r = bounds.getCopy();
		
		updateColors();
		Color top_gradient_color = bright;
		Color bottom_gradient_color = dark;
		
		graphics.setForegroundColor(top_gradient_color);
		graphics.setBackgroundColor(bottom_gradient_color);
		graphics.fillGradient(r, true);
		graphics.setForegroundColor(ColorConstants.black);
		graphics.drawLine(r.getTopLeft(), r.getBottomLeft());
	}
	
	private void fillShapeAsGradient(Graphics graphics, Rectangle bounds, int arcWidth) {
		bounds = bounds.getCopy();
		Rectangle r = bounds.getCopy();
		
		updateColors();
		
		r.y += arcWidth / 2;
		r.height -= arcWidth;
		
		Rectangle top = bounds.getCopy();
		top.height /= 2;
		top.width -= 1;

		Color top_gradient_color = bright;
		Color bottom_gradient_color = dark;
		
		//
		// Draw the top gradient
		graphics.setBackgroundColor(top_gradient_color);
		fillRectangle(graphics, top, arcWidth);
		graphics.setForegroundColor(ColorConstants.black);
		drawRectangle(graphics, top, arcWidth);

		//
		// Draw the lower gradient
		top.y = top.y + top.height;
		top.height -= 2;
		graphics.setBackgroundColor(bottom_gradient_color);
		fillRectangle(graphics, top, arcWidth);
		graphics.setForegroundColor(ColorConstants.black);
		drawRectangle(graphics, top, arcWidth);

		//
		// Draw the center gradient
		graphics.setForegroundColor(top_gradient_color);
		graphics.setBackgroundColor(bottom_gradient_color);
		graphics.fillGradient(r, true);
		graphics.setForegroundColor(ColorConstants.black);
		graphics.drawLine(r.getTopLeft().translate(0, -1), r.getBottomLeft());
		graphics.drawLine(r.getTopRight().translate(-1, -1), r.getBottomRight().translate(-1, 0));
	}
	
	private void fillShapeAsPattern(Graphics graphics, Rectangle bounds) {
		graphics.setForegroundColor(bright);
		graphics.setBackgroundColor(pale);
		int xStart = bounds.x - bounds.height;
		int xEnd = bounds.x + bounds.width;
		for (int x = xStart - bounds.height; x <= xEnd; x += PATTERN_LINE_SPACING) {
			int d = bounds.width - (x - bounds.x);
			if (d > bounds.height)  {
				d = bounds.height;
			}
			graphics.drawLine(x, bounds.y, x+d-1, bounds.y+d-1);
		}
		graphics.drawRectangle(bounds.x, bounds.y, bounds.width-1, bounds.height-1);
	}
	
	private void fillShapeAsIBar(Graphics graphics, Rectangle bounds) {
		graphics.setForegroundColor(ColorConstants.black);
		graphics.setBackgroundColor(pale);
		graphics.setLineWidth(1);
		int l = bounds.x;
		int t = bounds.y;
		int h = bounds.height;
		int w = bounds.width;
		int r = l + w;
		graphics.fillRectangle(l, t, 3, h);
		if (paintBorder) graphics.drawLine(l, t, l, t + h);
		graphics.fillRectangle(l, (t+h/2)-1, w, 3);
		graphics.fillRectangle(r-3, t, 3, h);
		if (paintBorder) graphics.drawLine(r-1, t, r-1, t + h);
	}

	private void drawRectangle(Graphics graphics, Rectangle rectangle, int arcWidth) {
		if (round) {
			graphics.drawRoundRectangle(rectangle, arcWidth, arcWidth);
		} else {
			graphics.drawRectangle(rectangle);
		}
	}
	
	private void fillRectangle(Graphics graphics, Rectangle rectangle, int arcWidth) {
		if (round) {
			graphics.fillRoundRectangle(rectangle, arcWidth, arcWidth);
		} else {
			graphics.fillRectangle(rectangle);
		}
	}

	private void fillInstantaneousShape(Graphics graphics, Rectangle bounds, Color color) {
		PointList list = new PointList();
		int padding = (int) (bounds.height * .15);
		int xCenter = bounds.x + bounds.width / 2;
		list.addPoint(xCenter, bounds.y + padding);
		list.addPoint(bounds.x + bounds.width - padding, bounds.y + bounds.height - 2*padding);
		list.addPoint(bounds.x + padding, bounds.y + bounds.height - 2*padding);
		graphics.setBackgroundColor(color);
		graphics.fillPolygon(list);

		graphics.setForegroundColor(this.pale);
		graphics.drawPolygon(list);
	}
	
	@Override
	public Color getBackgroundColor() {
		return normal_bright;
	}

	@Override
	public void setBackgroundColor(Color bg) {
		setNormalColorPalette(bg);
		for (Object o : getChildren()) {
			if (o instanceof BarFigure) {
				((BarFigure)o).setNormalColorPalette(bg);
			}
		}
		repaint();
	}

	@Override
	protected void outlineShape(Graphics graphics) {
		// no outline
	}

	private void updateColors() {
		if (palette == null) {
			return;
		}
		
		switch (palette) {
		case NORMAL:
			bright = normal_bright;
			pale = normal_pale;
			dark = normal_dark;
			break;
		case MOVING:
			bright = moving_bright;
			pale = moving_pale;
			dark = moving_dark;
			break;
		case UNMOVING:
			bright = unmoving_bright;
			pale = unmoving_pale;
			dark = unmoving_dark;
			break;
		}
	}
	
	public Color getNormalColorPalette() {
		return normal_bright;
	}
	
	/**
	 * Sets the hue that this bar will have under normal circumstances
	 * (i.e., unless overridden, e.g. by a constrained move).
	 * Sets the color triple (bright, pale, and dark), given just the bright color.
	 * @param brightColor -- The base color.  It shades into paler and darker from here.
	 */
	public void setNormalColorPalette(Color brightColor) {
		final RGB rgbBase = brightColor.getRGB();
		final float hsb[] = ColorUtils.getHSB(rgbBase);
		final float h = hsb[0];
		final float s = hsb[1];
		final float b = hsb[2];
		normal_bright = ColorMap.RGB_INSTANCE.getColor(ColorUtils.getRGB(h, s, b));
		normal_pale = ColorMap.RGB_INSTANCE.getColor(ColorUtils.getRGB(h, s*0.5f, b));
		normal_dark = ColorMap.RGB_INSTANCE.getColor(ColorUtils.getRGB(h, s, b*0.75f));
		updateColors();
	}
	
}
