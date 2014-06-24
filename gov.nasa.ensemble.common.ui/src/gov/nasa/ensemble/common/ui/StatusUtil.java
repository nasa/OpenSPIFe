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
package gov.nasa.ensemble.common.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IMessageProvider;

/**
 * A utility class to work with IStatus.
 */
public class StatusUtil {

	public final static String IDE_WORKBENCH_PLUGIN_ID = "org.eclipse.ui.ide"; 
	
	/**
	 * Compares two instances of <code>IStatus</code>. The more severe is returned:
	 * An error is more severe than a warning, and a warning is more severe
	 * than ok. If the two stati have the same severity, the second is returned.
	 * @param s1 first status
	 * @param s2 second status
	 * @return the more severe status
	 */
	public static IStatus getMoreSevere(IStatus s1, IStatus s2) {
		if (s1.getSeverity() > s2.getSeverity()) {
			return s1;
		} else {
			return s2;
		}
	}

	/**
	 * Finds the most severe status from a array of stati.
	 * An error is more severe than a warning, and a warning is more severe
	 * than ok.
	 * @param status an array of stati
	 * @return the most severe status
	 */
	public static IStatus getMostSevere(IStatus[] status) {
		IStatus max= null;
		for (int i= 0; i < status.length; i++) {
			IStatus curr= status[i];
			if (curr.matches(IStatus.ERROR)) {
				return curr;
			}
			if (max == null || curr.getSeverity() > max.getSeverity()) {
				max= curr;
			}
		}
		return max;
	}

	/**
	 * Applies the status to the status line of a dialog page.
	 * @param page the dialog page
	 * @param status the status to apply
	 */
	public static void applyToStatusLine(DialogPage page, IStatus status) {
		String message= status.getMessage();
		if (message != null && message.length() == 0) {
			message= null;
		}
		switch (status.getSeverity()) {
			case IStatus.OK:
				page.setMessage(message, IMessageProvider.NONE);
				page.setErrorMessage(null);
				break;
			case IStatus.WARNING:
				page.setMessage(message, IMessageProvider.WARNING);
				page.setErrorMessage(null);
				break;
			case IStatus.INFO:
				page.setMessage(message, IMessageProvider.INFORMATION);
				page.setErrorMessage(null);
				break;
			default:
				page.setMessage(null);
				page.setErrorMessage(message);
				break;
		}
	}
	
	/**
	 * Answer a flat collection of the passed status and its recursive children
	 */
	@SuppressWarnings("unchecked")
	protected static List flatten(IStatus aStatus) {
		List result = new ArrayList();

		if (aStatus.isMultiStatus()) {
			IStatus[] children = aStatus.getChildren();
			for (int i = 0; i < children.length; i++) {
				IStatus currentChild = children[i];
				if (currentChild.isMultiStatus()) {
					Iterator childStatiiEnum = flatten(currentChild).iterator();
					while (childStatiiEnum.hasNext()) {
						result.add(childStatiiEnum.next());
					}
				} else {
					result.add(currentChild);
				}
			}
		} else {
			result.add(aStatus);
		}

		return result;
	}

	/**
	 * This method must not be called outside the workbench.
	 * 
	 * Utility method for creating status.
	 */
	protected static IStatus newStatus(IStatus[] stati, String message,
			Throwable exception) {

		if (message == null || message.trim().length() == 0) {
			throw new IllegalArgumentException();
		}
		
		return new MultiStatus(IDE_WORKBENCH_PLUGIN_ID, IStatus.ERROR,
				stati, message, exception);
	}

	
	/**
	 * This method must not be called outside the workbench.
	 * 
	 * Utility method for creating status.
	 * @param severity
	 * @param message
	 * @param exception
	 * @return {@link IStatus}
	 */
	public static IStatus newStatus(int severity, String message,
			Throwable exception) {

		String statusMessage = message;
		if (message == null || message.trim().length() == 0) {
			if (exception == null) {
				throw new IllegalArgumentException();
			} else if (exception.getMessage() == null) {
				statusMessage = exception.toString();
			} else {
				statusMessage = exception.getMessage();
			}
		}

		return new Status(severity, IDE_WORKBENCH_PLUGIN_ID, severity,
				statusMessage, exception);
	}

	
	/**
	 * This method must not be called outside the workbench.
	 * 
	 * Utility method for creating status.
	 * @param children
	 * @param message
	 * @param exception
	 * @return {@link IStatus}
	 */
	@SuppressWarnings("unchecked")
	public static IStatus newStatus(List children, String message,
			Throwable exception) {

		List flatStatusCollection = new ArrayList();
		Iterator iter = children.iterator();
		while (iter.hasNext()) {
			IStatus currentStatus = (IStatus) iter.next();
			Iterator childrenIter = flatten(currentStatus).iterator();
			while (childrenIter.hasNext()) {
				flatStatusCollection.add(childrenIter.next());
			}
		}

		IStatus[] stati = new IStatus[flatStatusCollection.size()];
		flatStatusCollection.toArray(stati);
		return newStatus(stati, message, exception);
	}	
}
