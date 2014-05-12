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

import static fj.Function.compose;
import static fj.pre.Show.showS;
import fj.F;
import fj.data.vector.V2;
import fj.data.vector.V3;
import fj.data.vector.V4;
import fj.pre.Show;

public class Shows {
	public static <A> Show<A> map(final Show<A> sa, final F<String, String> f) {
		return showS(compose(f, sa.showS_()));
	}
	
	private static final F<String, String> modifyBrackets(final char openBracket, final char closeBracket) {
		return new F<String, String>() {
			@Override
			public String f(final String s) {
				return openBracket + s.substring(1, s.length() - 1) + closeBracket;
			}
		};
	}
	
	public static <A> Show<V2<A>> v2Show(final char openBracket, final char closeBracket, final Show<A> show) {
		return map(Show.v2Show(show), modifyBrackets(openBracket, closeBracket));
	}
	
	public static <A> Show<V3<A>> v3Show(final char openBracket, final char closeBracket, final Show<A> show) {
		return map(Show.v3Show(show), modifyBrackets(openBracket, closeBracket));
	}
	
	public static <A> Show<V4<A>> v4Show(final char openBracket, final char closeBracket, final Show<A> show) {
		return map(Show.v4Show(show), modifyBrackets(openBracket, closeBracket));
	}
}
