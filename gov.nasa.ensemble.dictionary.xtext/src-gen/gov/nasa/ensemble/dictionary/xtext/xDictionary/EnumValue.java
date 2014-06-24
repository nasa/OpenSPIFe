/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getName <em>Name</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getColor <em>Color</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getLiteral <em>Literal</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getEnumValue()
 * @model
 * @generated
 */
public interface EnumValue extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getEnumValue_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Color</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Color</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Color</em>' attribute.
   * @see #setColor(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getEnumValue_Color()
   * @model
   * @generated
   */
  String getColor();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getColor <em>Color</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Color</em>' attribute.
   * @see #getColor()
   * @generated
   */
  void setColor(String value);

  /**
   * Returns the value of the '<em><b>Literal</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Literal</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Literal</em>' attribute.
   * @see #setLiteral(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getEnumValue_Literal()
   * @model
   * @generated
   */
  String getLiteral();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getLiteral <em>Literal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Literal</em>' attribute.
   * @see #getLiteral()
   * @generated
   */
  void setLiteral(String value);

} // EnumValue
