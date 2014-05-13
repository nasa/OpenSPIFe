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
package gov.nasa.ensemble.core.model.plan.impl;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanHierarchyPositionMaintenerService;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.emf.SafeAdapter;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EPlan Child</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanChildImpl#getListPosition <em>List Position</em>}</li>
 *   <li>{@link gov.nasa.ensemble.core.model.plan.impl.EPlanChildImpl#getHierarchyPosition <em>Hierarchy Position</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class EPlanChildImpl extends EPlanElementImpl implements EPlanChild {
	
	/**
	 * The default value of the '{@link #getListPosition() <em>List Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getListPosition()
	 * @generated
	 * @ordered
	 */
	protected static final int LIST_POSITION_EDEFAULT = -1;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected EPlanChildImpl() {
		super();
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * Return the plan child class object.
	 * @return the plan child class object
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PlanPackage.Literals.EPLAN_CHILD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * The index of this plan child in the list of siblings. It is cached so that it does not
	 * have to be computed on each call.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	private int listPosition = -1;
	/**
	 * The default value of the '{@link #getHierarchyPosition() <em>Hierarchy Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHierarchyPosition()
	 * @generated
	 * @ordered
	 */
	protected static final int HIERARCHY_POSITION_EDEFAULT = -1;
	/**
	 * The cached value of the '{@link #getHierarchyPosition() <em>Hierarchy Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHierarchyPosition()
	 * @generated
	 * @ordered
	 */
	protected int hierarchyPosition = HIERARCHY_POSITION_EDEFAULT;

	/**
	 * If this plan child has no parent, return -1. If the locally-stored list position index does
	 * in fact index this plan child within the list of its siblings, return that index.
	 * Otherwise, store and return the index of this plan child within the collection of siblings.
	 * @return the index of this plan child in the list of siblings
	 */
	public int getListPosition() {
		EPlanElement parent = getParent();
		if (parent == null) {
			return -1;
		}
		List<? extends EPlanChild> groups = parent.getChildren();
		int pos = this.listPosition;
		if ((pos >= 0) && (pos < groups.size()) && (groups.get(pos) == this)) {
        	return pos; // lucky!
        }
        pos = groups.indexOf(this);
        this.listPosition = pos;
		return pos;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setListPosition(int newListPosition) {
		int oldListPosition = listPosition;
		listPosition = newListPosition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN_CHILD__LIST_POSITION, oldListPosition, listPosition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getHierarchyPosition() {
		EPlan plan = EPlanUtils.getPlan(this);
		if (plan == null) {
			hierarchyPosition = -1;
		} else if (!PlanHierarchyPositionMaintenerService.isHierarchyPositionMaintained() || (hierarchyPosition == -1)) {
			hierarchyPosition = PlanHierarchyPositionMaintenerService.getHierarchyPosition(this);
		}
		return hierarchyPosition;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setHierarchyPosition(int newHierarchyPosition) {
		if (hierarchyPosition != newHierarchyPosition) {
			int oldHierarchyPosition = hierarchyPosition;
			hierarchyPosition = newHierarchyPosition;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET, PlanPackage.EPLAN_CHILD__HIERARCHY_POSITION, oldHierarchyPosition, hierarchyPosition));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * Returns the EPlanElement containing this EPlanChild.
	 * If the EPlanChild is not contained by an EPlanElement, returns null.
	 * @return the EPlanElement containing this EPlanChild
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
    public EPlanElement getParent() {
		EObject container = eContainer();
		if (container instanceof EPlanElement) {
			return (EPlanElement)container;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Handle the cases in which the featureID designates the listPosition or hierarchyPosition
	 * properties; otherwise forward to the superclass.
	 * @param featureID designates the property
	 * @param resolve
	 * @param coreType
	 * @return the value of the designated property
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PlanPackage.EPLAN_CHILD__LIST_POSITION:
				return getListPosition();
			case PlanPackage.EPLAN_CHILD__HIERARCHY_POSITION:
				return getHierarchyPosition();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Handle the case in which the featureID designates the listPosition or hierarchyPosition
	 * property; otherwise forward to the superclass.
	 * @param featureID designates the property
	 * @param newValue the parent name to set; must be a String
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PlanPackage.EPLAN_CHILD__LIST_POSITION:
				setListPosition((Integer)newValue);
				return;
			case PlanPackage.EPLAN_CHILD__HIERARCHY_POSITION:
				setHierarchyPosition((Integer)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Handle the case in which the featureID designates the listPosition or hierarchyPosition
	 * property; otherwise forward to the superclass.
	 * @param featureID designates the property
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PlanPackage.EPLAN_CHILD__LIST_POSITION:
				setListPosition(LIST_POSITION_EDEFAULT);
				return;
			case PlanPackage.EPLAN_CHILD__HIERARCHY_POSITION:
				setHierarchyPosition(HIERARCHY_POSITION_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Handle the cases in which the featureID designates the listPosition or hierarchyPosition
	 * property; otherwise forward to the superclass.
	 * @param featureID designates the property
	 * @return whether the property is set
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PlanPackage.EPLAN_CHILD__LIST_POSITION:
				return listPosition != LIST_POSITION_EDEFAULT;
			case PlanPackage.EPLAN_CHILD__HIERARCHY_POSITION:
				return hierarchyPosition != HIERARCHY_POSITION_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * Append the parenthesized list position and parent name to the print name if it is not a
	 * proxy.
	 * @return the print name
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (listPosition: ");
		result.append(listPosition);
		result.append(", hierarchyPosition: ");
		result.append(hierarchyPosition);
		result.append(')');
		return result.toString();
	}

} //EPlanChildImpl
