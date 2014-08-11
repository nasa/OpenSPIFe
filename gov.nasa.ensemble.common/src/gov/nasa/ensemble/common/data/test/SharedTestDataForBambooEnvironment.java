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

import gov.nasa.ensemble.common.io.FileUtilities;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.time.DurationFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This is the implementation by SharedTestData when it finds it's running on Bamboo. The first call to its static methods creates one instance of this class, which it keeps statically for the
 * duration of the build job.
 * <p>
 * To save space, all Bamboo builds can share the same checked-out data. (We assume the data can be the same on the trunk and all branches.)
 * <p>
 * However, if two builds attempt to run SVN at the same moment, it can corrupt the working directory, and experience shows this happened once a week or so. Therefore, we use a lock-file, and one
 * build will use a different directory. We keep a pool of a small number of those directories. The lock is only held while writing to the directory (svn checkout the first time, then svn update), not
 * while reading files from it. The rationale for this is
 * <ol>
 * <li>I want to keep the API as simple as possible and not burden a caller with having a tearDown method that releases the lock, nor do I want to wait until the build is finish before releasing it.
 * <li>although it's theoretically possible for Run 1 to update a directory and then moments later while it's still running tests have Run 2 try to update the same directory and actually find
 * brand-new changes, <em>and</em> start updating a file at exactly the same time Run 1 is reading it, I expect this to be an extremely infrequent occurrence and to create only a single test failure
 * instead of corruption.
 * <li>if a build holds the lock for a long time, we'll wind up needing as many copies as there are build agents, in which case we might as well dedicate one to each agent.
 * </ol>
 * 
 * 
 * @author kanef
 * 
 */
