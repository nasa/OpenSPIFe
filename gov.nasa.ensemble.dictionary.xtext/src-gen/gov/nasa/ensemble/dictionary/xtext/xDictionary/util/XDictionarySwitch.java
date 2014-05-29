/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary.util;

import gov.nasa.ensemble.dictionary.xtext.xDictionary.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage
 * @generated
 */
public class XDictionarySwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static XDictionaryPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XDictionarySwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = XDictionaryPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case XDictionaryPackage.DICTIONARY:
      {
        Dictionary dictionary = (Dictionary)theEObject;
        T result = caseDictionary(dictionary);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.DEFINITION:
      {
        Definition definition = (Definition)theEObject;
        T result = caseDefinition(definition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.ENUM_DEF:
      {
        EnumDef enumDef = (EnumDef)theEObject;
        T result = caseEnumDef(enumDef);
        if (result == null) result = caseDefinition(enumDef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.ENUM_VALUE:
      {
        EnumValue enumValue = (EnumValue)theEObject;
        T result = caseEnumValue(enumValue);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.PARAMETER_DEF:
      {
        ParameterDef parameterDef = (ParameterDef)theEObject;
        T result = caseParameterDef(parameterDef);
        if (result == null) result = caseDefinition(parameterDef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.ATTRIBUTE_DEF:
      {
        AttributeDef attributeDef = (AttributeDef)theEObject;
        T result = caseAttributeDef(attributeDef);
        if (result == null) result = caseParameterDef(attributeDef);
        if (result == null) result = caseDefinition(attributeDef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.REFERENCE_DEF:
      {
        ReferenceDef referenceDef = (ReferenceDef)theEObject;
        T result = caseReferenceDef(referenceDef);
        if (result == null) result = caseParameterDef(referenceDef);
        if (result == null) result = caseDefinition(referenceDef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.ACTIVITY_DEF:
      {
        ActivityDef activityDef = (ActivityDef)theEObject;
        T result = caseActivityDef(activityDef);
        if (result == null) result = caseDefinition(activityDef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.ANNOTATION:
      {
        Annotation annotation = (Annotation)theEObject;
        T result = caseAnnotation(annotation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.REQUIREMENT:
      {
        Requirement requirement = (Requirement)theEObject;
        T result = caseRequirement(requirement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.NUMERIC_REQUIREMENT:
      {
        NumericRequirement numericRequirement = (NumericRequirement)theEObject;
        T result = caseNumericRequirement(numericRequirement);
        if (result == null) result = caseRequirement(numericRequirement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.STATE_REQUIREMENT:
      {
        StateRequirement stateRequirement = (StateRequirement)theEObject;
        T result = caseStateRequirement(stateRequirement);
        if (result == null) result = caseRequirement(stateRequirement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.ACTIVITY_GROUP_DEF:
      {
        ActivityGroupDef activityGroupDef = (ActivityGroupDef)theEObject;
        T result = caseActivityGroupDef(activityGroupDef);
        if (result == null) result = caseDefinition(activityGroupDef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.OBJECT_DEF:
      {
        ObjectDef objectDef = (ObjectDef)theEObject;
        T result = caseObjectDef(objectDef);
        if (result == null) result = caseDefinition(objectDef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.RESOURCE_DEF:
      {
        ResourceDef resourceDef = (ResourceDef)theEObject;
        T result = caseResourceDef(resourceDef);
        if (result == null) result = caseDefinition(resourceDef);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.NUMERIC_RESOURCE:
      {
        NumericResource numericResource = (NumericResource)theEObject;
        T result = caseNumericResource(numericResource);
        if (result == null) result = caseResourceDef(numericResource);
        if (result == null) result = caseDefinition(numericResource);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.STATE_RESOURCE:
      {
        StateResource stateResource = (StateResource)theEObject;
        T result = caseStateResource(stateResource);
        if (result == null) result = caseResourceDef(stateResource);
        if (result == null) result = caseDefinition(stateResource);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.CLAIMABLE_RESOURCE:
      {
        ClaimableResource claimableResource = (ClaimableResource)theEObject;
        T result = caseClaimableResource(claimableResource);
        if (result == null) result = caseResourceDef(claimableResource);
        if (result == null) result = caseDefinition(claimableResource);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.SHARABLE_RESOURCE:
      {
        SharableResource sharableResource = (SharableResource)theEObject;
        T result = caseSharableResource(sharableResource);
        if (result == null) result = caseResourceDef(sharableResource);
        if (result == null) result = caseDefinition(sharableResource);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XDictionaryPackage.SUMMARY_RESOURCE:
      {
        SummaryResource summaryResource = (SummaryResource)theEObject;
        T result = caseSummaryResource(summaryResource);
        if (result == null) result = caseResourceDef(summaryResource);
        if (result == null) result = caseDefinition(summaryResource);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Dictionary</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Dictionary</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDictionary(Dictionary object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Definition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Definition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDefinition(Definition object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Enum Def</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Enum Def</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEnumDef(EnumDef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Enum Value</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Enum Value</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEnumValue(EnumValue object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Parameter Def</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parameter Def</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParameterDef(ParameterDef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Attribute Def</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Attribute Def</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAttributeDef(AttributeDef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Reference Def</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Reference Def</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseReferenceDef(ReferenceDef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Activity Def</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Activity Def</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseActivityDef(ActivityDef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Annotation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Annotation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAnnotation(Annotation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Requirement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Requirement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRequirement(Requirement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Numeric Requirement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Numeric Requirement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNumericRequirement(NumericRequirement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>State Requirement</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>State Requirement</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStateRequirement(StateRequirement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Activity Group Def</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Activity Group Def</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseActivityGroupDef(ActivityGroupDef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Object Def</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Object Def</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseObjectDef(ObjectDef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Resource Def</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Resource Def</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseResourceDef(ResourceDef object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Numeric Resource</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Numeric Resource</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNumericResource(NumericResource object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>State Resource</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>State Resource</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStateResource(StateResource object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Claimable Resource</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Claimable Resource</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseClaimableResource(ClaimableResource object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Sharable Resource</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Sharable Resource</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSharableResource(SharableResource object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Summary Resource</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Summary Resource</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSummaryResource(SummaryResource object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //XDictionarySwitch
