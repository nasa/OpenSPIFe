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
/*
 * Created on Mar 15, 2005
 */
package gov.nasa.arc.spife.core.plan.editor.timeline.parts;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineConstants;
import gov.nasa.arc.spife.core.plan.editor.timeline.models.TemporalBoundLink;
import gov.nasa.arc.spife.core.plan.editor.timeline.models.TemporalChainLink;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.HighlightUpdateEditPolicy;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.TemporalNodeDecoratorBorderEditPolicy;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.TemporalNodeDecoratorColorEditPolicy;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.TemporalNodeDecoratorTextEditPolicy;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.TemporalNodeDropEditPolicy;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.TemporalNodeHoverEditPolicy;
import gov.nasa.arc.spife.timeline.model.FigureStyle;
import gov.nasa.arc.spife.timeline.model.Page;
import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.figure.BarFigure;
import gov.nasa.arc.spife.ui.timeline.part.TimepointConnectionAnchor;
import gov.nasa.arc.spife.ui.timeline.part.TreeTimelineNodeEditPart;
import gov.nasa.arc.spife.ui.timeline.preference.TimelinePreferencePage;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.TriState;
import gov.nasa.ensemble.common.ui.gef.DropEditPolicy;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.constraints.BinaryTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.parameters.SpifePlanUtils;
import gov.nasa.ensemble.emf.model.common.Timepoint;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.graphics.Color;

public class TemporalNodeEditPart extends TreeTimelineNodeEditPart<EPlanElement> implements NodeEditPart, PlanTimelineConstants {

	private final Listener listener = new Listener();
	private TemporalMember temporalMember;
	private AdapterFactory adapterFactory = null;

	public TemporalNodeEditPart() {
		// default constructor
	}

	@Override
	public void setModel(Object model) {
		super.setModel(model);
		temporalMember = getModel().getMember(TemporalMember.class);
	}

	@Override
	protected FigureStyle getDefaultBarFigureStyle() {
		if (getModel() instanceof EActivityGroup) {
			return FigureStyle.IBAR;
		}
		return super.getDefaultBarFigureStyle();
	}

	/**
	 * The temporal extent of this TemporalNodeEditPart.
	 *
	 * @return the TemporalExtent relating to this TemporalNodeEditPart.
	 */
	@Override
	public TemporalExtent getTemporalExtent() {
		return temporalMember.getExtent();
	}

	private TreeTimelineContentProvider getTreeTimelineContentProvider() {
		if (getViewer() == null) {
			return null;
		}
		return getViewer().getTreeTimelineContentProvider();
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(DropEditPolicy.DROP_ROLE, new TemporalNodeDropEditPolicy());
		installEditPolicy(TemporalNodeDecoratorTextEditPolicy.ROLE, new TemporalNodeDecoratorTextEditPolicy());
		installEditPolicy(TemporalNodeDecoratorColorEditPolicy.ROLE, new TemporalNodeDecoratorColorEditPolicy());
		installEditPolicy(TemporalNodeDecoratorBorderEditPolicy.ROLE, new TemporalNodeDecoratorBorderEditPolicy());
		if (getTimeline().getInfobarComposite() != null) {
			installEditPolicy(HighlightUpdateEditPolicy.ROLE, new HighlightUpdateEditPolicy());
		}
		installEditPolicy(TemporalNodeHoverEditPolicy.ROLE, new TemporalNodeHoverEditPolicy());
	}

