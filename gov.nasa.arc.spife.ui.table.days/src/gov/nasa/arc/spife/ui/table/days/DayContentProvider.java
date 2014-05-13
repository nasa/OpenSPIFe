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
package gov.nasa.arc.spife.ui.table.days;

import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.IPlanElementTransferable;
import gov.nasa.ensemble.core.plan.editor.PlanElementTransferable;
import gov.nasa.ensemble.core.plan.editor.PlanInsertionLocation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTreeContentProvider;
import gov.nasa.ensemble.core.plan.temporal.modification.DirectPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.measure.quantity.Duration;

import org.eclipse.jface.viewers.ISelection;
import org.jscience.physics.amount.Amount;

public class DayContentProvider extends MergeTreeContentProvider {
	
	private IStructureModifier modifier = new DayPlanStructureModifier();
	private Day day;

	@Override
	public IStructureModifier getStructureModifier() {
		return modifier;
	}

	public void setDay(Day day) {
		this.day = day;
	}
	
	private final class DayPlanStructureModifier extends PlanStructureModifier {
		
		private static final String SOURCE_DATE_KEY = "sourceDate";
		private static final String INITIAL_EXTENTS = "initialExtents";
		private static final String MOVED_ELEMENTS = "movedElements";
		
		@Override
		public PlanTransferable getTransferable(ISelection selection) {
			PlanTransferable transferable = super.getTransferable(selection);
			if ((transferable != null) && (day != null)) { 
				transferable.setData(SOURCE_DATE_KEY, day.getDate());
			}
			return transferable;
		}

		@Override
		public ITransferable copy(ITransferable transferable) {
			PlanElementTransferable planTransferable = (PlanElementTransferable) super.copy(transferable);
			Object sourceDate = planTransferable.getData(SOURCE_DATE_KEY);
			planTransferable.setData(SOURCE_DATE_KEY, sourceDate);
			return planTransferable;
		}
		
		@Override
		public void add(ITransferable transferable, IStructureLocation location) {
			if (location instanceof PlanInsertionLocation) {
				computeChangedTimes((IPlanElementTransferable) transferable, day);
			}
			super.add(transferable, location);
		}
		
		@Override
		public void remove(ITransferable transferable, IStructureLocation location) {
			super.remove(transferable, location);
			if (location instanceof PlanInsertionLocation) {
				IPlanElementTransferable elementTransferable = (IPlanElementTransferable) transferable;
				Iterable<? extends EPlanElement> elements = elementTransferable.getData(MOVED_ELEMENTS);
				TemporalExtentsCache initialExtents = elementTransferable.getData(INITIAL_EXTENTS);
				TemporalUtils.resetExtents(elements, initialExtents);
			}
		}
				
		private void computeChangedTimes(IPlanElementTransferable transferable, Day targetDay) {
			List<? extends EPlanElement> elements = transferable.getPlanElements();
			Date sourceDate = transferable.getData(SOURCE_DATE_KEY);
			TemporalExtentsCache cache = new TemporalExtentsCache();
			DirectPlanModifier modifier = new DirectPlanModifier();
			for (EPlanElement element : elements) {
				cache.cache(element);
			}
			Calendar calendar = MissionConstants.getInstance().getMissionCalendar();
			Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
			for (EPlanElement element : elements) {
				TemporalMember member = element.getMember(TemporalMember.class);
				Date startTime = member.getStartTime();
				Map<EPlanElement, TemporalExtent> extents;
				if (startTime == null) {
					Date start = targetDay.getDate();
					extents = modifier.moveToStart(element, start, cache);
				} else if (sourceDate == null) {
					calendar.setTime(startTime);
					int hour = calendar.get(Calendar.HOUR_OF_DAY);
					int minute = calendar.get(Calendar.MINUTE);
					int second = calendar.get(Calendar.SECOND);
					int millisecond = calendar.get(Calendar.MILLISECOND);
					Date targetDate = targetDay.getDate();
					calendar.setTime(targetDate);
					calendar.set(Calendar.HOUR_OF_DAY, hour);
					calendar.set(Calendar.MINUTE, minute);
					calendar.set(Calendar.SECOND, second);
					calendar.set(Calendar.MILLISECOND, millisecond);
					Date start = calendar.getTime();
					extents = modifier.moveToStart(element, start, cache);
				} else {
					Amount<Duration> duration = DateUtils.subtract(targetDay.getDate(), sourceDate);
					extents = modifier.shiftElement(element, duration, cache);
				}
				changedTimes.putAll(extents);
			}
			TemporalUtils.setExtents(changedTimes);
			transferable.setData(INITIAL_EXTENTS, cache);
			transferable.setData(MOVED_ELEMENTS, changedTimes.keySet());
		}
		
		
		
	}

}
