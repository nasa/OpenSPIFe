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

import gov.nasa.ensemble.common.CommonUtils;

import org.eclipse.core.commands.common.EventManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class Selector<T> extends EventManager {

	private static final String VALUE = "selected value";
	private final Class<T> valueType;
	private final Button button;
	private T value;
	
	public Selector(Class<T> valueType, FormToolkit toolkit, Composite parent) {
		this.valueType = valueType;
		this.button = toolkit.createButton(parent, "", SWT.PUSH);
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				open(button.getShell());
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				open(button.getShell());
			}
		});
	}
	
	/**
	 * Implement in child to open the selector dialog
	 * @param shell 
	 */
	protected abstract void open(Shell shell);

	public Class<T> getValueType() {
		return valueType;
	}
	
	public Button getButton() {
		return button;
	}
    
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		T oldValue = getValue();
		if (!CommonUtils.equals(value, oldValue)) {
			this.value = value;
			fireValueChange(oldValue, value);
		}
	}
	
    private void fireValueChange(T oldValue, T newValue) {
    	final Object[] finalListeners = getListeners();
	    if (finalListeners.length > 0) {
	        PropertyChangeEvent pEvent = new PropertyChangeEvent(this, VALUE, oldValue, newValue);
	        for (int i = 0; i < finalListeners.length; ++i) {
	            IPropertyChangeListener listener = (IPropertyChangeListener) finalListeners[i];
	            listener.propertyChange(pEvent);
	        }
	    }
	}

	public void setEnabled(boolean state) {
    	getButton().setEnabled(state);
    }

    public void addListener(IPropertyChangeListener listener) {
    	addListenerObject(listener);
    }

    public void removeListener(IPropertyChangeListener listener) {
    	removeListenerObject(listener);
    }
    
   
	
}
