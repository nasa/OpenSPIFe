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
package gov.nasa.ensemble.common.ui.layout;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.List;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This layout will cause the parent composite to have a preferred size equal to the first child IFigure, if any. Also this layout will set the first child IFigure, if any, to have the same bounds as
 * the parent composite.
 * 
 * The effect of this is that the parent composite will prefer to be at least large enough to contain the child IFigure. If the parent composite is sized larger than the child IFigure's preferred
 * size, the child IFigure will be stretched to the size of the parent composite.
 * 
 */
public class FillFigureLayout extends AbstractLayout {

	/**
	 * Construct a FillLayout
	 */
	public FillFigureLayout() {
		//
	}

	/**
	 * Get the preferred size of a figure computed from the attributes of its child.
	 * 
	 * @param container
	 *            the figure to calculate preferred size for.
	 * @param wHint
	 *            width hint to influence the size.
	 * @param hHint
	 *            height hint to influence the size.
	 */
	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
		List<?> children = container.getChildren();
		if (children.isEmpty()) {
			return Dimension.SINGLETON;
		}
		if (children.size() > 2) {
			LogUtil.warnOnce("this layout does not support more than one children");
		}
		IFigure child = (IFigure) children.get(0);
		int maxHeight = Math.max(0, child.getPreferredSize().width);
		int maxWidth = Math.max(0, child.getPreferredSize().height);
		return new Dimension(maxWidth, maxHeight);
	}

	/**
	 * Returns the origin for the given figure.
	 * 
	 * @param parent
	 *            the figure whose origin is requested
	 * @return the origin
	 */
	public Point getOrigin(IFigure parent) {
		return parent.getClientArea().getLocation();
	}

	/**
	 * Layout the figure.
	 * 
	 * @param container
	 *            the figure to layout.
	 */
	@Override
	public void layout(IFigure container) {
		List<?> children = container.getChildren();
		if (children.isEmpty()) {
			return;
		}
		if (children.size() > 2) {
			LogUtil.warnOnce("this layout does not support more than one children");
		}
		Dimension size = container.getClientArea().getSize();
		Point offset = getOrigin(container);
		IFigure child = (IFigure) children.get(0);
		child.setBounds(new Rectangle(0, 0, size.width, size.height).translate(offset));
	}

}
