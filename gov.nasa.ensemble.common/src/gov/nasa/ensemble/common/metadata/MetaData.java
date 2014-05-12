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
package gov.nasa.ensemble.common.metadata;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

public class MetaData extends MetaMetaData {
	
	private final Map<String, Reference> data = Collections.synchronizedMap(new HashMap<String, Reference>());
	
	private final List<PropertyChangeListener> untypedListeners = 
		Collections.synchronizedList(new ArrayList<PropertyChangeListener>());
	
	private final Map<String, List<PropertyChangeListener>> typedListeners = 
		Collections.synchronizedMap(new HashMap<String, List<PropertyChangeListener>>());
	
	public <T> T get(String key) {
		final Reference ref = data.get(key);
		if (ref == null)
			return (T)MetaExtensionPoint.getDefault(getOwner(), key).orSome((T)null);
		return (T)ref.get();
	}
	
	public <T> T get(String key, Callable<T> initializer) {
		synchronized (data) {
			T result = get(key);
			if (result == null) {
				try {
					result = initializer.call();
					put(key, result);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return result;
		}
	}
	
	public <T> T put(String key, Object value) {
		final Reference oldRef = data.put(key, new WeakReference(value));
		final T oldValue = oldRef == null ? null : (T)oldRef.get();
		fireEvent(new PropertyChangeEvent(getOwner(), key, oldValue, value));
		return oldValue;
	}

	public <T> T remove(String key) {
		final Reference ref = data.remove(key);
		if (ref == null)
			return null;
		T oldValue = (T)ref.get();
		fireEvent(new PropertyChangeEvent(getOwner(), key, oldValue, null));
		return oldValue;
	}
	
	public void addListener(PropertyChangeListener listener, String... keys) {
		if (keys == null || keys.length == 0) {
			untypedListeners.add(listener);
		} else {
			for (String key : keys) {
				getListeners(key).add(listener);
			}
		}
	}

	public void removeListener(PropertyChangeListener listener, String... keys) {
		if (keys == null || keys.length == 0) {
			untypedListeners.remove(listener);
		} else {
			for (String key : keys) {
				getListeners(key).remove(listener);
			}
		}
	}
	
	public List<PropertyChangeListener> getListeners(String key) {
		synchronized (typedListeners) {
			List<PropertyChangeListener> listeners = typedListeners.get(key);
			if (listeners == null) {
				listeners = Collections.synchronizedList(new ArrayList<PropertyChangeListener>());
				typedListeners.put(key, listeners);
			}
			return listeners;
		}
	}
	
	private void fireEvent(PropertyChangeEvent event) {
		final String key = event.getPropertyName();
		final List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

		// get global extension point-registered listeners
		for (PropertyChangeListener listener : MetaExtensionPoint.getListeners(key))
			listeners.add(listener);
		
		// get instance-registered untyped listeners
		synchronized (untypedListeners) {
			listeners.addAll(untypedListeners);
		}
		
		final List<PropertyChangeListener> typedListeners = getListeners(key);
		synchronized (typedListeners) {
			listeners.addAll(typedListeners);
		}
		
		for (PropertyChangeListener listener : listeners)
			notifyListener(listener, event);
	}
	
	private void notifyListener(PropertyChangeListener listener, PropertyChangeEvent event) {
		try {
			listener.propertyChange(event);
		} catch (Throwable t) {
			LogUtil.error(t);
		}
	}

	public void poke(String key) {
		final Object value = get(key);
		fireEvent(new PropertyChangeEvent(getOwner(), key, value, value));
	}
	
	public Set<String> keySet() {
		return Collections.unmodifiableSet(data.keySet());
	}
	
	public Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<String, Object>();
		synchronized (data) {
			final Set<Entry<String,Reference>> entrySet = data.entrySet();
			for (Entry<String, Reference> entry : entrySet) {
				final Object value = entry.getValue().get();
				if (value != null)
					map.put(entry.getKey(), value);
			}
		}
		return Collections.unmodifiableMap(map);
	}
}
