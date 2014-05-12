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
/*
 * Created on Sep 13, 2004
 */
package gov.nasa.ensemble.common.ui.image;

import gov.nasa.ensemble.common.cache.SizeReporterLRUCache;


/**
 * Singleton class that holds a mapping of keys to MemorySizeReporterImages.  The keys may be
 * any object, though in most cases it will be a String.
 * The values contained in the SWTImageCache should extend MemorySizeReporterImage.
 */
@SuppressWarnings("serial")
public class SWTImageCache extends SizeReporterLRUCache<Object, MemorySizeReporterImage> {
 	
    public static final long DEFAULT_CACHE_SIZE = 60 * 1024L * 1024L;

	protected SWTImageCache(long maxSizeInBytes) {
		super(maxSizeInBytes);
	}

    public static SWTImageCache getInstance() {
    	if (cache == null) cache = new SWTImageCache(DEFAULT_CACHE_SIZE);
    	return cache;
    }

    public MemorySizeReporterImage put(MemorySizeReporterImage image) {
    	return super.put(image, image);
    }
    
	private static SWTImageCache cache;
}


