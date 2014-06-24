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

import java.net.MalformedURLException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

/**
 * An editor launcher for opening the internal Eclipse browser. May be referenced as the launcher property when declaring an editor
 * for a file type in the extension regisry.
 * 
 * @author rnado
 *
 */
public class BrowserLauncher implements IEditorLauncher {

	@Override
	public void open(IPath file) {
		IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
		try {
				support.createBrowser(IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR,
						file.toPortableString(), null, null).openURL(file.toFile().toURI().toURL());
		}
		catch (MalformedURLException e) {
			// ignore
		}
		catch (PartInitException e) {
			MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					"Failed to open the internal Eclipse browser", e.getLocalizedMessage());
		}
	}

}
