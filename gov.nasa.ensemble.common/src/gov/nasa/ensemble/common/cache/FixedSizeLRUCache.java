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
package gov.nasa.ensemble.common.cache;

import gov.nasa.ensemble.common.event.EventListenerList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An LRU cache with a bounded capacity.
 */
public class FixedSizeLRUCache<K, V> extends LinkedHashMap<K, V> {

    /**
     * The load factor used when none specified in constructor.
     **/
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    /**
     * The default initial capacity - MUST be a power of two.
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    
	private long maxSize;
	private long currentSize;
	private EventListenerList eventListeners;
	
	/**
	 * @param maxSize
	 */
	public FixedSizeLRUCache(long maxSize) {
		/*
		 * setting access order true here will result in LRU access order to the map entries.
		 */
		super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
		if (maxSize <= 0)
			throw new IllegalArgumentException("Must have a positive maximum size");
		this.maxSize = maxSize;
		currentSize = 0;
	}

	@Override
	public synchronized void clear() {
		Collection<V> values = new ArrayList<V>(values());
		super.clear();
		currentSize = 0;
		for (V value: values)
			notifyListenersOfRemove(value);
	}

	@Override
	protected boolean removeEldestEntry(Entry<K, V> eldest) {
		Iterator<V> iterator = values().iterator();
		while (currentSize > maxSize) {
			iterator.next();
			iterator.remove(); // this calls remove() which accounts for the reduction in memory size
		}
		return false;
	}

	@Override
	public synchronized V put(K key, V value) {
		currentSize += getSizeOf(value);
		V rv = super.put(key, value);
		// if there was something there before... then remove the size it took up
		if (rv != null) {
			currentSize -= getSizeOf(rv);
			if (!value.equals(rv)) notifyListenersOfRemove(rv);
		}
		return rv;
	}

	@Override
	public synchronized V remove(Object key) {
		V rv = super.remove(key);
		if (rv != null) {
			currentSize -= getSizeOf(rv);
			notifyListenersOfRemove(rv);
		}
		return rv;
	}
	
	/**
	 * This method merely refreshes whatever object in the cache corresponds to
	 * the key, designating it the newest object, and hence the farthest away
	 * from being removed.
	 * 
	 * @param key the key of the object to be touched.
	 */
	public synchronized void touch(K key) {
		V object = super.remove(key);
		if (object != null)
			super.put(key, object);
	}

	@Override
	public synchronized boolean containsValue(Object value) {
		return super.containsValue(value);
	}

	@Override
	public synchronized V get(Object key) {
		return super.get(key);
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		return super.containsKey(key);
	}

	@Override
	public synchronized void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
	}

	public long getMaxSizeInBytes() {
		return maxSize;
	}

	public void setMaxSizeInBytes(long maxSizeInBytes) {
		this.maxSize = maxSizeInBytes;
		if (currentSize > maxSizeInBytes) 
			removeEldestEntry(null); 
	}
	
	public long getRemainingSize() {
		return maxSize - currentSize;
	}

	public long getCurrentSizeInBytes() {
		return currentSize;
	}
	
	public void addLRUCacheRemoveListener(LRUCacheListener listener) {
		if (eventListeners == null) eventListeners = new EventListenerList();
		eventListeners.add(listener);
	}
	
	public void removeLRUCacheRemoveListener(LRUCacheListener listener) {
		if (eventListeners == null) return;
		eventListeners.remove(listener);
	}
	
	@SuppressWarnings("unchecked")
	private void notifyListenersOfRemove(V object) {
		if (eventListeners == null) return;
		for (EventListener listener : eventListeners.getListenerList()) {
			((LRUCacheListener)listener).objectRemoved(object);
		}
	}

	/**
	 * Subclasses may override to specify the size that various items take up
	 */
	protected long getSizeOf(V value) {
		return 1;
	}
}
