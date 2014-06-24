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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

public class MultiItemPropertyDescriptor implements IItemPropertyDescriptor, IAdaptable {

	private final Map<IItemPropertyDescriptor, List<EObject>> pdsMap;
	private final List<IItemPropertyDescriptor> pds;

	private final IItemPropertyDescriptor primaryDescriptor;
	private final EObject primaryObject;
	
	public MultiItemPropertyDescriptor(List<IItemPropertyDescriptor> compatible, Map<IItemPropertyDescriptor, List<EObject>> pdsMap) {
		this.pds = compatible;
		this.pdsMap = pdsMap;
		this.primaryDescriptor = compatible.get(0);
		this.primaryObject = pdsMap.get(primaryDescriptor).get(0);
	}
	
	public EPackage getEPackage() {
		EObject object = primaryObject;
		EObject commandOwner = EMFDetailUtils.getCommandOwner(primaryDescriptor, object);
		if (commandOwner != null) {
			object = commandOwner;
		}
		return object == null ? null : object.eClass().getEPackage();
	}
	
	public List<IItemPropertyDescriptor> getPropertyDescriptors() {
		return pds;
	}

	public List<EObject> getCommandOwners() {
		List<EObject> objects = new ArrayList<EObject>();
		for (IItemPropertyDescriptor pd : pds) {
			List<EObject> list = pdsMap.get(pd);
			for (EObject eObject : list) {
				Object owner = EMFDetailUtils.getCommandOwner(pd, eObject);
				if (owner instanceof EObject) {
					objects.add((EObject)owner);
				} else {
					objects.add(eObject);
				}
			}
		}
		return objects;
	}
	
	@Override
	public boolean canSetProperty(Object object) {
		return primaryDescriptor.canSetProperty(primaryObject);
	}

	@Override
	public String getCategory(Object object) {
		return primaryDescriptor.getCategory(primaryObject);
	}

	@Override
	public Collection<?> getChoiceOfValues(Object object) {
		return primaryDescriptor.getChoiceOfValues(primaryObject);
	}

	@Override
	public String getDescription(Object object) {
		return primaryDescriptor.getDescription(primaryObject);
	}

	@Override
	public String getDisplayName(Object object) {
		return primaryDescriptor.getDisplayName(primaryObject);
	}
	
	@Override
	public Object getFeature(Object object) {
		return primaryDescriptor.getFeature(primaryObject);
	}

	@Override
	public String[] getFilterFlags(Object object) {
		return primaryDescriptor.getFilterFlags(primaryObject);
	}

	@Override
	public Object getHelpContextIds(Object object) {
		return primaryDescriptor.getHelpContextIds(primaryObject);
	}

	@Override
	public String getId(Object object) {
		return primaryDescriptor.getId(primaryObject);
	}

	@Override
	public IItemLabelProvider getLabelProvider(Object object) {
		return primaryDescriptor.getLabelProvider(primaryObject);
	}

	@Override
	public Object getPropertyValue(Object object) {
		boolean valueInitialized = false;
		Object value = null;
		for (IItemPropertyDescriptor pd : pds) {
			for (EObject eObject : pdsMap.get(pd)) {
				Object pdValue = getValue(eObject, pd);
				if (!valueInitialized) {
					value = pdValue;
					valueInitialized = true;
				} else {
					Object value2 = pdValue;
					if (!CommonUtils.equals(value, value2)) {
						return null;
					}
				}
			}
		}
		return value;
	}
	
	public Vector<Object> getValues() {
		Vector<Object> values = new Vector<Object>();
		for (IItemPropertyDescriptor pd : pds) {
			for (EObject eObject : pdsMap.get(pd)) {
				values.add(getValue(eObject, pd));
			}
		}
		return values;
	}
	
	private Object getValue(EObject eObject, IItemPropertyDescriptor pd) {
		return EMFUtils.getPropertyValue(pd, eObject);
	}

	@Override
	public boolean isCompatibleWith(Object object, Object anotherObject, IItemPropertyDescriptor anotherPropertyDescriptor) {
		return false;//primaryDescriptor.isCompatibleWith(object, anotherObject, anotherPropertyDescriptor);
	}

	@Override
	public boolean isMany(Object object) {
		return primaryDescriptor.isMany(primaryObject);
	}

	@Override
	public boolean isMultiLine(Object object) {
		return primaryDescriptor.isMultiLine(primaryObject);
	}

	@Override
	public boolean isPropertySet(Object object) {
		for (IItemPropertyDescriptor pd : pds) {
			for (EObject eObject : pdsMap.get(pd)) {
				if (pd.isPropertySet(eObject)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isSortChoices(Object object) {
		return primaryDescriptor.isSortChoices(primaryObject);
	}

	@Override
	public void resetPropertyValue(Object object) {
		for (IItemPropertyDescriptor pd : pds) {
			for (EObject eObject : pdsMap.get(pd)) {
				pd.resetPropertyValue(eObject);
			}
		}
	}

	@Override
	public void setPropertyValue(Object object, Object value) {
		for (IItemPropertyDescriptor pd : pds) {
			for (EObject eObject : pdsMap.get(pd)) {
				pd.setPropertyValue(eObject, value);
			}
		}
	}

	public void setValues(Collection<Object> oldValues) {
		Iterator<Object> i = oldValues.iterator();
		for (IItemPropertyDescriptor pd : pds) {
			for (EObject eObject : pdsMap.get(pd)) {
				pd.setPropertyValue(eObject, i.next());
			}
		}
	}

	public IItemPropertyDescriptor getPrimaryDescriptor() {
		return primaryDescriptor;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return CommonUtils.getAdapter(primaryDescriptor, adapter);
	}
	
	public EObject getPrimaryTarget() {
		return primaryObject;
	}
}
