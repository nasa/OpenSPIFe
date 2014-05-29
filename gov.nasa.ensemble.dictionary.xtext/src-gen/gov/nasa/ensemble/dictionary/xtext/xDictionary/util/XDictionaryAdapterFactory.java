/**
 */
package gov.nasa.ensemble.dictionary.xtext.xDictionary.util;

import gov.nasa.ensemble.dictionary.xtext.xDictionary.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage
 * @generated
 */
public class XDictionaryAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static XDictionaryPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XDictionaryAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = XDictionaryPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected XDictionarySwitch<Adapter> modelSwitch =
    new XDictionarySwitch<Adapter>()
    {
      @Override
      public Adapter caseDictionary(Dictionary object)
      {
        return createDictionaryAdapter();
      }
      @Override
      public Adapter caseDefinition(Definition object)
      {
        return createDefinitionAdapter();
      }
      @Override
      public Adapter caseEnumDef(EnumDef object)
      {
        return createEnumDefAdapter();
      }
      @Override
      public Adapter caseEnumValue(EnumValue object)
      {
        return createEnumValueAdapter();
      }
      @Override
      public Adapter caseParameterDef(ParameterDef object)
      {
        return createParameterDefAdapter();
      }
      @Override
      public Adapter caseAttributeDef(AttributeDef object)
      {
        return createAttributeDefAdapter();
      }
      @Override
      public Adapter caseReferenceDef(ReferenceDef object)
      {
        return createReferenceDefAdapter();
      }
      @Override
      public Adapter caseActivityDef(ActivityDef object)
      {
        return createActivityDefAdapter();
      }
      @Override
      public Adapter caseAnnotation(Annotation object)
      {
        return createAnnotationAdapter();
      }
      @Override
      public Adapter caseRequirement(Requirement object)
      {
        return createRequirementAdapter();
      }
      @Override
      public Adapter caseNumericRequirement(NumericRequirement object)
      {
        return createNumericRequirementAdapter();
      }
      @Override
      public Adapter caseStateRequirement(StateRequirement object)
      {
        return createStateRequirementAdapter();
      }
      @Override
      public Adapter caseActivityGroupDef(ActivityGroupDef object)
      {
        return createActivityGroupDefAdapter();
      }
      @Override
      public Adapter caseObjectDef(ObjectDef object)
      {
        return createObjectDefAdapter();
      }
      @Override
      public Adapter caseResourceDef(ResourceDef object)
      {
        return createResourceDefAdapter();
      }
      @Override
      public Adapter caseNumericResource(NumericResource object)
      {
        return createNumericResourceAdapter();
      }
      @Override
      public Adapter caseStateResource(StateResource object)
      {
        return createStateResourceAdapter();
      }
      @Override
      public Adapter caseClaimableResource(ClaimableResource object)
      {
        return createClaimableResourceAdapter();
      }
      @Override
      public Adapter caseSharableResource(SharableResource object)
      {
        return createSharableResourceAdapter();
      }
      @Override
      public Adapter caseSummaryResource(SummaryResource object)
      {
        return createSummaryResourceAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary <em>Dictionary</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary
   * @generated
   */
  public Adapter createDictionaryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Definition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Definition
   * @generated
   */
  public Adapter createDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef <em>Enum Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef
   * @generated
   */
  public Adapter createEnumDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue <em>Enum Value</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue
   * @generated
   */
  public Adapter createEnumValueAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef <em>Parameter Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ParameterDef
   * @generated
   */
  public Adapter createParameterDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef <em>Attribute Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef
   * @generated
   */
  public Adapter createAttributeDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef <em>Reference Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef
   * @generated
   */
  public Adapter createReferenceDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef <em>Activity Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef
   * @generated
   */
  public Adapter createActivityDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation <em>Annotation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation
   * @generated
   */
  public Adapter createAnnotationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.Requirement <em>Requirement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.Requirement
   * @generated
   */
  public Adapter createRequirementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement <em>Numeric Requirement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement
   * @generated
   */
  public Adapter createNumericRequirementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement <em>State Requirement</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement
   * @generated
   */
  public Adapter createStateRequirementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef <em>Activity Group Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef
   * @generated
   */
  public Adapter createActivityGroupDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ObjectDef <em>Object Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ObjectDef
   * @generated
   */
  public Adapter createObjectDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ResourceDef <em>Resource Def</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ResourceDef
   * @generated
   */
  public Adapter createResourceDefAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericResource <em>Numeric Resource</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericResource
   * @generated
   */
  public Adapter createNumericResourceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.StateResource <em>State Resource</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.StateResource
   * @generated
   */
  public Adapter createStateResourceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.ClaimableResource <em>Claimable Resource</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.ClaimableResource
   * @generated
   */
  public Adapter createClaimableResourceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.SharableResource <em>Sharable Resource</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.SharableResource
   * @generated
   */
  public Adapter createSharableResourceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link gov.nasa.ensemble.dictionary.xtext.xDictionary.SummaryResource <em>Summary Resource</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see gov.nasa.ensemble.dictionary.xtext.xDictionary.SummaryResource
   * @generated
   */
  public Adapter createSummaryResourceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //XDictionaryAdapterFactory
