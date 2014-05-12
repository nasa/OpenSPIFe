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
package gov.nasa.ensemble.common.ui.treetable;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class TreeTableColumnLayout extends TreeColumnLayout {

	private boolean inLayout = false;
	private final boolean proportionalResizing;
	
	public TreeTableColumnLayout(boolean proportionalResizing) {
		this.proportionalResizing = proportionalResizing;
	}

	public void setColumnWidth(TreeColumn column, int width) {
		ColumnLayoutData data = getLayoutData(column);
		if (data == null) {
			if (proportionalResizing) {
				data = new ColumnWeightData(width, width);
			} else {
				data = new ColumnPixelData(width);
			}
			column.setData(LAYOUT_DATA, data);
		} else if (data instanceof ColumnWeightData) {
			ColumnWeightData columnWeightData = (ColumnWeightData) data;
			columnWeightData.weight = width;
			columnWeightData.minimumWidth = width;
		} else if (data instanceof ColumnPixelData) {
			ColumnPixelData columnPixelData = (ColumnPixelData) data;
			columnPixelData.width = width;
		}
	}
	
	@Override
	protected ColumnLayoutData getLayoutData(Scrollable tableTree, int columnIndex) {
		TreeColumn column = ((Tree) tableTree).getColumn(columnIndex);
		ColumnLayoutData layoutData = getLayoutData(column);
		if (layoutData == null) {
			int width = column.getWidth();
			if (proportionalResizing) {
				layoutData = new ColumnWeightData(width, width);
			} else {
				layoutData = new ColumnPixelData(width);
			}
			column.setData(LAYOUT_DATA, layoutData);
		}
		return layoutData;
	}

	public ColumnLayoutData getLayoutData(TreeColumn column) {
		return (ColumnLayoutData) column.getData(LAYOUT_DATA);
	}

	public boolean isInLayout() {
		return inLayout;
	}
	
	@Override
	protected void layout(Composite composite, boolean flushCache) {
		boolean oldInLayout = inLayout;
		try {
			inLayout = true;
			super.layout(composite, flushCache);
		} finally {
			inLayout = oldInLayout;
		}
	}
	
}
