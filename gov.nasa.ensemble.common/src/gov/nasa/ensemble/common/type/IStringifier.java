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
package gov.nasa.ensemble.common.type;

import java.text.ParseException;


public interface IStringifier<T> {

	/**
	 * Returns a string for displaying the given java object.
	 * 
	 * Implementors must not return null.
	 * Callers can expect the returned value will not be null.
	 * 
	 * @param javaObject
	 * @return a string for displaying the given java object.
	 */
	public String getDisplayString(T javaObject);
	
	/**
	 * Returns the indicated java object, given a string from the user.
	 * If the user string is not valid throws a ParseException
	 * The default object is used to help provide context for partial user input
	 *  
	 * @param userString
	 * @param defaultObject 
	 * @return the indicated java object, given a string from the user.
	 */
	public T getJavaObject(String userString, T defaultObject) throws ParseException;
	
}
