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

import java.util.EventListener;

/**
 * A RangeChangeListener receives notification callbacks with the range model changes.
 */
public interface RangeChangeListener extends EventListener {

	/**
	 * Callback invoked when a range model changes.
	 * 
	 * @param minValue the minimum allowable value of the range
	 * @param maxValue the maximum allowable value of the range
	 * @param pickedMinValue the minimum currently selected value of the range
	 * @param pickedMaxValue the maximum currently selected value of the range
	 */
	public void rangeChanged(int minValue, int maxValue, int pickedMinValue, int pickedMaxValue);
	
}
