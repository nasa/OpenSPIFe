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
package gov.nasa.arc.spife.core.plan.editor.timeline.commands;

import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanTimelineEditPart;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.SetExtentsOperation;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.measure.quantity.Duration;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPartSite;
import org.jscience.physics.amount.Amount;

public class NudgeAction extends Action {
	
	private int factor = 0;
	protected EPlan plan = null;
	private PlanTimelineEditPart editPart = null;
	
	public NudgeAction(String label, int factor, PlanTimelineEditPart editPart) {
		super(label);
		this.factor = factor;
		this.editPart = editPart;
		this.plan = editPart.getModel();
	}

	private IUndoableOperation createOperation() {
		TimelineViewer viewer = editPart.getViewer();
		Timeline timeline = viewer.getTimeline();
		long step = timeline.getPage().getZoomOption().getMsNudgeThreshold();
		step *= this.factor;
		step *= MissionConstants.getInstance().getEarthSecondsPerLocalSeconds();
		Set<EPlanElement> elements = new LinkedHashSet<EPlanElement>();
		for (Object o : viewer.getSelectedEditParts()) {
			if (o instanceof TemporalNodeEditPart) {
				EPlanElement pe = ((TemporalNodeEditPart)o).getModel();
				if (canMove(pe)) {
					elements.add(pe);
				}
			}
		}
		if (elements.isEmpty()) {
			return null;
		}
		PlanModifierMember member = PlanModifierMember.get(plan);
		IPlanModifier modifier = member.getModifier();
		TemporalExtentsCache initialExtents = new TemporalExtentsCache(plan);
		Map<EPlanElement, TemporalExtent> allChangedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		Amount<Duration> delta = AmountUtils.toAmount(step, DateUtils.MILLISECONDS);
		for (EPlanElement pe : elements) {
			TemporalExtentsCache intermediateExtents = new TemporalExtentsCache(plan);
			Map<EPlanElement, TemporalExtent> changedTimes = modifier.shiftElement(pe, delta, intermediateExtents);
			allChangedTimes.putAll(changedTimes);
		}
		return new SetExtentsOperation(getText(), plan, allChangedTimes, initialExtents);
	}
	
	private boolean canMove(EPlanElement element) {
		return PlanEditApproverRegistry.getInstance().canModify(element);
	}

	@Override
	public void runWithEvent(Event event) {
		IUndoableOperation op = createOperation();
		if (op != null) {
			IUndoContext undoContext = TransactionUtils.getUndoContext(plan);
			IWorkbenchPartSite site = editPart.getViewer().getSite();
			WidgetUtils.execute(op, undoContext, event.widget, site);
		}
	}

}
