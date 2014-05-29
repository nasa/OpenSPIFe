/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary.impl;

import gov.nasa.ensemble.dictionary.xtext.xDictionary.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XDictionaryFactoryImpl extends EFactoryImpl implements XDictionaryFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static XDictionaryFactory init()
  {
    try
    {
      XDictionaryFactory theXDictionaryFactory = (XDictionaryFactory)EPackage.Registry.INSTANCE.getEFactory(XDictionaryPackage.eNS_URI);
      if (theXDictionaryFactory != null)
      {
        return theXDictionaryFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new XDictionaryFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XDictionaryFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case XDictionaryPackage.DICTIONARY: return createDictionary();
      case XDictionaryPackage.DEFINITION: return createDefinition();
      case XDictionaryPackage.ENUM_DEF: return createEnumDef();
      case XDictionaryPackage.ENUM_VALUE: return createEnumValue();
      case XDictionaryPackage.PARAMETER_DEF: return createParameterDef();
      case XDictionaryPackage.ATTRIBUTE_DEF: return createAttributeDef();
      case XDictionaryPackage.REFERENCE_DEF: return createReferenceDef();
      case XDictionaryPackage.ACTIVITY_DEF: return createActivityDef();
      case XDictionaryPackage.ANNOTATION: return createAnnotation();
      case XDictionaryPackage.REQUIREMENT: return createRequirement();
      case XDictionaryPackage.NUMERIC_REQUIREMENT: return createNumericRequirement();
      case XDictionaryPackage.STATE_REQUIREMENT: return createStateRequirement();
      case XDictionaryPackage.ACTIVITY_GROUP_DEF: return createActivityGroupDef();
      case XDictionaryPackage.OBJECT_DEF: return createObjectDef();
      case XDictionaryPackage.RESOURCE_DEF: return createResourceDef();
      case XDictionaryPackage.NUMERIC_RESOURCE: return createNumericResource();
      case XDictionaryPackage.STATE_RESOURCE: return createStateResource();
      case XDictionaryPackage.CLAIMABLE_RESOURCE: return createClaimableResource();
      case XDictionaryPackage.SHARABLE_RESOURCE: return createSharableResource();
      case XDictionaryPackage.SUMMARY_RESOURCE: return createSummaryResource();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Dictionary createDictionary()
  {
    DictionaryImpl dictionary = new DictionaryImpl();
    return dictionary;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition createDefinition()
  {
    DefinitionImpl definition = new DefinitionImpl();
    return definition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EnumDef createEnumDef()
  {
    EnumDefImpl enumDef = new EnumDefImpl();
    return enumDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EnumValue createEnumValue()
  {
    EnumValueImpl enumValue = new EnumValueImpl();
    return enumValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ParameterDef createParameterDef()
  {
    ParameterDefImpl parameterDef = new ParameterDefImpl();
    return parameterDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AttributeDef createAttributeDef()
  {
    AttributeDefImpl attributeDef = new AttributeDefImpl();
    return attributeDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ReferenceDef createReferenceDef()
  {
    ReferenceDefImpl referenceDef = new ReferenceDefImpl();
    return referenceDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ActivityDef createActivityDef()
  {
    ActivityDefImpl activityDef = new ActivityDefImpl();
    return activityDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Annotation createAnnotation()
  {
    AnnotationImpl annotation = new AnnotationImpl();
    return annotation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Requirement createRequirement()
  {
    RequirementImpl requirement = new RequirementImpl();
    return requirement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NumericRequirement createNumericRequirement()
  {
    NumericRequirementImpl numericRequirement = new NumericRequirementImpl();
    return numericRequirement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StateRequirement createStateRequirement()
  {
    StateRequirementImpl stateRequirement = new StateRequirementImpl();
    return stateRequirement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ActivityGroupDef createActivityGroupDef()
  {
    ActivityGroupDefImpl activityGroupDef = new ActivityGroupDefImpl();
    return activityGroupDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ObjectDef createObjectDef()
  {
    ObjectDefImpl objectDef = new ObjectDefImpl();
    return objectDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResourceDef createResourceDef()
  {
    ResourceDefImpl resourceDef = new ResourceDefImpl();
    return resourceDef;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NumericResource createNumericResource()
  {
    NumericResourceImpl numericResource = new NumericResourceImpl();
    return numericResource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public StateResource createStateResource()
  {
    StateResourceImpl stateResource = new StateResourceImpl();
    return stateResource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ClaimableResource createClaimableResource()
  {
    ClaimableResourceImpl claimableResource = new ClaimableResourceImpl();
    return claimableResource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SharableResource createSharableResource()
  {
    SharableResourceImpl sharableResource = new SharableResourceImpl();
    return sharableResource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SummaryResource createSummaryResource()
  {
    SummaryResourceImpl summaryResource = new SummaryResourceImpl();
    return summaryResource;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XDictionaryPackage getXDictionaryPackage()
  {
    return (XDictionaryPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static XDictionaryPackage getPackage()
  {
    return XDictionaryPackage.eINSTANCE;
  }

} //XDictionaryFactoryImpl
