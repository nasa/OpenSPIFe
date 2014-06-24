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

import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.plan.resources.dependency.Dependency;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Comparator;
import java.util.Date;

import javax.measure.quantity.Dimensionless;
import javax.measure.unit.Unit;

import org.jscience.physics.amount.Amount;

public interface TemporalActivityDependency extends Dependency {

	public static final Amount<Dimensionless> AMOUNT_ZERO = AmountUtils.exactZero(Unit.ONE);
	public static final Amount<Dimensionless> AMOUNT_MINUS_ONE = AmountUtils.toAmount(-1, Unit.ONE);
	public static final Amount<Dimensionless> AMOUNT_ONE = AmountUtils.toAmount(1, Unit.ONE);

	public Date getDate();
	
	public Timepoint getTimepoint();
	
	public DependencyMaintenanceSystem getDependencyMaintenanceSystem();

	public EActivity getActivity();

	public static class TemporalActivityDependencyComparator implements Comparator<TemporalActivityDependency> {
		
		public static TemporalActivityDependencyComparator INSTANCE = new TemporalActivityDependencyComparator();

		@Override
		public int compare(TemporalActivityDependency o1, TemporalActivityDependency o2) {
			DataPoint pt1 = (DataPoint)o1.getValue();
			DataPoint pt2 = (DataPoint)o2.getValue();
			Date date1 = pt1.getDate();
			Date date2 = pt2.getDate();
			int dateComparison = date1.compareTo(date2);
			
			int i = dateComparison;
			//
			// Sorting in order to make sure that the end effects get evaluated before the start
			// effects. This is not entirely necessary since we now merge the effects, but
			// may wish to uncomment in case we wish to preserve profile but-cracking
			if (i == 0) {
				Timepoint t0 = o1.getTimepoint();
				Timepoint t1 = o2.getTimepoint();
				if (t0 == Timepoint.END && t1 == Timepoint.START) {
					return -1;
				} else if (t0 == Timepoint.START && t1 == Timepoint.END) {
					return 1;
				}
			}
			return i;
		}
		
	}
	
}
