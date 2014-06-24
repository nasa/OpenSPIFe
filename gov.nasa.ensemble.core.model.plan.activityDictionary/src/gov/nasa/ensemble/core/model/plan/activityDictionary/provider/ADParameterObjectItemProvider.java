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
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package gov.nasa.ensemble.core.model.plan.activityDictionary.provider;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.provider.EMemberItemPropertyDescriptor;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.core.model.plan.util.PlanElementApprover;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EActivityGroupDef;
import gov.nasa.ensemble.dictionary.EAttributeParameter;
import gov.nasa.ensemble.dictionary.EChoice;
import gov.nasa.ensemble.dictionary.EParameterDef;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptorDecorator;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;

/**
 * This is the item provider adapter for a {@link gov.nasa.ensemble.core.model.plan.activityDictionary.ADParameterMember} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated NOT
 */
public class ADParameterObjectItemProvider extends ReflectiveItemProvider {

	private static final ParameterDescriptor PARAMETER_DESCRIPTOR = ParameterDescriptor.getInstance();

	private List<IItemPropertyDescriptor> itemPropertyDescriptors = null;
	
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ADParameterObjectItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public String getText(Object object) {
		IItemLabelProvider lp = EMFUtils.adapt(object, IItemLabelProvider.class);
		if (lp != null) {
			return lp.getText(object);
		}
		return "Parameters";
	}
	
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ADParameterObject.gif"));
	}

	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(final Object o) {
		if (itemPropertyDescriptors == null) {
			itemPropertyDescriptors = new ArrayList<IItemPropertyDescriptor>();
			EObject object = (EObject)o;
			EClass klass = object.eClass();
			if (klass instanceof EActivityDef) {
				// We want to add some property descriptors from the ActivityDef, so we use a Decorator here
				final EActivityDef activityDef = (EActivityDef) klass;
				IItemPropertySource activityDefPropertySource = (IItemPropertySource) adapterFactory.adapt(activityDef, IItemPropertySource.class);
				IItemPropertyDescriptor typePD = activityDefPropertySource.getPropertyDescriptor(activityDef, EcorePackage.Literals.ENAMED_ELEMENT__NAME);
				if (typePD != null) {
					itemPropertyDescriptors.add(new ItemPropertyDescriptorDecorator(activityDef, typePD) {
						@Override public String getCategory(Object thisObject) { return null; }
						@Override public String getDisplayName(Object thisObject) { return "Type"; }
						@Override public boolean canSetProperty(Object thisObject) { return false; }
					});
				}
				IItemPropertyDescriptor categoryPD = activityDefPropertySource.getPropertyDescriptor(activityDef, DictionaryPackage.Literals.EACTIVITY_DEF__CATEGORY);
				if (categoryPD != null) {
					itemPropertyDescriptors.add(new ItemPropertyDescriptorDecorator(activityDef, categoryPD) {
						@Override public String getCategory(Object thisObject) { return null; }
						@Override public String getDisplayName(Object thisObject) { return "Category"; }
						@Override public boolean canSetProperty(Object thisObject) { return false; }
					});
				}
			}
			itemPropertyDescriptors.addAll(getPropertyDescriptors(klass));
			for (EClass type : klass.getEAllSuperTypes()) {
				itemPropertyDescriptors.addAll(getPropertyDescriptors(type));
			}
			sortPropertyDescriptors(o, itemPropertyDescriptors);
		}
		return itemPropertyDescriptors;	
	}

	/**
	 * Sort based on the category name
	 * @param o object being instacted
	 * @param itemPropertyDescriptors to sort
	 */
	private void sortPropertyDescriptors(final Object o, List<IItemPropertyDescriptor> itemPropertyDescriptors) {
		Collections.sort(itemPropertyDescriptors, new Comparator<IItemPropertyDescriptor>() {
			@Override
			public int compare(IItemPropertyDescriptor pd1, IItemPropertyDescriptor pd2) {
				String c1 = pd1.getCategory(o);
				String c2 = pd2.getCategory(o);
				if (c1 == null && c2 == null) {
					return 0;
				} else if (c1 == null) {
					return -1;
				} else if (c2 == null) {
					return 1;
				}
				return c1.compareTo(c2);
			}
		});
	}

	private List<ItemPropertyDescriptor> getPropertyDescriptors(EClass type) {
		List<ItemPropertyDescriptor> pds = new ArrayList<ItemPropertyDescriptor>();
		for (EStructuralFeature feature : type.getEStructuralFeatures()) {
			try {
				EMemberItemPropertyDescriptor propertyDescriptor = getPropertyDescriptor(feature);
				if (propertyDescriptor != null) {
					pds.add(propertyDescriptor);
				}
			} catch (Exception e) {
				LogUtil.error(e);
			}
		}
		return pds;
	}

	private EMemberItemPropertyDescriptor getPropertyDescriptor(EStructuralFeature feature) {
	    if (PlanPackage.Literals.EMEMBER__PLAN_ELEMENT == feature
    		|| PlanPackage.Literals.EMEMBER__KEY == feature
    		|| !PARAMETER_DESCRIPTOR.isVisible(feature)) {
			 return null;
	    }
	    boolean isEditable = !feature.isDerived() && feature.isChangeable() && PARAMETER_DESCRIPTOR.isEditable(feature);
	    String displayName = PARAMETER_DESCRIPTOR.getDisplayName(feature);
	    if (displayName == null) {
	    	displayName = getFeatureText(feature);
	    }
	    String category = PARAMETER_DESCRIPTOR.getCategory(feature);
	    if (category == null || category.trim().length() == 0) {
	    	if (feature.getEContainingClass() instanceof EActivityDef
	    			|| feature.getEContainingClass() instanceof EActivityGroupDef) { 
	    		String featureName = feature.getName();
	    		if (ActivityDictionary.getInstance().getAttributeDef(featureName) == null) {
	    			category = "Parameters";
	    		} else {
	    			category = "Attributes";
	    		}
	    	}
	    }
		boolean isMulitline = PARAMETER_DESCRIPTOR.isMultiline(feature);
		AdapterFactory factory = adapterFactory;
		if (adapterFactory instanceof ComposeableAdapterFactory) {
			factory = ((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory();
		}
	    final Collection comboBoxObjects = getComboBoxObjects(feature);
	    String description = null;
	    if (feature instanceof EParameterDef) {
	    	description = ((EParameterDef)feature).getDescription();
	    }
	    String[] filterFlags = PARAMETER_DESCRIPTOR.getFilterFlags(feature);
	    
	    return new ADParameterObjectItemPropertyDescritpor(
				factory, getResourceLocator(),
				displayName, 
				description, 
				feature, 
				isEditable, 
				isMulitline, 
				false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, 
				category, 
				filterFlags, 
				comboBoxObjects);
    }

	private Collection<Object> getComboBoxObjects(EStructuralFeature feature) {
	    Collection<Object> comboBoxObjects = null;
	    if (feature instanceof EAttributeParameter) {
	    	boolean allValues = true;
	    	List<String> choiceValues = new ArrayList<String>();
	    	EAttributeParameter pDef = (EAttributeParameter) feature;
	    	EDataType eDataType = (EDataType) pDef.getEType();
	    	if (eDataType == null) {
	    		LogUtil.error("null EDataType for parameter: " + pDef.getName());
	    		return null;
	    	}
	    	EPackage ePackage = eDataType.getEPackage();
			EFactory eFactory = ePackage.getEFactoryInstance();
	    	for (EChoice choice : pDef.getChoices()) {
	    		String choiceValue = choice.getValue();
	    		if (choiceValue != null) {
	    			choiceValues.add(choiceValue);
	    		} else {
	    			allValues = false;
	    			break;
	    		}
	    	}
	    	if (allValues && !choiceValues.isEmpty()) {
	    		comboBoxObjects = new ArrayList<Object>();
	    		for (String valueLiteral : choiceValues) {
	    			Object value = eFactory.createFromString(eDataType, valueLiteral);
	    			comboBoxObjects.add(value);
	    		}
	    	}
	    }
	    return comboBoxObjects;
    }
	
	@Override
	public ResourceLocator getResourceLocator() {
		return ActivityDictionaryPlanningEditPlugin.INSTANCE;
	}
	
	private static final class ADParameterObjectItemPropertyDescritpor extends EMemberItemPropertyDescriptor {
		
		private final Collection comboBoxObjects;

		private ADParameterObjectItemPropertyDescritpor(
				AdapterFactory adapterFactory, ResourceLocator resourceLocator,
				String displayName, String description,
				EStructuralFeature feature, boolean isSettable,
				boolean multiLine, boolean sortChoices, Object staticImage,
				String category, String[] filterFlags,
				Collection comboBoxObjects) {
			super(adapterFactory, resourceLocator, displayName, description,
					feature, isSettable, multiLine, sortChoices, staticImage,
					category, filterFlags);
			this.comboBoxObjects = comboBoxObjects;
		}

		@Override
		protected Collection<?> getComboBoxObjects(Object object) {
			if (comboBoxObjects != null) {
				return comboBoxObjects;
			}
			// the following if clause is modeled after ItemPropertyDescriptor.getComboBoxObjects
			// but it should be significantly faster due to caching and precaching we do in EMFUtils. 
			if (object instanceof EObject) {
				if (feature instanceof EReference) {
					Collection<EObject> result = EMFUtils.getReachableObjectsOfType((EObject)object, feature.getEType());
					if (!feature.isMany() && !result.contains(null)) {
						result.add(null);
					}
					return result;
				}
			}
			return super.getComboBoxObjects(object);
		}

		@Override
		public boolean canSetProperty(Object object) {
			boolean isSubActivity = false;
			boolean isApproved = true;
			if (object instanceof EObject) {
				EObject container = ((EObject)object).eContainer();
				isSubActivity = container instanceof EActivity && ((EActivity)container).isIsSubActivity();
				if (container instanceof EPlanElement) {
					final EPlanElement planElement = (EPlanElement) container;
					for (PlanElementApprover approver : ClassRegistry.createInstances(PlanElementApprover.class)) {
						isApproved = isApproved && approver.canEdit(planElement);
					}
				}
			}
			return !isSubActivity && isApproved && super.canSetProperty(object);
		}
	}
	
}
