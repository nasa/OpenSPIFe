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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.io.FileUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.junit.Assert;
import org.osgi.framework.Bundle;


public abstract class TestUtil {
	
	private static Boolean isBamboo = null;

	/**
	 * Finds a URL for test data checked into a local data-test directory.
	 * Should work if Run As a JUnit Test or JUnit Plugin Test,
	 * as well as Bamboo.  (For Bamboo, remember to check the box for the data-test folder
	 * in the plugin's build.xml editor)
	 * @param yourPlugin -- use something like Activator.getDefault()
	 * @param filePath -- e.g. "data-test/foo.xml" (use slash URL separator, not platform-specific File.separator).
	 * @return URL (hint: call openStream)
	 * @throws FileNotFoundException
	 * @see SharedTestData for large data files you don't want checked out and delivered with the product
	 */
	public static URL findTestData(Plugin yourPlugin, String filePath) throws FileNotFoundException {
		URL url;
		try {
			if (yourPlugin==null) {
				url = new File(filePath).toURI().toURL();
			} else {
				url = FileLocator.find(yourPlugin.getBundle(),
						new Path(filePath),
						null);
			}
		} catch (IOException e) {
			throw(new FileNotFoundException(filePath + " (" + e.toString() + ")"));
		}
		if (url==null) {
			throw new FileNotFoundException("Can't find URL " + filePath);
		}
		return url;
	}
	
	/**
	 * Finds a test file.  Will work with when run as a JUnit Test, as a JUnit Plugin Test, or by Bamboo.
	 * @param yourPlugin -- use something like Activator.getDefault()
	 * @param filePath -- e.g. "data-test/foo.xml" (use slash URL separator, not platform-specific File.separator).
	 * @return File object
	 * @throws FileNotFoundException
	 * @see SharedTestData SharedTestData, for large data files you don't want checked out and delivered with the product
	 */
	public static File findTestFile(Plugin yourPlugin, String filePath) throws FileNotFoundException {
		URL url = findTestData(yourPlugin, filePath);
		try {
			if (yourPlugin==null) return new File(url.toURI()); // when "Run As JUnit Test"
			return new File(FileLocator.toFileURL(url).toURI());
		} catch (URISyntaxException e) {
			throw(new FileNotFoundException(filePath + " (" + e.toString() + ")"));
		} catch (FileNotFoundException e) {
			throw(e);
		} catch (IOException e) {
			throw(new FileNotFoundException(filePath + " (" + e.toString() + ")"));
		}
	}
		
	/**
	 * Finds the one and only test file with a given name pattern.  Will only work with when run as a JUnit Plugin Test, or by Bamboo.
	 * @param yourPlugin -- use something like Activator.getDefault()
	 * @param filePath -- usually "data-test/" (use slash URL separator, not platform-specific File.separator).
	 * @param filePattern -- e.g. "*.txt"
	 * @return URL
	 * @throws IOException on any I/O exception or if there are zero or >1 files matching
	 */
	public static URL findTestFileURLMatching(Plugin yourPlugin, String filePath, String filePattern) throws IOException {
		Enumeration<URL> enumeration = yourPlugin.getBundle().findEntries(filePath, filePattern, true);
		if (enumeration==null || !enumeration.hasMoreElements()) throw new FileNotFoundException("No file matches " + filePattern);
		URL url = enumeration.nextElement();
		if (enumeration.hasMoreElements()) throw new IOException("More than one file matches " + filePattern);
		return url;
	}
	
	public static File createTestOutputFile(String folderForActualFilesIfDebugging, String filename,
			String suffix) throws IOException {
		// This should work when running manually.
		// It creates files permanently (but they should be deleted manually, not checked in)
		// so they can be validated and compared manually and dragged to excepted-outputs if valid.
		Assert.assertNotNull("Null filename", filename);
		File file = new File(folderForActualFilesIfDebugging + File.separator + filename + suffix);
		try {
			file.delete(); // delete old one if it exists
			file.getParentFile().mkdirs();
			if (file.createNewFile()) {
				return file;
			}
		} catch (IOException e) {
			// Fall through for Bamboo
		}
		// On Bamboo, where the above doesn't work, we want to create a temp file
		// that is deleted on exit.
		return FileUtilities.createTempFile(filename, suffix);
	}

