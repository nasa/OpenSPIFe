package gov.nasa.ensemble.dictionary.xtext.serializer;

import com.google.inject.Inject;
import gov.nasa.ensemble.dictionary.xtext.services.XDictionaryGrammarAccess;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ActivityGroupDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Annotation;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.AttributeDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ClaimableResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.Dictionary;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.EnumValue;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericRequirement;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.NumericResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ObjectDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.ReferenceDef;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.SharableResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.StateRequirement;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.StateResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.SummaryResource;
import gov.nasa.ensemble.dictionary.xtext.xDictionary.XDictionaryPackage;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ISemanticNodeProvider.INodesForEObjectProvider;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;

@SuppressWarnings("all")
public class XDictionarySemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private XDictionaryGrammarAccess grammarAccess;
	
	@Override
	public void createSequence(EObject context, EObject semanticObject) {
		if(semanticObject.eClass().getEPackage() == XDictionaryPackage.eINSTANCE) switch(semanticObject.eClass().getClassifierID()) {
			case XDictionaryPackage.ACTIVITY_DEF:
				if(context == grammarAccess.getActivityDefRule() ||
				   context == grammarAccess.getDefinitionRule()) {
					sequence_ActivityDef(context, (ActivityDef) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.ACTIVITY_GROUP_DEF:
				if(context == grammarAccess.getActivityGroupDefRule() ||
				   context == grammarAccess.getDefinitionRule()) {
					sequence_ActivityGroupDef(context, (ActivityGroupDef) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.ANNOTATION:
				if(context == grammarAccess.getAnnotationRule()) {
					sequence_Annotation(context, (Annotation) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.ATTRIBUTE_DEF:
				if(context == grammarAccess.getAttributeDefRule() ||
				   context == grammarAccess.getDefinitionRule() ||
				   context == grammarAccess.getParameterDefRule()) {
					sequence_AttributeDef(context, (AttributeDef) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.CLAIMABLE_RESOURCE:
				if(context == grammarAccess.getClaimableResourceRule() ||
				   context == grammarAccess.getDefinitionRule() ||
				   context == grammarAccess.getResourceDefRule()) {
					sequence_ClaimableResource(context, (ClaimableResource) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.DICTIONARY:
				if(context == grammarAccess.getDictionaryRule()) {
					sequence_Dictionary(context, (Dictionary) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.ENUM_DEF:
				if(context == grammarAccess.getDefinitionRule() ||
				   context == grammarAccess.getEnumDefRule()) {
					sequence_EnumDef(context, (EnumDef) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.ENUM_VALUE:
				if(context == grammarAccess.getEnumValueRule()) {
					sequence_EnumValue(context, (EnumValue) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.NUMERIC_REQUIREMENT:
				if(context == grammarAccess.getNumericRequirementRule() ||
				   context == grammarAccess.getRequirementRule()) {
					sequence_NumericRequirement(context, (NumericRequirement) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.NUMERIC_RESOURCE:
				if(context == grammarAccess.getDefinitionRule() ||
				   context == grammarAccess.getNumericResourceRule() ||
				   context == grammarAccess.getResourceDefRule()) {
					sequence_NumericResource(context, (NumericResource) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.OBJECT_DEF:
				if(context == grammarAccess.getDefinitionRule() ||
				   context == grammarAccess.getObjectDefRule()) {
					sequence_ObjectDef(context, (ObjectDef) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.REFERENCE_DEF:
				if(context == grammarAccess.getDefinitionRule() ||
				   context == grammarAccess.getParameterDefRule() ||
				   context == grammarAccess.getReferenceDefRule()) {
					sequence_ReferenceDef(context, (ReferenceDef) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.SHARABLE_RESOURCE:
				if(context == grammarAccess.getDefinitionRule() ||
				   context == grammarAccess.getResourceDefRule() ||
				   context == grammarAccess.getSharableResourceRule()) {
					sequence_SharableResource(context, (SharableResource) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.STATE_REQUIREMENT:
				if(context == grammarAccess.getRequirementRule() ||
				   context == grammarAccess.getStateRequirementRule()) {
					sequence_StateRequirement(context, (StateRequirement) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.STATE_RESOURCE:
				if(context == grammarAccess.getDefinitionRule() ||
				   context == grammarAccess.getResourceDefRule() ||
				   context == grammarAccess.getStateResourceRule()) {
					sequence_StateResource(context, (StateResource) semanticObject); 
					return; 
				}
				else break;
			case XDictionaryPackage.SUMMARY_RESOURCE:
				if(context == grammarAccess.getDefinitionRule() ||
				   context == grammarAccess.getResourceDefRule() ||
				   context == grammarAccess.getSummaryResourceRule()) {
					sequence_SummaryResource(context, (SummaryResource) semanticObject); 
					return; 
				}
				else break;
			}
		if (errorAcceptor != null) errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Constraint:
	 *     (
	 *         name=ID 
	 *         description=STRING? 
	 *         category=STRING? 
	 *         duration=STRING? 
	 *         displayName=STRING? 
	 *         hiddenParams=STRING? 
	 *         annotations+=Annotation* 
	 *         parameters+=ParameterDef* 
	 *         requirements+=Requirement* 
	 *         effects+=Effect*
	 *     )
	 */
	protected void sequence_ActivityDef(EObject context, ActivityDef semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID annotations+=Annotation* parameters+=ParameterDef*)
	 */
	protected void sequence_ActivityGroupDef(EObject context, ActivityGroupDef semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (source=STRING key=STRING value=STRING)
	 */
	protected void sequence_Annotation(EObject context, Annotation semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.ANNOTATION__SOURCE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.ANNOTATION__SOURCE));
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.ANNOTATION__KEY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.ANNOTATION__KEY));
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.ANNOTATION__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.ANNOTATION__VALUE));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getAnnotationAccess().getSourceSTRINGTerminalRuleCall_1_0(), semanticObject.getSource());
		feeder.accept(grammarAccess.getAnnotationAccess().getKeySTRINGTerminalRuleCall_2_0(), semanticObject.getKey());
		feeder.accept(grammarAccess.getAnnotationAccess().getValueSTRINGTerminalRuleCall_3_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         name=ID 
	 *         type=STRING 
	 *         defaultValueLiteral=STRING? 
	 *         description=STRING? 
	 *         shortDescription=STRING? 
	 *         units=STRING? 
	 *         displayName=STRING? 
	 *         category=STRING? 
	 *         parameterName=STRING? 
	 *         annotations+=Annotation*
	 *     )
	 */
	protected void sequence_AttributeDef(EObject context, AttributeDef semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_ClaimableResource(EObject context, ClaimableResource semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getClaimableResourceAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         name=ID 
	 *         author=STRING? 
	 *         date=STRING? 
	 *         description=STRING? 
	 *         version=STRING? 
	 *         domain=STRING 
	 *         definitions+=Definition*
	 *     )
	 */
	protected void sequence_Dictionary(EObject context, Dictionary semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=ID values+=EnumValue*)
	 */
	protected void sequence_EnumDef(EObject context, EnumDef semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     (name=STRING color=STRING? literal=STRING?)
	 */
	protected void sequence_EnumValue(EObject context, EnumValue semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     expression=STRING
	 */
	protected void sequence_NumericRequirement(EObject context, NumericRequirement semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.NUMERIC_REQUIREMENT__EXPRESSION) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.NUMERIC_REQUIREMENT__EXPRESSION));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getNumericRequirementAccess().getExpressionSTRINGTerminalRuleCall_3_0(), semanticObject.getExpression());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_NumericResource(EObject context, NumericResource semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getNumericResourceAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_ObjectDef(EObject context, ObjectDef semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getObjectDefAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (
	 *         name=ID 
	 *         type=STRING 
	 *         description=STRING? 
	 *         displayName=STRING? 
	 *         category=STRING? 
	 *         containment=Boolean? 
	 *         annotations+=Annotation* 
	 *         requirements+=Requirement* 
	 *         effects+=Effect*
	 *     )
	 */
	protected void sequence_ReferenceDef(EObject context, ReferenceDef semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_SharableResource(EObject context, SharableResource semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getSharableResourceAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     (definition=STRING requiredState=STRING)
	 */
	protected void sequence_StateRequirement(EObject context, StateRequirement semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_StateResource(EObject context, StateResource semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getStateResourceAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.finish();
	}
	
	
	/**
	 * Constraint:
	 *     name=ID
	 */
	protected void sequence_SummaryResource(EObject context, SummaryResource semanticObject) {
		if(errorAcceptor != null) {
			if(transientValues.isValueTransient(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, XDictionaryPackage.Literals.DEFINITION__NAME));
		}
		INodesForEObjectProvider nodes = createNodeProvider(semanticObject);
		SequenceFeeder feeder = createSequencerFeeder(semanticObject, nodes);
		feeder.accept(grammarAccess.getSummaryResourceAccess().getNameIDTerminalRuleCall_1_0(), semanticObject.getName());
		feeder.finish();
	}
}
