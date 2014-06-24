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
package gov.nasa.ensemble.core.jscience.impl;

import java.util.Date;

import gov.nasa.ensemble.core.jscience.DataPoint;

public class DataPointImpl<T> extends DataPoint<T> {
	private Date date;
	private T value;
	private boolean outOfRange = false;

	public DataPointImpl(Date date, T value) {
		this.date = date;
		this.value = value;
	}
	@Override
	public Date getDate() {
		return date;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public boolean isOutOfRange() {
		return outOfRange;
	}
	
	public void setOutOfRange(boolean outOfRange) {
		this.outOfRange = outOfRange;
	}
}
