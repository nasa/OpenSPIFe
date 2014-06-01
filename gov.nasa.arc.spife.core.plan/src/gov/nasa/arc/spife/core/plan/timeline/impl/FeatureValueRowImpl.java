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
package gov.nasa.arc.spife.core.plan.timeline.impl;

import gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.util.DerivedAttributeAdapter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Feature Value Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.arc.spife.core.plan.timeline.impl.FeatureValueRowImpl#getFeatureName <em>Feature Name</em>}</li>
 *   <li>{@link gov.nasa.arc.spife.core.plan.timeline.impl.FeatureValueRowImpl#getValueLiteral <em>Value Literal</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FeatureValueRowImpl extends PlanSectionRowImpl implements FeatureValueRow {
	/**
	 * The default value of the '{@link #getFeatureName() <em>Feature Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureName()
	 * @generated
	 * @ordered
	 */
	protected static final String FEATURE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFeatureName() <em>Feature Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFeatureName()
	 * @generated
	 * @ordered
	 */
	protected String featureName = FEATURE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getValueLiteral() <em>Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueLiteral()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_LITERAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValueLiteral() <em>Value Literal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueLiteral()
	 * @generated
	 * @ordered
	 */
	protected String valueLiteral = VALUE_LITERAL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected FeatureValueRowImpl() {
		super();
		eAdapters().add(
				new DerivedAttributeAdapter(this, 
						PlanTimelinePackage.Literals.PLAN_SECTION_ROW__NAME, 
						PlanTimelinePackage.Literals.FEATURE_VALUE_ROW__FEATURE_NAME));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PlanTimelinePackage.Literals.FEATURE_VALUE_ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getFeatureName() {
		return featureName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFeatureName(String newFeatureName) {
		String oldFeatureName = featureName;
		featureName = newFeatureName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanTimelinePackage.FEATURE_VALUE_ROW__FEATURE_NAME, oldFeatureName, featureName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getValueLiteral() {
		return valueLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setValueLiteral(String newValueLiteral) {
		String oldValueLiteral = valueLiteral;
		valueLiteral = newValueLiteral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PlanTimelinePackage.FEATURE_VALUE_ROW__VALUE_LITERAL, oldValueLiteral, valueLiteral));
	}

	@Override
	public boolean isRelevant(EStructuralFeature feature) {
		return CommonUtils.equals(feature.getName(), getFeatureName());
	}

	@Override
	public boolean satisfies(EObject object) {
		String featureName = getFeatureName();
		if (featureName != null) {
			EObject data = ((EPlanElement)object).getData();
			if (data != null) {
				String expectedValueLiteral = normalize(getValueLiteral());
				EStructuralFeature feature = data.eClass().getEStructuralFeature(featureName);
				if (feature instanceof EAttribute) {
					EAttribute attribute = (EAttribute) feature;
					for (Object value : EMFUtils.eGetAsList(data, attribute)) {
						EDataType eDataType = attribute.getEAttributeType();
						EFactory factory = eDataType.getEPackage().getEFactoryInstance();
						String actualValueLiteral = normalize(factory.convertToString(eDataType, value));
						if (CommonUtils.equals(expectedValueLiteral, actualValueLiteral)) {
							return true;
						}
					}
				} else if (feature instanceof EReference) {
					for (EObject o : EMFUtils.eGetAsList(data, (EReference) feature)) {
						if (CommonUtils.equals(expectedValueLiteral, normalize(EcoreUtil.getID(o)))
								|| CommonUtils.equals(expectedValueLiteral, normalize(EMFUtils.getDisplayName(o)))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PlanTimelinePackage.FEATURE_VALUE_ROW__FEATURE_NAME:
				return getFeatureName();
			case PlanTimelinePackage.FEATURE_VALUE_ROW__VALUE_LITERAL:
				return getValueLiteral();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PlanTimelinePackage.FEATURE_VALUE_ROW__FEATURE_NAME:
				setFeatureName((String)newValue);
				return;
			case PlanTimelinePackage.FEATURE_VALUE_ROW__VALUE_LITERAL:
				setValueLiteral((String)newValue);
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
			case PlanTimelinePackage.FEATURE_VALUE_ROW__FEATURE_NAME:
				setFeatureName(FEATURE_NAME_EDEFAULT);
				return;
			case PlanTimelinePackage.FEATURE_VALUE_ROW__VALUE_LITERAL:
				setValueLiteral(VALUE_LITERAL_EDEFAULT);
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
			case PlanTimelinePackage.FEATURE_VALUE_ROW__FEATURE_NAME:
				return FEATURE_NAME_EDEFAULT == null ? featureName != null : !FEATURE_NAME_EDEFAULT.equals(featureName);
			case PlanTimelinePackage.FEATURE_VALUE_ROW__VALUE_LITERAL:
				return VALUE_LITERAL_EDEFAULT == null ? valueLiteral != null : !VALUE_LITERAL_EDEFAULT.equals(valueLiteral);
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
		result.append(" (featureName: ");
		result.append(featureName);
		result.append(", valueLiteral: ");
		result.append(valueLiteral);
		result.append(')');
		return result.toString();
	}

	private String normalize(String string) {
		if (string == null) {
			return string;
		}
		return string.toLowerCase();
	}
	
} //FeatureValueRowImpl
