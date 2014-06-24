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
package gov.nasa.arc.spife.ui.timeline.chart.model.impl;

import gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.LineChart;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Line Chart</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.LineChartImpl#getLines <em>Lines</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.LineChartImpl#getMaximumLine <em>Maximum Line</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.LineChartImpl#getMinimumLine <em>Minimum Line</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LineChartImpl extends ChartImpl implements LineChart {
	/**
	 * The cached value of the '{@link #getMaximumLine() <em>Maximum Line</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximumLine()
	 * @generated
	 * @ordered
	 */
	protected AmountLine maximumLine;
	/**
	 * The cached value of the '{@link #getMinimumLine() <em>Minimum Line</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimumLine()
	 * @generated
	 * @ordered
	 */
	protected AmountLine minimumLine;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected LineChartImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChartPackage.Literals.LINE_CHART;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<AmountLine> getLines() {
		EList list = new BasicEList<AmountLine>();
		if (getMinimumLine() != null) list.add(getMinimumLine());
		if (getMaximumLine() != null) list.add(getMaximumLine());
		return list;
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AmountLine getMaximumLine() {
		return maximumLine;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMaximumLine(AmountLine newMaximumLine, NotificationChain msgs) {
		AmountLine oldMaximumLine = maximumLine;
		maximumLine = newMaximumLine;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChartPackage.LINE_CHART__MAXIMUM_LINE, oldMaximumLine, newMaximumLine);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaximumLine(AmountLine newMaximumLine) {
		if (newMaximumLine != maximumLine) {
			NotificationChain msgs = null;
			if (maximumLine != null)
				msgs = ((InternalEObject)maximumLine).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChartPackage.LINE_CHART__MAXIMUM_LINE, null, msgs);
			if (newMaximumLine != null)
				msgs = ((InternalEObject)newMaximumLine).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChartPackage.LINE_CHART__MAXIMUM_LINE, null, msgs);
			msgs = basicSetMaximumLine(newMaximumLine, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.LINE_CHART__MAXIMUM_LINE, newMaximumLine, newMaximumLine));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AmountLine getMinimumLine() {
		return minimumLine;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMinimumLine(AmountLine newMinimumLine, NotificationChain msgs) {
		AmountLine oldMinimumLine = minimumLine;
		minimumLine = newMinimumLine;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ChartPackage.LINE_CHART__MINIMUM_LINE, oldMinimumLine, newMinimumLine);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMinimumLine(AmountLine newMinimumLine) {
		if (newMinimumLine != minimumLine) {
			NotificationChain msgs = null;
			if (minimumLine != null)
				msgs = ((InternalEObject)minimumLine).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ChartPackage.LINE_CHART__MINIMUM_LINE, null, msgs);
			if (newMinimumLine != null)
				msgs = ((InternalEObject)newMinimumLine).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ChartPackage.LINE_CHART__MINIMUM_LINE, null, msgs);
			msgs = basicSetMinimumLine(newMinimumLine, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.LINE_CHART__MINIMUM_LINE, newMinimumLine, newMinimumLine));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChartPackage.LINE_CHART__LINES:
				return ((InternalEList<?>)getLines()).basicRemove(otherEnd, msgs);
			case ChartPackage.LINE_CHART__MAXIMUM_LINE:
				return basicSetMaximumLine(null, msgs);
			case ChartPackage.LINE_CHART__MINIMUM_LINE:
				return basicSetMinimumLine(null, msgs);
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
			case ChartPackage.LINE_CHART__LINES:
				return getLines();
			case ChartPackage.LINE_CHART__MAXIMUM_LINE:
				return getMaximumLine();
			case ChartPackage.LINE_CHART__MINIMUM_LINE:
				return getMinimumLine();
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
			case ChartPackage.LINE_CHART__LINES:
				getLines().clear();
				getLines().addAll((Collection<? extends AmountLine>)newValue);
				return;
			case ChartPackage.LINE_CHART__MAXIMUM_LINE:
				setMaximumLine((AmountLine)newValue);
				return;
			case ChartPackage.LINE_CHART__MINIMUM_LINE:
				setMinimumLine((AmountLine)newValue);
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
			case ChartPackage.LINE_CHART__LINES:
				getLines().clear();
				return;
			case ChartPackage.LINE_CHART__MAXIMUM_LINE:
				setMaximumLine((AmountLine)null);
				return;
			case ChartPackage.LINE_CHART__MINIMUM_LINE:
				setMinimumLine((AmountLine)null);
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
			case ChartPackage.LINE_CHART__LINES:
				return !getLines().isEmpty();
			case ChartPackage.LINE_CHART__MAXIMUM_LINE:
				return maximumLine != null;
			case ChartPackage.LINE_CHART__MINIMUM_LINE:
				return minimumLine != null;
		}
		return super.eIsSet(featureID);
	}

} //LineChartImpl
