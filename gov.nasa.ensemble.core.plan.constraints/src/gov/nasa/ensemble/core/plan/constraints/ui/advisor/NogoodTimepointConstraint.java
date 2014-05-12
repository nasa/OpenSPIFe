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
package gov.nasa.ensemble.core.plan.constraints.ui.advisor;

import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;

public class NogoodTimepointConstraint implements INogoodPart {

	private final PeriodicTemporalConstraint constraint;
	
	public NogoodTimepointConstraint(PeriodicTemporalConstraint constraint) {
		this.constraint = constraint;
	}
	
	public PeriodicTemporalConstraint getConstraint() {
		return constraint;
	}

	@Override
	public int hashCode() {
		if (constraint == null) {
			return 0;
		}
		return constraint.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NogoodTimepointConstraint) {
			NogoodTimepointConstraint nogoodTimepointDistanceConstraint = (NogoodTimepointConstraint) obj;
			return (constraint == nogoodTimepointDistanceConstraint.constraint);
		}
		return false;
	}

}
