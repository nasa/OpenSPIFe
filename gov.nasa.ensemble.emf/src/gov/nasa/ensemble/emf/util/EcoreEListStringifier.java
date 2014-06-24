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

import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;

import java.text.ParseException;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;

public class EcoreEListStringifier extends AbstractTrimmingStringifier<Object> {

	private final EDataType eDataType;
	private final EFactory eFactoryInstance;

	public EcoreEListStringifier(EDataType eDataType) {
		this.eDataType = eDataType;
		this.eFactoryInstance = eDataType.getEPackage().getEFactoryInstance();
	}
	
	@Override
	public String getDisplayString(Object javaObject) {
		return formatString(eFactoryInstance.convertToString(eDataType, javaObject));
	}

	@Override
	protected Object getJavaObjectFromTrimmed(String string, Object defaultObject) throws ParseException {
		try {
			return eFactoryInstance.createFromString(eDataType, string);
		} catch (Exception e) {
			if (e instanceof ParseException) {
				throw (ParseException) e;
			}
			throw new ParseException(e.getMessage(), 0);
		}
	}

	public static String formatString(String text) {
		if(text != null) {
			if(text.startsWith("[") && text.endsWith("]")) {
				text = text.substring(1, text.length()-1);
			}
		}
		return text;
	}

}
