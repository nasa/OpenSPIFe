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
package gov.nasa.ensemble.resources;

import static fj.data.Option.*;
import static gov.nasa.ensemble.resources.ResourceUtil.*;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class ResourceModificationWatcher {
	private final Map<IResource, Long> modStamps = new HashMap<IResource, Long>();
	
	public void watch(final IWorkspaceRunnable runnable, final IProgressMonitor monitor) throws CoreException {
		runAndListen(runnable, new IResourceDeltaVisitor() {
			@Override
			public boolean visit(IResourceDelta delta) {
				final IResource resource = delta.getResource();
				final long modificationStamp = resource.getLocalTimeStamp();
				modStamps.put(resource, modificationStamp);
				return true;
			}
		}, monitor);
	}
	
	public boolean modified(final IResource resource) {
		return fromNull(modStamps.get(resource)).orSome(0L) == resource.getLocalTimeStamp();
	}
	
	public boolean modified() {
		return !modStamps.isEmpty();
	}
}
