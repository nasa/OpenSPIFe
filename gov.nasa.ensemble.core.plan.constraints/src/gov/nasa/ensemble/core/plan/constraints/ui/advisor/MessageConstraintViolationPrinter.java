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
package gov.nasa.ensemble.core.plan.constraints.ui.advisor;

import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import org.apache.log4j.Logger;

public class MessageConstraintViolationPrinter extends ConstraintViolationPrinter {

	public MessageConstraintViolationPrinter() {
		super(null);
	}
	
	@Override
	public String getText(EPlanElement element) {
		return element.getName();
	}
	
	@Override
	public String getHypertext(ConstraintPoint point) {
		return getHypertext(point, false) + " of " + getText(point.getElement());
	}
	
	@Override
	public String getHypertext(ConstraintPoint point, boolean presentNotSubjunctive) {
		if (point.hasEndpoint()) {
			Timepoint timepoint = point.getEndpoint();
			switch (timepoint) {
			case START: return getText(timepoint) + (presentNotSubjunctive ? "s" : "");
			case END: return getText(timepoint) + (presentNotSubjunctive ? "s" : "");
			default: Logger.getLogger(MessageConstraintViolationPrinter.class).warn("unexpected timepoint: " + timepoint);
			}
			return "?timepoint?";
		} else {
			String anchor = point.getAnchor();
			return ParameterDescriptor.getInstance().getDisplayName(point.getElement(), anchor);
		}
	}

}
