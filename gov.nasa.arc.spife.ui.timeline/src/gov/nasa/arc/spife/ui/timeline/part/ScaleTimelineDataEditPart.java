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

import gov.nasa.arc.spife.ui.timeline.figure.ScaleFigure;
import gov.nasa.arc.spife.ui.timeline.policy.CursorTimeFeedbackEditPolicy;
import gov.nasa.arc.spife.ui.timeline.policy.ScaleCursorTimeFeedbackEditPolicy;
import gov.nasa.arc.spife.ui.timeline.policy.ScaleEditPolicy;
import gov.nasa.arc.spife.ui.timeline.preference.TimelineDateFormatRegistry;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;

import java.text.DateFormat;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public class ScaleTimelineDataEditPart extends AbstractTimelineDataEditPart<Object> {

	private final Listener listener = new Listener();
	
	public ScaleTimelineDataEditPart() {
		// default constructor
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
	public boolean isSelectable() {
		return false;
	}

	@Override
	protected List<?> getModelChildren() {
		return Collections.singletonList(getTimeline().getTimelineMarkerManager());
	}
	
	@Override
	protected IFigure createPrimaryFigure() {
		ScaleFigure figure = new ScaleFigure();
		figure.setLayoutManager(new XYLayout());
		figure.setPreferredSize(computeFigureBounds());
		figure.setFont(TimelineUtils.deriveScaleHeightFont(figure.getFont()));
		return figure;
	}
	
	@Override
	protected Dimension computeFigureBounds() {
		Dimension d = super.computeFigureBounds();
		Font font = TimelineUtils.deriveScaleHeightFont(Display.getDefault().getSystemFont());
		int height = FigureUtilities.getFontMetrics(font).getHeight();
		d.height = (int) (height * (TimelineDateFormatRegistry.getDateFormats().size() + 3.25));
		return d;
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(ScaleEditPolicy.ROLE, new ScaleEditPolicy((ScaleFigure) getLayer(LAYER_DATA_PRIMARY_LAYER)) {

			@Override
			protected void dateFormatsUpdated(List<DateFormat> dateFormats) {
				super.dateFormatsUpdated(dateFormats);
				figure.setPreferredSize(computeFigureBounds());
			}
			
		});
		installEditPolicy(CursorTimeFeedbackEditPolicy.ROLE, new ScaleCursorTimeFeedbackEditPolicy());
	}
	
	private class Listener implements IPropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent event) {
			String property = event.getProperty();
			if (TimelinePreferencePage.P_SCALE_FONT_SIZE.equals(property)) {
				IFigure figure = getLayer(LAYER_DATA_PRIMARY_LAYER);
				figure.setFont(TimelineUtils.deriveScaleHeightFont(figure.getFont()));
				getFigure().setPreferredSize(computeFigureBounds());
			}
		}
		
	}
	
}
