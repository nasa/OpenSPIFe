/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary.impl;

import gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ClaimableResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Definition;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ObjectDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Requirement;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ResourceDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.SharableResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.StateResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.SummaryResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryFactory;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XDictionaryPackageImpl extends EPackageImpl implements XDictionaryPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dictionaryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass definitionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass enumDefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass enumValueEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass parameterDefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass attributeDefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass referenceDefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass activityDefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass annotationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass requirementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass numericRequirementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stateRequirementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass activityGroupDefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass objectDefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass resourceDefEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass numericResourceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stateResourceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass claimableResourceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass sharableResourceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass summaryResourceEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private XDictionaryPackageImpl()
  {
    super(eNS_URI, XDictionaryFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link XDictionaryPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static XDictionaryPackage init()
  {
    if (isInited) return (XDictionaryPackage)EPackage.Registry.INSTANCE.getEPackage(XDictionaryPackage.eNS_URI);

    // Obtain or create and register package
    XDictionaryPackageImpl theXDictionaryPackage = (XDictionaryPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof XDictionaryPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new XDictionaryPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theXDictionaryPackage.createPackageContents();

    // Initialize created meta-data
    theXDictionaryPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theXDictionaryPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(XDictionaryPackage.eNS_URI, theXDictionaryPackage);
    return theXDictionaryPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getDictionary()
  {
    return dictionaryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getDictionary_Name()
  {
    return (EAttribute)dictionaryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getDictionary_Author()
  {
    return (EAttribute)dictionaryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getDictionary_Date()
  {
    return (EAttribute)dictionaryEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getDictionary_Description()
  {
    return (EAttribute)dictionaryEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getDictionary_Version()
  {
    return (EAttribute)dictionaryEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getDictionary_Domain()
  {
    return (EAttribute)dictionaryEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EReference getDictionary_Definitions()
  {
    return (EReference)dictionaryEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getDefinition()
  {
    return definitionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getDefinition_Name()
  {
    return (EAttribute)definitionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getEnumDef()
  {
    return enumDefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EReference getEnumDef_Values()
  {
    return (EReference)enumDefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getEnumValue()
  {
    return enumValueEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getEnumValue_Name()
  {
    return (EAttribute)enumValueEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getEnumValue_Color()
  {
    return (EAttribute)enumValueEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getEnumValue_Literal()
  {
    return (EAttribute)enumValueEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getParameterDef()
  {
    return parameterDefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getParameterDef_Type()
  {
    return (EAttribute)parameterDefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getParameterDef_Description()
  {
    return (EAttribute)parameterDefEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getParameterDef_DisplayName()
  {
    return (EAttribute)parameterDefEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getParameterDef_Category()
  {
    return (EAttribute)parameterDefEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EReference getParameterDef_Annotations()
  {
    return (EReference)parameterDefEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getAttributeDef()
  {
    return attributeDefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getAttributeDef_DefaultValueLiteral()
  {
    return (EAttribute)attributeDefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getAttributeDef_ShortDescription()
  {
    return (EAttribute)attributeDefEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getAttributeDef_Units()
  {
    return (EAttribute)attributeDefEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getAttributeDef_ParameterName()
  {
    return (EAttribute)attributeDefEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getReferenceDef()
  {
    return referenceDefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getReferenceDef_Containment()
  {
    return (EAttribute)referenceDefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EReference getReferenceDef_Requirements()
  {
    return (EReference)referenceDefEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getReferenceDef_Effects()
  {
    return (EAttribute)referenceDefEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getActivityDef()
  {
    return activityDefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getActivityDef_Description()
  {
    return (EAttribute)activityDefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getActivityDef_Category()
  {
    return (EAttribute)activityDefEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getActivityDef_Duration()
  {
    return (EAttribute)activityDefEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getActivityDef_DisplayName()
  {
    return (EAttribute)activityDefEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getActivityDef_HiddenParams()
  {
    return (EAttribute)activityDefEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EReference getActivityDef_Annotations()
  {
    return (EReference)activityDefEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EReference getActivityDef_Parameters()
  {
    return (EReference)activityDefEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EReference getActivityDef_Requirements()
  {
    return (EReference)activityDefEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getActivityDef_Effects()
  {
    return (EAttribute)activityDefEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getAnnotation()
  {
    return annotationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getAnnotation_Source()
  {
    return (EAttribute)annotationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getAnnotation_Key()
  {
    return (EAttribute)annotationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getAnnotation_Value()
  {
    return (EAttribute)annotationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getRequirement()
  {
    return requirementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getNumericRequirement()
  {
    return numericRequirementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getNumericRequirement_Expression()
  {
    return (EAttribute)numericRequirementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getStateRequirement()
  {
    return stateRequirementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getStateRequirement_Definition()
  {
    return (EAttribute)stateRequirementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EAttribute getStateRequirement_RequiredState()
  {
    return (EAttribute)stateRequirementEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getActivityGroupDef()
  {
    return activityGroupDefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EReference getActivityGroupDef_Annotations()
  {
    return (EReference)activityGroupDefEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EReference getActivityGroupDef_Parameters()
  {
    return (EReference)activityGroupDefEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getObjectDef()
  {
    return objectDefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getResourceDef()
  {
    return resourceDefEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getNumericResource()
  {
    return numericResourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getStateResource()
  {
    return stateResourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getClaimableResource()
  {
    return claimableResourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getSharableResource()
  {
    return sharableResourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public EClass getSummaryResource()
  {
    return summaryResourceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
public XDictionaryFactory getXDictionaryFactory()
  {
    return (XDictionaryFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    dictionaryEClass = createEClass(DICTIONARY);
    createEAttribute(dictionaryEClass, DICTIONARY__NAME);
    createEAttribute(dictionaryEClass, DICTIONARY__AUTHOR);
    createEAttribute(dictionaryEClass, DICTIONARY__DATE);
    createEAttribute(dictionaryEClass, DICTIONARY__DESCRIPTION);
    createEAttribute(dictionaryEClass, DICTIONARY__VERSION);
    createEAttribute(dictionaryEClass, DICTIONARY__DOMAIN);
    createEReference(dictionaryEClass, DICTIONARY__DEFINITIONS);

    definitionEClass = createEClass(DEFINITION);
    createEAttribute(definitionEClass, DEFINITION__NAME);

    enumDefEClass = createEClass(ENUM_DEF);
    createEReference(enumDefEClass, ENUM_DEF__VALUES);

    enumValueEClass = createEClass(ENUM_VALUE);
    createEAttribute(enumValueEClass, ENUM_VALUE__NAME);
    createEAttribute(enumValueEClass, ENUM_VALUE__COLOR);
    createEAttribute(enumValueEClass, ENUM_VALUE__LITERAL);

    parameterDefEClass = createEClass(PARAMETER_DEF);
    createEAttribute(parameterDefEClass, PARAMETER_DEF__TYPE);
    createEAttribute(parameterDefEClass, PARAMETER_DEF__DESCRIPTION);
    createEAttribute(parameterDefEClass, PARAMETER_DEF__DISPLAY_NAME);
    createEAttribute(parameterDefEClass, PARAMETER_DEF__CATEGORY);
    createEReference(parameterDefEClass, PARAMETER_DEF__ANNOTATIONS);

    attributeDefEClass = createEClass(ATTRIBUTE_DEF);
    createEAttribute(attributeDefEClass, ATTRIBUTE_DEF__DEFAULT_VALUE_LITERAL);
    createEAttribute(attributeDefEClass, ATTRIBUTE_DEF__SHORT_DESCRIPTION);
    createEAttribute(attributeDefEClass, ATTRIBUTE_DEF__UNITS);
    createEAttribute(attributeDefEClass, ATTRIBUTE_DEF__PARAMETER_NAME);

    referenceDefEClass = createEClass(REFERENCE_DEF);
    createEAttribute(referenceDefEClass, REFERENCE_DEF__CONTAINMENT);
    createEReference(referenceDefEClass, REFERENCE_DEF__REQUIREMENTS);
    createEAttribute(referenceDefEClass, REFERENCE_DEF__EFFECTS);

    activityDefEClass = createEClass(ACTIVITY_DEF);
    createEAttribute(activityDefEClass, ACTIVITY_DEF__DESCRIPTION);
    createEAttribute(activityDefEClass, ACTIVITY_DEF__CATEGORY);
    createEAttribute(activityDefEClass, ACTIVITY_DEF__DURATION);
    createEAttribute(activityDefEClass, ACTIVITY_DEF__DISPLAY_NAME);
    createEAttribute(activityDefEClass, ACTIVITY_DEF__HIDDEN_PARAMS);
    createEReference(activityDefEClass, ACTIVITY_DEF__ANNOTATIONS);
    createEReference(activityDefEClass, ACTIVITY_DEF__PARAMETERS);
    createEReference(activityDefEClass, ACTIVITY_DEF__REQUIREMENTS);
    createEAttribute(activityDefEClass, ACTIVITY_DEF__EFFECTS);

    annotationEClass = createEClass(ANNOTATION);
    createEAttribute(annotationEClass, ANNOTATION__SOURCE);
    createEAttribute(annotationEClass, ANNOTATION__KEY);
    createEAttribute(annotationEClass, ANNOTATION__VALUE);

    requirementEClass = createEClass(REQUIREMENT);

    numericRequirementEClass = createEClass(NUMERIC_REQUIREMENT);
    createEAttribute(numericRequirementEClass, NUMERIC_REQUIREMENT__EXPRESSION);

    stateRequirementEClass = createEClass(STATE_REQUIREMENT);
    createEAttribute(stateRequirementEClass, STATE_REQUIREMENT__DEFINITION);
    createEAttribute(stateRequirementEClass, STATE_REQUIREMENT__REQUIRED_STATE);

    activityGroupDefEClass = createEClass(ACTIVITY_GROUP_DEF);
    createEReference(activityGroupDefEClass, ACTIVITY_GROUP_DEF__ANNOTATIONS);
    createEReference(activityGroupDefEClass, ACTIVITY_GROUP_DEF__PARAMETERS);

    objectDefEClass = createEClass(OBJECT_DEF);

    resourceDefEClass = createEClass(RESOURCE_DEF);

    numericResourceEClass = createEClass(NUMERIC_RESOURCE);

    stateResourceEClass = createEClass(STATE_RESOURCE);

    claimableResourceEClass = createEClass(CLAIMABLE_RESOURCE);

    sharableResourceEClass = createEClass(SHARABLE_RESOURCE);

    summaryResourceEClass = createEClass(SUMMARY_RESOURCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    enumDefEClass.getESuperTypes().add(this.getDefinition());
    parameterDefEClass.getESuperTypes().add(this.getDefinition());
    attributeDefEClass.getESuperTypes().add(this.getParameterDef());
    referenceDefEClass.getESuperTypes().add(this.getParameterDef());
    activityDefEClass.getESuperTypes().add(this.getDefinition());
    numericRequirementEClass.getESuperTypes().add(this.getRequirement());
    stateRequirementEClass.getESuperTypes().add(this.getRequirement());
    activityGroupDefEClass.getESuperTypes().add(this.getDefinition());
    objectDefEClass.getESuperTypes().add(this.getDefinition());
    resourceDefEClass.getESuperTypes().add(this.getDefinition());
    numericResourceEClass.getESuperTypes().add(this.getResourceDef());
    stateResourceEClass.getESuperTypes().add(this.getResourceDef());
    claimableResourceEClass.getESuperTypes().add(this.getResourceDef());
    sharableResourceEClass.getESuperTypes().add(this.getResourceDef());
    summaryResourceEClass.getESuperTypes().add(this.getResourceDef());

    // Initialize classes and features; add operations and parameters
    initEClass(dictionaryEClass, Dictionary.class, "Dictionary", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDictionary_Name(), ecorePackage.getEString(), "name", null, 0, 1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDictionary_Author(), ecorePackage.getEString(), "author", null, 0, 1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDictionary_Date(), ecorePackage.getEString(), "date", null, 0, 1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDictionary_Description(), ecorePackage.getEString(), "description", null, 0, 1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDictionary_Version(), ecorePackage.getEString(), "version", null, 0, 1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDictionary_Domain(), ecorePackage.getEString(), "domain", null, 0, 1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDictionary_Definitions(), this.getDefinition(), null, "definitions", null, 0, -1, Dictionary.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(definitionEClass, Definition.class, "Definition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDefinition_Name(), ecorePackage.getEString(), "name", null, 0, 1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(enumDefEClass, EnumDef.class, "EnumDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getEnumDef_Values(), this.getEnumValue(), null, "values", null, 0, -1, EnumDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(enumValueEClass, EnumValue.class, "EnumValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getEnumValue_Name(), ecorePackage.getEString(), "name", null, 0, 1, EnumValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEnumValue_Color(), ecorePackage.getEString(), "color", null, 0, 1, EnumValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEnumValue_Literal(), ecorePackage.getEString(), "literal", null, 0, 1, EnumValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(parameterDefEClass, ParameterDef.class, "ParameterDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getParameterDef_Type(), ecorePackage.getEString(), "type", null, 0, 1, ParameterDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getParameterDef_Description(), ecorePackage.getEString(), "description", null, 0, 1, ParameterDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getParameterDef_DisplayName(), ecorePackage.getEString(), "displayName", null, 0, 1, ParameterDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getParameterDef_Category(), ecorePackage.getEString(), "category", null, 0, 1, ParameterDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getParameterDef_Annotations(), this.getAnnotation(), null, "annotations", null, 0, -1, ParameterDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(attributeDefEClass, AttributeDef.class, "AttributeDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAttributeDef_DefaultValueLiteral(), ecorePackage.getEString(), "defaultValueLiteral", null, 0, 1, AttributeDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAttributeDef_ShortDescription(), ecorePackage.getEString(), "shortDescription", null, 0, 1, AttributeDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAttributeDef_Units(), ecorePackage.getEString(), "units", null, 0, 1, AttributeDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAttributeDef_ParameterName(), ecorePackage.getEString(), "parameterName", null, 0, 1, AttributeDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(referenceDefEClass, ReferenceDef.class, "ReferenceDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getReferenceDef_Containment(), ecorePackage.getEString(), "containment", null, 0, 1, ReferenceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getReferenceDef_Requirements(), this.getRequirement(), null, "requirements", null, 0, -1, ReferenceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getReferenceDef_Effects(), ecorePackage.getEString(), "effects", null, 0, -1, ReferenceDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(activityDefEClass, ActivityDef.class, "ActivityDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getActivityDef_Description(), ecorePackage.getEString(), "description", null, 0, 1, ActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getActivityDef_Category(), ecorePackage.getEString(), "category", null, 0, 1, ActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getActivityDef_Duration(), ecorePackage.getEString(), "duration", null, 0, 1, ActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getActivityDef_DisplayName(), ecorePackage.getEString(), "displayName", null, 0, 1, ActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getActivityDef_HiddenParams(), ecorePackage.getEString(), "hiddenParams", null, 0, 1, ActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getActivityDef_Annotations(), this.getAnnotation(), null, "annotations", null, 0, -1, ActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getActivityDef_Parameters(), this.getParameterDef(), null, "parameters", null, 0, -1, ActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getActivityDef_Requirements(), this.getRequirement(), null, "requirements", null, 0, -1, ActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getActivityDef_Effects(), ecorePackage.getEString(), "effects", null, 0, -1, ActivityDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(annotationEClass, Annotation.class, "Annotation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAnnotation_Source(), ecorePackage.getEString(), "source", null, 0, 1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAnnotation_Key(), ecorePackage.getEString(), "key", null, 0, 1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAnnotation_Value(), ecorePackage.getEString(), "value", null, 0, 1, Annotation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(requirementEClass, Requirement.class, "Requirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(numericRequirementEClass, NumericRequirement.class, "NumericRequirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getNumericRequirement_Expression(), ecorePackage.getEString(), "expression", null, 0, 1, NumericRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stateRequirementEClass, StateRequirement.class, "StateRequirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getStateRequirement_Definition(), ecorePackage.getEString(), "definition", null, 0, 1, StateRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStateRequirement_RequiredState(), ecorePackage.getEString(), "requiredState", null, 0, 1, StateRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(activityGroupDefEClass, ActivityGroupDef.class, "ActivityGroupDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getActivityGroupDef_Annotations(), this.getAnnotation(), null, "annotations", null, 0, -1, ActivityGroupDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getActivityGroupDef_Parameters(), this.getParameterDef(), null, "parameters", null, 0, -1, ActivityGroupDef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(objectDefEClass, ObjectDef.class, "ObjectDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(resourceDefEClass, ResourceDef.class, "ResourceDef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(numericResourceEClass, NumericResource.class, "NumericResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(stateResourceEClass, StateResource.class, "StateResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(claimableResourceEClass, ClaimableResource.class, "ClaimableResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(sharableResourceEClass, SharableResource.class, "SharableResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(summaryResourceEClass, SummaryResource.class, "SummaryResource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    // Create resource
    createResource(eNS_URI);
  }

} //XDictionaryPackageImpl