	/**
	 * Upon activation, attach to the model element as a property change listener.
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			getTimeline().addPropertyChangeListener(listener);
			getViewer().addPropertyChangeListener(listener);
			EPlanElement pe = getModel();
			TemporalMember tm = pe.getMember(TemporalMember.class);
			pe.eAdapters().add(listener);
			tm.eAdapters().add(listener);
			ConstraintsMember cm = pe.getMember(ConstraintsMember.class, false);
			if (cm != null) {
				cm.eAdapters().add(listener);
			}
		}
		updateFigureBounds();
	}

	/**
	 * Upon deactivation, detach from the model element as a property change listener.
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			getTimeline().removePropertyChangeListener(listener);
			getViewer().removePropertyChangeListener(listener);
			EPlanElement pe = getModel();
			TemporalMember tm = pe.getMember(TemporalMember.class);
			pe.eAdapters().remove(listener);
			tm.eAdapters().remove(listener);
			ConstraintsMember cm = pe.getMember(ConstraintsMember.class, false);
			if (cm != null) {
				cm.eAdapters().remove(listener);
			}
			super.deactivate();
		}
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		Object model = connection.getModel();
		if (model instanceof BinaryTemporalConstraint) {
			BinaryTemporalConstraint r = (BinaryTemporalConstraint) model;
			return new TimepointConnectionAnchor(r.getPointA().getEndpoint(), getFigure());
		} else
		if (model instanceof TemporalChainLink) {
			return new TimepointConnectionAnchor(Timepoint.END, getFigure());
		} else
		if (model instanceof TemporalBoundLink) {
			PeriodicTemporalConstraint b = ((TemporalBoundLink)model).getTarget();
			return new TimepointConnectionAnchor(b.getPoint().getEndpoint(), getFigure());
		}
		return null;
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return null;
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		Object model = connection.getModel();
		if (model instanceof BinaryTemporalConstraint) {
			BinaryTemporalConstraint r = (BinaryTemporalConstraint) model;
			return new TimepointConnectionAnchor(r.getPointB().getEndpoint(), getFigure());
		} else
		if (model instanceof TemporalChainLink) {
			return new TimepointConnectionAnchor(Timepoint.START, getFigure());
		}
		return null;
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return null;
	}

	/**
	 * Returns a <code>List</code> containing the children
	 * model objects. If this EditPart's model is a container, this method should be
	 * overridden to returns its children. This is what causes children EditParts to be
	 * created.
	 * <P>
	 * Callers must not modify the returned List. Must not return <code>null</code>.
	 * @return the List of children
	 */
	@Override
	protected List<?> getModelChildren() {
		return Collections.emptyList();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected List getModelSourceConnections() {
		if (getBoolean(PLAN_CONSTRAINTS_VISIBLE)) {
			List connections = getSourceTemporalRelations();
			TemporalChainLink link = getSourceTemporalChainLink();
			if (link != null) {
				Map<Object, EditPart> editPartRegistry = this.getViewer().getEditPartRegistry();
				if (link != null && editPartRegistry.get(link.getTarget()) != null) {
					connections.add(link);
				}
			}
			connections.addAll(getSourceTemporalBoundLinks());
			return connections;
		}
		return Collections.emptyList();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected List getModelTargetConnections() {
		if (getBoolean(PLAN_CONSTRAINTS_VISIBLE)) {
			List connections = getTargetTemporalRelations();
			TemporalChainLink link = getTargetTemporalChainLink();
			if (link != null) {
				Map<Object, EditPart> editPartRegistry = this.getViewer().getEditPartRegistry();
				if (link != null && editPartRegistry.get(link.getSource()) != null) {
					connections.add(link);
				}
			}
			return connections;
		}
		return Collections.emptyList();
	}

	/**
	 * Source {@link TemporalChainLink} is defined as the
	 * {@link TemporalChainLink} to the next {@link EPlanElement}
	 * defined in the {@link TemporalChain}
	 * @return
	 */
	private TemporalChainLink getSourceTemporalChainLink() {
		EPlanElement element = getModel();
		ConstraintsMember member = element.getMember(ConstraintsMember.class, false);
		if (member != null) {
			TemporalChain chain = member.getChain();
			if (chain != null) {
				int index = chain.getElements().indexOf(element);
				List<EPlanElement> elements = chain.getElements();
				if (index < elements.size() - 1) {
					return new TemporalChainLink(element, elements.get(++index));
				}
			}
		}
		return null;
	}

	/**
	 * Source {@link TemporalChainLink} is defined as the
	 * {@link TemporalChainLink} to the previous {@link EPlanElement}
	 * defined in the {@link TemporalChain}
	 * @return
	 */
	private TemporalChainLink getTargetTemporalChainLink() {
		EPlanElement element = getModel();
		ConstraintsMember member = element.getMember(ConstraintsMember.class, false);
		if (member != null) {
			TemporalChain chain = member.getChain();
			if (chain != null) {
				int index = chain.getElements().indexOf(element);
				List<EPlanElement> elements = chain.getElements();
				if (index > 0) {
					return new TemporalChainLink(elements.get(--index), element);
				}
			}
		}
		return null;
	}

	/**
	 * Source {@link BinaryTemporalConstraint} is defined as any
	 * temporal relations that has as its PlanElementA
	 * the model {@link EPlanElement}
	 * @return
	 */
	private List<?> getSourceTemporalRelations() {
		EPlanElement element = getModel();
		int depth = element.getDepth();
		List<BinaryTemporalConstraint> constraints = new ArrayList<BinaryTemporalConstraint>();
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
		if (constraintsMember != null) {
			for (BinaryTemporalConstraint constraint : constraintsMember.getBinaryTemporalConstraints()) {
				EPlanElement elementA = constraint.getPointA().getElement();
				EPlanElement elementB = constraint.getPointB().getElement();
				if ((elementA == element) && (elementB != null) && (elementB.getDepth() == depth)) {
					constraints.add(constraint);
				}
			}
		}
		return constraints;
	}

	/**
	 * Target {@link BinaryTemporalConstraint} is defined as any
	 * temporal relations that has as its PlanElementB
	 * the model {@link EPlanElement}
	 * @return
	 */
	private List<?> getTargetTemporalRelations() {
		EPlanElement element = getModel();
		int depth = element.getDepth();
		List<BinaryTemporalConstraint> constraints = new ArrayList<BinaryTemporalConstraint>();
		ConstraintsMember constraintsMember = element.getMember(ConstraintsMember.class, false);
		if (constraintsMember != null) {
			for (BinaryTemporalConstraint constraint : constraintsMember.getBinaryTemporalConstraints()) {
				EPlanElement elementA = constraint.getPointA().getElement();
				EPlanElement elementB = constraint.getPointB().getElement();
				if ((elementB == element) && (elementA != null) && (elementA.getDepth() == depth)) {
					constraints.add(constraint);
				}
			}
		}
		return constraints;
	}

	private List<?> getSourceTemporalBoundLinks() {
		EPlanElement ePlanElement = getModel();
		List<TemporalBoundLink> links = new ArrayList<TemporalBoundLink>();
		for (PeriodicTemporalConstraint b : ConstraintUtils.getPeriodicConstraints(ePlanElement, false)) {
			links.add(new TemporalBoundLink(ePlanElement, b));
		}
		return links;
	}

	/*
	 * NOTE: It is very important that this method does not
	 * become a 'catch-all' for every conceivable visual update.
	 * The methodology employed should check the origin of the
	 * event, decode it into the relevant info for the figure,
	 * invalidate *ONLY THE RELEVANT FIGURE BOUNDS* that correspond
	 * to the change, and then call refresh which repaints according
	 * to the invalid areas.
	 */
	@Override
	protected void refreshVisuals() {
		figure.repaint();
	}

	@Override
	protected IFigure createFigure() {
		BarFigure f = (BarFigure) super.createFigure();
		boolean scheduled = SpifePlanUtils.getScheduled(getModel()) != TriState.FALSE;
		f.setFigureStyle(scheduled ? getDefaultBarFigureStyle() : FigureStyle.PATTERN);
		return f;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void updateChildBounds() {
		Page page = getPage();
		if (page == null) {
			return; // ignore this update
		}
//		TransactionUtils.reading(getModel(), new RunnableWithResult.Impl<TemporalExtent>() {
//			public void run() {
				Date parentStart = temporalMember.getStartTime();
				if (parentStart == null) {
					return;  // ignore this update
				}
				List childFigures = new ArrayList<IFigure>(figure.getChildren());
				for (IFigure childFigure : (List<IFigure>)childFigures) {
					figure.remove(childFigure);
				}
				boolean showActivityGroupsNoGaps = TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_ROW_ELEMENT_ACTIVITY_GROUP_NOGAPS);
				for (EPlanElement child : getModel().getChildren()) {
					if (child.getMember(CommonMember.class).isVisible()) {
						// Displays the full duration of the parent Activity Group, even if there are gaps in between the child activities.
						TemporalMember member = (showActivityGroupsNoGaps) ? temporalMember : child.getMember(TemporalMember.class);
						TemporalExtent extent = member.getExtent();
						if (extent != null) {
							boolean scheduled = member.getScheduled() == Boolean.TRUE;
							int left = (int) getPage().convertToPixels(extent.getStart().getTime() - parentStart.getTime());
							int width = (int) getPage().convertToPixels(extent.getDurationMillis());
							BarFigure childFigure = new BarFigure();
							Color color = getCastedFigure().getNormalColorPalette();
							if (color == null) {
								color = ColorConstants.lightGray;
							}
							childFigure.setNormalColorPalette(color);
							childFigure.setFigureStyle(scheduled ? FigureStyle.SOLID : FigureStyle.PATTERN);
							childFigure.setRound(TIMELINE_PREFERENCES.getBoolean(TimelinePreferencePage.P_ROW_ELEMENT_ROUNDED));
							figure.add(childFigure);
							int rowElementHeight = TimelineUtils.getRowElementHeight(TemporalNodeEditPart.this);
							figure.setConstraint(childFigure, new Rectangle(left,0,width,rowElementHeight));
						}
					}
				}
//			}
//		});
		refreshVisuals();
	}
	
	protected List<EPlanElement> getAllChildren(EPlanElement parent) {
		List<EPlanElement> children = new ArrayList<EPlanElement>();
		TreeTimelineContentProvider cp = getTreeTimelineContentProvider();
		if (cp != null) {
			for (Object child : cp.getChildren(parent)) {
				if (child instanceof EPlanElement) {
					EPlanElement pe = (EPlanElement) child;
					if (pe instanceof EActivity) {
						children.add(pe);
					}
					children.addAll(getAllChildren(pe));
				}
			}
		}
		return children;
	}

	@SuppressWarnings("unchecked")
	public <A> A adapt(Class<A> type) {
		return (A) getAdapter(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class type) {
		AdapterFactory adapterFactory = getAdapterFactory();
		Object result = adapterFactory == null ? null : adapterFactory.adapt(getModel(), type);
		if (result == null) {
			result = super.getAdapter(type);
		}
		return result;
	}

	public AdapterFactory getAdapterFactory() {
		if (adapterFactory == null) {
			adapterFactory = EMFUtils.getAdapterFactory(getModel());
		}
		return adapterFactory;
	}

	public class Listener extends AdapterImpl implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (PLAN_CONSTRAINTS_VISIBLE == evt.getPropertyName()) {
				refreshInDisplayThread();
			} else if (Timeline.PROPERTY_HORIZONTAL_SCROLLING_ACTIVE == evt.getPropertyName()) {
				Timeline timeline = getTimeline();
				if (timeline != null) {
					getCastedFigure().setRenderSimply(timeline.isScrolling());
				}
				refreshInDisplayThread();
			}
		}

		@Override
		public void notifyChanged(Notification notification) {
			Object f = notification.getFeature();
			//
			// Name change for tooltip
			if (PlanPackage.Literals.EPLAN_ELEMENT__NAME == f)
			{
				refreshVisualsInDisplayThread();
			}
			//
			// Scheduled state changed
			else if (TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED == f)
			{
				BarFigure figure = getCastedFigure();
				TemporalMember member = getModel().getMember(TemporalMember.class);
				if (member.getScheduled() != Boolean.FALSE) {
					figure.setFigureStyle(getDefaultBarFigureStyle());
				} else {
					figure.setFigureStyle(FigureStyle.PATTERN);
				}
				refreshInDisplayThread();
			}
			//
			// Constraint connections
			else if (ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__CHAIN == f
					|| ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS == f
					|| ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__BINARY_TEMPORAL_CONSTRAINTS == f) {
				refreshInDisplayThread();
			}
			//
			// Temporal change
			else if (notification.getNotifier() instanceof TemporalMember) {
				if (notification.getEventType() != Notification.REMOVING_ADAPTER) {
					GEFUtils.runInDisplayThread(TemporalNodeEditPart.this, new Runnable() {
						@Override
						public void run() {
							updateFigureBounds();
						}
					});
				}
			}
		}
		
	}
	
}
