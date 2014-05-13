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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.LocationRequest;

public class TemporalNodeHoverEditPolicy extends PlanTimelineViewerEditPolicy {

	public static final String ROLE = TemporalNodeHoverEditPolicy.class.getName();
	
	public static final TooltipShellManager TOOLTIP_SHELL_MANAGER = new TooltipShellManager();

	/**
	 * Create a new instance of this TemporalNodeHoverEditPolicy. Initialize the
	 * tooltipAtributes variables and the tooltip name,value pair display length
	 * attributes.
	 */
	public TemporalNodeHoverEditPolicy() {
		super();
	}

	/**
	 * Remove the figure listener that was added in activate().
	 * If there is a tooltipShell which has not been disposed, dispose of it
	 * since it is only valid for this TemporalNodeEditPart instance.
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		GraphicalEditPart host = (GraphicalEditPart)getHost();
		TOOLTIP_SHELL_MANAGER.hide(host);
	}

	@Override
	public boolean understandsRequest(Request request) {
		return (RequestConstants.REQ_SELECTION_HOVER == request.getType());
	}

	/**
	 * When asked to show target feedback, figure out where to display the tooltip.
	 * Next, get the tooltip (maybe a new one will need to be created), and display
	 * it at the calculated location.
	 *
	 * @param request the request
	 */
	@Override
	public void showTargetFeedback(Request request) {
		if (understandsRequest(request)) {
			if (request instanceof LocationRequest) {
				LocationRequest locationRequest = (LocationRequest)request;
				Point location = locationRequest.getLocation();
				GraphicalEditPart host = (GraphicalEditPart)getHost();
				TOOLTIP_SHELL_MANAGER.display(host, location);
			}
		}
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		GraphicalEditPart host = (GraphicalEditPart)getHost();
		TOOLTIP_SHELL_MANAGER.hide(host);
	}

}
