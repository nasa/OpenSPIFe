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
package gov.nasa.ensemble.core.plan.resources.ui.profile.provider;

import gov.nasa.ensemble.core.detail.emf.DetailProvider;
import gov.nasa.ensemble.core.detail.emf.DetailProviderParameter;
import gov.nasa.ensemble.core.detail.emf.util.EMFDetailUtils;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.plan.resources.ui.profile.editor.ProfileDataPointDetailsFactory;

import org.eclipse.emf.ecore.EObject;

public class ProfileDetailProvider extends DetailProvider {
	
	private static final ProfileDataPointDetailsFactory PROFILE_EDITOR = new ProfileDataPointDetailsFactory();
	
	@Override
	public boolean canCreateBindings(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		if (target instanceof Profile) {
			Object feature = parameter.getPropertyDescriptor().getFeature(target);
			if (feature == JSciencePackage.Literals.PROFILE__EXTERNAL_CONDITION
				|| 	feature == JSciencePackage.Literals.PROFILE__DATA_POINTS) {
				return true;
			}
		}
		return super.canCreateBindings(parameter);
	}
	
	@Override
	public void createBinding(DetailProviderParameter parameter) {
		EObject target = parameter.getTarget();
		Object feature = parameter.getPropertyDescriptor().getFeature(target);
		if (feature == JSciencePackage.Literals.PROFILE__EXTERNAL_CONDITION) {
			EMFDetailUtils.TEXT_BINDING_FACTORY.createBinding(parameter);
		} else if (feature == JSciencePackage.Literals.PROFILE__DATA_POINTS) {
			PROFILE_EDITOR.createBinding(parameter);
		}
	}
}
