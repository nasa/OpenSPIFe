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
package gov.nasa.ensemble.common.event;

import java.util.EventObject;

import org.apache.commons.lang.builder.ToStringBuilder;


@SuppressWarnings("serial")
public class ModelChangeEvent extends EventObject {

	public enum TYPE {ADD, REMOVE, SET}
	
	private Enum<?> field;
	private Object oldValue;
	private Object newValue;
	private long time;
	
	/**
	 * @param source
	 * @param field
	 * @param oldValue
	 * @param newValue
	 */
	public ModelChangeEvent(Object source, Enum<?> field, Object oldValue, Object newValue) {
		super(source);
		this.field = field;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.time = System.nanoTime();
	}

	// For add
	//source = parent
	//propertyName = activity (list belongs to)
	//oldValue = null   
	//newValue = child
	
	// For remove
	//source = parent
	//propertyName = activity (list belongs to)
	//oldValue = child  
	//newValue = null

	/**
	 * @return Returns the field.
	 */
	public Enum<?> getField() {
		return field;
	}
	
	/**
	 * @return Returns the newValue.
	 */
	public Object getNewValue() {
		return newValue;
	}
	
	/**
	 * @return Returns the oldValue.
	 */
	public Object getOldValue() {
		return oldValue;
	}
	
	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("source",   source)
			.append("field",    field)
			.append("oldValue", oldValue)
			.append("newValue", newValue)
			.toString();
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + source.hashCode();
		result = PRIME * result + ((field == null) ? 0 : field.hashCode());
		result = PRIME * result + ((newValue == null) ? 0 : newValue.hashCode());
		result = PRIME * result + ((oldValue == null) ? 0 : oldValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ModelChangeEvent other = (ModelChangeEvent) obj;
		if (!source.equals(other.source))
			return false;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (newValue == null) {
			if (other.newValue != null)
				return false;
		} else if (!newValue.equals(other.newValue))
			return false;
		if (oldValue == null) {
			if (other.oldValue != null)
				return false;
		} else if (!oldValue.equals(other.oldValue))
			return false;
		return true;
	}
}
