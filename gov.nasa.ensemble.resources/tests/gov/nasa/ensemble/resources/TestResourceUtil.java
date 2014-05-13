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
package gov.nasa.ensemble.resources;

import static gov.nasa.ensemble.resources.ResourceUtil.*;
import static org.junit.Assert.*;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestResourceUtil {
	final IWorkspaceRoot wsRoot = ResourcesPlugin.getWorkspace().getRoot();
	private IProject proj1;
	private IProject proj2;
	
	@Before
	public void before() throws CoreException {
		proj1 = wsRoot.getProject("proj1");
		proj1.create(null);
		proj1.open(null);
		
		proj2 = wsRoot.getProject("proj2");
		proj2.create(null);
		proj2.open(null);
	}
	
	@After
	public void after() throws CoreException {
		proj1.delete(true, null);
		proj2.delete(true, null);
	}
	
	@Test
	public void relativePath() {
		final IProject proj1 = wsRoot.getProject("proj1");
		final IProject proj2 = wsRoot.getProject("proj2");
		final IFolder folder = proj1.getFolder("folder");
		final IFolder file = folder.getFolder("file");
		assertTrue(getRelativePath(proj1, proj2).isNone());
		assertTrue(getRelativePath(wsRoot, proj1).isSome());
		assertTrue(getRelativePath(wsRoot, proj1).some().equals(Path.EMPTY.append(proj1.getName())));
		assertTrue(getRelativePath(proj1, file).isSome());
		assertTrue(getRelativePath(proj1, file).some().equals(file.getFullPath().removeFirstSegments(1)));
		assertTrue(getRelativePath(proj2, file).isNone());
	}
	
	/**
	 * Verifies fix for MAE-5107
	 */
	@Test
	public void addBuilderIsIdempotent() throws CoreException {
		assertEquals(0, proj1.getDescription().getBuildSpec().length);
		final String builder1 = "builder1";
		final String builder2 = "builder2";
		addBuilders(proj1, new String[]{builder1});
		assertEquals(1, proj1.getDescription().getBuildSpec().length);
		addBuilders(proj1, new String[]{builder1, builder2});
		assertEquals(2, proj1.getDescription().getBuildSpec().length);
		assertEquals(builder1, proj1.getDescription().getBuildSpec()[0].getBuilderName());
		assertEquals(builder2, proj1.getDescription().getBuildSpec()[1].getBuilderName());
		addBuilders(proj1, new String[]{builder1});
		assertEquals(2, proj1.getDescription().getBuildSpec().length);
		assertEquals(builder1, proj1.getDescription().getBuildSpec()[0].getBuilderName());
		assertEquals(builder2, proj1.getDescription().getBuildSpec()[1].getBuilderName());
		addBuilders(proj1, new String[]{builder2});
		assertEquals(2, proj1.getDescription().getBuildSpec().length);
		assertEquals(builder1, proj1.getDescription().getBuildSpec()[0].getBuilderName());
		assertEquals(builder2, proj1.getDescription().getBuildSpec()[1].getBuilderName());
		addBuilders(proj1, new String[]{builder1, builder2});
		assertEquals(2, proj1.getDescription().getBuildSpec().length);
		assertEquals(builder1, proj1.getDescription().getBuildSpec()[0].getBuilderName());
		assertEquals(builder2, proj1.getDescription().getBuildSpec()[1].getBuilderName());
	}
	
	@Test
	public void addRemoveNature() throws CoreException {
		assertEquals(0, proj1.getDescription().getNatureIds().length);
		final String natureId = EnsembleResourcesPlugin.getTestNatureId();
		addNature(proj1, natureId, null);
		assertEquals(1, proj1.getDescription().getNatureIds().length);
		addNature(proj1, natureId, null);
		assertEquals("addNature is not idempotent", 1, proj1.getDescription().getNatureIds().length);
		removeNature(proj1, natureId, null);
		assertEquals(0, proj1.getDescription().getNatureIds().length);
	}
	
	/**
	 * Tests fix for MAE-5218
	 */
	@Test
	public void removingNatureDoesNotRemoveUnrelatedBuilders() throws Exception {
		final String testNature = EnsembleResourcesPlugin.getTestNatureId();
		final String testBuilder = EnsembleResourcesPlugin.getTestBuilderId();
		addNature(proj1, testNature, null);
		addBuilders(proj1, testBuilder);
		assertTrue(hasBuilder(proj1, testBuilder));
		removeNature(proj1, testNature, null);
		assertTrue(hasBuilder(proj1, testBuilder));
	}
}
