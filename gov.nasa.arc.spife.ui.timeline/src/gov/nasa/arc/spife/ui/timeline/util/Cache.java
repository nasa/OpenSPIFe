package gov.nasa.arc.spife.ui.timeline.util;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Closeable;

/**
 * Base class for cache implementations.
 */
public abstract class Cache<K,V> implements Closeable {
  
  /**
   * Simple Cache wrapper that synchronizes all
   * calls that access the cache. 
   */
  static class SynchronizedCache<K,V> extends Cache<K,V> {
    private Object mutex;
    private Cache<K,V> cache;
    
    SynchronizedCache(Cache<K,V> cache) {
      this.cache = cache;
      this.mutex = this;
    }
    
    SynchronizedCache(Cache<K,V> cache, Object mutex) {
      this.cache = cache;
      this.mutex = mutex;
    }
    
    @Override
    public void put(K key, V value) {
      synchronized(mutex) {cache.put(key, value);}
    }
    
    @Override
    public V get(Object key) {
      synchronized(mutex) {return cache.get(key);}
    }
    
    @Override
    public boolean containsKey(Object key) {
      synchronized(mutex) {return cache.containsKey(key);}
    }
    
    @Override
    public void close() {
      synchronized(mutex) {cache.close();}
    }
    
    @Override
    Cache<K,V> getSynchronizedCache() {
      return this;
    }
  }
  
  /**
   * Returns a thread-safe cache backed by the specified cache. 
   * In order to guarantee thread-safety, all access to the backed cache must
   * be accomplished through the returned cache.
   */
  public static <K,V> Cache<K,V> synchronizedCache(Cache<K,V> cache) {
    return cache.getSynchronizedCache();
  }

  /**
   * Called by {@link #synchronizedCache(Cache)}. This method
   * returns a {@link SynchronizedCache} instance that wraps
   * this instance by default and can be overridden to return
   * e. g. subclasses of {@link SynchronizedCache} or this
   * in case this cache is already synchronized.
   */
  Cache<K,V> getSynchronizedCache() {
    return new SynchronizedCache<K,V>(this);
  }
  
  /**
   * Puts a (key, value)-pair into the cache. 
   */
  public abstract void put(K key, V value);
  
  /**
   * Returns the value for the given key. 
   */
  public abstract V get(Object key);
  
  /**
   * Returns whether the given key is in this cache. 
   */
  public abstract boolean containsKey(Object key);
  
  /**
   * Closes the cache.
   */
  public abstract void close();
  
}