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
package gov.nasa.ensemble.core.plan.constraints;

import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintPoint;
import gov.nasa.ensemble.core.model.plan.util.ParameterDescriptor;
import gov.nasa.ensemble.core.plan.editor.PlanPrinter;
import gov.nasa.ensemble.emf.model.common.Timepoint;

import org.apache.log4j.Logger;

public class TemporalPrinter extends PlanPrinter {

	public TemporalPrinter(IdentifiableRegistry<EPlanElement> identifiableRegistry) {
		super(identifiableRegistry);
	}
	
	/**
	 * Returns the timepoint as an english string
	 * @param timepoint
	 * @return the timepoint as an english string
	 */
	public static String getText(Timepoint timepoint) {
		switch (timepoint) {
		case START: return "start";
		case END: return "end";
		default: Logger.getLogger(TemporalPrinter.class).warn("unexpected timepoint: " + timepoint);
		}
		return "?timepoint?";
	}
	
	/**
	 * Returns the timepoint as a hypertext string
	 * @param timepoint
	 * @return the timepoint as a hypertext string
	 */
	public String getHypertext(ConstraintPoint point, boolean presentNotSubjunctive) {
		EPlanElement planElement = point.getElement();
		Object anchorElement = point.getAnchorElement();
		if (anchorElement instanceof String) {
			String featureName = (String) anchorElement;
			return ParameterDescriptor.getInstance().getDisplayName(planElement, featureName);
		} else if (Timepoint.START == anchorElement) {
			return html("<span color=\"start\">") + getText((Timepoint) anchorElement) + (presentNotSubjunctive ? "s" : "") + html("</span>");
		} else if (Timepoint.END == anchorElement) {
			return html("<span color=\"end\">") + getText((Timepoint) anchorElement) + (presentNotSubjunctive ? "s" : "")  + html("</span>");
		}
		Logger.getLogger(TemporalPrinter.class).warn("unexpected timepoint: " + anchorElement);
		return "?timepoint?";
	}

	/**
	 * Returns an English string describing an timepoint of a plan element.
	 * @param timepoint
	 * @param node
	 * @return an English string describing an timepoint of a plan element.
	 */
	public static String getText(ConstraintPoint point) {
		Timepoint timepoint = point.getEndpoint();
		EPlanElement element = point.getElement();
		return getText(timepoint) + " of " + PlanPrinter.getPrintName(element);
	}
	
	/**
	 * Returns a form text string describing an timepoint of a plan element.
	 * @param timepoint
	 * @param node
	 * @return a form text string describing an timepoint of a plan element.
	 */
	public String getHypertext(ConstraintPoint point) {
		EPlanElement element = point.getElement();
		return getHypertext(point, false) + " of " + getText(element);
	}

}
