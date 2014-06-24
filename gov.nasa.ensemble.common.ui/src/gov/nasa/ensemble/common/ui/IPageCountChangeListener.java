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

import java.util.EventListener;

public interface IPageCountChangeListener extends EventListener {

	/**
	 * This method will be called when the maximum number of windowPages has changed. The maximum number of windowPages may change
	 * if more data results are returned from a query.
	 * 
	 * A value of 0 indicates that there are currently no windowPages and implementors of this method should handle that case.
	 * 
	 * @param maxPages
	 *            a positive integer representing the current maximum number of windowPages.
	 * 
	 * @see IMultiPageController
	 */
	public void pageCountChanged(int maxPages);
}
