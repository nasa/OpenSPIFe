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

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * An implementation of AutoCollectionMap where the collection is created as a LinkedHashSet.
 * 
 * @author abachmann
 * 
 * @param <K>
 *            the type of the key
 * @param <V>
 *            the type of the values in the collection
 */
public class AutoSetMap<K, V> extends AutoCollectionMap<K, Set<V>> {

	public AutoSetMap(Class<K> keyClass) {
		super(keyClass);
	}

	@Override
	protected Set<V> createCollection() {
		return new LinkedHashSet<V>();
	}

}
