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
package gov.nasa.ensemble.dictionary.impl;

import gov.nasa.ensemble.dictionary.DefinitionContext;
import gov.nasa.ensemble.dictionary.DefinitionContextImpl;
import gov.nasa.ensemble.dictionary.DictionaryPackage;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.EClaimableEffect;
import gov.nasa.ensemble.dictionary.ENumericRequirement;
import gov.nasa.ensemble.dictionary.ENumericResourceEffect;
import gov.nasa.ensemble.dictionary.ESharableResourceEffect;
import gov.nasa.ensemble.dictionary.EStateRequirement;
import gov.nasa.ensemble.dictionary.EStateResourceEffect;
import gov.nasa.ensemble.dictionary.ESubActivity;
import gov.nasa.ensemble.dictionary.INamedDefinition;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EActivity Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getClaimableEffects <em>Claimable Effects</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getNumericEffects <em>Numeric Effects</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getNumericRequirements <em>Numeric Requirements</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getSharedEffects <em>Shared Effects</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getStateEffects <em>State Effects</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.impl.EActivityDefImpl#getStateRequirements <em>State Requirements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EActivityDefImpl extends EClassImpl implements EActivityDef {
	/**
	 * The default value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected static final String CATEGORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected String category = CATEGORY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<ESubActivity> children;

	/**
	 * The cached value of the '{@link #getClaimableEffects() <em>Claimable Effects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClaimableEffects()
	 * @generated
	 * @ordered
	 */
	protected EList<EClaimableEffect> claimableEffects;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final String DURATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected String duration = DURATION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getNumericEffects() <em>Numeric Effects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumericEffects()
	 * @generated
	 * @ordered
	 */
	protected EList<ENumericResourceEffect> numericEffects;

	/**
	 * The cached value of the '{@link #getNumericRequirements() <em>Numeric Requirements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumericRequirements()
	 * @generated
	 * @ordered
	 */
	protected EList<ENumericRequirement> numericRequirements;

	/**
	 * The cached value of the '{@link #getSharedEffects() <em>Shared Effects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSharedEffects()
	 * @generated
	 * @ordered
	 */
	protected EList<ESharableResourceEffect> sharedEffects;

	/**
	 * The cached value of the '{@link #getStateEffects() <em>State Effects</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStateEffects()
	 * @generated
	 * @ordered
	 */
	protected EList<EStateResourceEffect<?>> stateEffects;

	/**
	 * The cached value of the '{@link #getStateRequirements() <em>State Requirements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStateRequirements()
	 * @generated
	 * @ordered
	 */
	protected EList<EStateRequirement> stateRequirements;

	protected final DefinitionContext definitionContextDelegate;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected EActivityDefImpl() {
		super();
		definitionContextDelegate = new DefinitionContextImpl(this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DictionaryPackage.Literals.EACTIVITY_DEF;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCategory() {
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCategory(String newCategory) {
		String oldCategory = category;
		category = newCategory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_DEF__CATEGORY, oldCategory, category));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_DEF__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDuration(String newDuration) {
		String oldDuration = duration;
		duration = newDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DictionaryPackage.EACTIVITY_DEF__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<ENumericResourceEffect> getNumericEffects() {
		if (numericEffects == null) {
			numericEffects = new EObjectContainmentEList<ENumericResourceEffect>(ENumericResourceEffect.class, this, DictionaryPackage.EACTIVITY_DEF__NUMERIC_EFFECTS);
		}
		return numericEffects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<ENumericRequirement> getNumericRequirements() {
		if (numericRequirements == null) {
			numericRequirements = new EObjectContainmentEList<ENumericRequirement>(ENumericRequirement.class, this, DictionaryPackage.EACTIVITY_DEF__NUMERIC_REQUIREMENTS);
		}
		return numericRequirements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<ESharableResourceEffect> getSharedEffects() {
		if (sharedEffects == null) {
			sharedEffects = new EObjectContainmentEList<ESharableResourceEffect>(ESharableResourceEffect.class, this, DictionaryPackage.EACTIVITY_DEF__SHARED_EFFECTS);
		}
		return sharedEffects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<EStateResourceEffect<?>> getStateEffects() {
		if (stateEffects == null) {
			stateEffects = new EObjectContainmentEList<EStateResourceEffect<?>>(EStateResourceEffect.class, this, DictionaryPackage.EACTIVITY_DEF__STATE_EFFECTS);
		}
		return stateEffects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<EStateRequirement> getStateRequirements() {
		if (stateRequirements == null) {
			stateRequirements = new EObjectContainmentEList<EStateRequirement>(EStateRequirement.class, this, DictionaryPackage.EACTIVITY_DEF__STATE_REQUIREMENTS);
		}
		return stateRequirements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<ESubActivity> getChildren() {
		if (children == null) {
			children = new EObjectContainmentWithInverseEList<ESubActivity>(ESubActivity.class, this, DictionaryPackage.EACTIVITY_DEF__CHILDREN, DictionaryPackage.ESUB_ACTIVITY__DEFINITION);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<EClaimableEffect> getClaimableEffects() {
		if (claimableEffects == null) {
			claimableEffects = new EObjectContainmentEList<EClaimableEffect>(EClaimableEffect.class, this, DictionaryPackage.EACTIVITY_DEF__CLAIMABLE_EFFECTS);
		}
		return claimableEffects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DictionaryPackage.EACTIVITY_DEF__CHILDREN:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildren()).basicAdd(otherEnd, msgs);
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
			case DictionaryPackage.EACTIVITY_DEF__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
			case DictionaryPackage.EACTIVITY_DEF__CLAIMABLE_EFFECTS:
				return ((InternalEList<?>)getClaimableEffects()).basicRemove(otherEnd, msgs);
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_EFFECTS:
				return ((InternalEList<?>)getNumericEffects()).basicRemove(otherEnd, msgs);
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_REQUIREMENTS:
				return ((InternalEList<?>)getNumericRequirements()).basicRemove(otherEnd, msgs);
			case DictionaryPackage.EACTIVITY_DEF__SHARED_EFFECTS:
				return ((InternalEList<?>)getSharedEffects()).basicRemove(otherEnd, msgs);
			case DictionaryPackage.EACTIVITY_DEF__STATE_EFFECTS:
				return ((InternalEList<?>)getStateEffects()).basicRemove(otherEnd, msgs);
			case DictionaryPackage.EACTIVITY_DEF__STATE_REQUIREMENTS:
				return ((InternalEList<?>)getStateRequirements()).basicRemove(otherEnd, msgs);
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
			case DictionaryPackage.EACTIVITY_DEF__CATEGORY:
				return getCategory();
			case DictionaryPackage.EACTIVITY_DEF__CHILDREN:
				return getChildren();
			case DictionaryPackage.EACTIVITY_DEF__CLAIMABLE_EFFECTS:
				return getClaimableEffects();
			case DictionaryPackage.EACTIVITY_DEF__DESCRIPTION:
				return getDescription();
			case DictionaryPackage.EACTIVITY_DEF__DURATION:
				return getDuration();
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_EFFECTS:
				return getNumericEffects();
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_REQUIREMENTS:
				return getNumericRequirements();
			case DictionaryPackage.EACTIVITY_DEF__SHARED_EFFECTS:
				return getSharedEffects();
			case DictionaryPackage.EACTIVITY_DEF__STATE_EFFECTS:
				return getStateEffects();
			case DictionaryPackage.EACTIVITY_DEF__STATE_REQUIREMENTS:
				return getStateRequirements();
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
			case DictionaryPackage.EACTIVITY_DEF__CATEGORY:
				setCategory((String)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DEF__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends ESubActivity>)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DEF__CLAIMABLE_EFFECTS:
				getClaimableEffects().clear();
				getClaimableEffects().addAll((Collection<? extends EClaimableEffect>)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DEF__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DEF__DURATION:
				setDuration((String)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_EFFECTS:
				getNumericEffects().clear();
				getNumericEffects().addAll((Collection<? extends ENumericResourceEffect>)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_REQUIREMENTS:
				getNumericRequirements().clear();
				getNumericRequirements().addAll((Collection<? extends ENumericRequirement>)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DEF__SHARED_EFFECTS:
				getSharedEffects().clear();
				getSharedEffects().addAll((Collection<? extends ESharableResourceEffect>)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DEF__STATE_EFFECTS:
				getStateEffects().clear();
				getStateEffects().addAll((Collection<? extends EStateResourceEffect<?>>)newValue);
				return;
			case DictionaryPackage.EACTIVITY_DEF__STATE_REQUIREMENTS:
				getStateRequirements().clear();
				getStateRequirements().addAll((Collection<? extends EStateRequirement>)newValue);
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
			case DictionaryPackage.EACTIVITY_DEF__CATEGORY:
				setCategory(CATEGORY_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_DEF__CHILDREN:
				getChildren().clear();
				return;
			case DictionaryPackage.EACTIVITY_DEF__CLAIMABLE_EFFECTS:
				getClaimableEffects().clear();
				return;
			case DictionaryPackage.EACTIVITY_DEF__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_DEF__DURATION:
				setDuration(DURATION_EDEFAULT);
				return;
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_EFFECTS:
				getNumericEffects().clear();
				return;
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_REQUIREMENTS:
				getNumericRequirements().clear();
				return;
			case DictionaryPackage.EACTIVITY_DEF__SHARED_EFFECTS:
				getSharedEffects().clear();
				return;
			case DictionaryPackage.EACTIVITY_DEF__STATE_EFFECTS:
				getStateEffects().clear();
				return;
			case DictionaryPackage.EACTIVITY_DEF__STATE_REQUIREMENTS:
				getStateRequirements().clear();
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
			case DictionaryPackage.EACTIVITY_DEF__CATEGORY:
				return CATEGORY_EDEFAULT == null ? category != null : !CATEGORY_EDEFAULT.equals(category);
			case DictionaryPackage.EACTIVITY_DEF__CHILDREN:
				return children != null && !children.isEmpty();
			case DictionaryPackage.EACTIVITY_DEF__CLAIMABLE_EFFECTS:
				return claimableEffects != null && !claimableEffects.isEmpty();
			case DictionaryPackage.EACTIVITY_DEF__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DictionaryPackage.EACTIVITY_DEF__DURATION:
				return DURATION_EDEFAULT == null ? duration != null : !DURATION_EDEFAULT.equals(duration);
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_EFFECTS:
				return numericEffects != null && !numericEffects.isEmpty();
			case DictionaryPackage.EACTIVITY_DEF__NUMERIC_REQUIREMENTS:
				return numericRequirements != null && !numericRequirements.isEmpty();
			case DictionaryPackage.EACTIVITY_DEF__SHARED_EFFECTS:
				return sharedEffects != null && !sharedEffects.isEmpty();
			case DictionaryPackage.EACTIVITY_DEF__STATE_EFFECTS:
				return stateEffects != null && !stateEffects.isEmpty();
			case DictionaryPackage.EACTIVITY_DEF__STATE_REQUIREMENTS:
				return stateRequirements != null && !stateRequirements.isEmpty();
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
		result.append(" (category: ");
		result.append(category);
		result.append(", description: ");
		result.append(description);
		result.append(", duration: ");
		result.append(duration);
		result.append(')');
		return result.toString();
	}

	@Override
	public void clearCache() {
		definitionContextDelegate.clearCache();
	}

	@Override
	public <T extends INamedDefinition> T getDefinition(Class<T> klass, String name) {
		return definitionContextDelegate.getDefinition(klass, name);
	}

	@Override
	public <T> List<T> getDefinitions(Class<T> klass) {
		return definitionContextDelegate.getDefinitions(klass);
	}

} //EActivityDefImpl
