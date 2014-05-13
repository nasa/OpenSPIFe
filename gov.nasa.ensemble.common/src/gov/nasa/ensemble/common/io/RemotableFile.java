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

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.authentication.AuthenticationUtil;
import gov.nasa.ensemble.common.authentication.Authenticator;
import gov.nasa.ensemble.common.cache.SizeReporter;
import gov.nasa.ensemble.common.http.HttpUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.log4j.Logger;

/**
 * RemotableFile enables the access of files that may actually reside on a
 * remote or local filesystem. Downloading is done lazily and the details are
 * hidden from the user.
 */
@SuppressWarnings({ "serial" })
public abstract class RemotableFile implements Serializable, SizeReporter {

	//use a default http client unless it is overriden by some other class
	private static HttpClient client = new DefaultHttpClient();
	private boolean isLocal = false;
	private final static String DATE_FORMAT_STR = "EEE, d MMM yyyy HH:mm:ss z";

	private static REMOTE_FILE_MODES override_mode = null;
	
	static {
		String localOnly = CommonPlugin.getDefault().getEnsembleProperties().getProperty("filelocator.localonly", "");
		if (localOnly.trim().equalsIgnoreCase("true"))
			setOverRideMode(REMOTE_FILE_MODES.LOCAL_ONLY);
	}

	public enum REMOTE_FILE_MODES {
		LOCAL_ONLY, // look only locally
		STANDARD_CACHE, // use the cache if it exists, otherwise download
		CONSISTENT_CACHE, // always check to see if a newer remote version
		// exists, use local if same
		IGNORE_CACHE, // always download
		CHECK_CUSTOM_TIMESTAMP
		// check against a particular time stamp for consistency rather than the
		// modified time provided on the file
	}

	/**
	 * Pointer to the file on the remote system. Null if this is a file on the
	 * local filesystem with no remote counterpart.
	 */
	protected URL remoteFile;

	/**
	 * Pointer to the file on the local system. Never null, but may not
	 * .exist().
	 */
	protected File localFile;

	/**
	 * location of the file relative to the root of either the local or remote
	 * filesystem
	 */
	protected String relativeFilePath; // package visible so that

	// VersionedFileLocator
	// can modify, but not subclasses of it

	private long customTimeStamp;

	private static final Logger trace = Logger.getLogger(RemotableFile.class);
	private static final int DEFAULT_SOCKET_TIMEOUT = 0;
	
	protected RemotableFile(String relativeFilePath) {
		if (relativeFilePath == null) {
			throw new IllegalArgumentException(
					"Relative Path should not be null");
		}

		this.relativeFilePath = relativeFilePath;
		String relPathUnixFormat = relativeFilePath.replaceAll("\\\\", "/");
		try {
			// only build a remote file if there is a remote host ... makes
			// sense to me
			String remoteHost = getRemoteHost();
			if (CommonUtils.isNullOrEmpty(remoteHost)) {
				remoteFile = null;
			} else {
				remoteFile = buildRemoteFile(relativeFilePath);
			}
			String localFilePath = getLocalRootPath();
			if (!localFilePath.endsWith("/") && !localFilePath.endsWith("\\"))
				localFilePath += File.separator;
			localFilePath += relativeFilePath;
			localFile = new File(localFilePath);
		} catch (MalformedURLException e) {
			trace.warn("Malformed relative path: " + relPathUnixFormat, e);
		} catch (NullPointerException npe) { // catch the case if we're
			// calling from a servlet and
			// there is no extension point
			// registry
			localFile = new File(relPathUnixFormat);
		}
		createParentDirs();
		
	}
	
	
	public static void setHttpClient(HttpClient newClient) {
		client = newClient;
	}
	
	private String getParentDir(String file){
		String ret =  (file.substring(0, file.lastIndexOf(File.separator)));
		return ret;
	}
	
	private void createParentDirs(){
		String localFilePath = getLocalRootPath() + File.separator + relativeFilePath;
		String localFilePathOnly = getParentDir(localFilePath);
		try {
			File dir = new File(localFilePathOnly);
			dir.mkdirs();
		} catch (SecurityException e) {
			trace.error("Error in creating directory: " + localFilePathOnly, e);
		}
	}

	protected RemotableFile(File file) {
		this.localFile = file;
		remoteFile = null;
		this.isLocal = true;
		createParentDirs();
	}
	
	protected RemotableFile(String relativeFilePath, long customTimeStamp) {
		this(relativeFilePath);
		this.customTimeStamp = customTimeStamp;
	}

