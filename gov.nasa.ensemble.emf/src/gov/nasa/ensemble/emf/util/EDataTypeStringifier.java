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

import java.math.BigDecimal;
import java.text.ParseException;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;

public class EDataTypeStringifier extends AbstractTrimmingStringifier<Object> {

	private final EDataType eDataType;
	private final EFactory eFactoryInstance;

	public EDataTypeStringifier(EDataType eDataType) {
		this.eDataType = eDataType;
		this.eFactoryInstance = eDataType.getEPackage().getEFactoryInstance();
	}

	/**
	 * @throws ParseException
	 */
	@Override
	protected Object getJavaObjectFromTrimmed(String string, Object defaultObject) throws ParseException {
		try {
			if (eDataType.getInstanceClassName() != null 
					&& eDataType.getInstanceClassName().equalsIgnoreCase(BigDecimal.class.getName())) {
				double value = Double.parseDouble(string);
				BasicExtendedMetaData bemd = new BasicExtendedMetaData();
				int factionDigits = bemd.getFractionDigitsFacet(eDataType);
				String minInclusiveString = bemd.getMinInclusiveFacet(eDataType);
				if (minInclusiveString != null) {
					int minInclusive = Integer.parseInt(minInclusiveString);
					if (value < minInclusive) {
						value = 360 - Math.abs(value); // See SPF-9712
					}
				}
				return eFactoryInstance.createFromString(eDataType, String.format("%." + factionDigits + "f", value));
			}
			return eFactoryInstance.createFromString(eDataType, string);
		} catch (Exception e) {
			if (e instanceof ParseException) {
				throw (ParseException) e;
			}
			throw new ParseException(e.getMessage(), 0);
		}
	}

	@Override
	public String getDisplayString(Object javaObject) {
		return eFactoryInstance.convertToString(eDataType, javaObject);
	}

}