/* private to this package */class SharedTestDataForBambooEnvironment {

	private static final String LOCATION_OF_THIS_FILE = "$HeadURL: https://ensemble.jpl.nasa.gov/svn/ensemble/branches/SPIFeOSS_Kepler/plugins/ensemble/gov.nasa.ensemble.common/src/gov/nasa/ensemble/common/data/test/SharedTestDataForBambooEnvironment.java $";
	private final String REPOSITORY_IN_BAMBOO = LOCATION_OF_THIS_FILE.replaceAll("^\\$HeadURL: *", "").replaceAll("/plugins/.+$", "/data/");
	private static final boolean IS_TRUNK = LOCATION_OF_THIS_FILE.contains("/trunk");
	private static final String BRANCH_NAME = IS_TRUNK ? null : LOCATION_OF_THIS_FILE.replaceAll("^\\$.*/branches/", "").replaceAll("/plugins/.+$", "");

	private static final String TEMPORARY_PREFIX = "new-one-being-checked-out-";
	private static final String NONTEMPORARY_PREFIX = "WorkingDir-";
	private static final String FORMER_PREFIX_TO_DELETE = "checkout-"; // swap to force clean checkout
	private static final String CORRUPTED_PREFIX = "MAY-BE-CORRUPTED-";
	private static final String DELETING_PREFIX = "DELETING-";
	private String dataPluginId;

	private static final File HOME_DIRECTORY = new File(System.getenv("ENSEMBLE_HOME"));
	private static final File WORKING_DIRECTORY_POOL = new File(HOME_DIRECTORY, "test/SharedTestData/" + (IS_TRUNK ? "Working-Directory-Pool/" : "Working-Directory-Pool-for-Branch-" + BRANCH_NAME + "/"));
	private File workingDirectory = null;
	private static final Map<File, FileLock> locks = new HashMap(10);
	private static final String LOCKABLE_FILE_NAME = ".SharedTestData-can-lock-this-file";
	private static final int MAX_POOL_SIZE = 5;

	private Set<File> directoriesAlreadyUpToDate = new HashSet<File>(); // some test updated these during this build
	private boolean alreadyCheckedConfiguration = false;
	private int keepCorruptedDirectoriesFor = 5 * DAYS;
	private int discardCorruptedDirectoriesIfDiskSpaceLessThan = 1 * GIGABYTES;

	private static final int MINUTES = 60 * 1000;
	private static final int HOURS = 60 * MINUTES;
	private static final int DAYS = 24 * HOURS;
	private static final int MEGABYTES = 1024 * 1024;
	private static final int GIGABYTES = 1024 * MEGABYTES;

	public SharedTestDataForBambooEnvironment(String dataPluginId) {
		this.dataPluginId = dataPluginId;
		reportMetrics();
		deleteStaleDirectories(TEMPORARY_PREFIX, 30 * MINUTES); // Should never find any, in theory.
		deleteStaleDirectories(FORMER_PREFIX_TO_DELETE, 20 * MINUTES); // after changing names to force clean checkout
		deleteStaleDirectories(CORRUPTED_PREFIX, WORKING_DIRECTORY_POOL.getUsableSpace() < discardCorruptedDirectoriesIfDiskSpaceLessThan ? 0 // delete immediately if running low on disk space
				: keepCorruptedDirectoriesFor);
	}

	private String getRepositoryURL() {
		return REPOSITORY_IN_BAMBOO + dataPluginId + "/";
		// For local test: return "https://ensemble.jpl.nasa.gov/svn/ensemble/trunk/plugins/ensemble/";
	}

	private String getCheckoutCommand() {
		return "svn checkout --quiet --non-interactive " + getRepositoryURL();
	}

	/** Returns a subdirectory that contains a freshly updated copy of the desired data */
	File findDirectoryForBambooAndCheckout(String path) throws IOException {
		if (workingDirectory == null) {
			workingDirectory = makeOrReuseWorkingDirectory();
		}
		File subdirectory = new File(workingDirectory, path);
		if (!directoriesAlreadyUpToDate.contains(subdirectory)) {
			try {
				// Get any updates checked in since the last run.
				// Also, since we may do non-recursive checkouts due to timing issues,
				// we need to update the first time too to get one directory recursively.
				doUpdate(path);
				directoriesAlreadyUpToDate.add(subdirectory);
			} finally {
				unlockDirectory(workingDirectory);
			}
		}
		return subdirectory;
	}

	/**
	 * Returns a directory containing an up-to-date checkout, ready to read from. It may be an existing one we updated, or a new one just checked out.
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private File makeOrReuseWorkingDirectory() throws IOException {
		if (!WORKING_DIRECTORY_POOL.exists())
			createDirectory(WORKING_DIRECTORY_POOL);

		workingDirectory = reuseAvailableWorkingDirectory();
		if (workingDirectory != null) {
			System.out.println("SharedTestData will reuse and update an existing working directory, " + workingDirectory);
			return workingDirectory;
		}

		System.out.println("SharedTestData needs to do a new checkout.");
		workingDirectory = makeAndCheckoutNewWorkingDirectory();
		if (workingDirectory != null) {
			System.out.println("SharedTestData created " + workingDirectory);
			return workingDirectory;
		}

		throw new IOException("For some reason, could not created a working directory");
	}

	protected void createDirectory(File directory) throws IOException {
		if (!directory.mkdirs()) {
			throw new IOException(getClass().getSimpleName() + ": Cannot create directory " + directory);
		}
		getLockableFile(directory).createNewFile();
	}

	protected File getLockableFile(File directory) {
		return new File(directory, LOCKABLE_FILE_NAME);
	}

	/**
	 * Find an unlocked directory from the pool that we atomically lock before returning.
	 * 
	 * @return null if all are locked.
	 */
	private File reuseAvailableWorkingDirectory() {
		for (File candidate : getExistingDirectoryPool()) {
			if (!isDirectoryLocked(candidate)) {
				if (lockDirectory(candidate)) {
					// Only return if (1) it wasn't locked a moment ago, and
					// (2) we were able to lock it before another build agent grabbed it in a race.
					return candidate;
				}
			}
		}
		return null;
	}

	private Collection<File> getExistingDirectoryPool() {
		return getExistingDirectoryPool(NONTEMPORARY_PREFIX);
	}

	private Collection<File> getExistingDirectoryPool(String prefix) {
		Collection<File> result = new TreeSet<File>();
		File[] allFiles = WORKING_DIRECTORY_POOL.listFiles();
		if (allFiles == null)
			return result; // null on I/O error or directory not found
		for (File file : allFiles) {
			if (file.isDirectory() && file.getName().startsWith(prefix)) {
				result.add(file);
			}
		}
		return result;
	}

	private void deleteStaleDirectories(String prefix, int aboveAge) {
		try {
			Collection<File> tempFiles = getExistingDirectoryPool(prefix);
			long now = System.currentTimeMillis();

			for (File dir : tempFiles) {
				long age = now - dir.lastModified();
				if (age > aboveAge) {
					System.out.println("Deleting " + DurationFormat.getEnglishDuration(age / 1000).replace(" ", "-") + "-old directory " + dir);
					System.out.println(" - Before deletion: " + getUsableSpaceString());
					deleteDirectoryAndContents(dir);
					System.out.println(" - After deletion: " + getUsableSpaceString());
				}
			}
		} catch (Exception e) {
			// Ignore errors.
		}
	}

	protected void reportMetrics() {
		try {
			Collection<File> pool = getExistingDirectoryPool(NONTEMPORARY_PREFIX);
			Collection<File> tempFiles = getExistingDirectoryPool(TEMPORARY_PREFIX);
			Collection<File> corruptedFiles = getExistingDirectoryPool(CORRUPTED_PREFIX);
			Collection<File> deletingFiles = getExistingDirectoryPool(DELETING_PREFIX);
			Collection<File> lockedDirectories = new ArrayList(1);

			for (File directory : pool) {
				if (isDirectoryLocked(directory)) {
					lockedDirectories.add(directory);
				}
			}
			System.out.println("SharedTestData location:");
			System.out.println(" - IS_TRUNK = " + IS_TRUNK);
			System.out.println(" - BRANCH_NAME = " + BRANCH_NAME);
			System.out.println(" - REPOSITORY_IN_BAMBOO = " + REPOSITORY_IN_BAMBOO);
			System.out.println("SharedTestData metrics:");
			System.out.println(" - " + getUsableSpaceString());
			reportMetricsOn("working directory in the pool", "working directories in the pool", pool);
			reportMetricsOn("directory is locked for update", "directories are locked for update", lockedDirectories);
			reportMetricsOn("temp directory created for a new checkout in progress", "temp directories created for new checkouts in progress", tempFiles);
			reportMetricsOn("directory that was previously discarded due to an SVN error", "directories that were previously discarded due to SVN errors", corruptedFiles);
			reportMetricsOn("directory that is in the process of being deleted", "directories that should be in the process of being deleted", deletingFiles);
		} catch (Exception e) {
			System.err.println("Cannot report metrics: " + e); // could happen due to timing race
		}
	}

	private String getUsableSpaceString() {
		return FileUtilities.getUsableSpaceString(HOME_DIRECTORY);
	}

	private void reportMetricsOn(String nounSingular, String nounPlural, Collection<File> matches) {
		long now = System.currentTimeMillis();
		int total = matches.size();
		long minLockAge = Long.MAX_VALUE;
		long maxLockAge = Long.MIN_VALUE;

		for (File file : matches) {
			long age = now - file.lastModified();
			if (age < minLockAge)
				minLockAge = age;
			if (age > maxLockAge)
				maxLockAge = age;
		}

		switch (total) {
		case 0:
			System.out.println("- No " + nounPlural);
			break;
		case 1:
			System.out.println("- There is one " + nounSingular + ", " + DurationFormat.getEnglishDuration(minLockAge / 1000) + " old.");
			break;
		default:
			System.out.println("- There are " + total + " " + nounPlural + ", " + DurationFormat.getEnglishDuration(minLockAge / 1000) + " to " + DurationFormat.getEnglishDuration(maxLockAge / 1000) + " old.");
		}
	}

	protected boolean isDirectoryLocked(File directory) {
		// Doesn't matter who locked it; if we don't remember locking it,
		// the lock must not belong to this process, since there's only
		// one instance of this class.
		return locks.containsKey(directory);
	}

	/**
	 * @param directory
	 *            to lock (during an SVN update).
	 * @return true, unless it's already locked, perhaps due to a race condition, or gets an error
	 */
	protected boolean lockDirectory(File directory) {
		FileLock lock = null;
		try {
			lock = new FileOutputStream(getLockableFile(directory)).getChannel().lock();
			locks.put(directory, lock);
			return true;
		} catch (FileNotFoundException e1) {
			LogUtil.warn("Unable to lock " + directory + ": " + e1);
			return false;
		} catch (OverlappingFileLockException e1) {
			LogUtil.warn("Unable to lock " + directory + ": " + e1);
			return false;
		} catch (IOException e1) {
			LogUtil.warn("Unable to lock " + directory + ": " + e1);
			return false;
		} finally {
			if (lock != null) {
				try {
					lock.close();
				} catch (IOException e) {
					LogUtil.error(e);
				}
			}
		}
	}

	/**
	 * @param directory
	 *            to unlock No-op if not locked.
	 */
	protected void unlockDirectory(File directory) {
		FileLock lock = locks.get(directory);
		if (lock != null) {
			locks.remove(directory);
			try {
				lock.release();
			} catch (IOException e) {
				LogUtil.warn("Unable to unlock " + directory + ": " + e);
			}
		}
	}

	private File renameToNextAvailableNumber(File currentTempLocation) throws IOException {
		int poolMemberNumber = 1;
		while (poolMemberNumber < MAX_POOL_SIZE) {
			File proposedLocation = new File(WORKING_DIRECTORY_POOL, NONTEMPORARY_PREFIX + poolMemberNumber);
			if (currentTempLocation.renameTo(proposedLocation)) {
				return proposedLocation;
			}
			poolMemberNumber++;
		}
		throw new IOException("Directories are not getting released from pool and reused.");
	}

	private void doUpdate(String subdirPath) throws IOException {
		long start = System.currentTimeMillis();
		try {
			runShellProcess("svn update --quiet --non-interactive " + subdirPath, workingDirectory);
		} catch (IOException e) {
			System.err.println("Update is throwing exception:  " + e.toString());
			discardCorruptedDirectory(workingDirectory);
			throw (e);
		}
		long finish = System.currentTimeMillis();
		long elapsed = finish - start;
		String formattedDuration = (elapsed >= 0 && elapsed < 2000) ? elapsed + " ms" : DurationFormat.getEnglishDuration((finish - start) / 1000);
		System.out.println("Update of " + subdirPath + " took " + formattedDuration);
	}

	/**
	 * Check out the entire data directory. In practice this is expected to be called just once each time Bamboo's /tmp is cleared, although if several builds race to do it, two or more may be created
	 * and be added to the pool for use by later builds also need them simultaneously.
	 * 
	 * @throws IOException
	 */
	private File makeAndCheckoutNewWorkingDirectory() throws IOException {
		File tempDirectory = new File(WORKING_DIRECTORY_POOL, TEMPORARY_PREFIX + "by-" + getJobname() + "-" + System.nanoTime() % 100000000);
		boolean cleanupNeeded = true;
		try {
			createDirectory(tempDirectory);
			runShellProcess(getCheckoutCommand(), tempDirectory);
			File permDirectory = renameToNextAvailableNumber(tempDirectory);
			cleanupNeeded = false;
			return permDirectory;
		} finally {
			if (cleanupNeeded) {
				deleteDirectoryAndContents(tempDirectory);
			}
		}
	}

	private void deleteDirectoryAndContents(File directory) {
		File parentDir = directory.getParentFile();
		File tempNameWhileBeingDeleted = new File(parentDir, DELETING_PREFIX + directory.getName());
		if (directory.renameTo(tempNameWhileBeingDeleted)) {
			directory = tempNameWhileBeingDeleted;
		}
		try {
			runShellProcess("rm -rf " + directory.getAbsolutePath(), parentDir);
		} catch (IOException e) {
			System.out.println("Could not clean up while handling the next exception:  " + e);
		}
	}

	private void runShellProcess(String command, File directory) throws IOException {
		Process process = Runtime.getRuntime().exec(command, null, directory);
		// ignoreOutput(process.getInputStream());
		try {
			process.waitFor();
		} catch (InterruptedException ignore) {
			// Continue.
		} catch (ThreadDeath exception) {
			throw new IOException("Timeout (" + exception.getClass().getCanonicalName() + ") while running " + command);
		}
		if (process.exitValue() != 0) {
			// Report as an error.
			StringBuilder s = new StringBuilder(command + " was not successful:\n");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				// The original implementation did nothing to prevent conflicts,
				// so this type of error was expected. But it's not expected to happen anymore.
				// if (line.contains("Working copy") && line.contains("locked")) {
				// LogUtil.warn("Ignoring hopefully transient error: " + line);
				// return;
				// }
				s.append(line);
				s.append('\n');
			}
			s.append("Working directory:  " + directory.getAbsolutePath());
			throw new IOException(s.toString());
		}
	}

	private void discardCorruptedDirectory(File directory) {
		// Too late to help this test, but it may help it fix itself for next test.
		File newName = new File(WORKING_DIRECTORY_POOL, CORRUPTED_PREFIX + new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date()));
		workingDirectory = null;
		System.out.println("Renaming " + directory + " to " + newName);
		if (!directory.renameTo(newName)) {
			System.out.println("Rename failed!");
		} else {
			newName.setLastModified(System.currentTimeMillis()); // so we don't expire it prematurely
		}

	}

	void checkWhetherConfiguredCorrectly() throws IOException {
		if (!alreadyCheckedConfiguration) {
			try {
				new URL(REPOSITORY_IN_BAMBOO + dataPluginId).openConnection();
			} catch (IOException e) {
				throw new IOException("Can't find data from Bamboo. Please check that " + getClass().getCanonicalName() + ".REPOSITORY_IN_BAMBOO is correct.");
			}
		}
		alreadyCheckedConfiguration = true;
	}

	private String getJobname() {
		try {
			String urlWithJobnameAsThirdElement = System.getProperty("eclipse.home.location", "unknown");
			return new URL(urlWithJobnameAsThirdElement).getPath().split("/")[3];
		} catch (MalformedURLException e) {
			return null;
		}
	}

}