	/**
	 * Get the concrete file from this locator. If the file is available
	 * remotely but not present locally, it will be downloaded before this
	 * method returns. Callers should be prepared for this method to block for
	 * some time.
	 * 
	 * Note that the file returned from this method may not exist - this happens
	 * if there was no remote counterpart, or the counterpart could not be
	 * retrieved, AND the file is not already present on the local disk.
	 * 
	 * @param useCache
	 *            by default this method is chained from getFile(), which uses
	 *            the cache everywhere in an Ensemble application. Things like
	 *            servlets need to call this method directly with useCache =
	 *            false to workaround using the cache, since they do not cache
	 *            data, and using the RCP extension registry is problematic.
	 *            This is also appropriate when the file should not be
	 *            downloaded regardless of whether the file exists or not.
	 * @return the requested file, if it exists.
	 * @throws IOException 
	 */
	public File getFile(boolean useCache, int socketTimeout) throws SocketTimeoutException {
		if (!useCache || override_mode==REMOTE_FILE_MODES.LOCAL_ONLY) {
			return localFile;
		}
		if (!localFile.exists())
			try {
				downloadRemoteFile(socketTimeout);
			} catch (HttpException hre) {
				// HACK HACK HACK
				if (hre.getMessage().contains("SocketTimeoutException"))
					throw new SocketTimeoutException();
				trace.warn("Error obtaining " + remoteFile, hre);
			} catch (Exception e) {
				trace.warn("Error obtaining " + remoteFile, e);
			}
		else {
			cacheAccessed();
		}
		return localFile;
	}

	/**
	 * Ensemble application code should call this method to get a File. It will
	 * implicit invoke the DataProductCacheManager to manage the disk space
	 * consumed by the local cache.
	 * 
	 * @return the file from the local cache.
	 * @throws IOException 
	 */
	public File getFile(int socketTimeout) throws SocketTimeoutException {
		return getFile(true, socketTimeout);
	}
	
	public File getFile(boolean useCache) {
		try {
			return getFile(useCache, DEFAULT_SOCKET_TIMEOUT);
		} catch (IOException e) {
			trace.warn(e);
			return null;
		}
	}
	
	public File getFile() {
		return getFile(true);
	}

	/**
	 * Find the file from a known root directory.
	 * 
	 * @param rootDir
	 * @return File
	 */
	public File getFile(String rootDir) {
		return new File(rootDir + File.separator + relativeFilePath);
	}

	public static void setOverRideMode(REMOTE_FILE_MODES override) {
		override_mode = override;
	}

	/**
	 * Returns the file after getting it through the specified mode. NOTE: IF THE <code>override_mode</code> static variable is set,
	 * then the mode passed in is ignored. As of now, this mode is only set from the Orchestrator product's run method.
	 * 
	 * @param myMode
	 * @param socketTimeout
	 * @return File
	 * @throws IOException
	 */
	public File getFileByMode(REMOTE_FILE_MODES myMode, int socketTimeout) throws SocketTimeoutException {
		if (override_mode != null) {
			myMode = override_mode;
		}
		if (myMode == REMOTE_FILE_MODES.LOCAL_ONLY) {
			return localFile;
		} else if (myMode == REMOTE_FILE_MODES.STANDARD_CACHE) {
			return getFile(true, socketTimeout);
		} else if (myMode == REMOTE_FILE_MODES.CONSISTENT_CACHE) {
			if (localFile.exists()) {
				localFile = getConsistentFile(localFile.lastModified(), myMode, socketTimeout);
			} else {
				// local file does not exist..get from server
				return getFile(true, socketTimeout);
			}
		} else if (myMode == REMOTE_FILE_MODES.IGNORE_CACHE) {
			try {
				downloadRemoteFile(socketTimeout);
				return localFile;
			} catch (Exception e) {
				trace.warn("Error downloading the file from the server");
			}
		} else if (myMode == REMOTE_FILE_MODES.CHECK_CUSTOM_TIMESTAMP) {
			if (localFile.exists() && localFile.lastModified() == customTimeStamp) {
				return localFile;
			}
			return getConsistentFile(0, myMode, socketTimeout);
		}
		return localFile;
	}
	
	public File getFileByMode(REMOTE_FILE_MODES myMode) {
		try {
			return getFileByMode(myMode, DEFAULT_SOCKET_TIMEOUT);
		} catch (IOException e) {
			trace.warn(e);
			return null;
		}
	}

	private File getConsistentFile(long modified, REMOTE_FILE_MODES myMode, int socketTimeout) throws SocketTimeoutException {
		Date modifiedDate = new Date(modified);
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_STR);
		String formatedModifiedDate = df.format(modifiedDate);
		if (remoteFile == null) {
			return localFile;
		}
		HttpConnectionParams.setSoTimeout(client.getParams(), socketTimeout);

