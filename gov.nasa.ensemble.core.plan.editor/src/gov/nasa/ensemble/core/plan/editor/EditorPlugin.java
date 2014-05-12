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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.ui.SynchronizedPreferenceStoreUIPlugin;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
@SuppressWarnings("restriction")
public class EditorPlugin extends SynchronizedPreferenceStoreUIPlugin {
	//The shared instance.
	private static EditorPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	public static final String ATTRIBUTE_CUSTODIAN 				= "ensemble_custodian";
	public static final String ATTRIBUTE_UPGRADE_NOTES          = "ensemble_upgrade_notes";
	public static final String ATTRIBUTE_PLAN_STATE 			= "ensemble_plan_state";
	public static final String ATTRIBUTE_NOTES     				= "ensemble_notes";
	public static final String ATTRIBUTE_GROUP     				= "ensemble_group";
	public static final String ATTRIBUTE_SOURCE_URI             = "source_uri";

	public static final String ID = "gov.nasa.ensemble.core.plan.editor";
	
	/**
	 * The constructor.
	 */
	public EditorPlugin() {
		super();
		CommonPlugin.getDefault(); // for JobOperationHistory initialization
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		IOperationHistory history = OperationHistoryFactory.getOperationHistory();
		history.addOperationApprover(PlanReadOnlyOperationApprover.getInstance());
		
		if (PlatformUI.isWorkbenchRunning()) {
			declareImageDescriptors();
		}
	}

	private void declareImageDescriptors() {
	    /*  this code is neccessary at this time because certain eclipse
			resources have been removed from other places. */
	
		final ImageDescriptor errorImageDescriptor
			= imageDescriptorFromPlugin(IDEWorkbenchPlugin
				.IDE_WORKBENCH, "icons/full/obj16/error_tsk.gif");
		
		final ImageDescriptor warningImageDescriptor
		= imageDescriptorFromPlugin(IDEWorkbenchPlugin
				.IDE_WORKBENCH, "icons/full/obj16/warn_tsk.gif");
		
		/*
		final ImageDescriptor infoImageDescriptor
		= imageDescriptorFromPlugin(IDEWorkbenchPlugin
				.IDE_WORKBENCH, "icons/full/obj16/info_tsk.gif");
				*/
		
		final boolean shared = true;
		
		WorkbenchImages.declareImage(IDEInternalWorkbenchImages
				.IMG_OBJS_ERROR_PATH, errorImageDescriptor, shared);
		
		WorkbenchImages.declareImage(IDEInternalWorkbenchImages
				.IMG_OBJS_WARNING_PATH, warningImageDescriptor, shared);
		
		/*
		WorkbenchImages.declareImage(IDEInternalWorkbenchImages
				.IMG_OBJS_INFO_PATH, infoImageDescriptor, shared);
				*/
    }

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static EditorPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = EditorPlugin.getDefault().getResourceBundle();
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
				resourceBundle = ResourceBundle.getBundle("gov.nasa.ensemble.plan.editor.EditorPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
		
}
