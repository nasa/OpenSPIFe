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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Plugin;
import org.junit.Assert;
import org.osgi.framework.Bundle;

/**
 * @see TestDataManipulator.getDescendantsOf()
 * @see TestDataManipulator.assertSameContent()
 */
public class TestDataManipulator {
	
	private static final String HINT = "Output does not match baseline.\nFor hints on how to fix, see https://ensemble.jpl.nasa.gov/confluence/x/VYrFAw";
	private Charset charSet = Charset.forName("UTF-8");
	
	public interface ILineMatcher {
		/**
		 * Allows custom match strategies, e.g. ignoring certain lines or looking at them more leniently.
		 * @param line1 content of line from one file, guaranteed not to be null
		 * @param line2 content of line from other file, guaranteed not to be null
		 * @return something like line1.equals(line2) in most cases
		 */
		public boolean linesMatch(String line1, String line2);

		/**
		 * This allows excluding a section of each file in the comparison.
		 * If the current line in either file is opening a section we don't want to compare,
		 * as defined by this method, we can skip to the end of the section in both files.
		 * @param string --The line (trimmed of whitespace) the comparison has just encountered (often an opening tag, e.g. "&lt;Foo&gt;") 
		 * @return null in the normal case where that line doesn't require skipping; otherwise the line that indicates we should continue comparing (often the closing tag, e.g. "&lt;/Foo&gt;" )
		 */
		public String skipSectionStartingWith(String string);

		public boolean ignoreBlankLines();

	}
	
	public interface ILineMatcher2 extends ILineMatcher {
		/** When ignoreBlankLines() needs to be position-sensitive. */
		public boolean ignoreBlankLine(int lineNo);
	}

	private Bundle bundle;

	public TestDataManipulator(Plugin plugin) {
		this(plugin.getBundle());
	}
	
	public TestDataManipulator(Bundle bundle) {
		this.bundle = bundle;
	}
	
	/**
	 * Intended for tests that want to map over a checked-in folder of data files
	 * without hard-coding the names of the files in the folder, so the test
	 * can be more data-driven.  The usual API for files unfortunately doesn't work
	 * for checked-in data that wind up bundled in to a single file. 
	 * <code>
	 * 
	 * TestDataUtil lister = new TestDataUtil(Activator.getDefault());
	 * Pattern TEXT_FILES = Pattern.compile(".+\\.txt$");
	 * getDescendantsOf("data-test/inputs/", TEXT_FILES)
	 * </code>
	 */
	public List<URL> getDescendantsOf (String pathname, Pattern matching) {
		List<URL> result = new ArrayList<URL>();
		if (matching.matcher(pathname).matches()) {
			result.add(bundle.getEntry(pathname));
		}
		List<URL> children = getChildrenOf(pathname);
		if (children != null) {
			for (URL child : children) {
				result.addAll(getDescendantsOf(child.getPath(), matching));
			}		
		}
		return result;
	}
	
	public List<URL> getChildrenOf (String directoryPathname) {
		List<URL> result = new ArrayList<URL>();
		Enumeration<?> entryPaths = bundle.getEntryPaths(directoryPathname);
		if (entryPaths==null) return null;
		while (entryPaths.hasMoreElements()) {
			String pathname = (String) entryPaths.nextElement();
			if (!getFilenamePart(pathname).startsWith(".")) {
				result.add(bundle.getEntry(pathname));
			}
		}
		return result;
	}
	public String getFilenamePart (String filePathname) {
		if (filePathname.endsWith("/")) {
			filePathname = filePathname.substring(0, filePathname.length()-1);
		}
		int lastSlash = filePathname.lastIndexOf('/');
		if (lastSlash < 0) return filePathname;
		return filePathname.substring(lastSlash+1);
	}

	public List<String> getFilenameParts (List<String> filePathnames) {
		List<String> result= new ArrayList<String>();
		for (String path : filePathnames) {
			result.add(getFilenamePart(path));
		}
		return result;
	}
	
