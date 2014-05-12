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
package gov.nasa.arc.spife.timeline.model.impl;

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.ZoomOption;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Zoom Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl#getMajorTickInterval <em>Major Tick Interval</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl#getMinorTickInterval <em>Minor Tick Interval</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl#getMsInterval <em>Ms Interval</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl#getMsMoveThreshold <em>Ms Move Threshold</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl#getMsNudgeThreshold <em>Ms Nudge Threshold</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ZoomOptionImpl#getScrollInterval <em>Scroll Interval</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ZoomOptionImpl extends MinimalEObjectImpl.Container implements ZoomOption {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getMajorTickInterval() <em>Major Tick Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMajorTickInterval()
	 * @generated
	 * @ordered
	 */
	protected static final long MAJOR_TICK_INTERVAL_EDEFAULT = 86400000L;

	/**
	 * The cached value of the '{@link #getMajorTickInterval() <em>Major Tick Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMajorTickInterval()
	 * @generated
	 * @ordered
	 */
	protected long majorTickInterval = MAJOR_TICK_INTERVAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinorTickInterval() <em>Minor Tick Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinorTickInterval()
	 * @generated
	 * @ordered
	 */
	protected static final long MINOR_TICK_INTERVAL_EDEFAULT = 3600000L;

	/**
	 * The cached value of the '{@link #getMinorTickInterval() <em>Minor Tick Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinorTickInterval()
	 * @generated
	 * @ordered
	 */
	protected long minorTickInterval = MINOR_TICK_INTERVAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getMsInterval() <em>Ms Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMsInterval()
	 * @generated
	 * @ordered
	 */
	protected static final long MS_INTERVAL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMsInterval() <em>Ms Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMsInterval()
	 * @generated
	 * @ordered
	 */
	protected long msInterval = MS_INTERVAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getMsMoveThreshold() <em>Ms Move Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMsMoveThreshold()
	 * @generated
	 * @ordered
	 */
	protected static final long MS_MOVE_THRESHOLD_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMsMoveThreshold() <em>Ms Move Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMsMoveThreshold()
	 * @generated
	 * @ordered
	 */
	protected long msMoveThreshold = MS_MOVE_THRESHOLD_EDEFAULT;

	/**
	 * The default value of the '{@link #getMsNudgeThreshold() <em>Ms Nudge Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMsNudgeThreshold()
	 * @generated
	 * @ordered
	 */
	protected static final long MS_NUDGE_THRESHOLD_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMsNudgeThreshold() <em>Ms Nudge Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMsNudgeThreshold()
	 * @generated
	 * @ordered
	 */
	protected long msNudgeThreshold = MS_NUDGE_THRESHOLD_EDEFAULT;

	/**
	 * The cached value of the '{@link #getScrollInterval() <em>Scroll Interval</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScrollInterval()
	 * @generated
	 * @ordered
	 */
	protected Amount<Duration> scrollInterval;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ZoomOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TimelinePackage.Literals.ZOOM_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.ZOOM_OPTION__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMajorTickInterval() {
		return majorTickInterval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMajorTickInterval(long newMajorTickInterval) {
		long oldMajorTickInterval = majorTickInterval;
		majorTickInterval = newMajorTickInterval;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.ZOOM_OPTION__MAJOR_TICK_INTERVAL, oldMajorTickInterval, majorTickInterval));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMinorTickInterval() {
		return minorTickInterval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinorTickInterval(long newMinorTickInterval) {
		long oldMinorTickInterval = minorTickInterval;
		minorTickInterval = newMinorTickInterval;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.ZOOM_OPTION__MINOR_TICK_INTERVAL, oldMinorTickInterval, minorTickInterval));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMsInterval() {
		return msInterval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMsInterval(long newMsInterval) {
		long oldMsInterval = msInterval;
		msInterval = newMsInterval;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.ZOOM_OPTION__MS_INTERVAL, oldMsInterval, msInterval));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMsMoveThreshold() {
		return msMoveThreshold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMsMoveThreshold(long newMsMoveThreshold) {
		long oldMsMoveThreshold = msMoveThreshold;
		msMoveThreshold = newMsMoveThreshold;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.ZOOM_OPTION__MS_MOVE_THRESHOLD, oldMsMoveThreshold, msMoveThreshold));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMsNudgeThreshold() {
		return msNudgeThreshold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMsNudgeThreshold(long newMsNudgeThreshold) {
		long oldMsNudgeThreshold = msNudgeThreshold;
		msNudgeThreshold = newMsNudgeThreshold;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.ZOOM_OPTION__MS_NUDGE_THRESHOLD, oldMsNudgeThreshold, msNudgeThreshold));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Amount<Duration> getScrollInterval() {
		return scrollInterval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScrollInterval(Amount<Duration> newScrollInterval) {
		Amount<Duration> oldScrollInterval = scrollInterval;
		scrollInterval = newScrollInterval;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.ZOOM_OPTION__SCROLL_INTERVAL, oldScrollInterval, scrollInterval));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TimelinePackage.ZOOM_OPTION__NAME:
				return getName();
			case TimelinePackage.ZOOM_OPTION__MAJOR_TICK_INTERVAL:
				return getMajorTickInterval();
			case TimelinePackage.ZOOM_OPTION__MINOR_TICK_INTERVAL:
				return getMinorTickInterval();
			case TimelinePackage.ZOOM_OPTION__MS_INTERVAL:
				return getMsInterval();
			case TimelinePackage.ZOOM_OPTION__MS_MOVE_THRESHOLD:
				return getMsMoveThreshold();
			case TimelinePackage.ZOOM_OPTION__MS_NUDGE_THRESHOLD:
				return getMsNudgeThreshold();
			case TimelinePackage.ZOOM_OPTION__SCROLL_INTERVAL:
				return getScrollInterval();
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
			case TimelinePackage.ZOOM_OPTION__NAME:
				setName((String)newValue);
				return;
			case TimelinePackage.ZOOM_OPTION__MAJOR_TICK_INTERVAL:
				setMajorTickInterval((Long)newValue);
				return;
			case TimelinePackage.ZOOM_OPTION__MINOR_TICK_INTERVAL:
				setMinorTickInterval((Long)newValue);
				return;
			case TimelinePackage.ZOOM_OPTION__MS_INTERVAL:
				setMsInterval((Long)newValue);
				return;
			case TimelinePackage.ZOOM_OPTION__MS_MOVE_THRESHOLD:
				setMsMoveThreshold((Long)newValue);
				return;
			case TimelinePackage.ZOOM_OPTION__MS_NUDGE_THRESHOLD:
				setMsNudgeThreshold((Long)newValue);
				return;
			case TimelinePackage.ZOOM_OPTION__SCROLL_INTERVAL:
				setScrollInterval((Amount<Duration>)newValue);
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
			case TimelinePackage.ZOOM_OPTION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TimelinePackage.ZOOM_OPTION__MAJOR_TICK_INTERVAL:
				setMajorTickInterval(MAJOR_TICK_INTERVAL_EDEFAULT);
				return;
			case TimelinePackage.ZOOM_OPTION__MINOR_TICK_INTERVAL:
				setMinorTickInterval(MINOR_TICK_INTERVAL_EDEFAULT);
				return;
			case TimelinePackage.ZOOM_OPTION__MS_INTERVAL:
				setMsInterval(MS_INTERVAL_EDEFAULT);
				return;
			case TimelinePackage.ZOOM_OPTION__MS_MOVE_THRESHOLD:
				setMsMoveThreshold(MS_MOVE_THRESHOLD_EDEFAULT);
				return;
			case TimelinePackage.ZOOM_OPTION__MS_NUDGE_THRESHOLD:
				setMsNudgeThreshold(MS_NUDGE_THRESHOLD_EDEFAULT);
				return;
			case TimelinePackage.ZOOM_OPTION__SCROLL_INTERVAL:
				setScrollInterval((Amount<Duration>)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TimelinePackage.ZOOM_OPTION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TimelinePackage.ZOOM_OPTION__MAJOR_TICK_INTERVAL:
				return majorTickInterval != MAJOR_TICK_INTERVAL_EDEFAULT;
			case TimelinePackage.ZOOM_OPTION__MINOR_TICK_INTERVAL:
				return minorTickInterval != MINOR_TICK_INTERVAL_EDEFAULT;
			case TimelinePackage.ZOOM_OPTION__MS_INTERVAL:
				return msInterval != MS_INTERVAL_EDEFAULT;
			case TimelinePackage.ZOOM_OPTION__MS_MOVE_THRESHOLD:
				return msMoveThreshold != MS_MOVE_THRESHOLD_EDEFAULT;
			case TimelinePackage.ZOOM_OPTION__MS_NUDGE_THRESHOLD:
				return msNudgeThreshold != MS_NUDGE_THRESHOLD_EDEFAULT;
			case TimelinePackage.ZOOM_OPTION__SCROLL_INTERVAL:
				return scrollInterval != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * We just want the 'name' property to be returned
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();
		return getName();
	}

} //ZoomOptionImpl
