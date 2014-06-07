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
import gov.nasa.arc.spife.timeline.TimelinePackage;
import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartFactory;
import gov.nasa.arc.spife.ui.timeline.chart.model.ChartStyle;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collections;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public abstract class AddChartActionDelegate implements IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window = null;
	
	@Override
	public void run(IAction action) {
		Timeline<?> timeline = EditorPartUtils.getAdapter(window, Timeline.class);
		if (timeline != null) {
			ETimeline timelineModel = timeline.getTimelineModel();
			if (timelineModel != null) {
				EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(timelineModel);
				if (domain != null) {
					Chart chart = createChart();
					Command command = AddCommand.create(domain, timelineModel, TimelinePackage.Literals.ETIMELINE__CONTENTS, Collections.singleton(chart));
					EMFUtils.executeCommand(domain, command);
				}
			}
		}
	}
	
	protected abstract Chart createChart();

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// don't care
	}

	@Override
	public void dispose() {
		window = null;
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}
	
	public static class AddLineChartActionDelegate extends AddChartActionDelegate {

		@Override
		protected Chart createChart() {
			return ChartFactory.eINSTANCE.createLineChart();
		}

	}
	
	public static class AddHeatMapChartActionDelegate extends AddChartActionDelegate {

		@Override
		protected Chart createChart() {
			Chart chart = ChartFactory.eINSTANCE.createChart();
			chart.setStyle(ChartStyle.HEAT_MAP);
			return chart;
		}

	}

}
