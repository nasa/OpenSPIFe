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
package gov.nasa.ensemble.core.model.plan.temporal.provider;


import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.provider.EMemberItemPropertyDescriptor;
import gov.nasa.ensemble.core.model.plan.provider.EMemberItemProvider;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalModifier;
import gov.nasa.ensemble.core.model.plan.temporal.util.TemporalMemberUtils;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
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
 * This is the item provider adapter for a {@link gov.nasa.ensemble.core.model.plan.temporal.TemporalMember} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TemporalMemberItemProvider
	extends EMemberItemProvider
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
	public TemporalMemberItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);
			
			addStartTimePropertyDescriptor(object);
			addDurationPropertyDescriptor(object);
			addEndTimePropertyDescriptor(object);
			addScheduledPropertyDescriptor(object);
			addStartOffsetTimepointPropertyDescriptor(object);
			addStartOffsetAmountPropertyDescriptor(object);
			addEndOffsetTimepointPropertyDescriptor(object);
			addEndOffsetAmountPropertyDescriptor(object);
		}
		
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Start Time feature.
	 * <!-- begin-user-doc -->
	 * Customize and use DateItemPropertyDescriptor
	 * <!-- end-user-doc -->
	 * @return 
	 * @generated NOT
	 */
	protected void addStartTimePropertyDescriptor(Object object) {
		EAttribute feature = TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME;
		if (!((EObject)object).eIsSet(feature) && !EMFUtils.isEditable(object)) {
			return;
		}
		String description = "The start time of the " + EPlanUtils.getPlanElementTypeName(object);
		itemPropertyDescriptors.add
			(new DateItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(), getString("_UI_TemporalMember_startTime_feature"),
				 description,
				 feature,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
			 	 null));
