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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;

public class FileUtilities {

	/**
	 * Strictly find the file in the plugin. If the file does not exist, an exception is thrown.
	 * 
	 * An alternative to this method is to use FileLocator.openStream, (preferred) or to call FileUtilities.copyToMetadata and then use paths relative to the metadata.
	 * 
	 * @param plugin
	 * @param filename
	 * @return File
	 * @throws IOException
	 * @deprecated use FileLocator.openStream(plugin.getBundle(), new Path(filename), null) instead
	 */
	@Deprecated
	public static File findFileInPlugin(Plugin plugin, String filename) throws IOException {
		return findFileInPluginBundle(plugin.getBundle(), filename);
	}

	/**
	 * Strictly find the file in the plugin. If the file does not exist, an exception is thrown.
	 * 
	 * An alternative to this method is to use FileLocator.openStream, (preferred) or to call FileUtilities.copyToMetadata and then use paths relative to the metadata.
	 * 
	 * @param bundle
	 * @param filename
	 * @return File
	 * @throws IOException
	 * @deprecated use FileLocator.openStream(bundle, new Path(filename), null) instead
	 */
	@Deprecated
	public static File findFileInPluginBundle(Bundle bundle, String filename) throws IOException {
		Path relativePath = new Path(filename);
		URL pluginRelativeURL = FileLocator.find(bundle, relativePath, null);
		if (pluginRelativeURL == null) {
			throw new FileNotFoundException("File not found: " + filename + " in bundle " + bundle.getSymbolicName());
		}
		URL fileURL = FileLocator.resolve(pluginRelativeURL);
		File file = new File(fileURL.getFile());
		if (file.exists()) {
			return file;
		}
		InputStream inputStream = FileLocator.openStream(bundle, relativePath, false);
		if (inputStream == null) {
			throw new FileNotFoundException("File not found: " + filename + " in bundle " + bundle.getSymbolicName());
		}
		file = createTempFile("bundleFile", "");
		FileOutputStream outputStream = new FileOutputStream(file);
		try {
			byte[] buffer = new byte[1024 * 1024];
			while (true) {
				int len = inputStream.read(buffer);
				if (len <= 0) {
					break;
				}
				outputStream.write(buffer, 0, len);
			}
		} finally {
			inputStream.close();
			outputStream.close();
		}
		return file;
	}

	/**
	 * Return the directory where temporary files are created.
	 * 
	 * @return the directory where temporary files are created.
	 * @throws IOException
	 */
	public static String getTempDirectory() throws IOException {
		String tempDirectory = System.getProperty("java.io.tmpdir"); // works in JDK 1.4+
		if (tempDirectory != null) {

			return tempDirectory;
		}
		File f = File.createTempFile("directory-finder", ".tmp"); // fall back for politeness
		f.delete();
		return f.getParent();
	}

	/**
	 * Creates a file in the platform's working directory.
	 * 
	 * @param relativeFilename
	 * @param fileContents
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean createFileInWorkingDirectory(String relativeFilename, String fileContents) throws IOException {
		return createFileInWorkingDirectory(relativeFilename, new ByteArrayInputStream(fileContents.getBytes()));
	}

	/**
	 * Creates a file in the platform's working directory.
	 * 
	 * @param relativeFilename
	 * @param ins
	 * @return boolean
	 * @throws IOException
	 */
	public static boolean createFileInWorkingDirectory(String relativeFilename, InputStream ins) throws IOException {
		Location instanceLocation = Platform.getInstanceLocation();
		if (instanceLocation == null || !instanceLocation.isSet()) {
			Logger logger = Logger.getLogger(FileUtilities.class);
			logger.warn("cannot create file '" + relativeFilename + "' because platform does not have a working directory.");
			return false;
		} else {
			File cacheFile = new File(instanceLocation.getURL().getFile() + File.separator + relativeFilename);
			// if cache file already exists, remove it because we are about to
			// overwrite it
			if (cacheFile.exists()) {
				cacheFile.delete();
			}
			cacheFile.getParentFile().mkdirs();
			cacheFile.createNewFile();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
			FileWriter writer = new FileWriter(cacheFile);
			String line = reader.readLine();
			while (line != null) {
				writer.write(line);
				writer.write("\n");
				line = reader.readLine();
			}
			writer.flush();
			writer.close();
			return true;
		}
	}

