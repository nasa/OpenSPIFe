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

import gov.nasa.arc.spife.PageUtils;
import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.ZoomOption;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;

import java.util.Collection;
import java.util.Date;

import javax.measure.quantity.Duration;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.jscience.physics.amount.Amount;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Page</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.PageImpl#getCurrentPageExtent <em>Current Page Extent</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.PageImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.PageImpl#getMilliSecondsPerPixel <em>Milli Seconds Per Pixel</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.PageImpl#getStartTime <em>Start Time</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.PageImpl#getWidth <em>Width</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.PageImpl#getZoomOption <em>Zoom Option</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.PageImpl#getZoomOptions <em>Zoom Options</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PageImpl extends MinimalEObjectImpl.Container implements Page {
	
	/**
	 * The default value of the '{@link #getCurrentPageExtent() <em>Current Page Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentPageExtent()
	 * @generated
	 * @ordered
	 */
	protected static final TemporalExtent CURRENT_PAGE_EXTENT_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getCurrentPageExtent() <em>Current Page Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentPageExtent()
	 * @generated
	 * @ordered
	 */
	protected TemporalExtent currentPageExtent = CURRENT_PAGE_EXTENT_EDEFAULT;
	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated NOT
	 * @ordered
	 */
	protected static final Amount<Duration> DURATION_EDEFAULT = DateUtils.ZERO_DURATION;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated NOT
	 * @ordered
	 */
	protected Amount<Duration> duration = DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getMilliSecondsPerPixel() <em>Milli Seconds Per Pixel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMilliSecondsPerPixel()
	 * @generated
	 * @ordered
	 */
	protected static final long MILLI_SECONDS_PER_PIXEL_EDEFAULT = 0L;

	/**
	 * The default value of the '{@link #getStartTime() <em>Start Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartTime()
	 * @generated NOT
	 * @ordered
	 */
	protected static final Date START_TIME_EDEFAULT = new Date();

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
	 * The default value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
	protected static final int WIDTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getZoomOption() <em>Zoom Option</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZoomOption()
	 * @generated
	 * @ordered
	 */
	protected ZoomOption zoomOption;

	/**
	 * The cached value of the '{@link #getZoomOptions() <em>Zoom Options</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZoomOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<ZoomOption> zoomOptions;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected PageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TimelinePackage.Literals.PAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TemporalExtent getCurrentPageExtent() {
		return currentPageExtent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public TemporalExtent getExtent() {
		return new TemporalExtent(getStartTime(), getDuration());
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentPageExtent(TemporalExtent newCurrentPageExtent) {
		TemporalExtent oldCurrentPageExtent = currentPageExtent;
		currentPageExtent = newCurrentPageExtent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.PAGE__CURRENT_PAGE_EXTENT, oldCurrentPageExtent, currentPageExtent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Amount<Duration> getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setDuration(Amount<Duration> newDuration) {
		if (duration != null 
				&& newDuration != null
				&& duration.approximates(newDuration)) {
			return;
		}
		Amount<Duration> oldDuration = duration;
		duration = newDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.PAGE__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * Convenience function to convert milliseconds to pixels
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	Long millSecondsPerPixel = null;
	public long getMilliSecondsPerPixel() {
		if (millSecondsPerPixel == null) {
			ZoomOption option = getZoomOption();
			millSecondsPerPixel = option == null ? 0 : option.getMsInterval() / PIXELS_PER_INCH;
		}
		return millSecondsPerPixel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setStartTime(Date newStartTime) {
		if (CommonUtils.equals(startTime, newStartTime)) {
			return;
		}
		Date oldStartTime = startTime;
		startTime = newStartTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.PAGE__START_TIME, oldStartTime, startTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * Convenience method to get the width (in pixels) of the timeline page
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getWidth() {
		long mspp = getMilliSecondsPerPixel();
		long milliseconds = duration.longValue(DateUtils.MILLISECONDS);
		int w = mspp == 0 ? 0 : (int)(milliseconds/mspp);
		if (PageUtils.isPagingEnabled()) {
			return Math.min(10000, w);
		}
		return w;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ZoomOption getZoomOption() {
		if (zoomOption != null && zoomOption.eIsProxy()) {
			InternalEObject oldZoomOption = (InternalEObject)zoomOption;
			zoomOption = (ZoomOption)eResolveProxy(oldZoomOption);
			if (zoomOption != oldZoomOption) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TimelinePackage.PAGE__ZOOM_OPTION, oldZoomOption, zoomOption));
			}
		}
		return zoomOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ZoomOption basicGetZoomOption() {
		return zoomOption;
	}

	public void setZoomOption(ZoomOption newZoomOption) {
		millSecondsPerPixel = null;
		setZoomOptionGen(newZoomOption);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */	
	public void setZoomOptionGen(ZoomOption newZoomOption) {
		ZoomOption oldZoomOption = zoomOption;
		zoomOption = newZoomOption;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.PAGE__ZOOM_OPTION, oldZoomOption, zoomOption));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ZoomOption> getZoomOptions() {
		if (zoomOptions == null) {
			zoomOptions = new EObjectContainmentEList<ZoomOption>(ZoomOption.class, this, TimelinePackage.PAGE__ZOOM_OPTIONS);
		}
		return zoomOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Convert the pixel count into duration
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public long convertToMilliseconds(int screenWidth) {
		return getMilliSecondsPerPixel() * screenWidth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Convert the duration into pixel count
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public long convertToPixels(long time) {
		return time/getMilliSecondsPerPixel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * Convert the position on the timeline into a time value
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Date getTime(long screenPosition) {
		return new Date(getStartTime().getTime() + getMilliSecondsPerPixel()*screenPosition);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Given the time, get the screen position on the timeline
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getScreenPosition(Date time) {
		long offset = (time.getTime() - getStartTime().getTime());
		int position = (int)(offset/getMilliSecondsPerPixel());
		return position;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TimelinePackage.PAGE__ZOOM_OPTIONS:
				return ((InternalEList<?>)getZoomOptions()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TimelinePackage.PAGE__CURRENT_PAGE_EXTENT:
				return getCurrentPageExtent();
			case TimelinePackage.PAGE__DURATION:
				return getDuration();
			case TimelinePackage.PAGE__MILLI_SECONDS_PER_PIXEL:
				return getMilliSecondsPerPixel();
			case TimelinePackage.PAGE__START_TIME:
				return getStartTime();
			case TimelinePackage.PAGE__WIDTH:
				return getWidth();
			case TimelinePackage.PAGE__ZOOM_OPTION:
				if (resolve) return getZoomOption();
				return basicGetZoomOption();
			case TimelinePackage.PAGE__ZOOM_OPTIONS:
				return getZoomOptions();
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
			case TimelinePackage.PAGE__CURRENT_PAGE_EXTENT:
				setCurrentPageExtent((TemporalExtent)newValue);
				return;
			case TimelinePackage.PAGE__DURATION:
				setDuration((Amount<Duration>)newValue);
				return;
			case TimelinePackage.PAGE__START_TIME:
				setStartTime((Date)newValue);
				return;
			case TimelinePackage.PAGE__ZOOM_OPTION:
				setZoomOption((ZoomOption)newValue);
				return;
			case TimelinePackage.PAGE__ZOOM_OPTIONS:
				getZoomOptions().clear();
				getZoomOptions().addAll((Collection<? extends ZoomOption>)newValue);
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
			case TimelinePackage.PAGE__CURRENT_PAGE_EXTENT:
				setCurrentPageExtent(CURRENT_PAGE_EXTENT_EDEFAULT);
				return;
			case TimelinePackage.PAGE__DURATION:
				setDuration((Amount<Duration>)null);
				return;
			case TimelinePackage.PAGE__START_TIME:
				setStartTime(START_TIME_EDEFAULT);
				return;
			case TimelinePackage.PAGE__ZOOM_OPTION:
				setZoomOption((ZoomOption)null);
				return;
			case TimelinePackage.PAGE__ZOOM_OPTIONS:
				getZoomOptions().clear();
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
			case TimelinePackage.PAGE__CURRENT_PAGE_EXTENT:
				return CURRENT_PAGE_EXTENT_EDEFAULT == null ? currentPageExtent != null : !CURRENT_PAGE_EXTENT_EDEFAULT.equals(currentPageExtent);
			case TimelinePackage.PAGE__DURATION:
				return duration != null;
			case TimelinePackage.PAGE__MILLI_SECONDS_PER_PIXEL:
				return getMilliSecondsPerPixel() != MILLI_SECONDS_PER_PIXEL_EDEFAULT;
			case TimelinePackage.PAGE__START_TIME:
				return START_TIME_EDEFAULT == null ? startTime != null : !START_TIME_EDEFAULT.equals(startTime);
			case TimelinePackage.PAGE__WIDTH:
				return getWidth() != WIDTH_EDEFAULT;
			case TimelinePackage.PAGE__ZOOM_OPTION:
				return zoomOption != null;
			case TimelinePackage.PAGE__ZOOM_OPTIONS:
				return zoomOptions != null && !zoomOptions.isEmpty();
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
		result.append(" (currentPageExtent: ");
		result.append(currentPageExtent);
		result.append(", duration: ");
		result.append(duration);
		result.append(", startTime: ");
		result.append(startTime);
		result.append(')');
		return result.toString();
	}

} //PageImpl
