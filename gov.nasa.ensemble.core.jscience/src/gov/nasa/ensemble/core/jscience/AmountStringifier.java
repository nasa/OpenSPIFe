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
package gov.nasa.ensemble.core.jscience;

import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;

import java.text.ParseException;

import javax.measure.unit.Unit;

import org.jscience.physics.amount.Amount;

public class AmountStringifier extends AbstractTrimmingStringifier<Amount> {

	@Override
	public Amount getJavaObjectFromTrimmed(String string, Amount defaultObject) throws ParseException {
		try {
			Unit defaultUnit = Unit.ONE;
			if (defaultObject != null) {
				defaultUnit = defaultObject.getUnit();
			}
			return EnsembleAmountFormat.INSTANCE.parseAmount(string, defaultUnit);
		} catch (Exception e) {
			throw new ParseException(e.getMessage(), 0);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getDisplayString(Amount amount) {
		if (amount == null) {
			return "";
		}
		return EnsembleAmountFormat.INSTANCE.formatAmount(amount);
	}

}
