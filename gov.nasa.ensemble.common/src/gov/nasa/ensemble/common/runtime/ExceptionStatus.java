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
package gov.nasa.ensemble.common.runtime;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

/**
 * This class makes a nice nested MultiStatus that will
 * allow the user to click "details" and see a full backtrace
 * in the dialog box.
 * 
 * @author abachman
 */
public class ExceptionStatus extends MultiStatus {

	public ExceptionStatus(String pluginId, String message, Throwable exception) {
		super(pluginId, IStatus.ERROR, message, exception);
		setSeverity(ERROR);
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		exception.printStackTrace(printWriter);
		StringBuffer buffer = stringWriter.getBuffer();
		appendStati(pluginId, buffer);
	}
	
	public ExceptionStatus(String pluginId, String message, String exception) {
		super(pluginId, IStatus.ERROR, message, null);
		setSeverity(ERROR);
		appendStati(pluginId, new StringBuffer(exception));
	}

	private void appendStati(String pluginId, StringBuffer buffer) {
		if (buffer.length() > 0) {
			int i = 1 + buffer.indexOf("\n", 1); // skip first line (already printed separately)
			while (i < buffer.length()) {
				int j = buffer.indexOf("\n", i);
				if (j == -1) {
					j = buffer.length();
				}
				String line = buffer.substring(i, j);
				add(new Status(IStatus.ERROR, pluginId, IStatus.OK, line, null));
				i = j + 1;
			}
		}
	}

}
