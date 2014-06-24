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

public class Longs {
	
	public static final F<String, Option<Long>> parseLong = parseLong(true);
	
	public static final F<String, Option<Long>> parseLong(final boolean handleHex) {
		return new F<String, Option<Long>>() {
			@Override
			public Option<Long> f(final String s) {
				try {
					if (handleHex && s.toLowerCase().startsWith("0x"))
						return some(Long.parseLong(s.substring(2), 16));
					else
						return some(Long.parseLong(s));
				} catch (NumberFormatException e) {
					return none();
				}
			}
		};
	}
	
	public static Option<Long> parseLong(final String s) {
		return parseLong.f(s);
	}
}
