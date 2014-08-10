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

import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartFactory;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.color.ColorUtils;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.jscience.ui.dnd.ProfileTransferProvider;
import gov.nasa.ensemble.core.jscience.ui.dnd.ProfileTransferable;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.gef.Request;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

public class ChartDropEditPolicy extends DropEditPolicy {

	private static final String P_AUTO_ASSIGN_RGB_DEFAULT = "timeline.heatmap.autoassignarb.default";

	private static final Transfer PROFILE_TRANSFER = ProfileTransferProvider.transfer;

	@Override
	protected boolean isSupportedTransferData(TransferData d) {
		return PROFILE_TRANSFER.isSupportedType(d);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected org.eclipse.gef.commands.Command getDropCommand(final Request request) {
		Object model = getHost().getModel();
		final Chart chart;
		if (model instanceof Chart) {
			chart = (Chart) model;
		} else if (model instanceof Plot) {
			chart = ((Plot) model).getChart();
		} else {
			return null;
		}
		final List<String> uriFragments = getURIFragments(request);
		if (uriFragments.isEmpty()) {
			return null;
		}

		return new AddResourceCommand(uriFragments, chart);
	}

	private Plot createPlot(Profile profile) {
		final Plot plot = ChartFactory.eINSTANCE.createPlot();
		plot.setName(getPlotName(profile));
		plot.setProfile(profile);
		plot.setShowText(EcorePackage.Literals.EBOOLEAN != profile.getDataType() && EcorePackage.Literals.EBOOLEAN_OBJECT != profile.getDataType());
		Object rgbString = profile.getAttributes().get(Profile.RGB_ATTRIBUTE);
		if (rgbString != null && rgbString instanceof String) {
			try {
				plot.setRgb(ColorUtils.parseRGB((String) rgbString));
			} catch (NumberFormatException e) {
				LogUtil.warn("Ignoring profile color spec: " + e.getMessage());
			}
		}
		plot.setAutoAssignRGB(Boolean.parseBoolean(System.getProperty(P_AUTO_ASSIGN_RGB_DEFAULT)));
		return plot;
	}

	private String getPlotName(Profile profile) {
		ChartDropEditPolicyProfileProvider provider = ClassRegistry.createInstance(ChartDropEditPolicyProfileProvider.class);
		if (provider != null) {
			final String name = provider.getPlotName(profile);
			if (name != null) {
				return name;
			}
		}
		return null;
	}

	private List<String> getURIFragments(final Request request) {
		List<String> uriFragments = Collections.emptyList();
		Map<String, Object> data = request.getExtendedData();
		DropTargetEvent event = (DropTargetEvent) data.get("dropTargetEvent");
		if (event.data != null) {
			ITransferable transferable = TransferRegistry.getInstance().getDroppedObjects(event.data, event.currentDataType);
			if (transferable instanceof ProfileTransferable) {
				ProfileTransferable erpt = (ProfileTransferable) transferable;
				uriFragments = erpt.getUriFragments();
				if (uriFragments.isEmpty()) {
					return null;
				}
			}
		}
		return uriFragments;
	}

	public void addResourceProfile(final Chart graph, Profile profile) {
		final Plot plot = createPlot(profile);
		gov.nasa.ensemble.emf.transaction.TransactionUtils.writing(graph, new Runnable() {
			@Override
			public void run() {
				graph.getPlots().add(plot);
			}
		});
	}

	private final class AddResourceCommand extends org.eclipse.gef.commands.Command {

		private final List<String> uriFragments;
		private final Chart chart;

		private AddResourceCommand(List<String> uriFragments, Chart chart) {
			this.uriFragments = uriFragments;
			this.chart = chart;
		}

		@Override
		public void execute() {
			List<Plot> plots = new ArrayList<Plot>();
			EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(chart);
			for (String uriStr : uriFragments) {
				URI uri = URI.createURI(uriStr);
				ResourceSet resourceSet = domain.getResourceSet();
				Profile profile = null;
				for (ProfileLoader loader : ClassRegistry.createInstances(ProfileLoader.class)) {
					profile = loader.load(domain, uri);
					if (profile != null)
						break;
				}

				if (profile == null) {
					profile = (Profile) resourceSet.getEObject(uri, true);
				}

				if (profile == null) {
					LogUtil.error("could not resolve '" + uriStr + "' into a profile");
					continue;
				}
				Plot plot = createPlot(profile);
				plots.add(plot);
			}

			Command command = AddCommand.create(domain, chart, ChartPackage.Literals.CHART__PLOTS, plots);
			Timeline<?> timeline = TimelineUtils.getTimeline(ChartDropEditPolicy.this);

			// SPF-4406 Timeline jumps horizontally when adding conditions
			if (timeline != null) {
				Date oldCenterTime = timeline.getCurrentScreenCenterDate();
				EMFUtils.executeCommand(domain, command);
				timeline.centerOnTime(oldCenterTime);
			} else {
				EMFUtils.executeCommand(domain, command);
			}

		}

		@Override
		public boolean canExecute() {
			return true;
		}

		@Override
		public boolean canUndo() {
			return false;
		}

	}

}
