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
package gov.nasa.arc.spife.europa;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalConstraint;
import gov.nasa.ensemble.core.plan.constraints.network.ConsistencyProperties;
import gov.nasa.ensemble.dictionary.ERule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EReference;

public class EuropaClientSideModel {

	private static final Logger trace = Logger.getLogger(EuropaClientSideModel.class);

	public final EuropaConverter converter;
	
	public final IdentifiableRegistry<EPlanElement> identifiableRegistry = new IdentifiableRegistry<EPlanElement>();
	public final IdentifiableRegistry<TemporalConstraint> constraintRegistry = new IdentifiableRegistry<TemporalConstraint>();
	public final List<EPlanElement> knownLeaves = new ArrayList<EPlanElement>();
	public final Map<EPlanElement, ConsistencyProperties> elementToConsistencyProperties = new LinkedHashMap<EPlanElement, ConsistencyProperties>();
	public final Map<TemporalChain, Set<BinaryTemporalConstraint>> chainRelations = new LinkedHashMap<TemporalChain, Set<BinaryTemporalConstraint>>();
	public final Map<BinaryTemporalConstraint, TemporalChain> relationsChain = new LinkedHashMap<BinaryTemporalConstraint, TemporalChain>();
	public final Map<EPlanElement, Date> lastStartTimeMap = new LinkedHashMap<EPlanElement, Date>();
	public final Map<EPlanElement, Long> lastDurationMap = new LinkedHashMap<EPlanElement, Long>();
	public final Map<EPlanElement, Boolean> lastSchedulednessMap = new LinkedHashMap<EPlanElement, Boolean>();
	public final Map<EPlanElement, Set<ERule>> waivedRulesForElement = new LinkedHashMap<EPlanElement, Set<ERule>>();
	public final Map<EActivity, Map<EReference, Map<String, String>>> activityResources = new LinkedHashMap<EActivity, Map<EReference, Map<String, String>>>();
	private final Map<EPlanElement, Set<PeriodicTemporalConstraint>> elementToTimepointConstraints = new LinkedHashMap<EPlanElement, Set<PeriodicTemporalConstraint>>();

	public Set<ERule> waivedRulesForPlan = Collections.emptySet();
	public PeriodicTemporalConstraint planBound;


	public EuropaClientSideModel(Date epoc) {
		this.converter = new EuropaConverter(epoc);
	}

	public void addedTimepointConstraint(EPlanElement element, PeriodicTemporalConstraint timepointConstraint) {
		Set<PeriodicTemporalConstraint> timepointConstraints = elementToTimepointConstraints.get(element);
		if (timepointConstraints == null) {
			timepointConstraints = new LinkedHashSet<PeriodicTemporalConstraint>();
			elementToTimepointConstraints.put(element, timepointConstraints);
		}
		timepointConstraints.add(timepointConstraint);
	}

	public void removedTimepointConstraint(EPlanElement node, PeriodicTemporalConstraint timepointConstraint) {
		Set<PeriodicTemporalConstraint> timepointConstraints = elementToTimepointConstraints.get(node);
		if (timepointConstraints != null) {
			timepointConstraints.remove(timepointConstraint);
		}
	}
	
	public Set<PeriodicTemporalConstraint> getTimepointConstraints(EPlanElement element) {
		return elementToTimepointConstraints.get(element);
	}

	/**
	 * Returns the last known scheduledness of the given element.
	 * Should only be called on elements which are known to europa.
	 * Will return "true" for elements whose scheduledness is unknown, 
	 * for robustness. (although an error will be logged)
	 * 
	 * @param element
	 * @return
	 */
	public boolean isScheduled(EPlanElement element) {
		Boolean scheduledness = lastSchedulednessMap.get(element);
    	if (scheduledness == null) {
    		trace.error("null scheduledness on a plan element: " + element.getName(), new Throwable());
    		return true;
    	}
    	return scheduledness.booleanValue();
	}
	
}
