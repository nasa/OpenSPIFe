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
package gov.nasa.arc.spife.ui.table.days;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * Layout based on ScrolledCompositeLayout
 * 
 * @author ideliz
 * @see ScrolledCompositeLayout
 */
public class DaysLayout extends Layout {

	private static final int DEFAULT_WIDTH	= 300;
	private static final int DEFAULT_HEIGHT	= 500;
	
	private boolean inLayout = false;
	private int dayCount;
	
	public void setDayCount(int count) {
		this.dayCount = count;
	}
	
	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		int x = wHint;
		if (x == SWT.DEFAULT) {
			Control[] children = composite.getChildren();
			if (children.length == 0) {
				x = DEFAULT_WIDTH;
			} else {
				x = children[0].getSize().x;
			}
		}
		int y = hHint;
		if (y == SWT.DEFAULT) {
			Control[] children = composite.getChildren();
			if (children.length == 0) {
				y = DEFAULT_HEIGHT;
			} else {
				y = children[0].getSize().y;
			}
		}
		return new Point(x, y);
	}

	@Override
	protected boolean flushCache(Control control) {
		return true;
	}

	/**
	 * Our layout performs these jobs:
	 * 1. compute the width that is needed to display the amount of days in the plan
	 * 2. update the horizontal scroll bar to match the new width
	 * 3. resize the children to fit their contents
	 */
	@Override
	protected void layout(Composite composite, boolean flushCache) {
		if (inLayout) {
			return;
		}
		DaysComposite sc = (DaysComposite)composite;
		ScrollBar hBar = sc.getHorizontalBar();
		int compositeHeight = sc.getSize().y;
		int scrollBarHeight = hBar.getSize().y;
		if (scrollBarHeight >= compositeHeight) {
			// the view is so short that only the horizontal bar can be seen
			return;
		}
		try {
			inLayout = true;
			int childHeight = compositeHeight - scrollBarHeight;
			int clientWidth = sc.getClientArea().width;
			int contentWidth;
			int childWidth = DEFAULT_WIDTH;
			Control[] children = composite.getChildren();
			if (children.length == 0) {
				contentWidth = clientWidth;
			} else {
				Control representative = children[0];
				Point size = representative.computeSize(SWT.DEFAULT, childHeight, flushCache);
				childWidth = size.x;
				contentWidth = Math.max(dayCount * childWidth, clientWidth);
			}
			hBar.setMaximum (contentWidth);
			hBar.setThumb (Math.min (contentWidth, clientWidth));
			for (Control child : children) {
				child.setSize(childWidth, childHeight);
			}
		} finally {
			inLayout = false;
		}
	}
	
}
