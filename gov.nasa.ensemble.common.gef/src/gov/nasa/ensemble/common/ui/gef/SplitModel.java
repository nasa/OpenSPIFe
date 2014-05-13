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
package gov.nasa.ensemble.common.ui.gef;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SplitModel implements SplitConstants {

	public static final String DIVIDER_LOCATION = "DividerLocation";
	
	private int dividerLocation = 0;
	
	private int orientation = -1;
	
	public SplitModel(int orientation) {
		this.orientation = orientation;
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public int getDividerLocation() {
		return dividerLocation;
	}
	
	public void setDividerLocation(int location) {
		int oldLocation = getDividerLocation();
		dividerLocation = location;
		firePropertyChange(DIVIDER_LOCATION, oldLocation, dividerLocation);
	}
	
	protected transient PropertyChangeSupport changers = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changers.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
    	changers.removePropertyChangeListener(l);
    }
  
    protected void firePropertyChange(String prop, Object old, Object newValue) {
    	changers.firePropertyChange(prop, old, newValue);
    }
    
}