		// tell the client to use the credential we got as parameter
		// if we have an authenticator
		Authenticator auth = AuthenticationUtil.getAuthenticator();
		if (auth != null) {
			if (client instanceof AbstractHttpClient) {
				((AbstractHttpClient)client).getCredentialsProvider().setCredentials(new AuthScope(null, AuthScope.ANY_PORT, null, AuthScope.ANY_SCHEME), auth.getCredentials());
			}
//			state.setAuthenticationPreemptive(true);
		}

		HttpResponse response = null;
		HttpGet get = new HttpGet(remoteFile.toString());
		HttpClientParams.setRedirecting(get.getParams(), false);
		HttpClientParams.setAuthenticating(get.getParams(), true);
		try {
			get.setHeader(HttpHeaders.IF_MODIFIED_SINCE, formatedModifiedDate);
			response = client.execute(get);
			if (HttpStatus.SC_NOT_MODIFIED == response.getStatusLine().getStatusCode()) {
				return localFile;
			} else if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				InputStream is = response.getEntity().getContent();
				getRemoteFileFromStream(is);
				if (REMOTE_FILE_MODES.CHECK_CUSTOM_TIMESTAMP == myMode) {
					localFile.setLastModified(customTimeStamp);
				} else {
					setModifiedTime(get);
				}
				return localFile;
			} else if (HttpStatus.SC_NOT_FOUND == response.getStatusLine().getStatusCode()){
				remoteFileNotFound();
			}
		} catch (SocketTimeoutException e1) {
			throw e1;
		} catch (IOException e1) {
			// HACK HACK HACK HACK
			if (e1.getMessage().contains("SocketTimeoutException"))
				throw new SocketTimeoutException();
			else
				trace.warn("Error connecting to server when "
						+ "checking the file modified time ", e1);
		} finally {
			try {
				HttpUtils.consume(get, response);
			} catch (IOException e) {
				trace.error(e);
			}
		}
		return localFile;

	}

	private void getRemoteFileFromStream(InputStream is) throws IOException,
			FileNotFoundException {
		// create localFile that we will write to
		// - create directories if necessary
		// - overwrite if already exists
		String urlPath = remoteFile.getPath();
		urlPath = urlPath.substring(getRemoteRootDirectory().length());
		try {
			int urlPathLength = urlPath.lastIndexOf('/');
			if (urlPathLength == -1)
				urlPathLength = 0;
			String dir = getLocalRootPath();
			if (!dir.endsWith("/") && !dir.endsWith("\\"))
				dir += File.separator;
			dir += urlPath.substring(0, urlPathLength);
			File dirs = new File(dir);
			dirs.mkdirs();
			if (!dirs.exists()) {
				trace.warn("Could not create directory"
						+ dirs.getAbsolutePath());
			}
			localFile.createNewFile();
		} catch (IOException e) {
			trace.error("Error creating the file " + localFile
					+ " on the local file system: " + e);
			return;
		}
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(localFile));
		try {
			byte[] buffer = new byte[4096];
			int bytesRead = 0;
			while (bytesRead != -1) {
				bytesRead = is.read(buffer, 0, 4096);
				if (bytesRead == -1)
					continue;
				bos.write(buffer, 0, bytesRead);
			}
		} finally {
			bos.close();
			closeRemoteFileStream(is);
		}
		remoteFileRetrieved();
	}

	@Override
	public long getSize() {
		if (localFile.exists())
			return localFile.length();
		return 0;
	}

	public String getRelativeFilePath() {
		return relativeFilePath;
	}

	/**
	 * This method will download the specified localFile to the local cache. Any
	 * directories that need to be created will be generated as a side-effect of
	 * this action. There is no discretion regarding whether the localFile
	 * currently exists or not, if a localFile exists in the specified location,
	 * it will be overwritten.
	 * 
	 * If the localFile does not exist in the central repository, this method
	 * will throw an IOException and will not create a zero length localFile.
	 * However the directories that would have contained the localFile will be
	 * created.
	 * 
	 * @return a copy of the remote localFile specified
	 * @throws IOException
	 *             Exception will be thrown if the remote localFile does not
	 *             exist.
	 */
	private void downloadRemoteFile(int socketTimeout) throws Exception {
		if (remoteFile == null)
			return;

		// tell the client to use the credential we got as parameter
		Authenticator authenticator = AuthenticationUtil.getAuthenticator();
		if (authenticator != null) {
			if (client instanceof AbstractHttpClient) {
				((AbstractHttpClient) client).getCredentialsProvider().setCredentials(new AuthScope(null, AuthScope.ANY_PORT, null, AuthScope.ANY_SCHEME), authenticator.getCredentials());
			}
//			state.setAuthenticationPreemptive(true);
		}

		final HttpGet get = new HttpGet(remoteFile.toString());
		HttpClientParams.setAuthenticating(get.getParams(), true);
		HttpClientParams.setRedirecting(get.getParams(), false);
		HttpUtils.execute(client, get, new HttpUtils.HttpResponseHandler() {
			@Override
			public void handleResponse(HttpResponse response) throws Exception {
				int status = response.getStatusLine().getStatusCode();
				if (status == HttpStatus.SC_OK) {
					getRemoteFileFromStream(response.getEntity().getContent());
					setModifiedTime(get);
				}
			}
		});
	}

	private void setModifiedTime(HttpGet get) {
		try {
			String mod = get.getLastHeader(HttpHeaders.LAST_MODIFIED).getValue();
			//DateFormat does not seem thread safe, so we need a new instance each time
			DateFormat df = new SimpleDateFormat(DATE_FORMAT_STR);
			long resp_mod_time = df.parse(mod).getTime();
			if (resp_mod_time > 0) {
				localFile.setLastModified(resp_mod_time);
			} else {
				//usually caused by unsafe thread use of DateFormat
				trace.error("Negative Time. ");
			}
		} catch (ParseException e) {
			trace.error("Error parsing date from the http response: "
					+ e.toString());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RemotableFile))
			return false;
		RemotableFile fl = (RemotableFile) obj;
		// localFile will be null if running in the servlet, so defer to
		// Object.equals()
		if (localFile == null)
			return super.equals(obj);
		return localFile.equals(fl.localFile);
	}

	@Override
	public int hashCode() {
		// localFile will be null if running in the servlet, so defer to
		// Object.hashCode()
		if (localFile == null)
			return super.hashCode();
		return localFile.hashCode();
	}

	private void closeRemoteFileStream(InputStream stream) {
		try {
			if (stream != null)
				stream.close();
		} catch (IOException e1) {
			trace.warn("IOException trying to close file stream.", e1);
		} // swallow any exception
	}

	@SuppressWarnings("unused")
	private InputStream openRemoteFileStream() throws IOException {
		// put the contents of the remote localFile into local localFile
		return remoteFile.openStream();
	}

	// builds a remote file from the given path
	// package visibility is intended because this is used by
	// VersionedFileLocator
	protected URL buildRemoteFile(String relativePath)
			throws MalformedURLException {
		String remoteHost = "http://" + getRemoteHost() + "/";
		String remoteRoot = getRemoteRootDirectory();
		String relPathUnixFormat = relativePath.replaceAll("\\\\", "/");
		// replace any # characters in the URL with the Hex code equivalent that
		// a URL can handle
		// # characters sometimes appear in product ids when the site or drive
		// index is extremely high
		relPathUnixFormat = relPathUnixFormat.replaceAll("#", "%23");
	 	String path = remoteRoot + relPathUnixFormat;
	 	path = path.replace("//", "/");  // AFTER
	 	if (path.startsWith("/"))   // AFTER
	 		path = path.substring(1);  // AFTER
	 	path = remoteHost + path; // AFTER
		return new URL(path);  // AFTER
	}

	@Override
	public String toString() {
		return relativeFilePath;
	}

	public File getLocalFile() {
		return localFile;
	}

	/**
	 * Return whether this FileLocator refers only to a local file (such as for
	 * a SingletonDataProductSet).
	 * 
	 * @return true if this FileLocator refers only to a local file with no
	 *         remote file counterpart.
	 */
	public boolean isLocal() {
		// return relativeFilePath == null;
		return isLocal;
	}

	public String getRemoteFileURL() {
		return remoteFile.toString();
	}

	/**
	 * 
	 * @return the name of the host that houses the remote instance of the file.
	 */
	protected abstract String getRemoteHost();

	/**
	 * 
	 * @return the directory from the root of the host that should be prepended
	 *         to the relative path specified in the constructor.
	 */
	protected abstract String getRemoteRootDirectory();

	/**
	 * 
	 * @return path on local disk that should be prepended to the relative path
	 *         specified in the constructor.
	 */
	protected abstract String getLocalRootPath();

	/**
	 * If caching is being used for this class of RemotableFile, let the cache
	 * manager be informed of the access.
	 */
	protected abstract void cacheAccessed();
	
	/**
	 * Called when the file has been retrieved from the remote host.
	 */
	protected abstract void remoteFileRetrieved();

	/**
	 * Called when no remote file was available on the remote host.
	 */
	protected abstract void remoteFileNotFound();
}
