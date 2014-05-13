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
package gov.nasa.ensemble.core.detail.emf.binding;

import java.util.List;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;

public class FiniteOrderedSetUpdateValueStrategy extends UpdateValueStrategy {

	private List<?> values;
	
	public FiniteOrderedSetUpdateValueStrategy(int updatePolicy, List<?> values) {
		super(true, updatePolicy);
		this.values = values;
	}
	
	public void setValues(List<?> values) {
		this.values = values;
	}
	
	@Override
	protected IConverter createConverter(Object fromType, Object toType) {
		if ((fromType == Integer.class) || (fromType == int.class)) {
			return new Converter(fromType, toType) {
				@Override
				public Object convert(Object fromObject) {
					Integer fromInteger = (Integer)fromObject;
					return values.get(fromInteger);
				}
			};
		}
		if ((toType == Integer.class) || (toType == int.class)) { 
			return new Converter(fromType, toType) {
				@Override
				public Object convert(Object fromObject) {
					return values.indexOf(fromObject);
				}
			};
		}
		return super.createConverter(fromType, toType);
	}
	
}
