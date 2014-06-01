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
package gov.nasa.arc.spife.core.plan.editor.timeline.builder;

import gov.nasa.arc.spife.core.plan.editor.timeline.AbstractPlanTimelineBuilder;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineViewer;
import gov.nasa.arc.spife.core.plan.editor.timeline.models.CustomContentProvider;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanTimelineEditPartFactory;
import gov.nasa.arc.spife.core.plan.timeline.FeatureValueRow;
import gov.nasa.arc.spife.core.plan.timeline.PlanSection;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineFactory;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelinePackage;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineToolBarContributor;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.Collections;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPartSite;


public class CustomTimelineBuilder extends AbstractPlanTimelineBuilder {
	
	@Override
	public String getName() {
		return "Custom";
	}
	
	@Override
	public Section createTimelineSection() {
		PlanSection section = PlanTimelineFactory.eINSTANCE.createPlanSection();
		section.setName(getName());
		section.setShowUnreferecedRow(false);
		section.getRows().add(PlanTimelineFactory.eINSTANCE.createFeatureValueRow());
		return section;
	}
	
	@Override
	public TimelineViewer build(PlanTimeline timeline, final Section section) {
		EPlan plan = timeline.getPlan();
		CustomContentProvider contentProvider = new CustomContentProvider(plan, (PlanSection) section);
		contentProvider.activate();
		
		final PlanTimelineViewer viewer = new PlanTimelineViewer(timeline);
		viewer.setTreeTimelineContentProvider(contentProvider);
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(EMFUtils.getAdapterFactory(section)));
		viewer.setEditPartFactory(new PlanTimelineEditPartFactory());
		viewer.setTimelineSectionModel(section);
		viewer.setProperty(TimelineConstants.TIMELINE_GROUP_ELEMENTS, false);
		viewer.setContents(plan);
		viewer.setTimelineToolBarContributionItem(new CustomTimelineToolBarContributor(viewer, section));
		return viewer;
	}
	
	private static class CustomTimelineToolBarContributor extends TimelineToolBarContributor {

		private PlanTimelineViewer viewer;
		private Object section;

		public CustomTimelineToolBarContributor(PlanTimelineViewer viewer, Object section) {
			super();
			this.viewer = viewer;
			this.section = section;
		}

		@Override
		public void fill(Composite toolbar) {
			Label upLabel = new Label(toolbar, SWT.SHADOW_IN);
			upLabel.setImage(WidgetPlugin.getImageFromRegistry(WidgetPlugin.KEY_IMAGE_PLUS));
			upLabel.addMouseListener(new MouseListener() {
				@Override
				public void mouseDoubleClick(MouseEvent e) 	{/* no action */}
				@Override
				public void mouseDown(MouseEvent e) 		{/* no action */}
				@Override
				public void mouseUp(MouseEvent e) 			{
					FeatureValueRow row = PlanTimelineFactory.eINSTANCE.createFeatureValueRow();
					EditingDomain domain = viewer.getPlanEditorModel().getEditingDomain();
					Command command = AddCommand.create(domain, section, PlanTimelinePackage.Literals.PLAN_SECTION__ROWS, Collections.singleton(row));
					EMFUtils.executeCommand(domain, command);
					IWorkbenchPartSite site = viewer.getSite();
					if(site != null) {
						ISelectionProvider selectionProvider = site.getSelectionProvider();
						selectionProvider.setSelection(new StructuredSelection(row));
					}
					viewer.getTimeline().layoutTimelineContentInDisplayThread();
				}
			});
		}
		
	}

}
