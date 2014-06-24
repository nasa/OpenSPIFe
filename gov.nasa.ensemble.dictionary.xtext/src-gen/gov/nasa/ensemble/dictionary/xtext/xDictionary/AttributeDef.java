/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Attribute Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getDefaultValueLiteral <em>Default Value Literal</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getShortDescription <em>Short Description</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getUnits <em>Units</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getParameterName <em>Parameter Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getAttributeDef()
 * @model
 * @generated
 */
public interface AttributeDef extends ParameterDef
{
  /**
   * Returns the value of the '<em><b>Default Value Literal</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default Value Literal</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default Value Literal</em>' attribute.
   * @see #setDefaultValueLiteral(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getAttributeDef_DefaultValueLiteral()
   * @model
   * @generated
   */
  String getDefaultValueLiteral();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getDefaultValueLiteral <em>Default Value Literal</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default Value Literal</em>' attribute.
   * @see #getDefaultValueLiteral()
   * @generated
   */
  void setDefaultValueLiteral(String value);

  /**
   * Returns the value of the '<em><b>Short Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Short Description</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Short Description</em>' attribute.
   * @see #setShortDescription(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getAttributeDef_ShortDescription()
   * @model
   * @generated
   */
  String getShortDescription();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getShortDescription <em>Short Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Short Description</em>' attribute.
   * @see #getShortDescription()
   * @generated
   */
  void setShortDescription(String value);

  /**
   * Returns the value of the '<em><b>Units</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Units</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Units</em>' attribute.
   * @see #setUnits(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getAttributeDef_Units()
   * @model
   * @generated
   */
  String getUnits();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getUnits <em>Units</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Units</em>' attribute.
   * @see #getUnits()
   * @generated
   */
  void setUnits(String value);

  /**
   * Returns the value of the '<em><b>Parameter Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parameter Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameter Name</em>' attribute.
   * @see #setParameterName(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getAttributeDef_ParameterName()
   * @model
   * @generated
   */
  String getParameterName();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getParameterName <em>Parameter Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parameter Name</em>' attribute.
   * @see #getParameterName()
   * @generated
   */
  void setParameterName(String value);

} // AttributeDef
