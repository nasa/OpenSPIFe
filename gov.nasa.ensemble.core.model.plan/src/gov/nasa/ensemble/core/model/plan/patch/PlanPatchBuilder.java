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
package gov.nasa.ensemble.core.model.plan.patch;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EMember;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.emf.model.patch.PatchBuilder;
import gov.nasa.ensemble.emf.model.patch.PatchFeatureChange;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class PlanPatchBuilder extends PatchBuilder {

	@Override
	protected EObject copy(EObject eObject) {
		if (eObject instanceof EPlanElement) {
			return EPlanUtils.copy(eObject, false);
		}
		return EcoreUtil.copy(eObject);
	}
	
	@Override
	public void generateDisplayMessage(EObject target, PatchFeatureChange featureChange) {
		if (target instanceof EMember) {
			EPlanElement planElement = ((EMember) target).getPlanElement();
			super.generateDisplayMessage(planElement, featureChange);
		} else if (target instanceof DynamicEObjectImpl) {
			if (target.eContainer() instanceof EPlanElement) {
				super.generateDisplayMessage(target.eContainer(), featureChange);
			}
		} else {
			super.generateDisplayMessage(target, featureChange);
		}
	}
}
