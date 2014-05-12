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
package gov.nasa.ensemble.emf;

import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.junit.Assert;
import org.junit.Test;

public class TestProjectURIConverter extends Assert {

	@Test
	public void testCreateProjectURI() {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("test");
		IFile file0 = project.getFile("file.text");
		URI uri0 = ProjectURIConverter.createProjectURI(file0);
		ProjectURIConverter converter = new ProjectURIConverter(project);
		URI uri1 = converter.normalize(uri0);
		IFile file1 = EMFUtils.getFile(uri1);
		assertEquals(file0, file1);
	}
	
}
