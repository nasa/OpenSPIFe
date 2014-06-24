/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary.impl;

import gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enum Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumValueImpl#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumValueImpl#getColor <em>Color</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumValueImpl#getLiteral <em>Literal</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EnumValueImpl extends MinimalEObjectImpl.Container implements EnumValue
{
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
   * The default value of the '{@link #getColor() <em>Color</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColor()
   * @generated
   * @ordered
   */
  protected static final String COLOR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getColor() <em>Color</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColor()
   * @generated
   * @ordered
   */
  protected String color = COLOR_EDEFAULT;

  /**
   * The default value of the '{@link #getLiteral() <em>Literal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLiteral()
   * @generated
   * @ordered
   */
  protected static final String LITERAL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLiteral() <em>Literal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLiteral()
   * @generated
   * @ordered
   */
  protected String literal = LITERAL_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EnumValueImpl()
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
    return XDictionaryPackage.Literals.ENUM_VALUE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ENUM_VALUE__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getColor()
  {
    return color;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setColor(String newColor)
  {
    String oldColor = color;
    color = newColor;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ENUM_VALUE__COLOR, oldColor, color));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getLiteral()
  {
    return literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setLiteral(String newLiteral)
  {
    String oldLiteral = literal;
    literal = newLiteral;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ENUM_VALUE__LITERAL, oldLiteral, literal));
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
      case XDictionaryPackage.ENUM_VALUE__NAME:
        return getName();
      case XDictionaryPackage.ENUM_VALUE__COLOR:
        return getColor();
      case XDictionaryPackage.ENUM_VALUE__LITERAL:
        return getLiteral();
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
      case XDictionaryPackage.ENUM_VALUE__NAME:
        setName((String)newValue);
        return;
      case XDictionaryPackage.ENUM_VALUE__COLOR:
        setColor((String)newValue);
        return;
      case XDictionaryPackage.ENUM_VALUE__LITERAL:
        setLiteral((String)newValue);
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
      case XDictionaryPackage.ENUM_VALUE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case XDictionaryPackage.ENUM_VALUE__COLOR:
        setColor(COLOR_EDEFAULT);
        return;
      case XDictionaryPackage.ENUM_VALUE__LITERAL:
        setLiteral(LITERAL_EDEFAULT);
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
      case XDictionaryPackage.ENUM_VALUE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case XDictionaryPackage.ENUM_VALUE__COLOR:
        return COLOR_EDEFAULT == null ? color != null : !COLOR_EDEFAULT.equals(color);
      case XDictionaryPackage.ENUM_VALUE__LITERAL:
        return LITERAL_EDEFAULT == null ? literal != null : !LITERAL_EDEFAULT.equals(literal);
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
    result.append(" (name: ");
    result.append(name);
    result.append(", color: ");
    result.append(color);
    result.append(", literal: ");
    result.append(literal);
    result.append(')');
    return result.toString();
  }

} //EnumValueImpl
