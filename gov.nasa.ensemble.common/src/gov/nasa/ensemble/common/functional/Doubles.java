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
package gov.nasa.ensemble.common.functional;

import static fj.data.Option.*;
import fj.F;
import fj.data.Option;
import fj.pre.Show;

public class Doubles {
	public static final F<String, Option<Double>> parseDouble = new F<String, Option<Double>>() {
		@Override
		public Option<Double> f(final String string) {
			try {
				return some(Double.parseDouble(string));
			} catch (NumberFormatException e) {
				return none();
			}
		}
	};
	
	public static final Option<Double> parseDouble(final String s) {
		return parseDouble.f(s);
	}
	
	public static final F<Double, String> doubleToString = Show.doubleShow.showS_();
}
