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

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

public class SelectorObservableValue extends AbstractObservableValue {

	private final Selector selector;
	private final Class valueType;
	private final WidgetListener widgetListener = new WidgetListener();
	
	public SelectorObservableValue(Selector selector) {
		this.selector = selector;
		this.valueType = selector.getValueType();
		selector.addListener(widgetListener);
		selector.getButton().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		});
	}
	
	@Override
	public synchronized void dispose() {
		selector.removeListener(widgetListener);
		super.dispose();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void doSetValue(Object value) {
		selector.setValue(value);
	}
	
	@Override
	protected Object doGetValue() {
		return selector.getValue();
	}

	private final class WidgetListener implements IPropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			Object oldValue = event.getOldValue();
			Object newValue = event.getNewValue();
			fireValueChange(Diffs.createValueDiff(oldValue, newValue));
		}
	}

	@Override
	public Object getValueType() {
		return valueType;
	}
	
}
