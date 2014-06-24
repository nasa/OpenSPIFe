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

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class PrunedFileSearch {
	
	private boolean stopRequested;

	public File findLastFileRecursively(String root, Pattern subdirPattern, String nameBeginsWith, String nameEndsWith)
	{
		stopRequested = false;
		return recurse(new File (root), subdirPattern, new MyFilter(nameBeginsWith, nameEndsWith));
	}
	
	public void stop () {
		stopRequested = true;
	}
	
	private File recurse (File dir, Pattern subdirPattern, MyFilter filter) {
		if (stopRequested) return null;
		File[] thisLevelAsArray = dir.listFiles();
		if (thisLevelAsArray == null) return null;
		List<File> thisLevel = Arrays.asList();
		if (stopRequested) return null;
        Collections.sort(thisLevel, Collections.reverseOrder()); // try for latest sol and version
		for (File file : thisLevel) { // check at this level before doing more work
			if (stopRequested) return null;
			if (!file.isDirectory() && filter.accept(dir, file.getName())) return file;
			}
		for (File file : thisLevel)
			if (file.isDirectory() && subdirPattern.matcher(file.toString()).matches()) {
				if (stopRequested) return null;
				File found = recurse(file, subdirPattern, filter);
				if (found != null) return found;
			}
		return null;
	}
	
	private class MyFilter implements FilenameFilter {
		String start;
		String end;

		MyFilter(String start, String end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.startsWith(this.start) && name.endsWith(this.end);
		}
	}

}
