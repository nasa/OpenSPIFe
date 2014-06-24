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

import org.junit.Assert;
import org.junit.Test;

public class TestFunctions extends Assert {
	@Test
	public void cast() {
		final Object string = "Hello";
		Functions.<Object, String>cast().f(string);
		Functions.cast(Object.class, String.class).f(string);
	}
	
	@Test
	public void safeCast() {
		final Object string = "Hello";
		assertTrue(Functions.safeCast(String.class).f(string).isSome());
		assertTrue(Functions.safeCast(Object.class, String.class).f(string).isSome());
	}
}
