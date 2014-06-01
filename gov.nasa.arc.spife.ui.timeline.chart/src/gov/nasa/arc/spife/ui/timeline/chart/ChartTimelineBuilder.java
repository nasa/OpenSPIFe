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
package gov.nasa.arc.spife.ui.timeline.chart;

import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.TimelineBuilder;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineToolBarContributor;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartPackage;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle;
import gov.nasa.arc.spife.ui.timeline.chart.model.provider.ChartItemProviderAdapterFactory;
import gov.nasa.arc.spife.ui.timeline.chart.part.HeatMapChartTimelineEditPartFactory;
import gov.nasa.arc.spife.ui.timeline.chart.part.LineChartTimelineFactory;
import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ChartTimelineBuilder extends TimelineBuilder<Chart> {

	private static final ChartTimelineBuilder INSTANCE = new ChartTimelineBuilder();
	
	public static ChartTimelineBuilder getInstance() {
		return INSTANCE;
	}

	@Override
	public TimelineViewer build(Timeline timeline, Chart chart) {
		final EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(chart);
		AdapterFactory factory = new ChartItemProviderAdapterFactory();//.getAdapterFactory(chart);
		AdapterFactoryLabelProvider labeler = new AdapterFactoryLabelProvider(factory);
		TimelineViewer viewer = new TimelineViewer(timeline);
		ChartStyle chartStyle = chart.getStyle();
		viewer.setTimelineSectionModel(chart);
		viewer.setImageDescriptor(ImageDescriptor.createFromImage(labeler.getImage(chart)));
		viewer.setLabelProvider(labeler);
		switch (chartStyle) {
		case HEAT_MAP:
			viewer.setTreeTimelineContentProvider(new ChartTimelineContentProvider());
			viewer.setEditPartFactory(new HeatMapChartTimelineEditPartFactory());
			viewer.setProperty(TimelineConstants.TIMELINE_GROUP_ELEMENTS, true);
			break;
		case LINE:
			viewer.setEditPartFactory(new LineChartTimelineFactory());
			break;
		}
		viewer.setTimelineToolBarContributionItem(new ChartTimelineToolBarContributor(timeline, chart, domain));
		viewer.setContents(chart);
		return viewer;
	}

	@Override
	public String getName() {
		return "Chart";
	}
	
	private class ChartTimelineToolBarContributor extends TimelineToolBarContributor  {

		private final Timeline timeline;
		private final Chart chart;
		private final EditingDomain domain;
		
		public ChartTimelineToolBarContributor(Timeline timeline, Chart chart, EditingDomain domain) {
			super();
			this.timeline = timeline;
			this.chart = chart;
			this.domain = domain;
		}

		@Override
		public void fill(Composite toolbar) {
			if (ChartStyle.HEAT_MAP == chart.getStyle()) {
				return;
			}
			
			ImageRegistry registry = WidgetPlugin.getDefault().getImageRegistry();
			
			Label smaller = new Label(toolbar, SWT.NONE);
			smaller.setImage(registry.get(WidgetPlugin.KEY_IMAGE_MINUS));
			smaller.addMouseListener(new MouseListener() {
				@Override
				public void mouseDoubleClick(MouseEvent e) 	{/* no impl */}
				@Override
				public void mouseDown(MouseEvent e) 		{/* no impl */}
				@Override
				public void mouseUp(MouseEvent e) 			{
					int minHeight = getMinimumHeight();
					
					int height = chart.getMinimumHeight();
					height -= 20;
					height = Math.max(minHeight, height);
					if (chart.getMinimumHeight() != height) {
						Command cmd = SetCommand.create(domain, chart, ChartPackage.Literals.CHART__MINIMUM_HEIGHT, height);
						EMFUtils.executeCommand(domain, cmd);
						timeline.layoutTimelineContentInDisplayThread();
					}
				}
			});
			
			Label bigger = new Label(toolbar, SWT.NONE);
			bigger.setImage(registry.get(WidgetPlugin.KEY_IMAGE_PLUS));
			bigger.addMouseListener(new MouseListener() {
				@Override
				public void mouseDoubleClick(MouseEvent e) 	{/* no impl */}
				@Override
				public void mouseDown(MouseEvent e) 		{/* no impl */}
				@Override
				public void mouseUp(MouseEvent e) 			{
					int minHeight = getMinimumHeight();
					
					int height = chart.getMinimumHeight();
					height += 20;
					height = Math.max(minHeight, height);
					if (chart.getMinimumHeight() != height) {
						Command cmd = SetCommand.create(domain, chart, ChartPackage.Literals.CHART__MINIMUM_HEIGHT, height);
						EMFUtils.executeCommand(domain, cmd);
						timeline.layoutTimelineContentInDisplayThread();
					}
				}
			});
		}
		
		private int getMinimumHeight() {
			GraphicalEditPart headerEditPart = getTimelineViewer().getTimelineEditPart().getHeaderEditPart();
			GraphicalEditPart dataEditPart = getTimelineViewer().getTimelineEditPart().getDataEditPart();
			Dimension minHeaderSize = headerEditPart.getFigure().getLayoutManager().getMinimumSize(headerEditPart.getFigure(), -1, -1);
			Dimension minDataSize = dataEditPart.getFigure().getLayoutManager().getMinimumSize(dataEditPart.getFigure(), -1, -1);
			int minHeight = Math.min(minHeaderSize.height, minDataSize.height);
			return minHeight;
		}
		
	}
	
	

}
