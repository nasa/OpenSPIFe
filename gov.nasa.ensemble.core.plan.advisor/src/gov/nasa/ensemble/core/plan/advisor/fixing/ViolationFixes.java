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
/**
 * 
 */
package gov.nasa.ensemble.core.plan.advisor.fixing;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ViolationFixes {
	final PlanAdvisor advisor;
	final List<SuggestedStartTime> startTimes;
	final List<EPlanElement> unsatisfiedNodes;
	final List<EPlanElement> opposingNodes;
	
	public ViolationFixes(PlanAdvisor advisor,
            List<SuggestedStartTime> startTimes,
            List<EPlanElement> unsatisfiedNodes,
			List<EPlanElement> opposingNodes) {
		this.advisor = advisor;
		this.startTimes = Collections.unmodifiableList(startTimes);
		this.unsatisfiedNodes = Collections.unmodifiableList(unsatisfiedNodes);
		this.opposingNodes = Collections.unmodifiableList(opposingNodes);
	}
	
	public PlanAdvisor getAdvisor() {
		return advisor;
	}
	
	public List<SuggestedStartTime> getStartTimes() {
		return startTimes;
	}
	
	public List<EPlanElement> getUnsatisfiedNodes() {
		return unsatisfiedNodes;
	}
	
	public List<EPlanElement> getOpposingNodes() {
		return opposingNodes;
	}

	public static ViolationFixes combineFixes(List<ViolationFixes> fixes) {
		if (fixes.isEmpty()) {
			return null;
		}
		for (ViolationFixes fix : fixes) {
			if (fix.getAdvisor().getName().contains("Europa")) {
				return fix;
			}
		}
		if (fixes.size() == 1) {
			return fixes.iterator().next();
		}
		List<EPlanElement> combinedUnsatisfiedNodes = new ArrayList<EPlanElement>();
		List<EPlanElement> combinedOpposingNodes = new ArrayList<EPlanElement>();
		Map<EPlanElement, List<SuggestedStartTime>> allStartTimes = new HashMap<EPlanElement, List<SuggestedStartTime>>();
		for (ViolationFixes fix : fixes) {
			for (SuggestedStartTime startTime : fix.getStartTimes()) {
				List<SuggestedStartTime> list = allStartTimes.get(startTime.node);
				if (list == null) {
					list = new ArrayList<SuggestedStartTime>();
					allStartTimes.put(startTime.node, list);
				}
				list.add(startTime);
			}
			combinedUnsatisfiedNodes.addAll(fix.getUnsatisfiedNodes());
			combinedOpposingNodes.addAll(fix.getOpposingNodes());
		}
		List<SuggestedStartTime> combinedStartTimes = new ArrayList<SuggestedStartTime>();
		for (Map.Entry<EPlanElement, List<SuggestedStartTime>> entry : allStartTimes.entrySet()) {
			EPlanElement element = entry.getKey();
			List<SuggestedStartTime> list = entry.getValue();
			int count = list.size();
			if (count == 1) {
				combinedStartTimes.add(list.iterator().next());
				continue;
			}
			long idealSum = 0;
			Date earliest = new Date(0);
			Date latest = new Date(Long.MAX_VALUE);
			for (SuggestedStartTime startTime : list) {
				idealSum += startTime.ideal.getTime();
				earliest = DateUtils.latest(earliest, startTime.earliest);
				latest = DateUtils.earliest(latest, startTime.latest);
			}
			Date ideal = new Date(idealSum/count);
			if (earliest.after(latest)) {
				Logger.getLogger(ViolationFixes.class).warn("advisor suggestions were incompatible for " + element.getName() + ", collapsing to ideal: " + ideal);
				earliest = ideal;
				latest = ideal;
			} else {
				ideal = DateUtils.bind(ideal, earliest, latest);
			}
			combinedStartTimes.add(new SuggestedStartTime(element, ideal, earliest, latest));
		}
		return new ViolationFixes(null, combinedStartTimes, combinedUnsatisfiedNodes, combinedOpposingNodes);
	}
	
}
