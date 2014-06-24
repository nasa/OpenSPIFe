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
package gov.nasa.ensemble.common.data.test;

import gov.nasa.ensemble.common.id.UniqueIdGenerator;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class TestSharedTestDataForBambooEnvironment extends TestCase {
	
	public void testLockDirectory() throws IOException {
		TestableSharedTestData it = new TestableSharedTestData();
		File directory = new File("/tmp/", getClass().getSimpleName() + "-" + UniqueIdGenerator.generateUniqueIdNumber());
		it.createDirectory(directory);
		directory.deleteOnExit();
		assertFalse(it.isDirectoryLocked(directory));
		boolean canLockFirstTime = it.lockDirectory(directory);
		assertTrue(canLockFirstTime);
		boolean canLockSecondTime = it.lockDirectory(directory);
		assertFalse(canLockSecondTime);
		assertTrue(it.isDirectoryLocked(directory));
		it.unlockDirectory(directory);
		assertFalse(it.isDirectoryLocked(directory));
	}
		
	private class TestableSharedTestData extends SharedTestDataForBambooEnvironment {

		public TestableSharedTestData() {
			super("gov.nasa.ensemble.common.data.test");
		}

		@Override
		protected void reportMetrics() {
			// Silent
		}
		
	}

}
