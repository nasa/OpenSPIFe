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
package gov.nasa.arc.spife.ui.timeline.service;

import gov.nasa.arc.spife.ui.timeline.TimelineService;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;

public class WorkspaceResourceService extends TimelineService implements IResourceChangeListener {
	private List<IResourceChangeListener> listeners;
	
	public WorkspaceResourceService() {
		 listeners = new ArrayList<IResourceChangeListener>();
	}
			
	@Override
	public void activate() {
		super.activate();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		
	}

	@Override
	public void deactivate() {
		listeners.clear();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.deactivate();
	}

	public void addResourceChangeListener(IResourceChangeListener listener) {
		listeners.add(listener);
	}
	
	public void removeResourceChangeListener(IResourceChangeListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		for(IResourceChangeListener listener : listeners) {
			listener.resourceChanged(event);
		}
	}	
}
