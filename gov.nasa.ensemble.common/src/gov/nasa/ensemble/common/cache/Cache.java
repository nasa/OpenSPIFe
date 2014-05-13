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

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * <P>
 * Base class for a cache of Objects with blocking fetch.
 * </P>
 * 
 * <P>
 * A Cache is a bit like a hash table except
 * <UL>
 * <LI>it knows how to fetch its own objects given their keys</LI>
 * <LI>objects it contains may be garbage collected if no external direct
 * references remain to them</LI>
 * </UL>
 * </P>
 * 
 * <P>
 * Note that the objects used as keys must have working hashValue() <I>and</I>
 * equals() methods.
 * </P>
 * 
 * <P>
 * The Cache is implemented with SoftReferences, so its contents should not be
 * garbage collected until memory actually becomes low.
 * </P>
 * 
 * <P>
 * Concrete subclasses must implement the {@link #fetch} method to fetch
 * cacheable objects as necessary.
 * </P>
 * 
 * <P>
 * Thread safety: As long as {@link #fetch} is implemented to be re-entrant, all
 * public methods of this class are properly re-entrant.
 * </P>
 * 
 * <P>
 * Copyright 2001, California Institute of Technology.<BR>
 * ALL RIGHTS RESERVED. U.S. Government Sponsorship acknowledged.
 * </P>
 * 
 * @author Marty Vona
 */
///CLOVER:OFF
public abstract class Cache {

	/**
	 * <P>
	 * Get the Object associated with the specified key.
	 * </P>
	 * 
	 * <P>
	 * Incurs a blocking {@link #fetch} iff the requested Object is not
	 * currently in the Cache. Only a SoftReference is kept to the Object, so no
	 * guarantee can be made that the Object will remain in the cache once all
	 * external direct references to the Object are released.
	 * </P>
	 * 
	 * @param key
	 *            identifies the Object to get
	 * 
	 * @return a reference to the requested Object
	 * 
	 * @exception FetchFailedException
	 *                if the Object was not cached and there was a problem
	 *                {@link #fetch}ing it (this is counted as a cache miss for
	 *                the purpose of statistics)
	 */
	public Object get(Object key) throws FetchFailedException {
		return update(key, true).get();
	}

	/**
	 * <P>
	 * Attempt to get a reference to the Object, but don't flag the Object for
	 * potential garbage collection.
	 * </P>
	 * 
	 * @param key
	 *            identifies the Object to get
	 * 
	 * @return a reference to the Object, or null if it has been garbage
	 *         collected or if has not yet been fetched
	 */
	public Object getIffCached(Object key) {

		CacheElement cacheElement = elements.get(key);

		if (cacheElement == null) {
			missCount++;
			return null;
		}

		Object object = cacheElement.getIffCached();

		if (object != null)
			hitCount++;
		else
			missCount++;

		return object;
	}

	/**
	 * <P>
	 * Ensure that the Object associated with the specified key is in the cache.
	 * </P>
	 * 
	 * <P>
	 * Incurs a blocking {@link #fetch} iff the requested Object is not
	 * currently in the Cache. A direct reference is kept to the Object until
	 * the next call to {@link #get} with the same key (or until {@link
	 * #releaseAll} is called), thus on return the Object is guaranteed to stay
	 * in the cache at least until it is next retrieved.
	 * </P>
	 * 
	 * <P>
	 * Thread safety: As long as {@link #fetch} is implemented to be re-entrant,
	 * all public methods of this class are properly re-entrant (but it will
	 * never be the case that this method is called concurrently on the same
	 * key).
	 * </P>
	 * 
	 * @param key
	 *            identifies the Object to prefetch
	 * 
	 * @exception FetchFailedException
	 *                if the Object was not cached and there was a problem
	 *                {@link #fetch}ing it
	 */
	public void prefetch(Object key) throws FetchFailedException {
		update(key, true);
	}

	/**
	 * <P>
	 * Downgrade any existing direct references to cached objects to
	 * SoftReferences.
	 * </P>
	 */
	public void releaseAll() {
		synchronized (elements) {
			for (Iterator<CacheElement> i = (elements.values()).iterator(); i.hasNext();)
				i.next().get();
		}
	}

	/**
	 * <P>
	 * Remove the Object corresponding to the specified key from the cache.
	 * </P>
	 * 
	 * @param key
	 *            identifies the Object to remove
	 * 
	 * @return true iff the key was actually removed
	 */
	public boolean remove(Object key) {

		synchronized (elements) {

			CacheElement cacheElement = elements.get(key);

			if (cacheElement != null) {

				cacheElement.removed = true;
				elements.remove(key);

				return true;

			}
			return false;
		}
	}

	/**
	 * <P>
	 * Remove all Objects currently in the cache.
	 * </P>
	 */
	public void flush() {

		synchronized (elements) {

			for (Iterator<CacheElement> i = (elements.values()).iterator(); i.hasNext();)
				i.next().removed = true;

			elements.clear();
		}
	}

	/**
	 * <P>
	 * Check and see if the Object associated with the specified key is
	 * currently in the cache.
	 * </P>
	 * 
	 * <P>
	 * Note that even if guarantee == true, nothing is to stop another thread
	 * from getting the object and subsequently releasing all its direct
	 * references to the object at any time. So the next get() by this thread
	 * may still incur a {@link #fetch}.
	 * </P>
	 * 
	 * @param key
	 *            identifies the Object to check
	 * @param guarantee
	 *            if true, then a true return value from this method means that
	 *            the Object is guaranteed to be in the cache until the next
	 *            {@link #get} for this key. Otherwise, even if this method
	 *            returns true, the next get for this key may require a
	 *            {@link #fetch} (because if all external direct references to
	 *            the object are released then the object may be garbage
	 *            collected before the next get)
	 * 
	 * @return true iff the object is in the cache at the time of return,
	 *         however unless guarantee == true the object may still be garbage
	 *         collected before the next {@link #get}
	 */
	public boolean isCached(Object key, boolean guarantee) {

		CacheElement cacheElement = elements.get(key);

		if (cacheElement == null)
			return false;

		if (guarantee)
			return cacheElement.refresh();
		return cacheElement.isSoftRefValid();
	}

	/**
	 * <P>
	 * Get total cumulative number of cache hits.
	 * </P>
	 * 
	 * @return total cumulative number of cache hits
	 */
	public int getHitCount() {
		return hitCount;
	}

	/**
	 * <P>
	 * Get total cumulative number of cache misses.
	 * </P>
	 * 
	 * @return total cumulative number of cache misses
	 */
	public int getMissCount() {
		return missCount;
	}

	/**
	 * <P>
	 * Get percent of hits relative to total number of accesses.
	 * </P>
	 * 
	 * @return percent of hits relative to total number of accesses
	 */
	public double getHitPercent() {

		int total = getHitCount() + getMissCount();

		return (total != 0) ? ((double) getHitCount()) / ((double) total)
				* 100.0 : 0.0;
	}

	/**
	 * <P>
	 * Get percent of misses relative to total number of accesses.
	 * </P>
	 * 
	 * @return percent of misses relative to total number of accesses
	 */
	public double getMissPercent() {

		int total = getHitCount() + getMissCount();

		return (total != 0) ? ((double) getMissCount()) / ((double) total)
				* 100.0 : 0.0;
	}

	/**
	 * <P>
	 * Get a human-readable summary of the cumulative cache performance.
	 * </P>
	 * 
	 * @return a human-readable summary of the cumulative cache performance
	 */
	public String getPerformanceSummary() {
		return "hits: " + getHitCount() + " (" + ((int) getHitPercent())
				+ "%); misses: " + getMissCount() + " ("
				+ (100 - ((int) getHitPercent())) + "%)";
	}

	/**
	 * <P>
	 * Do whatever is necessary to get an instance of the Object that
	 * corresponds to the supplied key.
	 * </P>
	 * 
	 * <P>
	 * Must be implemented to be properly re-entrant in order to guarantee that
	 * {@link #get} and {@link #prefetch} are re-entrant.
	 * </P>
	 * 
	 * @param key
	 *            identifies the Object to fetch
	 * 
	 * @exception FetchFailedException
	 *                if there was a problem fetching the Object (e.g. not
	 *                found, or I/O error). If the FetchFailedException is
	 *                incurred directly because of another Exception then its
	 *                <CODE>cause</CODE> will be set to the latter Exception.
	 */
	abstract protected Object fetch(Object key) throws FetchFailedException;

	/**
	 * <P>
	 * Ensure that there is an entry in {@link #elements} for the specified key,
	 * and if the directRef for that element is null then {@link
	 * CacheElement#update} it.
	 * </P>
	 * 
	 * @param key
	 *            identifies the object to prefetch
	 * @param updateCounts
	 *            whether to update {@link #hitCount} and {@link #missCount}
	 * 
	 * @return the {@link CacheElement} assocated with the key
	 * 
	 * @exception FetchFailedException
	 *                if the Object was not cached and there was a problem
	 *                {@link #fetch}ing it
	 */
	protected CacheElement update(Object key, boolean updateCounts)
			throws FetchFailedException {

		CacheElement cacheElement;

		// Don't let anybody else simultaneously modify the elements table.
		synchronized (elements) {

			// get the element
			cacheElement = elements.get(key);

			// not there, need to fetch it for the first time
			if (cacheElement == null) {

				cacheElement = new CacheElement(key);

				// put the new element in the table
				elements.put(key, cacheElement);
			}
		}

		synchronized (cacheElement) {

			// try to refresh the directRef from the softRef
			if (!cacheElement.refresh()) {

				// softRef stale, must fetch again

				if (updateCounts)
					missCount++;

				cacheElement.update(key);

			} else if (updateCounts) {

				// soft ref was good

				hitCount++;

			}
		}

		return cacheElement;
	}

	/**
	 * <p>
	 * Get an unmodifiable view of the set of keys in this cache.
	 * </p>
	 * 
	 * <p>
	 * This set will track updates to the cache.
	 * </p>
	 * 
	 * @return an unmodifiable view of the set of keys in this cache
	 */
	public Set<Object> getKeySet() {
		return unmodifiableKeySet;
	}

	/**
	 * <P>
	 * Encapsulates an element of the cache.
	 * </P>
	 * 
	 * <P>
	 * Note that CacheElements get removed from {@link #elements} automatically
	 * and asynchronously when the garbage collector informs the
	 * {@link CacheElementSoftReference} they contain that the soft reference is
	 * no longer reachable (and the CacheElement has not already been flagged as
	 * {@link #removed}.
	 * </P>
	 */
	protected class CacheElement {

		/**
		 * <P>
		 * Create a new CacheElement that references the Object corresponding to
		 * the supplied key.
		 * </P>
		 * 
		 * @param key
		 *            identifies the object to reference
		 * 
		 * <P>
		 * The key is stored by reference. It better not mutate!
		 * </P>
		 * 
		 * <P>
		 * Both the {@link #directRef} and the {@link #softRef} are initialized
		 * to null.
		 * </P>
		 */
		CacheElement(Object key) {

			this.key = key;

			directRef = null;
			softRef = new CacheElementSoftReference(null);
		}

		/**
		 * <P>
		 * Attempt to get a reference to the Object, simultaneously flagging the
		 * Object for potential garbage collection once all external direct
		 * references to it have been freed.
		 * </P>
		 * 
		 * <P>
		 * If the {@link #directRef} is valid, that is returned, but it is
		 * invalidated for the future. Otherwise, the {@link #softRef} is
		 * returned.
		 * </P>
		 * 
		 * @return a reference to the Object, or null if it has been garbage
		 *         collected (or if it has not yet been fetched)
		 */
		Object get() {

			if (directRef != null) {

				Object object = directRef;

				directRef = null;

				return object;

			}
			return softRef.get();
		}

		/**
		 * <P>
		 * Attempt to get a reference to the Object, but don't flag the Object
		 * for potential garbage collection.
		 * </P>
		 * 
		 * <P>
		 * The {@link #softRef} is returned. The validity of the {@link
		 * #directRef} is not modified.
		 * </P>
		 * 
		 * @return a reference to the Object, or null if it has been garbage
		 *         collected (or if it has not yet been fetched)
		 */
		Object getIffCached() {
			return softRef.get();
		}

		/**
		 * <P>
		 * Check if the {@link #softRef} is valid.
		 * </P>
		 * 
		 * @return true iff the softRef is not null
		 */
		boolean isSoftRefValid() {
			return (softRef.get() != null);
		}

		/**
		 * <P>
		 * Attempt to refresh the {@link #directRef} from the {@link #softRef}.
		 * </P>
		 * 
		 * @return true if the {@link #softRef} was still valid, false otherwise
		 */
		boolean refresh() {

			directRef = softRef.get();

			return (directRef != null);
		}

		/**
		 * <P>
		 * Update both the {@link #directRef} and the {@link #softRef} to
		 * reference the object associated with the key that was supplied to the
		 * constructor.
		 * </P>
		 * 
		 * <P>
		 * Incurs a blocking fetch() of the object.
		 * </P>
		 * 
		 * @param key
		 *            identifies the Object to fetch
		 * 
		 * @exception FetchFailedException
		 *                if there was problem during the {@link Cache#fetch}
		 */
		void update(Object key) throws FetchFailedException {

			// Some keys come with extra, possibly ephemeral data used to
			// service the
			// fetch. This extra data does not affect the identity of the key,
			// but we
			// must be careful to use the most recently supplied key because the
			// extra
			// data from the old key might be stale.
			if (this.key != null) {
				if (!(this.key).equals(key))
					trace.warn("previous key (" + this.key
							+ ") does not match new key (" + key + ")");
			} else {
				trace.assertLog(key == null, "Key should be null.");
			}

			this.key = key;

			directRef = fetch(key);
			softRef = new CacheElementSoftReference(directRef);
		}

		/**
		 * <P>
		 * Subclass of SoftReference that helps manage removal of stale
		 * CacheElements.
		 * </P>
		 */
		protected class CacheElementSoftReference extends SoftReference<Object> {

			/**
			 * <P>
			 * Constructor registers this CacheElementSoftReference with a
			 * private static singleton ReferenceQueue that is accessable to all
			 * {@link Cache} instances.
			 * </P>
			 */
			CacheElementSoftReference(Object referent) {
				super(referent, CACHE_ELEMENT_CLEANUP_QUEUE);
			}

			/**
			 * <P>
			 * Returns the {@link Cache.CacheElement} to which this
			 * CacheElementSoftReference belongs.
			 * </P>
			 * 
			 * @return the Cache.CacheElement to which this
			 *         CacheElementSoftReference belongs
			 */
			CacheElement getCacheElement() {
				return CacheElement.this;
			}

			/**
			 * <P>
			 * Returns the {@link Cache} to which this CacheElementSoftReference
			 * belongs.
			 * </P>
			 * 
			 * @return the Cache to which this CacheElementSoftReference belongs
			 */
			Cache getCache() {
				return Cache.this;
			}
		}

		/**
		 * <P>
		 * A SoftReference to the Object that remembers what CacheElement it
		 * belongs to.
		 * </P>
		 */
		protected CacheElementSoftReference softRef;

		/**
		 * <P>
		 * A direct reference to the Object.
		 * </P>
		 */
		protected Object directRef;

		/**
		 * <P>
		 * Whether we have been {@link Cache#remove}d yet.
		 * </P> *
		 */
		protected boolean removed = false;

		/**
		 * <p>
		 * Our key.
		 * </p> *
		 */
		protected Object key;
	}

	/** Our CacheElements. * */
	protected Map<Object, CacheElement> elements = Collections
			.synchronizedMap(new HashMap<Object, CacheElement>());

	/** an unmodifiable view of the keySet of {@link #elements} * */
	protected Set<Object> unmodifiableKeySet = Collections
			.unmodifiableSet(elements.keySet());

	/** How many cache hits we've had. * */
	protected int hitCount = 0;

	/** How many cache misses we've had. * */
	protected int missCount = 0;

	// singleton ReferenceQueue to which all
	// CacheElement.CacheElementSoftReferences register themselves
	private static final ReferenceQueue<Object> CACHE_ELEMENT_CLEANUP_QUEUE = new ReferenceQueue<Object>();

	// singleton, low priority, daemon thread that hangs around and waits for
	// CacheElment.CacheElementSoftReferences to enter
	// CACHE_ELEMENT_CLEANUP_QUEUE, then pulls them off and removes their
	// CacheElements from the elements list in their respective Caches.
	private static final Thread CACHE_ELEMENT_CLEANUP_THREAD = new Thread(
			"CacheElement Cleanup Thread") {

		{
			setDaemon(true);
			setPriority(MIN_PRIORITY);
		}

		@Override
		public void run() {

			for (;;) {

				try {

					// this will wait() until something is in the queue
					CacheElement.CacheElementSoftReference softRef = (CacheElement.CacheElementSoftReference) CACHE_ELEMENT_CLEANUP_QUEUE
							.remove();

					Cache cache = softRef.getCache();
					CacheElement cacheElement = softRef.getCacheElement();

					// Trace.debug(
					// "SoftRef CacheElement for key " + cacheElement.key +
					// " has been enqueued for cleanup.");

					// remove the corresponding CacheElement from its Cache
					if (!cacheElement.removed) {
						synchronized (cache) {
							(cache.elements).remove(cacheElement.key);
						}
					}

					// Trace.debug(
					// "Auto removed CacheElement for key " + cacheElement.key +
					// ".");

				} catch (InterruptedException e) {
					continue;
				}
			}
		}
	};

	// Here's where we start the CACHE_ELEMENT_CLEANUP_THREAD
	static {
		CACHE_ELEMENT_CLEANUP_THREAD.start();
	}
	/* Setup logging */
	private static final Logger trace = Logger.getLogger(Cache.class);
}
