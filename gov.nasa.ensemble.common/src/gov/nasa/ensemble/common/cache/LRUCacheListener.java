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
 * Listener for removal events from the LRUCache. It is important that implementers of this method not attempt to manipulate the
 * LRUCache during the execution of this method. It is even more important that the implementors not attempt to read the removed
 * object.
 * 
 * @author mpowell
 * 
 */
public interface LRUCacheListener<V> extends EventListener {
	/**
	 * Notify the listener that an object has been removed from the cache. This method is often used to free resources that this
	 * object consumes, e.g. memory, disk space.
	 * 
	 * @param reporter
	 */
	public void objectRemoved(V reporter);
}
