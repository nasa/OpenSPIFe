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

import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.jscience.mathematics.function.Linear;
import gov.nasa.ensemble.core.jscience.mathematics.function.Step;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.measure.quantity.Length;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.junit.Assert;

import org.eclipse.emf.ecore.EDataType;
import org.jscience.mathematics.function.DiscreteFunction;
import org.jscience.mathematics.function.Interpolator;
import org.jscience.mathematics.function.Variable.Local;
import org.jscience.physics.amount.Amount;
import org.junit.Test;


public class TestJScience extends Assert {
	
	private static final EDataType TYPE_EDURATION = JSciencePackage.Literals.EDURATION;
	private static final JSciencePackage JSCIENCE_PACKAGE = JSciencePackage.eINSTANCE;
	private static final JScienceFactory JSCIENCE_FACTORY = JScienceFactory.eINSTANCE;
	
	/*
	 * Test for SPF-3272
	 */
	@Test
	public void testEmptyComputableAmountSerialization() {
		assertRoundTripEquality(JSCIENCE_FACTORY.createComputableAmount());
		assertRoundTripEquality(JSCIENCE_FACTORY.createComputableAmount(Amount.ZERO, ComputingState.COMPUTING));
		assertRoundTripEquality(JSCIENCE_FACTORY.createComputableAmount(Amount.valueOf(0, SI.SECOND), ComputingState.COMPUTING));
		assertRoundTripEquality(JSCIENCE_FACTORY.createComputableAmount(Amount.valueOf(60, SI.SECOND), ComputingState.COMPUTING));
	}
	
	@Test
	public void testLargeNumberStringification() {
		ComputableAmount cAmount = JScienceFactory.eINSTANCE.createComputableAmount(Amount.valueOf(10000, Unit.ONE), ComputingState.COMPLETE);
		IStringifier<ComputableAmount> s = StringifierRegistry.getStringifier(ComputableAmount.class);
		assertNotNull(s);
		String displayString = s.getDisplayString(cAmount);
		assertFalse("bad format '"+displayString+"'", displayString.endsWith("*1000"));
	}
	
	@Test
	public void testLinearInterpolator() {
		final int numSamples = 3;
		DiscreteFunction<Date, Amount<Length>> discrete = createTestFunction(new Linear<Length>(), numSamples);
		for (int i=0; i<numSamples; i++) {
			Date date = new Date(1000*(i+1));
			assertEquals(Amount.valueOf(5*i, SI.METER), discrete.evaluate(date));
			date = new Date(date.getTime() + 500);
			if (i < numSamples-1)
				assertTrue(Amount.valueOf(5*i + 2.5, SI.METER).approximates(discrete.evaluate(date)));
		}
	}
	
	@Test
	public void testStepInterpolator() {
		final int numSamples = 3;
		DiscreteFunction<Date, Amount<Length>> discrete = createTestFunction(new Step<Length>(), numSamples);
		for (int i=0; i<numSamples; i++) {
			Date date = new Date(1000*(i+1));
			assertEquals(Amount.valueOf(5*i, SI.METER), discrete.evaluate(date));
			date = new Date(date.getTime() + 500);
			if (i < numSamples-1)
				assertTrue(Amount.valueOf(5*i, SI.METER).approximates(discrete.evaluate(date)));
		}
	}
	
	public void testEDurationParser() {
		assertEquals(Amount.valueOf(600, SI.SECOND), JScienceFactory.eINSTANCE.createFromString(TYPE_EDURATION, "600 s"));
		assertEquals(Amount.valueOf(-600, SI.SECOND), JScienceFactory.eINSTANCE.createFromString(TYPE_EDURATION, "-600 s"));
		assertEquals(Amount.valueOf(600, SI.SECOND), JScienceFactory.eINSTANCE.createFromString(TYPE_EDURATION, "00:10:00"));
		assertEquals(Amount.valueOf(-600, SI.SECOND), JScienceFactory.eINSTANCE.createFromString(TYPE_EDURATION, "-00:10:00"));
		assertEquals(Amount.valueOf(600, SI.SECOND), JScienceFactory.eINSTANCE.createFromString(TYPE_EDURATION, "-P10M"));
		assertEquals(Amount.valueOf(-600, SI.SECOND), JScienceFactory.eINSTANCE.createFromString(TYPE_EDURATION, "P10M"));
	}

	private DiscreteFunction<Date, Amount<Length>> createTestFunction(Interpolator<Date, Amount<Length>> interpolator, final int numSamples) {
		SortedMap<Date, Amount<Length>> values = new TreeMap<Date, Amount<Length>>();
		for (int i=0; i<numSamples; i++) {
			values.put(new Date(1000*(i+1)), Amount.valueOf(5*i, SI.METER));
		}
		DiscreteFunction<Date, Amount<Length>> discrete = new DiscreteFunction<Date, Amount<Length>>(values, interpolator, new Local<Date>("time"));
		return discrete;
	}
	
	private static final double ERROR = .0001;
	
	@SuppressWarnings("unchecked")
	private void assertRoundTripEquality(ComputableAmount in) {
		EDataType classifier = JSCIENCE_PACKAGE.getEComputableAmount();
		String inString = JSCIENCE_FACTORY.convertToString(classifier, in);
		ComputableAmount out = (ComputableAmount) JSCIENCE_FACTORY.createFromString(classifier, inString);
		if (in.getAmount() != null) {
			Unit inUnit = in.getAmount().getUnit();
			assertEquals(inUnit, out.getAmount().getUnit());
			assertEquals(in.getAmount().doubleValue(inUnit), out.getAmount().doubleValue(inUnit), ERROR);
		}
		assertEquals(in.getComputing(), out.getComputing());
		String outString = JSCIENCE_FACTORY.convertToString(classifier, out);
		assertNotNull(outString);
	}
	
}
