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
 * 
 */
package gov.nasa.ensemble.core.detail.emf.binding;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EAttribute;

final class PathUpdateValueStrategy extends EMFUpdateValueStrategy {

	PathUpdateValueStrategy(int updatePolicy) {
		super(updatePolicy);
	}

	@Override
	protected IConverter createConverter(Object fromType, Object toType) {
		if (fromType instanceof EAttribute && toType == String.class) {
			return new RelativePathToStringConverter();
		} else if (fromType == String.class && toType instanceof EAttribute) {
			return new StringToRelativePathConverter();
		}
		return super.createConverter(fromType, toType);
	}
	
	private class RelativePathToStringConverter extends Converter {

		public RelativePathToStringConverter() {
			super(IPath.class, String.class);
		}
		
		@Override
		public Object convert(Object fromObject) {
			if (fromObject == null) {
				return null;
			}
			IPath path = (IPath)fromObject;
			return path.toString();
		}
		
	}

	private class StringToRelativePathConverter extends Converter {
		
		public StringToRelativePathConverter() {
			super(String.class, IPath.class);
		}
		
		@Override
		public Object convert(Object fromObject) {
			if (fromObject == null) {
				return null;
			}
			String string = (String)fromObject;
			IPath path = new Path(string);
			return path;
		}
		
	}

}
