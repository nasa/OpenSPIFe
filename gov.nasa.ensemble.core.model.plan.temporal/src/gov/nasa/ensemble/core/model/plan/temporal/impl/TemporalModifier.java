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
package gov.nasa.ensemble.core.model.plan.temporal.impl;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.translator.transactions.FeatureTransactionChangeOperation;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.ecore.EStructuralFeature;

public class TemporalModifier implements MissionExtendable {

	private static TemporalModifier instance = null;
	
	public static final TemporalModifier getInstance() {
		if (instance == null) {
			instance = MissionExtender.constructSafely(TemporalModifier.class);
			if (instance == null) {
				instance = new TemporalModifier();
			}
		}
		return instance;
	}
	
	public IUndoableOperation set(TemporalMember owner, EStructuralFeature feature, Object value) {
		String displayName = EMFUtils.getDisplayName(owner, feature);
		String label = "Edit " + displayName;
		return new FeatureTransactionChangeOperation(label, owner, feature, value);
	}

}
