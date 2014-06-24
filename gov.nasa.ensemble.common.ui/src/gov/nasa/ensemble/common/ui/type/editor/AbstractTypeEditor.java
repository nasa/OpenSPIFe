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
package gov.nasa.ensemble.common.ui.type.editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This is a convenience abstract class which implements ITypeEditor and
 * provides property change support.
 * 
 * @see gov.nasa.ensemble.common.ui.type.editor.ITypeEditor
 */
public abstract class AbstractTypeEditor<T> implements ITypeEditor {
	
    private T value;
    private PropertyChangeSupport pcSupport = null;
	private final Class<T> type;
	
	public static final String OBJECT = "editor.object";
	
    public AbstractTypeEditor(Class<T> type) {
    	this.type = type;
		pcSupport = new PropertyChangeSupport(this);
    }
    
	/**
     * Set the value of the property being edited.  Primitive types 
     * such as "int" must be wrapped as the corresponding object 
     * type such as "java.lang.Integer".
     *
     * @param object The new value for the target object.
     */
    @Override
	public void setObject(Object object) {
		Object oldValue = this.value;
		this.value = type.cast(object);
		firePropertyChange(oldValue, this.value);
	}
	
    /**
     * Gets the property value.
     *
     * @return The value of the property.  Primitive types such as "int" will
     * be wrapped as the corresponding object type such as "java.lang.Integer".
     */
	@Override
	public T getObject() {
		return value;
	}

	/**
	 * @return the type for this AbstractTypeEditor
	 */
	public Class<T> getType() {
		return type;
	}
	
    /**
     * Report that we have been modified to any interested listeners.
     */
	protected void firePropertyChange(Object oldValue, Object newValue) {
		pcSupport.firePropertyChange(OBJECT, oldValue, newValue);
	}
	
	/**
	 * Register a listener for the PropertyChange event. When a PropertyEditor
	 * changes its value it should fire a PropertyChange event on all registered
	 * PropertyChangeListeners, specifying the null value for the property name
	 * and itself as the source.
	 * 
	 * @param listener
	 *            An object to be invoked when a PropertyChange event is fired.
	 */
	@Override
	public final void addPropertyChangeListener(PropertyChangeListener listener) {
		pcSupport.addPropertyChangeListener(listener);
	}
	
    /**
     * Remove a listener for the PropertyChange event.
     *
     * @param listener  The PropertyChange listener to be removed.
     */
	@Override
	public final void removePropertyChangeListener(PropertyChangeListener listener) {
		pcSupport.removePropertyChangeListener(listener);
	}

}
