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
package gov.nasa.arc.spife.europa.clientside.xmlrpc;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

public class ClientExceptionHandler {

	public static void handle(final Exception e) {

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				openErrorDialog(e, MessageDialog.WARNING, new String[] { "OK" });
			}
		});
		throw new RuntimeException(e);

	}

	private static MessageDialog openErrorDialog(final Exception e, int dialogType, String[] buttons) {

		String exceptionMessage = "unknown";
		if (e != null) exceptionMessage = e.getMessage();
		String causeExceptionMessage = "unknown";
		if (e != null && e.getCause() != null) causeExceptionMessage = e.getCause().toString();

		MessageDialog dialog = new MessageDialog(Display.getDefault().getActiveShell(), "Europa2 Problem", null,
				"There is a problem connecting with Europa2.  It is possible that "
						+ "there is a network problem.\n\n" + "Reason:  "
						+ truncateExceptionString(exceptionMessage)
						+ "\n\nCause:  "
						+ truncateExceptionString(causeExceptionMessage),
				dialogType, buttons,
				0); // 0 to select first (and only) button by default
		dialog.open();
		return dialog;
	}

	private static String truncateExceptionString(String originalString) {
		if (originalString.length() < 200)
			return originalString;
		return originalString.substring(0, 200) + " ...\n(truncated)";
	}

}
