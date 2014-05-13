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
package gov.nasa.ensemble.common.collections;

import gov.nasa.ensemble.common.CommonUtils;

import java.util.Comparator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * This default comparator implements a comparison that will always return the same result for two objects. The ordering will depend
 * on the order in which the comparator are exposed to the objects but users of this class are advised to not rely on this behavior.
 * 
 * The comparator tracks comparable items using a WeakHashMap so odd behaviors may occur when comparing objects that have custom
 * keys. (those that implement hashCode and equals) It is recommended to use this class with objects that implement equals as ==.
 * 
 * @author abachmann
 * 
 * @param <T>
 */
public class EncounterOrderComparator<T> implements Comparator<T> {

	private static final Map<Object, Long> map = new WeakHashMap<Object, Long>();
	private static long counter = Long.MIN_VALUE;

	public static EncounterOrderComparator<Object> INSTANCE = new EncounterOrderComparator<Object>();

	@Override
	public int compare(T o1, T o2) {
		long l1 = getValue(o1);
		long l2 = getValue(o2);
		return CommonUtils.compare(l1, l2);
	}

	private static long getValue(Object o) {
		Long value = map.get(o);
		if (value == null) {
			synchronized (EncounterOrderComparator.class) {
				value = counter++;
				map.put(o, value);
			}
		}
		return value.longValue();
	}

}
