/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Activity Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getCategory <em>Category</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDuration <em>Duration</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDisplayName <em>Display Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getHiddenParams <em>Hidden Params</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getAnnotations <em>Annotations</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getParameters <em>Parameters</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getEffects <em>Effects</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef()
 * @model
 * @generated
 */
public interface ActivityDef extends Definition
{
  /**
   * Returns the value of the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Description</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Description</em>' attribute.
   * @see #setDescription(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef_Description()
   * @model
   * @generated
   */
  String getDescription();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDescription <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Description</em>' attribute.
   * @see #getDescription()
   * @generated
   */
  void setDescription(String value);

  /**
   * Returns the value of the '<em><b>Category</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Category</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Category</em>' attribute.
   * @see #setCategory(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef_Category()
   * @model
   * @generated
   */
  String getCategory();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getCategory <em>Category</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Category</em>' attribute.
   * @see #getCategory()
   * @generated
   */
  void setCategory(String value);

  /**
   * Returns the value of the '<em><b>Duration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Duration</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Duration</em>' attribute.
   * @see #setDuration(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef_Duration()
   * @model
   * @generated
   */
  String getDuration();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDuration <em>Duration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Duration</em>' attribute.
   * @see #getDuration()
   * @generated
   */
  void setDuration(String value);

  /**
   * Returns the value of the '<em><b>Display Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Display Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Display Name</em>' attribute.
   * @see #setDisplayName(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef_DisplayName()
   * @model
   * @generated
   */
  String getDisplayName();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDisplayName <em>Display Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Display Name</em>' attribute.
   * @see #getDisplayName()
   * @generated
   */
  void setDisplayName(String value);

  /**
   * Returns the value of the '<em><b>Hidden Params</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Hidden Params</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hidden Params</em>' attribute.
   * @see #setHiddenParams(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef_HiddenParams()
   * @model
   * @generated
   */
  String getHiddenParams();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getHiddenParams <em>Hidden Params</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hidden Params</em>' attribute.
   * @see #getHiddenParams()
   * @generated
   */
  void setHiddenParams(String value);

  /**
   * Returns the value of the '<em><b>Annotations</b></em>' containment reference list.
   * The list contents are of type {@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Annotations</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Annotations</em>' containment reference list.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef_Annotations()
   * @model containment="true"
   * @generated
   */
  EList<Annotation> getAnnotations();

  /**
   * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
   * The list contents are of type {@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameters</em>' containment reference list.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef_Parameters()
   * @model containment="true"
   * @generated
   */
  EList<ParameterDef> getParameters();

  /**
   * Returns the value of the '<em><b>Requirements</b></em>' containment reference list.
   * The list contents are of type {@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Requirement}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Requirements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Requirements</em>' containment reference list.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef_Requirements()
   * @model containment="true"
   * @generated
   */
  EList<Requirement> getRequirements();

  /**
   * Returns the value of the '<em><b>Effects</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Effects</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Effects</em>' attribute list.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getActivityDef_Effects()
   * @model unique="false"
   * @generated
   */
  EList<String> getEffects();

} // ActivityDef
