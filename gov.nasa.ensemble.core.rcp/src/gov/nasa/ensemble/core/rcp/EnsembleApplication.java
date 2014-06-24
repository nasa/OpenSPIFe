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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.authentication.AuthenticationUtil;
import gov.nasa.ensemble.common.authentication.Authenticator;
import gov.nasa.ensemble.common.extension.DynamicExtensionUtils;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;

import java.net.PasswordAuthentication;
import java.util.Properties;

import org.apache.http.auth.Credentials;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class EnsembleApplication implements IApplication {

	private static final String APACHE_SERVER_USER_PROPERTY = "apache.server.user";
	private static final String APACHE_SERVER_PASS_PROPERTY = "apache.server.pass";
	private static Credentials credentials;

	Logger trace = Logger.getLogger(EnsembleApplication.class);

	@Override
	public Object start(IApplicationContext context) throws Exception {
		//
		// Comment out check until the missing
//		if (!checkWorkspaceLock(new Shell(PlatformUI.createDisplay(), SWT.ON_TOP))) {
//			Platform.endSplash();
//			return IApplication.EXIT_OK;
//		}

		String productName = "Ensemble";
		if (Platform.getProduct() != null)
			productName = Platform.getProduct().getName();
		Display.setAppName(productName);

		Authenticator login = AuthenticationUtil.getAuthenticator();
		boolean isAuthenticated = false;

		if (login != null) {
			// may need these credentials later
			credentials = login.getCredentials();
			isAuthenticated = login.isAuthenticated();
		} else {
			// Setup the authenticator that gets us through the password check on the website
			java.net.Authenticator.setDefault(new java.net.Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					Properties properties = CommonPlugin.getDefault().getEnsembleProperties();
					String user = properties.getProperty(APACHE_SERVER_USER_PROPERTY, "");
					String pass = properties.getProperty(APACHE_SERVER_PASS_PROPERTY, "");
					return new PasswordAuthentication(user, pass.toCharArray());
				}
			});

			isAuthenticated = true;
		}
		Display display = null;
		String sleakEnabled = EnsembleProperties.getProperty("sleak.enabled");
		if (sleakEnabled != null && Boolean.valueOf(sleakEnabled)){
			DeviceData data = new DeviceData();
		    data.tracking = true;
		    display = new Display(data);
		    Sleak sleak = new Sleak();
		    sleak.open();
		} else {
			display = Display.getDefault();
		}
		PlatformUI.createDisplay();
		try {
			if (isAuthenticated) {
				WorkbenchAdvisor workbenchAdvisor;
				try {
					workbenchAdvisor = MissionExtender.construct(EnsembleWorkbenchAdvisor.class);
				} catch (ConstructionException e) {
					trace.warn("couldn't instantiate mission-specific EnsembleWorkbenchAdvisor");
					workbenchAdvisor = new EnsembleWorkbenchAdvisor() {
						@Override
						public String getInitialWindowPerspectiveId() {
							return null;
						}
					};
				}
				DynamicExtensionUtils.removeIgnoredExtensions();
				int returnCode = PlatformUI.createAndRunWorkbench(display, workbenchAdvisor);
				if (returnCode == PlatformUI.RETURN_RESTART) {
					return IApplication.EXIT_RESTART;
				}
				return IApplication.EXIT_OK;
			}
			// authentication failed
			context.applicationRunning();
			return IApplication.EXIT_OK;

		} finally {
			display.dispose();
		}
	}

//	protected boolean checkWorkspaceLock(Shell shell) {
//		Location workspaceLocation = Platform.getInstanceLocation();
//		IOException exception = null;
//		try {
//			if (workspaceLocation.lock()) {
//				return true;
//			}
//		} catch (IOException e) {
//			exception = e;
//		}
//		String appName = Platform.getProduct().getName();
//		String message = appName + " cannot be started while another instance is running. Switch to the running instance or close it and restart the application again";
//		if (exception != null) {
//			trace.error(message, exception);
//			dumpFrameworkProperties();
//			MessageDialog.openInformation(shell, "Cannot start " + appName, exception.getMessage());
//		} else {
//			MessageDialog.openInformation(shell, "Cannot start " + appName, message);
//		}
//		return false;
//	}
//
//	@SuppressWarnings("restriction")
//	private void dumpFrameworkProperties() {
//		Properties properties = FrameworkProperties.getProperties();
//		int maxKeyLength = 0;
//		for (Object key : properties.keySet()) {
//			maxKeyLength = Math.max(key.toString().length(), maxKeyLength);
//		}
//		
//		for (Object key : properties.keySet()) {
//			int keyLength = key.toString().length();
//			StringBuffer buffer = new StringBuffer();
//			buffer.append(key);
//			for (int i=0; i<maxKeyLength - keyLength; i++) {
//				buffer.append(' ');
//			}
//			String value = properties.getProperty((String) key);
//			System.out.println(buffer.toString()+'|'+value);
//		}
//	}

	@Override
	public void stop() {
//		Location workspaceLocation = Platform.getInstanceLocation();
//		workspaceLocation.release();
		final IWorkbench workbench;
		try {
			workbench = PlatformUI.getWorkbench();
		} catch (IllegalStateException e) {
			// already done (or not started)
			return;
		}
		final Display display = workbench.getDisplay();
		if (!display.isDisposed()) {
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					if (!display.isDisposed()) {
						workbench.close();
					}					
				}
			});
		}
	}

	/**
	 * 
	 * @return The master username/password key
	 */
	public static Credentials getCredentials() {
		return credentials;
	}
}
