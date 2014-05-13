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

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.constraints.DrudgerySavingHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

public class AlignToStartHandler extends DrudgerySavingHandler {

	public AlignToStartHandler() {
		super("Align To Start", "Aligning to Start");
	}

	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> elements) {
		Date start = elements.get(0).getMember(TemporalMember.class).getStartTime();
		Map<EPlanElement, Date> map = new HashMap<EPlanElement, Date>();
		for (EPlanElement element: elements) {
			map.put(element, new Date(start.getTime()));
		}
		return map;
	}

	@Override
	public String getCommandId() {
		return ALIGN_START_COMMAND_ID;
	}

}
