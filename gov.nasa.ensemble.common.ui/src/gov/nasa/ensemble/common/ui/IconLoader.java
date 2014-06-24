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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

public abstract class IconLoader
{
	private static final Logger trace = Logger.getLogger(IconLoader.class);
	
	/**
	 * An icon cache for all icons that are retrieved via this plug-in's
	 * getIcon() methods
	 */
	private static final Map<String, Image> iconCache = new HashMap<String, Image>();
	
	/**
	 * Retrieve the icon with the specified path from the specified bundle.
	 * 
	 * @param url
	 *            path to the icon
	 * 
	 * @return the icon image
	 */
	public static Image getIcon(URL url) {
		String key = url.toString();
		Image icon = iconCache.get(key);
		if (icon == null) {
			ImageData imageData = getImageData(url);
			if (imageData != null) {
				icon = new Image(Display.getDefault(), imageData);
				iconCache.put(key, icon);
			}
		}
		return icon;
	}
	
	/**
	 * Retrieve the icon with the specified path from the specified bundle.
	 * 
	 * @param bundle
	 *            bundle from which to retrieve the icon
	 * @param path
	 *            path to the icon
	 * 
	 * @return the icon image
	 */
	public static Image getIcon(Bundle bundle, String path) {
		String key = bundle.getSymbolicName() + ":" + path;
		Image icon = iconCache.get(key);
		if (icon == null) {
			URL url = FileLocator.find(bundle, new Path(path), null);
			if (url != null) {
				icon = getIcon(url);
				if (icon != null) {
					iconCache.put(key, icon);
				}
			}
		}
		return icon;
	}
	
	public static ImageData getImageData(Bundle bundle, String path) {
		return getImageData(FileLocator.find(bundle, new Path(path), null));
	}

	private static ImageData getImageData(URL url) {
		ImageData imageData = null;
		if (url != null) {
			try {
				imageData = new ImageData(url.openStream());
			} catch (IOException e) {
				trace.error("Error retrieving icon from "+url.toString()+": " + e);
			}
		}
		return imageData;
	}
	
	/**
	 * Dispose of all images in the icon cache and clear the contents of the
	 * icon cache. Should be called during shutdown of the containing plug-in.
	 */
	public static void disposeImagesInCache() {
		for (Image icon : iconCache.values())
			icon.dispose();
		iconCache.clear();
	}
	
}
