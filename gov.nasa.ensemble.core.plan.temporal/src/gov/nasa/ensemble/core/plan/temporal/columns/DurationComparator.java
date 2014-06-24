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
package gov.nasa.ensemble.core.plan.temporal.columns;

import java.util.Comparator;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class DurationComparator implements Comparator<Amount<Duration>> {

	public static final DurationComparator INSTANCE = new DurationComparator();
	
    @Override
	public int compare(Amount<Duration> duration1, Amount<Duration> duration2) {
	    if (duration1 == duration2) {
	    	return 0;
	    }
	    if (duration1 == null) {
	    	return -1;
	    }
	    if (duration2 == null) {
	    	return 1;
	    }
	    return duration1.compareTo(duration2);
    }
    
}
