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
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.figure.ScaleFigure;
import gov.nasa.arc.spife.ui.timeline.figure.ScaleHeaderFigure;
import gov.nasa.arc.spife.ui.timeline.policy.ScaleEditPolicy;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class ScaleTimelineHeaderEditPart extends TimelineViewerEditPart<Object> {

	private final Listener listener = new Listener();
	
	@Override
	protected IFigure createFigure() {
		ScaleHeaderFigure f = new ScaleHeaderFigure();
		f.setFont(TimelineUtils.deriveScaleHeightFont(f.getFont()));
		return f;
	}

	@Override
	public void activate() {
		super.activate();
		TIMELINE_PREFERENCES.addPropertyChangeListener(listener);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		TIMELINE_PREFERENCES.removePropertyChangeListener(listener);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(ScaleEditPolicy.ROLE, new ScaleEditPolicy((ScaleFigure) getFigure()));
	}
	
	private class Listener implements IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			if (TimelinePreferencePage.P_SCALE_FONT_SIZE.equals(property)) {
				IFigure figure = getFigure();
				figure.setFont(TimelineUtils.deriveScaleHeightFont(figure.getFont()));
			}
		}
		
	}
	
}
