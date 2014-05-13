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
package gov.nasa.ensemble.core.plan.parameters;

import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;

public class SpifePlanUtils {

	public static TriState getVisible(EPlanElement pe) {
		return getVisibleR(pe, TriState.TRUE);
	}
	
	public static void setVisible(EPlanElement node, boolean isVisible) {
		setVisibleR(node, isVisible);
	}
	
	public static TriState getScheduled(EPlanElement pe) {
		TemporalMember member = pe.getMember(TemporalMember.class);
		Boolean value = member.getScheduled();
		if (value == null) {
			return TriState.QUASI;
		} else if (value.booleanValue()) {
			return TriState.TRUE;
		} // else...
		return TriState.FALSE;
	}
	
	private static TriState getVisibleR(EPlanElement pe, TriState defaultValue) {
		if (pe instanceof EActivity) {
			boolean value = pe.getMember(CommonMember.class).isVisible();
			return (value ? TriState.TRUE : TriState.FALSE);
		} // else...
		TriState prevValue = null;
		for (EPlanElement child : EPlanUtils.getChildren(pe)) {
			TriState curValue = getVisibleR(child, defaultValue);
			if ( prevValue!= null && !prevValue.equals(curValue) ) {
				return TriState.QUASI; 
			}
			prevValue = curValue;
		}
		return prevValue==null?defaultValue:prevValue;
	}
	
	private static void setVisibleR(EPlanElement pe, boolean value) {
		if (pe instanceof EActivity) {
			pe.getMember(CommonMember.class).setVisible(value);
		} // else...
		for (EPlanElement child : EPlanUtils.getChildren(pe)) {
			setVisibleR(child, value);
		}
	}
	
}
