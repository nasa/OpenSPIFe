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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.constraints.DrudgerySavingHandler;

public class CompressRightHandler extends DrudgerySavingHandler {

	public CompressRightHandler() {
		super("Compress Right", "Compressing Right");
	}

	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> elements) {
		return checkOrChangeTimes(elements, false);
	}
	
	/**
	 * 
	 * @param elements
	 * @param abortEarly if true, will return as soon as the map is not empty
	 * @return
	 */
	private Map<EPlanElement, Date> checkOrChangeTimes(List<? extends EPlanElement> elements, boolean abortEarly) {
		Map<EPlanElement, Date> map = new HashMap<EPlanElement, Date>();
		if (elements.size() >= 1) {
			EPlanElement lastElement = elements.get(elements.size() - 1);
			TemporalExtent start = lastElement.getMember(TemporalMember.class).getExtent();
			Date date = start.getStart();
			for (int i = elements.size() - 2; i >= 0; i--) {
				EPlanElement element = elements.get(i);
				TemporalMember member = element.getMember(TemporalMember.class);
				date = DateUtils.subtract(date, member.getDuration());
				if (!member.getStartTime().equals(date)) {
					map.put(element, date);
					if (abortEarly) {
						break;
					}
				}
			}
		}
		return map;
	}	

	@Override
	public String getCommandId() {
		return COMPRESS_RIGHT_COMMAND_ID;
	}

}
