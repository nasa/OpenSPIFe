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
import gov.nasa.ensemble.common.ui.SynchronizedPreferenceStoreUIPlugin;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class RcpPlugin extends SynchronizedPreferenceStoreUIPlugin {
	
    //The shared instance.
    private static RcpPlugin plugin;
    //Resource bundle.
    private ResourceBundle resourceBundle;
    
    private OperationHistoryMonitor operationHistoryMonitor = new OperationHistoryMonitor();
    
    public static final String PLUGIN_ID = "gov.nasa.ensemble.core.rcp";
    
    /**
     * The constructor.
     */
    public RcpPlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle("gov.nasa.ensemble.core.rcp.RcpPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }
    
    /**
     * This method is called upon plug-in activation
     */
    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        CommonPlugin.getDefault(); // set up JobOperationHistory
        OperationHistoryFactory.getOperationHistory().addOperationHistoryListener(operationHistoryMonitor);
    }
    
    /**
     * This method is called when the plug-in is stopped
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        try {
            plugin = null;
            resourceBundle = null;
            OperationHistoryFactory.getOperationHistory().removeOperationHistoryListener(operationHistoryMonitor);
        } finally {
            super.stop(context);
        }
    }
    
    /**
     * Returns the shared instance.
     */
    public static RcpPlugin getDefault() {
        return plugin;
    }
    
    /**
     * Returns the string from the plugin's resource bundle,
     * or 'key' if not found.
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle = RcpPlugin.getDefault().getResourceBundle();
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
                resourceBundle = ResourceBundle.getBundle("gov.nasa.ensemble.core.rcp.RcpPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
        return resourceBundle;
    }  
     
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("gov.nasa.ensemble.core.rcp", path);
	}
    
}
