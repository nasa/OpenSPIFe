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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.http.HttpUtils;
import gov.nasa.ensemble.common.io.RemotableFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * This "export" wizard page takes a server name and a directory name. It will
 * attempt to list the files in the remote directory. The directory listing of
 * the server is expected to be in the following format:
 * 
 * file::foo
 * dir::bar/
 * 
 * If the directory listing is of another format, this page will not render
 * correctly.  In this case, override the listRemoteDirectory(String) method.
 * 
 * This page will then allow the user to select a local directory to which the
 * remote files will be downloaded.
 */
public class RemoteFileExportWizardPage extends FileSystemExportWizardPage
{
	private static final Logger trace = Logger.getLogger(RemoteFileExportWizardPage.class);
	
	private String hostURL;
	private String localDestinationDirectory;

	public RemoteFileExportWizardPage(String pageName, String hostURL, String dirName, String defaultDestDirName) {
		super(pageName, dirName, defaultDestDirName);
		this.hostURL = hostURL;
	}
	
	public RemoteFileExportWizardPage(String pageName, String hostURL, String dirName) {
		super(pageName, dirName);
		this.hostURL = hostURL;
	}
	
	@Override
	protected void createDirectoryGroup(Composite parent) {
		listViewer = CheckboxTableViewer.newCheckList(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData data = new GridData(GridData.FILL_BOTH);
		listViewer.getTable().setLayoutData(data);
		listViewer.getTable().setFont(parent.getFont());
		listViewer.setContentProvider(new IStructuredContentProvider()
		{
			@Override
			public Object[] getElements(Object inputElement)
			{
				return getFilenamesFromRemoteDirectory(RemoteFileExportWizardPage.this.dirName);
			}
			@Override
			public void dispose() { /* empty */ }
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { /* empty */ }

		});
		listViewer.setLabelProvider(new ILabelProvider()
		{
			@Override
			public String getText(Object element) {
				return element.toString();
			}
			@Override
			public boolean isLabelProperty(Object element, String property) { return true; }
			@Override
			public Image getImage(Object element) { return null; }
			@Override
			public void addListener(ILabelProviderListener listener) { /* empty */ }
			@Override
			public void removeListener(ILabelProviderListener listener) { /* empty */ }
			@Override
			public void dispose() { /* empty */ }
		});
		listViewer.setInput(this.dir);
	}
	
	public List<String> getFileNamesToCopy() {
	   	if (defaultDestDirName != null)
	   	{
	   		// list all files in the remote directory
	   		List<String> list = new ArrayList<String>();
	   		for (String elem : getFilenamesFromRemoteDirectory(RemoteFileExportWizardPage.this.dirName))
	   			list.add(elem);
	   		return list;
    	}
	   	else
	   	{
        	if (listViewer == null)
        	{
        		// return an empty list b/c of an error
                MessageDialog.openError(
                		getContainer().getShell(),
                		"Error",
                		"Invalid source directory: " + this.dirName);
                return Collections.emptyList();    		
        	}
        	else
        	{
        		// return a list of all checked items
        		List<String> list = new ArrayList<String>();
        		for (Object obj : listViewer.getCheckedElements())
        		{
        			if (obj instanceof String)
        				list.add((String) obj);
        			else
        			{
        				// warning
        			}
        		}
        		return list;
        	}
    	}		
	}
	
	@Override
	public boolean finish() {
    	File destination = new File(getDestinationValue());
    	if (!ensureDirectoryExists(destination))
    	{
            MessageDialog.openError(
            		getContainer().getShell(),
            		"Error",
            		"Error validating destination directory: " + getDestinationValue());
    		return false;
    	}

    	// create RemotableFile objects for every remote file that has been selected
    	final List<RemotableFile> remoteFiles = new ArrayList<RemotableFile>();
    	localDestinationDirectory = destination.getAbsolutePath();

    	for (String filename : getFileNamesToCopy())
    		remoteFiles.add(new MyRemotableFile(filename));
    	
    	// download remote files in the background
    	new Job("Download remote files")
    	{
			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				List<File> failedToDelete = null;
				for (RemotableFile remoteFile : remoteFiles) {
					// first check if the local file exists
					// if yes, then always attempt to delete it
					// the assumption is that the remote file is the 'best' version
					File localFile = remoteFile.getLocalFile();
					if (localFile.exists()) {
						boolean deleted = localFile.delete();
						if (deleted)
							trace.info("Deleted local file (" + localFile.getAbsolutePath() + ") prior to download.");
						else {
							trace.error("Failed to delete local file (" + localFile.getAbsolutePath() + ") prior to download.");
							if (failedToDelete == null)
								failedToDelete = new ArrayList<File>();
							failedToDelete.add(localFile);
						}
					}
					
					// we have made our best attempt to clear the way prior to
					// the download
					remoteFile.getFile();
				}

				if (failedToDelete != null) {
					final StringBuilder b = new StringBuilder();
					for (File file : failedToDelete)
						b.append("\t")
						 .append(file.getAbsolutePath())
						 .append("\n");
					
					Display.getDefault().asyncExec(new Runnable()
					{
						@Override
						public void run() {
							MessageDialog.openError(
									Display.getCurrent().getActiveShell(),
									"Error Retrieving Files",
									"The following files were not successfully downloaded because a local copy "
									+ "already exists that could not be deleted:\n\n"
									+ b.toString() + "\n"
									+ "Please delete the local copies of these files or choose another directory before retrying.");
						}
					});
				}
				
				return Status.OK_STATUS;
			}
    	}.schedule();

    	// since the files are downloaded in the background, we don't have much
		// choice but to return true at this point.
    	return true;
	}

	
	/**
	 * This method will list the files that are contained in the remote
	 * directory. The directory listing of the server is expected to be in the
	 * following format:
	 * 
	 * file::foo dir::bar/
	 * 
	 * Technically, this method should not be called from the UI thread (as it
	 * currently is in the content provider).
	 * 
	 * @param remoteDir remote directory to list
	 * @return array of strings representing the file names from the remote directory
	 */
	protected String[] getFilenamesFromRemoteDirectory(String remoteDir) {
		String response = listRemoteDirectory(remoteDir);
		String[] lines = response.split("\n");
		List<String> remoteFiles = new ArrayList<String>();
		for (String line : lines) {
			if (line.startsWith("file::")) {
				String filename = line.substring(6).trim();  // skip over the six characters in 'file::'
				remoteFiles.add(filename);
			}
		}
		return remoteFiles.toArray(new String[remoteFiles.size()]);
	}
	
