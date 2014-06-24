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
package gov.nasa.ensemble.core.jscience.csvxml;

import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.DataPoint;
import gov.nasa.ensemble.core.jscience.impl.ProfileImpl;

import java.util.Collection;

import org.eclipse.emf.common.util.EList;

/**
 * A Profile that loads its datapoints only on demand.
 * 
 * Call getDataPointsLazily() if caller deals with ProfileLoadingException
 * --  this also can act like any other Profile, except that in case of
 * an error parsing the CSV, a runtime error may be thrown by getDataPoints().
 *
 * @param <T> -- see DataPoint
 */
public class ProfileWithLazyDatapointsFromCsv<T> extends ProfileImpl<T> {
	
	private ProfileLoader loader;
	private boolean currentlySetting = false;
		
	public ProfileWithLazyDatapointsFromCsv(ProfileLoader loader) {
		super();
		this.loader = loader;
	}
	
	@Override
	public AmountExtent<?> getExtent() {
		if (!currentlySetting) { // because setDataPoints calls getDataPoints to make an empty list.
			try {
				loader.ensureDataLoaded();
			} catch (ProfileLoadingException e) {
				throw new RuntimeException(e);
			}
		}
		return super.getExtent();
	}

	public EList<DataPoint<T>> getDataPointsLazily() throws ProfileLoadingException {
		if (!currentlySetting) { // because setDataPoints calls getDataPoints to make an empty list.
			loader.ensureDataLoaded();
		}
		return super.getDataPoints();
	}

	@Override
	public EList<DataPoint<T>> getDataPoints() {
		try {
			return getDataPointsLazily();
		} catch (ProfileLoadingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setDataPoints(Collection<DataPoint<T>> dataPoints) {
		try {
			currentlySetting = true;
			super.setDataPoints(dataPoints);
		} finally {
			currentlySetting = false;
		}
	}

}
