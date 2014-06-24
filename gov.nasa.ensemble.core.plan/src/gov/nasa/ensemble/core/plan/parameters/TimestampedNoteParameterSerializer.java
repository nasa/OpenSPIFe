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
package gov.nasa.ensemble.core.plan.parameters;

import gov.nasa.ensemble.common.time.ISO8601DateFormat;
import gov.nasa.ensemble.common.ui.type.editor.TimestampedNote;

import org.apache.log4j.Logger;

public class TimestampedNoteParameterSerializer implements IParameterSerializer<TimestampedNote> {

	private final Logger trace = Logger.getLogger(getClass());
	private final ISO8601DateFormat dateFormat;

	public TimestampedNoteParameterSerializer() {
		dateFormat = new ISO8601DateFormat();
		dateFormat.setMillisFormatMode(true);
	}

	
	@Override
	public String getHibernateString(TimestampedNote javaObject) {
		if (javaObject == null) {
			return null;
		}
		synchronized (dateFormat) {
			return javaObject.value
				+  "\n" + dateFormat.format(javaObject.created)
				+  "\n" + dateFormat.format(javaObject.modified);
		}
	}

	@Override
	public TimestampedNote getJavaObject(String hibernateString) {
		if (hibernateString == null) {
			return null;
		}
		try {
			synchronized (dateFormat) {
				TimestampedNote result = new TimestampedNote();
				String[] lines = hibernateString.split("\n");
				result.value = lines[0];
				result.created = dateFormat.parse(lines[1]);
				result.modified = dateFormat.parse(lines[2]);
				return result;
			}
		} catch (Exception e) {
			trace.error("bad hibernate string", e);
		}		
		return null;
	}

}
