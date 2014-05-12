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
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.Section;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>ETimeline</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ETimelineImpl#getTopContents <em>Top Contents</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ETimelineImpl#getContents <em>Contents</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ETimelineImpl#getPage <em>Page</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.timeline.model.impl.ETimelineImpl#getRowHeight <em>Row Height</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ETimelineImpl extends MinimalEObjectImpl.Container implements ETimeline {
	/**
	 * The cached value of the '{@link #getTopContents() <em>Top Contents</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTopContents()
	 * @generated
	 * @ordered
	 */
	protected EList<Section> topContents;

	/**
	 * The cached value of the '{@link #getContents() <em>Contents</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContents()
	 * @generated
	 * @ordered
	 */
	protected EList<Section> contents;

	/**
	 * The cached value of the '{@link #getPage() <em>Page</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPage()
	 * @generated
	 * @ordered
	 */
	protected Page page;

	/**
	 * The default value of the '{@link #getRowHeight() <em>Row Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRowHeight()
	 * @generated
	 * @ordered
	 */
	protected static final Integer ROW_HEIGHT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRowHeight() <em>Row Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRowHeight()
	 * @generated
	 * @ordered
	 */
	protected Integer rowHeight = ROW_HEIGHT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ETimelineImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TimelinePackage.Literals.ETIMELINE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Section> getContents() {
		if (contents == null) {
			contents = new EObjectContainmentEList<Section>(Section.class, this, TimelinePackage.ETIMELINE__CONTENTS);
		}
		return contents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPage(Page newPage, NotificationChain msgs) {
		Page oldPage = page;
		page = newPage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TimelinePackage.ETIMELINE__PAGE, oldPage, newPage);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPage(Page newPage) {
		if (newPage != page) {
			NotificationChain msgs = null;
			if (page != null)
				msgs = ((InternalEObject)page).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TimelinePackage.ETIMELINE__PAGE, null, msgs);
			if (newPage != null)
				msgs = ((InternalEObject)newPage).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TimelinePackage.ETIMELINE__PAGE, null, msgs);
			msgs = basicSetPage(newPage, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.ETIMELINE__PAGE, newPage, newPage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer getRowHeight() {
		return rowHeight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRowHeight(Integer newRowHeight) {
		Integer oldRowHeight = rowHeight;
		rowHeight = newRowHeight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TimelinePackage.ETIMELINE__ROW_HEIGHT, oldRowHeight, rowHeight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * Retrieves Sections for top (frozen) and main contents.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Section> getAllContents() {
		EList<Section> allContents = new BasicEList<Section>();
		allContents.addAll(getTopContents());
		allContents.addAll(getContents());
		return allContents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Section> getTopContents() {
		if (topContents == null) {
			topContents = new EObjectContainmentEList<Section>(Section.class, this, TimelinePackage.ETIMELINE__TOP_CONTENTS);
		}
		return topContents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TimelinePackage.ETIMELINE__TOP_CONTENTS:
				return ((InternalEList<?>)getTopContents()).basicRemove(otherEnd, msgs);
			case TimelinePackage.ETIMELINE__CONTENTS:
				return ((InternalEList<?>)getContents()).basicRemove(otherEnd, msgs);
			case TimelinePackage.ETIMELINE__PAGE:
				return basicSetPage(null, msgs);
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
			case TimelinePackage.ETIMELINE__TOP_CONTENTS:
				return getTopContents();
			case TimelinePackage.ETIMELINE__CONTENTS:
				return getContents();
			case TimelinePackage.ETIMELINE__PAGE:
				return getPage();
			case TimelinePackage.ETIMELINE__ROW_HEIGHT:
				return getRowHeight();
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
			case TimelinePackage.ETIMELINE__TOP_CONTENTS:
				getTopContents().clear();
				getTopContents().addAll((Collection<? extends Section>)newValue);
				return;
			case TimelinePackage.ETIMELINE__CONTENTS:
				getContents().clear();
				getContents().addAll((Collection<? extends Section>)newValue);
				return;
			case TimelinePackage.ETIMELINE__PAGE:
				setPage((Page)newValue);
				return;
			case TimelinePackage.ETIMELINE__ROW_HEIGHT:
				setRowHeight((Integer)newValue);
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
			case TimelinePackage.ETIMELINE__TOP_CONTENTS:
				getTopContents().clear();
				return;
			case TimelinePackage.ETIMELINE__CONTENTS:
				getContents().clear();
				return;
			case TimelinePackage.ETIMELINE__PAGE:
				setPage((Page)null);
				return;
			case TimelinePackage.ETIMELINE__ROW_HEIGHT:
				setRowHeight(ROW_HEIGHT_EDEFAULT);
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
			case TimelinePackage.ETIMELINE__TOP_CONTENTS:
				return topContents != null && !topContents.isEmpty();
			case TimelinePackage.ETIMELINE__CONTENTS:
				return contents != null && !contents.isEmpty();
			case TimelinePackage.ETIMELINE__PAGE:
				return page != null;
			case TimelinePackage.ETIMELINE__ROW_HEIGHT:
				return ROW_HEIGHT_EDEFAULT == null ? rowHeight != null : !ROW_HEIGHT_EDEFAULT.equals(rowHeight);
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
		result.append(" (rowHeight: ");
		result.append(rowHeight);
		result.append(')');
		return result.toString();
	}

} //ETimelineImpl
