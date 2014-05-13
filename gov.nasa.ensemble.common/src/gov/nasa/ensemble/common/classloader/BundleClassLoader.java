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
package gov.nasa.ensemble.common.classloader;

import org.osgi.framework.Bundle;

/**
 * Classloader used to help Castor load classes from other Eclipse plugins
 */
public class BundleClassLoader extends ClassLoader {

	private Bundle bundle;
	private ClassLoader loader;

	public BundleClassLoader(Bundle bundle, ClassLoader loader) {
		this.bundle = bundle;
		this.loader = loader;
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

		Class<?> clazz = null;
		try {
			// first try the context class loader
			clazz = loader.loadClass(name);
		} catch (ClassNotFoundException e) {
			// if context class loader fails, then use the bundle for
			// loading the class
			clazz = bundle.loadClass(name);
		}

		if (resolve)
			resolveClass(clazz);

		return clazz;
	}
	
}
