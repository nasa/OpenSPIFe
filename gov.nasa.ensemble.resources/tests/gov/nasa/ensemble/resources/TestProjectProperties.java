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

import static fj.data.Option.*;
import static gov.nasa.ensemble.resources.ResourceUtil.*;
import static java.util.concurrent.TimeUnit.*;
import static junit.framework.Assert.*;
import static org.eclipse.core.resources.IResourceDelta.*;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fj.data.Option;
import gov.nasa.ensemble.common.logging.LogUtil;

public class TestProjectProperties {
	private final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	private final IProject proj = root.getProject("testProject");
	private final IFile file = proj.getFile("testFile");
	
	@Before
	public void before() throws CoreException {
		proj.create(null);
		proj.open(null);
		file.create(IOUtils.toInputStream(""), true, null);
	}
	
	@After
	public void after() throws CoreException {
		proj.delete(true, null);
	}
	
	@Test
	public void getSetUnset() {
		final String key = "key", val = "val";
		final ProjectProperties props = projProps(file);
		Option<String> prop = props.get(key);
		assertTrue(prop.isNone());
		props.set(key, val);
		prop = props.get(key);
		assertTrue(prop.isSome());
		assertEquals(val, prop.some());
		props.unset(key);
		prop = props.get(key);
		assertFalse(prop.isSome());
	}
	
	@Test
	public void idempotency() {
		final String key = "key", val = "val";
		final ProjectProperties props = projProps(file);
		props.set(key, val);
		
		final TestListener listener = new TestListener();
		props.listeners(key).add(listener);
		props.set(key, val);
		assertFalse("Should have ignored second set to same value", listener.changed);
	}
	
	@Test
	public void listen() {
		final String key1 = "key", key2 = "key2", val1 = "val1", val2 = "val2";
		final ProjectProperties props = projProps(file);
		final TestListener listener1 = new TestListener();
		final TestListener listener2 = new TestListener();
		props.listeners(key1).add(listener1);
		props.listeners(key2).add(listener2);
		props.set(key1, val1);
		assertTrue(listener1.changed);
		assertEquals(val1, listener1.lastVal.some());
		assertFalse(listener2.changed);
		
		// make sure listener removal is working
		props.listeners(key1).remove(listener1);
		props.set(key1, val2);
		assertEquals(val1, listener1.lastVal.some());
		
		final TestListener listener3 = new TestListener();
		props.listeners(key1).add(listener3);
		assertFalse(listener3.changed);
		props.unset(key1);
		assertTrue(listener3.changed);
		assertTrue(listener3.lastVal.isNone());
	}
	
	@Test
	public void move() throws CoreException, InterruptedException {
		final String key = "key", val1 = "val1", val2 = "val2";
		final TestListener listener = new TestListener();
		projProps(file).set(key, val1);
		
		while (!ProjectProperties.propFile(file, key).exists())
			Thread.sleep(50);
		
		projProps(file).addListener(listener);
		final IFile moved = proj.getFile("moved");
		file.move(moved.getFullPath(), true, null);
		
		file.create(IOUtils.toInputStream(""), true, null);
		assertTrue("After moving file, new file created in original's place still had old project properties", 
				projProps(file).get(key).isNone());
		assertFalse(listener.changed);
		assertTrue("Moved file did not retain its project properties", 
				projProps(moved).get(key).isSome());
		assertEquals(val1, projProps(moved).get(key).some());
		projProps(moved).set(key, val2);
		assertFalse(listener.changed);
		assertTrue(projProps(file).get(key).isNone());
	}
	
	@Test
	public void copy() throws CoreException, InterruptedException {
		final String key = "key", val1 = "val1", val2 = "val2";
		final TestListener listener = new TestListener();
		final ProjectProperties props = projProps(file);
		props.set(key, val1);
		
		while (!ProjectProperties.propFile(file, key).exists())
			Thread.sleep(50);
		
		props.addListener(listener);
		final IFile copied = proj.getFile("moved");
		file.copy(copied.getFullPath(), true, null);
		assertFalse(listener.changed);
		file.delete(true, null);
		assertTrue("Copied file lost project properties", projProps(copied).get(key).isSome());
		assertEquals(val1, projProps(copied).get(key).some());
		projProps(copied).set(key, val2);
		assertFalse(listener.changed);
	}
	
