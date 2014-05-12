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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.transfers.ClipboardServer;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class WidgetPlugin extends SynchronizedPreferenceStoreUIPlugin {

	public static final String PLUGIN_ID = "gov.nasa.ensemble.common.ui";
	
	//The shared instance.
	private static WidgetPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	public static final String KEY_IMAGE_CLOSE_NICE = "image.close.nice";
	private static final String PATH_IMAGE_CLOSE_NICE = "icons/nice_close_button.gif";

	public static final String KEY_IMAGE_NAV_DOWN = "image.nav.down";
	private static final String PATH_IMAGE_NAV_DOWN = "icons/down.gif";

	public static final String KEY_IMAGE_NAV_UP = "image.nav.up";
	private static final String PATH_IMAGE_NAV_UP = "icons/up.gif";

	public static final String KEY_IMAGE_PLUS = "image.plus";
	private static final String PATH_IMAGE_PLUS = "icons/plus.png";

	public static final String KEY_IMAGE_MINUS = "image.minus";
	private static final String PATH_IMAGE_MINUS = "icons/minus.png";
	
	public static final String KEY_IMAGE_GRAPH = "image.graph";
	private static final String PATH_IMAGE_GRAPH = "icons/resource_profile.gif";
	
	public static final String KEY_IMAGE_GRAPH_MODELED = "image.graph.modeled";
	private static final String PATH_IMAGE_GRAPH_MODELED = "icons/resource_profile_modeled.gif";
	
	public static final String KEY_IMAGE_GRAPH_DISCRETE = "image.graph.discrete";
	private static final String PATH_IMAGE_GRAPH_DISCRETE = "icons/resource_profile_discrete.gif";
	
	public static final String KEY_IMAGE_GRAPH_DISCRETE_MODELED = "image.graph.discrete.modeled";
	private static final String PATH_IMAGE_GRAPH_DISCRETE_MODELED = "icons/resource_profile_discrete_modeled.gif";
	
	public static final String KEY_IMAGE_FOLDER_OPEN = "image.folder.open";
	private static final String PATH_IMAGE_FOLDER_OPEN = "icons/folder.gif";
	
	/**
	 * The constructor.
	 */
	public WidgetPlugin() {
		super();
		plugin = this;
		CommonPlugin.getDefault(); // for JobOperationHistory initialization
	}
	
	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}
	
	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		ClipboardServer.instance.disposeClipboards();
		IconLoader.disposeImagesInCache();
		ColorMap.RGB_INSTANCE.dispose();
		plugin = null;
		resourceBundle = null;
	}
	
	/**
	 * Returns the shared instance.
	 */
	public static WidgetPlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = WidgetPlugin.getDefault().getResourceBundle();
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
				resourceBundle = ResourceBundle.getBundle("gov.nasa.ensemble.common.ui.WidgetPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}

	public static Image getImage(String path) {
		ImageDescriptor imageDescriptor = getImageDescriptor(path);
		if (imageDescriptor != null) {
			return imageDescriptor.createImage();
		}
		return null;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("gov.nasa.ensemble.common.ui", path);
	}

	public static Image getImageFromRegistry(String key) {
		return getDefault().getImageRegistry().get(key);
	}
	
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(KEY_IMAGE_CLOSE_NICE, SWTUtils.getImage(this, PATH_IMAGE_CLOSE_NICE));
		reg.put(KEY_IMAGE_NAV_DOWN, SWTUtils.getImage(this, PATH_IMAGE_NAV_DOWN));
		reg.put(KEY_IMAGE_NAV_UP, SWTUtils.getImage(this, PATH_IMAGE_NAV_UP));
		reg.put(KEY_IMAGE_PLUS, SWTUtils.getImage(this, PATH_IMAGE_PLUS));
		reg.put(KEY_IMAGE_MINUS, SWTUtils.getImage(this, PATH_IMAGE_MINUS));
		reg.put(KEY_IMAGE_GRAPH, SWTUtils.getImage(this, PATH_IMAGE_GRAPH));
		reg.put(KEY_IMAGE_GRAPH_MODELED, SWTUtils.getImage(this, PATH_IMAGE_GRAPH_MODELED));
		reg.put(KEY_IMAGE_GRAPH_DISCRETE, SWTUtils.getImage(this, PATH_IMAGE_GRAPH_DISCRETE));
		reg.put(KEY_IMAGE_GRAPH_DISCRETE_MODELED, SWTUtils.getImage(this, PATH_IMAGE_GRAPH_DISCRETE_MODELED));
		reg.put(KEY_IMAGE_FOLDER_OPEN, SWTUtils.getImage(this, PATH_IMAGE_FOLDER_OPEN));
	}
	
}