//		 {
//				@Override
//				public boolean canSetProperty(Object object) {
//					if (super.canSetProperty(object) && (object instanceof TemporalMember)) {
//						return ((TemporalMember)object).getCalculatedVariable() != CalculatedVariable.START;
//					}
//					return false;
//				}
//			}
	}

	/**
	 * This adds a property descriptor for the Duration feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addDurationPropertyDescriptor(Object object) {
		AdapterFactory rootAdapterFactory = ((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory();

		String displayName = getString("_UI_TemporalMember_duration_feature");
		String description = "The duration of the " + EPlanUtils.getPlanElementTypeName(object);

		EStructuralFeature eStructuralFeature
			= TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION;

		boolean isSettable = (object instanceof TemporalMember ? !TemporalMemberUtils.hasDurationFormula((TemporalMember)object) : false);
		boolean multiLine = false;
		boolean sortChoices = false;
		Object staticImage = ItemPropertyDescriptor.GENERIC_VALUE_IMAGE;
		String category = null;
		String[] filterFlags = null;

		itemPropertyDescriptors.add(createItemPropertyDescriptor(
				rootAdapterFactory, getResourceLocator(), displayName,
				description, eStructuralFeature, isSettable, multiLine,
				sortChoices, staticImage, category, filterFlags));
	}

	/**
	 * This adds a property descriptor for the End Time feature.
	 * <!-- begin-user-doc -->
	 * Customize and use DateItemPropertyDescriptor
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addEndTimePropertyDescriptor(Object object) {
		EAttribute feature = TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME;
		if (!((EObject)object).eIsSet(feature) && !EMFUtils.isEditable(object)) {
			return;
		}
		String description = "The end time of the " + EPlanUtils.getPlanElementTypeName(object);
		itemPropertyDescriptors.add
			(new DateItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TemporalMember_endTime_feature"),
				 description,
				 feature,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
//		 {
//				@Override
//				public boolean canSetProperty(Object object) {
//					if (super.canSetProperty(object) && (object instanceof TemporalMember)) {
//						return ((TemporalMember)object).getCalculatedVariable() != CalculatedVariable.END;
//					}
//					return false;
//				}
//			}
	}

	/**
	 * This adds a property descriptor for the Calculated Variable feature.
	 * <!-- begin-user-doc -->
	 * This is here in the off chance that we want to put it back for debugging, etc.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addCalculatedVariablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(new EMemberItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 intern(getString("_UI_TemporalMember_calculatedVariable_feature")),
				 intern(getString("_UI_PropertyDescriptor_description", "_UI_TemporalMember_calculatedVariable_feature", "_UI_TemporalMember_type")),
				 TemporalPackage.Literals.TEMPORAL_MEMBER__CALCULATED_VARIABLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null) {
				@Override
				protected Collection<?> getComboBoxObjects(Object object) {
					Collection<?> objects = super.getComboBoxObjects(object);
					if (TemporalMemberUtils.hasDurationFormula((TemporalMember)object)) {
						objects.remove(CalculatedVariable.DURATION);
					}
					return objects;
				}
			}
		);
	}

	/**
	 * This adds a property descriptor for the Scheduled feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected void addScheduledPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor(
				((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TemporalMember_scheduled_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TemporalMember_scheduled_feature", "_UI_TemporalMember_type"),
				 TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Offset Timepoint feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartOffsetTimepointPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TemporalMember_startOffsetTimepoint_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TemporalMember_startOffsetTimepoint_feature", "_UI_TemporalMember_type"),
				 TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Offset Amount feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartOffsetAmountPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TemporalMember_startOffsetAmount_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TemporalMember_startOffsetAmount_feature", "_UI_TemporalMember_type"),
				 TemporalPackage.Literals.TEMPORAL_MEMBER__START_OFFSET_AMOUNT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End Offset Timepoint feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndOffsetTimepointPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TemporalMember_endOffsetTimepoint_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TemporalMember_endOffsetTimepoint_feature", "_UI_TemporalMember_type"),
				 TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End Offset Amount feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndOffsetAmountPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TemporalMember_endOffsetAmount_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TemporalMember_endOffsetAmount_feature", "_UI_TemporalMember_type"),
				 TemporalPackage.Literals.TEMPORAL_MEMBER__END_OFFSET_AMOUNT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns TemporalMember.gif.
	 * <!-- begin-user-doc -->
	 * Customize the icon
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/TemporalMember.png"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		return "Schedule";
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

		switch (notification.getFeatureID(TemporalMember.class)) {
			case TemporalPackage.TEMPORAL_MEMBER__START_TIME:
			case TemporalPackage.TEMPORAL_MEMBER__DURATION:
			case TemporalPackage.TEMPORAL_MEMBER__END_TIME:
			case TemporalPackage.TEMPORAL_MEMBER__SCHEDULED:
			case TemporalPackage.TEMPORAL_MEMBER__USE_CHILD_TIMES:
			case TemporalPackage.TEMPORAL_MEMBER__USE_PARENT_TIMES:
			case TemporalPackage.TEMPORAL_MEMBER__CALCULATED_VARIABLE:
			case TemporalPackage.TEMPORAL_MEMBER__EXTENT:
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT:
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_AMOUNT:
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT:
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_AMOUNT:
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

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return TemporalPlanningEditPlugin.INSTANCE;
	}

	@Override
	protected Command createSetCommand(EditingDomain domain, EObject owner,
			EStructuralFeature feature, Object value, int index) {
		if (TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME == feature) {
			return new SetTemporalCommand(domain, owner, feature, value, index);
		}
		if (TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME == feature) {
			return new SetTemporalCommand(domain, owner, feature, value, index);
		}
		if (TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION == feature) {
			return new SetTemporalCommand(domain, owner, feature, value, index);
		}
		if (TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED == feature) {
			return new SetScheduledCommand(domain, owner, feature, value, index);
		}
		return super.createSetCommand(domain, owner, feature, value, index);
	}

	private static class DateItemPropertyDescriptor extends EMemberItemPropertyDescriptor {
		private DateItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
				EStructuralFeature feature, boolean isSettable, boolean multiLine, boolean sortChoices, Object staticImage, String category,
				String[] filterFlags) {
			super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices, staticImage, category,
					filterFlags);
		}

		@Override
		public IItemLabelProvider getLabelProvider(Object object) {
			return new IItemLabelProvider() {

				private final IStringifier<Date> stringifier = StringifierRegistry.getStringifier(Date.class);

				@Override
				public Object getImage(Object object) {
					return null;
				}

				@Override
				public String getText(Object object) {
					return stringifier.getDisplayString((Date) object);
				}

			};
		}
	}

	private static final class SetTemporalCommand extends SetCommand {
		
		private static final NullProgressMonitor MONITOR = new NullProgressMonitor();
		private IUndoableOperation operation = null;

		private SetTemporalCommand(EditingDomain domain, EObject owner,
				EStructuralFeature feature, Object value, int index) {
			super(domain, owner, feature, value, index);
		}

		@Override
		public void doExecute() {
			TemporalMember member = (TemporalMember)getOwner();
			if ((member.getStartTime() == null) && (member.getEndTime() == null)) {
				operation = new FeatureTransactionChangeOperation("update duration", member, getFeature(), value);
			} else {
				operation = TemporalModifier.getInstance().set(member, getFeature(), value);
			}
			IUndoContext undoContext = TransactionUtils.getUndoContext(member);
			if (undoContext != null) {
				operation.addContext(undoContext);
			}
			try {
				operation.execute(MONITOR, null);
			} catch (ExecutionException e) {
				LogUtil.error(e);
			}
		}
		
		@Override
		public void doUndo() {
			try {
				operation.undo(MONITOR, null);
			} catch (ExecutionException e) {
				LogUtil.error(e);
			}
		}
		
		@Override
		public void doRedo() {
			try {
				operation.redo(MONITOR, null);
			} catch (ExecutionException e) {
				LogUtil.error(e);
			}
		}
		
	}

	private static final class SetScheduledCommand extends SetCommand {

		private Map<TemporalMember, Boolean> newValues = new LinkedHashMap<TemporalMember, Boolean>();
		private Map<TemporalMember, Boolean> oldValues = new LinkedHashMap<TemporalMember, Boolean>();

		private SetScheduledCommand(EditingDomain domain, EObject owner,
				EStructuralFeature feature, Object value, int index) {
			super(domain, owner, feature, value, index);
		}

		@Override
		public void doExecute() {
			computeValues();
			installValues(newValues);
		}

		@Override
		public void doUndo() {
			installValues(oldValues);
		}

		@Override
		public void doRedo() {
			installValues(newValues);
		}

		private void installValues(Map<TemporalMember, Boolean> values) {
			for (Map.Entry<TemporalMember, Boolean> entry : values.entrySet()) {
				TemporalMember member = entry.getKey();
				Boolean scheduled = entry.getValue();
				member.setScheduled(scheduled);
			}
		}

		private void computeValues() {
			TemporalMember member = (TemporalMember)owner;
			EPlanElement element = member.getPlanElement();
			Boolean scheduled = (Boolean)value;
			if (scheduled != null) {
				setNewValue(member, scheduled);
				scheduleChildren(element, scheduled);
				scheduleParents(element, scheduled);
			} else {
				mixscheduleParents(element);
			}
		}

		private void scheduleChildren(EPlanElement element, Boolean newScheduled) {
			for (EPlanElement child : EPlanUtils.getChildren(element)) {
				setNewValue(child.getMember(TemporalMember.class), newScheduled);
				scheduleChildren(child, newScheduled);
			}
		}

		private void mixscheduleParents(EPlanElement element) {
			EObject parent = element.eContainer();
			while (parent instanceof EPlanElement) {
				EPlanElement parentElement = (EPlanElement) parent;
				setNewValue(parentElement.getMember(TemporalMember.class), null);
				parent = parentElement.eContainer();
			}
		}

		private void scheduleParents(EPlanElement element, Boolean scheduled) {
			EObject parent = element.eContainer();
			if (parent instanceof EPlanElement) {
				EPlanElement parentElement = (EPlanElement) parent;
				TemporalMember parentTemporalMember = parentElement.getMember(TemporalMember.class);
				Boolean oldScheduled = parentTemporalMember.getScheduled();
				if (oldScheduled != scheduled) {
					Boolean newScheduled = computeSchedulednessFromChildren(EPlanUtils.getChildren(parentElement));
					if (oldScheduled != newScheduled) {
						setNewValue(parentTemporalMember, newScheduled);
						if (newScheduled == null) {
							mixscheduleParents(parentElement);
						} else {
							scheduleParents(parentElement, newScheduled);
						}
					}
				}
			}
		}

		private Boolean computeSchedulednessFromChildren(List<? extends EPlanElement> children) {
			boolean anyScheduled = false;
			boolean anyUnscheduled = false;
			for (EPlanElement element : children) {
				final Boolean scheduled;
				TemporalMember member = element.getMember(TemporalMember.class);
				if (newValues.containsKey(member)) {
					scheduled = newValues.get(member);
				} else {
					scheduled = member.getScheduled();
				}
				if (scheduled == null) {
					anyScheduled = true;
					anyUnscheduled = true;
				} else if (scheduled.booleanValue()) {
					anyScheduled = true;
				} else {
					anyUnscheduled = true;
				}
				if (anyScheduled && anyUnscheduled) {
					break;
				}
			}
			Boolean scheduled;
			if (anyScheduled && anyUnscheduled) {
				scheduled = null;
			} else if (anyScheduled) {
				scheduled = true;
			} else if (anyUnscheduled) {
				scheduled = false;
			} else { // no children = unscheduled
				scheduled = false;
			}
			return scheduled;
		}

		private void setNewValue(TemporalMember member, Boolean scheduled) {
			oldValues.put(member, member.getScheduled());
			newValues.put(member, scheduled);
		}

	}

}
