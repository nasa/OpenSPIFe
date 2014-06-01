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
package gov.nasa.ensemble.common.ui.gef;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class SplitFigureLayout extends AbstractLayout {
	
	private IFigure leftFigure = null;
	private IFigure dividerFigure = null;
	private IFigure rightFigure = null;
	
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		Dimension leftSize = getFigureSize(leftFigure);
		Dimension rightSize = getFigureSize(rightFigure);
		Dimension min = container.getMinimumSize();
		int width = -1, height = -1;
		if (((SplitFigure)container).getOrientation() == SplitConstants.HORIZONTAL_SPLIT) {
			height = Math.max(leftSize.height, rightSize.height);
			if (min != null) {
				height = Math.max(height, min.height);
			}
			height += container.getInsets().getHeight();
		} else {
			width = Math.max(leftSize.width, rightSize.width);
			if (min != null) {
				width = Math.max(width, min.width);
			}
			width += container.getInsets().getWidth();
		}
		return new Dimension(width, height);
	}

	private static Dimension getFigureSize(IFigure figure) {
		IFigure contents = getContents(figure);
		if (contents == null) {
			return Dimension.SINGLETON;
		}
		Dimension preferred = contents.getPreferredSize();
		Dimension min = contents.getMinimumSize(preferred.width, preferred.height);
		Dimension max = contents.getMaximumSize();
//		System.out.println(contents);
//		System.out.println("\tprf: "+preferred);
//		System.out.println("\tmin: "+min);
//		System.out.println("\tmax: "+max);
		
		Dimension size = new Dimension(
			Math.max(preferred.width, min.width),
			Math.max(preferred.height, min.height)
		);
		
		return new Dimension(
			Math.min(size.width, max.width),
			Math.min(size.height, max.height)
		);
	}
	
	private static IFigure getContents(IFigure figure) {
		if (figure == null) {
			return null;
		}
		
		if (figure instanceof ScrollPane) {
			IFigure contents = ((ScrollPane)figure).getContents();
			if (contents != null) {
				return contents;
			}
		}
		return figure;
	}

	@Override
	public Dimension getMinimumSize(IFigure container, int wHint, int hHint) {
		if(((SplitFigure)container).getOrientation() == SplitConstants.HORIZONTAL_SPLIT) {
			return new Dimension(0, -1);
		}
		return super.getMinimumSize(container, wHint, hHint);
	}

	@Override
	public void setConstraint(IFigure child, Object constraint) {
		super.setConstraint(child, constraint);
		
		if (SplitFigure.LEFT.equals(constraint) || SplitFigure.TOP.equals(constraint)) {
			leftFigure = child;
		} else 
		if (SplitFigure.DIVIDER.equals(constraint)) {
			dividerFigure = child;
		} else 
		if (SplitFigure.RIGHT.equals(constraint) || SplitFigure.BOTTOM.equals(constraint)) {
			rightFigure = child;
		}
	}

	@Override
	public void layout(IFigure container) {
		Rectangle area = container.getClientArea();
//		Dimension minSize = getMinimumSize(container, -1, -1);
		Rectangle bounds = new Rectangle();
		
		Point sashLocation = dividerFigure.getBounds().getLocation();

		if(((SplitFigure)container).getOrientation() == SplitConstants.HORIZONTAL_SPLIT) {
			int sashX = 0;
			if (sashLocation.x < 0) {
				sashX = 0;
//			} else if (area.width > minSize.width && sashLocation.x  - dividerFigure.getPreferredSize().width > area.width) {
//				sashX = area.width  - dividerFigure.getPreferredSize().width;
			} else {
				sashX = sashLocation.x;
			}

			bounds.y = area.y;
			bounds.height = area.height;//Math.max(leftFigure.getPreferredSize().height, rightFigure.getPreferredSize().height));
			
			if (leftFigure != null) {
				bounds.x = 0;
				bounds.width = sashX;
				leftFigure.setBounds(bounds);
			}

			if (dividerFigure != null) {
				bounds.x += sashX;
				bounds.width = dividerFigure.getPreferredSize().width;
				dividerFigure.setBounds(bounds);
			}

			if (rightFigure != null) {
				bounds.x += dividerFigure.getPreferredSize().width;
				if (rightFigure instanceof ScrollPane) {
					bounds.width = area.width - bounds.x;
				} else {
					bounds.width = rightFigure.getPreferredSize().width;
					bounds.width = area.width - bounds.x;
				}
				rightFigure.setBounds(bounds);
			}
		} else {
			int sashY = 0;
			if (sashLocation.y < 0) {
				sashY = 0;
//			} else if (sashLocation.y  - dividerFigure.getPreferredSize().height > area.height) {
//				sashY = area.height  - dividerFigure.getPreferredSize().height;
			} else {
				sashY = sashLocation.y;
			}

			bounds.x = area.x;
			bounds.width = area.width;

			if (leftFigure != null) { // top figure
				bounds.y = 0;
				bounds.height = sashY;
				leftFigure.setBounds(bounds);
			}

			if (dividerFigure != null) {
				bounds.y += sashY;
				bounds.height = dividerFigure.getPreferredSize().height;
				dividerFigure.setBounds(bounds);
			}

			if (rightFigure != null) { // bottom figure
				bounds.y += dividerFigure.getPreferredSize().height;
				if (rightFigure instanceof ScrollPane) {
					bounds.height = area.height - bounds.y;
				} else {
					bounds.height = rightFigure.getPreferredSize().height;
				}
				rightFigure.setBounds(bounds);
			}
		}
		
		
	}

}
