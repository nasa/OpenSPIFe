/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryFactory
 * @model kind="package"
 * @generated
 */
public interface XDictionaryPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "xDictionary";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.nasa.gov/ensemble/dictionary/xtext/XDictionary";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "xDictionary";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  XDictionaryPackage eINSTANCE = gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl.init();

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.DictionaryImpl <em>Dictionary</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.DictionaryImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getDictionary()
   * @generated
   */
  int DICTIONARY = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DICTIONARY__NAME = 0;

  /**
   * The feature id for the '<em><b>Author</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DICTIONARY__AUTHOR = 1;

  /**
   * The feature id for the '<em><b>Date</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DICTIONARY__DATE = 2;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DICTIONARY__DESCRIPTION = 3;

  /**
   * The feature id for the '<em><b>Version</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DICTIONARY__VERSION = 4;

  /**
   * The feature id for the '<em><b>Domain</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DICTIONARY__DOMAIN = 5;

  /**
   * The feature id for the '<em><b>Definitions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DICTIONARY__DEFINITIONS = 6;

  /**
   * The number of structural features of the '<em>Dictionary</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DICTIONARY_FEATURE_COUNT = 7;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.DefinitionImpl <em>Definition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.DefinitionImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getDefinition()
   * @generated
   */
  int DEFINITION = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__NAME = 0;

  /**
   * The number of structural features of the '<em>Definition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumDefImpl <em>Enum Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumDefImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getEnumDef()
   * @generated
   */
  int ENUM_DEF = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_DEF__NAME = DEFINITION__NAME;

  /**
   * The feature id for the '<em><b>Values</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_DEF__VALUES = DEFINITION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Enum Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_DEF_FEATURE_COUNT = DEFINITION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumValueImpl <em>Enum Value</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumValueImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getEnumValue()
   * @generated
   */
  int ENUM_VALUE = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_VALUE__NAME = 0;

  /**
   * The feature id for the '<em><b>Color</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_VALUE__COLOR = 1;

  /**
   * The feature id for the '<em><b>Literal</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_VALUE__LITERAL = 2;

  /**
   * The number of structural features of the '<em>Enum Value</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ENUM_VALUE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ParameterDefImpl <em>Parameter Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ParameterDefImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getParameterDef()
   * @generated
   */
  int PARAMETER_DEF = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_DEF__NAME = DEFINITION__NAME;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_DEF__TYPE = DEFINITION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_DEF__DESCRIPTION = DEFINITION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Display Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_DEF__DISPLAY_NAME = DEFINITION_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Category</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_DEF__CATEGORY = DEFINITION_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_DEF__ANNOTATIONS = DEFINITION_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the '<em>Parameter Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PARAMETER_DEF_FEATURE_COUNT = DEFINITION_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AttributeDefImpl <em>Attribute Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AttributeDefImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getAttributeDef()
   * @generated
   */
  int ATTRIBUTE_DEF = 5;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__NAME = PARAMETER_DEF__NAME;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__TYPE = PARAMETER_DEF__TYPE;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__DESCRIPTION = PARAMETER_DEF__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Display Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__DISPLAY_NAME = PARAMETER_DEF__DISPLAY_NAME;

  /**
   * The feature id for the '<em><b>Category</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__CATEGORY = PARAMETER_DEF__CATEGORY;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__ANNOTATIONS = PARAMETER_DEF__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Default Value Literal</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__DEFAULT_VALUE_LITERAL = PARAMETER_DEF_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Short Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__SHORT_DESCRIPTION = PARAMETER_DEF_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Units</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__UNITS = PARAMETER_DEF_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Parameter Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF__PARAMETER_NAME = PARAMETER_DEF_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>Attribute Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ATTRIBUTE_DEF_FEATURE_COUNT = PARAMETER_DEF_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ReferenceDefImpl <em>Reference Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ReferenceDefImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getReferenceDef()
   * @generated
   */
  int REFERENCE_DEF = 6;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF__NAME = PARAMETER_DEF__NAME;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF__TYPE = PARAMETER_DEF__TYPE;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF__DESCRIPTION = PARAMETER_DEF__DESCRIPTION;

  /**
   * The feature id for the '<em><b>Display Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF__DISPLAY_NAME = PARAMETER_DEF__DISPLAY_NAME;

  /**
   * The feature id for the '<em><b>Category</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF__CATEGORY = PARAMETER_DEF__CATEGORY;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF__ANNOTATIONS = PARAMETER_DEF__ANNOTATIONS;

  /**
   * The feature id for the '<em><b>Containment</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF__CONTAINMENT = PARAMETER_DEF_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Requirements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF__REQUIREMENTS = PARAMETER_DEF_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Effects</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF__EFFECTS = PARAMETER_DEF_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Reference Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REFERENCE_DEF_FEATURE_COUNT = PARAMETER_DEF_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl <em>Activity Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getActivityDef()
   * @generated
   */
  int ACTIVITY_DEF = 7;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__NAME = DEFINITION__NAME;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__DESCRIPTION = DEFINITION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Category</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__CATEGORY = DEFINITION_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Duration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__DURATION = DEFINITION_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Display Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__DISPLAY_NAME = DEFINITION_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Hidden Params</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__HIDDEN_PARAMS = DEFINITION_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__ANNOTATIONS = DEFINITION_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__PARAMETERS = DEFINITION_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>Requirements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__REQUIREMENTS = DEFINITION_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>Effects</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF__EFFECTS = DEFINITION_FEATURE_COUNT + 8;

  /**
   * The number of structural features of the '<em>Activity Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_DEF_FEATURE_COUNT = DEFINITION_FEATURE_COUNT + 9;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AnnotationImpl <em>Annotation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AnnotationImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getAnnotation()
   * @generated
   */
  int ANNOTATION = 8;

  /**
   * The feature id for the '<em><b>Source</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION__SOURCE = 0;

  /**
   * The feature id for the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION__KEY = 1;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION__VALUE = 2;

  /**
   * The number of structural features of the '<em>Annotation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ANNOTATION_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.RequirementImpl <em>Requirement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.RequirementImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getRequirement()
   * @generated
   */
  int REQUIREMENT = 9;

  /**
   * The number of structural features of the '<em>Requirement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int REQUIREMENT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.NumericRequirementImpl <em>Numeric Requirement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.NumericRequirementImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getNumericRequirement()
   * @generated
   */
  int NUMERIC_REQUIREMENT = 10;

  /**
   * The feature id for the '<em><b>Expression</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_REQUIREMENT__EXPRESSION = REQUIREMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>Numeric Requirement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_REQUIREMENT_FEATURE_COUNT = REQUIREMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.StateRequirementImpl <em>State Requirement</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.StateRequirementImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getStateRequirement()
   * @generated
   */
  int STATE_REQUIREMENT = 11;

  /**
   * The feature id for the '<em><b>Definition</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_REQUIREMENT__DEFINITION = REQUIREMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Required State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_REQUIREMENT__REQUIRED_STATE = REQUIREMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>State Requirement</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_REQUIREMENT_FEATURE_COUNT = REQUIREMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityGroupDefImpl <em>Activity Group Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityGroupDefImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getActivityGroupDef()
   * @generated
   */
  int ACTIVITY_GROUP_DEF = 12;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_GROUP_DEF__NAME = DEFINITION__NAME;

  /**
   * The feature id for the '<em><b>Annotations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_GROUP_DEF__ANNOTATIONS = DEFINITION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_GROUP_DEF__PARAMETERS = DEFINITION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Activity Group Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ACTIVITY_GROUP_DEF_FEATURE_COUNT = DEFINITION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ObjectDefImpl <em>Object Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ObjectDefImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getObjectDef()
   * @generated
   */
  int OBJECT_DEF = 13;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBJECT_DEF__NAME = DEFINITION__NAME;

  /**
   * The number of structural features of the '<em>Object Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OBJECT_DEF_FEATURE_COUNT = DEFINITION_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ResourceDefImpl <em>Resource Def</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ResourceDefImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getResourceDef()
   * @generated
   */
  int RESOURCE_DEF = 14;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_DEF__NAME = DEFINITION__NAME;

  /**
   * The number of structural features of the '<em>Resource Def</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RESOURCE_DEF_FEATURE_COUNT = DEFINITION_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.NumericResourceImpl <em>Numeric Resource</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.NumericResourceImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getNumericResource()
   * @generated
   */
  int NUMERIC_RESOURCE = 15;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_RESOURCE__NAME = RESOURCE_DEF__NAME;

  /**
   * The number of structural features of the '<em>Numeric Resource</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NUMERIC_RESOURCE_FEATURE_COUNT = RESOURCE_DEF_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.StateResourceImpl <em>State Resource</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.StateResourceImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getStateResource()
   * @generated
   */
  int STATE_RESOURCE = 16;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_RESOURCE__NAME = RESOURCE_DEF__NAME;

  /**
   * The number of structural features of the '<em>State Resource</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int STATE_RESOURCE_FEATURE_COUNT = RESOURCE_DEF_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ClaimableResourceImpl <em>Claimable Resource</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ClaimableResourceImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getClaimableResource()
   * @generated
   */
  int CLAIMABLE_RESOURCE = 17;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLAIMABLE_RESOURCE__NAME = RESOURCE_DEF__NAME;

  /**
   * The number of structural features of the '<em>Claimable Resource</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CLAIMABLE_RESOURCE_FEATURE_COUNT = RESOURCE_DEF_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.SharableResourceImpl <em>Sharable Resource</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.SharableResourceImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getSharableResource()
   * @generated
   */
  int SHARABLE_RESOURCE = 18;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SHARABLE_RESOURCE__NAME = RESOURCE_DEF__NAME;

  /**
   * The number of structural features of the '<em>Sharable Resource</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SHARABLE_RESOURCE_FEATURE_COUNT = RESOURCE_DEF_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.SummaryResourceImpl <em>Summary Resource</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.SummaryResourceImpl
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getSummaryResource()
   * @generated
   */
  int SUMMARY_RESOURCE = 19;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SUMMARY_RESOURCE__NAME = RESOURCE_DEF__NAME;

  /**
   * The number of structural features of the '<em>Summary Resource</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SUMMARY_RESOURCE_FEATURE_COUNT = RESOURCE_DEF_FEATURE_COUNT + 0;


  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary <em>Dictionary</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Dictionary</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary
   * @generated
   */
  EClass getDictionary();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getName()
   * @see #getDictionary()
   * @generated
   */
  EAttribute getDictionary_Name();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getAuthor <em>Author</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Author</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getAuthor()
   * @see #getDictionary()
   * @generated
   */
  EAttribute getDictionary_Author();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getDate <em>Date</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Date</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getDate()
   * @see #getDictionary()
   * @generated
   */
  EAttribute getDictionary_Date();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getDescription <em>Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Description</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getDescription()
   * @see #getDictionary()
   * @generated
   */
  EAttribute getDictionary_Description();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getVersion <em>Version</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Version</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getVersion()
   * @see #getDictionary()
   * @generated
   */
  EAttribute getDictionary_Version();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getDomain <em>Domain</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Domain</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getDomain()
   * @see #getDictionary()
   * @generated
   */
  EAttribute getDictionary_Domain();

  /**
   * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getDefinitions <em>Definitions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Definitions</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary#getDefinitions()
   * @see #getDictionary()
   * @generated
   */
  EReference getDictionary_Definitions();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Definition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Definition</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Definition
   * @generated
   */
  EClass getDefinition();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Definition#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Definition#getName()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_Name();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef <em>Enum Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Enum Def</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef
   * @generated
   */
  EClass getEnumDef();

  /**
   * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef#getValues <em>Values</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Values</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef#getValues()
   * @see #getEnumDef()
   * @generated
   */
  EReference getEnumDef_Values();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue <em>Enum Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Enum Value</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue
   * @generated
   */
  EClass getEnumValue();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getName()
   * @see #getEnumValue()
   * @generated
   */
  EAttribute getEnumValue_Name();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getColor <em>Color</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Color</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getColor()
   * @see #getEnumValue()
   * @generated
   */
  EAttribute getEnumValue_Color();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getLiteral <em>Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Literal</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue#getLiteral()
   * @see #getEnumValue()
   * @generated
   */
  EAttribute getEnumValue_Literal();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef <em>Parameter Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Parameter Def</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef
   * @generated
   */
  EClass getParameterDef();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getType()
   * @see #getParameterDef()
   * @generated
   */
  EAttribute getParameterDef_Type();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getDescription <em>Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Description</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getDescription()
   * @see #getParameterDef()
   * @generated
   */
  EAttribute getParameterDef_Description();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getDisplayName <em>Display Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Display Name</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getDisplayName()
   * @see #getParameterDef()
   * @generated
   */
  EAttribute getParameterDef_DisplayName();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getCategory <em>Category</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Category</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getCategory()
   * @see #getParameterDef()
   * @generated
   */
  EAttribute getParameterDef_Category();

  /**
   * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getAnnotations <em>Annotations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Annotations</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef#getAnnotations()
   * @see #getParameterDef()
   * @generated
   */
  EReference getParameterDef_Annotations();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef <em>Attribute Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Attribute Def</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef
   * @generated
   */
  EClass getAttributeDef();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getDefaultValueLiteral <em>Default Value Literal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Default Value Literal</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getDefaultValueLiteral()
   * @see #getAttributeDef()
   * @generated
   */
  EAttribute getAttributeDef_DefaultValueLiteral();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getShortDescription <em>Short Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Short Description</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getShortDescription()
   * @see #getAttributeDef()
   * @generated
   */
  EAttribute getAttributeDef_ShortDescription();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getUnits <em>Units</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Units</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getUnits()
   * @see #getAttributeDef()
   * @generated
   */
  EAttribute getAttributeDef_Units();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getParameterName <em>Parameter Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Parameter Name</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef#getParameterName()
   * @see #getAttributeDef()
   * @generated
   */
  EAttribute getAttributeDef_ParameterName();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef <em>Reference Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Reference Def</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef
   * @generated
   */
  EClass getReferenceDef();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getContainment <em>Containment</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Containment</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getContainment()
   * @see #getReferenceDef()
   * @generated
   */
  EAttribute getReferenceDef_Containment();

  /**
   * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getRequirements <em>Requirements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Requirements</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getRequirements()
   * @see #getReferenceDef()
   * @generated
   */
  EReference getReferenceDef_Requirements();

  /**
   * Returns the meta object for the attribute list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getEffects <em>Effects</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Effects</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef#getEffects()
   * @see #getReferenceDef()
   * @generated
   */
  EAttribute getReferenceDef_Effects();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef <em>Activity Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Activity Def</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef
   * @generated
   */
  EClass getActivityDef();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDescription <em>Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Description</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDescription()
   * @see #getActivityDef()
   * @generated
   */
  EAttribute getActivityDef_Description();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getCategory <em>Category</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Category</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getCategory()
   * @see #getActivityDef()
   * @generated
   */
  EAttribute getActivityDef_Category();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDuration <em>Duration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Duration</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDuration()
   * @see #getActivityDef()
   * @generated
   */
  EAttribute getActivityDef_Duration();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDisplayName <em>Display Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Display Name</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getDisplayName()
   * @see #getActivityDef()
   * @generated
   */
  EAttribute getActivityDef_DisplayName();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getHiddenParams <em>Hidden Params</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Hidden Params</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getHiddenParams()
   * @see #getActivityDef()
   * @generated
   */
  EAttribute getActivityDef_HiddenParams();

  /**
   * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getAnnotations <em>Annotations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Annotations</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getAnnotations()
   * @see #getActivityDef()
   * @generated
   */
  EReference getActivityDef_Annotations();

  /**
   * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getParameters <em>Parameters</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Parameters</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getParameters()
   * @see #getActivityDef()
   * @generated
   */
  EReference getActivityDef_Parameters();

  /**
   * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getRequirements <em>Requirements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Requirements</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getRequirements()
   * @see #getActivityDef()
   * @generated
   */
  EReference getActivityDef_Requirements();

  /**
   * Returns the meta object for the attribute list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getEffects <em>Effects</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Effects</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef#getEffects()
   * @see #getActivityDef()
   * @generated
   */
  EAttribute getActivityDef_Effects();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation <em>Annotation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Annotation</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation
   * @generated
   */
  EClass getAnnotation();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation#getSource <em>Source</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Source</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation#getSource()
   * @see #getAnnotation()
   * @generated
   */
  EAttribute getAnnotation_Source();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation#getKey <em>Key</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Key</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation#getKey()
   * @see #getAnnotation()
   * @generated
   */
  EAttribute getAnnotation_Key();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation#getValue()
   * @see #getAnnotation()
   * @generated
   */
  EAttribute getAnnotation_Value();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Requirement <em>Requirement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Requirement</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Requirement
   * @generated
   */
  EClass getRequirement();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement <em>Numeric Requirement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Numeric Requirement</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement
   * @generated
   */
  EClass getNumericRequirement();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Expression</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement#getExpression()
   * @see #getNumericRequirement()
   * @generated
   */
  EAttribute getNumericRequirement_Expression();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement <em>State Requirement</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>State Requirement</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement
   * @generated
   */
  EClass getStateRequirement();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement#getDefinition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Definition</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement#getDefinition()
   * @see #getStateRequirement()
   * @generated
   */
  EAttribute getStateRequirement_Definition();

  /**
   * Returns the meta object for the attribute '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement#getRequiredState <em>Required State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Required State</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement#getRequiredState()
   * @see #getStateRequirement()
   * @generated
   */
  EAttribute getStateRequirement_RequiredState();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef <em>Activity Group Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Activity Group Def</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef
   * @generated
   */
  EClass getActivityGroupDef();

  /**
   * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef#getAnnotations <em>Annotations</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Annotations</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef#getAnnotations()
   * @see #getActivityGroupDef()
   * @generated
   */
  EReference getActivityGroupDef_Annotations();

  /**
   * Returns the meta object for the containment reference list '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef#getParameters <em>Parameters</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Parameters</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef#getParameters()
   * @see #getActivityGroupDef()
   * @generated
   */
  EReference getActivityGroupDef_Parameters();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ObjectDef <em>Object Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Object Def</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ObjectDef
   * @generated
   */
  EClass getObjectDef();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ResourceDef <em>Resource Def</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Resource Def</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ResourceDef
   * @generated
   */
  EClass getResourceDef();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericResource <em>Numeric Resource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Numeric Resource</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericResource
   * @generated
   */
  EClass getNumericResource();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.StateResource <em>State Resource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>State Resource</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.StateResource
   * @generated
   */
  EClass getStateResource();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ClaimableResource <em>Claimable Resource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Claimable Resource</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ClaimableResource
   * @generated
   */
  EClass getClaimableResource();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.SharableResource <em>Sharable Resource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Sharable Resource</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.SharableResource
   * @generated
   */
  EClass getSharableResource();

  /**
   * Returns the meta object for class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.SummaryResource <em>Summary Resource</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Summary Resource</em>'.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.SummaryResource
   * @generated
   */
  EClass getSummaryResource();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  XDictionaryFactory getXDictionaryFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.DictionaryImpl <em>Dictionary</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.DictionaryImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getDictionary()
     * @generated
     */
    EClass DICTIONARY = eINSTANCE.getDictionary();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DICTIONARY__NAME = eINSTANCE.getDictionary_Name();

    /**
     * The meta object literal for the '<em><b>Author</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DICTIONARY__AUTHOR = eINSTANCE.getDictionary_Author();

    /**
     * The meta object literal for the '<em><b>Date</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DICTIONARY__DATE = eINSTANCE.getDictionary_Date();

    /**
     * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DICTIONARY__DESCRIPTION = eINSTANCE.getDictionary_Description();

    /**
     * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DICTIONARY__VERSION = eINSTANCE.getDictionary_Version();

    /**
     * The meta object literal for the '<em><b>Domain</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DICTIONARY__DOMAIN = eINSTANCE.getDictionary_Domain();

    /**
     * The meta object literal for the '<em><b>Definitions</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DICTIONARY__DEFINITIONS = eINSTANCE.getDictionary_Definitions();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.DefinitionImpl <em>Definition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.DefinitionImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getDefinition()
     * @generated
     */
    EClass DEFINITION = eINSTANCE.getDefinition();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DEFINITION__NAME = eINSTANCE.getDefinition_Name();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumDefImpl <em>Enum Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumDefImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getEnumDef()
     * @generated
     */
    EClass ENUM_DEF = eINSTANCE.getEnumDef();

    /**
     * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ENUM_DEF__VALUES = eINSTANCE.getEnumDef_Values();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumValueImpl <em>Enum Value</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.EnumValueImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getEnumValue()
     * @generated
     */
    EClass ENUM_VALUE = eINSTANCE.getEnumValue();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENUM_VALUE__NAME = eINSTANCE.getEnumValue_Name();

    /**
     * The meta object literal for the '<em><b>Color</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENUM_VALUE__COLOR = eINSTANCE.getEnumValue_Color();

    /**
     * The meta object literal for the '<em><b>Literal</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ENUM_VALUE__LITERAL = eINSTANCE.getEnumValue_Literal();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ParameterDefImpl <em>Parameter Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ParameterDefImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getParameterDef()
     * @generated
     */
    EClass PARAMETER_DEF = eINSTANCE.getParameterDef();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMETER_DEF__TYPE = eINSTANCE.getParameterDef_Type();

    /**
     * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMETER_DEF__DESCRIPTION = eINSTANCE.getParameterDef_Description();

    /**
     * The meta object literal for the '<em><b>Display Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMETER_DEF__DISPLAY_NAME = eINSTANCE.getParameterDef_DisplayName();

    /**
     * The meta object literal for the '<em><b>Category</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute PARAMETER_DEF__CATEGORY = eINSTANCE.getParameterDef_Category();

    /**
     * The meta object literal for the '<em><b>Annotations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference PARAMETER_DEF__ANNOTATIONS = eINSTANCE.getParameterDef_Annotations();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AttributeDefImpl <em>Attribute Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AttributeDefImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getAttributeDef()
     * @generated
     */
    EClass ATTRIBUTE_DEF = eINSTANCE.getAttributeDef();

    /**
     * The meta object literal for the '<em><b>Default Value Literal</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTRIBUTE_DEF__DEFAULT_VALUE_LITERAL = eINSTANCE.getAttributeDef_DefaultValueLiteral();

    /**
     * The meta object literal for the '<em><b>Short Description</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTRIBUTE_DEF__SHORT_DESCRIPTION = eINSTANCE.getAttributeDef_ShortDescription();

    /**
     * The meta object literal for the '<em><b>Units</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTRIBUTE_DEF__UNITS = eINSTANCE.getAttributeDef_Units();

    /**
     * The meta object literal for the '<em><b>Parameter Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ATTRIBUTE_DEF__PARAMETER_NAME = eINSTANCE.getAttributeDef_ParameterName();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ReferenceDefImpl <em>Reference Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ReferenceDefImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getReferenceDef()
     * @generated
     */
    EClass REFERENCE_DEF = eINSTANCE.getReferenceDef();

    /**
     * The meta object literal for the '<em><b>Containment</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REFERENCE_DEF__CONTAINMENT = eINSTANCE.getReferenceDef_Containment();

    /**
     * The meta object literal for the '<em><b>Requirements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference REFERENCE_DEF__REQUIREMENTS = eINSTANCE.getReferenceDef_Requirements();

    /**
     * The meta object literal for the '<em><b>Effects</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute REFERENCE_DEF__EFFECTS = eINSTANCE.getReferenceDef_Effects();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl <em>Activity Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityDefImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getActivityDef()
     * @generated
     */
    EClass ACTIVITY_DEF = eINSTANCE.getActivityDef();

    /**
     * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ACTIVITY_DEF__DESCRIPTION = eINSTANCE.getActivityDef_Description();

    /**
     * The meta object literal for the '<em><b>Category</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ACTIVITY_DEF__CATEGORY = eINSTANCE.getActivityDef_Category();

    /**
     * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ACTIVITY_DEF__DURATION = eINSTANCE.getActivityDef_Duration();

    /**
     * The meta object literal for the '<em><b>Display Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ACTIVITY_DEF__DISPLAY_NAME = eINSTANCE.getActivityDef_DisplayName();

    /**
     * The meta object literal for the '<em><b>Hidden Params</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ACTIVITY_DEF__HIDDEN_PARAMS = eINSTANCE.getActivityDef_HiddenParams();

    /**
     * The meta object literal for the '<em><b>Annotations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ACTIVITY_DEF__ANNOTATIONS = eINSTANCE.getActivityDef_Annotations();

    /**
     * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ACTIVITY_DEF__PARAMETERS = eINSTANCE.getActivityDef_Parameters();

    /**
     * The meta object literal for the '<em><b>Requirements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ACTIVITY_DEF__REQUIREMENTS = eINSTANCE.getActivityDef_Requirements();

    /**
     * The meta object literal for the '<em><b>Effects</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ACTIVITY_DEF__EFFECTS = eINSTANCE.getActivityDef_Effects();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AnnotationImpl <em>Annotation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.AnnotationImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getAnnotation()
     * @generated
     */
    EClass ANNOTATION = eINSTANCE.getAnnotation();

    /**
     * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ANNOTATION__SOURCE = eINSTANCE.getAnnotation_Source();

    /**
     * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ANNOTATION__KEY = eINSTANCE.getAnnotation_Key();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ANNOTATION__VALUE = eINSTANCE.getAnnotation_Value();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.RequirementImpl <em>Requirement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.RequirementImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getRequirement()
     * @generated
     */
    EClass REQUIREMENT = eINSTANCE.getRequirement();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.NumericRequirementImpl <em>Numeric Requirement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.NumericRequirementImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getNumericRequirement()
     * @generated
     */
    EClass NUMERIC_REQUIREMENT = eINSTANCE.getNumericRequirement();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NUMERIC_REQUIREMENT__EXPRESSION = eINSTANCE.getNumericRequirement_Expression();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.StateRequirementImpl <em>State Requirement</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.StateRequirementImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getStateRequirement()
     * @generated
     */
    EClass STATE_REQUIREMENT = eINSTANCE.getStateRequirement();

    /**
     * The meta object literal for the '<em><b>Definition</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STATE_REQUIREMENT__DEFINITION = eINSTANCE.getStateRequirement_Definition();

    /**
     * The meta object literal for the '<em><b>Required State</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute STATE_REQUIREMENT__REQUIRED_STATE = eINSTANCE.getStateRequirement_RequiredState();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityGroupDefImpl <em>Activity Group Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ActivityGroupDefImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getActivityGroupDef()
     * @generated
     */
    EClass ACTIVITY_GROUP_DEF = eINSTANCE.getActivityGroupDef();

    /**
     * The meta object literal for the '<em><b>Annotations</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ACTIVITY_GROUP_DEF__ANNOTATIONS = eINSTANCE.getActivityGroupDef_Annotations();

    /**
     * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ACTIVITY_GROUP_DEF__PARAMETERS = eINSTANCE.getActivityGroupDef_Parameters();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ObjectDefImpl <em>Object Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ObjectDefImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getObjectDef()
     * @generated
     */
    EClass OBJECT_DEF = eINSTANCE.getObjectDef();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ResourceDefImpl <em>Resource Def</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ResourceDefImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getResourceDef()
     * @generated
     */
    EClass RESOURCE_DEF = eINSTANCE.getResourceDef();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.NumericResourceImpl <em>Numeric Resource</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.NumericResourceImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getNumericResource()
     * @generated
     */
    EClass NUMERIC_RESOURCE = eINSTANCE.getNumericResource();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.StateResourceImpl <em>State Resource</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.StateResourceImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getStateResource()
     * @generated
     */
    EClass STATE_RESOURCE = eINSTANCE.getStateResource();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ClaimableResourceImpl <em>Claimable Resource</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.ClaimableResourceImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getClaimableResource()
     * @generated
     */
    EClass CLAIMABLE_RESOURCE = eINSTANCE.getClaimableResource();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.SharableResourceImpl <em>Sharable Resource</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.SharableResourceImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getSharableResource()
     * @generated
     */
    EClass SHARABLE_RESOURCE = eINSTANCE.getSharableResource();

    /**
     * The meta object literal for the '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.SummaryResourceImpl <em>Summary Resource</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.SummaryResourceImpl
     * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.impl.XDictionaryPackageImpl#getSummaryResource()
     * @generated
     */
    EClass SUMMARY_RESOURCE = eINSTANCE.getSummaryResource();

  }

} //XDictionaryPackage
