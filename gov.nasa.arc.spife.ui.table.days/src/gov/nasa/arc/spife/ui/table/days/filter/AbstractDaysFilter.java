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
package gov.nasa.arc.spife.ui.table.days.filter;

import gov.nasa.ensemble.common.ui.treetable.TreeTableViewerFilter;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;

import org.eclipse.jface.viewers.Viewer;

/**
 * Abstract Days Viewer Filter class!
 * 
 * @author ideliz
 */
public abstract class AbstractDaysFilter extends TreeTableViewerFilter {

	private final PlanVisitor visitor = new MatchPlanVisitor();
	private final String name;

	public AbstractDaysFilter(String name) {
		this.name = name;
	}

	/**
	 * Checks the abstract method for 'filterCondition' for EActivity,
	 * if it's not an activity it checks if his children activities
	 * obey the condition and if it does show it!  
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof EActivity) {
			if (filterCondition(element)) {
				return true;
			}
		} else if (element instanceof EPlanElement) {
			try {
				visitor.visitAll((EPlanElement)element);
			} catch (MatchException m) {
				return true;
			}
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * abstract method to evaluate if the element gets down in the filter
	 * */
	public abstract boolean filterCondition(Object element);

	private static final class MatchException extends RuntimeException {
		// marker exception
	}
	private final class MatchPlanVisitor extends PlanVisitor {
		@Override
		protected void visit(EPlanElement element) {
			if (element instanceof EActivity) {
				EActivity activity = (EActivity) element;
				if (filterCondition(activity)) {
					throw new MatchException();
				}
			}
		}
	}

}
