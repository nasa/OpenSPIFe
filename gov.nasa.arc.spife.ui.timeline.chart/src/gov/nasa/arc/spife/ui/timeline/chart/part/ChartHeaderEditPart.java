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
package gov.nasa.arc.spife.ui.timeline.chart.part;

import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartElement;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.policy.ChartDropEditPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.policy.ChartHeaderLayoutEditPolicy;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPolicy;

public class ChartHeaderEditPart extends TimelineViewerEditPart<Chart> {

	private final Listener listener = new Listener();
	
	@Override
	public void activate() {
		super.activate();
		getModel().eAdapters().add(listener);
	}

	@Override
	public void deactivate() {
		super.deactivate();
		getModel().eAdapters().remove(listener);
	}
	
	@Override
	protected List getModelChildren() {
		List<ChartElement> children = new ArrayList<ChartElement>();
		children.addAll(getModel().getPlots());
		return children;
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new Figure();
		ToolbarLayout layout = new ToolbarLayout();
		layout.setSpacing(2);
		figure.setLayoutManager(layout);
		figure.setOpaque(true);
		figure.setBackgroundColor(ColorConstants.white);
		figure.setMinimumSize(new Dimension(0, getModel().getMinimumHeight()));
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(DropEditPolicy.DROP_ROLE, new ChartDropEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ChartHeaderLayoutEditPolicy());
	}

	public class Listener extends AdapterImpl {

		@Override
		public void notifyChanged(final Notification notification) {
			Object f = notification.getFeature();
			if (ChartPackage.Literals.CHART__MINIMUM_HEIGHT == f) {
				GEFUtils.runInDisplayThread(ChartHeaderEditPart.this, new Runnable() {
					@Override
					public void run() {
						int newHeight = notification.getNewIntValue();
						getFigure().setMinimumSize(new Dimension(0, newHeight));
						getFigure().invalidate();
					}
				});
			}
			refreshChildrenInDisplayThread();
		}
		
	}
}
