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


import java.util.Comparator;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.util.Policy;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

public abstract class AbstractTreeTableColumn<T> implements ITreeTableColumn<T> {

	private final Comparator<T> defaultComparator = new DefaultComparator();
	
	protected static final Color OUT_OF_DATE_COLOR = new Color(null, 160, 160, 160);
	protected static final Color ERROR_COLOR = new Color(null, 250, 140, 140);
	
	private int defaultWidth;
	private String headerName;
	private Image headerImage;
	
	public AbstractTreeTableColumn(String headerName, int defaultWidth) {
		setHeaderName(headerName);
		setDefaultWidth(defaultWidth);
	}
	
	protected void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public String getHeaderName() {
		return headerName;
	}

	protected void setHeaderImage(Image headerImage) {
		this.headerImage = headerImage;
	}
	
	public Image getHeaderImage() {
		return headerImage;
	}
	
	public int getAlignment() {
		return SWT.CENTER;
	}

	protected void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public int getDefaultWidth() {
		return defaultWidth;
	}

	public abstract boolean needsUpdate(Object feature);

	public abstract T getFacet(Object element);

    //
    // facet presentation (default to empty)
    //
    
	public Image getImage(T facet) {
		return null;
	}

	public String getText(T facet) {
		return "";
	}

	public String getToolTipText(T facet) {
		return null;
	}
	
	public Color getForeground(T facet) {
		return null;
	}

	public Color getBackground(T facet) {
		return null;
	}

	public Font getFont(T facet) {
		return null;
	}

    //
    // support for in-place editing (default to read-only)
    //
    
	public CellEditor getCellEditor(Composite parent, T facet) {
		return null;
	}

	public boolean canModify(T facet) {
		return false;
	}

	public boolean editOnActivate(T facet, IUndoContext undoContext, TreeItem item, int index) {
		return false;
	}
	
	public boolean editOnDoubleClick() {
		return false;
	}

	public Object getValue(T facet) {
		return null;
	}

	public void modify(T facet, Object value, IUndoContext undoContext) {
		// no default implementation
	}

	//
	// default sort based on text of facet
	//
	
	public Comparator<T> getComparator() {
		return defaultComparator;
	}
	
	/**
	 * Default comparator simply compares the text for the facet,
	 * unless the facets are themselves numbers, in which case
	 * they are numerically compared. 
	 * 
	 * @author Andrew
	 */
	private class DefaultComparator implements Comparator<T> {
		@SuppressWarnings("unchecked")
		public int compare(T facet1, T facet2) {
			if ((facet1 instanceof Number) && (facet2 instanceof Number)) {
				Number number1 = (Number) facet1;
				Number number2 = (Number) facet2;
				int result = Double.compare(number1.doubleValue(), number2.doubleValue());
				if (result != 0) {
					return result;
				}
			}
			String text1 = getText(facet1); 
			String text2 = getText(facet2);
			if (text1 == text2) {
				return 0;
			}
			int result = Policy.getComparator().compare(text1, text2);
			return result;
		}
	}
	
}
