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
package gov.nasa.ensemble.core.detail.emf.binding;

import java.util.List;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.AbstractObservableList;

public class DuplexingObservableList extends AbstractObservableList {

	/**
	 * List of observables to observe and report changes from
	 */
	private final List<IObservable> observables;
	
	/**
	 * Type of element contained within the value of the observable
	 */
	private final Object elementType;

	/**
	 * Listener that forwards messages to external clients
	 */
	private final Listener listener = new Listener();
	
	public DuplexingObservableList(List<IObservable> observables, Object elementType) {
		if (observables == null) {
			throw new IllegalArgumentException("observables can not be null");
		}
		this.observables = observables;
		this.elementType = elementType;
	}
	
	@Override
	protected void firstListenerAdded() {
		for (IObservable v : getObservables()) {
			v.addChangeListener(listener);
		}
		super.firstListenerAdded();
	}

	@Override
	protected void lastListenerRemoved() {
		for (IObservable v : getObservables()) {
			v.removeChangeListener(listener);
		}
		super.lastListenerRemoved();
	}

	public List<IObservable> getObservables() {
		return observables;
	}

	@Override
	protected int doGetSize() {
		return getObservables().size();
	}

	@Override
	public Object get(int index) {
		return getObservables().get(index);
	}

	@Override
	public Object getElementType() {
		return elementType;
	}

	private final class Listener implements IChangeListener {
		@Override
		public void handleChange(ChangeEvent event) {
			getRealm().asyncExec(new Runnable() {

				@Override
				public void run() {
					fireChange();
				}
				
			});
		}
	}
	
}
