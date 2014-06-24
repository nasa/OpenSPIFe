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
package gov.nasa.ensemble.common.extension;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class ExtensionUtils {
	
	private static final Logger trace = Logger.getLogger(ExtensionUtils.class);
	
	/**
	 * Given an element and the resource attribute, a stream is returned from within
	 * that plugin that corresponds to the attribute value. Otherwise an exception
	 * is thrown.
	 */
	public static InputStream getInputStream(IConfigurationElement element, String attribute) throws IOException {
		Bundle bundle = Platform.getBundle(element.getContributor().getName());
		if (bundle == null) {
			throw new IOException("No plugin found for ID '"+element.getNamespaceIdentifier()+"'");
		}
		String attributeValueString = element.getAttribute(attribute);
		return FileLocator.openStream(bundle, new Path(attributeValueString), false);
	}

	public static Class getClass(IConfigurationElement lookupElement, String className) {
		Bundle bundle = Platform.getBundle(lookupElement.getContributor().getName());
		try {
			return bundle.loadClass(className);
		} catch (InvalidRegistryObjectException e) {
			trace.error(e.getMessage()+": "+className, e);
		} catch (ClassNotFoundException e) {
			trace.error(e.getMessage()+": "+className, e);
		}
		return null;
	}
	
}
