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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationKey;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class ViolationViewerComparator extends ViewerComparator {

	private ViolationKey key = ViolationKey.TIME;
	private boolean reverse = false;
	
	public ViolationKey getKey() {
		return key;
	}

	public void setKey(ViolationKey key) {
		this.key = key;
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		return (reverse ? -1 : 1) * order(viewer, e1, e2);
	}

	private int order(Viewer viewer, Object e1, Object e2) {
		if ((e1 instanceof PlanAdvisorGroup) && (e2 instanceof PlanAdvisorGroup)) {
			PlanAdvisorGroup group1 = (PlanAdvisorGroup) e1;
			PlanAdvisorGroup group2 = (PlanAdvisorGroup) e2;
			String value1 = group1.getValue();
			String value2 = group2.getValue();
			return String.CASE_INSENSITIVE_ORDER.compare(value1, value2);
		}
		if ((e1 instanceof ViolationTracker) && (e2 instanceof ViolationTracker)) {
			ViolationTracker t1 = (ViolationTracker)e1;
			ViolationTracker t2 = (ViolationTracker)e2;
			Violation violation1 = t1.getViolation();
			Violation violation2 = t2.getViolation();
			int result = violation1.compareBy(violation2, key);
			if (result == 0) {
				result = t1.compareTo(t2);
			}
			return result;
		}
		return super.compare(viewer, e1, e2);
	}
	
}
