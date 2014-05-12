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
package gov.nasa.ensemble.core.model.plan.provider;

import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.common.ui.color.ColorUtils;
import gov.nasa.ensemble.common.ui.mission.MissionUIConstants;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITableItemColorProvider;
import org.eclipse.emf.edit.provider.ITableItemFontProvider;
import org.eclipse.emf.edit.provider.ITableItemLabelProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.swt.graphics.RGB;

/**
 * This is the item provider adapter for a {@link gov.nasa.ensemble.core.model.plan.EActivity} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EActivityItemProvider
	extends EPlanChildItemProvider
	implements	
		IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, ITableItemLabelProvider, ITableItemColorProvider, ITableItemFontProvider, IItemColorProvider, IItemFontProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EActivityItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			getPropertyDescriptorsGen(object);
			sortPropertyDescriptors(object, itemPropertyDescriptors);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<IItemPropertyDescriptor> getPropertyDescriptorsGen(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addChildrenPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Children feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addChildrenPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EActivity_children_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EActivity_children_feature", "_UI_EActivity_type"),
				 PlanPackage.Literals.EACTIVITY__CHILDREN,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(PlanPackage.Literals.EACTIVITY__CHILDREN);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		EActivityDef activityDef = ActivityDictionary.getInstance().getActivityDef(((EActivity)object).getType());
		if (activityDef != null) {
			String activityDefName  = activityDef.getCategory();
			try {
				MissionUIConstants lookupMissionExtender = MissionExtender.construct(MissionUIConstants.class);
				Object icon = lookupMissionExtender.getIcon(activityDefName);
			if (icon != null)
				return icon;
			} catch (ConstructionException e) {
				//Do nothing and use the EMF one below...
			}
		}
		RGB rgb = super.getBackground(object);
		if (rgb == null)
			rgb = new RGB(125,125,125);
				
		return ColorUtils.isBrightColor(rgb) ? overlayImage(object, getResourceLocator().getImage("full/obj16/EActivity_gray.png")) : overlayImage(object, getResourceLocator().getImage("full/obj16/EActivity.png"));	
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * Delegate to super
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		return super.getText(object);
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(EActivity.class)) {
			case PlanPackage.EACTIVITY__IS_SUB_ACTIVITY:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case PlanPackage.EACTIVITY__CHILDREN:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(PlanPackage.Literals.EACTIVITY__CHILDREN,
				 PlanFactory.eINSTANCE.createEActivity()));
	}
	
	@Override
	protected ItemPropertyDescriptor createItemPropertyDescriptor(
        AdapterFactory adapterFactory,
	    ResourceLocator resourceLocator,
	    String displayName,
	    String description,
	    EStructuralFeature feature,
	    boolean isSettable,
	    boolean multiLine,
	    boolean sortChoices,
	    Object staticImage,
	    String category,
	    String[] filterFlags)
	{
	    return new ItemPropertyDescriptor(
	        adapterFactory,
	        resourceLocator,
		    displayName,
		    description,
		    feature,
		    isSettable,
		    multiLine,
		    sortChoices,
		    staticImage,
		    category,
		    filterFlags) {

				@Override
				public boolean isCompatibleWith(Object object, Object anotherObject, IItemPropertyDescriptor propertyDescriptor) {
					if (propertyDescriptor == this) {
				      return true;
				    } else if (propertyDescriptor instanceof ItemPropertyDescriptor) {
				      ItemPropertyDescriptor itemPropertyDescriptor = (ItemPropertyDescriptor)propertyDescriptor;
				      EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(anotherObject);
						if (domain instanceof AdapterFactoryEditingDomain) {
							AdapterFactoryEditingDomain aDomain = (AdapterFactoryEditingDomain) domain;
							AdapterFactory adapterFactory = aDomain.getAdapterFactory();
							if (this.adapterFactory == adapterFactory &&
					            displayName.equals(itemPropertyDescriptor.getDisplayName(anotherObject)) &&
					            (category == null && itemPropertyDescriptor.getCategory(anotherObject) == null 
				            		|| category.equals(itemPropertyDescriptor.getCategory(anotherObject))))
					      {
					        return true;
					      }
						}
				    }
					return false;	
				}
				
				@Override
				public boolean canSetProperty(Object object) {
					boolean canSet = super.canSetProperty(object);
					if (canSet && (object instanceof EObject)) {
						EObject eObject = (EObject) object;
						if (eObject.eContainer() == null || eObject.eContainer().eContainer() == null)
						   return canSet;
						return (!eObject.eContainer().getClass().equals(eObject.getClass()));
					}
					return canSet;
				}
	    	
	    };
	  }
}
