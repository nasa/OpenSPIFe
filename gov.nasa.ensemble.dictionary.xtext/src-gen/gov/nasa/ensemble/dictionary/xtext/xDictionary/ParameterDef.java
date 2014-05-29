/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getType <em>Type</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getDescription <em>Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getDisplayName <em>Display Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getCategory <em>Category</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getAnnotations <em>Annotations</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getParameterDef()
 * @model
 * @generated
 */
public interface ParameterDef extends Definition
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getParameterDef_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

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
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getParameterDef_Description()
   * @model
   * @generated
   */
  String getDescription();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getDescription <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Description</em>' attribute.
   * @see #getDescription()
   * @generated
   */
  void setDescription(String value);

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
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getParameterDef_DisplayName()
   * @model
   * @generated
   */
  String getDisplayName();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getDisplayName <em>Display Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Display Name</em>' attribute.
   * @see #getDisplayName()
   * @generated
   */
  void setDisplayName(String value);

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
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getParameterDef_Category()
   * @model
   * @generated
   */
  String getCategory();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getCategory <em>Category</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Category</em>' attribute.
   * @see #getCategory()
   * @generated
   */
  void setCategory(String value);

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
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getParameterDef_Annotations()
   * @model containment="true"
   * @generated
   */
  EList<Annotation> getAnnotations();

} // ParameterDef
