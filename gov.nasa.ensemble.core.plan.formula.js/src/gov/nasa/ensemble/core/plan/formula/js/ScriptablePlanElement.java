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
package gov.nasa.ensemble.core.plan.formula.js;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class ScriptablePlanElement extends ScriptableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final EAttribute START_TIME_ATTRIBUTE = TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME;
	private static final String START_TIME_NAME = WrapperUtils.mapStructuralFeatureToParameterName(START_TIME_ATTRIBUTE);
	private static final EAttribute DURATION_ATTRIBUTE = TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION;
	private static final String DURATION_NAME = WrapperUtils.mapStructuralFeatureToParameterName(DURATION_ATTRIBUTE);

	private EPlanElement planElement;

	public ScriptablePlanElement(EPlanElement planElement) {
		this.planElement = planElement;
	}

	public EPlanElement getPlanElement() {
		return planElement;
	}

	@Override
	public String getClassName() {
		return null;
	}

	@Override
	public Object get(String name, Scriptable start) {
		EObject object;
		EStructuralFeature feature;
		if (name.equals(START_TIME_NAME)) { // temporary hack here
			object = planElement.getMember(TemporalMember.class);
			feature = START_TIME_ATTRIBUTE;
		} else if (name.equals(DURATION_NAME)) { // temporary hack here
			object = planElement.getMember(TemporalMember.class);
			feature = DURATION_ATTRIBUTE;
		} else {
			object = planElement.getData();
			if (object == null) {
				object = planElement;
			}
			EClass eClass = object.eClass();
			feature = eClass.getEStructuralFeature(name);
			if (feature == null) {
				return super.get(name, start);
			}
		}
		Object value = JSUtils.normalize(object, feature);
		return JSUtils.unwrap(value, start);
	}

}
