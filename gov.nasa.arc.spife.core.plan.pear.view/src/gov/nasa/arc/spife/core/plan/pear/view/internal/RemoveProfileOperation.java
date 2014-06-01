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
package gov.nasa.arc.spife.core.plan.pear.view.internal;

import gov.nasa.arc.spife.timeline.model.ETimeline;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.chart.model.Chart;
import gov.nasa.arc.spife.ui.timeline.chart.model.Plot;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;
import gov.nasa.ensemble.core.jscience.Profile;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;


public class RemoveProfileOperation extends AbstractProfileUndoableOperation {

	private List<Profile> profiles;
	private Map<Profile, List<Plot>> profileToPlotsMap = new HashMap<Profile, List<Plot>>();
	private Map<Plot, ChartPosition> plotToChartMap = new HashMap<Plot, ChartPosition>();
	
	public RemoveProfileOperation(EPlan plan, List<Profile> profiles) {
		super(plan, "Removing profiles");
		this.profiles = profiles;
		mapProfilesToPlots(plan, profiles);
	}
	
	private void mapProfilesToPlots(EPlan plan, List<Profile> profiles) {
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		IEditorPart editor = EditorPartUtils.getCurrent(PlatformUI.getWorkbench());
		Timeline timeline = TimelineUtils.getTimeline(editor);
		if (timeline != null) {
			ETimeline timelineModel = timeline.getTimelineModel();
			for (Section section : timelineModel.getContents()) {
				if (section instanceof Chart) {
					Chart chart = (Chart)section;
					int position = 0;
					for (Plot plot : chart.getPlots()) {
						Profile profile = plot.getProfile();
						if (profiles.contains(profile)) {
							List<Plot> plotList = profileToPlotsMap.get(profile);
							if (plotList == null) {
								plotList = new ArrayList<Plot>();
								profileToPlotsMap.put(profile, plotList);
							}
							plotList.add(plot);
							plotToChartMap.put(plot, new ChartPosition(chart, position));
						}
						position++;
					}
				}
			}
		}
	}

	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				for (Profile profile : profiles) {
					removeProfile(profile);
					List<Plot> plots = profileToPlotsMap.get(profile);
					if (plots != null) {
						for (Plot plot : plots) {
							Chart chart = plot.getChart();
							chart.getPlots().remove(plot);
						}
					}
				}
			}
		});
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				for (Profile profile : profiles) {
					addProfile(profile);
					List<Plot> plots = profileToPlotsMap.get(profile);
					if (plots != null) {
						for (Plot plot : plots) {
							ChartPosition chartPos = plotToChartMap.get(plot);
							Chart chart = chartPos.chart;
							int pos = chartPos.position;
							chart.getPlots().add(pos, plot);
						}
					}
				}
			}
		});
	}

	@Override
	public String toString() {
		return getLabel();
	}
	
	@Override
	protected void dispose(UndoableState state) {
		this.plan = null;
		if (profiles != null) {
			this.profiles.clear();
		}
		profileToPlotsMap.clear();
		this.resourceSet = null;
	}
	
	private static class ChartPosition {
		final Chart chart;
		final int position;
		
		ChartPosition(Chart chart, int pos) {
			this.chart = chart;
			this.position = pos;
		}
	}

}
