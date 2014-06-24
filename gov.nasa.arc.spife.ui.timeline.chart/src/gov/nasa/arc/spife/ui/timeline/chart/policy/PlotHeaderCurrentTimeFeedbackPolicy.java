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
package gov.nasa.arc.spife.ui.timeline.chart.policy;

import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.part.PlotHeaderEditPart;
import gov.nasa.arc.spife.ui.timeline.policy.CursorTimeFeedbackEditPolicy;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.util.ProfileUtil;

import java.util.Date;

import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

public class PlotHeaderCurrentTimeFeedbackPolicy extends CursorTimeFeedbackEditPolicy implements TimelineConstants {

	private Listener listener = new Listener();
	
	@Override
	public void activate() {
		Plot plot = (Plot) getHost().getModel();
		Chart chart = plot.getChart();
		chart.eAdapters().add(listener);
		super.activate();
	}

	@Override
	public void deactivate() {
		Plot plot = (Plot) getHost().getModel();
		if (plot != null) {
			Chart chart = plot.getChart();
			if (chart != null) {
				chart.eAdapters().remove(listener);
			}
		}
		super.deactivate();
	}

	@Override
	protected void updateCursorTimeFeedback(Date date) {
		PlotHeaderEditPart ep = (PlotHeaderEditPart) getHost();
		Label label = ep.getCurrentValueLabel();
		Plot plot = (Plot) getHost().getModel();
		Profile profile = plot.getProfile();
		String text = ProfileUtil.getDisplayString(profile, date);
		if (text != null) {
			label.setText(text);
		}
		super.updateCursorTimeFeedback(date);
	}
	
	private class Listener extends AdapterImpl {

		@Override
		public void notifyChanged(Notification notification) {
			if (getViewer() == null) {
				return;
			}
			if (ChartPackage.Literals.CHART__PLOTS == notification.getFeature()) {
				final Long cursorTime = getTimeline().getCursorTime();
				if (cursorTime != null) {
					GEFUtils.runLaterInDisplayThread(getHost(), new Runnable() {
						@Override
						public void run() {
							updateCursorTimeFeedback(new Date(cursorTime));
						}
					});
				}
			}
		}
		
	}

}
