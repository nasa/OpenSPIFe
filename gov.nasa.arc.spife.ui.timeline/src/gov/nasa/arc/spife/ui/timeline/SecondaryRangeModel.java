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
package gov.nasa.arc.spife.ui.timeline;

import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.RangeModel;

/**
 * This Secondary range model follows the primary range model.
 * Changes to min/max/extent on the secondary range model are
 * ignored in favor of the primary range model. 
 * 
 * @author abachman
 */
public class SecondaryRangeModel implements RangeModel {

	private final RangeModel primaryRangeModel;

	public SecondaryRangeModel(RangeModel primaryRangeModel) {
		this.primaryRangeModel = primaryRangeModel;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		primaryRangeModel.addPropertyChangeListener(listener);
	}

	@Override
	public int getExtent() {
		return primaryRangeModel.getExtent();
	}

	@Override
	public int getMaximum() {
		return primaryRangeModel.getMaximum();
	}

	@Override
	public int getMinimum() {
		return primaryRangeModel.getMinimum();
	}

	@Override
	public int getValue() {
		return primaryRangeModel.getValue();
	}

	@Override
	public boolean isEnabled() {
		return primaryRangeModel.isEnabled();
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		primaryRangeModel.removePropertyChangeListener(listener);
	}

	@Override
	public void setAll(int min, int extent, int max) {
		// this range model defers to the primary range model
	}

	@Override
	public void setExtent(int extent) {
		// this range model defers to the primary range model
	}

	@Override
	public void setMaximum(int max) {
		// this range model defers to the primary range model
	}

	@Override
	public void setMinimum(int min) {
		// this range model defers to the primary range model
	}

	@Override
	public void setValue(int value) {
		primaryRangeModel.setValue(value);
	}
	
	
}
