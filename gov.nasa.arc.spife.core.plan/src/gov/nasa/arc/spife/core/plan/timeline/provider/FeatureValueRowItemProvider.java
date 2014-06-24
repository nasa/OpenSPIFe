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
package gov.nasa.arc.spife.core.plan.timeline.provider;


import gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class FeatureValueRowItemProvider
	extends PlanSectionRowItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureValueRowItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addFeatureNamePropertyDescriptor(object);
			addValueLiteralPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Feature Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addFeatureNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new ItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_FeatureValueRow_featureName_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_FeatureValueRow_featureName_feature", "_UI_FeatureValueRow_type"),
				 PlanTimelinePackage.Literals.FEATURE_VALUE_ROW__FEATURE_NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null) {
				
					@Override
					protected Collection<?> getComboBoxObjects(Object object) {
						Set<String> featureNames = new HashSet<String>();
						for (EStructuralFeature f : ActivityDictionary.getInstance().getAttributeDefs()) {
							featureNames.add(f.getName());
						}
						for (EActivityDef def : ActivityDictionary.getInstance().getActivityDefs()) {
							for (EStructuralFeature f : def.getEStructuralFeatures()) {
								featureNames.add(f.getName());
							}
						}
						List<String> comboBoxChoices = new ArrayList<String>(featureNames);
						Collections.sort(comboBoxChoices, CommonUtils.ALPHABETICAL_COMPARATOR);
						return comboBoxChoices;
					}
					
			});
	}

	/**
	 * This adds a property descriptor for the Value Literal feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addValueLiteralPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_FeatureValueRow_valueLiteral_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_FeatureValueRow_valueLiteral_feature", "_UI_FeatureValueRow_type"),
				 PlanTimelinePackage.Literals.FEATURE_VALUE_ROW__VALUE_LITERAL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns FeatureValueRow.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		return null; //overlayImage(object, getResourceLocator().getImage("full/obj16/FeatureValueRow"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		FeatureValueRow featureValueRow = (FeatureValueRow)object;
		String label = featureValueRow.getName();
		boolean noLabel = isTrivial(label);
		if (noLabel) {
			if (!isTrivial(featureValueRow.getValueLiteral())) {
				label = featureValueRow.getValueLiteral();
			}
		}
		return label;
	}

	private boolean isTrivial(String label) {
		boolean noLabel = label == null || label.length() == 0;
		return noLabel;
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

		switch (notification.getFeatureID(FeatureValueRow.class)) {
			case PlanTimelinePackage.FEATURE_VALUE_ROW__FEATURE_NAME:
			case PlanTimelinePackage.FEATURE_VALUE_ROW__VALUE_LITERAL:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
	}

	@Override
	public Command createCommand(Object object, final EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter) {
		final FeatureValueRow row = (FeatureValueRow) object;
		if (SetCommand.class == commandClass) {
			return createSetCommand(row, domain, commandParameter);
		} else if (AddCommand.class == commandClass) {
			return createAddCommand(row, domain, commandParameter);
		}
		return super.createCommand(object, domain, commandClass, commandParameter);
	}
	
	private Command createAddCommand(final FeatureValueRow row, final EditingDomain domain, CommandParameter commandParameter) {
		final CompoundCommand cmd = new CompoundCommand();
		for (EPlanElement pe : getPlanElements(commandParameter.getCollection())) {
			new PlanVisitor() {
				@Override
				protected void visit(EPlanElement element) {
					EObject data = element.getData();
					if (data != null) {
						String valueLiteral = row.getValueLiteral();
						String featureName = row.getFeatureName();
						EClass eClass = data.eClass();
						EStructuralFeature feature = eClass.getEStructuralFeature(featureName);
						if (feature != null) {
							Object value = null; 
							if (feature instanceof EAttribute) {
								EAttribute eAttribute = (EAttribute)feature;
								value = EcoreUtil.createFromString(eAttribute.getEAttributeType(), valueLiteral);
							} else {
								LogUtil.error("value for attribute literal impossible");
							}
							if (value != null) {
								if (feature.isMany()) {
									cmd.append(AddCommand.create(domain, data, feature, Collections.singletonList(value)));
								} else {
									cmd.append(SetCommand.create(domain, data, feature, value));
								}
							}
						}
					}
				}
			}.visitAll(pe);
		}
		if (!cmd.isEmpty()) {
			return cmd;
		}
		return null;
	}

	private Command createSetCommand(final FeatureValueRow row, final EditingDomain domain, CommandParameter commandParameter) {
		final CompoundCommand cmd = new CompoundCommand();
		for (EPlanElement pe : getPlanElements(commandParameter.getValue())) {
			new PlanVisitor() {
				@Override
				protected void visit(EPlanElement element) {
					EObject data = element.getData();
					if (data != null) {
						String valueLiteral = row.getValueLiteral();
						String featureName = row.getFeatureName();
						EClass eClass = data.eClass();
						EStructuralFeature feature = eClass.getEStructuralFeature(featureName);
						if (feature != null) {
							Object value = null; 
							if (feature instanceof EAttribute) {
								EAttribute eAttribute = (EAttribute)feature;
								value = EcoreUtil.createFromString(eAttribute.getEAttributeType(), valueLiteral);
							} else {
								LogUtil.error("value for EReference literal impossible");
							}
							if (value != null) {
								if (feature.isMany()) {
									cmd.append(SetCommand.create(domain, data, feature, Collections.singletonList(value)));
								} else {
									cmd.append(SetCommand.create(domain, data, feature, value));
								}
							}
						}
					}
				}
			}.visitAll(pe);
		}
		if (!cmd.isEmpty()) {
			return cmd;
		}
		return super.createCommand(row, domain, SetCommand.class, commandParameter);
	}

}
