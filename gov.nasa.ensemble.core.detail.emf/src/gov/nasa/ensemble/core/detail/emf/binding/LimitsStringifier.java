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
import gov.nasa.ensemble.common.ui.type.editor.OutOfBoundsException;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Specalized version of a stringifier which handles
 * Choices.
 * 
 * This includes both simple choices, as well as a multi value choices
 * 
 * Uses a string as input and output, since its desgined to handle multiitem values
 * which are implemented as comma seperated values.
 */
public class LimitsStringifier<T> implements IStringifier<T>
{
	private EAttribute attribute;
	private IStringifier<T> stringifier;
	private String min;
	private String max;
	private Comparable minObject;
	private Comparable maxObject;

	@SuppressWarnings("unchecked")
	public LimitsStringifier(EAttribute attribute, String min, String max) {
		this.attribute = attribute;
		this.stringifier = (IStringifier<T>) EMFUtils.getStringifier(attribute);
		this.min = min;
		this.max = max;
		minObject = (Comparable) createFromString(min);
		maxObject = (Comparable) createFromString(max);
	}
	
	// Simply show the specified object
	@Override
	public String getDisplayString(T javaObject) {
		return stringifier.getDisplayString(javaObject);
	}

	/**
	 * Validate the object as formed, and if a parse error ocures produce an error message
	 * 
	 * Will return null, if string is null
	 */
	@Override
	public T getJavaObject(String userString, T defaultObject) throws ParseException {
		if (userString == null || userString.trim().length() == 0) {
			return null;
		}
		// Serializer for the objects in this list
		T javaObject = stringifier.getJavaObject(userString, defaultObject);

		if (javaObject instanceof Comparable) {
			if (checkMinMax(javaObject)) {
				return javaObject;
			}
		}
		
		// if we are here, no choices have been satisfied
		String exceptionMessage = getChoiceFormatExceptionMessage();

		// Need to use the AD PreferenceStore, since it has the "setting"
		throw new OutOfBoundsException(exceptionMessage, -1, javaObject);
	}

	/**
	 * Min and Max may each be null, meaning "unrestricted".
	 * (If both are, always return true).
	 * Each non-null value has a chance to reject the value by returning false.
	 * Ranges are inclusive. 
	 */
	private boolean checkMinMax(T javaObject) {
		if (minObject == null && maxObject == null) {
			return true;
		}
		
		@SuppressWarnings("unchecked")
		Comparable<T> comparable = ((Comparable<T>)javaObject);
		if ((minObject != null) && comparable.compareTo((T) minObject) < 0) {
			return false;
		} else if ((maxObject != null) && comparable.compareTo((T) maxObject) > 0) {
			return false;
		} // else...
		return true;
	}

	protected String getChoiceFormatExceptionMessage() {
		StringBuilder errorMsg = new StringBuilder();
		errorMsg.append("Value must conform to the following:\n");
		if (minObject == null && maxObject != null) {
			errorMsg.append("     Be less than or equal to " + max);
		} else if (minObject != null && maxObject == null) {
			errorMsg.append("     Be greater than or equal to " + min);
		} else if (minObject != null && maxObject != null) {
			errorMsg.append("     Be between " + min +" and " + max + " inclusive");
		}
		return errorMsg.toString();
	}

	private Object createFromString(String value) {
		// null or empty string value may cause EMF createfromString to throw an exception
		if (value == null || value.length() == 0) {
			return null;
		}
		EDataType eDataType = attribute.getEAttributeType();
		try {
			Object minObject = EcoreUtil.createFromString(eDataType, value);
			return minObject;
		} catch (Exception e) {
			Logger.getLogger(LimitsStringifier.class).warn("Error parsing attribute value limit", e);
			return null;
		}
	}
	
}
