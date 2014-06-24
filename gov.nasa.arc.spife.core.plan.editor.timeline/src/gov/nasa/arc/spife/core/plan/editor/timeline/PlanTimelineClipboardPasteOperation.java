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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.Timeline.SELECTION_MODE;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.AmountUtils;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.PlanClipboardPasteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.temporal.modification.DirectPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchSite;
import org.jscience.physics.amount.Amount;

public class PlanTimelineClipboardPasteOperation extends
		PlanClipboardPasteOperation {

	private TemporalExtentsCache initialExtents;
	private final Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
	private boolean pasteHereChecked = false;
	private List<? extends EPlanElement> elements;
	private PlanTimeline planTimeline;
	private Event event;

	public PlanTimelineClipboardPasteOperation(ISelection targetSelection, IStructureModifier modifier, Event event, PlanTimeline planTimeline) {
		super(targetSelection, modifier);
		this.planTimeline = planTimeline;
		this.event = event;
	}
	
	@Override
	protected void dispose(UndoableState state) {
		super.dispose(state);
		changedTimes.clear();
		initialExtents = null;
		elements = null;
		planTimeline = null;
	}
	
	@Override
	protected void doit() {
		if (!pasteHereChecked) {
			checkForPasteHere();
			pasteHereChecked = true;
		}
		TemporalUtils.setExtents(changedTimes);
		super.doit();
	}

	@Override
	protected void undo() {
		super.undo();
		TemporalUtils.resetExtents(changedTimes.keySet(), initialExtents);
	}

	private void checkForPasteHere() {
		if ((event != null) && (event.widget instanceof MenuItem)) {
			final MenuItem menuItem = (MenuItem)event.widget;
			WidgetUtils.runInDisplayThread(menuItem, new Runnable() {
				@Override
				public void run() {
					setChangedTimesForPasteHere(menuItem);
				}
			}, true);
		}
	}
	
	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
		SELECTION_MODE oldMode = planTimeline.getSelectionMode();
		try {
			planTimeline.setSelectionMode(SELECTION_MODE.NONE);
			super.displayExecute(widget, site);
		} finally {
			planTimeline.setSelectionMode(oldMode);
		}
	}
	
	private void setChangedTimesForPasteHere(MenuItem menuItem) {
		Menu parentMenu = menuItem.getParent();
		MenuItem parentItem = parentMenu.getParentItem();
		if ((parentItem == null) && (transferable instanceof PlanTransferable)) {
			PlanTransferable planTransferable = (PlanTransferable)transferable;
			elements = planTransferable.getPlanElements();
			initialExtents = new TemporalExtentsCache();
			for (EPlanElement element : elements) {
				initialExtents.cache(element);
			}
			/* determine how much time needs to be added remaining element's
			 * start time (everything should be moved by the same amount)
			 */
			Page page = planTimeline.getPage();
			Long cursorTime = planTimeline.getCursorTime();				
			EPlanElement earliestElement = getEarliestPlanElement(elements);
			Amount<Duration> delta = getTimeDelta(earliestElement, cursorTime, page);
			IPlanModifier directPlanModifier = new DirectPlanModifier();
			for (EPlanElement element : elements) {
				changedTimes.putAll(directPlanModifier.shiftElement(element, delta, initialExtents));
			}
		}
	}
	
	private EPlanElement getEarliestPlanElement(List<? extends EPlanElement> elements) {
		Date earliestDate = null;
		EPlanElement earliestElement = null;
		for (EPlanElement element : elements) {
			TemporalMember temporalMember = element.getMember(TemporalMember.class);
			if (temporalMember != null) {
				Date elementStartTime = temporalMember.getStartTime();
				if ((earliestDate == null) || elementStartTime.before(earliestDate)) {
					earliestDate = elementStartTime;
					earliestElement = element;
				}
			}
		}
		return earliestElement;
	}

	private Amount<Duration> getTimeDelta(EPlanElement ePlanElement, long cursorTime, Page page) {
		TemporalMember temporalMember = ePlanElement.getMember(TemporalMember.class);
		long currentStartTime = temporalMember.getStartTime().getTime();
		long perspectiveDelta = cursorTime - currentStartTime;
		int moveThreshold = (int) page.getZoomOption().getMsMoveThreshold();
		Date delta = MissionCalendarUtils.round(perspectiveDelta, moveThreshold);
		return AmountUtils.toAmount(delta.getTime(), DateUtils.MILLISECONDS);		
	}
	
}
