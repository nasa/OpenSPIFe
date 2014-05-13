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

import org.eclipse.swt.widgets.Control;

public interface ITypeEditor {
	
	/**
     * Set the value of the property being edited.  Primitive types 
     * such as "int" must be wrapped as the corresponding object 
     * type such as "java.lang.Integer".
     *
     * @param object The new value for the target object.
     */
    void setObject(Object object);
	
    /**
     * Gets the property value.
     *
     * @return The value of the property.  Primitive types such as "int" will
     * be wrapped as the corresponding object type such as "java.lang.Integer".
     */
    Object getObject();
	
	/**
	 * Returns the editing widget that is capable of editing this value
	 * graphically.
	 * @return not supplied
	 */
	Control getEditorControl();
	
	 /**
     * Register a listener for the PropertyChange event.  When a
     * PropertyEditor changes its value it should fire a PropertyChange
     * event on all registered PropertyChangeListeners, specifying the
     * null value for the property name and itself as the source.
     *
     * @param listener  An object to be invoked when a PropertyChange
     *		event is fired.
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove a listener for the PropertyChange event.
     *
     * @param listener  The PropertyChange listener to be removed.
     */
    void removePropertyChangeListener(PropertyChangeListener listener);
	
}
