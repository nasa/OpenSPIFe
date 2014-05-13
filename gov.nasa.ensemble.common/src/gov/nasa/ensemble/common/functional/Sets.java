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

import java.util.ArrayList;

import fj.data.Array;
import fj.data.Java;
import fj.data.Set;

public class Sets {

	public static <T> Set<T> fj(java.util.Set<T> set) {
		return Set.iterableSet(Orders.<T>unordered(), set);
	}

	public static <T> ArrayList<T> j(Array<T> array) {
		return Java.<T>Array_ArrayList().f(array);
	}

}
