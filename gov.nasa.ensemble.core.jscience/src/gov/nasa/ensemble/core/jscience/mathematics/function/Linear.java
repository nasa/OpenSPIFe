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
/**
 * 
 */
package gov.nasa.ensemble.core.jscience.mathematics.function;

import java.util.Date;
import java.util.SortedMap;

import javax.measure.quantity.Quantity;

import org.jscience.mathematics.function.Interpolator;
import org.jscience.physics.amount.Amount;

public class Linear<F extends Quantity> implements Interpolator<Date, Amount<F>> {
	@Override
	public Amount<F> interpolate(Date point, SortedMap<Date, Amount<F>> pointValues) {
		// Searches exact.
		Amount<F> y = pointValues.get(point);
	    if (y != null)
	        return y;

	    // Searches surrounding points/values.
	    SortedMap<Date, Amount<F>> headMap = pointValues.headMap(point);
	    Date x1 = headMap.lastKey();
	    Amount<F> y1 = headMap.get(x1);
	    SortedMap<Date, Amount<F>> tailMap = pointValues.tailMap(point);
	    Date x2 = tailMap.firstKey();
	    Amount<F> y2 = tailMap.get(x2);

	    // Interpolates.
	    final Date x = point;
		double pct = (x.getTime() - x1.getTime()) / (float) (x2.getTime() - x1.getTime());
		Amount<F> value = y2.minus(y1).times(pct).plus(y1);
	    return value;
	}
}
