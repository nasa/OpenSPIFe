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
package gov.nasa.ensemble.common.ui.slider;

import gov.nasa.ensemble.common.event.EventListenerList;

import java.util.EventListener;

/**
 * A model for a selectable range of sols.
 */
public class SolRangeModel {

	private int minValue;

	private int maxValue;

	private int pickedMinValue;

	private int pickedMaxValue;

	private EventListenerList eventListenerList;

	/**
	 * Construct a sol range model.
	 * 
	 * @param minValue
	 *            min allowable value
	 * @param maxValue
	 *            max allowable value
	 * @param initialPickedMinValue
	 *            min selected value
	 * @param initialPickedMaxValue
	 *            max selected value
	 */
	public SolRangeModel(int minValue, int maxValue, int initialPickedMinValue,
			int initialPickedMaxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.pickedMinValue = initialPickedMinValue;
		this.pickedMaxValue = initialPickedMaxValue;
	}

	/**
	 * Get the max allowable value
	 * 
	 * @return the max allowable value
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * Set the max allowable value
	 * 
	 * @param maxValue
	 *            the new max allowable value
	 */
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		fireRangeChangeEvent();
	}

	/**
	 * Get the min allowable value.
	 * 
	 * @return the min allowable value.
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * Set the min allowable value.
	 * 
	 * @param minValue
	 *            the new min allowable value.
	 */
	public void setMinValue(int minValue) {
		this.minValue = minValue;
		fireRangeChangeEvent();
	}

	/**
	 * Get the max selected value.
	 * 
	 * @return the max selected value.
	 */
	public int getPickedMaxValue() {
		return pickedMaxValue;
	}

	/**
	 * Set the max selected value.
	 * 
	 * @param pickedMaxValue
	 *            the new max selected value.
	 */
	public void setPickedMaxValue(int pickedMaxValue) {
		this.pickedMaxValue = pickedMaxValue;
		fireRangeChangeEvent();
	}

	/**
	 * Get the min selected value.
	 * 
	 * @return the min selected value.
	 */
	public int getPickedMinValue() {
		return pickedMinValue;
	}

	/**
	 * Set the min selected value.
	 * 
	 * @param pickedMinValue
	 *            the min selected value.
	 */
	public void setPickedMinValue(int pickedMinValue) {
		this.pickedMinValue = pickedMinValue;
		fireRangeChangeEvent();
	}

	/**
	 * Add a listener to be notified upon sol range changes.
	 * 
	 * @param cl
	 *            listener to add
	 */
	public void addChangeListener(RangeChangeListener cl) {
		if (eventListenerList == null)
			eventListenerList = new EventListenerList();
		eventListenerList.add(cl);
	}

	/**
	 * Remove a listener.
	 * 
	 * @param cl
	 *            listener to remove.
	 */
	public void removeChangeListener(RangeChangeListener cl) {
		if (eventListenerList == null)
			return;
		eventListenerList.remove(cl);
	}

	/**
	 * Notify our listeners about a model change.
	 */
	private void fireRangeChangeEvent() {
		if (eventListenerList == null)
			return;
		for (EventListener listener : eventListenerList.getListenerList()) {
			RangeChangeListener solListener = (RangeChangeListener) listener;
			solListener.rangeChanged(minValue, maxValue, pickedMinValue,
					pickedMaxValue);
		}
	}

}
