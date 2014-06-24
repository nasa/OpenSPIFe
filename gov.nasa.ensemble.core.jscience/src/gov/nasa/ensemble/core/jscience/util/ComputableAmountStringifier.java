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
package gov.nasa.ensemble.core.jscience.util;

import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.core.jscience.ComputableAmount;
import gov.nasa.ensemble.core.jscience.ComputingState;
import gov.nasa.ensemble.core.jscience.EnsembleAmountFormat;
import gov.nasa.ensemble.core.jscience.EnsembleUnitFormat;
import gov.nasa.ensemble.core.jscience.JScienceFactory;

import java.text.ParseException;

import javax.measure.unit.Unit;

import org.apache.log4j.Logger;
import org.jscience.physics.amount.Amount;

public class ComputableAmountStringifier extends AbstractTrimmingStringifier<ComputableAmount> implements IStringifier<ComputableAmount> {

	private static final Logger trace = Logger.getLogger(ComputableAmountStringifier.class);
	
	@Override
	public ComputableAmount getJavaObjectFromTrimmed(String string, ComputableAmount defaultObject) {
		if (string.equals("-")) {
			return null;
		} else if (string.equals("%")) {
			return JScienceFactory.eINSTANCE.createComputableAmount(null, ComputingState.COMPUTING);
		}
		Double value = null;
		Unit<?> unit = null;
		int index = string.indexOf(',');
		if (index != -1) {
			string = string.replaceAll(",", "");
		}
		index = string.indexOf(' ');
		if (index == -1) {
			value = Double.parseDouble(string);
			unit = Unit.ONE;
		} else {
			String valueString = string.substring(0, index);
			String unitString = string.substring(index+1);
			value = Double.parseDouble(valueString);
			try {
				unit = EnsembleUnitFormat.INSTANCE.parse(unitString);
			} catch (ParseException e) {
				trace.warn("parse exception on units "+value);
				return null;
			}
		}
		return JScienceFactory.eINSTANCE.createComputableAmount(Amount.valueOf(value, unit), ComputingState.COMPLETE);
	}

	@Override
	@SuppressWarnings("unchecked")
	public String getDisplayString(ComputableAmount formulaAmount) {
		if (formulaAmount == null || formulaAmount.getAmount() == null) {
			return "-";
		}
		ComputingState state = formulaAmount.getComputing();
		Amount amount = formulaAmount.getAmount();
		switch(state) {
		case COMPLETE:
			return EnsembleAmountFormat.INSTANCE.formatAmount(amount);
		case COMPUTING:
			return "%";
		}
		return null;
	}

}
