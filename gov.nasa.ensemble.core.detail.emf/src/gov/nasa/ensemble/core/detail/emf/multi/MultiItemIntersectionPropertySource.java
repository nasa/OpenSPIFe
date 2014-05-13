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
package gov.nasa.ensemble.core.detail.emf.multi;

import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor.PropertyValueWrapper;

public class MultiItemIntersectionPropertySource extends AdapterImpl implements MultiItemPropertySource {

	private final List<IItemPropertyDescriptor> filteredItemPropertyDescriptors = new ArrayList<IItemPropertyDescriptor>();
	private final Map<Object, IItemPropertyDescriptor> pdsByID = new LinkedHashMap<Object, IItemPropertyDescriptor>();
	
	public MultiItemIntersectionPropertySource(List<? extends EObject> eObjects) {
		Map<IItemPropertyDescriptor, List<EObject>> pdsMap = new LinkedHashMap<IItemPropertyDescriptor, List<EObject>>();
		EObject eObject = eObjects.get(0);
		IItemPropertySource source = EMFUtils.adapt(eObject, IItemPropertySource.class);
		if (source != null) {
			List<IItemPropertyDescriptor> pds = getDescriptors(eObject, source);
			for (IItemPropertyDescriptor pd : pds) {
				boolean allCompatible = true;
				List<IItemPropertyDescriptor> compatible = new ArrayList<IItemPropertyDescriptor>();
				for (Iterator<? extends EObject> i=eObjects.iterator(); i.hasNext() && allCompatible; ) {
					EObject eObject2 = i.next();
					if (eObject2 == eObject) {
						continue;
					}
					
					boolean isCompatible = false;
					IItemPropertySource source2 = EMFUtils.adapt(eObject2, IItemPropertySource.class);
					if (source2 != null) {
						List<IItemPropertyDescriptor> pds2 = getDescriptors(eObject2, source2);
						for (Iterator<IItemPropertyDescriptor> i2 = pds2.iterator(); i2.hasNext() && !isCompatible; ) {
							IItemPropertyDescriptor pd2 = i2.next();
							isCompatible = pd == pd2;
							if (!isCompatible) {
								EObject o1 = getCommandOwner(pd, eObject);
								EObject o2 = getCommandOwner(pd2, eObject2);
								isCompatible = pd.isCompatibleWith(o1, o2, pd2);
							}	
							
							if (isCompatible) {
								compatible.add(pd2);
								addPropertyDescriptorEntry(pdsMap, pd2, eObject2);
								break;
							}
						}
					}
					allCompatible = allCompatible && isCompatible;
				}
				if (allCompatible) {
					compatible.add(0, pd);
					addPropertyDescriptorEntry(pdsMap, pd, eObject);
					MultiItemPropertyDescriptor thisPD = new MultiItemPropertyDescriptor(compatible, pdsMap);
					pdsByID.put(thisPD.getFeature(null), thisPD);
					filteredItemPropertyDescriptors.add(thisPD);
				}
			}
		}
	}

	private void addPropertyDescriptorEntry(
			Map<IItemPropertyDescriptor, List<EObject>> pdsMap,
			IItemPropertyDescriptor pd,
			EObject eObject) {
		List<EObject> list = pdsMap.get(pd);
		if (list == null) {
			list = new ArrayList<EObject>();
			pdsMap.put(pd, list);
		}
		list.add(eObject);
	}

	private EObject getCommandOwner(IItemPropertyDescriptor pd, EObject sourceObject) {
		EObject owner = EMFDetailUtils.getCommandOwner(pd, sourceObject);
		if (owner == null) {
			owner = sourceObject;
		}
		return owner;
	}
	
	private List<IItemPropertyDescriptor> getDescriptors(EObject eObject, IItemPropertySource source) {
		List<IItemPropertyDescriptor> list = new ArrayList<IItemPropertyDescriptor>();
		unwrapPropertyDescriptors(eObject, list, source.getPropertyDescriptors(eObject));
		return list;
	}

	private void unwrapPropertyDescriptors(Object target, List<IItemPropertyDescriptor> list, List<IItemPropertyDescriptor> pds) {
		for (IItemPropertyDescriptor pd : pds) {
			Object value = pd.getPropertyValue(target);
			if (value instanceof PropertyValueWrapper) {
				PropertyValueWrapper wrapper = (PropertyValueWrapper)value;
				List<IItemPropertyDescriptor> unwrappedPropertyDescriptors = wrapper.getPropertyDescriptors(value);
				if (!unwrappedPropertyDescriptors.isEmpty()) {
					unwrapPropertyDescriptors(value, list, unwrappedPropertyDescriptors);
				}
			}
			list.add(pd);
		}
	}

	@Override
	public Object getEditableValue(Object object) {
		return null;
	}

	@Override
	public IItemPropertyDescriptor getPropertyDescriptor(Object object, Object propertyID) {
		IItemPropertyDescriptor cached = pdsByID.get(propertyID);
		if (cached != null) {
			return cached;
		}
		
		for (IItemPropertyDescriptor pd : getPropertyDescriptors(object)) {
			if (pd.getFeature(object) == propertyID) {
				pdsByID.put(propertyID, pd);
				return pd;
			}
		}
		return null;
	}

	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		return filteredItemPropertyDescriptors;
	}

}