	@Test
	public void deleteResourceDeletesProperties() throws CoreException, InterruptedException {
		final String key = "key", val = "val";
		projProps(file).set(key, val);
		file.delete(true, null);
		file.create(IOUtils.toInputStream(""), true, null);
		assertTrue("File retained its project properties across deletion and recreation", 
				projProps(file).get(key).isNone());
		Thread.sleep(1000);
		assertFalse(ProjectProperties.propsFolder(file).exists());
	}
	
	@Test
	public void sessionProperties() throws CoreException {
		final QualifiedName key = new QualifiedName(EnsembleResourcesPlugin.PLUGIN_ID, "testymctest");
		final String val = "moo";
		file.setPersistentProperty(key, val);
		final IFile dest = proj.getFile("dest");
		file.copy(dest.getFullPath(), true, null);
		assertEquals(val, dest.getPersistentProperty(key));
	}
	
	@Test
	public void reading() throws CoreException {
		final String key = "key", val1 = "val1", val2 = "val2";
		final IFile propFile = ProjectProperties.propFile(file, key);
		final ProjectProperties props = projProps(file);
		assertTrue(props.get(key).isNone());
		assertFalse(propFile.exists());
		mkdirs(propFile);
		
		// create
		propFile.create(IOUtils.toInputStream(val1), true, null);
		assertTrue("Created property file, property does not exist in memory", props.get(key).isSome());
		assertEquals(val1, props.get(key).some());
		
		// update
		propFile.setContents(IOUtils.toInputStream(val2), true, true, null);
		assertTrue("Modified property file, value was not mirrored in memory", props.get(key).isSome());
		assertEquals(val2, props.get(key).some());
		
		// delete
		propFile.delete(true, null);
		assertTrue("Deleted a property file, property remains in memory", props.get(key).isNone());
	}
	
	@Test
	public void writing() throws CoreException, IOException, InterruptedException {
		final String key = "key", val1 = "val1", val2 = "val2";
		final IFile propFile = ProjectProperties.propFile(file, key);
		final ProjectProperties props = projProps(file);
		assertTrue(props.get(key).isNone());
		assertFalse(propFile.exists());
		
		final BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				if (event.getDelta() != null) {
					try {
						event.getDelta().accept(new IResourceDeltaVisitor() {
							@Override
							public boolean visit(IResourceDelta delta) {
								if (propFile.equals(delta.getResource()))
									try {
										queue.put(delta.getKind());
									} catch (InterruptedException e) {
										LogUtil.error(e);
									}
								return true;
							}
						});
					} catch (CoreException e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
		final long timeout = 1000;
		
		// create
		props.set(key, val1);
		assertEquals(ADDED, (int)queue.poll(timeout, MILLISECONDS));
		assertTrue("Property file does not exist after setting property in memory", 
				propFile.exists());
		assertEquals(val1, getStringContents(propFile));
		
		// update
		props.set(key, val2);
		assertEquals(CHANGED, (int)queue.poll(timeout, MILLISECONDS));
		assertEquals("Property file does not exist after updating property in memory", 
				val2, getStringContents(propFile));
		
		// delete
		props.unset(key);
		assertEquals(REMOVED, (int)queue.poll(timeout, MILLISECONDS));
		assertFalse("Property file still exists after unsetting property in memory", 
				propFile.exists());
	}
	
	@Test
	public void preexisting() throws CoreException {
		final IFile fooFile = proj.getFile("fooFile");
		final String key = "key", val = "val";
		final IFile propFile = ProjectProperties.propFile(fooFile, key);
		mkdirs(propFile);
		propFile.create(IOUtils.toInputStream(val), true, null);
		fooFile.create(IOUtils.toInputStream(""), true, null);
		final Option<String> prop = projProps(fooFile).get(key);
		assertTrue(prop.isSome());
		assertEquals(val, prop.some());
	}
	
	private static class TestListener implements IProjectPropertyListener {
		boolean changed = false;
		Option<String> lastVal = none();
		BlockingQueue<ProjectPropertyEvent> queue = new LinkedBlockingQueue<ProjectPropertyEvent>();
		
		@Override
		public void propertyChanged(final ProjectPropertyEvent event) {
			changed = true;
			lastVal = event.value;
			queue.add(event);
		}
	}
}
