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
package gov.nasa.ensemble.core.plan.editor.constraints;

import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class EarliestHandler extends TimeOfDayConstraintHandler {

	public EarliestHandler() {
		super();
	}
	
	@Override
	protected String getStartOrEndName() {
		return "start";
	}

	@Override
	protected Timepoint getStartOrEndObject() {
		return Timepoint.START;
	}

	@Override
	protected String getEarliestOrLatestName() {
		return "Earliest";
	}

	@Override
	protected String getEarlierOrLaterName() {
		return "Earlier";
	}

	@Override
	protected Amount<Duration> getRelevantPartOfConstraint(PeriodicTemporalConstraint constraint) {
		return constraint.getEarliest();
	}

	@Override
	protected void setRelevantPartOfConstraint(PeriodicTemporalConstraint constraint, Amount<Duration> offset) {
		constraint.setEarliest(offset);
	}

	@Override
	protected boolean isRelevantConstraint(PeriodicTemporalConstraint constraint) {
		return constraint.getEarliest() != null && constraint.getLatest() == null;
	}

	@Override
	public String getCommandId() {
		return EARLIEST_COMMAND_ID;
	}


}
