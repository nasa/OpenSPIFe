/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.core.plan.formula.js.constraint;

import static gov.nasa.ensemble.core.plan.formula.js.constraint.JSConstraintUtils.*;

import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.javascript.rhino.FormulaInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintStatus;

public class JSAnnotationConstraint extends AbstractModelConstraint {
	
	private static final String CONSTRAINT_SOURCE = "constraint";
	private static final String MESSAGE_SOURCE = "message";
	
	private Map<EClass, List<String>> classConstraints = new HashMap<EClass, List<String>>();
	private Map<EClass, List<EAnnotation>> featureConstraints = new HashMap<EClass, List<EAnnotation>>();
	private Map<EStructuralFeature, Map<EClass, List<String>>> affectedConstraints = new HashMap<EStructuralFeature, Map<EClass, List<String>>>();
	private JSEvaluator evaluator = new JSEvaluator();

	/**
	 * Validates an EObject in the given context to ensure that constraints defined as
	 * annotations on the object's EClass are satisfied
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @return IStatus the status object giving the result of the validation
	 */
	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject target = ctx.getTarget();
		EClass eClass = target.eClass();
		List<IStatus> failureList = new ArrayList<IStatus>(3);
		EMFEventType eventType = ctx.getEventType();
		// In the case of batch mode.
		if (eventType == EMFEventType.NULL) {
			List<EAnnotation> constraintAnnotations = getFeatureConstraints(eClass);
			for (EAnnotation annotation : constraintAnnotations) {
				EStructuralFeature feature = (EStructuralFeature)annotation.getEModelElement();
				if (feature.isMany()) {
					for (Object value : (Collection<?>)target.eGet(feature)) {
						validateFeatureConstraints(ctx, target, feature, annotation, value, failureList);
					}
				} else {
					validateFeatureConstraints(ctx, target, feature, annotation, target.eGet(feature), failureList);
				}
			}
			validateObjectConstraints(ctx, target, eClass, getAllConstraints(eClass), failureList);
		} else if (eventType == EMFEventType.SET || eventType == EMFEventType.ADD || eventType == EMFEventType.ADD_MANY) {
			// In the case of live mode
			EStructuralFeature feature = ctx.getFeature();
			EAnnotation annotation = feature.getEAnnotation(CONSTRAINT_SOURCE);
			if (annotation != null) {
				if (eventType == EMFEventType.ADD_MANY) {
					for (Object value : (Collection<?>)ctx.getFeatureNewValue()) {
						validateFeatureConstraints(ctx, target, feature, annotation, value, failureList);
					}
				} else {
					validateFeatureConstraints(ctx, target, feature, annotation, ctx.getFeatureNewValue(), failureList);
				}
			}
			validateAffectedObjectConstraints(ctx, target, eClass, feature, failureList);
		}
		if (!failureList.isEmpty()) {
			return combinedStatus(ctx, target, failureList);
		}
		return ctx.createSuccessStatus();
	}
	
	/**
	 * Tests the value of a feature of a target object to determine whether the value violates any constraints
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param target the EObject to be validated
	 * @param feature the EStructuralFeature whose value is to be validated
	 * @param EAnnotation an EMF annotation with JavaScript constraints
	 * @param value the Object value of the feature that is to be validated
	 * @param failures an accumulator list of failure IStatus objects for any constraint violations
	 */
	private void validateFeatureConstraints(IValidationContext ctx, EObject target, EStructuralFeature feature,
			EAnnotation annotation, Object value, List<IStatus> failures) {
		for (Map.Entry<String,String> entry : annotation.getDetails()) {
			String constraintName = entry.getKey();
			String expression = entry.getValue();
			if (expression != null && expression.length() > 0) {
				Object result = evaluator.getValue(target, expression, true, value);
				if (result instanceof Boolean && !((Boolean)result).booleanValue()) {
					String message = EMFUtils.getAnnotation(feature, MESSAGE_SOURCE, constraintName);
					boolean batch = ctx.getEventType() == EMFEventType.NULL;
					String template = batch?"The {0} of {1} {2}":"The value {0}";
					int severityCode = IStatus.ERROR;
					if (message == null || message.length() < 1) {
						template = batch?"The {0} of {1} must satisfy {2}":"The value must satisfy {0}";
						message = expression;
					}
					Collection<EObject> locus = EMFUtils.getConstraintStatusLocus(target, feature);
					if (batch) {
						String featureName =  EMFUtils.getDisplayName(target, feature);
						failures.add(ConstraintStatus.createStatus(ctx, locus, severityCode, 1, template, featureName, targetName(target), message));
					} else {
						failures.add(ConstraintStatus.createStatus(ctx, locus, severityCode, 1, template, message));
					}
				}
			}
		}
	}
	
	/**
	 * Tests a target object to determine whether it violates any of the constraints associated with the object's
	 * EMF class
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param target the EObject to be validated
	 * @param eClass the target object's EClass
	 * @param constraintNames a list of the names of the constraints to be tested
	 * @param failures an accumulator list of failure IStatus objects for any constraint violations
	 */
	private void validateObjectConstraints(IValidationContext ctx, EObject target, EClass eClass, List<String> constraintNames, List<IStatus> failures) {
		for (String name : constraintNames) {
			String expression = lookupConstraintInfo(eClass, name, CONSTRAINT_SOURCE);
			if (expression != null && expression.length() > 0) {
				Object result = evaluator.getValue(target, expression, false, null);
				if (result instanceof Boolean && !((Boolean)result).booleanValue()) {
					String message = lookupConstraintInfo(eClass, name, MESSAGE_SOURCE);
					int severityCode = IStatus.ERROR;
					if (message == null || message.length() < 1) {
						message = "{0} must satisfy " + expression;
					} else {
						int commaPos = message.indexOf(',');
						if (commaPos > 0) {
							String severity = message.substring(0, commaPos);
							severityCode = getSeverityCode(severity);
							if (severityCode != IStatus.OK) {
								message = message.substring(commaPos + 1);
							} else {
								severityCode = IStatus.ERROR;
							}
						}
					}
					// Record the target EObject and involved structural features in the status object for use in error
					// decorations and explanations
					Collection<EObject> locus = EMFUtils.getConstraintStatusLocus(target, eClass, FormulaInfo.getVariableNames(expression));
					failures.add(ConstraintStatus.createStatus(ctx, locus, severityCode, 1, message, targetName(target)));
				}
			}
		}
	}
	
	/**
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param target the EObject to be validated
	 * @param eClass the target object's EClass
	 * @param feature the EStructuralFeature whose whose new value is being validated
	 * @param failures an accumulator list of failure IStatus objects for any constraint violations
	 */
	private void validateAffectedObjectConstraints(IValidationContext ctx, EObject target, EClass eClass, EStructuralFeature feature, List<IStatus> failures) {
		for (String name : getAffectedConstraints(eClass, feature)) {
			String expression = EMFUtils.getAnnotation(eClass, CONSTRAINT_SOURCE, name);
			if (expression != null && expression.length() > 0) {
				Object result = evaluator.getValue(target, expression, false, null);
				if (result instanceof Boolean && !((Boolean)result).booleanValue()) {
					String message = EMFUtils.getAnnotation(eClass, MESSAGE_SOURCE, name);
					int severityCode = IStatus.ERROR;
					if (message == null || message.length() < 1) {
						message = "{0} must satisfy " + expression;
					} else {
						int commaPos = message.indexOf(',');
						if (commaPos > 0) {
							String severity = message.substring(0, commaPos);
							severityCode = getSeverityCode(severity);
							if (severityCode != IStatus.OK) {
								message = message.substring(commaPos + 1);
							} else {
								severityCode = IStatus.ERROR;
							}
						}
					}
					// Record the target EObject and involved structural features in the status object for use in error
					// decorations and explanations
					Collection<EObject> locus = EMFUtils.getConstraintStatusLocus(target, eClass, FormulaInfo.getVariableNames(expression));
					failures.add(ConstraintStatus.createStatus(ctx, locus, severityCode, 1, message, targetName(target)));
				}
			}
		}
	}
				
	private List<String> getAffectedConstraints(EClass eClass, EStructuralFeature feature) {
		Map<EClass, List<String>> constraintsByClass = affectedConstraints.get(feature);
		if (constraintsByClass == null) {
			constraintsByClass = new HashMap<EClass, List<String>>();
			affectedConstraints.put(feature, constraintsByClass);
		}
		List<String> constraintNames = constraintsByClass.get(eClass);
		if (constraintNames == null) {
			// class-level constraints have not yet been analyzed for this feature
			constraintNames = getAllConstraints(eClass, feature);
			constraintsByClass.put(eClass, constraintNames);
		}
		return constraintNames;
	}
	
	private List<String> getAllConstraints(EClass eClass) {
		List<String> constraintNames = classConstraints.get(eClass);
		if (constraintNames == null) {
			constraintNames = getAllConstraints(eClass, null);
			classConstraints.put(eClass, constraintNames);
		}
		return constraintNames;
	}
	
	private List<String> getAllConstraints(EClass eClass, EStructuralFeature feature) {
		Set<String> results = null;
		EAnnotation constraintAnnotation = eClass.getEAnnotation(CONSTRAINT_SOURCE);
		if (constraintAnnotation != null) {
			if (results == null) {
				results = new HashSet<String>();
			}
			results.addAll(getAnnotationConstraints(constraintAnnotation, feature));
		}
		for (EClass superType : eClass.getEAllSuperTypes()) {
			EAnnotation superAnnotation = superType.getEAnnotation(CONSTRAINT_SOURCE);
			if (superAnnotation != null) {
				if (results == null) {
					results = new HashSet<String>();
				}
				results.addAll(getAnnotationConstraints(superAnnotation, feature));
			}
		}
		if (results == null) {
			return Collections.emptyList();
		}
		return new ArrayList<String>(results);
	}
	
	private Collection<String> getAnnotationConstraints(EAnnotation constraintAnnotation, EStructuralFeature feature) {
		if (feature == null) {
			return constraintAnnotation.getDetails().keySet();
		}
		List<String> constraintNames = new ArrayList<String>();
		String featureName = feature.getName();
		for (Map.Entry<String,String> entry : constraintAnnotation.getDetails()) {
			String name = entry.getKey();
			String expression = entry.getValue();
			if (expression != null && expression.length() > 0 &&
					FormulaInfo.getVariableNames(expression).contains(featureName)) {
				constraintNames.add(name);
			}
		}
		return constraintNames;
	}
	
	private List<EAnnotation> getFeatureConstraints(EClass eClass) {
		List<EAnnotation> annotations = featureConstraints.get(eClass);
	    if (annotations == null) {
	    	annotations = new ArrayList<EAnnotation>(3);
	    	for (EStructuralFeature feature : eClass.getEAllStructuralFeatures()) {
				EAnnotation annotation = feature.getEAnnotation(CONSTRAINT_SOURCE);
				if (annotation != null) {
					annotations.add(annotation);
				}
	    	}
	    	featureConstraints.put(eClass, annotations);
		}
	    return annotations;
	}
	
	private String lookupConstraintInfo(EClass eClass, String name, String source) {
		String body = EMFUtils.getAnnotation(eClass, source, name);
		if (body != null) {
			return body;
		}
		for (EClass superType : eClass.getEAllSuperTypes()) {
			body = EMFUtils.getAnnotation(superType, source, name);
			if (body != null) {
				return body;
			}
		}
		return null;
	}
	
}
