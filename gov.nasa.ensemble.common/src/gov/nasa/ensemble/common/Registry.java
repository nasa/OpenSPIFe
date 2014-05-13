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
package gov.nasa.ensemble.common;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A wrapper around a Map, re-naming the needed operations (the Gang of Four Adapter pattern, but not an Eclipse adapter).
 */
public class Registry {

	/** The underlying Map. */
	protected Map map;

	/** The only constructor. */
	public Registry() {
		map = new WeakHashMap();
	}

	/**
	 * Map the given key to the given value. The postcondition is that isRegistered(key) returns true and that getRegisteredValue()
	 * returns value.
	 * 
	 * @param <K>
	 *            the type of the key
	 * @param <V>
	 *            the type of the value
	 * @param <R>
	 *            unused
	 * @param key
	 *            the key under which the value is mapped
	 * @param value
	 *            the value mapped to the key
	 */
	public <K, V, R> void register(K key, V value) {
		map.put(key, value);
	}

	/**
	 * Unmap the given key; it will no longer be mapped to a value. The postcondition is that isRegistered(key) returns false.
	 * 
	 * @param <K>
	 *            the type of the key
	 * @param key
	 *            the key to unmap
	 */
	public <K> void unregisterKey(K key) {
		map.remove(key);
	}

	/**
	 * Return the value mapped to the given key.
	 * 
	 * @param <K>
	 *            the type of the key
	 * @param <V>
	 *            the type of the value
	 * @param key
	 *            the key whose value is sought
	 * @return the value that was mapped to the key, if any; otherwise null
	 */
	public <K, V> V getRegisteredValue(K key) {
		return (V) map.get(key);
	}

	/**
	 * Query whether the given key is mapped to a value without retrieving that value.
	 * 
	 * @param <K>
	 *            the type of the key
	 * @param key
	 *            the key
	 * @return whether the key is mapped to a vaue
	 */
	public <K> boolean isRegistered(K key) {
		return map.containsKey(key);
	}
}
