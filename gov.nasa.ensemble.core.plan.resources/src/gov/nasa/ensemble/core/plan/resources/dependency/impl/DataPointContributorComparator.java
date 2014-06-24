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
package gov.nasa.ensemble.core.plan.resources.dependency.impl;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.core.plan.resources.dependency.impl.TemporalActivityDependency.TemporalActivityDependencyComparator;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Comparator;
import java.util.Date;

public class DataPointContributorComparator implements Comparator<Dependency> {

	public static final DataPointContributorComparator INSTANCE = new DataPointContributorComparator();

	@Override
	public int compare(Dependency o1, Dependency o2) {
		DataPoint pt1 = (DataPoint) o1.getValue();
		DataPoint pt2 = (DataPoint) o2.getValue();
		Date date1 = pt1.getDate();
		Date date2 = pt2.getDate();
		int comparison = date1.compareTo(date2);
		if (comparison != 0) {
			return comparison;
		}
		if (o1 instanceof TemporalActivityDependency && o2 instanceof TemporalActivityDependency) {
			return TemporalActivityDependencyComparator.INSTANCE.compare((TemporalActivityDependency) o1, (TemporalActivityDependency) o2);

		} else if (o1 instanceof TemporalActivityDependency && o2 instanceof ConditionDependency) {
			// If TAD is a start effect, always comes after the evaluation of
			// the condition
			if (Timepoint.START == ((TemporalActivityDependency) o1).getTimepoint()) {
				return 1;
			} // else, TAD is an end effect, evaluate before
			return -1;
		} else if (o1 instanceof ConditionDependency && o2 instanceof TemporalActivityDependency) {
			// If TAD is a start effect, always comes after the evaluation of
			// the condition
			if (Timepoint.START == ((TemporalActivityDependency) o2).getTimepoint()) {
				return -1;
			} // else, TAD is an end effect, evaluate before
			return 1;
		} else if (o1 instanceof ConditionDependency && o2 instanceof ConditionDependency) {
			LogUtil.errorOnce("Comparing two condtions at the same time: "
					+ o1.getName() + " and " + o2.getName() + " both start at "
					+ date1);
			return 0;
		}
		return 0;
	}

}
