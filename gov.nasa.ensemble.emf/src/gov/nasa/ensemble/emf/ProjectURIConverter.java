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
/**
 * 
 */
package gov.nasa.ensemble.emf;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

public class ProjectURIConverter extends ExtensibleURIConverterImpl {
	
	public static final String SCHEME_PROJECT = "project";
	
	private final IProject project;
	
	public ProjectURIConverter(IProject project) {
		super();
		this.project = project;
	}
	
	public static URI createProjectURI(IFile file) {
		return createProjectURI(file.getProjectRelativePath().toString());
	}
	
	public static URI createProjectURI(String projectRelativePath) {
		if (projectRelativePath.charAt(0) == '/') {
			projectRelativePath = projectRelativePath.substring(1);
		}
		return URI.createURI(SCHEME_PROJECT+":/"+projectRelativePath);
	}
	
	@Override
	public URI normalize(URI uri) {
		String scheme = uri.scheme();
		if (SCHEME_PROJECT.equals(scheme)) {
			try {
				String projectName = project.getName();
				String pathString = getProjectRelativePathString(uri);
				String pathName = projectName+"/"+pathString;
				String decodedURI = URI.decode(pathName);
				return URI.createPlatformResourceURI(decodedURI, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.normalize(uri);
	}

	private String getProjectRelativePathString(URI uri) {
		String uriString = uri.toString();
		int index = uriString.indexOf(":");
		String pathString = uriString.substring(index+1);
		while (pathString.startsWith("/")) {
			pathString = pathString.substring(1);
		}
		return pathString;
	}
	
}
