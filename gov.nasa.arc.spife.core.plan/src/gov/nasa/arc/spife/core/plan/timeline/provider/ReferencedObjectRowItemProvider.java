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


import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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

/**
 * This is the item provider adapter for a {@link gov.nasa.arc.spife.core.plan.timeline.ReferencedObjectRow} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ReferencedObjectRowItemProvider
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
	public ReferencedObjectRowItemProvider(AdapterFactory adapterFactory) {
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

			addReferencePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Reference feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReferencePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ReferencedObjectRow_reference_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ReferencedObjectRow_reference_feature", "_UI_ReferencedObjectRow_type"),
				 PlanTimelinePackage.Literals.REFERENCED_OBJECT_ROW__REFERENCE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This returns ReferencedObjectRow.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		ReferencedObjectRow row = (ReferencedObjectRow) object;
		EObject reference = row.getReference();
		if (reference != null) {
			IItemLabelProvider lp = EMFUtils.adapt(reference, IItemLabelProvider.class);
			if (lp != null) {
				return lp.getImage(reference);
			}
		}
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ReferencedObjectRow"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		return ((ReferencedObjectRow)object).getName();
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
		final ReferencedObjectRow row = (ReferencedObjectRow) object;
		if (SetCommand.class == commandClass) {
			return createSetCommand(row, domain, commandParameter);
		} else if (AddCommand.class == commandClass) {
			return createAddCommand(row, domain, commandParameter);
		}
		return super.createCommand(object, domain, commandClass, commandParameter);
	}

	private Command createAddCommand(final ReferencedObjectRow row, final EditingDomain domain, CommandParameter commandParameter) {
		final CompoundCommand cmd = new CompoundCommand();
		for (EPlanElement pe : getPlanElements(commandParameter.getCollection())) {
			new PlanVisitor() {
				@Override
				protected void visit(EPlanElement element) {
					EObject data = element.getData();
					if (data != null) {
						for (EReference r : data.eClass().getEReferences()) {
							EObject reference = row.getReference();
							if (r.getEReferenceType().isSuperTypeOf(reference.eClass())) {
								if (r.isMany()) {
									cmd.append(AddCommand.create(domain, data, r, Collections.singletonList(reference)));
								} else {
									cmd.append(SetCommand.create(domain, data, r, reference));
								}
							}
						}
					}
				}
				
			}.visitAll(pe);
		}
		if (!cmd.isEmpty()) {
			return cmd;
		} else {
			return null;
		}
	}

	private Command createSetCommand(final ReferencedObjectRow row, final EditingDomain domain, CommandParameter commandParameter) {
		final CompoundCommand cmd = new CompoundCommand();
		for (EPlanElement pe : getPlanElements(commandParameter.getValue())) {
			new PlanVisitor() {
				@Override
				protected void visit(EPlanElement element) {
					EObject data = element.getData();
					if (data != null) {
						for (EReference r : data.eClass().getEReferences()) {
							EObject reference = row.getReference();
							if (r.getEReferenceType().isSuperTypeOf(reference.eClass())) {
								if (r.isMany()) {
									cmd.append(SetCommand.create(domain, data, r, Collections.singletonList(reference)));
								} else {
									cmd.append(SetCommand.create(domain, data, r, reference));
								}
							}
						}
					}
				}
				
			}.visitAll(pe);
		}
		if (!cmd.isEmpty()) {
			return cmd;
		} else {
			return null;
		}
	}

}
