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
package gov.nasa.ensemble.core.model.plan.activityDictionary.provider;

import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

public class DynamicDictonaryAdapterFactory extends AdapterFactoryImpl {

	protected Collection<Object> supportedTypes = new ArrayList<Object>();
	private Map<EClass, ADParameterObjectItemProvider> map = new HashMap<EClass, ADParameterObjectItemProvider>();
	
	public DynamicDictonaryAdapterFactory() {
		super();
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	@Override
	public boolean isFactoryForType(Object object) {
		return 
			object instanceof EActivityDictionary
			
			|| (object instanceof EObject
					&& ((EObject)object).eClass().getEPackage() instanceof EActivityDictionary)
					
			|| (object instanceof EObject 
					&& ((EObject)object).eClass() instanceof EActivityDef)
					
			|| supportedTypes.contains(object);
	}
	
	@Override
	public Adapter adapt(Notifier target, Object type) {
		EClass eClass = ((EObject)target).eClass();
		ADParameterObjectItemProvider provider = map.get(eClass);
		if (provider == null) {
			provider = new ADParameterObjectItemProvider(this);
			map.put(eClass, provider);
		}
		return provider;
	}
	
}
