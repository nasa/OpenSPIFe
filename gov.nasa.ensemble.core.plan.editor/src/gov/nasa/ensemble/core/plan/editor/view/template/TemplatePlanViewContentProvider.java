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
package gov.nasa.ensemble.core.plan.editor.view.template;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.provider.EPlanElementTreeContentProvider;
import gov.nasa.ensemble.core.plan.editor.search.PlanIndexer;
import gov.nasa.ensemble.core.plan.editor.search.PlanSearcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

public class TemplatePlanViewContentProvider extends EPlanElementTreeContentProvider {	
	protected PlanSearcher searcher = null;
	
	private PlanIndexer indexer = null;
	protected Set<EPlanElement> currentElements = null;
	private Object inputElement = null; 
	private String searchInput = "not initialized";

	@Override
	public void dispose() {
		super.dispose();
		if (currentElements != null) {
			currentElements.clear();
			currentElements = null;
		}
		
		PlanIndexer planIndexer = getIndexer();
		if(planIndexer != null) {
			planIndexer.clear();
		}
		if (searcher != null) {
			searcher.clearQueries();
			searcher.clearResults();
			searcher = null;
		}
	}

	protected boolean shouldDisplayActivityGroup(EActivityGroup eActivityGroup) {
		boolean result = false;
		if(eActivityGroup != null) {
			if (currentElements != null && hasAncestorMatch(eActivityGroup)) {
				return true;
			}
			for(EPlanChild ePlanChild : eActivityGroup.getChildren()) {
				if (ePlanChild instanceof EActivityGroup) {
					result = shouldDisplayActivityGroup((EActivityGroup)ePlanChild);
				} else if (ePlanChild instanceof EActivity) {
					if (currentElements != null) {
						result = currentElements.contains(ePlanChild);		
					}
				}
				if (result == true) {
					break;
				}
			}
		}
		
		return result;
	}
	
	@Override
	public Object[] getChildren(Object parent) {
		List<?> children = null;
		if (parent instanceof EPlanElement) {
			children = ((EPlanElement)parent).getChildren();
		} else if(contentProvider == null) {
			children = Collections.emptyList();
		} else {
			children = Arrays.asList(contentProvider.getChildren(parent));
		}
		return getMatchedPlanElements(children);
	}

	protected Object[] getMatchedPlanElements(List<?> children) {
		List<EPlanElement> planElements = new ArrayList<EPlanElement>();
		for (Object child : children) {
			if (child instanceof EPlanElement) {
				EPlanElement ePlanElement = (EPlanElement)child;
				if(ePlanElement instanceof EActivityGroup) {
					if(shouldDisplayActivityGroup((EActivityGroup)ePlanElement)) {
						planElements.add(ePlanElement);
					}
				}
								
				else if(ePlanElement instanceof EActivity && currentElements != null) {
					if (hasAncestorMatch(ePlanElement)) {
						planElements.add(ePlanElement);
					}
				}
			}
		}
		return planElements.toArray(new EPlanElement[planElements.size()]);
	}
	
	protected boolean hasAncestorMatch(EPlanElement element) {
		if (currentElements.contains(element)) {
			return true;
		}
		Object parent = getParent(element);
		if (parent instanceof EPlanChild) {
			return hasAncestorMatch((EPlanElement)parent);
		}
		return false;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement == null) {
			inputElement = this.inputElement;
		}
		return super.getElements(inputElement);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		super.inputChanged(viewer, oldInput, newInput);
		
		if (newInput == null) {
			return;
		}
	
		this.inputElement = newInput;
		initializeSearch(viewer, oldInput, newInput);
	}

	protected final void initializeSearch(Viewer viewer, Object oldInput, Object newInput) {
		currentElements = null;
		
		indexer = new PlanIndexer();
		searcher = new PlanSearcher(indexer.getFileDirectory());
		searchInput = null;
		
		doInputChanged(viewer, oldInput, newInput);
		
		indexer.refresh();
		searchInputChanged("", "");
	}

	public void doInputChanged(Viewer viewer, Object oldInput, Object newInput) {
		EPlan plan = (EPlan) newInput;
		indexElements(plan.getChildren());
	}
	
	protected void indexElements(List<? extends EPlanChild> ePlanChildren) {
		for(EPlanChild ePlanChild : ePlanChildren) {
			getIndexer().indexElementByLabel(ePlanChild);
			
			indexElements(ePlanChild.getChildren());
		}
	}
	
	protected void updateSearchForNewElements(List<EPlanChild> planChildren) {
		indexElements(planChildren);
		getIndexer().refresh();
		searcher.refresh();
		searcher.zearch();
		List<String> resultIDs = getResultIDs();
		
		Set<EPlanElement> resultElements = new LinkedHashSet<EPlanElement>();
		for (String resultID : resultIDs) {
			resultElements.add(getIdentifiableRegistry()
					.getIdentifiable(EPlanElement.class, resultID));	
		}			
		
		currentElements = resultElements;
	}

	@Override
	protected void addChildren(EPlanElement element, List<EPlanElement> childrenAdded) {
		List<EPlanChild> planChildren = new ArrayList<EPlanChild>();
		for (EPlanElement child : childrenAdded) {
			if (child instanceof EPlanChild) {
				planChildren.add((EPlanChild)child);
			}
		}
		updateSearchForNewElements(planChildren);
		super.addChildren(element, childrenAdded);
		viewer.setSelection(new StructuredSelection(childrenAdded), true);
	}

	protected void searchInputChanged(String fieldName, String text) {
		if(CommonUtils.equals(searchInput, text.toLowerCase()) || searcher == null) {
			return;
		}
		
		searchInput = text.toLowerCase();
		searcher.refresh();
		searcher.clearQueries();
		
		if(searchInput.equals("")){
			searcher.addQuery(PlanSearcher.DEFAULT_QUERY);
		}
		else{
			searcher.addQueryForWordsContaining(fieldName.toLowerCase(), searchInput);
		}
		searcher.zearch();
		
		List<String> resultIDs = getResultIDs();
		
		Set<EPlanElement> resultElements = new LinkedHashSet<EPlanElement>();
		for (String resultID : resultIDs) {
			resultElements.add(getIdentifiableRegistry()
					.getIdentifiable(EPlanElement.class, resultID));	
		}			
		
		currentElements = resultElements;
	}

	protected IdentifiableRegistry<EPlanElement> getIdentifiableRegistry() {
		return getIndexer().idRegistry;
	}
	
	protected Vector<String> getResultIDs() {
		return searcher.getResultIDs();
	}

	public PlanIndexer getIndexer() {
		return indexer;
	}	
}
