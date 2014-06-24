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

import java.util.EventListener;

/**
 * <P>Interface for classes that want to be notified when an async prefetch
 * completes.</P>
 *
 * <P>Copyright 2001, California Institute of Technology.<BR>
 * ALL RIGHTS RESERVED. U.S. Government Sponsorship acknowledged.</P>
 *
 * @see CacheWithAsyncFetch
 *
 * @author Marty Vona
 **/
public interface PrefetchListener extends EventListener {

  /**
   * <P>Called by the {@link CacheWithAsyncFetch} when an asynchronous
   * prefetch has finished (either normally or because it threw an Exception)
   * or been cacelled.</P>
   *
   * <P>Remember that the cache will maintain a direct reference to the Object
   * until the next get() for the Object (or the next releaseAll()) unless one
   * of the PrefetchListeners for the Object returns true from
   * prefetchFinished.</P>
   * 
   * @param key the key for the Object that was fetched
   * @param object the Object that was fetched unless prefetchTime ==
   * THREW_EXCEPTION or CANCELLED
   * @param prefetchTime approx number of milliseconds from time of prefetch
   * request till time of prefetch completion.  Negative indicates a special
   * condition, either {@link #ALREADY_CACHED}, {@link #THREW_EXCEPTION}, or
   * {@link #CANCELLED}.
   *
   * @return true iff the cache should release the Object and allow it to be
   * garbage collected once all of the prefetchFinished() events have been
   * fired for the Object and no external direct references to the Object
   * remain
   **/
  public boolean prefetchFinished(Object key, Object object, long prefetchTime);

  /**
   * <P>Called by the {@link CacheWithAsyncFetch} when an Exception has been
   * thrown during an asynchronous prefetch.</P>
   *
   * @param key the key for the Object that was fetched
   * @param e the Exception that occurred
   **/
  public void prefetchExceptionOccurred(Object key, Exception e);

  /**
   * <P>Constant supplied to {@link #prefetchFinished} to indicate that no
   * fetch was required.</P>
   **/
  public static final int ALREADY_CACHED = -1;

  /**
   * <P>Constant supplied to {@link #prefetchFinished} to indicate that the
   * prefetch finished because it was cancelled.</P>
   **/
  public static final int CANCELLED = -2;

  /**
   * <P>Constant supplied to {@link #prefetchFinished} to indicate that the
   * prefetch finished because it threw an exception.</P>
   **/
  public static final int THREW_EXCEPTION = -3;

  /**
   * <P>Human-readable names for the condition constants.</P>
   *
   * @see CacheWithAsyncFetch#getPrefetchTimeConditionName
   **/
  public static final String[] CONDITION_NAME =
  {"Already Cached", "Cancelled", "Threw Exception"};
}
