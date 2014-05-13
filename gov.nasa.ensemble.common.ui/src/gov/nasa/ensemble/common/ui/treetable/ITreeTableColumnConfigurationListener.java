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
/**
 * 
 */
package gov.nasa.ensemble.common.ui.treetable;


import java.util.List;

public interface ITreeTableColumnConfigurationListener<C extends ITreeTableColumn> {
			
	/**
	 * Called when a column is resized
	 * @param mergeColumn
	 * @param width
	 */
	public void columnResized(C mergeColumn, int width);
	
	/**
	 * Called when the column ordering has changed -- it should add and delete the difference in columns
	 * @param oldColumns
	 * @param newColumns
	 */
	public void columnsChanged(List<? extends C> oldColumns, List<? extends C> newColumns);
	
	/**
	 * Called when the sort has changed
	 * @param column
	 * @param direction
	 */
	public void sortChanged(C column, int direction);
	
}
