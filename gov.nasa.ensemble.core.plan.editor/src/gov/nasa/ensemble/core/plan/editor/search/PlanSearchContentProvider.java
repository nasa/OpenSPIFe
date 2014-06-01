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
package gov.nasa.ensemble.core.plan.editor.search;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;

/**
 * This class provides the tree structure for the search results.
 * An invisible root is used to make the top node of each branch 
 * a plan.
 * 
 * @author abenavides
 */
public class PlanSearchContentProvider implements ITreeContentProvider, ISearchResultListener {

	private final boolean flat;
	private ColumnViewer viewer;
	public PlanSearchResult fSearchResult;
	protected class InvisibleRoot { /* empty */ }
	public InvisibleRoot invisibleRoot = new InvisibleRoot();
	
	public PlanSearchContentProvider(boolean flat) {
		this.flat = flat;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(invisibleRoot);
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (ColumnViewer) viewer;
		if (fSearchResult != null) {
		    fSearchResult.removeListener(this);
		}
		fSearchResult= (PlanSearchResult) newInput;
		if (fSearchResult != null) {
		    fSearchResult.addListener(this);
		}
	}

	public void clear() {	
		fSearchResult.clear();
		if (viewer != null) {
			viewer.refresh();
		}
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (flat) {
			return getFlattenedChildren(fSearchResult.getPlans());
		}
		if (parentElement instanceof InvisibleRoot){
			return fSearchResult.getPlans();
		}
		return fSearchResult.getChildrenToShow(parentElement);
	}

	private Object[] getFlattenedChildren(Object[] plans) {
		Queue<Object> leftovers = new LinkedList<Object>(Arrays.asList(plans));
		List<Object> flattenedChildren = new ArrayList<Object>();
		while (true) {
			Object object = leftovers.poll();
			if (object == null) {
				break;
			}
			Object[] children = fSearchResult.getChildrenToShow(object);
			if ((children == null) || children.length == 0) {
				flattenedChildren.add(object);
			} else {
				leftovers.addAll(Arrays.asList(children));
			}
		}
		return flattenedChildren.toArray();
	}

	@Override
	public Object getParent(Object element) {
		if (element == invisibleRoot) {
			return null;
		}
		if ((element instanceof EPlan) || flat) {
			return invisibleRoot;
		}
		return ((EPlanElement)element).eContainer();
	}

	@Override
	public boolean hasChildren(Object element) {	
		Object[] children = getChildren(element);
		if (children == null || children.length == 0) {
			return false;	
		}
		return true;
	}

	@Override
	public void searchResultChanged(SearchResultEvent event) {
		if (viewer != null) {
			viewer.refresh();
		}
	}
	
	@Override
	public void dispose() {
		// do nothing
	}
}
