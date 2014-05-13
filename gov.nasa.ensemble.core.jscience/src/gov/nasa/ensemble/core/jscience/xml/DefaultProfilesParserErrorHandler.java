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
package gov.nasa.ensemble.core.jscience.xml;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.runtime.ExceptionStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

public class DefaultProfilesParserErrorHandler implements ProfilesParserErrorHandler {
	public static final String PLUGIN_ID = "gov.nasa.ensemble.ngps";
	protected List<Throwable> errors = new ArrayList<Throwable>();
	
	public List<Throwable> getUnhandledExceptions() {
		return errors;
	}
	
	@Override
	public void unhandledException(Throwable t) {
		LogUtil.error(t);
		errors.add(t);
	}

	@Override
	public void parseComplete() {
		if (!errors.isEmpty())
			LogUtil.error(errors.size() + " exceptions thrown while parsing profiles.");
	}

	@Override
	public IStatus getStatus() {
		if (errors.size() == 1) {
			Throwable t = errors.get(0);
			return new ExceptionStatus(PLUGIN_ID, createExceptionsMessage(), t);
		} else if (errors.size() > 1)  {
			MultiStatus status = new MultiStatus(PLUGIN_ID, IStatus.ERROR, createExceptionsMessage(), null);
			for (Throwable t : errors) {
				status.add(new ExceptionStatus(PLUGIN_ID, createExceptionsMessage(), t));
			}
			return status;			
		}
		return Status.OK_STATUS;
	}

	private String createExceptionsMessage() {
		StringBuilder message = new StringBuilder();
		SortedMap<String, Integer> errorCounts = new TreeMap<String, Integer>();
		for (Throwable error : errors) {
			String errorClassName = error.getClass().getName();
			if (!errorCounts.containsKey(errorClassName)) {
				errorCounts.put(errorClassName, 1);
			}
			else {
				errorCounts.put(errorClassName, errorCounts.get(errorClassName) + 1);
			}
		}
		Iterator<Entry<String, Integer>> errorCountsIterator = errorCounts.entrySet().iterator();
		Entry<String, Integer> errorCount = errorCountsIterator.next();
		message.append(errorCount.getValue()).append(" ").append(errorCount.getKey());
		if (errorCount.getValue() > 1)
			message.append("s");
		while (errorCountsIterator.hasNext()) {
			errorCount = errorCountsIterator.next();
			message.append(", ");
			if (!errorCountsIterator.hasNext())
				message.append("and ");
			message.append(errorCount.getValue()).append(" ").append(errorCount.getKey());
			if (errorCount.getValue() > 1)
				message.append("s");
		}
		return message.toString();
	}
	
}
