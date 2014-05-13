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

import org.eclipse.swt.widgets.Event;

/**
 * Implement this interface if your column requires advanced drawing support
 * http://www.eclipse.org/articles/Article-CustomDrawingTableAndTreeItems/customDraw.htm
 */
public interface IAdvancedDrawingColumn<T> {

	/**
	 * SWT.MeasureItem: allows a client to specify the dimensions of a cell's content
	 * @return true if the event was handled, false if not
	 */
	public abstract boolean handleMeasureItem(T facet, Event event);

	/**
	 * SWT.EraseItem: allows a client to custom draw a cell's background and/or selection, and to influence whether the cell's foreground should be drawn
	 * @return true if the event was handled, false if not
	 */
	public abstract boolean handleEraseItem(T facet, Event event);

	/**
	 * SWT.PaintItem: allows a client to custom draw or augment a cell's foreground and/or focus rectangle
	 * @return true if the event was handled, false if not
	 */
	public abstract boolean handlePaintItem(T facet, Event event);

}
