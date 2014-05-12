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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.EMFEventType;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintStatus;

public class AttributeValueLengthConstraint extends AbstractModelConstraint {
	
	public static final String ANNOTATION_SOURCE_DETAIL = "detail";
	public static final String ANNOTATION_DETAIL_CHAR_LENGTH = "charlen";
	private static final int MAXIMUM_DISPLAY_LENGTH = 50;

	/**
	 * Validates an object in the given context to ensure that attribute value
	 * strings do not exceed a maximum number of characters
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
					IStatus failure = validateLength(ctx, target, attribute, target.eGet(attribute));
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
								"Attribute value string length violations for {0}", 
								new Object[] {target}, 
								failureList);
					}
				}
			} else {
				// In the case of live mode
				EStructuralFeature feature = ctx.getFeature();
				if (feature instanceof EAttribute) {
					IStatus status =  validateLength(ctx, target, (EAttribute)feature, ctx.getFeatureNewValue());
					if (status != null) {
						return status;
					}
				}
			}
		} catch (Exception e) {
			LogUtil.error("Run time exception while checking the attribute value length constraint", e);
		}
		return ctx.createSuccessStatus();
	}
	
	/**
	 * Tests the value of an attribute of a target object to determine whether the value exceeds a
	 * character length restriction
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param target the EObject to be validated
	 * @param attribute the EAttribute whose value is to be validated
	 * @param value the Object value of the attribute to be validated
	 * @return IStatus a failure status object if the value violates the length restriction, else null
	 */
	private IStatus validateLength(IValidationContext ctx, EObject target, EAttribute attribute, Object value) {
		if (value instanceof String) {
			String string = (String)value;
			String annotation = EMFUtils.getAnnotation(attribute, ANNOTATION_SOURCE_DETAIL, ANNOTATION_DETAIL_CHAR_LENGTH);
			if (annotation != null && annotation.length() > 0) {
				try {
					int charlength = Integer.parseInt(annotation);
					boolean batch = ctx.getEventType() == EMFEventType.NULL;
					if (string.length() > charlength) {
						if (batch) {
							return createBatchFailureStatus(ctx, target, attribute, string, charlength);
						} else {
							return createLiveFailureStatus(ctx, attribute, string, charlength);
						}
					}
				} catch(NumberFormatException e) {
					LogUtil.warn(attribute.getName() + " has an invalid charlen annotation.");
				}
			}
		}
		return null;
	}
	
	/**
	 * Create a constraint failure status with a message appropriate for a batch validation
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param attribute the EAttribute whose value failed the constraint
	 * @param value the value that failed the constraint
	 * @param charlen the maximum number of characters
	 * @return IStatus a constraint failure status
	 */
	private IStatus createBatchFailureStatus(IValidationContext ctx, EObject target, EAttribute attribute, String value, int charlen) {
		String attrName = EMFUtils.getDisplayName(target, attribute);
		String targetName = EMFUtils.getDisplayName(target);
		if (value.length() > MAXIMUM_DISPLAY_LENGTH) {
			value = value.substring(0, MAXIMUM_DISPLAY_LENGTH) + "...";
		}
		return ConstraintStatus.createStatus(ctx, EMFUtils.getConstraintStatusLocus(target, attribute), 
				"The {0} of {1} ({2}) exceeds {3} characters.",
				new Object[] {attrName, targetName, value, charlen});
	}
	
	public static Collection<EObject> getLocus(EObject target, EStructuralFeature feature) {
		Collection<EObject> locus = new ArrayList<EObject>();
		locus.add(target);
		locus.add(feature);
		return locus;
	}
	
	/**
	 * Create a constraint failure status with a message appropriate for a live validation
	 * 
	 * @param ctx the IValidationContext used for the validation
	 * @param attribute the EAttribute whose value failed the constraint
	 * @param value the value that failed the constraint
	 * @param charlen the maximum number of characters
	 * @return IStatus a constraint failure status
	 */
	private IStatus createLiveFailureStatus(IValidationContext ctx, EAttribute attribute, String value, int charlen) {
			return ConstraintStatus.createStatus(ctx, null, 
					"The value must be a maximum of {0} characters.",
					new Object[] {charlen});
	}
	
}
