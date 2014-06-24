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
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanTimelineDataEditPart;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanTimelineDataRowEditPart;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.PlanTimelineEditPartFactory;
import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.RowDataDropEditPolicy;
import gov.nasa.arc.spife.core.plan.timeline.PlanTimelineContentProvider;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.model.Section;
import gov.nasa.arc.spife.ui.timeline.TimelineConstants;
import gov.nasa.arc.spife.ui.timeline.TimelineViewer;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanFactory;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;

public class ContainersTimelineBuilder extends AbstractPlanTimelineBuilder {

	@Override
	public TimelineViewer build(PlanTimeline timeline, Section section) {
		EPlan plan = timeline.getPlan();
		PlanTimelineContentProvider defaultContentProvider = getPlanTimelineContentProvider(plan);
		defaultContentProvider.activate();

		PlanTimelineViewer viewer = new PlanTimelineViewer(timeline);
		viewer.setTreeTimelineContentProvider(defaultContentProvider);
		viewer.setEditPartFactory(new ContainersTimelineFactory());
		viewer.setTimelineSectionModel(section);
		viewer.setProperty(TimelineConstants.TIMELINE_GROUP_ELEMENTS, false);
		viewer.setContents(plan);
		return viewer;
	}

	protected PlanTimelineContentProvider getPlanTimelineContentProvider(
			EPlan plan) {
		return new ContainersTimelineContentProvider(plan);
	}

	@Override
	public String getName() {
		return "Containers";
	}

	private static class ContainersTimelineFactory extends
			PlanTimelineEditPartFactory {

		@Override
		public EditPart createEditPart(EditPart context, Object model) {
			if (context instanceof PlanTimelineDataRowEditPart
					&& model instanceof EPlanElement) {
				EditPart editPart = new TemporalNodeEditPart() {

					@Override
					protected List<EPlanElement> getAllChildren(
							EPlanElement parent) {
						return new ArrayList<EPlanElement>(parent.getChildren());
					}

				};
				editPart.setModel(model);
				return editPart;
			} else if (context instanceof PlanTimelineDataEditPart
					&& model instanceof EPlanElement) {
				EditPart editPart = new PlanTimelineDataRowEditPart() {

					@Override
					protected void createEditPolicies() {
						super.createEditPolicies();
						installEditPolicy(DropEditPolicy.DROP_ROLE,
								new RowDataDropEditPolicy() {

									@Override
									protected EPlanElement getTargetNode() {
										return ((PlanTimeline) getTimeline())
												.getPlanEditorModel()
												.getEPlan();
									}

								});
					}
				};
				editPart.setModel(model);
				return editPart;
			}
			return super.createEditPart(context, model);
		}

	}

	public static class ContainersTimelineContentProvider extends
			PlanTimelineContentProvider {

		protected static final EPlanElement GROUPING_CONTAINERS = PlanFactory.eINSTANCE
				.createEActivity();
		private TransactionalEditingDomain domain;
		private Listener listener = new Listener();

		public ContainersTimelineContentProvider(EPlan ePlan) {
			super(ePlan);
			GROUPING_CONTAINERS.setName(getContainerName());
		}

		protected String getContainerName() {
			return "Containers";
		}

		@Override
		public void activate() {
			domain = TransactionUtils.getDomain(getPlan());
			domain.addResourceSetListener(listener);
		}

		@Override
		public void dispose() {
			domain = TransactionUtils.getDomain(getPlan());
			domain.removeResourceSetListener(listener);
		}

		@Override
		public Collection<?> getChildren(Object object) {
			final List<EPlanElement> children = new ArrayList<EPlanElement>();
			if (GROUPING_CONTAINERS == object) {
				new PlanVisitor(false) {
					@Override
					protected void visit(EPlanElement element) {
						if (element instanceof EActivityGroup) {
							children.add(element);
						}
					}
				}.visitAll(getPlan());
			}
			return children;
		}

		@Override
		public Collection<?> getElements(Object object) {
			return Arrays.asList(new EPlanElement[] { GROUPING_CONTAINERS });
		}

		@Override
		public String getName() {
			return "Containers";
		}

		private class Listener extends PostCommitListener {
			@Override
			public void resourceSetChanged(ResourceSetChangeEvent event) {
				Set<Object> contentForRefresh = new LinkedHashSet<Object>();
				for (Notification notification : event.getNotifications()) {
					if (notification.getNotifier() instanceof Page) {
						continue;
					}
					boolean added = !EMFUtils.getAddedObjects(notification,
							EPlanElement.class).isEmpty();
					boolean removed = !EMFUtils.getRemovedObjects(notification,
							EPlanElement.class).isEmpty();
					if (added || removed) {
						contentForRefresh.add(GROUPING_CONTAINERS);
						break;
					}
				}
				if (!contentForRefresh.isEmpty()) {
					refreshContents(contentForRefresh);
				}
			}
		}

	}

}
