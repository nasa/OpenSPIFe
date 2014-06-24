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
package gov.nasa.ensemble.core.jscience.util;

import gov.nasa.ensemble.core.jscience.DataPoint;

import java.util.Comparator;
import java.util.Date;

public class DataPointComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {
		Date d1 = null;
		Date d2 = null;
		if (o1 instanceof Date)
			d1 = (Date) o1;
		if (o2 instanceof Date)
			d2 = (Date) o2;
		if (o1 instanceof DataPoint)
			d1 = ((DataPoint) o1).getDate();
		if (o2 instanceof DataPoint)
			d2 = ((DataPoint) o2).getDate();
		if (d1 == null || d2 == null)
			throw new NullPointerException();
		return d1.compareTo(d2);
	}

}
