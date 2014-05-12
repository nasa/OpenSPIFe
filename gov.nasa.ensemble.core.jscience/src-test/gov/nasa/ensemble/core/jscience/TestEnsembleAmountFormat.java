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


import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.jscience.physics.amount.Amount;
import org.junit.Assert;
import org.junit.Test;

public class TestEnsembleAmountFormat extends Assert {

	@Test
	public void testDataAmountFormat() {
		assertAmountFormat(Amount.valueOf(1000, SI.BIT), "1000 bit");
		assertAmountFormat(Amount.valueOf(1024, SI.BIT), "1 Kbit");
		assertAmountFormat(Amount.valueOf(1024*1024, SI.BIT), "1 Mbit");
		assertAmountFormat(null, "");
	}

	@Test
	public void testTemperatureFormat() {
		assertAmountFormat(Amount.valueOf(2.2, SI.CELSIUS), "2.2 C");
		assertAmountParsing("2 C", Amount.valueOf(2, SI.CELSIUS));
	}

	private void assertAmountParsing(String string, Amount<?> expectedAmount) {
		try {
			Amount amount = EnsembleAmountFormat.INSTANCE.parseAmount(string, Unit.ONE);
			// handle null as a blank string
			if(amount==null){
				assertTrue(string.trim().equalsIgnoreCase(""));
			}else{
				assertTrue(expectedAmount.approximates(amount));
			}
		} catch (Exception e) {
			fail("parsing '"+string+"': "+e.getMessage());
		}
	}

	private void assertAmountFormat(Amount<?> amount, String expectedFormatAmount) {
		String formatAmount = EnsembleAmountFormat.INSTANCE.formatAmount(amount);
		assertAmountParsing( expectedFormatAmount, amount);
		assertEquals(expectedFormatAmount, formatAmount);
	}
	
}
