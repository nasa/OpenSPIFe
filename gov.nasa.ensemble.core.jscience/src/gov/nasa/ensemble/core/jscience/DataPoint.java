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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gov.nasa.ensemble.core.jscience;

import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EEnumLiteral;
import org.jscience.physics.amount.Amount;

public abstract class DataPoint<T> {
	
	public static final Comparator<DataPoint> DEFAULT_COMPARATOR = new Comparator<DataPoint>() {
		@Override
		public int compare(DataPoint o1, DataPoint o2) {
			return o1.getDate().compareTo(o2.getDate());
		}
	};
	
	private List<Object> contributors = null;
	
	public List<Object> getContributors() {
		if (contributors == null) {
			contributors = new ArrayList<Object>();
		}
		return contributors;
	}

	public abstract Date getDate();
	
	public abstract T getValue();
	
	public abstract void setValue(T value);

	public abstract boolean isOutOfRange();
	
	public Number getNumericValue() {
		T value = getValue();
		if (value == null) {
			return null;
		}
		if (value instanceof Number) {
			return ((Number)value);
		}
		if (value instanceof Amount) {
			Amount amount = (Amount) value;
			return AmountUtils.getNumericValue(amount);
		}
//		if (value instanceof EEnumLiteral) {
//			return ((EEnumLiteral)value).getValue(); 
//		}
		throw new UnsupportedOperationException("can't get a numeric value for the type:" + value.getClass());
	}
	
	public boolean isZero() {
		T value = getValue();
		if (value == null) {
			return true;
		}
		if (value instanceof Boolean) {
			Boolean b = (Boolean) value;
			return (b == Boolean.FALSE);
		}
		if (value instanceof EEnumLiteral) {
			return ((EEnumLiteral) value).getValue() == 0;
		}
		if (value instanceof Enum) {
			return ((Enum) value).ordinal() == 0;
		}
		Number number = (value instanceof Number ? (Number)value : null);
		if (value instanceof Amount) {
			Amount amount = (Amount) value;
			number = AmountUtils.getNumericValue(amount);
		}
		if (number != null) {
			return number.doubleValue() == 0;
		}
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getDate() == null) ? 0 : getDate().hashCode());
		result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
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
		final DataPoint other = (DataPoint) obj;
		if (getDate() == null) {
			if (other.getDate() != null)
				return false;
		} else if (!getDate().equals(other.getDate()))
			return false;
		if (getValue() == null) {
			if (other.getValue() != null)
				return false;
		} else if (getValue() instanceof Number && other.getValue() instanceof Number) {
			return ((Number)getValue()).doubleValue() == ((Number)other.getValue()).doubleValue();
		} else if (getValue() instanceof Amount && other.getValue() instanceof Amount) {
			return ((Amount)getValue()).approximates((Amount)other.getValue());
		} else if (!getValue().equals(other.getValue()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + StringifierRegistry.getStringifier(Date.class).getDisplayString(getDate()) + ", " + getValue() + ")";
	}
} // EDataPoint
