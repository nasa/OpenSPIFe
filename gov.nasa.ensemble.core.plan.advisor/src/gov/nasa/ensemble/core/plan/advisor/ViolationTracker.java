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
package gov.nasa.ensemble.core.plan.advisor;

import gov.nasa.ensemble.common.time.DateUtils;

import java.util.Date;

public class ViolationTracker implements Comparable<ViolationTracker> {

	private final Date birthday = new Date();
	private Violation violation;
	
	public ViolationTracker(Violation violation) {
		this.violation = violation;
	}

	public Violation getViolation() {
		return violation;
	}

	public void setViolation(Violation violation) {
		this.violation = violation;
	}
	
	@Override
	public int compareTo(ViolationTracker o2) {
		long diff = DateUtils.subtract(o2.birthday, this.birthday);
		return (diff == 0 ? 0 : (diff > 0 ? 1 : -1));
	}
	
}
