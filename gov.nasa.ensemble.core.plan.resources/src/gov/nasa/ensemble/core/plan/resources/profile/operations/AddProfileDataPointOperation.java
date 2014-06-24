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
package gov.nasa.ensemble.core.plan.resources.profile.operations;

import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.emf.common.util.EList;

public class AddProfileDataPointOperation extends AbstractEnsembleUndoableOperation {

	private Profile profile;
	private DataPoint dataPoint;

	public AddProfileDataPointOperation(Profile profile, DataPoint dp) {
		super("add data point");
		this.profile = profile;
		this.dataPoint = dp;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void execute() throws Throwable {
		final EList<DataPoint> dataPoints = profile.getDataPoints();
		TransactionUtils.writing(dataPoints, new Runnable() {
			@Override
			public void run() {
				ProfileUtil.insertNewDataPoint(dataPoint, dataPoints);
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void undo() throws Throwable {
		final EList<DataPoint> dataPoints = profile.getDataPoints();
		TransactionUtils.writing(dataPoints, new Runnable() {
			@Override
			public void run() {
				dataPoints.remove(dataPoint);
			}
		});
	}

	@Override
	public String toString() {
		return "add profile data point "+ this.dataPoint;
	}
	

	@Override
	protected void dispose(UndoableState state) {
		this.profile = null;
		this.dataPoint = null;
	}

}
