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
package gov.nasa.ensemble.core.model.plan.temporal.impl;

import gov.nasa.ensemble.common.CommonPlugin;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.impl.EMemberImpl;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceXMLHelper;
import gov.nasa.ensemble.emf.SafeAdapter;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import java.util.Date;
import java.util.List;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.NotificationChainImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Member</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getEndTime <em>End Time</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getScheduled <em>Scheduled</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#isUseChildTimes <em>Use Child Times</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#isUseParentTimes <em>Use Parent Times</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getCalculatedVariable <em>Calculated Variable</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getExtent <em>Extent</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getStartOffsetTimepoint <em>Start Offset Timepoint</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getStartOffsetAmount <em>Start Offset Amount</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getEndOffsetTimepoint <em>End Offset Timepoint</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalMemberImpl#getEndOffsetAmount <em>End Offset Amount</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TemporalMemberImpl extends EMemberImpl implements TemporalMember {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;

	/**
	 * The default value of the '{@link #getStartTime() <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartTime()
	 * @generated
	 * @ordered
	 */
	protected static final Date START_TIME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getStartTime() <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartTime()
	 * @generated
	 * @ordered
	 */
	protected Date startTime = START_TIME_EDEFAULT;
	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * This field is guaranteed to be a nonnegative duration.
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated NOT
	 * @ordered
	 */
	protected Amount<Duration> duration = DateUtils.ZERO_DURATION;
	/**
	 * The default value of the '{@link #getEndTime() <em>End Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndTime()
	 * @generated
	 * @ordered
	 */
	protected static final Date END_TIME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getEndTime() <em>End Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndTime()
	 * @generated
	 * @ordered
	 */
	protected Date endTime = END_TIME_EDEFAULT;
	/**
	 * The default value of the '{@link #getScheduled() <em>Scheduled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScheduled()
	 * @generated
	 * @ordered
	 */
	protected static final Boolean SCHEDULED_EDEFAULT = Boolean.TRUE;
	/**
	 * The cached value of the '{@link #getScheduled() <em>Scheduled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScheduled()
	 * @generated
	 * @ordered
	 */
	protected Boolean scheduled = SCHEDULED_EDEFAULT;
	/**
	 * The default value of the '{@link #isUseChildTimes() <em>Use Child Times</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseChildTimes()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_CHILD_TIMES_EDEFAULT = true;
	/**
	 * The flag representing the value of the '{@link #isUseChildTimes() <em>Use Child Times</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseChildTimes()
	 * @generated
	 * @ordered
	 */
	protected static final int USE_CHILD_TIMES_EFLAG = 1 << 8;
	/**
	 * The default value of the '{@link #isUseParentTimes() <em>Use Parent Times</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseParentTimes()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_PARENT_TIMES_EDEFAULT = false;
	/**
	 * The flag representing the value of the '{@link #isUseParentTimes() <em>Use Parent Times</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseParentTimes()
	 * @generated
	 * @ordered
	 */
	protected static final int USE_PARENT_TIMES_EFLAG = 1 << 9;
	/**
	 * The default value of the '{@link #getCalculatedVariable() <em>Calculated Variable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCalculatedVariable()
	 * @generated
	 * @ordered
	 */
	protected static final CalculatedVariable CALCULATED_VARIABLE_EDEFAULT = CalculatedVariable.END;
	/**
	 * The cached value of the '{@link #getCalculatedVariable() <em>Calculated Variable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCalculatedVariable()
	 * @generated
	 * @ordered
	 */
	protected CalculatedVariable calculatedVariable = CALCULATED_VARIABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getExtent() <em>Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtent()
	 * @generated
	 * @ordered
	 */
	protected static final TemporalExtent EXTENT_EDEFAULT = null;
	
	/**
	 * The default value of the '{@link #getStartOffsetTimepoint() <em>Start Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartOffsetTimepoint()
	 * @generated
	 * @ordered
	 */
	protected static final Timepoint START_OFFSET_TIMEPOINT_EDEFAULT = Timepoint.START;
	/**
	 * The cached value of the '{@link #getStartOffsetTimepoint() <em>Start Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartOffsetTimepoint()
	 * @generated
	 * @ordered
	 */
	protected Timepoint startOffsetTimepoint = START_OFFSET_TIMEPOINT_EDEFAULT;
	/**
	 * The cached value of the '{@link #getStartOffsetAmount() <em>Start Offset Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartOffsetAmount()
	 * @generated
	 * @ordered
	 */
	protected Amount<Duration> startOffsetAmount;
	/**
	 * The default value of the '{@link #getEndOffsetTimepoint() <em>End Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndOffsetTimepoint()
	 * @generated
	 * @ordered
	 */
	protected static final Timepoint END_OFFSET_TIMEPOINT_EDEFAULT = Timepoint.START;
	/**
	 * The cached value of the '{@link #getEndOffsetTimepoint() <em>End Offset Timepoint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndOffsetTimepoint()
	 * @generated
	 * @ordered
	 */
	protected Timepoint endOffsetTimepoint = END_OFFSET_TIMEPOINT_EDEFAULT;
	/**
	 * The cached value of the '{@link #getEndOffsetAmount() <em>End Offset Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndOffsetAmount()
	 * @generated
	 * @ordered
	 */
	protected Amount<Duration> endOffsetAmount;
	private Adapter childrenChangedAdapter = null;
	
	private synchronized Adapter getChildrenChangedAdapter() {
		if (childrenChangedAdapter == null) {
			childrenChangedAdapter = new SafeAdapter() {
				@Override
				protected void handleNotify(Notification notification) {
					Object feature = notification.getFeature();
					if (feature == PlanPackage.Literals.EPLAN_PARENT__CHILDREN) {
						int eventType = notification.getEventType();
						Object oldValue = notification.getOldValue();
						Object newValue = notification.getNewValue();
						childrenChanged(eventType, oldValue, newValue);
					}
				}
			};
		}
		return childrenChangedAdapter;
	}
	
	private void childrenChanged(int eventType, Object oldValue, Object newValue) {
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		switch (eventType) {
		case Notification.ADD:
			EPlanChild addedElement = (EPlanChild)newValue;
			TemporalMemberImpl childMember = (TemporalMemberImpl)addedElement.getMember(TemporalMember.class, true);
			if (childMember.isUseParentTimes()) {
				break;
			}
			Date start = childMember.getStartTime();
			Date end = childMember.getEndTime();
			if (start != null) {
				TemporalPropagation.updateParentExtent(this, start, end, notifications);
			}
			break;
		case Notification.ADD_MANY:
			@SuppressWarnings("unchecked")
			List<EPlanChild> added = (List<EPlanChild>)newValue;
			TemporalPropagation.updateParentWithAddedChildren(this, added, notifications);
			break;
		case Notification.REMOVE:
		case Notification.REMOVE_MANY:
			TemporalPropagation.updateThisParentFromChildren(this, notifications);
			break;
		case Notification.MOVE:
			// reordering the children has no effect on our parent
			break;
		}
		if (notifications != null) {
			notifications.dispatch();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TemporalMemberImpl() {
		super();
		eFlags |= USE_CHILD_TIMES_EFLAG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TemporalPackage.Literals.TEMPORAL_MEMBER;
	}

	@Override
	public NotificationChain eBasicSetContainer(InternalEObject newContainer, int newContainerFeatureID, NotificationChain msgs) {
		if (childrenChangedAdapter != null) {
			childrenChangedAdapter.getTarget().eAdapters().remove(childrenChangedAdapter);
		}
		NotificationChain container = super.eBasicSetContainer(newContainer, newContainerFeatureID, msgs);
		if (isUseChildTimes()) {
			EPlanElement newElement = (EPlanElement)newContainer;
			if (newElement != null) {
				newElement.eAdapters().add(getChildrenChangedAdapter());
			}
		}
		return container;
	}
		
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setStartTime(Date newStart) {
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		setStartTime(newStart, notifications);
		if (notifications != null) {
			notifications.dispatch();
		}
	}

	/* package */ synchronized void setStartTime(Date newStart, NotificationChain notifications) {
		Date oldStart = startTime;
		if ((oldStart != null) && (calculatedVariable == CalculatedVariable.START) && !isBoundsFromEndpoints()) {
			if (oldStart.equals(newStart)) {
				return;
			}
			if (PlanResourceXMLHelper.isSetFromXML()) {
				startTime = newStart;
				return;
			}
			
			throwCalculatingError("start", "end minus duration");
		}
		setStartNotify(oldStart, newStart, notifications);
		Date oldEnd = endTime;
		if (newStart == null) {
			setEndNotify(oldEnd, null, notifications);
		} else {
			if ((oldEnd == null) || (calculatedVariable == CalculatedVariable.END)) {
				Date newEnd = deriveEndTime(newStart, duration);
				setEndNotify(oldEnd, newEnd, notifications);
				TemporalPropagation.maybeUpdateParentStartEnd(this, oldStart, newStart, oldEnd, newEnd, notifications);
			} else if (oldEnd.before(newStart)) {
				setDurationNotify(duration, DateUtils.ZERO_DURATION, notifications);
				Date newEnd = newStart;
				setEndNotify(oldEnd, newEnd, notifications);
				TemporalPropagation.maybeUpdateParentStartEnd(this, oldStart, newStart, oldEnd, newEnd, notifications);
			} else if (isBoundsFromEndpoints()) {
				Amount<Duration> newDuration = deriveDuration(newStart, oldEnd);
				setDurationNotify(duration, newDuration, notifications);
				TemporalPropagation.maybeUpdateParentStart(this, oldStart, newStart, notifications);
			}
		}
	}

	private void setStartNotify(Date oldStart, Date newStart, NotificationChain notifications) {
		startTime = newStart;
		if ((notifications != null) && !CommonUtils.equals(oldStart, newStart)) {
			notifications.add(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__START_TIME, oldStart, newStart));
		}
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Amount<Duration> getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * The newDuration must be non-negative.
	 * If it is not, then an IllegalArgumentException will be thrown.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setDuration(Amount<Duration> newDuration) {
		if (newDuration == null) {
			throw new IllegalArgumentException("duration must be non-null");
		} else if (newDuration.isLessThan(DateUtils.ZERO_DURATION)) {
			throw new IllegalArgumentException("duration must be non-negative");
		}
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		setDuration(newDuration, notifications);
		if (notifications != null) {
			notifications.dispatch();
		}
	}

	/* package */ synchronized void setDuration(Amount<Duration> newDuration, NotificationChain notifications) {
		Amount<Duration> oldDuration = duration;
		if ((calculatedVariable == CalculatedVariable.DURATION) && ((startTime != null) || (endTime != null))) {
			if (AmountUtils.equals(oldDuration, newDuration)) {
				return;
			}
			if (PlanResourceXMLHelper.isSetFromXML()) {
				duration = newDuration;
				return;
			}
			
			throwCalculatingError("duration", "end minus start");
		}
		setDurationNotify(oldDuration, newDuration, notifications);
		Date oldEnd = endTime;
		Date oldStart = startTime;
		if ((calculatedVariable == CalculatedVariable.START) && (oldEnd != null)) {
			Date newStart = deriveStartTime(newDuration, oldEnd);
			setStartNotify(oldStart, newStart, notifications);
			TemporalPropagation.maybeUpdateParentStart(this, oldStart, newStart, notifications);
		}
		if ((calculatedVariable == CalculatedVariable.END) && (oldStart != null)) {
			Date newEnd = deriveEndTime(oldStart, newDuration);
			setEndNotify(oldEnd, newEnd, notifications);
			TemporalPropagation.maybeUpdateParentEnd(this, oldEnd, newEnd, notifications);
		}
		if ((oldEnd == null) && (oldStart == null)) {
			TemporalPropagation.maybeUpdateParentDuration(this, oldDuration, newDuration, notifications);
		}
	}

	private void setDurationNotify(Amount<Duration> oldDuration, Amount<Duration> newDuration, NotificationChain notifications) {
		duration = newDuration;
		if ((notifications != null) && !AmountUtils.equals(oldDuration, newDuration)) {
			notifications.add(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__DURATION, oldDuration, newDuration));
		}
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setEndTime(Date newEnd) {
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		setEndTime(newEnd, notifications);
		if (notifications != null) {
			notifications.dispatch();
		}
	}
	
	/* package */ synchronized void setEndTime(Date newEnd, NotificationChain notifications) {
		Date oldEnd = endTime;
		if ((oldEnd != null) && (calculatedVariable == CalculatedVariable.END) && !isBoundsFromEndpoints()) {
			if (oldEnd.equals(newEnd)) {
				return;
			}
			if (PlanResourceXMLHelper.isSetFromXML()) {
				endTime = newEnd;
				return;
			}
			throwCalculatingError("end", "start plus duration");
		}
		setEndNotify(oldEnd, newEnd, notifications);
		Date oldStart = startTime;
		if (newEnd == null) {
			setStartNotify(oldStart, null, notifications);
		} else {
			if ((oldStart == null) || (calculatedVariable == CalculatedVariable.START)) {
				Date newStart = deriveStartTime(duration, newEnd);
				setStartNotify(oldStart, newStart, notifications);
				TemporalPropagation.maybeUpdateParentStartEnd(this, oldStart, newStart, oldEnd, newEnd, notifications);
			} else if (oldStart.after(newEnd)) {
				setDurationNotify(duration, DateUtils.ZERO_DURATION, notifications);
				Date newStart = newEnd;
				setStartNotify(oldStart, newStart, notifications);
				TemporalPropagation.maybeUpdateParentStartEnd(this, oldStart, newStart, oldEnd, newEnd, notifications);
			} else if (isBoundsFromEndpoints()) {
				Amount<Duration> newDuration = deriveDuration(oldStart, newEnd);
				setDurationNotify(duration, newDuration, notifications);
				TemporalPropagation.maybeUpdateParentEnd(this, oldEnd, newEnd, notifications);
			}
		}
	}

	private void setEndNotify(Date oldEnd, Date newEnd, NotificationChain notifications) {
		endTime = newEnd;
		if ((notifications != null) && !CommonUtils.equals(oldEnd, newEnd)) {
			notifications.add(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__END_TIME, oldEnd, newEnd));
		}
	}
	
	private void throwCalculatingError(final String fieldName, final String otherTwo) {
		final StringBuilder msg = new StringBuilder();
		msg.append("Can't change " + fieldName + " because it is set to be calculated as " + otherTwo);
		final EPlanElement pe = getPlanElement();
		if (pe != null)
			msg.append(" -- TemporalMember parented by " + pe.getClass() + ": " + pe.getName());
		throw new UnsupportedOperationException(msg.toString());
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Boolean getScheduled() {
		return scheduled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setScheduled(Boolean newScheduled) {
		Boolean oldScheduled = scheduled;
		scheduled = newScheduled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__SCHEDULED, oldScheduled, scheduled));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isUseChildTimes() {
		return (eFlags & USE_CHILD_TIMES_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setUseChildTimes(boolean newUseChildTimes) {
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		notifications = setUseChildTimes(newUseChildTimes, notifications);
		if (notifications != null) {
			notifications.dispatch();
		}
	}

	NotificationChain setUseChildTimes(boolean newUseChildTimes, NotificationChain notifications) {
		boolean oldUseChildTimes = (eFlags & USE_CHILD_TIMES_EFLAG) != 0;
		if (notifications != null && oldUseChildTimes != newUseChildTimes) {
			notifications.add(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__USE_CHILD_TIMES, oldUseChildTimes, newUseChildTimes));
		}
		if (newUseChildTimes) {
			eFlags |= USE_CHILD_TIMES_EFLAG;
			setUseParentTimes(false, notifications);
			EPlanElement element = getPlanElement();
			if (element != null) { // may reasonably be null during deserialization
				element.eAdapters().add(getChildrenChangedAdapter());
				TemporalPropagation.updateThisParentFromChildren(this, notifications);
			}
		} else {
			eFlags &= ~USE_CHILD_TIMES_EFLAG;
			if (childrenChangedAdapter != null) {
				Notifier target = childrenChangedAdapter.getTarget();
				if (target != null) {
					target.eAdapters().remove(childrenChangedAdapter);
				}
			}
		}
		return notifications;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isUseParentTimes() {
		return (eFlags & USE_PARENT_TIMES_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setUseParentTimes(boolean newUseParentTimes) {
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		notifications = setUseParentTimes(newUseParentTimes, notifications);
		if (notifications != null) {
			notifications.dispatch();
		}
	}

	NotificationChain setUseParentTimes(boolean newUseParentTimes, NotificationChain notifications) {
		boolean oldUseParentTimes = (eFlags & USE_PARENT_TIMES_EFLAG) != 0;
		if (notifications != null && oldUseParentTimes != newUseParentTimes) {
			notifications.add(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__USE_PARENT_TIMES, oldUseParentTimes, newUseParentTimes));
		}
		if (newUseParentTimes) {
			eFlags |= USE_PARENT_TIMES_EFLAG;
			setUseChildTimes(false, notifications);
		} else {
			eFlags &= ~USE_PARENT_TIMES_EFLAG;
		}
		return notifications;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CalculatedVariable getCalculatedVariable() {
		return calculatedVariable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setCalculatedVariable(CalculatedVariable newCalculatedVariable) {
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		setCalculatedVariable(newCalculatedVariable, notifications);
		if (notifications != null) {
			notifications.dispatch();
		}
	}

	private synchronized void setCalculatedVariable(CalculatedVariable newCalculatedVariable, NotificationChain notifications) {
		if (newCalculatedVariable == null) {
			newCalculatedVariable = CALCULATED_VARIABLE_EDEFAULT;
		}
		CalculatedVariable oldCalculatedVariable = calculatedVariable;
		calculatedVariable = newCalculatedVariable;
		if (notifications != null) {
			notifications.add(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__CALCULATED_VARIABLE, oldCalculatedVariable, newCalculatedVariable));
		}
		if ((startTime != null) && (endTime != null)) {
			switch (calculatedVariable) {
			case START:
				Date oldStart = startTime;
				Date newStart = deriveStartTime(duration, endTime);
				setStartNotify(oldStart, newStart, notifications);
				TemporalPropagation.maybeUpdateParentStart(this, oldStart, startTime, notifications);
				break;
			case DURATION:
				Amount<Duration> oldDuration = duration;
				Amount<Duration> newDuration = deriveDuration(startTime, endTime);
				setDurationNotify(oldDuration, newDuration, notifications);
				// Note: Changing the calculated variable to DURATION for a parent that uses child times
				// when we have start and end times can not possibly affect the duration, 
				// so there is no need to propagate this up.
				break;
			case END:
				Date oldEnd = endTime;
				Date newEnd = deriveEndTime(startTime, duration);
				setEndNotify(oldEnd, newEnd, notifications);
				TemporalPropagation.maybeUpdateParentEnd(this, oldEnd, endTime, notifications);
				break;
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date getTimepointDate(Timepoint timepoint) {
		switch (timepoint) {
		case START: return getStartTime();
		case END: return getEndTime();
		}
		return null;
	}

	/**
	 * Returns true if the bounds are calculated 
	 * from this member's endpoints or 
	 * determined by some existing children's endpoints.
	 * 
	 * @return
	 */
	private boolean isBoundsFromEndpoints() {
		if (calculatedVariable == CalculatedVariable.DURATION) {
			return true;
		}
		if (isUseChildTimes()) {
			EPlanElement element = getPlanElement();
			if ((element != null) && !element.getChildren().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public synchronized TemporalExtent getExtent() {
		switch (calculatedVariable) {
		case START:
			if (endTime != null) {
				return new TemporalExtent(duration, endTime);
			} else if (startTime != null) {
				String message = "START: startTime was not null, but endTime was!";
				LogUtil.warn(message);
				if (CommonPlugin.isJunitRunning()) {
					throw new IllegalStateException(message);
				}
			}
			break;
		case DURATION:
			if (startTime != null) {
				if (endTime != null) {
					return new TemporalExtent(startTime, endTime);
				}
				String message = "DURATION: startTime was not null, but endTime was!";
				LogUtil.warn(message);
				if (CommonPlugin.isJunitRunning()) {
					throw new IllegalStateException(message);
				}
			} else if (endTime != null) {
				String message = "DURATION: startTime was null, but endTime was not!";
				LogUtil.warn(message);
				if (CommonPlugin.isJunitRunning()) {
					throw new IllegalStateException(message);
				}
			}
			break;
		case END:
			if (startTime != null) {
				return new TemporalExtent(startTime, duration);
			} else if (endTime != null) {
				String message = "END: startTime was null, but endTime was not!";
				LogUtil.warn(message);
				if (CommonPlugin.isJunitRunning()) {
					throw new IllegalStateException(message);
				}
			}
			break;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public void setExtent(TemporalExtent extent) {
		NotificationChain notifications = null;
		if (eNotificationRequired()) {
			notifications = new NotificationChainImpl();
		}
		setExtent(extent, notifications);
		if (notifications != null) {
			notifications.dispatch();
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Timepoint getStartOffsetTimepoint() {
		return startOffsetTimepoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartOffsetTimepoint(Timepoint newStartOffsetTimepoint) {
		Timepoint oldStartOffsetTimepoint = startOffsetTimepoint;
		startOffsetTimepoint = newStartOffsetTimepoint == null ? START_OFFSET_TIMEPOINT_EDEFAULT : newStartOffsetTimepoint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT, oldStartOffsetTimepoint, startOffsetTimepoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Amount<Duration> getStartOffsetAmount() {
		return startOffsetAmount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartOffsetAmount(Amount<Duration> newStartOffsetAmount) {
		Amount<Duration> oldStartOffsetAmount = startOffsetAmount;
		startOffsetAmount = newStartOffsetAmount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_AMOUNT, oldStartOffsetAmount, startOffsetAmount));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Timepoint getEndOffsetTimepoint() {
		return endOffsetTimepoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndOffsetTimepoint(Timepoint newEndOffsetTimepoint) {
		Timepoint oldEndOffsetTimepoint = endOffsetTimepoint;
		endOffsetTimepoint = newEndOffsetTimepoint == null ? END_OFFSET_TIMEPOINT_EDEFAULT : newEndOffsetTimepoint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT, oldEndOffsetTimepoint, endOffsetTimepoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Amount<Duration> getEndOffsetAmount() {
		return endOffsetAmount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEndOffsetAmount(Amount<Duration> newEndOffsetAmount) {
		Amount<Duration> oldEndOffsetAmount = endOffsetAmount;
		endOffsetAmount = newEndOffsetAmount;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_AMOUNT, oldEndOffsetAmount, endOffsetAmount));
	}

	/* package */ synchronized void setExtent(TemporalExtent extent, NotificationChain notifications) {
		Date oldStart = startTime;
		Amount<Duration> oldDuration = duration;
		Date oldEnd = endTime;
		Date newStart = (extent != null ? extent.getStart() : null);
		Amount<Duration> newDuration = (extent != null ? extent.getDuration() : null);
		Date newEnd = (extent != null ? extent.getEnd() : null);
		setStartNotify(oldStart, newStart, notifications);
		if (newDuration != null) {
			setDurationNotify(oldDuration, newDuration, notifications);
		}
		setEndNotify(oldEnd, newEnd, notifications);
		TemporalPropagation.maybeUpdateParentStartEnd(this, oldStart, newStart, oldEnd, newEnd, notifications);
	}

	/**
	 * Compute a new start time.
	 * 
	 * @param duration
	 * @param endTime
	 * @return 
	 */
	private Date deriveStartTime(Amount<Duration> duration, Date endTime) {
		return DateUtils.subtract(endTime, duration);
	}

	/**
	 * Compute a new duration.
	 * 
	 * @param startTime
	 * @param endTime
	 * @param notifications
	 * @return 
	 */
	private Amount<Duration> deriveDuration(Date startTime, Date endTime) {
		return DateUtils.subtract(endTime, startTime);
	}

	/**
	 * Compute a new end time.
	 * 
	 * @param startTime
	 * @param duration
	 * @return 
	 */
	private Date deriveEndTime(Date startTime, Amount<Duration> duration) {
		return DateUtils.add(startTime, duration);
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String getKey() {
		return TemporalMemberFactory.KEY;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * Temporal members can decide dynamically which feature (start, duration, or end)
	 * is computed from the two input by the user.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean isCalculated(EStructuralFeature feature) {
		switch (feature.getFeatureID()) {
		case TemporalPackage.TEMPORAL_MEMBER__START_TIME:
			return getCalculatedVariable()==CalculatedVariable.START;
		case TemporalPackage.TEMPORAL_MEMBER__DURATION:
			return getCalculatedVariable()==CalculatedVariable.DURATION;
		case TemporalPackage.TEMPORAL_MEMBER__END_TIME:
			return getCalculatedVariable()==CalculatedVariable.END;
		default: return false;
		}
	}
	
	@Override
	public EStructuralFeature getCalculatedVariableFeature() {
		return TemporalPackage.Literals.TEMPORAL_MEMBER__CALCULATED_VARIABLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TemporalPackage.TEMPORAL_MEMBER__START_TIME:
				return getStartTime();
			case TemporalPackage.TEMPORAL_MEMBER__DURATION:
				return getDuration();
			case TemporalPackage.TEMPORAL_MEMBER__END_TIME:
				return getEndTime();
			case TemporalPackage.TEMPORAL_MEMBER__SCHEDULED:
				return getScheduled();
			case TemporalPackage.TEMPORAL_MEMBER__USE_CHILD_TIMES:
				return isUseChildTimes();
			case TemporalPackage.TEMPORAL_MEMBER__USE_PARENT_TIMES:
				return isUseParentTimes();
			case TemporalPackage.TEMPORAL_MEMBER__CALCULATED_VARIABLE:
				return getCalculatedVariable();
			case TemporalPackage.TEMPORAL_MEMBER__EXTENT:
				return getExtent();
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT:
				return getStartOffsetTimepoint();
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_AMOUNT:
				return getStartOffsetAmount();
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT:
				return getEndOffsetTimepoint();
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_AMOUNT:
				return getEndOffsetAmount();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TemporalPackage.TEMPORAL_MEMBER__START_TIME:
				setStartTime((Date)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__DURATION:
				setDuration((Amount<Duration>)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__END_TIME:
				setEndTime((Date)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__SCHEDULED:
				setScheduled((Boolean)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__USE_CHILD_TIMES:
				setUseChildTimes((Boolean)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__USE_PARENT_TIMES:
				setUseParentTimes((Boolean)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__CALCULATED_VARIABLE:
				setCalculatedVariable((CalculatedVariable)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__EXTENT:
				setExtent((TemporalExtent)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT:
				setStartOffsetTimepoint((Timepoint)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_AMOUNT:
				setStartOffsetAmount((Amount<Duration>)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT:
				setEndOffsetTimepoint((Timepoint)newValue);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_AMOUNT:
				setEndOffsetAmount((Amount<Duration>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TemporalPackage.TEMPORAL_MEMBER__START_TIME:
				setStartTime(START_TIME_EDEFAULT);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__DURATION:
				setDuration((Amount<Duration>)null);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__END_TIME:
				setEndTime(END_TIME_EDEFAULT);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__SCHEDULED:
				setScheduled(SCHEDULED_EDEFAULT);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__USE_CHILD_TIMES:
				setUseChildTimes(USE_CHILD_TIMES_EDEFAULT);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__USE_PARENT_TIMES:
				setUseParentTimes(USE_PARENT_TIMES_EDEFAULT);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__CALCULATED_VARIABLE:
				setCalculatedVariable(CALCULATED_VARIABLE_EDEFAULT);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__EXTENT:
				setExtent(EXTENT_EDEFAULT);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT:
				setStartOffsetTimepoint(START_OFFSET_TIMEPOINT_EDEFAULT);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_AMOUNT:
				setStartOffsetAmount((Amount<Duration>)null);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT:
				setEndOffsetTimepoint(END_OFFSET_TIMEPOINT_EDEFAULT);
				return;
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_AMOUNT:
				setEndOffsetAmount((Amount<Duration>)null);
				return;
		}
		super.eUnset(featureID);
	}

	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case TemporalPackage.TEMPORAL_MEMBER__START_TIME:
			if (calculatedVariable == CalculatedVariable.START) {
				return false;
			}
			break;
		case TemporalPackage.TEMPORAL_MEMBER__DURATION:
			if (calculatedVariable == CalculatedVariable.DURATION) {
				return false;
			}
			break;
		case TemporalPackage.TEMPORAL_MEMBER__END_TIME:
			if (calculatedVariable == CalculatedVariable.END) {
				return false;
			}
			break;
		}
		return eIsSetGen(featureID);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSetGen(int featureID) {
		switch (featureID) {
			case TemporalPackage.TEMPORAL_MEMBER__START_TIME:
				return START_TIME_EDEFAULT == null ? startTime != null : !START_TIME_EDEFAULT.equals(startTime);
			case TemporalPackage.TEMPORAL_MEMBER__DURATION:
				return duration != null;
			case TemporalPackage.TEMPORAL_MEMBER__END_TIME:
				return END_TIME_EDEFAULT == null ? endTime != null : !END_TIME_EDEFAULT.equals(endTime);
			case TemporalPackage.TEMPORAL_MEMBER__SCHEDULED:
				return SCHEDULED_EDEFAULT == null ? scheduled != null : !SCHEDULED_EDEFAULT.equals(scheduled);
			case TemporalPackage.TEMPORAL_MEMBER__USE_CHILD_TIMES:
				return ((eFlags & USE_CHILD_TIMES_EFLAG) != 0) != USE_CHILD_TIMES_EDEFAULT;
			case TemporalPackage.TEMPORAL_MEMBER__USE_PARENT_TIMES:
				return ((eFlags & USE_PARENT_TIMES_EFLAG) != 0) != USE_PARENT_TIMES_EDEFAULT;
			case TemporalPackage.TEMPORAL_MEMBER__CALCULATED_VARIABLE:
				return calculatedVariable != CALCULATED_VARIABLE_EDEFAULT;
			case TemporalPackage.TEMPORAL_MEMBER__EXTENT:
				return EXTENT_EDEFAULT == null ? getExtent() != null : !EXTENT_EDEFAULT.equals(getExtent());
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_TIMEPOINT:
				return startOffsetTimepoint != START_OFFSET_TIMEPOINT_EDEFAULT;
			case TemporalPackage.TEMPORAL_MEMBER__START_OFFSET_AMOUNT:
				return startOffsetAmount != null;
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_TIMEPOINT:
				return endOffsetTimepoint != END_OFFSET_TIMEPOINT_EDEFAULT;
			case TemporalPackage.TEMPORAL_MEMBER__END_OFFSET_AMOUNT:
				return endOffsetAmount != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (startTime: ");
		result.append(startTime);
		result.append(", duration: ");
		result.append(duration);
		result.append(", endTime: ");
		result.append(endTime);
		result.append(", scheduled: ");
		result.append(scheduled);
		result.append(", useChildTimes: ");
		result.append((eFlags & USE_CHILD_TIMES_EFLAG) != 0);
		result.append(", useParentTimes: ");
		result.append((eFlags & USE_PARENT_TIMES_EFLAG) != 0);
		result.append(", calculatedVariable: ");
		result.append(calculatedVariable);
		result.append(", startOffsetTimepoint: ");
		result.append(startOffsetTimepoint);
		result.append(", startOffsetAmount: ");
		result.append(startOffsetAmount);
		result.append(", endOffsetTimepoint: ");
		result.append(endOffsetTimepoint);
		result.append(", endOffsetAmount: ");
		result.append(endOffsetAmount);
		result.append(')');
		return result.toString();
	}

} //TemporalMemberImpl
