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
package gov.nasa.arc.spife.ui.timeline.util;

import gov.nasa.ensemble.common.logging.LogUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

public class TimelineLayout extends Layout {
	
	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		Point extent = new Point(-1, -1);
		Control[] children = composite.getChildren();
		int count = children.length;
		StringBuilder builder = (LogUtil.logger().isDebugEnabled() ? new StringBuilder() : null);
		for (int i=0; i<count; i++) {
			Control child = children[i];
			Point childExtent = computeSize(child, wHint, hHint);
			if (builder != null) {
				builder.append("childExtent: " + childExtent + "\n");
			}
			extent.x = Math.max(extent.x, childExtent.x);
			extent.y += childExtent.y;
		}
		if (builder != null) {
			builder.append("\t" + extent + "\n");
			LogUtil.debug(builder.toString());
		}
		return extent;
	}
	
	private boolean inLayout = false;
	private boolean onceMore = false;
	
	@Override
	protected synchronized void layout(Composite composite, boolean flushCache) {
		if (inLayout) {
			onceMore = true;
			return; // prevent recursive layout
		}
		Composite scrolled = composite.getParent();
		Rectangle clientArea = scrolled.getClientArea();
		int width = clientArea.width;
		if (width == 0) {
			return; // nothing to do yet
		}
		try {
			inLayout = true;
			doLayout(composite, width);
		} finally {
			inLayout = false;
		}
		if (onceMore) {
			onceMore = false;
			layout(composite, flushCache);
		}
	}

	private void doLayout(Composite composite, int width) {
		int x = composite.getClientArea().x;
		int y = composite.getClientArea().y;
		Control[] children = composite.getChildren();
		int count = children.length;
		for (int i = 0 ; i < count ; i++) {
			Control child = children[i];
			Point childExtent = computeSize(child, SWT.DEFAULT, SWT.DEFAULT);
			Rectangle r = new Rectangle(x, y, width, childExtent.y);
			child.setBounds(r);
			y += childExtent.y;
		}
	}

	private Point computeSize(Control child, int wHint, int hHint) {
		Object layoutData = child.getLayoutData();
		if (layoutData instanceof TimelineLayoutData) {
			TimelineLayoutData data = (TimelineLayoutData) layoutData;
			if (data.maximized) {
				ScrolledComposite parent = (ScrolledComposite) child.getParent().getParent();
				Rectangle a = parent.getClientArea();
				return new Point(a.width, a.height - 2);
			}
			if (data != null && data.preferredSize != null) {
				return data.preferredSize;
			}
		}
		return child.computeSize(wHint, hHint, true);
	}

}
