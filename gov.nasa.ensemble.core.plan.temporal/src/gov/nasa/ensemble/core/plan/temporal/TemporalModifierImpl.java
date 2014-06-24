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
package gov.nasa.ensemble.core.plan.temporal;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.CalculatedVariable;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.temporal.impl.TemporalModifier;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.SetExtentsOperation;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;

import java.util.Date;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.jscience.physics.amount.Amount;

public class TemporalModifierImpl extends TemporalModifier {

	@Override
	public IUndoableOperation set(TemporalMember owner, EStructuralFeature feature, Object value) {
		return set(owner, feature, value, owner.getCalculatedVariable());
	}
	
	public IUndoableOperation set(TemporalMember owner, EStructuralFeature feature, Object value, CalculatedVariable calculated) {
		EPlan plan = EPlanUtils.getPlan(owner);
		IPlanModifier modifier = PlanModifierMember.get(plan).getModifier();
		if (modifier != null) {
			TemporalExtentsCache cache = new TemporalExtentsCache(plan);
			EPlanElement element = owner.getPlanElement();
			Map<EPlanElement, TemporalExtent> changedTimes = null;
			if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME) {
				Date startTime = (Date)value;
				if (calculated == CalculatedVariable.DURATION) {
					changedTimes = modifier.setStart(element, startTime, cache);
				} else {
					changedTimes = modifier.moveToStart(element, startTime, cache);
				}
			}
			if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME) {
				Date endTime = (Date)value;
				if (calculated == CalculatedVariable.DURATION) {
					changedTimes = modifier.setEnd(element, endTime, cache);
				} else {
					changedTimes = modifier.moveToEnd(element, endTime, cache);
				}
			}
			if (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION) {
				Amount<Duration> duration = (Amount<Duration>)value;
				if (calculated == CalculatedVariable.END) {
					changedTimes = modifier.setDuration(element, duration, cache, true);
				} else {
					changedTimes = modifier.setDuration(element, duration, cache, false);
				}
			}
			if (changedTimes != null) {
				return new SetExtentsOperation("set start times", plan, changedTimes, cache);
			}
		}
		return super.set(owner, feature, value);
	}
	
}
