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
package gov.nasa.ensemble.core.plan.editor.drudgery;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.constraints.DrudgerySavingHandler;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

public class AlignToEndHandler extends DrudgerySavingHandler {

	public AlignToEndHandler() {
		super("Align To End", "Aligning to End");
	}
	
	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> elements) {
		ECollections.sort((EList<? extends EPlanElement>)elements, END_TIME_ORDER);
		Date end = elements.get(elements.size()-1).getMember(TemporalMember.class).getExtent().getEnd();
		Map<EPlanElement, Date> map = new HashMap<EPlanElement, Date>();
		for (EPlanElement element: elements) {
			TemporalExtent extent = element.getMember(TemporalMember.class).getExtent();
			if (extent != null) {
				map.put(element, new Date(end.getTime() - extent.getDurationMillis() ));
			}
		}
		return map;
	}

	@Override
	public String getCommandId() {
		return ALIGN_END_COMMAND_ID;
	}
	
	public static final Comparator<EPlanElement> END_TIME_ORDER = new Comparator<EPlanElement>() {
		
		@Override
		public int compare(EPlanElement o1, EPlanElement o2) {
			TemporalMember m1 = o1.getMember(TemporalMember.class);
			TemporalMember m2 = o2.getMember(TemporalMember.class);
			if ((m1 == null) || (m2 == null)) {
				return PlanUtils.INHERENT_ORDER.compare(o1, o2);
			}
			Date d1 = m1.getExtent().getEnd();
			Date d2 = m2.getExtent().getEnd();
			if ((d1 == null) || (d2 == null)) {
				return PlanUtils.INHERENT_ORDER.compare(o1, o2);
			}
			int result = (int)DateUtils.subtract(d1, d2);
			if (result != 0) {
				return result;
			}
			return PlanUtils.INHERENT_ORDER.compare(o1, o2);
		}
	};

}
