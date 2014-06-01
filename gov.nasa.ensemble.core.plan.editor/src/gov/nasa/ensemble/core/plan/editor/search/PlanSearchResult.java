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
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.text.AbstractTextSearchResult;
import org.eclipse.search.ui.text.IEditorMatchAdapter;
import org.eclipse.search.ui.text.IFileMatchAdapter;
import org.eclipse.search.ui.text.Match;
import org.eclipse.ui.IEditorPart;

/**
 * This class  maintains the collections of all search results 
 * that need to display in the search view. This is done by keeping
 * an array, elementsToShow, of PlanElements that must be shown 
 * by the tree content provider.
 * 
 * @author abenavides
 */
public class PlanSearchResult extends AbstractTextSearchResult implements IEditorMatchAdapter{
	
	private PlanSearchQuery searchQuery;
	private String label;
	private ArrayList<Object> elementsToShow = new ArrayList<Object>();
	
	public PlanSearchResult(PlanSearchQuery planSearchQuery, String label) {
		this.searchQuery = planSearchQuery;
		this.label = label;
	}

	public void clear(){	
		elementsToShow.clear();
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getLabel() {
		return label + " - " + getMatchCount() + " references.";
	}
	
	public String getSearchLabel(){
		return label;
	}

	@Override
	public ISearchQuery getQuery() {
		return searchQuery;
	}

	@Override
	public String getTooltip() {
		return null;
	}
	
	@Override
	public void addListener(ISearchResultListener l) {
		// do nothing
	}

	@Override
	public void removeListener(ISearchResultListener l) {
		// do nothing
	}

	@Override
	public IEditorMatchAdapter getEditorMatchAdapter() {
		return this;
	}

	@Override
	public IFileMatchAdapter getFileMatchAdapter() {
		return null;
	}

	@Override
	public Match[] computeContainedMatches(AbstractTextSearchResult result, IEditorPart editor) {
		return null;
	}

	@Override
	public boolean isShownInEditor(Match match, IEditorPart editor) {
		return false;
	}
	
	/**
	 * Add the match and fill in the elementsToShow array.
	 * 
	 * @param entry
	 */
	public void addAll(Object entry) {
		addMatch(new Match(entry,0,0));
		fillMatchHeirarchy(entry);
	}
	
	/**
	 * Add the plan element and all the plan elements above the 
	 * specified initial entry to the elementsToShow array. This 
	 * is so that we know every element that needs to be displayed
	 * in the tree view.
	 * 
	 * @param entry
	 */
	public void fillMatchHeirarchy(Object entry){
		if (entry == null) {
			return;
		} else {
			if(!elementsToShow.contains(entry)){
				elementsToShow.add(entry);
			    EObject container = ((EPlanElement)entry).eContainer();
			    if (!(container instanceof EPlanElement)) {
			    	container = null;
			    }
				fillMatchHeirarchy(container);
			}
			else{
				// short circuit to increase speed
				return;
			}
		}
	}
	
	/**
	 * This method finds the intersection of children for the
	 * parent item and the items that need to be displayed in 
	 * the tree view. 
	 * 
	 * @param parent
	 * @return array of children that need to be displayed
	 */
	public Object[] getChildrenToShow(Object parent){
		List<? extends EPlanElement> children = EPlanUtils.getChildren((EPlanElement)parent);
		ArrayList<Object> retVal = new ArrayList<Object>(children.size());
		for (Object child : children){
			if (elementsToShow.contains(child)) {
				retVal.add(child);
			}
		}		
		sortByStartTime(retVal, false);
		return retVal.toArray();
	}
	
	private static void sortByStartTime(List<Object> ePlanElements, final boolean sortDescendingOrder) {
		Collections.sort(ePlanElements, new Comparator<Object>() {
			@Override
			public int compare(Object e1, Object e2) {
				TemporalMember m1 = ((EPlanElement)e1).getMember(TemporalMember.class);
				TemporalMember m2 = ((EPlanElement)e2).getMember(TemporalMember.class);
				int compareTo = m1.getStartTime().compareTo(m2.getStartTime());
				if(sortDescendingOrder) {
					compareTo *= -1;
				}
				return compareTo;
			}			
		});
	}	
	
	/**
	 * This method iterates through all the elements that need to be
	 * displayed and creates an array of root nodes (plans) for the
	 * search results.
	 * 
	 * @return array of plans involved in the search result items
	 */
	public Object[] getPlans(){
		ArrayList<Object> retVal = new ArrayList<Object>();
		for (Object element : elementsToShow){
			if (element instanceof EPlan){
				retVal.add(element);
			}
		}
		return retVal.toArray();
	}
}
