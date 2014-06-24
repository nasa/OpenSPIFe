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
package gov.nasa.ensemble.core.model.plan.provider;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.emf.provider.CommandDelegate;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;

public abstract class PlanElementDelegatingWrapperItemProvider extends DelegatingWrapperItemProvider {

	public PlanElementDelegatingWrapperItemProvider(Object value, Object owner, AdapterFactory adapterFactory) {
		super(value, owner, null, CommandParameter.NO_INDEX, adapterFactory);
	}
	
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (propertyDescriptors == null) {
			if (delegateItemProvider instanceof IItemPropertySource) {
				List<IItemPropertyDescriptor> l = ((IItemPropertySource) delegateItemProvider).getPropertyDescriptors(getDelegateObject(object));
				propertyDescriptors = new ArrayList<IItemPropertyDescriptor>(l.size());
				String csv = getProperty((EPlanElement) object, "readOnly");
				List<String> readOnlyList = null;
				if (csv != null) {
					readOnlyList = Arrays.asList(CommonUtils.COMMA_PATTERN.split(csv));
				}
				for (IItemPropertyDescriptor pd : l) {
					boolean editable = readOnlyList == null || !isReadOnly(pd, readOnlyList);
					propertyDescriptors.add(new PlanElementDelegatingPropertyDescriptor(pd, editable));
				}
			} else {
				propertyDescriptors = Collections.emptyList();
			}
		}
		return propertyDescriptors;
	}

	protected abstract Object getDelegateObject(Object thisObject);
	
	private boolean isReadOnly(IItemPropertyDescriptor pd, List<String> readOnlyList) {
		String category = pd.getCategory(getDelegateValue());
		String displayName = pd.getDisplayName(getOwner());
		return readOnlyList.contains(category) || readOnlyList.contains(displayName);
	}
	
	private static String getProperty(EPlanElement pe, String key) {
		String csv = null;
		EObject data = pe.getData();
		if (data != null) {
			EClass dClass = data.eClass();
			csv = EMFUtils.getAnnotation(dClass, ParameterDescriptor.ANNOTATION_SOURCE, key);
		}
		if (csv == null || csv.trim().length() == 0) {
			csv = EMFUtils.getAnnotation(pe.eClass(), ParameterDescriptor.ANNOTATION_SOURCE, key);
		}
		if (csv == null && !(pe instanceof EPlan)) {
			for (EObject o : pe.eContents()) {
				if (o instanceof EActivity || o instanceof EActivityGroup) {
					csv = getProperty((EPlanElement)o, key);
				}
				
				if (csv != null) {
					return csv;
				}
			}
		}
		return csv;
	}
	
	private class PlanElementDelegatingPropertyDescriptor extends DelegatingWrapperItemPropertyDescriptor implements CommandDelegate {

		private final boolean editable;
		
		public PlanElementDelegatingPropertyDescriptor(IItemPropertyDescriptor itemPropertyDescriptor, boolean editable) {
			super(null, itemPropertyDescriptor);
			this.editable = editable;
		}

		@Override
		public EObject getCommandOwner(Object target) {
			return (EObject) getDelegateObject(target);
		}

		@Override
		public void setCommandOwner(Object commandOwner) {
			super.setCommandOwner(commandOwner);
		}

		@Override
		public Object getCommandOwner() {
			return super.getCommandOwner();
		}
		
		/**
		 * This returns the group of properties into which this one should be
		 * placed.
		 */
		@Override
		public String getCategory(Object thisObject) {
			return itemPropertyDescriptor.getCategory(getDelegateObject(thisObject));
		}

		/**
		 * This returns the description to be displayed in the property sheet
		 * when this property is selected.
		 */
		@Override
		public String getDescription(Object thisObject) {
			return itemPropertyDescriptor.getDescription(getDelegateObject(thisObject));
		}

		/**
		 * This returns the name of the property to be displayed in the property
		 * sheet.
		 */
		@Override
		public String getDisplayName(Object thisObject) {
			return itemPropertyDescriptor.getDisplayName(getDelegateObject(thisObject));
		}

		/**
		 * This returns the flags used as filters in the property sheet.
		 */
		@Override
		public String[] getFilterFlags(Object thisObject) {
			return itemPropertyDescriptor.getFilterFlags(getDelegateObject(thisObject));
		}

		/**
		 * This returns the interface name of this property. This is the key
		 * that is passed around and must uniquely identify this descriptor.
		 */
		@Override
		public String getId(Object thisObject) {
			return itemPropertyDescriptor.getId(getDelegateObject(thisObject));
		}

		@Override
		public Object getHelpContextIds(Object thisObject) {
			return itemPropertyDescriptor.getHelpContextIds(getDelegateObject(thisObject));
		}

		/**
		 * This does the delegated job of getting the label provider for the
		 * given object
		 */
		@Override
		public IItemLabelProvider getLabelProvider(Object thisObject) {
			return itemPropertyDescriptor.getLabelProvider(getDelegateObject(thisObject));
		}

		/**
		 * This indicates whether these two property descriptors are equal. It's
		 * not really clear to me how this is meant to be used, but it's a
		 * little bit like an equals test.
		 */
		@Override
		public boolean isCompatibleWith(Object object, Object anotherObject, IItemPropertyDescriptor anotherItemPropertyDescriptor) {
			return itemPropertyDescriptor.isCompatibleWith(object, anotherObject, anotherItemPropertyDescriptor);
		}

		/**
		 * This does the delegated job of getting the property value from the
		 * given object
		 */
		@Override
		public Object getPropertyValue(Object thisObject) {
			return itemPropertyDescriptor.getPropertyValue(getDelegateObject(thisObject));
		}

		/**
		 * This does the delegated job of determining whether the property value
		 * from the given object is set.
		 */
		@Override
		public boolean isPropertySet(Object thisObject) {
			return itemPropertyDescriptor.isPropertySet(getDelegateObject(thisObject));
		}

		/**
		 * This does the delegated job of determining whether the property value
		 * from the given object supports set (and reset).
		 */
		@Override
		public boolean canSetProperty(Object thisObject) {
			if (!editable) {
				return false;
			}
			return itemPropertyDescriptor.canSetProperty(getDelegateObject(thisObject));
		}

		/**
		 * This does the delegated job of resetting property value back to it's
		 * default value.
		 */
		@Override
		public void resetPropertyValue(Object thisObject) {
			itemPropertyDescriptor.resetPropertyValue(getDelegateObject(thisObject));
		}

		/**
		 * This does the delegated job of setting the property to the given
		 * value.
		 */
		@Override
		public void setPropertyValue(Object thisObject, Object value) {
			itemPropertyDescriptor.setPropertyValue(getDelegateObject(thisObject), value);
		}

		@Override
		public Object getFeature(Object thisObject) {
			return itemPropertyDescriptor.getFeature(getDelegateObject(thisObject));
		}

		@Override
		public Collection<?> getChoiceOfValues(Object thisObject) {
			return itemPropertyDescriptor.getChoiceOfValues(getDelegateObject(thisObject));
		}

		/**
		 * This does the delegated job of determining whether the property
		 * represents multiple values.
		 */
		@Override
		public boolean isMany(Object thisObject) {
			return itemPropertyDescriptor.isMany(thisObject);
		}

		/**
		 * This does the delegated job of determining whether the property's
		 * value consists of multi-line text.
		 * 
		 * @since 2.2.0
		 */
		@Override
		public boolean isMultiLine(Object object) {
			return itemPropertyDescriptor.isMultiLine(object);
		}

		/**
		 * This does the delegated job of determining the choices for this
		 * property should be sorted for display.
		 * 
		 * @since 2.2.0
		 */
		@Override
		public boolean isSortChoices(Object object) {
			return itemPropertyDescriptor.isSortChoices(object);
		}

	}
	
}
