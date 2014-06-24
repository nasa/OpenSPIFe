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
package gov.nasa.ensemble.core.jscience.xml;

import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.emf.resource.ProgressMonitorInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;

public abstract class ProfilesParser {
	public static final int UNPARSABLE = -1;
	private boolean parseRan = false;
	protected ProfilesParserErrorHandler errorHandler = new DefaultProfilesParserErrorHandler();
	protected List<Profile> profiles;

	/**
	 * Determine if parsing this stream will throw errors and if not whether it
	 * contains profiles.  Subclasses may call doParse to implement this,
	 * but are encouraged to instead implement a lightweight check if possible.
	 * 
	 * @param uri
	 *            The URL of the file being parsed.
	 * @param inputStream
	 *            Stream to test for parsability.
	 * @return ProfilesParser.UNPARSABLE if the stream cannot be parsed into profiles of this type, 0 if the
	 *         stream contains no profiles, or the number of profiles if profiles were successfully parsable.
	 *         Should return ProfilesParser.UNPARSABLE if doParse() will later thrown an exception,
	 *         to the extent that this can be checked at low cost.
	 */
	public abstract int numberParsable(URI uri, InputStream inputStream);

	/**
	 * Determine if parsing this stream will throw errors and if not whether it
	 * contains profiles.  Subclasses may call doParse to implement this,
	 * but are encouraged to instead implement a lightweight check if possible.
	 * 
	 * @param file
	 *            The file handle to test parsing on.
	 * @return ProfilesParser.UNPARSABLE if the stream cannot be parsed into profiles of this type, 0 if the
	 *         file contains no profiles, or the number of profiles if profiles were successfully parsable.
	 *         Should return ProfilesParser.UNPARSABLE if doParse() will later thrown an exception,
	 *         to the extent that this can be checked at low cost.
	 */
	public int numberParsable(File file) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			URI uri = URI.createFileURI(file.getPath());
			return numberParsable(uri, bis);
		} catch (FileNotFoundException e) {
			return ProfilesParser.UNPARSABLE;
		} finally {
			IOUtils.closeQuietly(bis);
			IOUtils.closeQuietly(fis);
		}
	}

	/**
	 * This is the main method a subclass is required to implement.
	 * Implementation should do something like <code>profiles.add(Profile)</code>.
	 * @param filename -- the URL of the file that was opened, in case it's needed
	 * @param inputStream -- the input stream.  Implementation should leave stream open; caller is expected to close it when done.
	 * @return Should return normally if and only if parse was successful.
	 */
	protected abstract void doParse(URI filename, InputStream inputStream);

	public void parse(File file, IProgressMonitor monitor) {
		FileInputStream fis = null;
		ProgressMonitorInputStream pmis = null;
		try {
			fis = new FileInputStream(file);
			URI uri = URI.createFileURI(file.getPath());
			if (monitor != null) {
				long length = file.length();
				int work = 100;
				pmis = new ProgressMonitorInputStream(length, work, fis, monitor);
				monitor.beginTask("Loading " + file.getName(), work);
				parse(uri, pmis);
			} else {
				parse(uri, fis);
			}
		} catch (IOException ex) {
			errorHandler.unhandledException(ex);
		} finally {
			IOUtils.closeQuietly(pmis);
			IOUtils.closeQuietly(fis);
		}
	}

	public void parse(URI filename, InputStream is) {
		profiles = new ArrayList<Profile>();
		doParse(filename, is);
		errorHandler.parseComplete();
		parseRan = true;
	}

	public boolean isParseRan() {
		return parseRan;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}
	
	public ProfilesParserErrorHandler getErrorHandler() {
		return errorHandler;
	}
	
	public void setErrorHandler(ProfilesParserErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}
	
	public void dispose() {
		// do nothing by default
	}
	
}
