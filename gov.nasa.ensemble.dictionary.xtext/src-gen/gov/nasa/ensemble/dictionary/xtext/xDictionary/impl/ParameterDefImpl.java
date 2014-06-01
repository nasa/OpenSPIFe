/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary.impl;

import gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ParameterDefImpl#getType <em>Type</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ParameterDefImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ParameterDefImpl#getDisplayName <em>Display Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ParameterDefImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ParameterDefImpl#getAnnotations <em>Annotations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterDefImpl extends DefinitionImpl implements ParameterDef
{
  /**
   * The default value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected static final String TYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected String type = TYPE_EDEFAULT;

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
   * The default value of the '{@link #getDisplayName() <em>Display Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDisplayName()
   * @generated
   * @ordered
   */
  protected static final String DISPLAY_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDisplayName() <em>Display Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDisplayName()
   * @generated
   * @ordered
   */
  protected String displayName = DISPLAY_NAME_EDEFAULT;

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
   * The cached value of the '{@link #getAnnotations() <em>Annotations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAnnotations()
   * @generated
   * @ordered
   */
  protected EList<Annotation> annotations;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ParameterDefImpl()
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
    return XDictionaryPackage.Literals.PARAMETER_DEF;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setType(String newType)
  {
    String oldType = type;
    type = newType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.PARAMETER_DEF__TYPE, oldType, type));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getDescription()
  {
    return description;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setDescription(String newDescription)
  {
    String oldDescription = description;
    description = newDescription;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.PARAMETER_DEF__DESCRIPTION, oldDescription, description));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getDisplayName()
  {
    return displayName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setDisplayName(String newDisplayName)
  {
    String oldDisplayName = displayName;
    displayName = newDisplayName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.PARAMETER_DEF__DISPLAY_NAME, oldDisplayName, displayName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getCategory()
  {
    return category;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setCategory(String newCategory)
  {
    String oldCategory = category;
    category = newCategory;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.PARAMETER_DEF__CATEGORY, oldCategory, category));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EList<Annotation> getAnnotations()
  {
    if (annotations == null)
    {
      annotations = new EObjectContainmentEList<Annotation>(Annotation.class, this, XDictionaryPackage.PARAMETER_DEF__ANNOTATIONS);
    }
    return annotations;
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
      case XDictionaryPackage.PARAMETER_DEF__ANNOTATIONS:
        return ((InternalEList<?>)getAnnotations()).basicRemove(otherEnd, msgs);
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
      case XDictionaryPackage.PARAMETER_DEF__TYPE:
        return getType();
      case XDictionaryPackage.PARAMETER_DEF__DESCRIPTION:
        return getDescription();
      case XDictionaryPackage.PARAMETER_DEF__DISPLAY_NAME:
        return getDisplayName();
      case XDictionaryPackage.PARAMETER_DEF__CATEGORY:
        return getCategory();
      case XDictionaryPackage.PARAMETER_DEF__ANNOTATIONS:
        return getAnnotations();
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
      case XDictionaryPackage.PARAMETER_DEF__TYPE:
        setType((String)newValue);
        return;
      case XDictionaryPackage.PARAMETER_DEF__DESCRIPTION:
        setDescription((String)newValue);
        return;
      case XDictionaryPackage.PARAMETER_DEF__DISPLAY_NAME:
        setDisplayName((String)newValue);
        return;
      case XDictionaryPackage.PARAMETER_DEF__CATEGORY:
        setCategory((String)newValue);
        return;
      case XDictionaryPackage.PARAMETER_DEF__ANNOTATIONS:
        getAnnotations().clear();
        getAnnotations().addAll((Collection<? extends Annotation>)newValue);
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
      case XDictionaryPackage.PARAMETER_DEF__TYPE:
        setType(TYPE_EDEFAULT);
        return;
      case XDictionaryPackage.PARAMETER_DEF__DESCRIPTION:
        setDescription(DESCRIPTION_EDEFAULT);
        return;
      case XDictionaryPackage.PARAMETER_DEF__DISPLAY_NAME:
        setDisplayName(DISPLAY_NAME_EDEFAULT);
        return;
      case XDictionaryPackage.PARAMETER_DEF__CATEGORY:
        setCategory(CATEGORY_EDEFAULT);
        return;
      case XDictionaryPackage.PARAMETER_DEF__ANNOTATIONS:
        getAnnotations().clear();
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
      case XDictionaryPackage.PARAMETER_DEF__TYPE:
        return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
      case XDictionaryPackage.PARAMETER_DEF__DESCRIPTION:
        return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
      case XDictionaryPackage.PARAMETER_DEF__DISPLAY_NAME:
        return DISPLAY_NAME_EDEFAULT == null ? displayName != null : !DISPLAY_NAME_EDEFAULT.equals(displayName);
      case XDictionaryPackage.PARAMETER_DEF__CATEGORY:
        return CATEGORY_EDEFAULT == null ? category != null : !CATEGORY_EDEFAULT.equals(category);
      case XDictionaryPackage.PARAMETER_DEF__ANNOTATIONS:
        return annotations != null && !annotations.isEmpty();
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
    result.append(" (type: ");
    result.append(type);
    result.append(", description: ");
    result.append(description);
    result.append(", displayName: ");
    result.append(displayName);
    result.append(", category: ");
    result.append(category);
    result.append(')');
    return result.toString();
  }

} //ParameterDefImpl
