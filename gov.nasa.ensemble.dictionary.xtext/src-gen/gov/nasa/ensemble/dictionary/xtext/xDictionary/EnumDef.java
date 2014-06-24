/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef#getValues <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getEnumDef()
 * @model
 * @generated
 */
public interface EnumDef extends Definition
{
  /**
   * Returns the value of the '<em><b>Values</b></em>' containment reference list.
   * The list contents are of type {@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Values</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Values</em>' containment reference list.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getEnumDef_Values()
   * @model containment="true"
   * @generated
   */
  EList<EnumValue> getValues();

} // EnumDef
