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
package gov.nasa.ensemble.core.plan.advisor.fixing;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;

import gov.nasa.ensemble.common.operation.AbstractEnsembleDoableOperation;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.advisor.markers.MarkerViolation;

public class MarkerResolutionOperation extends AbstractEnsembleDoableOperation {
	
	private final MarkerViolation violation;
	private final IMarkerResolution resolution;
	private final EPlan plan;

	public MarkerResolutionOperation(MarkerViolation violation, IMarkerResolution resolution, EPlan plan) {
		super(resolution.getLabel());
		this.violation = violation;
		this.resolution = resolution;
		this.plan = plan;
	}

	@Override
	protected void execute() throws Throwable {
		IMarker marker = violation.getMarker();
		if (resolution instanceof IPlanMarkerResolution) {
			((IPlanMarkerResolution)resolution).run(marker, plan);
		} else {
			resolution.run(marker);
		}
	}

	@Override
	public String toString() {
		return resolution.getLabel() + " for " + violation.getDescription();
	}

}
