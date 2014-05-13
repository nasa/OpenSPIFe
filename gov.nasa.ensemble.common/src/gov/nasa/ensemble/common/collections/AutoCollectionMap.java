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

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * This class is an abstract implementation of a map where the value is a collection. When get(key) is called and the map does not
 * contain the key, an instance of the collection will be created automatically and inserted into the map. This collection will be
 * returned.
 * 
 * @author abachmann
 * @see AutoSetMap
 * @see AutoListMap
 * 
 * @param <K>
 *            the type of the key
 * @param <C>
 *            the type of the collection
 */
public abstract class AutoCollectionMap<K, C extends Collection> extends LinkedHashMap<K, C> {

	private final Class<K> keyClass;

	public AutoCollectionMap(Class<K> keyClass) {
		this.keyClass = keyClass;
	}

	protected abstract C createCollection();

	@Override
	public C get(Object key) {
		if (!containsKey(key)) {
			if (key != null && !keyClass.isInstance(key)) {
				return null;
			}
			K castKey = keyClass.cast(key);
			C collection = createCollection();
			put(castKey, collection);
			return collection;
		}
		return super.get(key);
	}

}