	/**
	 * @param yourPlugin -- use something like Activator.getDefault()
	 * @param directoryPath -- e.g. "data-test/" (use slash URL separator, not platform-specific File.separator).
	 * @return Listing of directory as a List of URLs.
	 * @throws FileNotFoundException
	 * @see SharedTestData for large data files you don't want checked out and delivered with the product
	 */
	public static List<URL> listURLContents(Plugin yourPlugin, String directoryPath) throws FileNotFoundException {
		List<URL> toRet = new ArrayList<URL>();
		if (yourPlugin == null) {
			File dirFile = new File(directoryPath);
			for (File child : dirFile.listFiles()) {
				try {
					toRet.add(child.toURI().toURL());
				} catch (MalformedURLException e) {
					throw(new FileNotFoundException(directoryPath + " (" + e.toString() + ")"));
				}
			}
		} else {
			Bundle bundle = yourPlugin.getBundle();
			Enumeration entryPaths = bundle.getEntryPaths(directoryPath);
			while (entryPaths.hasMoreElements()) {
				String entryPath = (String) entryPaths.nextElement();
				toRet.add(bundle.getEntry(entryPath));
			}
		}
		return toRet;
	}
	
	/**
	 * @param yourPlugin
	 *            -- use something like Activator.getDefault()
	 * @param directoryPath
	 *            -- e.g. "data-test/" (use slash URL separator, not
	 *            platform-specific File.separator).
	 * @return Listing of directory as a List of Files.
	 * @throws FileNotFoundException
	 * @see SharedTestData for large data files you don't want checked out and
	 *      delivered with the product
	 */
	public static List<File> listFileContents(Plugin yourPlugin,String directoryPath) throws FileNotFoundException {
		List<File> toRet = new ArrayList<File>();
		if (yourPlugin == null) {
			File dirFile = new File(directoryPath);
			toRet = Arrays.asList(dirFile.listFiles());
		} else {
			Bundle bundle = yourPlugin.getBundle();
			Enumeration entryPaths = bundle.getEntryPaths(directoryPath);
			while (entryPaths.hasMoreElements()) {
				String entryPath = (String) entryPaths.nextElement();
				try {
					toRet.add(new File(FileLocator.toFileURL(bundle.getEntry(entryPath)).toURI()));
				} catch (URISyntaxException e) {
					throw(new FileNotFoundException(directoryPath + " (" + e.toString() + ")"));
				} catch (FileNotFoundException e) {
					throw(e);
				} catch (IOException e) {
					throw(new FileNotFoundException(directoryPath + " (" + e.toString() + ")"));
				}
			}
		}
		return toRet;
	}

	/**
	 * @return true if running as part of a JUnit test.
	 */
	public static boolean isJunitRunning() {
		return CommonPlugin.isJunitRunning();
	}

	/**
	 * Detects whether being run by Bamboo.
	 * 
	 *  (FIXME:  The implementation is a heuristic that is probably not
	 *  very definitive.  Feel free to improve it if you know how.)
	 *  
	 * @return true if being run by Bamboo, false if running in IDE or built application.
	 */
	public static boolean isBamboo() {
		if (isBamboo==null) {
			boolean ramDiskTest = new File("/ramdisk/bamboo/").exists();
			String directory = System.getenv("ENSEMBLE_HOME");
			boolean dirTest = directory != null && new File(directory).exists();
			String home = System.getProperty("eclipse.home.location");
			boolean homeTest = home != null && home.contains("bamboo");
			String username = System.getProperty("user.name");
			boolean usernameTest = username != null && username.equals("merops");
			int amountOfEvidence = (ramDiskTest?1:0) + (dirTest?1:0) + (homeTest?1:0) + (usernameTest?1:0);
			isBamboo = amountOfEvidence >= 2;
		}
		return isBamboo;
	}
	
	public static double SKIP_TESTS_PREDICTED_TO_RUN_LONGER_THAN_N_SECONDS = isBamboo()? 25.0 : 60.0;
	
	/** Some of our Score tests that use naturally-occuring plans with thousands of activities and millions of lines
	 * take longer than 20 seconds, which adds up to more than the 10-minute timeout. Rather than commenting them out,
	 * here's a central place we can call to skip tests that have been manually recorded (from Bamboo logs)
	 * to take longer than a certain amount of time.
	 */
	public static boolean canAffordToRunTestsLongerThan(double nSeconds) {
		return nSeconds <= SKIP_TESTS_PREDICTED_TO_RUN_LONGER_THAN_N_SECONDS;
	}


}