	/**
	 * @param relativeFilename
	 * @return a File corresponding to the working directory + relativeFilename or null if the platform's working directory is not specified
	 */
	public static File getFileInWorkingDirectory(String relativeFilename) {
		File cacheFile = null;
		Location instanceLocation = Platform.getInstanceLocation();
		if (instanceLocation == null || !instanceLocation.isSet()) {
			Logger logger = Logger.getLogger(FileUtilities.class);
			logger.warn("cannot retrieve file '" + relativeFilename + "' because platform does not have a working directory.");
		} else {
			cacheFile = new File(instanceLocation.getURL().getFile() + File.separator + relativeFilename);
		}
		return cacheFile;
	}

	/**
	 * 
	 * @param directory
	 * @param extension
	 * @return A list of fileNames in the directory with the specified extension.
	 */
	public static String[] getAllFileNamesWithExtension(String directory, final String extension) {
		return getAllFileNamesWithExtension(new File(directory), extension);
	}

	/**
	 * 
	 * @param directory
	 * @param extension
	 * @return A list of fileNames in the directory with the specified extension.
	 */
	public static String[] getAllFileNamesWithExtension(File directory, final String extension) {
		String[] ret = directory.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(extension);
			}
		});
		return ret == null ? new String[0] : ret;
	}

	/**
	 * 
	 * @param directory
	 * @param extensions
	 *            the list of acceptable extensions
	 * @return A list of fileNames in the directory with the specified extension.
	 */
	public static String[] getAllFileNamesWithExtension(File directory, final String... extensions) {
		String[] ret = directory.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean ret = false;
				for (String extension : extensions)
					ret |= name.endsWith(extension);
				return ret;
			}
		});
		return ret == null ? new String[0] : ret;
	}

	/**
	 * 
	 * @param directory
	 * @param extensions
	 *            the list of acceptable extensions
	 * @return A list of fileNames in the directory with the specified extension.
	 */
	public static File[] getAllFilesWithExtensions(String directory, final String... extensions) {
		return getAllFilesWithExtensions(new File(directory), extensions);
	}

	/**
	 * 
	 * @param directory
	 * @param extensions
	 *            the list of acceptable extensions
	 * @return A list of fileNames in the directory with the specified extension.
	 */
	public static File[] getAllFilesWithExtensions(File directory, final String... extensions) {
		File[] ret = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				boolean ret = false;
				for (String extension : extensions)
					ret |= name.endsWith(extension);
				return ret;
			}
		});
		return ret == null ? new File[0] : ret;
	}

	/**
	 * 
	 * @param directory
	 * @param extension
	 * @return A list of files in the directory with the specified extension.
	 */
	public static File[] getAllFilesWithExtension(String directory, final String extension) {
		return getAllFilesWithExtension(new File(directory), extension);
	}

	/**
	 * 
	 * @param directory
	 * @param extension
	 * @return A list of files in the directory with the specified extension.
	 */
	public static File[] getAllFilesWithExtension(File directory, final String extension) {
		File[] ret = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(extension);
			}
		});
		return ret == null ? new File[0] : ret;
	}

	/**
	 * Create a temporary file that will auto-delete when this VM terminates
	 * 
	 * @param prefix
	 * @param suffix
	 * @return File
	 * @throws IOException
	 */
	public static File createTempFile(String prefix, String suffix) throws IOException {
		File tempFile = File.createTempFile(prefix, suffix);
		tempFile.deleteOnExit();
		return tempFile;
	}

	/**
	 * Create a temporary file that will auto-delete when this VM terminates
	 * 
	 * @param prefix
	 * @param suffix
	 * @param directory
	 * @return File
	 * @throws IOException
	 */
	public static File createTempFile(String prefix, String suffix, File directory) throws IOException {
		File tempFile = File.createTempFile(prefix, suffix, directory);
		tempFile.deleteOnExit();
		return tempFile;
	}

	/**
	 * Make a copy of a file on the filesystem (platform independent).
	 * 
	 * @param source
	 *            the file to copy.
	 * @param dest
	 *            the copy to make.
	 * @return whether the file copy exists on the filesystem upon completion.
	 * @throws IOException
	 */
	public static boolean copyFile(File source, File dest) throws IOException {
		FileInputStream sourceStream = new FileInputStream(source);
		FileChannel sourceChannel = sourceStream.getChannel();
		FileOutputStream destStream = new FileOutputStream(dest);
		FileChannel destChannel = destStream.getChannel();
		try {
			sourceChannel.transferTo(0, sourceChannel.size(), destChannel);
		} catch (IOException ex) {
			if (ex.getCause() instanceof OutOfMemoryError) {
				IOUtils.copy(sourceStream, destStream);
			}
		} finally {
			destChannel.close();
			IOUtils.closeQuietly(destStream);
			sourceChannel.close();
			IOUtils.closeQuietly(sourceStream);
		}

		return dest.exists();
	}

	public static String getExtension(File file) {
		if (file == null)
			return null;
		String name = file.getName();
		int lastDot = name.lastIndexOf('.');
		if (lastDot == -1)
			return "";
		return name.substring(lastDot + 1);
	}

	public static String getNameWithoutExtension(File file) {
		if (file == null)
			return null;
		String name = file.getName();
		int lastDot = name.lastIndexOf('.');
		if (lastDot == -1)
			return name;
		return name.substring(0, lastDot);
	}

	/**
	 * This function will copy files or directories from one location to another. note that the source and the destination must be mutually exclusive. This function can not be used to copy a directory
	 * to a sub directory of itself. The function will also have problems if the destination files already exist.
	 * 
	 * @param src
	 *            -- A File object that represents the source for the copy
	 * @param dest
	 *            -- A File object that represnts the destination for the copy.
	 * @throws IOException
	 *             if unable to copy.
	 */
	public static void copyFiles(File src, File dest) throws IOException {
		// Check to ensure that the source is valid...
		if (!src.exists()) {
			throw new IOException("copyFiles: Can not find source: " + src.getAbsolutePath() + ".");
		} else if (!src.canRead()) { // check to ensure we have rights to the source...
			throw new IOException("copyFiles: No right to source: " + src.getAbsolutePath() + ".");
		}
		// is this a directory copy?
		if (src.isDirectory()) {
			if (!dest.exists()) { // does the destination already exist?
				// if not we need to make it exist if possible (note this is mkdirs not mkdir)
				if (!dest.mkdirs()) {
					throw new IOException("copyFiles: Could not create direcotry: " + dest.getAbsolutePath() + ".");
				}
			}
			// get a listing of files...
			String list[] = src.list();
			// copy all the files in the list.
			for (int i = 0; i < list.length; i++) {
				File dest1 = new File(dest, list[i]);
				File src1 = new File(src, list[i]);
				copyFiles(src1, dest1);
			}
		} else {
			// This was not a directory, so lets just copy the file
			FileInputStream fin = null;
			FileOutputStream fout = null;
			byte[] buffer = new byte[4096]; // Buffer 4K at a time (you can change this).
			int bytesRead;
			try {
				// open the files for input and output
				fin = new FileInputStream(src);
				fout = new FileOutputStream(dest);
				// while bytesRead indicates a successful read, lets write...
				while ((bytesRead = fin.read(buffer)) >= 0) {
					fout.write(buffer, 0, bytesRead);
				}
			} catch (IOException e) { // Error copying file...
				IOException wrapper = new IOException("copyFiles: Unable to copy file: " + src.getAbsolutePath() + "to" + dest.getAbsolutePath() + ".");
				wrapper.initCause(e);
				wrapper.setStackTrace(e.getStackTrace());
				throw wrapper;
			} finally { // Ensure that the files are closed (if they were open).
				if (fin != null) {
					fin.close();
				}
				if (fout != null) {
					fout.close();
				}
			}
		}
	}

	public static void saveDocument(Document doc, File file) throws TransformerFactoryConfigurationError, TransformerException {
		saveDocument(doc, new StreamResult(file));
	}

	public static void saveDocument(Document doc, Writer w) throws TransformerFactoryConfigurationError, TransformerException {
		saveDocument(doc, new StreamResult(w));
	}

	public static void saveDocument(Document doc, Result result) throws TransformerFactoryConfigurationError, TransformerException {
		Source source = new DOMSource(doc);

		// Write the DOM document to the file
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "ASCII");
		transformer.transform(source, result);
	}

	/**
	 * recursive last modified method
	 * 
	 * @param file
	 * @return Date
	 */
	public static Date getLastModified(File file) {
		Date date = null;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File currentFile : files) {
				Date currentLastModifiedDate = getLastModified(currentFile);
				if (date == null || (currentLastModifiedDate != null && date.before(currentLastModifiedDate))) {
					date = currentLastModifiedDate;
				}
			}
		} else {
			date = new Date(file.lastModified());
		}
		return date;
	}

	/**
	 * recursive method to set modified time to a file/directory
	 * 
	 * @param file
	 * @param time
	 */
	public static void setLastModified(File file, long time) {
		if (file.isDirectory()) {
			for (File currentFile : file.listFiles()) {
				setLastModified(currentFile, time);
			}
		} else {
			file.setLastModified(time);
		}
	}

	/**
	 * Copy the entries specified from the bundle provided to the metadata area for the bundle provided, and return the URL for the metadata area for this bundle.
	 * 
	 * @param bundle
	 * @param path
	 * @param filePattern
	 * @param recurse
	 * @return URL
	 * @throws IOException
	 */
	public static URL copyToMetadata(Bundle bundle, String path, String filePattern, boolean recurse) throws IOException {
		Location instanceLocation = Platform.getInstanceLocation();
		URL dataArea = getDataArea(instanceLocation, bundle.getSymbolicName());
		Enumeration entries = bundle.findEntries(path, filePattern, recurse);
		while (entries.hasMoreElements()) {
			URL entry = (URL) entries.nextElement();
			String entryPath = entry.getPath();
			try {
				InputStream inputStream = FileLocator.openStream(bundle, new Path(entryPath), false);
				URI dataURI = dataArea.toURI();
				URI fullPath = URI.create(dataURI.toString() + entryPath);
				File file = new File(fullPath);
				if (!file.exists()) {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				FileOutputStream outputStream = new FileOutputStream(file);
				IOUtils.copy(inputStream, outputStream);
				outputStream.close();
			} catch (Exception e) {
				// skip this one
			}
		}
		return dataArea;
	}

	/**
	 * 3.5 compatibility with 3.6
	 */
	private static String dataAreaPrefix = ".metadata/.plugins/";

	public static URL getDataArea(Location location, String filename) throws IOException {
		URL base = location.getURL();
		if (base == null)
			throw new IOException("Null location");
		String prefix = base.toExternalForm();
		if (prefix.length() > 0 && prefix.charAt(prefix.length() - 1) != '/')
			prefix += '/';
		prefix = prefix.replace(" ", "%20");
		filename = filename.replace('\\', '/');
		if (filename.length() > 0 && filename.charAt(0) == '/')
			filename.substring(1);
		return new URL(prefix + dataAreaPrefix + filename);
	}

	public static String getUsableSpaceString(File anyFileInSystem) {
		long totalSpace = anyFileInSystem.getTotalSpace();
		if (totalSpace == 0)
			return "Disk space unknown.";
		long usableSpace = anyFileInSystem.getUsableSpace();
		return bytesString(usableSpace) + " out of " + bytesString(totalSpace) + " available.";
	}

	public static String bytesString(long totalSpace) {
		double number = totalSpace;
		NumberFormat format = new DecimalFormat("0.00");
		number /= 1024;
		if (number < 1)
			return "< 1kB";
		if (number < 1024)
			return format.format(number) + "kb";
		number /= 1024;
		if (number < 1024)
			return format.format(number) + "MB";
		number /= 1024;
		if (number < 1024)
			return format.format(number) + "GB";
		number /= 1024;
		return format.format(number) + "TB";
	}

}
