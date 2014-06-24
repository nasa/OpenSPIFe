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
package gov.nasa.ensemble.core.plan.temporal.columns;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.merge.IMergeColumnProvider;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ParameterColumn;
import gov.nasa.ensemble.core.plan.editor.merge.columns.ParameterFacet;
import gov.nasa.ensemble.core.plan.editor.merge.contributions.MergeTreePlanContributor;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.SetExtentsOperation;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;

public class StartTimeParameterColumn extends ParameterColumn<Date> {
	
	private static final int DEFAULT_WIDTH = 105;
	private static final EAttribute START_TIME_FEATURE = TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME;
	
	public StartTimeParameterColumn(IMergeColumnProvider provider) {
		super(provider, "Start Time", DEFAULT_WIDTH);
	}
	
	@Override
	public boolean needsUpdate(Object feature) {
		if (feature == START_TIME_FEATURE) {
			return true;
		}
		return false;
	}
	
	@Override
	public ParameterFacet<Date> getFacet(Object element) {
		if (element instanceof EPlanElement) {
			EPlanElement ePlanElement = (EPlanElement) element;
			TemporalMember member = ePlanElement.getMember(TemporalMember.class);
			Date value = member.getStartTime();
			return new ParameterFacet<Date>(member, START_TIME_FEATURE, value);
		} else if (element instanceof EObject) {
			EObject eObject = (EObject) element;
			Object value = MergeTreePlanContributor.getInstance().getValueForFeature(eObject, START_TIME_FEATURE);
			if (value instanceof Date) {
				return new ParameterFacet<Date>(eObject, START_TIME_FEATURE, (Date) value);
			}
		}
		return null;
	}
	
	@Override
	public void modify(ParameterFacet<Date> parameter, Object date, IUndoContext undoContext) {
		Date newStart = (Date)date;
		EPlanElement planElement = parameter.getElement();
		EPlan plan = EPlanUtils.getPlan(planElement);
		TemporalMember temporal = planElement.getMember(TemporalMember.class);
		Date startTime = temporal.getStartTime();
		IPlanModifier modifier = PlanModifierMember.get(plan).getModifier();
		if (modifier == null) {
			ParameterFacet<Date> facet = new ParameterFacet<Date>(parameter.getObject(), START_TIME_FEATURE, startTime);
			super.modify(facet, newStart, undoContext);
			return;
		}
		TemporalExtentsCache cache = new TemporalExtentsCache(plan);
		Map<EPlanElement, TemporalExtent> changedTimes = modifier.moveToStart(planElement, newStart, cache);
		IUndoableOperation operation = new SetExtentsOperation("set start times", plan, changedTimes, cache);
		operation = EMFUtils.addContributorOperations(operation, temporal, START_TIME_FEATURE, startTime, newStart);
		CommonUtils.execute(operation, undoContext);
	}

	@Override
	public Color getForeground(ParameterFacet<Date> parameter) {
		if (parameter != null) {
			EPlanElement planElement = parameter.getElement();
			Date startTime = planElement.getMember(TemporalMember.class).getStartTime();
			if (startTime == null) {
				return OUT_OF_DATE_COLOR;
			}
		}
		return super.getForeground(parameter);
	}

	@Override
	public Comparator<ParameterFacet<Date>> getComparator() {
		final Comparator<ParameterFacet<Date>> superComparator = super.getComparator();
		return new Comparator<ParameterFacet<Date>>() {
			@Override
			public int compare(ParameterFacet<Date> o1, ParameterFacet<Date> o2) {
				if ((o1 == null) || (o2 == null)) {
					// handle null
					return superComparator.compare(o1, o2);
				}
				Date date1 = o1.getValue();
				Date date2 = o2.getValue();
				if ((date1 == null) || (date2 == null)) {
					// handle null
					return superComparator.compare(o1, o2);
				}
				int result = date1.compareTo(date2);
				if (result == 0) {
					// break ties
					return superComparator.compare(o1, o2);
				}
				return result;
			}
		};
	}

}
