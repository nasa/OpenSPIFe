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
package gov.nasa.ensemble.core.model.plan.advisor.provider.detail;

import gov.nasa.ensemble.core.detail.emf.IBindingFactory;
import gov.nasa.ensemble.core.model.plan.advisor.AdvisorPackage;
import gov.nasa.ensemble.core.model.plan.advisor.util.AdvisorAdapterFactory;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EDataType;

public class RuleSetDetailBindingAdapterFactory extends AdvisorAdapterFactory {

	@Override
	public Adapter adapt(Notifier target, Object type) {
		if (AdvisorPackage.Literals.WAIVER_PROPERTIES_ENTRY == target) {
			return new RuleSetBindingFactory();
		}
		return super.adapt(target, type);
	}

	@Override
	public boolean isFactoryForType(Object object) {
		if (object instanceof EDataType) {
			return AdvisorPackage.Literals.WAIVER_PROPERTIES_ENTRY == object;
		}
		return IBindingFactory.class == object
				|| super.isFactoryForType(object);
	}

}
