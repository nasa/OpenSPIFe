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
package gov.nasa.ensemble.common.ui.ide.navigator;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.thread.ThreadUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;

import java.util.concurrent.ExecutorService;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class EnsembleCommonNavigatorContentProvider implements ITreeContentProvider, IResourceChangeListener {

	private static final Object[] NO_CHILDREN = new Object[0];
	private Viewer viewer;
	private final ExecutorService resourceChangeRefresher = ThreadUtils.newLastRequestThreadPool("Ensemble Navigator Resource Change Refresher");
	
	public EnsembleCommonNavigatorContentProvider() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.BaseWorkbenchContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object element) {
		return getChildren(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.model.BaseWorkbenchContentProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object element) {
		try {
			if(element instanceof IContainer) {
				IContainer container = (IContainer)element;
				if(container.isAccessible()) {
					return ((IContainer)element).members();
				}
			}
		} catch (Exception e) {
			LogUtil.error("fetching children of "+element, e);
		}
		return NO_CHILDREN;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.model.BaseWorkbenchContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		try {
			if (element instanceof IProject && !((IProject)element).isOpen()) {
				return false;
			}
			if (element instanceof IContainer) {
				return ((IContainer)element).members().length > 0;
			}
		} catch (Exception e) {
			LogUtil.error("fetching children of "+element, e);
		}
		return false;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { 
		this.viewer = viewer;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof IResource) {
			return ((IResource)element).getParent();
		}
		return null;
	}
	
	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		// Execute in a separate thread to isolate needed delay
		resourceChangeRefresher.execute(new Runnable() {
			@Override
			public void run() {
				// Some delay seems to be needed to get the display of markers refreshed
				ThreadUtils.sleep(200);
				if(!viewer.getControl().isDisposed()) {
					WidgetUtils.runInDisplayThread(viewer.getControl(), new Runnable() {
						@Override
						public void run() {
								viewer.refresh();
						}						
					});
				}
			}
		});
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
	}
}
