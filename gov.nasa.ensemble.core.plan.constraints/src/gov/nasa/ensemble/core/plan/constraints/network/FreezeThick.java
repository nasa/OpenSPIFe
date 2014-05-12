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
package gov.nasa.ensemble.core.plan.constraints.network;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;

import javax.measure.quantity.Duration;

import org.jscience.physics.amount.Amount;

/**
 * This version freezes all child relative positions to their respective container.
 * 
 * @author abachman
 */
public class FreezeThick<Time extends Long> extends Freezer<Time> {
	
	private final TemporalNetworkModel<Time> temporalNetworkModel;

	public FreezeThick(EPlan plan, TemporalNetwork<Time> temporalNetwork, TemporalNetworkModel<Time> temporalNetworkModel) {
		super(plan, temporalNetwork);
		this.temporalNetworkModel = temporalNetworkModel;
	}

	@Override
	protected void freeze(EPlan plan) {
		PlanVisitor visitor = new PlanVisitor() {
			@Override
			protected void visit(EPlanElement element) {
				if (element instanceof EActivityGroup) {
					EActivityGroup group = (EActivityGroup) element;
					freeze(group);
				}
			}
		};
		visitor.visitAll(plan);
	}
	
	private void freeze(EActivityGroup group) {
		PlanElementConstraintCache<Time> groupCache = temporalNetworkModel.elementToCache.get(group);
		TemporalExtent groupExtent = group.getMember(TemporalMember.class).getExtent();
		TemporalNetwork<Time>.Timepoint groupStart = groupCache.start;
		TemporalNetwork<Time>.Timepoint groupEnd = groupCache.end;
		for (EPlanElement child : group.getChildren()) {
			PlanElementConstraintCache<Time> childCache = temporalNetworkModel.elementToCache.get(child);
			TemporalExtent childExtent = child.getMember(TemporalMember.class).getExtent();
			TemporalNetwork<Time>.Timepoint childStart = childCache.start;
			TemporalNetwork<Time>.Timepoint childEnd = childCache.end;
			Amount<Duration> startOffset = DateUtils.subtract(childExtent.getStart(), groupExtent.getStart());
			Amount<Duration> endOffset = DateUtils.subtract(groupExtent.getEnd(), childExtent.getEnd());
			Time startBound = temporalNetworkModel.convertTimeDistanceToNetwork(startOffset);
			Time endBound = temporalNetworkModel.convertTimeDistanceToNetwork(endOffset);
			addConstraint(groupStart, childStart, startBound);
			addConstraint(childEnd, groupEnd, endBound);
		}
	}

}
