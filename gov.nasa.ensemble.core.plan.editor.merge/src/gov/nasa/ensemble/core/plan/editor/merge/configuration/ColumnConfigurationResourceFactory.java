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
package gov.nasa.ensemble.core.plan.editor.merge.configuration;

import fj.data.Option;
import gov.nasa.ensemble.emf.EnsembleResourceFactory;

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

public class ColumnConfigurationResourceFactory extends EnsembleResourceFactory {

	public static final String CONTENT_TYPE = "text/columns";
	private static final String[] VALID_FILE_EXTENSIONS = new String[] { "days", "table", "tasklist", "oos" };

	@Override
	public Option<? extends Resource> createResource(URI uri, String contentType) {
		if (CONTENT_TYPE.equals(contentType)) {
			return Option.some(new ColumnConfigurationResource(uri));
		}
		if (hasValidExtension(uri)) {
			ColumnConfigurationResource resource = new ColumnConfigurationResource(uri);
			try {
				resource.load(getResourceSet().getLoadOptions());
			} catch (IOException e) {
				//silent -- just return resource with default configuration
			}
			return Option.some(resource);
		}
		return Option.none();
	}
	
	private static boolean hasValidExtension(URI uri) {
		if (uri != null) {
			String extension = uri.fileExtension();
			if (extension != null) {
				for (String ext : VALID_FILE_EXTENSIONS) {
					if (extension.equals(ext)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
