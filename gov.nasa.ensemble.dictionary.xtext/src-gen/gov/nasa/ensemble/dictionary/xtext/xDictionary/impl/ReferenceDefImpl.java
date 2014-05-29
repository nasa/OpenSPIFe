/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary.impl;

import gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Requirement;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ReferenceDefImpl#getContainment <em>Containment</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ReferenceDefImpl#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ReferenceDefImpl#getEffects <em>Effects</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ReferenceDefImpl extends ParameterDefImpl implements ReferenceDef
{
  /**
   * The default value of the '{@link #getContainment() <em>Containment</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContainment()
   * @generated
   * @ordered
   */
  protected static final String CONTAINMENT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getContainment() <em>Containment</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContainment()
   * @generated
   * @ordered
   */
  protected String containment = CONTAINMENT_EDEFAULT;

  /**
   * The cached value of the '{@link #getRequirements() <em>Requirements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRequirements()
   * @generated
   * @ordered
   */
  protected EList<Requirement> requirements;

  /**
   * The cached value of the '{@link #getEffects() <em>Effects</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEffects()
   * @generated
   * @ordered
   */
  protected EList<String> effects;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ReferenceDefImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return XDictionaryPackage.Literals.REFERENCE_DEF;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getContainment()
  {
    return containment;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setContainment(String newContainment)
  {
    String oldContainment = containment;
    containment = newContainment;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.REFERENCE_DEF__CONTAINMENT, oldContainment, containment));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Requirement> getRequirements()
  {
    if (requirements == null)
    {
      requirements = new EObjectContainmentEList<Requirement>(Requirement.class, this, XDictionaryPackage.REFERENCE_DEF__REQUIREMENTS);
    }
    return requirements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getEffects()
  {
    if (effects == null)
    {
      effects = new EDataTypeEList<String>(String.class, this, XDictionaryPackage.REFERENCE_DEF__EFFECTS);
    }
    return effects;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case XDictionaryPackage.REFERENCE_DEF__REQUIREMENTS:
        return ((InternalEList<?>)getRequirements()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case XDictionaryPackage.REFERENCE_DEF__CONTAINMENT:
        return getContainment();
      case XDictionaryPackage.REFERENCE_DEF__REQUIREMENTS:
        return getRequirements();
      case XDictionaryPackage.REFERENCE_DEF__EFFECTS:
        return getEffects();
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
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case XDictionaryPackage.REFERENCE_DEF__CONTAINMENT:
        setContainment((String)newValue);
        return;
      case XDictionaryPackage.REFERENCE_DEF__REQUIREMENTS:
        getRequirements().clear();
        getRequirements().addAll((Collection<? extends Requirement>)newValue);
        return;
      case XDictionaryPackage.REFERENCE_DEF__EFFECTS:
        getEffects().clear();
        getEffects().addAll((Collection<? extends String>)newValue);
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
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case XDictionaryPackage.REFERENCE_DEF__CONTAINMENT:
        setContainment(CONTAINMENT_EDEFAULT);
        return;
      case XDictionaryPackage.REFERENCE_DEF__REQUIREMENTS:
        getRequirements().clear();
        return;
      case XDictionaryPackage.REFERENCE_DEF__EFFECTS:
        getEffects().clear();
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
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case XDictionaryPackage.REFERENCE_DEF__CONTAINMENT:
        return CONTAINMENT_EDEFAULT == null ? containment != null : !CONTAINMENT_EDEFAULT.equals(containment);
      case XDictionaryPackage.REFERENCE_DEF__REQUIREMENTS:
        return requirements != null && !requirements.isEmpty();
      case XDictionaryPackage.REFERENCE_DEF__EFFECTS:
        return effects != null && !effects.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (containment: ");
    result.append(containment);
    result.append(", effects: ");
    result.append(effects);
    result.append(')');
    return result.toString();
  }

} //ReferenceDefImpl
