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
package gov.nasa.ensemble.core.detail.emf.binding;

import gov.nasa.ensemble.common.CommonPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.provider.FileInfo;
import org.eclipse.core.filesystem.provider.FileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Use this class instead of LocalFile, since as of 3.4.2 LocalFile is
 * internal api. 
 * @author Eugene Turkov
 *
 */
public class LocalFileStore extends FileStore {
	private File file;
	
	public LocalFileStore(File file) {
		this.file = file;
	}

		@Override
	public String[] childNames(int options, IProgressMonitor monitor) {
		String[] names = file.list();
		return (names == null ? EMPTY_STRING_ARRAY : names);
	}

	@Override
	public IFileInfo fetchInfo(int options, IProgressMonitor monitor) {
		/* DEPENDENCY ON INTERNAL API, comment out.
		if (LocalFileNatives.usingNatives()) {
			FileInfo info = LocalFileNatives.fetchFileInfo(filePath);
			// natives don't set the file name on all platforms
			if (info.getName().length() == 0)
				info.setName(file.getName());
			return info;
		} */
		
		// in-lined non-native implementation
		FileInfo info = new FileInfo(file.getName());
		final long lastModified = file.lastModified();
		if (lastModified <= 0) {
			// if the file doesn't exist, all other attributes should be default
			// values
			info.setExists(false);
			return info;
		}
		info.setLastModified(lastModified);
		info.setExists(true);
		info.setLength(file.length());
		info.setDirectory(file.isDirectory());
		info.setAttribute(EFS.ATTRIBUTE_READ_ONLY, file.exists()
				&& !file.canWrite());
		info.setAttribute(EFS.ATTRIBUTE_HIDDEN, file.isHidden());
		return info;
	}

	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public IFileStore getParent() {
		File parent = file.getParentFile();
		return parent == null ? null : new LocalFileStore(parent);
	}

	@Override
	/**
	 * Do not use options, they are not "checked"
	 */
	public InputStream openInputStream(int options, IProgressMonitor monitor)
		throws CoreException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
		}
		
		catch (FileNotFoundException e) {
			Status status = new Status(IStatus.ERROR, CommonPlugin.ID, e.getMessage());
			CoreException coreException = new CoreException(status);
			coreException.setStackTrace(e.getStackTrace());
			throw coreException;
		}
		
		return inputStream;
	}

	@Override
	public URI toURI() {
		return file.toURI();
	}

	@Override
	public IFileStore getChild(String name) {
		return new LocalFileStore(new File(file, name));
	}
}
