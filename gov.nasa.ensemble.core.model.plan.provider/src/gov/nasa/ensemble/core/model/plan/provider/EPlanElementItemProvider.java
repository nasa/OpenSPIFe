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


import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.color.ColorUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.IPlanElementMemberFactory;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.impl.EPlanElementImpl;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.emf.util.CompatibleItemPropertyDescriptor;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.CopyCommand.Helper;
import org.eclipse.emf.edit.command.InitializeCopyCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
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
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.swt.graphics.RGB;

/**
 * This is the item provider adapter for a {@link gov.nasa.ensemble.core.model.plan.EPlanElement} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EPlanElementItemProvider
	extends ItemProviderAdapter
	implements	
		IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, ITableItemLabelProvider, ITableItemColorProvider, ITableItemFontProvider, IItemColorProvider, IItemFontProvider {


	private final List<DelegatingWrapperItemProvider> wrapperItemProviders = new ArrayList<DelegatingWrapperItemProvider>();
	
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPlanElementItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * Create wrappers for member descriptors
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);
			EPlanElement element = (EPlanElement)object;
			addNamePropertyDescriptor(object);
			addMemberPropertyDescriptors(element, adapterFactory);
			addDataPropertyDescriptors(element, adapterFactory);
		}
		return itemPropertyDescriptors;
	}
	
	private void addMemberPropertyDescriptors(final EPlanElement pe, AdapterFactory factory) {
		for (IPlanElementMemberFactory f : IPlanElementMemberFactory.FACTORIES) {
			final String memberKey = f.getKey();
			try {
				EMember member = pe.getMember(memberKey);
				IItemPropertySource source = EMFUtils.adapt(member, IItemPropertySource.class);
				if (source != null) {
					addWrappedDescriptors(pe, member, new MemberDelegatingWrapperItemProvider(member, pe, factory, memberKey));
				}
			} catch (Exception e) {
				// ignore missing members
			}
		}
	}
	
	/**
	 * DelegatingWrapperItemProvider is overridden to customize DelegatingWrapperItemPropertyDescriptor in order to allow for
	 * editability canSetProperty customization via properties file entries
	 */
	private void addDataPropertyDescriptors(EPlanElement pe, AdapterFactory factory) {
		EObject data = pe.getData();
		IItemPropertySource source = EMFUtils.adapt(data, IItemPropertySource.class);
		if (source != null) {
			addWrappedDescriptors(pe, pe.getData(), new DataDelegatingWrapperItemProvider(pe.getData(), pe, factory));
		}
	}
	
	private void addWrappedDescriptors(EPlanElement element, Object reference, DelegatingWrapperItemProvider provider) {
	    wrapperItemProviders.add(provider);
	    for (IItemPropertyDescriptor pd : provider.getPropertyDescriptors(element)) {
	    	if (isVisible(element, reference, pd)) {
		    	itemPropertyDescriptors.add(pd);
	    	}
	    }
    }
	
	@Override
	public void dispose() {
	    super.dispose();
	    for (DelegatingWrapperItemProvider provider : wrapperItemProviders) {
	    	provider.dispose();
	    }
	    wrapperItemProviders.clear();
	}
	
	public void sortPropertyDescriptors(final Object object, final List<IItemPropertyDescriptor> pds) {
		// default instance does nothing
	}
	
	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addNamePropertyDescriptor(Object object) {
		String description = "The name of the " + EPlanUtils.getPlanElementTypeName(object);
		itemPropertyDescriptors.add
			(new CompatibleItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EPlanElement_name_feature"),
				 description,
				 PlanPackage.Literals.EPLAN_ELEMENT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null)
			{
				@Override
				public boolean canSetProperty(Object object) {
					if (object instanceof EPlanElement) {
						return EPlanUtils.canEdit((EPlanElement) object) && super.canSetProperty(object);
					}
					return super.canSetProperty(object);
				}
			}
		);
	}

	/**
	 * This adds a property descriptor for the Members feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMembersPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EPlanElement_members_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EPlanElement_members_feature", "_UI_EPlanElement_type"),
				 PlanPackage.Literals.EPLAN_ELEMENT__MEMBERS,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Data feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDataPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_EPlanElement_data_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_EPlanElement_data_feature", "_UI_EPlanElement_type"),
				 PlanPackage.Literals.EPLAN_ELEMENT__DATA,
				 false,
				 false,
				 false,
				 null,
				 getString("_UI_DataPropertyCategory"),
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(PlanPackage.Literals.EPLAN_ELEMENT__MEMBERS);
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
	 * This returns EPlanElement.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/EPlanElement"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * Return the name of the plan element
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String name = ((EPlanElement)object).getName();
		return (name != null ? name : "");
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

		switch (notification.getFeatureID(EPlanElement.class)) {
			case PlanPackage.EPLAN_ELEMENT__NAME:
			case PlanPackage.EPLAN_ELEMENT__PERSISTENT_ID:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case PlanPackage.EPLAN_ELEMENT__MEMBERS:
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
				(PlanPackage.Literals.EPLAN_ELEMENT__MEMBERS,
				 PlanFactory.eINSTANCE.createCommonMember()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return PlanningEditPlugin.INSTANCE;
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
		return new CompatibleItemPropertyDescriptor(
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
			filterFlags);
	}

	@Override
	public RGB getBackground(Object object) {
		EPlanElement ePlanElement = (EPlanElement) object;
		CommonMember commonMember = ePlanElement.getMember(CommonMember.class);
		if (commonMember != null) {
			RGB rgb = ColorUtils.getAsRGB(commonMember.getColor());
			if (rgb != null)
				return rgb;
		}
		return null;
	}

	private boolean isVisible(final EPlanElement pe, Object object, IItemPropertyDescriptor pd) {
		String csv = getProperty(pe, ParameterDescriptor.ANNOTATION_DETAIL_HIDDEN);
		if (csv == null ) {
			return true;
		}
		String category = pd.getCategory(object);
		List<String> values = Arrays.asList(CommonUtils.COMMA_PATTERN.split(csv));
		String displayName = pd.getDisplayName(pe);
		return (category == null || !values.contains(category)) && !values.contains(displayName);
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
				if (o instanceof EActivity)
					csv = getProperty((EActivity)o, key);
				else if (o instanceof EActivityGroup)
					csv = getProperty((EActivityGroup)o, key);
				
				if (csv != null)
					return csv;
			}
		}
		return csv;
	}
	
	@Override
	protected Command createInitializeCopyCommand(EditingDomain domain, EObject owner, Helper helper) {
		return new InitializeCopyCommand(domain, owner, helper) {

			@Override
			public void doExecute() {
				EPlanElement pe = (EPlanElement) getCopy();
				pe.eAdapters().remove(EPlanElementImpl.MEMBER_INVARIANT_ADAPTER);
				pe.getMembers().clear();
				super.doExecute();
			}
			
		};
	}
	
	@Override
	protected Command createCopyCommand(EditingDomain domain, EObject owner, final Helper helper) {
		return new CopyCommand(domain, owner, helper, domain.getOptimizeCopy()) {

			@Override
			public void execute() {
				super.execute();
				EObject copy = helper.getCopy(owner);
				copy.eAdapters().add(EPlanElementImpl.MEMBER_INVARIANT_ADAPTER);
			}
			
		};
	}

	private static final class DataDelegatingWrapperItemProvider extends PlanElementDelegatingWrapperItemProvider {
		private DataDelegatingWrapperItemProvider(Object value, Object owner, AdapterFactory adapterFactory) {
			super(value, owner, adapterFactory);
		}

		@Override
		protected Object getDelegateObject(Object thisObject) {
			if (thisObject instanceof EPlanElement) {
				return ((EPlanElement) thisObject).getData();
			}
			return thisObject;
		}
	}

	private static final class MemberDelegatingWrapperItemProvider extends PlanElementDelegatingWrapperItemProvider {
		
		private final String memberKey;

		private MemberDelegatingWrapperItemProvider(Object value, Object owner, AdapterFactory adapterFactory, String memberKey) {
			super(value, owner, adapterFactory);
			this.memberKey = memberKey;
		}

		@Override
		protected Object getDelegateObject(Object thisObject) {
			if (thisObject instanceof EPlanElement) {
				return ((EPlanElement) thisObject).getMember(memberKey);
			}
			return thisObject;
		}
	}
	
}
