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
package gov.nasa.ensemble.common.flags;

import java.util.HashMap;
import java.util.Map;

public class Flags {

	public static final Map<String, ThreadLocal<Boolean>> flags = new HashMap<String, ThreadLocal<Boolean>>();
	
	public static void setFlag(String name, boolean value) {
		get(name).set(value);
	}
	
	public static boolean getFlag(String name) {
		return get(name).get();
	}
	
	private static ThreadLocal<Boolean> get(String name) {
		ThreadLocal<Boolean> flag = flags.get(name);
		if (flag == null) {
			flag = new ThreadLocal<Boolean>()
			{
				/** Initialize the flag to false (failing to initialize causes an NPE). */
				@Override
				protected Boolean initialValue() { return false; }
			};
			flags.put(name, flag);
		}
		return flag;
	}
}
