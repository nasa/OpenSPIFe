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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.formula.js.constraint;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.formula.FormulaEngine;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IModelConstraint;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.emf.validation.service.IParameterizedConstraintDescriptor;
import org.eclipse.emf.validation.service.IParameterizedConstraintParser;
import org.eclipse.emf.validation.xml.ConstraintParserException;

/**
 * @author rnado
 *
 */
public class JSConstraintParser implements IParameterizedConstraintParser {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.validation.service.IParameterizedConstraintParser#parseConstraint(org.eclipse.emf.validation.service.IParameterizedConstraintDescriptor)
	 */
	@Override
	public IModelConstraint parseConstraint(
			IParameterizedConstraintDescriptor descriptor)
			throws ConstraintParserException {
		String expression = descriptor.getBody();
		if (expression == null || expression.length() == 0) {
			throw(new ConstraintParserException("No JavaScript constraint expression."));
		}
		return new JSConstraint(descriptor, expression);
	}
	
	private static class JSConstraint implements IModelConstraint {
		
		private final IConstraintDescriptor descriptor;
		private final String expression;
		private final JSEvaluator evaluator = new JSEvaluator();
		private final Set<String> messages = new HashSet<String>();
		
		JSConstraint(IConstraintDescriptor descriptor, String expression) {
			this.descriptor = descriptor;
			this.expression = expression;
		}
		
		@Override
		public IConstraintDescriptor getDescriptor() {
			return descriptor;
		}

		@Override
		public IStatus validate(IValidationContext ctx) {
			EObject target = ctx.getTarget();
			Object value = null;
			if (target instanceof EPlanElement) {
				value = getValue((EPlanElement)target);
			} else {
				value = evaluator.getValue(target, expression, false, null);
			}
			if (value instanceof Boolean && !((Boolean)value).booleanValue()) {
				return ctx.createFailureStatus(target);
			} else {
				return ctx.createSuccessStatus();
			}
		}
		
		protected Object getValue(EPlanElement planElement) {
			try {
				return FormulaEngine.getInstance().getValue(planElement, expression);
			} catch (Exception e) {
				String message = planElement.getName() + ": " + expression + " " + e.getMessage();
				if (!messages.contains(message)) {
					Logger.getLogger(getClass()).error(message);
					messages.add(message);
				}
			}
			return null;
		}
		
	}

}
