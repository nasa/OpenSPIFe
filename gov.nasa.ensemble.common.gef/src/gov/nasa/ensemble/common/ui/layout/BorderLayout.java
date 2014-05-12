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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class BorderLayout extends Layout {

	private Control top = null, bottom = null, left = null, right = null, center = null;
	private boolean inLayout = false;
	
	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		refreshBoundedControls(composite);
		
		int minWHint = 0, minHHint = 0;
		if (wHint < 0) {
			minWHint = -1;
		}

		if (hHint < 0) {
			minHHint = -1;
		}
		
		Point childSize;
		Point prefSize = new Point(0,0);
		int middleRowWidth = 0, middleRowHeight = 0;
		if (top != null) {
			childSize = top.computeSize(wHint, hHint);
			hHint = Math.max(minHHint, hHint - (childSize.y));
			prefSize.x = childSize.x;
			prefSize.y = childSize.y;
		}
		if (bottom != null) {
			childSize = bottom.computeSize(wHint, hHint);
			hHint = Math.max(minHHint, hHint - (childSize.y));
			prefSize.x = Math.max(prefSize.x, childSize.x);
			prefSize.y += childSize.y;
		}
		if (left != null) {
			childSize = left.computeSize(wHint, hHint);
			middleRowWidth = childSize.x;
			middleRowHeight = childSize.y;
			wHint = Math.max(minWHint, wHint - (childSize.x));
		}
		if (right != null) {
			childSize = right.computeSize(wHint, hHint);
			middleRowWidth += childSize.x;
			middleRowHeight = Math.max(childSize.y, middleRowHeight);
			wHint = Math.max(minWHint, wHint - (childSize.x));
		}
		if (center != null) {
			childSize = center.computeSize(wHint, hHint);
			middleRowHeight = Math.max(childSize.y, middleRowHeight);
			middleRowWidth += childSize.x;
		}
		
		// Add the size of the middle row
		prefSize.x = Math.max(prefSize.x, middleRowWidth);
		prefSize.y += middleRowHeight;
		
		LogUtil.debug("PreferredSize: "+prefSize);
		return prefSize;
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		if (inLayout) {
			return;
		}
		refreshBoundedControls(composite);
		Rectangle area = composite.getClientArea();
		if (area.width == 0) {
			return;
		}
		try {
			inLayout = true;
			Rectangle rect = new Rectangle(0,0,0,0);
			Point childSize;
			if (top != null) {
				childSize = top.computeSize(area.width, -1);
				rect.x = area.x;
				rect.y = area.y;
				rect.width = area.width;
				rect.height = childSize.y;
				top.setBounds(rect);
				area.y += rect.height;
				area.height -= rect.height;
				LogUtil.debug("Top: "+rect);
			}
			if (bottom != null) {
				childSize = bottom.computeSize(area.width, -1);
				rect.x = area.x;
				rect.y = area.y + area.height - childSize.y;
				rect.width = area.width;
				rect.height = childSize.y;
				bottom.setBounds(rect);
				area.height -= rect.height;
				LogUtil.debug("Bottom: "+rect);
			}
			if (left != null) {
				childSize = left.computeSize(-1, Math.max(0, area.height));
				rect.x = area.x;
				rect.y = area.y;
				rect.width = childSize.x;
				rect.height = Math.max(0, area.height);
				left.setBounds(rect);
				area.x += rect.width;
				area.width -= rect.width;
				LogUtil.debug("Left: "+rect);
			}
			if (right != null) {
				childSize = right.computeSize(-1, Math.max(0, area.height));
				rect.x = area.x + area.width - childSize.x;
				rect.y = area.y;
				rect.width = childSize.x;
				rect.height = Math.max(0, area.height);
				right.setBounds(rect);
				area.width -= rect.width;
				LogUtil.debug("Right: "+rect);
			}
			if (center != null) {
				center.setBounds(area);
				LogUtil.debug("Center: "+area);
			}
		} finally {
			inLayout = false;
		}
	}

	private void refreshBoundedControls(Composite composite) {
		for (Control control : composite.getChildren()) {
			Object data = control.getLayoutData();
			if (!(data instanceof Integer)) {
				String message = "invalid data for border layout: " + data;
				message += "\n on control: " + control;
				LogUtil.warnOnce(message);
				continue;
			}
			
			switch ((Integer)data) {
			case SWT.TOP:
				top = control;
				break;
			case SWT.BOTTOM:
				bottom = control;
				break;
			case SWT.LEFT:
				left = control;
				break;
			case SWT.RIGHT:
				right = control;
				break;
			case SWT.CENTER:
				center = control;
				break;
			}
		}
	}

}
