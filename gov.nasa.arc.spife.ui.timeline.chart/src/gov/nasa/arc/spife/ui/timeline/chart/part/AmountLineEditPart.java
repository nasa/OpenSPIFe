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

import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.ui.timeline.chart.figure.LineFigure;
import gov.nasa.arc.spife.ui.timeline.chart.figure.PrecisionPointList;
import gov.nasa.arc.spife.ui.timeline.chart.model.AmountLine;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.AmountExtent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.swt.SWT;
import org.jscience.physics.amount.Amount;

public class AmountLineEditPart extends ChartElementDataEditPart<AmountLine> {

	private Listener listener = new Listener();
	
	@Override
	public void activate() {
		super.activate();
		getModel().eAdapters().add(listener);
		getTimeline().getPage().eAdapters().add(listener);
		GEFUtils.runLaterInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				refreshVisuals();
			}
		});
	}

	@Override
	public void deactivate() {
		getModel().eAdapters().remove(listener);
		getTimeline().getPage().eAdapters().remove(listener);
		super.deactivate();
	}

	@Override
	protected IFigure createFigure() {
		LineFigure figure = new LineFigure();
		figure.setLineWidth(3);
		figure.setForegroundColor(ColorConstants.red);
		figure.setLineStyle(SWT.LINE_DASHDOT);
		refreshVisuals(figure);
		return figure;
	}
	
	@Override
	protected void createEditPolicies() {
		// no edit policies
	}

	@Override
	protected void refreshVisuals() {
		refreshVisuals(getFigure());
	}

	private void refreshVisuals(final IFigure figure) {
		AmountLine amountLine = getModel();
		LineFigure lineFigure = (LineFigure) figure;
		AmountExtent<?> extent = getExtent();
		IFigure parent = figure.getParent();
		if (extent != null && parent != null) {
			int x_start = 0;
			int x_end = parent.getBounds().width;
			Amount<?> amount = amountLine.getAmount();
			Double value = normalize(amount, extent);
			final PrecisionPointList points = new PrecisionPointList();
			points.addPrecisionPoint(x_start, value);
			points.addPrecisionPoint(x_end, value);
			lineFigure.setNormalizedPointList(points);
			lineFigure.repaint();
		}
	}
	
	private class Listener extends AdapterImpl {

		@Override
		public void notifyChanged(Notification notification) {
			Object feature = notification.getFeature();
			if (notification.getNotifier() instanceof AmountLine
					|| TimelinePackage.Literals.PAGE__CURRENT_PAGE_EXTENT == feature
					|| TimelinePackage.Literals.PAGE__START_TIME == feature
					|| TimelinePackage.Literals.PAGE__DURATION == feature
					|| TimelinePackage.Literals.PAGE__ZOOM_OPTION == feature) {
				refreshVisualsInDisplayThread();
			}
		}
		
	}

}
