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
package gov.nasa.ensemble.core.plan.formula.js;

import java.util.List;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptableList extends ScriptableObject {
	
	private final List list;
	
	public ScriptableList(List value) {
		this.list = value;
	}
	
	public List getList() {
		return list;
	}

	@Override
	public Object get(String name, Scriptable start) {
		if ("length".equals(name)) {
			return list.size();
		}
		return super.get(name, start);
	}

	@Override
	public Object get(int index, Scriptable start) {
		return JSUtils.unwrap(list.get(index));
	}

	@Override
	public String getClassName() {
		return "List";
	}

}
