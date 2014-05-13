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

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class TreeTableViewerComparator extends ViewerComparator {

	private final boolean reverse;
	private final ITreeTableColumn column;

	public TreeTableViewerComparator(ITreeTableColumn column, boolean reverse) {
		super();
		this.column = column;
		this.reverse = reverse;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public int compare(Viewer viewer, Object e1, Object e2) {
		Object facet1 = column.getFacet(e1);
		Object facet2 = column.getFacet(e2);
		Comparator comparator = column.getComparator();
		int result = 0;
		try {
			result = comparator.compare(facet1, facet2);
		} catch (ThreadDeath td) {
			throw td;
		} catch (Throwable t) {
			Logger.getLogger(TreeTableViewerComparator.class).error("column comparator", t);
		}
		if (result == 0) {
			((TreeTableViewer)viewer).getDefaultComparator().compare(e1, e2);
		}
		return (reverse ? -result : result);
	}
	
}
