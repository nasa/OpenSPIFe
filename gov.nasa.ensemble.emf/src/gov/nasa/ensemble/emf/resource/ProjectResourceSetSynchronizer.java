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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Service that makes sure that the project being pointed to and the
 * resource set for an editing domain remain 'n synch.
 * 
 * EMF resources are created via the resourceSet.createResource(URI) call,
 * developers should take care in registering the proper resource factories
 * on the resource set via resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(key, value)
 * 
 */
public class ProjectResourceSetSynchronizer {

	private final IProject project;
	private final ResourceSet resourceSet;
	private final Listener listener = new Listener();
	
	public ProjectResourceSetSynchronizer(IProject project, ResourceSet resourceSet) {
		super();
		this.project = project;
		this.resourceSet = resourceSet;
	}

	public void activate() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.POST_CHANGE);
	}
	
	public IProject getProject() {
		return project;
	}

	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
	}
	
	private class Listener implements IResourceChangeListener {
		
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			processDelta(event.getDelta(), 0);
		}
		
		private void processDelta(IResourceDelta delta, int i) {
			Resource eResource = null;
			IResource resource = delta.getResource();
			if (resource instanceof IFile && resource.getProject().equals(project)) {
				URI uri = URI.createURI(resource.getLocationURI().toString());
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					eResource = resourceSet.createResource(uri);
					LogUtil.debug(CommonUtils.pad(i, " ")+"ADDED: "+uri+"\t"+eResource);
					break;
				case IResourceDelta.CHANGED:
					// If the file system resource has changed, we want to update the
					// timestamp so that listeners can know it changed
					eResource = resourceSet.getResource(uri, false);
					if (eResource != null) {
						eResource.setTimeStamp(resource.getLocalTimeStamp());
						LogUtil.debug(CommonUtils.pad(i, " ")+"CHANGED: "+uri+"\t"+eResource);
					}
					break;
				case IResourceDelta.REMOVED:
					eResource = resourceSet.getResource(uri, false);
					if (eResource != null) {
						resourceSet.getResources().remove(eResource);
						LogUtil.debug(CommonUtils.pad(i, " ")+"REMOVED: "+uri+"\t"+eResource);
					}
					break;
				case IResourceDelta.ADDED_PHANTOM:
				case IResourceDelta.REMOVED_PHANTOM:
					LogUtil.debug(CommonUtils.pad(i, " ")+"PHANTOM: "+uri+"\tignored");
					break;
				}
			}
			//
			// Recurse
			for (IResourceDelta child : delta.getAffectedChildren()) {
				processDelta(child, i+1);
			}
		}
		
	}
	
}
