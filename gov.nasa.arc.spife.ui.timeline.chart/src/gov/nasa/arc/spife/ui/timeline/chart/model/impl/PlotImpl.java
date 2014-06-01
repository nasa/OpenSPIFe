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

import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.FitPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.util.PlotRgbRegistry;
import gov.nasa.ensemble.common.ui.color.RGBMap;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.Profile;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.RGB;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource Plot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl#getChart <em>Chart</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl#getExtent <em>Extent</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl#getRgb <em>Rgb</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl#getFit <em>Fit</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl#getProfile <em>Profile</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl#isShowText <em>Show Text</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl#isAutoAssignRGB <em>Auto Assign RGB</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.ui.timeline.chart.model.impl.PlotImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PlotImpl extends MinimalEObjectImpl.Container implements Plot {
	
	/**
	 * int field to store booleans and enums
	 */
	protected int eFlags = 0;
	
	/**
	 * The cached value of the '{@link #getExtent() <em>Extent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtent()
	 * @generated
	 * @ordered
	 */
	protected AmountExtent<?> extent;
	/**
	 * The default value of the '{@link #getRgb() <em>Rgb</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRgb()
	 * @generated
	 * @ordered
	 */
	protected static final RGB RGB_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getRgb() <em>Rgb</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRgb()
	 * @generated
	 * @ordered
	 */
	protected RGB rgb = RGB_EDEFAULT;
	/**
	 * The default value of the '{@link #getFit() <em>Fit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFit()
	 * @generated
	 * @ordered
	 */
	protected static final FitPolicy FIT_EDEFAULT = FitPolicy.AUTO;
	/**
	 * The cached value of the '{@link #getFit() <em>Fit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFit()
	 * @generated
	 * @ordered
	 */
	protected FitPolicy fit = FIT_EDEFAULT;
	/**
	 * The cached value of the '{@link #getProfile() <em>ResourceProfile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfile()
	 */
	protected Profile profile;

	/**
	 * The default value of the '{@link #isShowText() <em>Show Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowText()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_TEXT_EDEFAULT = false;
	/**
	 * The flag representing the value of the '{@link #isShowText() <em>Show Text</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowText()
	 * @generated
	 * @ordered
	 */
	protected static final int SHOW_TEXT_EFLAG = 1 << 8;
	/**
	 * The default value of the '{@link #isAutoAssignRGB() <em>Auto Assign RGB</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutoAssignRGB()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AUTO_ASSIGN_RGB_EDEFAULT = false;
	/**
	 * The flag representing the value of the '{@link #isAutoAssignRGB() <em>Auto Assign RGB</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutoAssignRGB()
	 * @generated
	 * @ordered
	 */
	protected static final int AUTO_ASSIGN_RGB_EFLAG = 1 << 9;

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
	private static final RGBMap<String> rgbMap = new RGBMap<String>();
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ChartPackage.Literals.PLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Chart getChart() {
		if (eContainerFeatureID() != ChartPackage.PLOT__CHART) return null;
		return (Chart)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetChart(Chart newChart, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newChart, ChartPackage.PLOT__CHART, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setChart(Chart newChart) {
		if (newChart != eInternalContainer() || (eContainerFeatureID() != ChartPackage.PLOT__CHART && newChart != null)) {
			if (EcoreUtil.isAncestor(this, newChart))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newChart != null)
				msgs = ((InternalEObject)newChart).eInverseAdd(this, ChartPackage.CHART__PLOTS, Chart.class, msgs);
			msgs = basicSetChart(newChart, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.PLOT__CHART, newChart, newChart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public AmountExtent<?> getExtent() {
		FitPolicy policy = getFit();
		switch (policy) {
			case AUTO:
				Profile profile = getProfile();
				if (profile != null) {
					return profile.getExtent();
				}
				return null;
			case CUSTOM:
				return extent;
		}
		return extent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExtent(AmountExtent<?> newExtent) {
		AmountExtent<?> oldExtent = extent;
		extent = newExtent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.PLOT__EXTENT, oldExtent, extent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getName() {
		if (name == null && getProfile() != null) {
			return getProfile().getId();
		}
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.PLOT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * Gets the color (in red/green/blue space) associated with the plot as a whole.
	 * The order of precedence is:
	 * <ol>
	 *   <li> The value specified by the user in this session or persisted in the workspace (in an XML resource) from last session.
	 *   <li> The value specified in ensemble.properties as <plotId>.color=#hexcode
	 *   <li> A new color arbitrarily chosen from unused bright colors.
	 * </ol>
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public RGB getRgb() {
		if (rgb == null) {			
			RGB newRgb = PlotRgbRegistry.INSTANCE.getColorForEntirePlot(this);
			if (newRgb != null) rgb = newRgb;
		}
		if (rgb == null) {
			rgb = rgbMap.getRGB(getName());
		}
		return rgb;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRgb(RGB newRgb) {
		RGB oldRgb = rgb;
		rgb = newRgb;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.PLOT__RGB, oldRgb, rgb));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FitPolicy getFit() {
		return fit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFit(FitPolicy newFit) {
		FitPolicy oldFit = fit;
		fit = newFit == null ? FIT_EDEFAULT : newFit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.PLOT__FIT, oldFit, fit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Profile<?> getProfile() {
		if (profile != null && profile.eIsProxy()) {
			InternalEObject oldProfile = (InternalEObject)profile;
			profile = (Profile<?>)eResolveProxy(oldProfile);
			if (profile != oldProfile) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ChartPackage.PLOT__PROFILE, oldProfile, profile));
			}
		}
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Profile<?> basicGetProfile() {
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProfile(Profile<?> newProfile) {
		Profile<?> oldProfile = profile;
		profile = newProfile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.PLOT__PROFILE, oldProfile, profile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isShowText() {
		return (eFlags & SHOW_TEXT_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShowText(boolean newShowText) {
		boolean oldShowText = (eFlags & SHOW_TEXT_EFLAG) != 0;
		if (newShowText) eFlags |= SHOW_TEXT_EFLAG; else eFlags &= ~SHOW_TEXT_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.PLOT__SHOW_TEXT, oldShowText, newShowText));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isAutoAssignRGB() {
		return (eFlags & AUTO_ASSIGN_RGB_EFLAG) != 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAutoAssignRGB(boolean newAutoAssignRGB) {
		boolean oldAutoAssignRGB = (eFlags & AUTO_ASSIGN_RGB_EFLAG) != 0;
		if (newAutoAssignRGB) eFlags |= AUTO_ASSIGN_RGB_EFLAG; else eFlags &= ~AUTO_ASSIGN_RGB_EFLAG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ChartPackage.PLOT__AUTO_ASSIGN_RGB, oldAutoAssignRGB, newAutoAssignRGB));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChartPackage.PLOT__CHART:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetChart((Chart)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ChartPackage.PLOT__CHART:
				return basicSetChart(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case ChartPackage.PLOT__CHART:
				return eInternalContainer().eInverseRemove(this, ChartPackage.CHART__PLOTS, Chart.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ChartPackage.PLOT__CHART:
				return getChart();
			case ChartPackage.PLOT__EXTENT:
				return getExtent();
			case ChartPackage.PLOT__RGB:
				return getRgb();
			case ChartPackage.PLOT__FIT:
				return getFit();
			case ChartPackage.PLOT__PROFILE:
				if (resolve) return getProfile();
				return basicGetProfile();
			case ChartPackage.PLOT__SHOW_TEXT:
				return isShowText();
			case ChartPackage.PLOT__AUTO_ASSIGN_RGB:
				return isAutoAssignRGB();
			case ChartPackage.PLOT__NAME:
				return getName();
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
			case ChartPackage.PLOT__CHART:
				setChart((Chart)newValue);
				return;
			case ChartPackage.PLOT__EXTENT:
				setExtent((AmountExtent<?>)newValue);
				return;
			case ChartPackage.PLOT__RGB:
				setRgb((RGB)newValue);
				return;
			case ChartPackage.PLOT__FIT:
				setFit((FitPolicy)newValue);
				return;
			case ChartPackage.PLOT__PROFILE:
				setProfile((Profile<?>)newValue);
				return;
			case ChartPackage.PLOT__SHOW_TEXT:
				setShowText((Boolean)newValue);
				return;
			case ChartPackage.PLOT__AUTO_ASSIGN_RGB:
				setAutoAssignRGB((Boolean)newValue);
				return;
			case ChartPackage.PLOT__NAME:
				setName((String)newValue);
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
			case ChartPackage.PLOT__CHART:
				setChart((Chart)null);
				return;
			case ChartPackage.PLOT__EXTENT:
				setExtent((AmountExtent<?>)null);
				return;
			case ChartPackage.PLOT__RGB:
				setRgb(RGB_EDEFAULT);
				return;
			case ChartPackage.PLOT__FIT:
				setFit(FIT_EDEFAULT);
				return;
			case ChartPackage.PLOT__PROFILE:
				setProfile((Profile<?>)null);
				return;
			case ChartPackage.PLOT__SHOW_TEXT:
				setShowText(SHOW_TEXT_EDEFAULT);
				return;
			case ChartPackage.PLOT__AUTO_ASSIGN_RGB:
				setAutoAssignRGB(AUTO_ASSIGN_RGB_EDEFAULT);
				return;
			case ChartPackage.PLOT__NAME:
				setName(NAME_EDEFAULT);
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
			case ChartPackage.PLOT__CHART:
				return getChart() != null;
			case ChartPackage.PLOT__EXTENT:
				return extent != null;
			case ChartPackage.PLOT__RGB:
				return RGB_EDEFAULT == null ? rgb != null : !RGB_EDEFAULT.equals(rgb);
			case ChartPackage.PLOT__FIT:
				return fit != FIT_EDEFAULT;
			case ChartPackage.PLOT__PROFILE:
				return profile != null;
			case ChartPackage.PLOT__SHOW_TEXT:
				return ((eFlags & SHOW_TEXT_EFLAG) != 0) != SHOW_TEXT_EDEFAULT;
			case ChartPackage.PLOT__AUTO_ASSIGN_RGB:
				return ((eFlags & AUTO_ASSIGN_RGB_EFLAG) != 0) != AUTO_ASSIGN_RGB_EDEFAULT;
			case ChartPackage.PLOT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
		result.append(" (extent: ");
		result.append(extent);
		result.append(", rgb: ");
		result.append(rgb);
		result.append(", fit: ");
		result.append(fit);
		result.append(", showText: ");
		result.append((eFlags & SHOW_TEXT_EFLAG) != 0);
		result.append(", autoAssignRGB: ");
		result.append((eFlags & AUTO_ASSIGN_RGB_EFLAG) != 0);
		result.append(", name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //ResourcePlotImpl
