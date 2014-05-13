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
package gov.nasa.ensemble.common;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.io.DirectoryWalker;

/**
 * Utility made for streaming files into a queue as they are discovered on the filesystem.
 * 
 * @author mpowell
 * 
 */
public class EnsembleDirectoryWalker extends DirectoryWalker {

	private final File directory;
	private final FileFilter filter;

	public EnsembleDirectoryWalker(File directory) {
		this(directory, new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return true;
			}
		});
	}

	public EnsembleDirectoryWalker(String filePath, FileFilter filter) {
		this(new File(filePath), filter);
	}

	public EnsembleDirectoryWalker(File directory, FileFilter filter) {
		this.directory = directory;
		this.filter = filter;
	}

	public void start(Queue<String> foundFiles) throws IOException {
		walk(directory, foundFiles);
	}

	@Override
	protected void handleFile(File file, int depth, Collection results) {
		if (filter.accept(file))
			if (results instanceof BlockingQueue) {
				try {
					((BlockingQueue) results).put(file.getAbsolutePath());
				} catch (InterruptedException e) {
					LogUtil.error(e);
				}
			} else {
				results.add(file.getAbsolutePath());
			}
	}
}
