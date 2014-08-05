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
package gov.nasa.ensemble.emf.stringifier;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.type.AbstractTrimmingStringifier;

import java.net.URL;
import java.text.ParseException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

public class URIStrigifier extends AbstractTrimmingStringifier<URI> {

	private URIConverter uriConverter;

	public URIStrigifier() {
		this(null);
	}

	public URIStrigifier(URIConverter uriConverter) {
		this.uriConverter = uriConverter;
	}

	@Override
	public String getDisplayString(URI javaObject) {
		if (javaObject == null || uriConverter == null) {
			return "";
		}
		URI normalize = uriConverter.normalize(javaObject);
		String normalizedPath = normalize.toString();
		if (isWorkspaceRelativePath(normalizedPath)) {
			IPath path = new Path(normalizedPath);
			if (path.segmentCount() > 3) {
				path = path.removeFirstSegments(3);
			} else {
				// platform uri is invalid format
				return "";
			}
			String projectRelativePath = path.toOSString();
			return "project:/" + projectRelativePath;
		}
		return normalizedPath;
	}

	@Override
	protected URI getJavaObjectFromTrimmed(String string, URI defaultObject) throws ParseException {
		if (CommonUtils.isNullOrEmpty(string)) {
			return null;
		}
		try {
			URI uri = null;
			if (!isProjectRelativePath(string)) {
				URL url = new URL(string);
				uri = URI.createURI(url.toString());
			} else {
				uri = URI.createURI(string);
			}
			if (uriConverter == null) {
				return uri;
			}
			URI normalize = uriConverter.normalize(uri);
			return normalize;
		} catch (Exception e) {
			throw new ParseException(string, 0);
		}

	}

	public void setUriConverter(URIConverter uriConverter) {
		this.uriConverter = uriConverter;
	}

	private boolean isProjectRelativePath(String path) {
		return path.trim().startsWith("project:/");
	}

	private boolean isWorkspaceRelativePath(String path) {
		return path.trim().startsWith("platform:/");
	}
}
