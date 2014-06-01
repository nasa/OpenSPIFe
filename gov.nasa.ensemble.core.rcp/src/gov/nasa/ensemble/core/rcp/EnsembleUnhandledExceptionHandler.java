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
package gov.nasa.ensemble.core.rcp;

import gov.nasa.ensemble.common.ui.ForbiddenWorkbenchUtils;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;

/**
 * Handles all uncaught exceptions by logging them.
 */
public class EnsembleUnhandledExceptionHandler implements UncaughtExceptionHandler, ILogListener {

	private static final List<String> SPURIOUS_ERRORS = Arrays.asList(
		"WARNING: Prevented recursive attempt to activate part gov.nasa.ensemble.core.detail.view.DetailView while still in the middle of activating part",
		"Core exception while retrieving the content description",
		"An exception occurred during console notification",
		"Problems occurred when invoking code from plug-in: \"org.eclipse.ui.console\".");
	
	private static EnsembleUnhandledExceptionHandler instance = null;
	protected Logger trace = Logger.getLogger(EnsembleUnhandledExceptionHandler.class);
	
	protected EnsembleUnhandledExceptionHandler() {
		// create through getInstance()
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		if (exception instanceof ThreadDeath) {
			// carry on
			return;
		}
		if (exception instanceof ClassCastException) {
			if ("sun.java2d.HeadlessGraphicsEnvironment cannot be cast to apple.awt.CGraphicsEnvironment".equals(exception.getMessage())) {
				// harmless error that occurs when display is disconnected
				return;
			}
		}
		trace.error(exception.getLocalizedMessage(), exception);
	}
	
	public static EnsembleUnhandledExceptionHandler getInstance() {
		if (instance == null) instance = new EnsembleUnhandledExceptionHandler();
		return instance;
	}

	@Override
	public void logging(IStatus status, String plugin) {
		String message = status.getMessage();
		for (String error : SPURIOUS_ERRORS) {
			if (message.contains(error))
				return;
		}
		String PART_ALREADY_EXISTS = "Part already exists in page layout: ";
		if (message.startsWith(PART_ALREADY_EXISTS)) {
			String partId = message.substring(PART_ALREADY_EXISTS.length(), message.length() - 1);
			if (ForbiddenWorkbenchUtils.alreadyExistingPart(partId)) {
				return;
			}
		}
		if ((status.getSeverity() & IStatus.ERROR) != 0)
			trace.error(message, status.getException());
		else if ((status.getSeverity() & IStatus.WARNING) != 0)
			trace.warn(message, status.getException());
	}
}
