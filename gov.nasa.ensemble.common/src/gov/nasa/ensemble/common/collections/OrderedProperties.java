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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

/**
 * This class implements Properties, but maintains the order of the keys.
 * 
 * @author abachman
 *
 */
public class OrderedProperties extends Properties {

	private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>(); 
	
	@Override
	public synchronized Enumeration<Object> keys() {
		return new Enumeration<Object>() {
			
			private Iterator<Object> iterator = keys.iterator();

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public Object nextElement() {
				return iterator.next();
			}
			
		};
	}
	
	@Override
	public Set<Object> keySet() {
		return keys;
	}
	
	@Override
	public synchronized Object remove(Object key) {
		Object result = super.remove(key);
		keys.remove(key);
		return result;
	}
	
	@Override
	public synchronized Object put(Object key, Object value) {
		keys.add(key);
		return super.put(key, value);
	}
	
}
