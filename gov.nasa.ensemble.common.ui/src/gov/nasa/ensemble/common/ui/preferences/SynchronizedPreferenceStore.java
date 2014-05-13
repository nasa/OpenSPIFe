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
package gov.nasa.ensemble.common.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

public class SynchronizedPreferenceStore implements IPreferenceStore {

	private IPreferenceStore delegate;

	public SynchronizedPreferenceStore(IPreferenceStore store) {
		delegate = store;
	}
	
	
	// generated code (added synchronized keyword) 
	
	public synchronized void addPropertyChangeListener(IPropertyChangeListener listener) {
		delegate.addPropertyChangeListener(listener);
	}

	public synchronized boolean contains(String name) {
		return delegate.contains(name);
	}

	@Override
	public synchronized boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	public synchronized void firePropertyChangeEvent(String name, Object oldValue, Object newValue) {
		delegate.firePropertyChangeEvent(name, oldValue, newValue);
	}

	public synchronized boolean getBoolean(String name) {
		return delegate.getBoolean(name);
	}

	public synchronized boolean getDefaultBoolean(String name) {
		return delegate.getDefaultBoolean(name);
	}

	public synchronized double getDefaultDouble(String name) {
		return delegate.getDefaultDouble(name);
	}

	public synchronized float getDefaultFloat(String name) {
		return delegate.getDefaultFloat(name);
	}

	public synchronized int getDefaultInt(String name) {
		return delegate.getDefaultInt(name);
	}

	public synchronized long getDefaultLong(String name) {
		return delegate.getDefaultLong(name);
	}

	public synchronized String getDefaultString(String name) {
		return delegate.getDefaultString(name);
	}

	public synchronized double getDouble(String name) {
		return delegate.getDouble(name);
	}

	public synchronized float getFloat(String name) {
		return delegate.getFloat(name);
	}

	public synchronized int getInt(String name) {
		return delegate.getInt(name);
	}

	public synchronized long getLong(String name) {
		return delegate.getLong(name);
	}

	public synchronized String getString(String name) {
		return delegate.getString(name);
	}

	@Override
	public synchronized int hashCode() {
		return delegate.hashCode();
	}

	public synchronized boolean isDefault(String name) {
		return delegate.isDefault(name);
	}

	public synchronized boolean needsSaving() {
		return delegate.needsSaving();
	}

	public synchronized void putValue(String name, String value) {
		delegate.putValue(name, value);
	}

	public synchronized void removePropertyChangeListener(IPropertyChangeListener listener) {
		delegate.removePropertyChangeListener(listener);
	}

	public synchronized void setDefault(String name, boolean value) {
		delegate.setDefault(name, value);
	}

	public synchronized void setDefault(String name, double value) {
		delegate.setDefault(name, value);
	}

	public synchronized void setDefault(String name, float value) {
		delegate.setDefault(name, value);
	}

	public synchronized void setDefault(String name, int value) {
		delegate.setDefault(name, value);
	}

	public synchronized void setDefault(String name, long value) {
		delegate.setDefault(name, value);
	}

	public synchronized void setDefault(String name, String defaultObject) {
		delegate.setDefault(name, defaultObject);
	}

	public synchronized void setToDefault(String name) {
		delegate.setToDefault(name);
	}

	public synchronized void setValue(String name, boolean value) {
		delegate.setValue(name, value);
	}

	public synchronized void setValue(String name, double value) {
		delegate.setValue(name, value);
	}

	public synchronized void setValue(String name, float value) {
		delegate.setValue(name, value);
	}

	public synchronized void setValue(String name, int value) {
		delegate.setValue(name, value);
	}

	public synchronized void setValue(String name, long value) {
		delegate.setValue(name, value);
	}

	public synchronized void setValue(String name, String value) {
		delegate.setValue(name, value);
	}
}
