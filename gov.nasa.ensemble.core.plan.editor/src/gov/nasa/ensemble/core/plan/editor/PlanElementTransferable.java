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
package gov.nasa.ensemble.core.plan.editor;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class PlanElementTransferable implements IPlanElementTransferable, Serializable {

	protected Map<String, Object> map = new HashMap<String, Object>();

	public Map<String, Object> getMap() {
		return map;
	}

	@Override
	public void setData(String key, Object value) {
		map.put(key, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getData(String key) {
		return (T)map.get(key);
	}

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(map);
	}

	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
				Object object = in.readObject();
				if (object instanceof Map) {
					map = (Map<String, Object>)object;
				} else {
					throw new IOException("unexpected object type [expected Map]");
				}
			}

	@Override
	public String toString() {
	    StringBuilder builder = new StringBuilder(getClass().getSimpleName());
	    builder.append(map.toString());
		return builder.toString();
	}
	
}
