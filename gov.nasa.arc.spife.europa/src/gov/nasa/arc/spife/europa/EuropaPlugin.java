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
package gov.nasa.arc.spife.europa;

import gov.nasa.arc.spife.europa.clientside.EuropaClientSidePlugin;
import gov.nasa.arc.spife.europa.clientside.EuropaServerManager;
import gov.nasa.arc.spife.europa.clientside.EuropaServerManager.EuropaServerManagerPreferences;
import gov.nasa.arc.spife.europa.clientside.EuropaServerMonitor;
import gov.nasa.arc.spife.europa.preferences.EuropaPreferences;
import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class EuropaPlugin extends AbstractUIPlugin {

	public static final EuropaServerManagerPreferences PREF_STORE_PREFERENCES = new EuropaServerManager.EuropaServerManagerPreferences() {						
		@Override
		public boolean useXmlRpc() 
		{
			return EuropaPreferences.isUseXmlRpc();
		}

		@Override
		public boolean useRemoteServer() 
		{
			return EuropaPreferences.isUseRemoteServer();
		}

		@Override
		public String engineType() 
		{
			return EuropaPreferences.getEngineType();
		}
	};

	public static final String ID = "gov.nasa.arc.spife.europa";
	
	//The shared instance.
	private static EuropaPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
	 */
	public EuropaPlugin() {
		super();
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		if (!EuropaClientSidePlugin.getInitialized()) {
			EuropaServerMonitor.getInstance().setException(EuropaClientSidePlugin.getException());
			throw new RuntimeException("Failed to Start Europa Plugin because native lib is not available",EuropaClientSidePlugin.getException());
		}
		
		EuropaServerManager.setPreferences(PREF_STORE_PREFERENCES);
		
		if (!CommonPlugin.isJunitRunning()) {
			try {
				EuropaServerManager.startServerManager(null, false); //don't wait to wait when running as part of an application

				if (modelAutoExportNeeded()) 
					doModelAutoExport();		
			} catch (Throwable t) {
				LogUtil.error("starting EUROPA server manager", t);
			}
		}		
	}

	protected static boolean modelAutoExportNeeded()
	{
		return (EuropaPreferences.isDoModelAutoExport() &&
				!Europa.getModelAlreadyExported(ActivityDictionary.getInstance()));
	}
	
	protected static void doModelAutoExport()
	{
		Europa.setDoingModelAutoExport(true);
		Job exportModel = new Job("Loading Activity Dictionary") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					Europa.uploadModel(
							ActivityDictionary.getInstance(), 
							false, //overwriteModel 
							monitor
					);
				} 
				catch (Exception e) {
					LogUtil.error("EuropaModel auto-export failed",e);
				}
				finally {
					Europa.setDoingModelAutoExport(false);
				}
				return Status.OK_STATUS;
			}
		};    
		exportModel.schedule();		
	}
	
	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		if (!CommonPlugin.isJunitRunning()) {
			try {
				EuropaServerManager.stopServerManager();
			} catch (Throwable t) {
				LogUtil.error("stopping EUROPA server manager", t);
			}
		}
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static EuropaPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = EuropaPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("gov.nasa.arc.spife.europa.EuropaPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
}
