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
package gov.nasa.ensemble.common.ui.info;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;

/** Get the name of the user, for printing in reports and the like.
 * This may be a full name rather than a computer username.
 * The default implementation is to get the logged-in user's username,
 * but products may extend this to get it from Preferences or a login dialog
 * and may opt to use directory services to look up a full name. */
public class UserExtendedNameProvider implements MissionExtendable {
	
	private static UserExtendedNameProvider instance;

	public static UserExtendedNameProvider getInstance() {
		if (instance == null) {
			try {
				instance = MissionExtender.construct(UserExtendedNameProvider.class);
			} catch (ConstructionException e) {
				LogUtil.error("constructing UserNameProvider");
				instance = new UserExtendedNameProvider();
			}
		}
		return instance;
	}
	
	/** Get the name of the user, for printing in reports and the like.
	 * This may be a full name rather than a computer username.
	 * The default implementation is to get the logged-in user's username,
	 * but products may extend this to get it from Preferences or a login dialog
	 * and may opt to use directory services to look up a full name.
	 * This method may return null. */
	protected String getUserNameForReports() {
		return System.getProperty("user.name", null);
	}
	
	/** Get the name of the user, for printing in reports and the like.
	 * This may be a full name rather than a computer username.
	 * The default implementation is to get the logged-in user's username,
	 * but products may extend this to get it from Preferences or a login dialog
	 * and may opt to use directory services to look up a full name.
	 * For convenience, this lets the caller specify a default value and will not return null. */
	public final String getUserNameForReports(String defaultValue) {
		String result = getUserNameForReports();
		if (result==null) return defaultValue;
		return result;
	}

}
