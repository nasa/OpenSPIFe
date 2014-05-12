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
package gov.nasa.ensemble.common.properties;

import static org.eclipse.core.runtime.FileLocator.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public abstract class EnsemblePropertiesProvider {

	private static final Path ENSEMBLE_PROPERTIES_FILENAME = new Path("ensemble.properties");
	public static final String ENSEMBLE_PROPERTIES_FILE_PROPERTY = "ensemble.properties.file";
	
	public abstract String getPropertiesFileLocation();
	
	public static class ProductPropertiesProvider extends EnsemblePropertiesProvider {

		@Override
		public String getPropertiesFileLocation() {
			return null;
		}

		public InputStream getPropertiesInputStream() throws IOException {
			Bundle bundle = getProductBundle();
			if (bundle != null) {
				return FileLocator.openStream(bundle, ENSEMBLE_PROPERTIES_FILENAME, false);
			}
			return null;
		}
		
		private Bundle getProductBundle() {
			IProduct product = Platform.getProduct();
			if (product != null) {
				return product.getDefiningBundle();
			}
			return null;
		}

	}
	
	public static Properties loadProperties(final Bundle bundle, final String path) throws IOException {
		final Properties properties = new Properties();
		final InputStream is = openStream(bundle, Path.fromOSString(path), false);
		try {
			properties.load(is);
		} finally {
			is.close();
		}
		return properties;
	}
	
}
