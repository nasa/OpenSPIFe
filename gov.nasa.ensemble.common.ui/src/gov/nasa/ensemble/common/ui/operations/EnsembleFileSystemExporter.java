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
package gov.nasa.ensemble.common.ui.operations;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Helper class for exporting resources to the file system.
 */
public class EnsembleFileSystemExporter {
	private static final int DEFAULT_BUFFER_SIZE = 16*1024;
	
    /**
     *  Creates the specified file system directory at <code>destinationPath</code>.
     *  This creates a new file system directory.
     *  
     *  @param destinationPath location to which files will be written
     */
    public void createFolder(IPath destinationPath) {
        new File(destinationPath.toOSString()).mkdir();
    }

    /**
     *  Writes the passed resource to the specified location recursively.
     *  
     *  @param resource the resource to write out to the file system
     *  @param destinationPath location where the resource will be written
     * @param monitor 
     *  @exception CoreException if the operation fails 
     *  @exception IOException if an I/O error occurs when writing files
     */
    public final void write(IResource resource, IPath destinationPath, IProgressMonitor monitor)
            throws CoreException, IOException {
        if (resource.getType() == IResource.FILE) {
			writeFile((IFile) resource, destinationPath, monitor);
		} else {
			writeChildren((IContainer) resource, destinationPath, monitor);
		}
    }

    /**
     *  Exports the passed container's children
     * @param monitor 
     */
    private final void writeChildren(IContainer folder, IPath destinationPath, IProgressMonitor monitor)
            throws CoreException, IOException {
        if (folder.isAccessible()) {
            IResource[] children = folder.members();
            for (int i = 0; i < children.length; i++) {
                IResource child = children[i];
                writeResource(child, destinationPath.append(child.getName()), monitor);
            }
        }
    }

    /**
     *  Writes the passed file resource to the specified destination on the local
     *  file system
     * @param monitor TODO
     */
    protected void writeFile(IFile file, IPath destinationPath, IProgressMonitor monitor)
            throws IOException, CoreException {
        OutputStream output = null;
        InputStream contentStream = null;

        try {
            contentStream = new BufferedInputStream(file.getContents(false));
            output = new BufferedOutputStream(new FileOutputStream(destinationPath.toOSString()));
            // for large files, need to make sure the chunk size can be handled by the VM
            int available = contentStream.available();
            available = available <= 0 ? DEFAULT_BUFFER_SIZE : available;
            int chunkSize = Math.min(DEFAULT_BUFFER_SIZE, available);
            byte[] readBuffer = new byte[chunkSize];
            int n = contentStream.read(readBuffer);

            while (n > 0) {
            	// only write the number of bytes read
                output.write(readBuffer, 0, n);
                n = contentStream.read(readBuffer);
            }
        } finally {
            if (contentStream != null) {
            	// wrap in a try-catch to ensure attempt to close output stream
            	try {
            		contentStream.close();
            	}
            	catch(IOException e){
            		LogUtil.error("Error closing input stream for file: " + file.getLocation(), e);
            	}
			}
        	if (output != null) {
        		// propogate this error to the user
           		output.close();
			}
        }
        copyFileProperties(file, destinationPath);

    }

    /**
     * Copies java reachable properties from file to file.
     * @param source Source resource
     * @param destinationPath Destination file
     * 
     * @throws IOException If any of the properties cannot be copied.
     */
	public void copyFileProperties(IResource source, IPath destinationPath)
			throws IOException {
		File inputFile = source.getLocation().toFile();
		File outputFile = new File(destinationPath.toOSString());

		boolean setRead = outputFile.setReadable(inputFile.canRead());
		boolean setExec = outputFile.setExecutable(inputFile.canExecute());
		boolean setWrite = outputFile.setWritable(inputFile.canWrite());
		boolean setTS = outputFile.setLastModified(inputFile.lastModified());
		if (!(setRead && setExec && setWrite && setTS)) {
			String failedProperties = (setExec ? "" : "X") + (setWrite ? "" : "W") + (setRead ? "" : "R") + (setTS ? "" : "D");
			throw new IOException("Some file properties (" + failedProperties + ") could not be set on \"" + outputFile.getName() + "\"");
		}
	}

    /**
     *  Writes the passed resource to the specified location recursively
     * @param monitor 
     */
    private void writeResource(IResource resource, IPath destinationPath, IProgressMonitor monitor)
            throws CoreException, IOException {
        if (resource.getType() == IResource.FILE) {
			writeFile((IFile) resource, destinationPath, monitor);
		} else {
            createFolder(destinationPath);
            writeChildren((IContainer) resource, destinationPath, monitor);
        }
    }
}
