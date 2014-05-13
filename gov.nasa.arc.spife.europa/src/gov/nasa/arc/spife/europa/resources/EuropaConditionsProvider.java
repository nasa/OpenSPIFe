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
package gov.nasa.arc.spife.europa.resources;

import gov.nasa.arc.spife.europa.Europa;
import gov.nasa.arc.spife.europa.EuropaMember;
import gov.nasa.arc.spife.europa.EuropaPlugin;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.resources.ConditionsProvider;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;

import java.util.Date;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class EuropaConditionsProvider extends ConditionsProvider {

	public EuropaConditionsProvider() {
		super();
	}
	
	@Override
	public IStatus getStatus(EPlan plan) {
		if (EuropaMember.get(plan).getEuropa() == null) {
			return new Status(IStatus.ERROR, EuropaPlugin.ID, "No active Europa connection for the current plan");
		}
		return super.getStatus(plan);
	}

	@Override
	public Conditions getConditions(EPlan plan, Date date) {
		Europa europa = EuropaMember.get(plan).getEuropa();
		if (europa == null) {
			LogUtil.warn("europa not available, conditions not being exported");
			return null;
		}
		return europa.getFinalConditions(date);
	}

}
