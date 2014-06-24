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
package gov.nasa.ensemble.core.plan.constraints.network;

import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.transaction.IConsistencyMaintenanceListener;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.jscience.physics.amount.Amount;

public class TemporalNetworkConsistencyMaintenanceListener implements IConsistencyMaintenanceListener {

	private static final EAttribute SCHEDULED_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED;
	private static final EAttribute START_TIME_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME;
	private static final EAttribute DURATION_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION;
	private static final EAttribute END_TIME_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME;
	private static final EReference PERIODIC_CONSTRAINTS_FEATURE = ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS;
	private static final EReference BINARY_CONSTRAINTS_FEATURE = ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS;
	private static final EReference CHAIN_FEATURE = ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__CHAIN;

	@Override
	public Command createConsistencyMaintenanceCommand(ResourceSetChangeEvent event) {
		EPlan plan = EPlanUtils.getPlanNotifications(event);
		if (plan != null) {
			List<Notification> notifications = event.getNotifications();
			return getPlanCommand(event.getEditingDomain(), plan, notifications);
		}
		return null;
	}

	private Command getPlanCommand(TransactionalEditingDomain editingDomain, EPlan plan, List<Notification> notifications) {
		TemporalNetworkMember networkMember = TemporalNetworkMember.get(plan);
		if (networkMember == null) {
			return null;
		}
		TemporalNetworkCommandBuilder<Long> builder = new TemporalNetworkCommandBuilder<Long>(networkMember);
		// gather
		for (Notification notification : notifications) {
			int eventType = notification.getEventType();
			Object notifier = notification.getNotifier();
			if (notifier instanceof EPlanElement) {
				EPlanElement element = (EPlanElement) notifier;
				List<EPlanElement> childrenRemoved = EPlanUtils.getElementsRemoved(notification);
				if (!childrenRemoved.isEmpty()) {
					builder.childrenRemoved(element, childrenRemoved);
				}
				List<EPlanElement> childrenAdded = EPlanUtils.getElementsAdded(notification);
				if (!childrenAdded.isEmpty()) {
					builder.childrenAdded(element, childrenAdded);
				}
				if (eventType == Notification.MOVE) {
					Logger logger = Logger.getLogger(TemporalNetworkConsistencyMaintenanceListener.class);
					logger.warn("move!");
				}
			}
			Object feature = notification.getFeature();
			if (notifier instanceof EMember) {
				EMember member = (EMember) notifier;
				EPlanElement element = member.getPlanElement();
				if (eventType == Notification.SET) {
					if (feature == SCHEDULED_FEATURE) {
						if (element instanceof EPlanChild) {
							EPlanChild child = (EPlanChild) element;
							Boolean oldValue = (Boolean)notification.getOldValue();
							Boolean newValue = (Boolean)notification.getNewValue();
							builder.schedulednessChanged(child, oldValue, newValue);
						}
					} else if (feature == START_TIME_FEATURE) {
						if (element instanceof EPlanChild) {
							EPlanChild child = (EPlanChild) element;
							Date oldValue = (Date)notification.getOldValue();
							builder.startTimeChanged(child, oldValue);
						}
					} else if (feature == DURATION_FEATURE) {
						if (element instanceof EPlanChild) {
							EPlanChild child = (EPlanChild) element;
							@SuppressWarnings("unchecked")
							Amount<Duration> oldValue = (Amount<Duration>)notification.getOldValue();
							builder.durationChanged(child, oldValue);
						}
					} else if (feature == END_TIME_FEATURE) {
						if (element instanceof EPlanChild) {
							EPlanChild child = (EPlanChild) element;
							Date oldValue = (Date)notification.getOldValue();
							builder.endTimeChanged(child, oldValue);
						}
					} else if (feature == CHAIN_FEATURE) {
						TemporalChain oldChain = (TemporalChain)notification.getOldValue();
						TemporalChain newChain = (TemporalChain)notification.getNewValue();
						builder.temporalChainChanged(element, oldChain, newChain);
					}
				} else if (feature == PERIODIC_CONSTRAINTS_FEATURE) {
					List<PeriodicTemporalConstraint> constraintsRemoved = EMFUtils.getRemovedObjects(notification, PeriodicTemporalConstraint.class);
					List<PeriodicTemporalConstraint> constraintsAdded = EMFUtils.getAddedObjects(notification, PeriodicTemporalConstraint.class);
					builder.periodicConstraintsChanged(element, constraintsRemoved, constraintsAdded);
				} else if (feature == BINARY_CONSTRAINTS_FEATURE) {
					List<BinaryTemporalConstraint> constraintsRemoved = EMFUtils.getRemovedObjects(notification, BinaryTemporalConstraint.class);
					List<BinaryTemporalConstraint> constraintsAdded = EMFUtils.getAddedObjects(notification, BinaryTemporalConstraint.class);
					builder.binaryConstraintsChanged(element, constraintsRemoved, constraintsAdded);
				}
			} else if (ConstraintUtils.areAnchorsAllowed() && ADParameterUtils.isActivityAttributeOrParameter(notifier)) {
				EPlanElement planElement = ADParameterUtils.getActivityAttributeOrParameter(notifier);
				builder.activityParameterChanged(planElement, (EStructuralFeature) notification.getFeature());
			} else if (notifier instanceof TemporalConstraint) {
				if (feature == AdvisorPackage.Literals.IWAIVABLE__WAIVER_RATIONALE) {
					builder.waiverUpdated((TemporalConstraint)notifier);
				}
			}
		}
		return builder.createCommand(editingDomain);
	}

}
