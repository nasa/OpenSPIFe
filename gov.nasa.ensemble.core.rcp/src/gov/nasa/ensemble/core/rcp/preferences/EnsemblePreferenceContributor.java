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
package gov.nasa.ensemble.core.rcp.preferences;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

public class EnsemblePreferenceContributor implements MissionExtendable {
	
	private static EnsemblePreferenceContributor instance;
	protected static final Logger trace = Logger.getLogger(EnsemblePreferenceContributor.class);
	
	protected EnsemblePreferenceContributor() {
		// construct from subclass only
	}
	
	/**
	 * Implement this method to initialize the ensemble rcp preference store
	 * according to your mission specific values.  Initialize only the
	 * preference store provided.  Do not attempt to initialize other
	 * preference stores or perform other initialization.
	 * 
	 * @param rcpPreferenceStore
	 */
	public void initializeRcpDefaultPreferences(IPreferenceStore rcpPreferenceStore) {
		// no default implementation
	}
	
	public static EnsemblePreferenceContributor getInstance() {
		if (instance == null) {
			try {
				instance = MissionExtender.construct(EnsemblePreferenceContributor.class);
			} catch (ConstructionException e) {
				trace.error("Could not construct instance of EnsemblePreferenceContributor.", e);
			}
		}
		return instance;
	}
	
}
