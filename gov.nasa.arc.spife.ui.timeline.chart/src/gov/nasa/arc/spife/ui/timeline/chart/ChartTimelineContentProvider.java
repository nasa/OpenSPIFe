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
/**
 * 
 */
package gov.nasa.arc.spife.ui.timeline.chart;

import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.chart.model.provider.ChartItemProviderAdapterFactory;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

public class ChartTimelineContentProvider extends TreeTimelineContentProvider {
	
	private final INotifyChangedListener listener = new TreeViewerNotifyChangedListener();
		
	private final AdapterFactory adapterFactory = new ChartItemProviderAdapterFactory();
	
	public ChartTimelineContentProvider() {
		// nothing to do
	}

	@Override
	public void activate() {
		if (this.adapterFactory instanceof IChangeNotifier) {
			((IChangeNotifier) this.adapterFactory).addListener(listener);
		}
	}
	
	@Override
	public void dispose() {
		if (this.adapterFactory instanceof IChangeNotifier) {
			((IChangeNotifier) this.adapterFactory).removeListener(listener);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Collection<?> getChildren(Object object) {
		ITreeItemContentProvider cp = (ITreeItemContentProvider) this.adapterFactory.adapt(object, ITreeItemContentProvider.class);
		return cp == null ? Collections.emptyList() : cp.getChildren(object);
	}
	
	@Override
	public Object getParent(Object object) {
		ITreeItemContentProvider cp = (ITreeItemContentProvider) this.adapterFactory.adapt(object, ITreeItemContentProvider.class);
		return cp == null ? null : cp.getParent(object);
	}

}
