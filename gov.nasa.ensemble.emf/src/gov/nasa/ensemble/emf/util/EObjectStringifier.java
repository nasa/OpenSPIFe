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
package gov.nasa.ensemble.emf.util;

import gov.nasa.ensemble.common.type.IStringifier;

import java.text.ParseException;
import java.util.Collection;

import org.eclipse.emf.ecore.EObject;

/**
 * A one way IStringifier for either a single EObject or a collection of EObjects
 * Adapts the EObject(s) to an IItemLabelProvider and then uses the getText method to get a display string
 * The display strings for multiple EObjects are separated by a comma
 * 
 * @author rnado
 *
 */
public class EObjectStringifier implements IStringifier<Object> {

	@Override
	public String getDisplayString(Object javaObject) {
		if (javaObject instanceof EObject) {
			return EMFUtils.getDisplayName((EObject)javaObject);
		} else if (javaObject instanceof Collection) {
			StringBuffer buffer = new StringBuffer();
			boolean first = true;
			for (Object object : ((Collection)javaObject)) {
				if (object instanceof EObject) {
					if (!first) {
						buffer.append(", ");
					}
					buffer.append(EMFUtils.getDisplayName((EObject)object));
					first = false;
				}
			}
			return buffer.toString();
		}
		return "";
	}

	@Override
	public Object getJavaObject(String userString, Object defaultObject) throws ParseException {
		throw new ParseException("Parsing EObjects from a string not supported", 0);
	}

}
