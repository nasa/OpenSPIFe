/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Numeric Requirement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getNumericRequirement()
 * @model
 * @generated
 */
public interface NumericRequirement extends Requirement
{
  /**
   * Returns the value of the '<em><b>Expression</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Expression</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expression</em>' attribute.
   * @see #setExpression(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getNumericRequirement_Expression()
   * @model
   * @generated
   */
  String getExpression();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement#getExpression <em>Expression</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expression</em>' attribute.
   * @see #getExpression()
   * @generated
   */
  void setExpression(String value);

} // NumericRequirement