	/**
	 * 
	 * @param remoteDir
	 * @return String
	 */
	@SuppressWarnings("deprecation")
	protected String listRemoteDirectory(final String remoteDir) {
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 20*1000);
		HttpGet get = new HttpGet(hostURL + (hostURL.endsWith("/") ? "" : "/") + remoteDir);
		try {
			return HttpUtils.execute(client, get, new HttpUtils.HttpResponseHandlerWithResult<String>() {
				@Override
				public String handleResponse(HttpResponse response) throws Exception {
					int ret = response.getStatusLine().getStatusCode();
					if (ret != HttpStatus.SC_OK) {
						trace.error("Error (code " + ret + ") listing remote directory: " + remoteDir);
						return "";
					} else {
						return EntityUtils.toString(response.getEntity());
					}
				}
			});
		} catch (Exception e) {
			trace.error("Error listing remote directory: " + remoteDir);
			return "";
		}
	}
	
	/**
	 * A simple implementation of the RemotableFile interface. 
	 */
	@SuppressWarnings("serial")
	class MyRemotableFile extends RemotableFile {
		protected MyRemotableFile(String relativeFilePath) { super(relativeFilePath); }
		@Override
		protected String getRemoteHost() {
			String url = hostURL.startsWith("http://") ? hostURL.substring(7) : hostURL;
			return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
		}
		@Override
		protected String getRemoteRootDirectory() { return dirName + File.separator; }
		@Override
		protected String getLocalRootPath()       { return localDestinationDirectory; }
		@Override
		protected void cacheAccessed()       { /* ignore */ }
		@Override
		protected void remoteFileNotFound()  { /* ignore */ }
		@Override
		protected void remoteFileRetrieved() { /* ignore */ }
	}
}
