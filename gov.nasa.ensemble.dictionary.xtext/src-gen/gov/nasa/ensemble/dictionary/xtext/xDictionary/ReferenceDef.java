/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference Def</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getContainment <em>Containment</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getRequirements <em>Requirements</em>}</li>
 *   <li>{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getEffects <em>Effects</em>}</li>
 * </ul>
 * </p>
 *
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getReferenceDef()
 * @model
 * @generated
 */
public interface ReferenceDef extends ParameterDef
{
  /**
   * Returns the value of the '<em><b>Containment</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Containment</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Containment</em>' attribute.
   * @see #setContainment(String)
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getReferenceDef_Containment()
   * @model
   * @generated
   */
  String getContainment();

  /**
   * Sets the value of the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getContainment <em>Containment</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Containment</em>' attribute.
   * @see #getContainment()
   * @generated
   */
  void setContainment(String value);

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
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getReferenceDef_Requirements()
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
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#getReferenceDef_Effects()
   * @model unique="false"
   * @generated
   */
  EList<String> getEffects();

} // ReferenceDef
