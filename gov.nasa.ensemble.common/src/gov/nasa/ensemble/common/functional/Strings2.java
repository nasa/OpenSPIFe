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

import static fj.Function.*;
import static fj.data.Option.*;
import static fj.data.vector.V.*;
import fj.F;
import fj.F2;
import fj.data.Option;
import fj.data.vector.V2;

public class Strings2 {
	public static final F<String, F<String, String>> append = curry(new F2<String, String, String>() {
		@Override
		public String f(final String a, final String b) {
			return a + b;
		}
	});
	
	public static final F<String, String> trim = new F<String, String>() {
		@Override
		public String f(final String string) {
			return string.trim();
		}
	};
	
	public static final F<String, Option<String>> emptyToNone = new F<String, Option<String>>() {
		@Override
		public Option<String> f(final String input) {
			if (input.equals("")) return none(); else return some(input);
		}
	};

	public static F<String, V2<String>> split(final String regex) {
		return new F<String, V2<String>>() {
			@Override
			public V2<String> f(final String input) {
				final String[] split = input.split(regex);
				return v(split[0], split[1]);
			}
		};
	}
	
	public static final F<String, String> toLowerCase = new F<String, String>() {
		@Override
		public String f(final String string) {
			return string.toLowerCase();
		}
	};
}
