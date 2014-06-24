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
package gov.nasa.ensemble.core.plan.resources.profile;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.jscience.INTERPOLATION;
import gov.nasa.ensemble.core.jscience.JScienceFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.dictionary.ENumericResourceDef;
import gov.nasa.ensemble.dictionary.EResourceDef;
import gov.nasa.ensemble.dictionary.EStateResourceDef;

import org.apache.log4j.Logger;

public class ResourceProfileFactory implements MissionExtendable {

	private static final ParameterDescriptor PARAMETER_DESCRIPTOR = ParameterDescriptor.getInstance();

	private static final Logger trace = Logger.getLogger(ResourceProfileFactory.class);

	/**
	 * Singleton pattern to allow the application the ability to create plan
	 * members independently.
	 */
	private static ResourceProfileFactory instance = null;

	public static final ResourceProfileFactory getInstance() {
		if (instance == null) {
			try {
				instance = MissionExtender.construct(ResourceProfileFactory.class);
			} catch (ConstructionException e) {
				trace.error(e.getMessage(), e);
				instance = new ResourceProfileFactory();
			}
		}
		return instance;
	}

	/**
	 * Create a resource profile for the given plan.
	 */
	public Profile createResourceProfile(EResourceDef definition) {
		Profile<Object> profile = JScienceFactory.eINSTANCE.createProfile();
		profile.setId(definition.getName());
		String displayName = PARAMETER_DESCRIPTOR.getDisplayName(definition);
		profile.setName(displayName);
		profile.setDataType(definition.getEAttributeType());
		profile.setCategory(definition.getCategory());
		if (definition instanceof ENumericResourceDef) {
			profile.setUnits(((ENumericResourceDef) definition).getUnits());
		}
		if (definition instanceof EStateResourceDef) {
			profile.setInterpolation(INTERPOLATION.STEP);
		}
		return profile;
	}

}
