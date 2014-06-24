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

import gov.nasa.ensemble.common.collections.DefaultComparator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class DefaultViewerComparator extends ViewerComparator {

	public static final DefaultViewerComparator INSTANCE = new DefaultViewerComparator();
	
	/**
	 * @param viewer
	 * @param e1
	 * @param e2
	 * @return a negative number if the first element is less than the 
     *  second element; the value <code>0</code> if the first element is
     *  equal to the second element; and a positive number if the first
     *  element is greater than the second element
	 */
	@Override
	@SuppressWarnings("unchecked")
	public int compare(Viewer viewer, Object e1, Object e2) {
		return DefaultComparator.INSTANCE.compare(e1, e2);
	}

}
