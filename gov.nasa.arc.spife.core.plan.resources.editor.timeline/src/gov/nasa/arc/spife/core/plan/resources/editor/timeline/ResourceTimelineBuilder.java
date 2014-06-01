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
package gov.nasa.arc.spife.core.plan.resources.editor.timeline;

import gov.nasa.arc.spife.core.plan.editor.timeline.AbstractPlanTimelineBuilder;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.chart.ChartTimelineBuilder;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartFactory;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle;
import gov.nasa.arc.spife.ui.timeline.chart.model.LineChart;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.ensemble.common.extension.ClassRegistryFactory;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.translator.WrapperUtils;
import gov.nasa.ensemble.core.plan.resources.profile.ResourceProfileMember;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ResourceTimelineBuilder extends AbstractPlanTimelineBuilder {
	
	public static final String ID_RESOURCE_GRAPH = "Resource Graph";
	public static final String ID_HEAT_MAP = "Heat Map";
	
	private static final ImageDescriptor IMAGE_DESCRIPTOR_ACTION = AbstractUIPlugin.imageDescriptorFromPlugin(TimelinePlugin.PLUGIN_ID, "icons/new_resource_timeline.gif");
	private static final ImageDescriptor IMAGE_DESCRIPTOR_HEAT_MAP = AbstractUIPlugin.imageDescriptorFromPlugin(TimelinePlugin.PLUGIN_ID, "icons/resource_profile_part_heat_map.png");
		
	private final Chart chart;
	
	public ResourceTimelineBuilder(Chart chart) {
		super();
		this.chart = chart;
	}

	@Override
	public Section createTimelineSection() {
		return EMFUtils.copy(this.chart);
	}
	
	@Override
	public ImageDescriptor getActionImageDescriptor() {
		switch (chart.getStyle()) {
		case HEAT_MAP:
			return IMAGE_DESCRIPTOR_HEAT_MAP;
		default:
			return IMAGE_DESCRIPTOR_ACTION;
		}
	}
	
	@Override
	public String getName() {
		return chart.getName();
	}

	@Override
	public TimelineViewer build(PlanTimeline timeline, Section section) {
		EPlan plan = timeline.getPlan();
		final Chart chart = EMFUtils.copy(this.chart);
		populatePlots(plan, chart);
		return ChartTimelineBuilder.getInstance().build(timeline, chart);
	}

	private static void populatePlots(EPlan plan, final Chart chart) {
		ResourceProfileMember member = WrapperUtils.getMember(plan, ResourceProfileMember.class);
		Map<String, Profile<?>> profilesByName = new HashMap<String, Profile<?>>();
		for (Profile<?> p : member.getResourceProfiles()){
			profilesByName.put(p.getId(), p);
		}
		
		final Set<Plot> unusedPlots = new HashSet<Plot>();
		for (Plot plot : chart.getPlots()) {
			String definitionName = plot.getName();
			Profile profile = plot.getProfile();
			if (profile == null) {
				profile = profilesByName.get(definitionName);
				if (profile == null) {
					unusedPlots.add(plot);
					continue;
				} else {
					plot.setProfile(profile);
				}
			}
		}
		TransactionUtils.writing(chart, new Runnable() {
			@Override
			public void run() {
				chart.getPlots().removeAll(unusedPlots);
			}
		});
	}
	
	public static class Factory implements ClassRegistryFactory<AbstractPlanTimelineBuilder> {

		@Override
		public List<AbstractPlanTimelineBuilder> createInstances() {
			List<AbstractPlanTimelineBuilder> builders = new ArrayList<AbstractPlanTimelineBuilder>();
			addDefaultChartBuilder(builders);
			addHeatMapChartBuilder(builders);
			return builders;
		}
		
		private void addHeatMapChartBuilder(List<AbstractPlanTimelineBuilder> builders) {
			Chart heatMapChart = ChartFactory.eINSTANCE.createChart();
			heatMapChart.setName(ID_HEAT_MAP);
			heatMapChart.setStyle(ChartStyle.HEAT_MAP);
			builders.add(new ResourceTimelineBuilder(heatMapChart));
		}
		
		private void addDefaultChartBuilder(List<AbstractPlanTimelineBuilder> builders) {
			LineChart defaultChart = ChartFactory.eINSTANCE.createLineChart();
			defaultChart.setName(ID_RESOURCE_GRAPH);
			builders.add(new ResourceTimelineBuilder(defaultChart));
		}
		
	}
	
}
