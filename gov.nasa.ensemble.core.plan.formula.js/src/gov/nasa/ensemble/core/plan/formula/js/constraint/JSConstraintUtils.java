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

import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.ConstraintStatus;

public class JSConstraintUtils {
	
	public static IStatus combinedStatus(IValidationContext ctx, EObject target, List<IStatus> statusList) {
		if (statusList.size() == 1) {
			return statusList.get(0);
		} else {
			return ConstraintStatus.createMultiStatus(ctx, 
					"Multiple validation results for {0}", 
					new Object[] {targetName(target)}, 
					statusList);
		}
	}

	public static int getSeverityCode(String severity) {
		if (severity.equalsIgnoreCase("WARNING")) {
			return IStatus.WARNING;
		} else if (severity.equalsIgnoreCase("ERROR")) {
			return IStatus.ERROR;
		} else if (severity.equalsIgnoreCase("INFO")) {
			return IStatus.INFO;
		} else if (severity.equalsIgnoreCase("CANCEL")) {
			return IStatus.CANCEL;
		}
		return IStatus.OK;
	}
	
	public static String targetName(EObject target) {
		String targetName = EMFUtils.getText(target, EcoreUtil.getID(target));
		if (targetName == null) {
			String className = target.eClass().getName();
			String article = isVowel(className.charAt(0))?"an ":"a ";
			targetName = article + className;
		}
		return targetName;
	}
	
	private static boolean isVowel(char c) {
		return (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' ||
				c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' );
	}
}
