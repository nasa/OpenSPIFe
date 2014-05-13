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
package gov.nasa.ensemble.ui.operations;

import gov.nasa.ensemble.common.ui.operations.EnsembleFileSystemExportOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public class TestEnsembleFileSystemExportOperation extends TestCase {
	private IProject project0 = null;
	private IProject project1 = null;
	private List<? extends IResource> resources = null;
	private File destination = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		project0 = root.getProject("project0");
		assertFalse(project0.exists());
		project0.create(null);
		assertTrue(project0.exists());
	
		project1 = root.getProject("project1");
		assertFalse(project1.exists());
		project1.create(null);
		assertTrue(project1.exists());
		
		resources = new ArrayList<IResource>();
		((ArrayList<IResource>)resources).add(project0);
		((ArrayList<IResource>)resources).add(project1);
		
		destination = new File("destination");
		destination.delete();
		assertFalse(destination.exists());
		destination.mkdir();
		assertTrue(destination.exists());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		project0.delete(true, null);
		project1.delete(true, null);
		assertFalse(project0.exists());
		assertFalse(project1.exists());
		destination.delete();
		assertFalse(destination.exists());
	}
	
	public void testEnsembleFileSystemExportOperation() throws InterruptedException {
		 EnsembleFileSystemExportOperation ensembleFileSystemExportOperation
		 	= new EnsembleFileSystemExportOperation(null, resources, destination.getAbsolutePath(), null);		

		ensembleFileSystemExportOperation.run(null);

	}

}
