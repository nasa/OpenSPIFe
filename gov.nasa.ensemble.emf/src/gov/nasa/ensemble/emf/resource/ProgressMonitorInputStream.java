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
package gov.nasa.ensemble.emf.resource;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.logging.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * This class follows the reading of an input stream
 * by recording the progress in a progress monitor.
 * 
 * To use, add the option ProgressMonitorInputStream.option(<your monitor>)
 * to the options for an EMF resource during load(<options>).
 * 
 * To implement, call ProgressMonitorInputStream.wrapInputStream in your
 * load method.  (or use ProgressMonitorXMLLoadImpl as a convenience) 
 * 
 * Alternatively, if you have a file instead of an EMF resource
 * call openFileProgressMonitorInputStream.
 * 
 * @author abachman
 *
 */
public class ProgressMonitorInputStream extends InputStream {

	private static final Object OPTION_PROGRESS_MONITOR = IProgressMonitor.class.getCanonicalName();

	private long streamPosition = 0;
	private int progressReported = 0;
	private final InputStream stream;
	private final IProgressMonitor monitor;
	private final long length;
	private final int reports;

	/**
	 * 
	 * @param length The length of the input stream in bytes
	 * @param reports The number of reports to make to monitor.worked 
	 * @param stream
	 * @param monitor
	 */
	public ProgressMonitorInputStream(long length, int reports, InputStream stream, IProgressMonitor monitor) {
		this.stream = stream;
		this.monitor = monitor;
		this.length = length;
		this.reports = reports;
	}

	@Override
	public int read() throws IOException {
		int result = stream.read();
		if (result != -1) {
			++streamPosition;
			if (streamPosition % 255 == 0)
				reportPosition();
		}
		return result;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int result = stream.read(b);
		if (result != -1) {
			streamPosition += result;
			reportPosition();
		}
		return result;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int result = stream.read(b, off, len);
		if (result != -1) {
			streamPosition += result;
			reportPosition();
		}
		return result;
	}

	@Override
	public long skip(long n) throws IOException {
		long result = stream.skip(n);
		if (result != -1) {
			streamPosition += result;
			reportPosition();
		}
		return result;
	}
	
	private void reportPosition() {
		int totalWorked = (int) (reports * (((float) streamPosition) / length));
		if (totalWorked > progressReported) {
			monitor.worked(totalWorked - progressReported);
			progressReported = totalWorked;
		}
	}

	@Override
	public int available() throws IOException {
		return stream.available();
	}

	@Override
	public void close() throws IOException {
		try {
			stream.close();
		} finally {
			monitor.done();
		}
	}

	@Override
	public void mark(int readlimit) {
		// not supported
		// stream.mark(readlimit);
	}

	@Override
	@SuppressWarnings("unused")
	public void reset() throws IOException {
		// not supported
		// stream.reset();
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	/**
	 * Convenience method for creating options to use a progress monitor
	 * 
	 * @param monitor
	 * @return
	 */
	public static Map<Object, IProgressMonitor> option(IProgressMonitor monitor) {
		return Collections.singletonMap(OPTION_PROGRESS_MONITOR, monitor);
	}
	
	/**
	 * Method for retrieving the underlying progress monitor from options.
	 * Options may be null.  Returns either the progress monitor specified,
	 * or null if no monitor was specified or the monitor specified to be null.
	 * 
	 * @param options
	 * @return
	 */
	public static IProgressMonitor getProgressMonitor(Map<?, ?> options) {
		IProgressMonitor monitor = null;
		if (options != null) {
			monitor = (IProgressMonitor)options.get(OPTION_PROGRESS_MONITOR);
		}
		return monitor;
	}
	
	/**
	 * Given the resource, if the appropriate option is set and it is possible,
	 * then wrap the input stream so that it passes reading progress to the monitor. 
	 * 
	 * @param resource
	 * @param inputStream
	 * @param options
	 * @return
	 */
	public static InputStream wrapInputStream(Resource resource, InputStream inputStream, Map<?, ?> options) {
		IProgressMonitor monitor = getProgressMonitor(options);
		if (monitor != null) {
			URI uri = resource.getURI();
			File file = null;
			try {
				if (uri.isPlatform()) {
					String platformString = uri.toPlatformString(true);
					IPath path = new Path(platformString);
					IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
					IPath rawLocation = iFile.getRawLocation();
					file = rawLocation.toFile();
				} else if (uri.isFile()) {
					String fileString = uri.toFileString();
					file = new File(fileString);
				} else {
					LogUtil.warn("unexpected URI: " + uri, new IllegalArgumentException());
				}
			} catch (Exception e) {
				LogUtil.error("couldn't find file for URI: ", e);
			}
			if (file != null) {
				inputStream = createStreamAndStartProgressMonitor(file, inputStream, monitor);
			}
		}
		return inputStream;
	}

	/**
	 * Open a file input stream on the file and then wrap the input 
	 * stream so that it passes reading progress to the monitor.
	 * Return the progress input stream.
	 * 
	 * @param file
	 * @param monitor
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream openFileProgressMonitorInputStream(File file, IProgressMonitor monitor) throws FileNotFoundException {
		return createStreamAndStartProgressMonitor(file, new FileInputStream(file), monitor);
	}
	
	private static InputStream createStreamAndStartProgressMonitor(File file, InputStream inputStream, IProgressMonitor monitor) {
		if (CommonPlugin.isJunitRunning()) {
			return inputStream;
		}
		long length = file.length();
		int work = 100;
		monitor.beginTask("Loading " + file.getName(), work);
		inputStream = new ProgressMonitorInputStream(length, work, inputStream, monitor);
		return inputStream;
	}
	
}
