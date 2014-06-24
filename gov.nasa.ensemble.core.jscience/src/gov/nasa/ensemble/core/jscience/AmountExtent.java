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
package gov.nasa.ensemble.core.jscience;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

import org.jscience.physics.amount.Amount;

public class AmountExtent<T extends Quantity> {
	
	private final Amount<T> min;
	private final Amount<T> max;
	private Amount<T> delta;
	
	public AmountExtent(Amount<T> min, Amount<T> max) {
		this.min = min;
		this.max = max;
	}
	
	public Amount<T> getMax() {
		return max;
	}
	
	public Amount<T> getMin() {
		return min;
	}
	
	public Amount<T> getDelta() {
		if (delta == null) {
			delta = max.minus(min);
		}
		return delta;
	}

	public AmountExtent<T> to(Unit<T> nominalUnits) {
		Amount<T> min = getMin();
		if (min != null) {
			min = min.to(nominalUnits);
		}
		Amount<T> max = getMax();
		if (max != null) {
			max = max.to(nominalUnits);
		}
		return new AmountExtent(min, max);
	}
	
	public AmountExtent<T> union(AmountExtent<T> extent) {
		if (extent == null) {
			return this;
		}
		Amount<T> min = getMin();
		Amount<T> max = getMax();
		if (max == null || (extent.getMax() != null
								&& max.getUnit().isCompatible(extent.getMax().getUnit())
								&& max.isLessThan(extent.getMax()))) {
			max = extent.getMax();
		}
		if (min == null || (extent.getMin() != null 
								&& min.getUnit().isCompatible(extent.getMin().getUnit())
								&& min.isGreaterThan(extent.getMin()))) {
			min = extent.getMin();
		}
		return new AmountExtent<T>(min, max);
	}
	
	public AmountExtent<T> intersection(AmountExtent<T> extent) {
		Amount<T> min = getMin();
		Amount<T> max = getMax();
		if (max == null || (extent.getMax() != null && max.isGreaterThan(extent.getMax()))) max = extent.getMax();
		if (min == null || (extent.getMin() != null && min.isLessThan(extent.getMin()))) min = extent.getMin();
		return new AmountExtent<T>(min, max);
	}
	
	public boolean contains(AmountExtent<T> that) {
		return contains(that.getMin()) && contains(that.getMax());
	}
	
	public boolean contains(Amount<T> amount) {
		return (getMin().approximates(amount) || getMin().isLessThan(amount))
				&& (getMax().approximates(amount) || getMax().isGreaterThan(amount));
	}
	
	public boolean intersects(AmountExtent<T> that) {
		return contains(that.getMin()) || contains(that.getMax());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + ((min == null) ? 0 : min.hashCode());
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
		AmountExtent other = (AmountExtent) obj;
		if (max == null) {
			if (other.max != null)
				return false;
		}		
		else if(other.max == null) {
			if(max != null) {
				return false;
			}
		}
		else if (max != null && other.max != null) {
			if (max.getUnit() == null)
				if (other.max.getUnit() != null)
					return false;
			else if(other.max.getUnit() == null)
				if(max.getUnit() != null)
					return false;
			else if (!max.getUnit().isCompatible(other.max.getUnit()))
				return false;
		}
		else if (!max.approximates(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if(other.min == null) {
			if(min != null) {
				return false;
			}
		} else if (min != null && other.min != null) {
			if (min.getUnit() == null)
				if (other.min.getUnit() != null)
					return false;
			else if(other.min.getUnit() == null)
				if(min.getUnit() != null)
					return false;
			else if (!min.getUnit().isCompatible(other.min.getUnit()))
				return false;
		} else if (!min.approximates(other.min))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AmountExtent [min=" + min + ", max=" + max + "]";
	}
	
}
