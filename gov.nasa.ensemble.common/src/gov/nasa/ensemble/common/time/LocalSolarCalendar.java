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
package gov.nasa.ensemble.common.time;

import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.core.runtime.Platform;

public class LocalSolarCalendar extends Calendar implements MissionExtendable {
	
	
	private static LocalSolarCalendar instance = MissionExtender.constructSafely(LocalSolarCalendar.class);
	
	/*The code below does not use synchronization and ensures that the Singleton object 
	 * is not created until a call is made to the static getInstance() method. This is a 
	 * good alternative if we want to avoid the overhead of synchronization.
	*/
	public static LocalSolarCalendar getInstance() {
		if (!Platform.isRunning()) {
			throw new IllegalStateException("platform must be running");
		}
		return instance;
	}

	public LocalSolarCalendar() {
		super();
	}

	public LocalSolarCalendar(TimeZone arg0, Locale arg1) {
		super(arg0, arg1);
	}

	@Override
	public void add(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void computeFields() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void computeTime() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getGreatestMinimum(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLeastMaximum(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaximum(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinimum(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void roll(int arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
}
