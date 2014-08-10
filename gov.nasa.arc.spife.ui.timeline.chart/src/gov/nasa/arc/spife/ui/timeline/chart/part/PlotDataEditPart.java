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
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.chart.policy.ChartDropEditPolicy;
import gov.nasa.arc.spife.ui.timeline.chart.util.PlotUtil;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.AmountExtent;
import gov.nasa.ensemble.core.jscience.JSciencePackage;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.emf.SafeAdapter;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;

import javax.measure.converter.ConversionException;
import javax.measure.unit.Unit;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.swt.graphics.Color;

public abstract class PlotDataEditPart extends ChartElementDataEditPart<Plot> {

	private Listener listener = new Listener();

	private Page page = null;

	private TransactionalEditingDomain domain = null;

	private Adapter adapter = new SafeAdapterImpl();

	public Page getPage() {
		if (page == null) {
			page = getViewer() == null ? null : getViewer().getPage();
		}
		return page;
	}

	@Override
	public void activate() {
		super.activate();
		domain = gov.nasa.ensemble.emf.transaction.TransactionUtils.getDomain(getTimeline().getTimelineModel());
		if (domain != null) {
			domain.addResourceSetListener(listener);
			getModel().eAdapters().add(adapter);
		}
		GEFUtils.runLaterInDisplayThread(this, new Runnable() {
			@Override
			public void run() {
				try {
					updatePointList();
				} catch (ProfileUpdatedException e) {
					// fall out because we should get a notice on the listener that it is needs update
				}
			}
		});
	}

	@Override
	public void deactivate() {
		super.deactivate();
		if (domain != null) {
			domain.removeResourceSetListener(listener);
			domain = null;
			getModel().eAdapters().remove(adapter);
		}
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(DropEditPolicy.DROP_ROLE, new ChartDropEditPolicy());
	}

	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		try {
			updatePointList();
			GEFUtils.runInDisplayThread(this, new Runnable() {
				@Override
				public void run() {
					getFigure().repaint();
				}
			});
		} catch (ProfileUpdatedException e) {
			// fall out because we will refresh again later in response to this profile change
		}
	}

	public abstract void updatePointList() throws ProfileUpdatedException;

	@Override
	protected AmountExtent<?> getExtent() {
		final Plot plot = getModel();
		AmountExtent<?> extent = super.getExtent();
		if (extent == null) {
			extent = plot.getExtent();
		}
		if (extent != null) {
			Unit nominalUnits = getModel().getProfile().getUnits();
			try {
				extent = extent.to(nominalUnits);
			} catch (ConversionException e) {
				// silence
			}
		}
		return extent;
	}

	private void refreshColorInDisplayThread() {
		final Color color = PlotUtil.getColor(getModel());
		if (color != null) {
			GEFUtils.runInDisplayThread(PlotDataEditPart.this, new Runnable() {
				@Override
				public void run() {
					getFigure().setForegroundColor(color);
				}
			});
		}
	}

	private class SafeAdapterImpl extends SafeAdapter {

		@Override
		protected void handleNotify(Notification notification) {
			if (Notification.RESOLVE == notification.getEventType() && ChartPackage.Literals.PLOT__PROFILE == notification.getFeature()) {
				refreshVisualsInDisplayThread();
				refreshColorInDisplayThread();
			}
		}

	}

	private class Listener extends PostCommitListener {

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			boolean updatePointList = false;
			boolean refreshColorInDisplayThread = false;
			Plot plot = getModel();
			Profile<?> profile = plot == null ? null : plot.getProfile();
			Chart chart = plot == null ? null : plot.getChart();
			Timeline timeline = getTimeline();
			for (Notification notification : event.getNotifications()) {
				Object feature = notification.getFeature();
				Object notifier = notification.getNotifier();

				if (TimelinePackage.Literals.PAGE__CURRENT_PAGE_EXTENT == feature || TimelinePackage.Literals.PAGE__START_TIME == feature || TimelinePackage.Literals.PAGE__DURATION == feature || TimelinePackage.Literals.PAGE__ZOOM_OPTION == feature) {
					if (timeline != null && timeline.getTimelineModel() != null && timeline.getTimelineModel().getPage() == notifier) {
						updatePointList = true;
					}
				}

				// SPF-11662 -- Allow the point list to be updated if setting a plot's profile to a proxy
				if (profile != null && notifier == plot && ChartPackage.Literals.PLOT__PROFILE == feature) {
					updatePointList = true;
				}

				if (profile == null || profile.eIsProxy()) {
					continue;
				}

				if (notifier != plot && notifier != profile && notifier != chart) {
					continue;
				}

				if ((JSciencePackage.Literals.PROFILE__VALID == feature || ChartPackage.Literals.PLOT__RGB == feature)) {
					refreshColorInDisplayThread = true;
				}

				boolean proxyResolved = Notification.RESOLVE == notification.getEventType() && ChartPackage.Literals.PLOT__PROFILE == feature;
				if (proxyResolved || JSciencePackage.Literals.PROFILE__DATA_POINTS == feature || JSciencePackage.Literals.PROFILE__EXTENT == feature || ChartPackage.Literals.LINE_CHART__MAXIMUM_LINE == feature || ChartPackage.Literals.LINE_CHART__MINIMUM_LINE == feature || ChartPackage.Literals.LINE_CHART__LINES == feature || ChartPackage.Literals.PLOT__FIT == feature || ChartPackage.Literals.PLOT__EXTENT == feature || ChartPackage.Literals.PLOT__SHOW_TEXT == feature
						|| ChartPackage.Literals.PLOT__AUTO_ASSIGN_RGB == feature) {
					updatePointList = true;
				}
			}

			if (updatePointList) {
				try {
					updatePointList();
				} catch (ProfileUpdatedException e) {
					LogUtil.error("profile was updated while in a post commit listener?!", e);
				}
			}

			if (refreshColorInDisplayThread) {
				refreshColorInDisplayThread();
			}
		}

	}

	public static class ProfileUpdatedException extends Exception {
		// marker exception
	}

}
