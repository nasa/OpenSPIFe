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

import gov.nasa.ensemble.common.CommonUtils;

import org.eclipse.core.databinding.UpdateValueStrategy;

public abstract class SatisfactionUpdateValueStrategy extends UpdateValueStrategy {

	@Override
	public Object convert(Object value) {
		return satisfies(value);
	}

	protected abstract Boolean satisfies(Object value);

	public static class Equality extends SatisfactionUpdateValueStrategy {

		private final Object value;
		
		public Equality(Object value) {
			super();
			this.value = value;
		}

		@Override
		protected Boolean satisfies(Object value) {
			return CommonUtils.equals(value, this.value);
		}
		
	}
	
	public static class Inequality extends Equality {

		public Inequality(Object value) {
			super(value);
		}

		@Override
		protected Boolean satisfies(Object value) {
			return !super.satisfies(value);
		}

	}
	
	
}
