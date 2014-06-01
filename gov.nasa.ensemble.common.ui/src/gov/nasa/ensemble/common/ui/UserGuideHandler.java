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

import gov.nasa.ensemble.common.EnsembleProperties;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.handlers.HandlerUtil;

public class UserGuideHandler extends AbstractHandler {

	private static final String user_guide_location = "user.guide.location";
	
	@Override
	public Object execute(ExecutionEvent event) {
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		String location = EnsembleProperties.getProperty(user_guide_location);
		if (location == null) {
			MessageDialog.openError(shell, "Error", "No user.guide.location configured in the Ensemble properties");
			return null;
		}
		try {
			URL url = new URL(location);
			IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = browserSupport.getExternalBrowser();
			browser.openURL(url);
		} catch (MalformedURLException e) {
			MessageDialog.openError(shell, "Error", "The user.guide.location in the Ensemble properties is not a valid URL :\n" + location);
		} catch (PartInitException e) {
			MessageDialog.openError(shell, "Error", "Failed to open external browser on URL:\n" + location);
		}
		return null;
	}

}
