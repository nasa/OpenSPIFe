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

import gov.nasa.ensemble.common.event.ModelChangeEvent;
import gov.nasa.ensemble.common.event.ModelChangeListener;
import gov.nasa.ensemble.common.event.ModelChangeNotifier;

import java.util.ArrayList;
import java.util.List;

public class NotifyingRegistry extends Registry implements ModelChangeNotifier {

	private List<ModelChangeListener> listeners;

	public NotifyingRegistry() {
		super();
		listeners = new ArrayList<ModelChangeListener>();
	}

	@Override
	public <K, V, R> void register(K key, V newValue) {
		Object oldValue = getRegisteredValue(key);
		super.register(key, newValue);
		Action notificationType = Action.REGISTERED;
		if (oldValue != null) {
			notificationType = Action.UPDATED;
		}
		ModelChangeEvent modelChangeEvent = new ModelChangeEvent(key, notificationType, oldValue, newValue);
		fireModelChangeEvent(modelChangeEvent);
	}

	@Override
	public <K> void unregisterKey(K key) {
		if (this.isRegistered(key)) {
			Object oldValue = getRegisteredValue(key);
			super.unregisterKey(key);
			ModelChangeEvent modelChangeEvent = new ModelChangeEvent(key, Action.UNREGISTERED, oldValue, null);
			fireModelChangeEvent(modelChangeEvent);
		}
	}

	@Override
	public void fireModelChangeEvent(ModelChangeEvent modelChangeEvent) {
		for (ModelChangeListener listener : listeners) {
			listener.modelChanged(modelChangeEvent);
		}
	}

	@Override
	public void addModelChangeListener(ModelChangeListener modelChangeListener) {
		if (!listeners.contains(modelChangeListener)) {
			listeners.add(modelChangeListener);
		}
	}

	@Override
	public void removeModelChangeListener(ModelChangeListener modelChangeListener) {
		listeners.remove(modelChangeListener);
	}

	public enum Action {
		REGISTERED, UPDATED, UNREGISTERED
	}
}
