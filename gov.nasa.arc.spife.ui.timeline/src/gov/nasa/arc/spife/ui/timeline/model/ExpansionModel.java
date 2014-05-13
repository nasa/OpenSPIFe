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
package gov.nasa.arc.spife.ui.timeline.model;

import gov.nasa.arc.spife.ui.timeline.util.DefaultModel;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ExpansionModel extends DefaultModel {

	public static final String ID = ExpansionModel.class.getName();
	public static final String EXPANDED = "expanded";
	
	private Set<Object> expandedElements = new LinkedHashSet<Object>();
	
	public boolean isExpanded(Object pe) {
		return expandedElements.contains(pe);
	}
	
	public void setExpanded(Collection elements, boolean expanded) {
		if (elements.isEmpty()) {
			return;
		}
		if (expanded) {
			Set<Object> newlyExpanded = new LinkedHashSet<Object>(elements);
			newlyExpanded.removeAll(expandedElements);
			if (!newlyExpanded.isEmpty()) {
				expandedElements.addAll(newlyExpanded);
				firePropertyChange(EXPANDED, null, newlyExpanded);
			}
		} else {
			Set<Object> formerlyExpanded = new LinkedHashSet<Object>(elements);
			formerlyExpanded.retainAll(expandedElements);
			if (!formerlyExpanded.isEmpty()) {
				expandedElements.removeAll(formerlyExpanded);
				firePropertyChange(EXPANDED, formerlyExpanded, null);
			}
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		expandedElements.clear();
	}
	
}
