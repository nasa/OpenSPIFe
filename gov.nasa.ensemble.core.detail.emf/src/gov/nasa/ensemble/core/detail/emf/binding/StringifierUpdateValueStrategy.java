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

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.stringifier.DateStringifier;
import gov.nasa.ensemble.common.ui.date.defaulting.DefaultDateUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.ParseException;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;

@SuppressWarnings("unchecked")
public class StringifierUpdateValueStrategy extends EMFUpdateValueStrategy {

	private Binding binding = null;
	private Object lastSetValue = null;
	private EObject target = null;

	public StringifierUpdateValueStrategy(int updatePolicy) {
		super(updatePolicy);
	}


	public StringifierUpdateValueStrategy(int updatePolicy, EObject target) {
		super(updatePolicy);
		this.target  = target;
	}

	@Override
	protected IStatus doSet(IObservableValue observableValue, Object value) {
		lastSetValue = value;
		return super.doSet(observableValue, value);
	}

	@Override
	protected void fillDefaults(IObservableValue source, IObservableValue destination) {
		lastSetValue = destination.getValue();
		super.fillDefaults(source, destination);
	}

	@Override
	protected IValidator createValidator(Object fromType, Object toType) {
		if (fromType instanceof EAttribute && toType == String.class) {
			IStringifier stringifier = getStringifier((EAttribute) fromType);
			if (stringifier != null) {
				return new ObjectToStringValidator(stringifier);
			}
		} else if (fromType instanceof EDataType && toType == String.class) {
			IStringifier stringifier = getStringifier((EDataType) fromType);
			if (stringifier != null) {
				return new ObjectToStringValidator(stringifier);
			}
		} else if (fromType == String.class && toType instanceof EAttribute) {
			IStringifier stringifier = getStringifier((EAttribute) toType);
			if (stringifier != null) {
				return new StringToObjectValidator(stringifier);
			}
		} else if (fromType == String.class && toType instanceof EDataType) {
			IStringifier stringifier = getStringifier((EDataType) toType);
			if (stringifier != null) {
				return new StringToObjectValidator(stringifier);
			}
		}
		return super.createValidator(fromType, toType);
	}

	/**
	 * Override this method to use the stringifier registry when asked to create a converter with a toType of String.class
	 */
	@Override
	protected IConverter createConverter(final Object fromType, final Object toType) {
		if (fromType instanceof EAttribute && toType == String.class) {
			IStringifier stringifier = getStringifier((EAttribute) fromType);
			if (stringifier != null) {
				return new ObjectToStringConverter(stringifier, String.class);
			}
		} else if (fromType instanceof EDataType && toType == String.class) {
			IStringifier stringifier = getStringifier((EDataType) fromType);
			if (stringifier != null) {
				return new ObjectToStringConverter(stringifier, String.class);
			}
		} else if (fromType == String.class && toType instanceof EAttribute) {
			IStringifier stringifier = getStringifier((EAttribute) toType);
			if (stringifier != null) {
				return new StringToObjectConverter(stringifier, toType);
			}
		} else if (fromType == String.class && toType instanceof EDataType) {
			IStringifier stringifier = getStringifier((EDataType) toType);
			if (stringifier != null) {
				return new StringToObjectConverter(stringifier, toType);
			}
		}
		return super.createConverter(fromType, toType);
	}
	
	protected IStringifier getStringifier(EAttribute eAttribute) {
		return EMFUtils.getStringifier(eAttribute);
	}

	/**
	 * @Deprecated Use getStringifier(eAttribute) instead of  getStringifier(eAttribute.getEAttributeType())
	 */
	protected IStringifier getStringifier(EDataType eDataType) {
		return EMFUtils.getStringifier(eDataType);
	}

	private abstract class StringifierConverter extends Converter {

		protected final IStringifier stringifier;

		public StringifierConverter(IStringifier stringifier, Object fromType, Object toType) {
			super(fromType, toType);
			this.stringifier = stringifier;
		}

	}

	private class ObjectToStringConverter extends StringifierConverter {

		public ObjectToStringConverter(IStringifier stringifier, Object fromType) {
			super(stringifier, fromType, String.class);
		}

		@Override
		public Object convert(Object fromObject) {
			return stringifier.getDisplayString(fromObject);
		}

	}

	private class StringToObjectConverter extends StringifierConverter {

		public StringToObjectConverter(IStringifier stringifier, Object toType) {
			super(stringifier, String.class, toType);
		}

		@Override
		public Object convert(Object fromObject) {
			try {
				return stringifier.getJavaObject((String) fromObject, getDefault(stringifier));
			} catch (ParseException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
		}

	}
	
	private Object getDefault(IStringifier stringifier) {
		if (lastSetValue != null || !(stringifier instanceof DateStringifier)) return lastSetValue;
		else return DefaultDateUtil.tryHarderToFindDefaultDateIfApplicable(target, stringifier);
	}

	private abstract class StringifierValidator implements IValidator {

		protected final IStringifier stringifier;

		public StringifierValidator(IStringifier stringifier) {
			super();
			this.stringifier = stringifier;
		}

	}

	private class StringToObjectValidator extends StringifierValidator {

		public StringToObjectValidator(IStringifier stringifier) {
			super(stringifier);
		}

		@Override
		public IStatus validate(Object value) {
			try {
				stringifier.getJavaObject((String) value, getDefault(stringifier));
			} catch (ParseException e) {
				if (binding != null)
					binding.updateModelToTarget();
				return ValidationStatus.error(e.getMessage());
			}
			return ValidationStatus.ok();
		}

	}

	private class ObjectToStringValidator extends StringifierValidator {

		public ObjectToStringValidator(IStringifier stringifier) {
			super(stringifier);
		}

		@Override
		public IStatus validate(Object value) {
			stringifier.getDisplayString(value);
			return ValidationStatus.ok();
		}

	}

//	@Override
//	protected IStatus doSet(IObservableValue observableValue, Object value) {
//		IStatus status = super.doSet(observableValue, value);
//		if (binding != null)
//			binding.updateModelToTarget();
//		return status;
//	}

	public void setBinding(Binding binding) {
		this.binding = binding;
	}
	
}
