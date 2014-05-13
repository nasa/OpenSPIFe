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
 * Created on Apr 13, 2005
 */
package gov.nasa.arc.spife.ui.timeline.util;

import gov.nasa.ensemble.common.CommonUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * @author aaghevli
 */
public class DefaultModel {
	
	private final Logger trace = Logger.getLogger(getClass());
	
	private transient Set<PropertyChangeListener> listeners = new HashSet<PropertyChangeListener>();
	
	/**
	 * Add a PropertyChangeListener to the listener list. The listener is registered for all properties. The same listener object
	 * may be added more than once, and will be called as many times as it is added. If <code>listener</code> is null, no exception
	 * is thrown and no action is taken.
	 * 
	 * @param l
	 *            The PropertyChangeListener to be added
	 */
	public void addPropertyChangeListener(PropertyChangeListener l) {
		synchronized (listeners) {
			listeners.add(l);
		}
	}

    public void removePropertyChangeListener(PropertyChangeListener l) {
    	synchronized (listeners) {
    		listeners.remove(l);
    	}
    }
  
    protected void firePropertyChange(String prop, Object oldValue, Object newValue) {
    	if (CommonUtils.equals(oldValue, newValue)) {
    	    return;
    	}
    	PropertyChangeEvent event = new PropertyChangeEvent(this, prop, oldValue, newValue);
    	List<PropertyChangeListener> listeners;
    	synchronized (this.listeners) {
			listeners = new ArrayList<PropertyChangeListener>(this.listeners);
    	}
		for (PropertyChangeListener l : listeners) {
			try {
				l.propertyChange(event);
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				trace.error("throwable from property change listener", t);
			}
		}
    }
    
	/**
	 * Remove all registered listeners and clear the list.
	 */
	public void dispose() {
		synchronized (listeners) {
			listeners.clear();
		}
	}

}
