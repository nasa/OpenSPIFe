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
package gov.nasa.arc.spife.core.plan.editor.timeline.parts;

import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimeline;
import gov.nasa.arc.spife.core.plan.editor.timeline.PlanTimelineConstants;
import gov.nasa.arc.spife.core.plan.editor.timeline.policies.HighlightUpdateEditPolicy;
import gov.nasa.arc.spife.timeline.provider.TreeTimelineContentProvider;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsPackage;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;
import gov.nasa.ensemble.emf.util.EMFUtils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class PlanTimelineDataRowEditPart extends PlanTreeTimelineDataRowEditPart<EPlanElement> implements PlanTimelineConstants {

	private Listener listener = new Listener();
	
	private TransactionalEditingDomain domain;

	private AdapterFactory adapterFactory = null;
	
	@Override
	public void activate() {
		PlanTimeline timeline = (PlanTimeline) getViewer().getTimeline();
		domain = timeline.getEditingDomain();
		if (domain != null) { 
			domain.addResourceSetListener(listener);
		}
		getViewer().addPropertyChangeListener(listener);
		super.activate();
	}

	@Override
	public void deactivate() {
		if (domain != null) {
			domain.removeResourceSetListener(listener);
			domain = null;
		}
		getViewer().removePropertyChangeListener(listener);
		super.deactivate();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected List getModelChildren() {
		List list = new ArrayList();
		EPlanElement ePlanElement = getModel();
		if (ePlanElement != getViewer().getTimeline().getModel() 
				&& ePlanElement.getMember(CommonMember.class).isVisible()) {
			if (getBoolean(TIMELINE_GROUP_ELEMENTS)) {
				list.add(ePlanElement);
				if (getBoolean(PLAN_CONSTRAINTS_VISIBLE)) {
					ConstraintsMember member = ePlanElement.getMember(ConstraintsMember.class, false);
					if (member != null) {
						list.addAll(member.getPeriodicTemporalConstraints());
					}
				}
			} else {
				TreeTimelineContentProvider cp = getViewer().getTreeTimelineContentProvider();
				for (Object child : cp.getChildren(ePlanElement)) {
					EPlanElement e = (EPlanElement) child;
					list.add(e);
					if (getBoolean(PLAN_CONSTRAINTS_VISIBLE)) {
						ConstraintsMember member = e.getMember(ConstraintsMember.class, false);
						if (member != null) {
							list.addAll(member.getPeriodicTemporalConstraints());
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		if (getTimeline().getInfobarComposite() != null) {
			installEditPolicy(HighlightUpdateEditPolicy.ROLE, new HighlightUpdateEditPolicy());
		}
	}

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
	
	private class Listener extends PostCommitListener implements PropertyChangeListener {

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			boolean refresh = false;
			boolean refreshChildren = false;
			for (Notification notification : event.getNotifications()) {
				if (notification.getFeature() == ConstraintsPackage.Literals.CONSTRAINTS_MEMBER__PERIODIC_TEMPORAL_CONSTRAINTS) {
					refresh = true;
				}
				if (notification.getFeature() == PlanPackage.Literals.COMMON_MEMBER__VISIBLE) {
					refreshChildren = true;
				}
				if (refresh) {
					break;
				}
			}
			if (refresh || refreshChildren) {
				final boolean doRefresh = refresh;
				final boolean doRefreshChildren = refreshChildren;
				GEFUtils.runInDisplayThread(PlanTimelineDataRowEditPart.this, new Runnable() {
					@Override
					public void run() {
						if (doRefresh) {
							refresh();
						} else if (doRefreshChildren) {
							refreshChildren();
						}
					}
				});
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			String p = evt.getPropertyName();
			if (PLAN_CONSTRAINTS_VISIBLE.equals(p)) {
				refreshChildrenInDisplayThread();
			}
		}
		
	}

}
