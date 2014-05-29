/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage
 * @generated
 */
public interface XDictionaryFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  XDictionaryFactory eINSTANCE = gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Dictionary</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Dictionary</em>'.
   * @generated
   */
  Dictionary createDictionary();

  /**
   * Returns a new object of class '<em>Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Definition</em>'.
   * @generated
   */
  Definition createDefinition();

  /**
   * Returns a new object of class '<em>Enum Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Enum Def</em>'.
   * @generated
   */
  EnumDef createEnumDef();

  /**
   * Returns a new object of class '<em>Enum Value</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Enum Value</em>'.
   * @generated
   */
  EnumValue createEnumValue();

  /**
   * Returns a new object of class '<em>Parameter Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Parameter Def</em>'.
   * @generated
   */
  ParameterDef createParameterDef();

  /**
   * Returns a new object of class '<em>Attribute Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Attribute Def</em>'.
   * @generated
   */
  AttributeDef createAttributeDef();

  /**
   * Returns a new object of class '<em>Reference Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Reference Def</em>'.
   * @generated
   */
  ReferenceDef createReferenceDef();

  /**
   * Returns a new object of class '<em>Activity Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Activity Def</em>'.
   * @generated
   */
  ActivityDef createActivityDef();

  /**
   * Returns a new object of class '<em>Annotation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Annotation</em>'.
   * @generated
   */
  Annotation createAnnotation();

  /**
   * Returns a new object of class '<em>Requirement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Requirement</em>'.
   * @generated
   */
  Requirement createRequirement();

  /**
   * Returns a new object of class '<em>Numeric Requirement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Numeric Requirement</em>'.
   * @generated
   */
  NumericRequirement createNumericRequirement();

  /**
   * Returns a new object of class '<em>State Requirement</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>State Requirement</em>'.
   * @generated
   */
  StateRequirement createStateRequirement();

  /**
   * Returns a new object of class '<em>Activity Group Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Activity Group Def</em>'.
   * @generated
   */
  ActivityGroupDef createActivityGroupDef();

  /**
   * Returns a new object of class '<em>Object Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Object Def</em>'.
   * @generated
   */
  ObjectDef createObjectDef();

  /**
   * Returns a new object of class '<em>Resource Def</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Resource Def</em>'.
   * @generated
   */
  ResourceDef createResourceDef();

  /**
   * Returns a new object of class '<em>Numeric Resource</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Numeric Resource</em>'.
   * @generated
   */
  NumericResource createNumericResource();

  /**
   * Returns a new object of class '<em>State Resource</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>State Resource</em>'.
   * @generated
   */
  StateResource createStateResource();

  /**
   * Returns a new object of class '<em>Claimable Resource</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Claimable Resource</em>'.
   * @generated
   */
  ClaimableResource createClaimableResource();

  /**
   * Returns a new object of class '<em>Sharable Resource</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Sharable Resource</em>'.
   * @generated
   */
  SharableResource createSharableResource();

  /**
   * Returns a new object of class '<em>Summary Resource</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Summary Resource</em>'.
   * @generated
   */
  SummaryResource createSummaryResource();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  XDictionaryPackage getXDictionaryPackage();

} //XDictionaryFactory
