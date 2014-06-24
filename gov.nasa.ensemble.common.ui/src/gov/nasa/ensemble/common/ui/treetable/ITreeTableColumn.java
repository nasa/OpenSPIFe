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
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

public interface ITreeTableColumn<T> {

    //
    // column general properties
    //
    
	/**
	 * Returns the name of the column header.
	 * @return name
	 */
	public String getHeaderName();

	/**
	 * Returns the image for the column header.
	 * @return image
	 */
	public Image getHeaderImage();
	
	/**
	 * Return the alignment of the labels in the column
	 * @return SWT.LEFT, SWT.CENTER, SWT.RIGHT
	 */
	public int getAlignment();
	
	/**
	 * Returns the width of the column
	 * @return width
	 */
	public int getDefaultWidth();
	
	/**
     * Returns whether the column would be affected by the given 
     * model change event.  This can be used to optimize a 
     * non-structural viewer update.  If the event does not 
     * affect the column, then the viewer need not update the column.
     *
     * @param feature the event
     * @return <code>true</code> if the column would be affected,
     *    and <code>false</code> if it would be unaffected
     */
    public boolean needsUpdate(Object feature);

	/** Called once before calling other content functions.  This can support
	 * caching and can be used to return a "snapshot" of the data that will 
	 * eliminate inconsistencies from data changes during rendering.
	 * @param element the model element
	 * @return the facet of the element that this column represents
	 */
	public T getFacet(Object element);
	
    //
    // facet presentation
    //
    
    /**
     * Returns the image for the label of the given facet.
     *
     * @param facet the facet for which to provide the label image
     * @return the image used to label the facet, or <code>null</code>
     *   if there is no image for the given object
     */
    public Image getImage(T facet);

    /**
     * Returns the text for the label of the given facet.
     *
     * @param facet the facet for which to provide the label text
     * @return the text string used to label the facet, or <code>null</code>
     *   if there is no text label for the given object
     */
    public String getText(T facet);

    /**
     * Returns the text to be used as a tooltip over the given facet.
     *
     * @param facet the facet for which to provide the tooltip text
     * @return the text string used as the tooltip for the facet, 
     *   or <code>null</code> if there is no tooltip for the given object
     */
    public String getToolTipText(T facet);
    
    /**
     * Provides a foreground color for the given facet.
     * 
     * @param facet the facet
     * @return	the foreground color for the facet, or <code>null</code> 
     *   to use the default foreground color
     */
    public Color getForeground(T facet);

    /**
     * Provides a background color for the given facet.
     * 
     * @param facet the facet
     * @return	the background color for the facet, or <code>null</code> 
     *   to use the default background color
     */
    public Color getBackground(T facet);

    /**
     * Provides a font for the given facet.
     * 
     * @param facet the facet
     * @return the font for the facet, or <code>null</code> 
     *   to use the default font
     */
    public Font getFont(T facet);

    //
    // support for in-place editing
    //
    
    /**
	 * Returns an editor for editing the contents of this column.
	 * @param parent
	 * @return editor
	 */
	public CellEditor getCellEditor(Composite parent, T facet);
	
    /**
	 * See ICellModifier.canModify.
	 * Note that the element & property have already been resolved to the facet.
	 * @param facet the facet to be modified
	 * @return true if the facet can be modified in place
	 */
	public boolean canModify(T facet);
	
	/**
	 * See ICellModifier.getValue.
	 * Note that the element & property have already been resolved to the facet.
	 * @param facet the facet to be modified
	 * @return an object to be passed to the CellEditor
	 */
	public Object getValue(T facet);
	
	/**
	 * See ICellModifier.modify
	 * Note that the element & property have already been resolved to the facet.
	 * @param facet the facet to be modified
	 * @param value the value from the CellEditor
	 * @param undoContext the undo context to do this operation in
	 */
	public void modify(T facet, Object value, IUndoContext undoContext);

    /**
     * In order to allow columns to process events without the creation
     * of a cell editor. This is particularly useful for for checkboxes
     * where a single edit activates the editor, and another one is
     * required for the actual toggle. Other options include incrementing
     * to the next value in a combo box.
     * 
     * @param facet to edit upon
     * @param undoContext the undo context to do this operation in
     * @param item TODO
     * @param index TODO
     * @return boolean if the edit is successful
     */
	public boolean editOnActivate(T facet, IUndoContext undoContext, TreeItem item, int index);
	
	public boolean editOnDoubleClick();

	//
	// Support for sorting
	//
	
    /**
     * Return the comparator to be used when sorting on this column
     * The comparator should expect model elements
     * 
     * Return null if this column is not sortable.
     * 
     * @return the Comparator
     */
    public Comparator<T> getComparator();

}
