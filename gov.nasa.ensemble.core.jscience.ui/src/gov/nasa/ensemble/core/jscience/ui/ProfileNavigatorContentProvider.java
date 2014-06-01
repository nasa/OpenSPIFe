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
package gov.nasa.ensemble.core.jscience.ui;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.EnsembleEditingDomain;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

public class ProfileNavigatorContentProvider implements ITreeContentProvider, IResourceChangeListener, IResourceDeltaVisitor {

	private EditingDomain editingDomain = EMFUtils.createEditingDomain();
	private Viewer _viewer;

	public ProfileNavigatorContentProvider() {
		super();
		ResourceSet resourceSet = editingDomain.getResourceSet();
		resourceSet.getLoadOptions().put(ProfileUtil.IGNORE_DATA_POINTS, Boolean.TRUE);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	public Object[] getChildren(Object element) {
		if (!(element instanceof IFile))
			return new Object[0];
		URI uri = getPlatformResourceURI((IFile) element);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource resource = resourceSet.getResource(uri, true);
		((EnsembleEditingDomain)editingDomain).getResourceToReadOnlyMap().put(resource, Boolean.TRUE);
		List<Object> list = new ArrayList<Object>();
		for (EObject o : resource.getContents()) {
			if (o instanceof Profile) {
				list.add(o);
			}
		}
		return list.toArray(new Object[0]);
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof EObject) {
			return ((EObject)element).eContainer();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IFile) {
			URI uri = getPlatformResourceURI((IFile) element);
			ResourceSet resourceSet = editingDomain.getResourceSet();
			Resource resource = resourceSet.getResource(uri, true);
			for (EObject o : resource.getContents()) {
				if (o instanceof Profile) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Object[] getElements(Object element) {
		return getChildren(element);
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		_viewer = viewer;
	}

	private URI getPlatformResourceURI(IFile file) {
		return EMFUtils.getURI(file);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		try {
			IResourceDelta delta = event.getDelta();
			delta.accept(this);
		} catch (CoreException e) {
			System.out.println("Resource Changed Fail - " + e.toString());
		}

	}
	
	@Override
	@SuppressWarnings("unused")
	public boolean visit(IResourceDelta delta) throws CoreException {
		IResource changedResource = delta.getResource();
		if (changedResource.getType() == IResource.FILE) {
			try {
				ResourceSet resourceSet = editingDomain.getResourceSet();
				URI uri = EMFUtils.getURI(changedResource);
				URIConverter uriConverter = resourceSet.getURIConverter();
				if (uriConverter.exists(uri, null)) {
					Resource res = resourceSet.getResource(uri, false);
					if (res != null) {
						if (res.isLoaded()) {
							res.unload();
						}
						res.load(resourceSet.getLoadOptions());
						final TreeViewer viewer = (TreeViewer) _viewer;
						WidgetUtils.runInDisplayThread(viewer.getControl(), new Runnable() {
							@Override
							public void run() {
								TreePath[] treePaths = viewer.getExpandedTreePaths();
								viewer.refresh();
								viewer.setExpandedTreePaths(treePaths);
							}
						});
					}
				}
			} catch (Exception e) {
				LogUtil.error("Error reloading resource", e);
			}	
			return false;
		}
		return true;
	}
}
