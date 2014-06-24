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
package gov.nasa.ensemble.core.model.plan.provider;

import gov.nasa.ensemble.emf.util.EMFUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanAdapterFactory;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.jface.viewers.ILabelProvider;

public class EPlanElementLabelProviderAdapterFactory extends PlanAdapterFactory {

	@Override
	public boolean isFactoryForType(Object type) {
		if (ILabelProvider.class == type) {
			return true;
		}
		return super.isFactoryForType(type);
	}

	@Override
	public Adapter adapt(Notifier target, Object type) {
		if (target instanceof EPlanElement && ILabelProvider.class == type) {
			AdapterFactory factory = EMFUtils.getAdapterFactory(target);
			return new EPlanElementLabelProvider(factory);
		}
		return super.adapt(target, type);
	}

}
