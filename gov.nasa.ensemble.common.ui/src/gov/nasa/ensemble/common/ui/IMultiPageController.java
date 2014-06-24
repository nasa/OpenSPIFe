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
package gov.nasa.ensemble.common.ui;

/**
 * An interaction handler for a multi-page view of data, such as is returned from a database query. This specifies support for a
 * model that progressively loads data such that the number of windowPages can dynamically change. This handler also maintains a
 * listener mechanism to notify other objects when the number of windowPages of data is updated.
 */
public interface IMultiPageController {

	/**
	 * Get the current page number.
	 * 
	 * @return the current page number
	 */
	public int getCurrentPage();

	/**
	 * Display a given page of results. Valid values are [0,(getNumPages() - 1]
	 * 
	 * @param page
	 *            the page index to display.
	 */
	public void setCurrentPage(int page);

	/**
	 * Get the total number of windowPages of data. This total is considered temporary if {@link #hasUnknownMaxTotalPages()} returns
	 * true (indicating data is not yet finished loading or updating).
	 * 
	 * @return the total number of windowPages
	 */
	public int getNumPages();

	/**
	 * Return whether the total number of windowPages is known with certainty (e.g. if data has fully loaded into the data model).
	 * 
	 * @return true if total number of windowPages is currently known with certainty.
	 */
	public boolean hasUnknownMaxTotalPages();

	/**
	 * Add a listener to changes in the page count.
	 * 
	 * @param listener
	 */
	public void addPageCountChangeListener(IPageCountChangeListener listener);

	/**
	 * Remove a listener to changes in the page count.
	 * 
	 * @param listener
	 */
	public void removePageCountChangeListener(IPageCountChangeListener listener);

}
