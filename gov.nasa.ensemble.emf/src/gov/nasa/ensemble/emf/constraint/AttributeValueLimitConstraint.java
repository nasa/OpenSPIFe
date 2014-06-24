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
package gov.nasa.ensemble.emf.constraint;


import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintStatus;

public class AttributeValueLimitConstraint extends AbstractModelConstraint {
	
	private static final String ANNOTATION_SOURCE_DETAIL = "detail";
	private static final String ANNOTATION_DETAIL_LIMIT = "limit";

	/**
	 * Validates an object in the given context to ensure that attribute values
	 * conform to any minimum and/or maximum limit restrictions
	 * 
	 * @param ctx the IValidationContext used for the validation
	 */
	@Override
	public IStatus validate(IValidationContext ctx) {
		EObject target = ctx.getTarget();
		EMFEventType eventType = ctx.getEventType();
		try {
			// In the case of batch mode.
			if (eventType == EMFEventType.NULL) {
				EClass eClass = target.eClass();
				List<IStatus> failureList = null;
				for (EAttribute attribute : eClass.getEAllAttributes()) {
					IStatus failure = validateAttributeLimit(ctx, target, attribute, target.eGet(attribute));
					if (failure != null) {
						if (failureList == null) {
							failureList = new ArrayList<IStatus>();
						}
						failureList.add(failure);
					}
				}
				if (failureList != null) {
					if (failureList.size() == 1) {
						return failureList.get(0);
					} else {
						return ConstraintStatus.createMultiStatus(ctx, 
								"Attribute limit violations for {0}", 
								new Object[] {target}, 
								failureList);
					}
				}
			} else {
				// In the case of live mode
				EStructuralFeature feature = ctx.getFeature();
				if (feature instanceof EAttribute) {
					IStatus status =  validateAttributeLimit(ctx, target, (EAttribute)feature, ctx.getFeatureNewValue());
					if (status != null) {
						return status;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error("Run time exception while checking the attribute value limit constraint", e);
		}
		return ctx.createSuccessStatus();
	}
	
	/**
	 * Tests the value of an attribute of a target object to determine whether the value violates any limits
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param target the EObject to be validated
	 * @param attribute the EAttribute whose value is to be validated
	 * @param value the Object value of the attribute to be validated
	 * @return IStatus a failure status object if the value violates the limits, else null
	 */
	private IStatus validateAttributeLimit(IValidationContext ctx, EObject target, EAttribute attribute, Object value) {
		if (value instanceof Comparable) {
			Comparable comparable = (Comparable)value;
			String annotation = EMFUtils.getAnnotation(attribute, ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_LIMIT);
			if (annotation != null && annotation.length() > 0) {
				int index = annotation.indexOf(",");
				if (index != -1) {
					String min = annotation.substring(0, index).trim();
					try {
						Comparable minValue = (Comparable)createFromString(attribute, min);
						String max = annotation.substring(index+1).trim();
						Comparable maxValue = (Comparable)createFromString(attribute, max);
						boolean batch = ctx.getEventType() == EMFEventType.NULL;
						if ((minValue != null) && comparable.compareTo(minValue) < 0) {
							if (batch) {
								return createBatchFailureStatus(ctx, target, attribute, comparable, minValue, maxValue);
							} else {
								return createLiveFailureStatus(ctx, attribute, comparable, minValue, maxValue);
							}
						} else if ((maxValue != null) && comparable.compareTo(maxValue) > 0) {
							if (batch) {
								return createBatchFailureStatus(ctx, target, attribute, comparable, minValue, maxValue);
							} else {
								return createLiveFailureStatus(ctx, attribute, comparable, minValue, maxValue);
							}
						}
					} catch (ClassCastException e) {
						return createWrongTypeFailureStatus(ctx, target, attribute, value);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Convert a string value to a value of the attribute's type.  Returns null if the string
	 * is empty or if there is a problem parsing the string.
	 * 
	 * @param attribute the EAttribute whose limit value should be converted
	 * @param value the String to be converted
	 * @return Object the converted value
	 */
	private Object createFromString(EAttribute attribute, String value) {
		// empty string value may cause EMF createfromString to throw an exception
		if (value.length() == 0) {
			return null;
		}
		EDataType eDataType = attribute.getEAttributeType();
		try {
			Object valueObject = EcoreUtil.createFromString(eDataType, value);
			return valueObject;
		} catch (Exception e) {
			Logger.getLogger(AttributeValueLimitConstraint.class).warn("Error parsing attribute value limit", e);
			return null;
		}
	}
	
	/**
	 * Create a constraint failure status with a message appropriate for a batch validation
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param attribute the EAttribute whose value failed the constraint
	 * @param value the value that failed the constraint
	 * @param minValue the minimum allowed value or null
	 * @param maxValue the maximum allowed value or null
	 * @return IStatus a constraint failure status
	 */
	private IStatus createBatchFailureStatus(IValidationContext ctx, EObject target, EAttribute attribute, Object value, Object minValue, Object maxValue) {
		String attrName = EMFUtils.getDisplayName(target, attribute);
		String targetName = EMFUtils.getDisplayName(target);
		Collection<EObject> locus = EMFUtils.getConstraintStatusLocus(target, attribute);
		if (minValue == null && maxValue != null) {
			return ConstraintStatus.createStatus(ctx, locus, 
					"The {0} of {1} ({2}) is not less than or equal to the maximum of {3}.",
					new Object[] {attrName, targetName, value, maxValue});
		} else if (minValue != null && maxValue == null) {
			return ConstraintStatus.createStatus(ctx, locus, 
					"The {0} of {1} ({2}) is not greater than or equal to the minimum of {3}.",
					new Object[] {attrName, targetName, value, minValue});
		} else {
			return ConstraintStatus.createStatus(ctx, locus, 
					"The {0} of {1} ({2}) is not between {3} and {4} inclusive.",
					new Object[] {attrName, targetName, value, minValue, maxValue});
		}
	}
	
	/**
	 * Create a constraint failure status with a message appropriate for a live validation
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param attribute the EAttribute whose value failed the constraint
	 * @param value the value that failed the constraint
	 * @param minValue the minimum allowed value or null
	 * @param maxValue the maximum allowed value or null
	 * @return IStatus a constraint failure status
	 */
	private IStatus createLiveFailureStatus(IValidationContext ctx, EAttribute attribute, Object value, Object minValue, Object maxValue) {
		if (minValue == null && maxValue != null) {
			return ConstraintStatus.createStatus(ctx, null, 
					"The value must be less than or equal to {0}.",
					new Object[] {maxValue});
		} else if (minValue != null && maxValue == null) {
			return ConstraintStatus.createStatus(ctx, null, 
					"The value must be greater than or equal to {0}.",
					new Object[] {minValue});
		} else {
			return ConstraintStatus.createStatus(ctx, null, 
					"The value must be between {0} and {1} inclusive.",
					new Object[] {minValue, maxValue});
		}
	}
	
	private IStatus createWrongTypeFailureStatus(IValidationContext ctx, EObject target, EAttribute attribute, Object value) {
		if (ctx.getEventType() == EMFEventType.NULL) {
			String attrName = EMFUtils.getDisplayName(target, attribute);
			String targetName = EMFUtils.getDisplayName(target);
			Collection<EObject> locus = EMFUtils.getConstraintStatusLocus(target, attribute);
			return ConstraintStatus.createStatus(ctx, locus, 
					"The {0} of {1} ({2}) has a type inconsistent with the attribute limits.",
					new Object[] {attrName, targetName, value});
		} else {
			return ConstraintStatus.createStatus(ctx, null, 
					"The value has a type inconsistent with the attribute limits.",
					new Object[] {});
		}
	}

}
