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
package gov.nasa.ensemble.core.plan;

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.common.ui.preferences.UIPreferenceUtils;
import gov.nasa.ensemble.common.ui.type.editor.OutOfBoundsException;
import gov.nasa.ensemble.common.ui.type.editor.SoftOutOfBoundsException;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionaryPlugin;
import gov.nasa.ensemble.core.activityDictionary.ParameterDef;
import gov.nasa.ensemble.core.plan.parameters.IParameterSerializer;
import gov.nasa.ensemble.dictionary.EChoice;

import java.text.ParseException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Specalized version of a stringifier which handles
 * Choices.
 * 
 * This includes both simple choices, as well as a multi value choices
 * 
 * Uses a string as input and output, since its desgined to handle multiitem values
 * which are implemented as comma seperated values.
 */
public class ChoicesStringifier<T> implements IStringifier<T>
{
	private ParameterDef parameterDef;
	private IParameterSerializer<T> serializer;
	private IStringifier<T> stringifier;

	@SuppressWarnings("unchecked")
	public ChoicesStringifier(ParameterDef parameterDef) {
		this.parameterDef = parameterDef;
		this.serializer = ParameterSerializerRegistry.getSerializer(parameterDef);
		this.stringifier = StringifierRegistry.getStringifier(parameterDef.getType());
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
			throw new ParseException(getChoiceFormatExceptionMessage(), -1);
		}
		// Serializer for the objects in this list
		T javaObject = stringifier.getJavaObject(userString, defaultObject);
		for (EChoice choice : parameterDef.getChoices()) {
			// Does it match an exact value?
			T valueObject = (choice.getValue() == null) ? null : serializer.getJavaObject(choice.getValue());
			if (javaObject.equals(valueObject)) {
				return javaObject; // Matched
			} // else...
			
			if (checkMultiple(javaObject, choice)) {
				return javaObject;
			}

			// If the object is comparable, do a comparison to see if within min/max inclusive
			if (javaObject instanceof Comparable) {
				if (checkMinMax(javaObject, choice)) {
					return javaObject;
				}
			}
		}
		
		// if we are here, no choices have been satisfied
		String exceptionMessage = getChoiceFormatExceptionMessage();

		// Need to use the AD PreferenceStore, since it has the "setting"
		IPreferenceStore store = UIPreferenceUtils.getPreferenceStore(ActivityDictionaryPlugin.getDefault());
		if (store.getBoolean(ActivityDictionaryPlugin.ACTIVITY_DICTIONARY_STRICT_CHECKING_PROPERTY)) {
			throw new OutOfBoundsException(exceptionMessage, -1, javaObject);
		}
		throw new SoftOutOfBoundsException(exceptionMessage, -1, javaObject);
	}

	/**
	 * Min and Max may each be null, meaning "unrestricted".
	 * (If both are, always return true).
	 * Each non-null value has a chance to reject the value by returning false.
	 * Ranges are inclusive. 
	 */
	private boolean checkMinMax(T javaObject, EChoice choice) {
		if (choice.getMinimum() == null && choice.getMaximum() == null) {
			return false;
		}
		
		T minObject = (choice.getMinimum() == null) ? null : serializer.getJavaObject(choice.getMinimum());
		T maxObject = (choice.getMaximum() == null) ? null : serializer.getJavaObject(choice.getMaximum());
		@SuppressWarnings("unchecked")
		Comparable<T> comparable = ((Comparable<T>)javaObject);
		if ((minObject != null) && comparable.compareTo(minObject) < 0) {
			return false;
		} else if ((maxObject != null) && comparable.compareTo(maxObject) > 0) {
			return false;
		} // else...
		return true;
	}

	private boolean checkMultiple(T javaObject, EChoice choice) {
		if (choice.getMultipleOf() == null) {
			return false;
		}
		
		T multipleObject = serializer.getJavaObject(choice.getMultipleOf());
		if (multipleObject != null) {
			if (multipleObject instanceof Number) {
				Number multiple = (Number) multipleObject;
				Number c = (Number) javaObject;
				if (c.doubleValue() % multiple.doubleValue() != 0)  {
					// Not a multiple, stop processing.
					throw new IllegalArgumentException(getChoiceFormatExceptionMessage());
				}
				return true;
			}
			Logger.getLogger(ChoicesStringifier.class).error("MultipleOf handler does not recognize type '"+multipleObject.getClass()+"'");
		}
		return false;
	}
	
	protected String getChoiceFormatExceptionMessage() {
		StringBuilder errorMsg = new StringBuilder();
		ArrayList<String> choiceList = new ArrayList<String>();
		errorMsg.append("Value must conform to the following:\n");
		for (EChoice choice : parameterDef.getChoices()) {
			String value = choice.getValue();
			if (value != null) {
				// errorMsg.append("\tBe equal to " + value);
				choiceList.add(value);
			} else {
				errorMsg.append("\n");
				if (choice.getMinimum() == null && choice.getMaximum() != null) {
					Object max = unserializeAndDisplay(choice.getMaximum());
					errorMsg.append("     Be less then or Equal to " + max);
				} else if (choice.getMinimum() != null && choice.getMaximum() == null) {
					Object min = unserializeAndDisplay(choice.getMinimum());
					errorMsg.append("     Be greater or Equal to " + min);
				} else if (choice.getMinimum() != null && choice.getMaximum() != null) {
					Object min = unserializeAndDisplay(choice.getMinimum());
					Object max = unserializeAndDisplay(choice.getMaximum());
					errorMsg.append("     Be between " + min +" and " + max + " inclusive");
				}
				if (choice.getMultipleOf() != null) {
					Object multiple = unserializeAndDisplay(choice.getMultipleOf());
					errorMsg.append("     In multiples of " + multiple);
				}
			}
		}
		if (choiceList.size() > 0) {
			errorMsg.append("\n     Be equal to one of:");
			for (String choice : choiceList) {
				errorMsg.append("\n     " + choice);
			}
		}
		return errorMsg.toString();
	}

	private String unserializeAndDisplay(String hibernateString) {
		T object = serializer.getJavaObject(hibernateString);
		return stringifier.getDisplayString(object);
	}

}
