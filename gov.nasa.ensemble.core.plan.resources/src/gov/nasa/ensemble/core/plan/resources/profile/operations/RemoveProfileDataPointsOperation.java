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

import java.util.Arrays;
import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import org.eclipse.emf.common.util.EList;

public class RemoveProfileDataPointsOperation extends AbstractEnsembleUndoableOperation {

	private Profile profile;
	private DataPoint[] dataPoints;
	private int[] indexes;

	public RemoveProfileDataPointsOperation(Profile profile, DataPoint[] toDelete) {
		super("remove profile data points");
		this.profile = profile;
		this.dataPoints = toDelete;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void execute() throws Throwable {
		int count = dataPoints.length;
		this.indexes = new int[count];
		for (int i = 0; i < count; i++) {
			indexes[i] = profile.getDataPoints().indexOf(dataPoints[i]);
		}
		final EList<DataPoint> profileDataPoints = profile.getDataPoints();
		TransactionUtils.writing(profileDataPoints, new Runnable() {
			@Override
			public void run() {
				profileDataPoints.removeAll(Arrays.asList(dataPoints));
			}
		});
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void undo() throws Throwable {
		final EList<DataPoint> profileDataPoints = profile.getDataPoints();
		TransactionUtils.writing(profileDataPoints, new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < indexes.length; i++) {
					profileDataPoints.add(indexes[i], dataPoints[i]);
				}
			}
		});
	}

	@Override
	public String toString() {
		return null;
	}
	

	@Override
	protected void dispose(UndoableState state) {
		this.profile = null;
		this.dataPoints = null;
	}
	
}
