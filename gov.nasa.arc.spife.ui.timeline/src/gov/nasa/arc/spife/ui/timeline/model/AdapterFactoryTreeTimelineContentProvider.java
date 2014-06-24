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
package gov.nasa.arc.spife.ui.timeline.model;

import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;

public class AdapterFactoryTreeTimelineContentProvider extends TreeTimelineContentProvider {

	private final INotifyChangedListener listener = new TreeViewerNotifyChangedListener();
	
	private final AdapterFactoryContentProvider provider;
	
	public AdapterFactoryTreeTimelineContentProvider() {
		this(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE));
	}
	
	public AdapterFactoryTreeTimelineContentProvider(AdapterFactory adapterFactory) {
		this.provider = new AdapterFactoryContentProvider(adapterFactory);
		if (adapterFactory instanceof IChangeNotifier) {
			((IChangeNotifier)adapterFactory).addListener(listener);
		}
	}

	@Override
	public Collection<?> getChildren(Object object) {
		return Arrays.asList(provider.getChildren(object));
	}

	@Override
	public Object getParent(Object object) {
		if (object instanceof Resource 
				|| !(object instanceof EObject)
				|| ((EObject)object).eContainer() instanceof Resource) {
			return null;
		}
		return provider.getParent(object);
	}

}
