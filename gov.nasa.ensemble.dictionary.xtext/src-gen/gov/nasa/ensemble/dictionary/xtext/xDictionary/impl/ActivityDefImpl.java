/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary.impl;

import gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef;
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
 * An implementation of the model object '<em><b>Activity Def</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl#getDisplayName <em>Display Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl#getHiddenParams <em>Hidden Params</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl#getAnnotations <em>Annotations</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl#getEffects <em>Effects</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ActivityDefImpl extends DefinitionImpl implements ActivityDef
{
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
   * The default value of the '{@link #getHiddenParams() <em>Hidden Params</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHiddenParams()
   * @generated
   * @ordered
   */
  protected static final String HIDDEN_PARAMS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getHiddenParams() <em>Hidden Params</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHiddenParams()
   * @generated
   * @ordered
   */
  protected String hiddenParams = HIDDEN_PARAMS_EDEFAULT;

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
   * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParameters()
   * @generated
   * @ordered
   */
  protected EList<ParameterDef> parameters;

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
  protected ActivityDefImpl()
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
    return XDictionaryPackage.Literals.ACTIVITY_DEF;
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
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ACTIVITY_DEF__DESCRIPTION, oldDescription, description));
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
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ACTIVITY_DEF__CATEGORY, oldCategory, category));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getDuration()
  {
    return duration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setDuration(String newDuration)
  {
    String oldDuration = duration;
    duration = newDuration;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ACTIVITY_DEF__DURATION, oldDuration, duration));
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
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ACTIVITY_DEF__DISPLAY_NAME, oldDisplayName, displayName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public String getHiddenParams()
  {
    return hiddenParams;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public void setHiddenParams(String newHiddenParams)
  {
    String oldHiddenParams = hiddenParams;
    hiddenParams = newHiddenParams;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XDictionaryPackage.ACTIVITY_DEF__HIDDEN_PARAMS, oldHiddenParams, hiddenParams));
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
      annotations = new EObjectContainmentEList<Annotation>(Annotation.class, this, XDictionaryPackage.ACTIVITY_DEF__ANNOTATIONS);
    }
    return annotations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EList<ParameterDef> getParameters()
  {
    if (parameters == null)
    {
      parameters = new EObjectContainmentEList<ParameterDef>(ParameterDef.class, this, XDictionaryPackage.ACTIVITY_DEF__PARAMETERS);
    }
    return parameters;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EList<Requirement> getRequirements()
  {
    if (requirements == null)
    {
      requirements = new EObjectContainmentEList<Requirement>(Requirement.class, this, XDictionaryPackage.ACTIVITY_DEF__REQUIREMENTS);
    }
    return requirements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EList<String> getEffects()
  {
    if (effects == null)
    {
      effects = new EDataTypeEList<String>(String.class, this, XDictionaryPackage.ACTIVITY_DEF__EFFECTS);
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
      case XDictionaryPackage.ACTIVITY_DEF__ANNOTATIONS:
        return ((InternalEList<?>)getAnnotations()).basicRemove(otherEnd, msgs);
      case XDictionaryPackage.ACTIVITY_DEF__PARAMETERS:
        return ((InternalEList<?>)getParameters()).basicRemove(otherEnd, msgs);
      case XDictionaryPackage.ACTIVITY_DEF__REQUIREMENTS:
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
      case XDictionaryPackage.ACTIVITY_DEF__DESCRIPTION:
        return getDescription();
      case XDictionaryPackage.ACTIVITY_DEF__CATEGORY:
        return getCategory();
      case XDictionaryPackage.ACTIVITY_DEF__DURATION:
        return getDuration();
      case XDictionaryPackage.ACTIVITY_DEF__DISPLAY_NAME:
        return getDisplayName();
      case XDictionaryPackage.ACTIVITY_DEF__HIDDEN_PARAMS:
        return getHiddenParams();
      case XDictionaryPackage.ACTIVITY_DEF__ANNOTATIONS:
        return getAnnotations();
      case XDictionaryPackage.ACTIVITY_DEF__PARAMETERS:
        return getParameters();
      case XDictionaryPackage.ACTIVITY_DEF__REQUIREMENTS:
        return getRequirements();
      case XDictionaryPackage.ACTIVITY_DEF__EFFECTS:
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
      case XDictionaryPackage.ACTIVITY_DEF__DESCRIPTION:
        setDescription((String)newValue);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__CATEGORY:
        setCategory((String)newValue);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__DURATION:
        setDuration((String)newValue);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__DISPLAY_NAME:
        setDisplayName((String)newValue);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__HIDDEN_PARAMS:
        setHiddenParams((String)newValue);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__ANNOTATIONS:
        getAnnotations().clear();
        getAnnotations().addAll((Collection<? extends Annotation>)newValue);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__PARAMETERS:
        getParameters().clear();
        getParameters().addAll((Collection<? extends ParameterDef>)newValue);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__REQUIREMENTS:
        getRequirements().clear();
        getRequirements().addAll((Collection<? extends Requirement>)newValue);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__EFFECTS:
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
      case XDictionaryPackage.ACTIVITY_DEF__DESCRIPTION:
        setDescription(DESCRIPTION_EDEFAULT);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__CATEGORY:
        setCategory(CATEGORY_EDEFAULT);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__DURATION:
        setDuration(DURATION_EDEFAULT);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__DISPLAY_NAME:
        setDisplayName(DISPLAY_NAME_EDEFAULT);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__HIDDEN_PARAMS:
        setHiddenParams(HIDDEN_PARAMS_EDEFAULT);
        return;
      case XDictionaryPackage.ACTIVITY_DEF__ANNOTATIONS:
        getAnnotations().clear();
        return;
      case XDictionaryPackage.ACTIVITY_DEF__PARAMETERS:
        getParameters().clear();
        return;
      case XDictionaryPackage.ACTIVITY_DEF__REQUIREMENTS:
        getRequirements().clear();
        return;
      case XDictionaryPackage.ACTIVITY_DEF__EFFECTS:
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
      case XDictionaryPackage.ACTIVITY_DEF__DESCRIPTION:
        return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
      case XDictionaryPackage.ACTIVITY_DEF__CATEGORY:
        return CATEGORY_EDEFAULT == null ? category != null : !CATEGORY_EDEFAULT.equals(category);
      case XDictionaryPackage.ACTIVITY_DEF__DURATION:
        return DURATION_EDEFAULT == null ? duration != null : !DURATION_EDEFAULT.equals(duration);
      case XDictionaryPackage.ACTIVITY_DEF__DISPLAY_NAME:
        return DISPLAY_NAME_EDEFAULT == null ? displayName != null : !DISPLAY_NAME_EDEFAULT.equals(displayName);
      case XDictionaryPackage.ACTIVITY_DEF__HIDDEN_PARAMS:
        return HIDDEN_PARAMS_EDEFAULT == null ? hiddenParams != null : !HIDDEN_PARAMS_EDEFAULT.equals(hiddenParams);
      case XDictionaryPackage.ACTIVITY_DEF__ANNOTATIONS:
        return annotations != null && !annotations.isEmpty();
      case XDictionaryPackage.ACTIVITY_DEF__PARAMETERS:
        return parameters != null && !parameters.isEmpty();
      case XDictionaryPackage.ACTIVITY_DEF__REQUIREMENTS:
        return requirements != null && !requirements.isEmpty();
      case XDictionaryPackage.ACTIVITY_DEF__EFFECTS:
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
    result.append(" (description: ");
    result.append(description);
    result.append(", category: ");
    result.append(category);
    result.append(", duration: ");
    result.append(duration);
    result.append(", displayName: ");
    result.append(displayName);
    result.append(", hiddenParams: ");
    result.append(hiddenParams);
    result.append(", effects: ");
    result.append(effects);
    result.append(')');
    return result.toString();
  }

} //ActivityDefImpl
