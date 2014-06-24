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
package gov.nasa.ensemble.common.io;

import gov.nasa.ensemble.common.io.RemotableFile.REMOTE_FILE_MODES;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class TestRemotableFile extends TestCase {

	private String remoteRootStr = "/oss/";
	private File localRoot;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		final long startTime = System.currentTimeMillis();
		final String localRootPath = System.getProperty("java.io.tmpdir") + File.separator + "local_" + startTime;
		
		localRoot = new File(localRootPath);
		localRoot.mkdirs();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		localRoot.delete();
	}
	
	public void testNullRelativePath() {
		try {
			new DummyRemotableFile(null);
			fail();
		} catch (IllegalArgumentException e) {
			// expecting exception
		}
	}
	
	public void testDownload() {
//		RemotableFile rf = new DummyRemotableFile("mera/ops/ops/surface/tactical/sol/003/opgs/edr/ncam/2N126632591ESF0200P1520L0M1.JPG");
//		File file = rf.getFile();
//		assertNotNull(file);
//		assertTrue(file.exists());
//		assertTrue(file.canRead());
//		assertTrue(file.length() > 0);
//		assertTrue(file.lastModified() <= System.currentTimeMillis());
	}
	
	public void testLocalOnly() throws IOException {
		File file = new File(localRoot + File.separator + "localFile");
		file.createNewFile();
		RemotableFile remotableFile = new DummyRemotableFile(file.getName());
		File file2 = remotableFile.getFileByMode(REMOTE_FILE_MODES.LOCAL_ONLY);
		assertEquals(file.getAbsolutePath(), file2.getAbsolutePath());
		file.delete();
	}
	
	@SuppressWarnings("serial")
	private class DummyRemotableFile extends RemotableFile {
		private final String hostURL = "msl-apss";
		
		public DummyRemotableFile(String relativeFilePath) {
			super(relativeFilePath);
		}
		
		@Override
		protected String getRemoteHost() {
			return hostURL;
		}
		
		@Override
		protected String getRemoteRootDirectory() { return remoteRootStr; }
		
		@Override
		protected String getLocalRootPath()       { return localRoot.getAbsolutePath(); }
		
		@Override
		protected void cacheAccessed()       { /* ignore */ }

		@Override
		protected void remoteFileNotFound()  { /* ignore */ }
		
		@Override
		protected void remoteFileRetrieved() { /* ignore */ }
	}
}
