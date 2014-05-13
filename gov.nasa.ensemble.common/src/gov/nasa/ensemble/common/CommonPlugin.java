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
package gov.nasa.ensemble.common;

import gov.nasa.ensemble.common.debug.EnsembleUsageLogger;
//import gov.nasa.ensemble.common.documentation.EnsemblePropertiesSurveyor;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.EnsembleLoggingConfigurator;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.operation.JobOperationHistory;
import gov.nasa.ensemble.common.preferences.EnsembleCommonPreferenceConstants;
import gov.nasa.ensemble.common.properties.EnsemblePropertiesPatch;
import gov.nasa.ensemble.common.properties.EnsemblePropertiesProvider;
import gov.nasa.ensemble.common.properties.EnsemblePropertiesProvider.ProductPropertiesProvider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class CommonPlugin extends Plugin {
	
	//The shared instance
	private static CommonPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	public static final String ID = "gov.nasa.ensemble.common";

    private static final String ENSEMBLE_PROPERTIES_PROVIDER_EXT_POINT = "gov.nasa.ensemble.common.EnsemblePropertiesProvider";
	
	public static final String JUNIT_IS_RUNNING_PROPERTY    = "junit.isRunning";
	private static final String ECLIPSE_APPLICATION_PROPERTY = "eclipse.application";
	private static final String ECLIPSE_COMMANDS_PROPERTY    = "eclipse.commands";

    private Properties ensembleProperties = null;
	private List<String> ensemblePropertiesDefinedInFile;

	/**
	 * Checks whether the 'junit.isRunning' property is set to "true". If not, then it checks the 'eclipse.application' property to
	 * test whether the application currently running has "testapplication" is its name. If yes, it returns true. Else, returns
	 * false.
	 * 
	 * NOTE: Bamboo sets the 'junit.isRunning' property before all of its test cases. And when developers run a test case as a
	 * "JUnit Plug-in Test" the application will include "testapplication" in the name. If the test case is run as "JUnit Test",
	 * this method will not be reliable.
	 * 
	 * @return boolean
	 */
	public static boolean isJunitRunning() {
		Properties p = System.getProperties();

		// debug
		// for(Object obj : p.keySet()) {
		// String prop = (String) obj;
		// System.out.println(prop + " : " + p.getProperty(prop));
		// }

		// cruise control sets this property when running test cases
		String junit = p.getProperty(JUNIT_IS_RUNNING_PROPERTY, "false");
		if (junit.equals("true"))
			return true;

		// when running test cases from the GUI, one of the following two
		// properties will most likely be set (depending upon your Eclipse
		// version). This heuristic may have to be updated as new Eclipse
		// versions come on line and change their running property set

		// Eclipse 3.1
		String app = p.getProperty(ECLIPSE_APPLICATION_PROPERTY, "");
		if (app.contains("testapplication"))
			return true;

		// Eclipse 3.2
		String cmds = p.getProperty(ECLIPSE_COMMANDS_PROPERTY, "");
		if (cmds.contains("junit") && cmds.contains("testapplication"))
			return true;
		if (cmds.contains("EnsembleTestRunner"))
			return true;

		// didn't find any of the properties we were looking for so the only
		// conclusion at this point is that we are not in a test case
		return false;
	}	
	
	/**
	 * The constructor.
	 */
	public CommonPlugin() {
		super();
		plugin = this;
	}
	
	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		OperationHistoryFactory.setOperationHistory(JobOperationHistory.INSTANCE);
		// configure logging
		// TODO this may cease working once the EnsembleLoggingConfigurator is extended
		ensembleProperties = initializeEnsembleProperties();
//		EnsemblePropertiesSurveyor.initialize();
		EnsembleLoggingConfigurator construct = MissionExtender.construct(EnsembleLoggingConfigurator.class);
		if (construct != null) {
			construct.configureLogging();
		} else {
			// logging not initialized
			System.err.println("EnsembleLoggingConfigurator == null");
		}
        
	}
    
    public Properties getEnsembleProperties() {
        return ensembleProperties;
    }

    public List<String> getEnsemblePropertiesInFile() {
        return ensemblePropertiesDefinedInFile;
    }

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		try {
//			EnsemblePropertiesSurveyor.generateReport();
			EnsembleUsageLogger.removeErrorAppender();
			plugin = null;
			resourceBundle = null;
		} finally {
			super.stop(context);
		}
	}
	
	/**
	 * Returns the shared instance.
	 */
	public static CommonPlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = CommonPlugin.getDefault().getResourceBundle();
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
				resourceBundle = ResourceBundle.getBundle("gov.nasa.ensemble.common.CommonPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
    
    /**
     * Initialize Ensemble with the ensemble properties file. This method will
     * attempt to find the properties file in the following manner:
     * 
     * 1. Search for the system property "ensemble.properties.file". If this
     * property is defined, then use the property's value to find the properties
     * file.
     * 
     * 2. Search for the extension point
     * gov.nasa.ensemble.core.rcp.EnsemblePropertiesProvider. If one and only
     * one of these extension points are defined, then use it to create an
     * EnsemblePropertiesProvider class and then ask this class for the
     * properties file location.
     * 
     * 3. If neither of these options provide a properties file location, then
     * return an empty properties object. Else read in the properties file to a
     * properties object and return it.  In addition, populate the System
     * properties with all the Ensemble properties.
     * 
     * 4. If there are any registered implementations of IEnsemblePropertiesPatch,
     * load their properties on top of the properties assembled thus far.
     * 
     */
    private Properties initializeEnsembleProperties() {
    	final Properties props = basicInitializeEnsembleProperties();
    	ensemblePropertiesDefinedInFile = new ArrayList(props.keySet());
     	TreeSet<EnsemblePropertiesPatch> patchers = new TreeSet<EnsemblePropertiesPatch>(ClassRegistry.createInstances(EnsemblePropertiesPatch.class));
		for (EnsemblePropertiesPatch patch : patchers)
    		props.putAll(patch.getPatchedProperties(props));
		    	
    	// MAE-5459, -D<property>=<value> should override ensemble.properties
    	props.putAll(System.getProperties());
    	
        // add the ensemble properties to the System properties 
        for(Object obj : props.keySet()) {
            String key   = (String) obj;
            
            // Don't patch in empty keys
            if (key.isEmpty())
            	continue;
            
            System.setProperty(key, props.getProperty(key));
        }
            	
    	return props;
    }
    
    private Properties basicInitializeEnsembleProperties() {
    	boolean usingExtensionPoint = false;
        Properties ensembleProperties = new Properties();
        
        String propertiesFileLocation = System.getProperty(EnsemblePropertiesProvider.ENSEMBLE_PROPERTIES_FILE_PROPERTY);
        if (CommonUtils.isNullOrEmpty(propertiesFileLocation)) {
            IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
            IExtensionPoint extPoint = extRegistry.getExtensionPoint(ENSEMBLE_PROPERTIES_PROVIDER_EXT_POINT);
            if (extPoint == null)
            	return new Properties();
            
            IConfigurationElement[] extensions = extPoint.getConfigurationElements();
            if (extensions.length == 1) {
                try {
                    IConfigurationElement ext = extensions[0];
                    EnsemblePropertiesProvider p = (EnsemblePropertiesProvider) ext.createExecutableExtension("class");
                    propertiesFileLocation = p.getPropertiesFileLocation();
                    usingExtensionPoint = true;
                } catch (Exception e) {
                    System.err.println("Error instantiating extension point " + ENSEMBLE_PROPERTIES_PROVIDER_EXT_POINT + ": " + e);
                    e.printStackTrace(System.err);
                }
            }
            else if (extensions.length > 1) {
                System.err.println("More than one extension declared (" + extensions.length + ") for extension point " + 
                        ENSEMBLE_PROPERTIES_PROVIDER_EXT_POINT + ".");
                for (int i = 0 ; i < extensions.length ; i++) {
                	System.err.println("  " + extensions[i].getAttribute("class"));
                }
            }
        }
        
        if (CommonUtils.isNullOrEmpty(propertiesFileLocation)) {
        	InputStream propertiesInputStream = null;
			try {
				ProductPropertiesProvider p = new ProductPropertiesProvider();
				propertiesInputStream = p.getPropertiesInputStream();
				if (propertiesInputStream != null)
					ensembleProperties.load(propertiesInputStream);
			} catch (Exception e) {
				// Logging failed to get initialized
				System.err.println("Error while attempting to open/read properties file defined by product: ");
				//e.printStackTrace(System.err);
			} finally {
				IOUtils.closeQuietly(propertiesInputStream);
			}
        } else if (propertiesFileLocation != null && propertiesFileLocation.trim().length() != 0) {
            try {
                FileInputStream propertiesFile = new FileInputStream(propertiesFileLocation);
                ensembleProperties.load(propertiesFile);
                propertiesFile.close();
                
                // output which properties file the system is using and its source
                @SuppressWarnings("unused")
				String propertySource = usingExtensionPoint ? ENSEMBLE_PROPERTIES_PROVIDER_EXT_POINT + " extension point" :
                	EnsemblePropertiesProvider.ENSEMBLE_PROPERTIES_FILE_PROPERTY + " system property";
//                System.out.println("using the " + propertySource + " to define the location " +
//                		"of the properties file: " + propertiesFileLocation);
            } catch (Exception e) {
            	System.err.println("Error while attempting to open/read properties file '" + propertiesFileLocation + "': " + e);
            	e.printStackTrace(System.err);
            }
        } else {
        	System.err.println("Unable to find an ensemble properties file (either through the " +
                    EnsemblePropertiesProvider.ENSEMBLE_PROPERTIES_FILE_PROPERTY + " property or the extension point " +
                    ENSEMBLE_PROPERTIES_PROVIDER_EXT_POINT + ").");
        }
        
        // TODO: Add property contributor for XML.
        
        return ensembleProperties;
    }
    
	private static String cachedExercise;
    
    /**
	 * Convenience method to return the current exercise of the application.
	 * 
	 * @return the current exercise (String)
	 */
    @SuppressWarnings("deprecation")
	public static String getExercise() {
    	// the last exercise needs to be initialized
    	if (cachedExercise == null) { 
    		cachedExercise = System.getProperty("ensemble.exercise");
    		if (cachedExercise== null || cachedExercise.trim().length()==0) 
    			cachedExercise = plugin.getPluginPreferences().getString(EnsembleCommonPreferenceConstants.P_EXERCISE);
    	}
    	return cachedExercise;
    }
}
