/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary.impl;

import gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Attribute Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AttributeDefImpl#getDefaultValueLiteral <em>Default Value Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AttributeDefImpl#getShortDescription <em>Short Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AttributeDefImpl#getUnits <em>Units</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AttributeDefImpl#getParameterName <em>Parameter Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AttributeDefImpl extends ParameterDefImpl implements AttributeDef
{
  /**
   * The default value of the '{@link #getDefaultValueLiteral() <em>Default Value Literal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultValueLiteral()
   * @generated
   * @ordered
   */
  protected static final String DEFAULT_VALUE_LITERAL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDefaultValueLiteral() <em>Default Value Literal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultValueLiteral()
   * @generated
   * @ordered
   */
  protected String defaultValueLiteral = DEFAULT_VALUE_LITERAL_EDEFAULT;

  /**
   * The default value of the '{@link #getShortDescription() <em>Short Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getShortDescription()
   * @generated
   * @ordered
   */
  protected static final String SHORT_DESCRIPTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getShortDescription() <em>Short Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getShortDescription()
   * @generated
   * @ordered
   */
  protected String shortDescription = SHORT_DESCRIPTION_EDEFAULT;

  /**
   * The default value of the '{@link #getUnits() <em>Units</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnits()
   * @generated
   * @ordered
   */
  protected static final String UNITS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUnits() <em>Units</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnits()
   * @generated
   * @ordered
   */
  protected String units = UNITS_EDEFAULT;

  /**
   * The default value of the '{@link #getParameterName() <em>Parameter Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParameterName()
   * @generated
   * @ordered
   */
  protected static final String PARAMETER_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getParameterName() <em>Parameter Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParameterName()
   * @generated
   * @ordered
   */
  protected String parameterName = PARAMETER_NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AttributeDefImpl()
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
    return XDictionaryPackage.Literals.ATTRIBUTE_DEF;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getDefaultValueLiteral()
  {
    return defaultValueLiteral;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setDefaultValueLiteral(String newDefaultValueLiteral)
  {
    String oldDefaultValueLiteral = defaultValueLiteral;
    defaultValueLiteral = newDefaultValueLiteral;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ATTRIBUTE_DEF__DEFAULT_VALUE_LITERAL, oldDefaultValueLiteral, defaultValueLiteral));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getShortDescription()
  {
    return shortDescription;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setShortDescription(String newShortDescription)
  {
    String oldShortDescription = shortDescription;
    shortDescription = newShortDescription;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ATTRIBUTE_DEF__SHORT_DESCRIPTION, oldShortDescription, shortDescription));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getUnits()
  {
    return units;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setUnits(String newUnits)
  {
    String oldUnits = units;
    units = newUnits;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ATTRIBUTE_DEF__UNITS, oldUnits, units));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getParameterName()
  {
    return parameterName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setParameterName(String newParameterName)
  {
    String oldParameterName = parameterName;
    parameterName = newParameterName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ATTRIBUTE_DEF__PARAMETER_NAME, oldParameterName, parameterName));
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
      case XDictionaryPackage.ATTRIBUTE_DEF__DEFAULT_VALUE_LITERAL:
        return getDefaultValueLiteral();
      case XDictionaryPackage.ATTRIBUTE_DEF__SHORT_DESCRIPTION:
        return getShortDescription();
      case XDictionaryPackage.ATTRIBUTE_DEF__UNITS:
        return getUnits();
      case XDictionaryPackage.ATTRIBUTE_DEF__PARAMETER_NAME:
        return getParameterName();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case XDictionaryPackage.ATTRIBUTE_DEF__DEFAULT_VALUE_LITERAL:
        setDefaultValueLiteral((String)newValue);
        return;
      case XDictionaryPackage.ATTRIBUTE_DEF__SHORT_DESCRIPTION:
        setShortDescription((String)newValue);
        return;
      case XDictionaryPackage.ATTRIBUTE_DEF__UNITS:
        setUnits((String)newValue);
        return;
      case XDictionaryPackage.ATTRIBUTE_DEF__PARAMETER_NAME:
        setParameterName((String)newValue);
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
      case XDictionaryPackage.ATTRIBUTE_DEF__DEFAULT_VALUE_LITERAL:
        setDefaultValueLiteral(DEFAULT_VALUE_LITERAL_EDEFAULT);
        return;
      case XDictionaryPackage.ATTRIBUTE_DEF__SHORT_DESCRIPTION:
        setShortDescription(SHORT_DESCRIPTION_EDEFAULT);
        return;
      case XDictionaryPackage.ATTRIBUTE_DEF__UNITS:
        setUnits(UNITS_EDEFAULT);
        return;
      case XDictionaryPackage.ATTRIBUTE_DEF__PARAMETER_NAME:
        setParameterName(PARAMETER_NAME_EDEFAULT);
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
      case XDictionaryPackage.ATTRIBUTE_DEF__DEFAULT_VALUE_LITERAL:
        return DEFAULT_VALUE_LITERAL_EDEFAULT == null ? defaultValueLiteral != null : !DEFAULT_VALUE_LITERAL_EDEFAULT.equals(defaultValueLiteral);
      case XDictionaryPackage.ATTRIBUTE_DEF__SHORT_DESCRIPTION:
        return SHORT_DESCRIPTION_EDEFAULT == null ? shortDescription != null : !SHORT_DESCRIPTION_EDEFAULT.equals(shortDescription);
      case XDictionaryPackage.ATTRIBUTE_DEF__UNITS:
        return UNITS_EDEFAULT == null ? units != null : !UNITS_EDEFAULT.equals(units);
      case XDictionaryPackage.ATTRIBUTE_DEF__PARAMETER_NAME:
        return PARAMETER_NAME_EDEFAULT == null ? parameterName != null : !PARAMETER_NAME_EDEFAULT.equals(parameterName);
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
    result.append(" (defaultValueLiteral: ");
    result.append(defaultValueLiteral);
    result.append(", shortDescription: ");
    result.append(shortDescription);
    result.append(", units: ");
    result.append(units);
    result.append(", parameterName: ");
    result.append(parameterName);
    result.append(')');
    return result.toString();
  }

} //AttributeDefImpl
