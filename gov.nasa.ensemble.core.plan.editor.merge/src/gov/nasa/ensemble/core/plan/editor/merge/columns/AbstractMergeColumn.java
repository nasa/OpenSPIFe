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
package gov.nasa.ensemble.core.plan.editor.merge.columns;

import gov.nasa.ensemble.common.ui.treetable.AbstractTreeTableColumn;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;

import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public abstract class AbstractMergeColumn<T> extends AbstractTreeTableColumn<T> {

	private final IMergeColumnProvider provider;

	public AbstractMergeColumn(IMergeColumnProvider provider, String headerName, int defaultWidth) {
		super(headerName, defaultWidth);
		this.provider = provider;
		if (provider == null) {
			throw new IllegalArgumentException("consider using AbstractTreeTableColumn directly if you have no provider");
		}
	}

	/**
	 * Returns the id for this column.  Will be used in
	 * persisting information about column visibility,
	 * formatting, etc.  Therefore, must be identical 
	 * across different executions of the tool.
	 */
	public String getId() {
		return getProviderName() + " - " + getHeaderName();
	}
	
	public String getProviderName() {
		return provider.getName();
	}
	
	/**
	 * For some columns, the whole viewer must be updated (@See ProfileColumn).
	 * Handle if the viewer has a column like this differently.
	 * because 
	 * @return
	 */
	public boolean updateAll() {
		return false;
	}

	/**
	 * Returns the center of the cell at the index on the item,
	 * in display coordinates.
	 * 
	 * @param item
	 * @param index
	 * @return
	 */
	public static Point getCellCenterInDisplayCoordinates(TreeItem item, int index) {
		Rectangle bounds = item.getBounds(index);
		Point controlCenter = Geometry.centerPoint(bounds);
		Tree tree = item.getParent();
		return tree.toDisplay(controlCenter);
	}

}