	public void assertSameContent(URL expectedFile, File actualFile) throws IOException {
		assertSameContent(expectedFile, actualFile, new ILineMatcher2() {
			@Override
			public boolean linesMatch(String line1, String line2) {
				return line1==line2 || (line1 != null && line2 != null && line1.equals(line2));
			}

			@Override
			public String skipSectionStartingWith(String string) {
				// Default:  Don't skip anything.
				return null;
			}

			@Override
			public boolean ignoreBlankLines() {
				return false;
			}

			@Override
			public boolean ignoreBlankLine(int lineNo) {
				return false;
			}
		});
	}

	public void assertSameContent(URL expectedFile, File actualFile, ILineMatcher matcher) throws IOException {
		Assert.assertNotNull("Actual file does not exist.", actualFile);
		Assert.assertNotNull("Expected file does not exist.", expectedFile);
		BufferedReader streamE = new BufferedReader(new InputStreamReader(expectedFile.openStream(), charSet));
		BufferedReader streamA = new BufferedReader(new InputStreamReader(new FileInputStream(actualFile), charSet));
		String expectedLine;
		String actualLine;
		int lineNoInActual = 0;
		int lineNoInExpected = 0;
		boolean ignoreBlankLines = matcher.ignoreBlankLines();
		do {
			do {
				lineNoInExpected++;
				expectedLine = streamE.readLine();
				if (matcher instanceof ILineMatcher2) {	
					ignoreBlankLines = ((ILineMatcher2) matcher).ignoreBlankLine(lineNoInActual);
				}
			} while (expectedLine != null && ignoreBlankLines && expectedLine.isEmpty());
			do {
				lineNoInActual++;
				actualLine = streamA.readLine();
			} while (actualLine != null && ignoreBlankLines && actualLine.isEmpty());
			if (expectedLine != null && actualLine==null) {
				if (lineNoInActual > 1) {
					Assert.fail(actualFile + " is truncated.");
				} else {
					Assert.fail(actualFile + " is empty.");
				}
			} else if (expectedLine == null && actualLine != null) {
				Assert.fail(actualFile + " continues past the point where expected output ends.");
			} else if (expectedLine==null && actualLine==null) {
				// successful case, when end of both files is reached simultaneously.
			} else if (expectedLine==null || actualLine==null) {
				Assert.fail("Logical error in test.");
				// Java 1.6 compiler's null-warning logic is not quite clever enough
				// to realize that this clause can't ever be reached.
			} else {
				// If either line is opening a section we don't want to compare, skip to the end tag in both files.
				String tagToSkip1To = matcher.skipSectionStartingWith(expectedLine.trim());
				String tagToSkip2To = matcher.skipSectionStartingWith(actualLine.trim());
				if (tagToSkip1To != null) {
					lineNoInExpected += skipTo(tagToSkip1To, streamE);
				}
				if (tagToSkip2To != null) {
					lineNoInActual += skipTo(tagToSkip2To, streamA);
				}
				if (tagToSkip1To==null && tagToSkip2To==null) {
					if (!matcher.linesMatch(expectedLine, actualLine)) {
						Assert.assertEquals(
								HINT + '\n' +
								"In " + actualFile.getParentFile().getName() + File.separatorChar + actualFile.getName()
								+ "\nLine "
								+ lineNoInExpected + " in expected file "
								+ " does not match "
								+ lineNoInActual + " in actual file: ",
								expectedLine, actualLine);
					}
				}
			}
		} while (expectedLine != null && actualLine != null);
	}

	private int skipTo(String endingString, BufferedReader stream) throws IOException {
		String line;
		int linesSkipped = 0;
		do {
			line = stream.readLine();
			linesSkipped++;
			if (line==null) throw new IOException("Reached end of file while looking for " + endingString);
			}
		while (line != null && !line.trim().equals(endingString));
		return linesSkipped;
	}

	public Charset getCharSet() {
		return charSet;
	}

	public void setCharSet(Charset charSet) {
		this.charSet = charSet;
	}
	
}
