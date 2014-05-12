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
package gov.nasa.ensemble.core.plan.temporal.modification;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

public class TemporalExtentsCache {

	private final Map<EPlanElement, TemporalExtent> planElementToExtent = new HashMap<EPlanElement, TemporalExtent>();
	
	public TemporalExtentsCache() {
		// default constructor
	}
	
	public TemporalExtentsCache(EPlanElement plan) {
		cache(plan);
	}
	
	public TemporalExtentsCache(TemporalExtentsCache initialState) {
		planElementToExtent.putAll(initialState.planElementToExtent);
	}

	public void cache(EPlanElement pe) {
		planElementToExtent.put(pe, pe.getMember(TemporalMember.class).getExtent());
		for (EPlanElement child : EPlanUtils.getChildren(pe)) {
			cache(child);
		}
	}
	
	public TemporalExtent get(EPlanElement pe) {
		return planElementToExtent.get(pe);
	}
	
	public Date getStart(EPlanElement element) {
		TemporalExtent initialExtent = get(element);
		if (initialExtent != null) {
			return initialExtent.getStart();
		}
		return element.getMember(TemporalMember.class).getStartTime();
	}

	public Amount<Duration> getDuration(EPlanElement element) {
		TemporalExtent initialExtent = get(element);
		if (initialExtent != null) {
			return initialExtent.getDuration();
		}
		return element.getMember(TemporalMember.class).getDuration();
	}

	public Date getEnd(EPlanElement element) {
		TemporalExtent initialExtent = get(element);
		if (initialExtent != null) {
			return initialExtent.getEnd();
		}
		return element.getMember(TemporalMember.class).getEndTime();
	}

	protected void set(EPlanElement element, TemporalExtent extent) {
		planElementToExtent.put(element, extent);
	}
	
	public void clear() {
		planElementToExtent.clear();
	}
	
	@Override
	public TemporalExtentsCache clone() {
		return new TemporalExtentsCache(this);
	}

}
