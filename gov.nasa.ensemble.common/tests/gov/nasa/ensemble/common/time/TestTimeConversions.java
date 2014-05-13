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
package gov.nasa.ensemble.common.time;

import static fj.data.Option.*;
import static gov.nasa.ensemble.common.time.TimeConversions.*;
import static org.junit.Assert.*;

import org.junit.Test;

import fj.P;
import fj.P2;
import fj.data.Option;

public class TestTimeConversions {

	private static final double ERROR = .0001;

	private TimeConversions converter = TimeConversions.instance();
	
	@Test
	public void simpleConversions() {
		for (String fromFormat : converter.getSupportedFormats()) {
			for (String input : getTestValue(fromFormat)) {
				for (String toFormat : converter.getSupportedFormats()) {
					System.err.println("Testing conversion from " + fromFormat + " to " + toFormat);
					final String intermediate = converter.convert(input, fromFormat, toFormat);
					final String output = converter.convert(intermediate, toFormat, fromFormat);
					System.err.println(input + " --> " + intermediate + " --> " + output);
					try {
						// try treating it as a double first...
						final double inputDouble = Double.parseDouble(input);
						final double outputDouble = Double.parseDouble(output);
						assertEquals(inputDouble, outputDouble, ERROR);
					} catch (NumberFormatException nfe) {
						// not a double
						final P2<String, Double> splitInput = stripFraction(input), splitOutput = stripFraction(output);
						assertEquals(splitInput._1(), splitOutput._1());
						assertEquals(splitInput._2(), splitOutput._2(), ERROR);
					}
				}
			}
		}
	}
	
	private P2<String, Double> stripFraction(final String input) {
		final int index = input.indexOf('.');
		if (index < 0)
			return P.p(input, 0.0);
		return P.p(input.substring(0, index), Double.parseDouble(input.substring(index)));
	}

	private Option<String> getTestValue(final String format) {
		if (SCLK_SCLKD.equals(format))
			return some("436236010.184");
		if (UTC_SCET.equals(format))
			return some("2012-301T12:39:04.129");
		if (LST_LMST.equals(format))
			return some("12M16:22:08.602");
		if (ET_SECONDS.equals(format))
			return some("406714559.168670");
		return none();
	}
}
