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

import gov.nasa.ensemble.core.detail.emf.IDetailProvider;
import gov.nasa.ensemble.core.jscience.util.JScienceAdapterFactory;
import gov.nasa.ensemble.core.plan.resources.profile.ProfilePackage;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

public class ProfileDetailProviderAdapterFactory extends JScienceAdapterFactory {

	@Override
	public boolean isFactoryForType(Object object) {
		if (IDetailProvider.class == object) {
			return true;
		}
		// handle StructuralFeatureProfiles from the ProfilePackage
		if (object == ProfilePackage.eINSTANCE) {
			return true;
		}
		return super.isFactoryForType(object);
	}

	@Override
	public Adapter adapt(Notifier target, Object type) {
		if (IDetailProvider.class == type) {
			return new ProfileDetailProvider();
		}
		return super.adapt(target, type);
	}
}
