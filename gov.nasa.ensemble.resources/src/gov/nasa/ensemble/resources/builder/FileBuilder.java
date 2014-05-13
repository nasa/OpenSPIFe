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
package gov.nasa.ensemble.resources.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class FileBuilder extends AbstractBuilder {
	
	@Override
	protected void handleChanged(IResource resource, IProgressMonitor monitor) throws CoreException {
		if (resource instanceof IFile) {
			final IFile file = (IFile) resource;
			if (isCompilable(file))
				compile(file, monitor);
		}
	}
	
	@Override
	protected void handleAdded(IResource resource, IProgressMonitor monitor) throws CoreException {
		handleChanged(resource, monitor);
	}
	
	protected abstract boolean isCompilable(IFile file) throws CoreException;

	protected abstract void compile(IFile file, IProgressMonitor monitor) throws CoreException;
}
