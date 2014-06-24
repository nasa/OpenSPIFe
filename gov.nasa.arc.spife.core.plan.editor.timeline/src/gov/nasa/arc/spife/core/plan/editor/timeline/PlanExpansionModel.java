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
package gov.nasa.arc.spife.core.plan.editor.timeline;

import gov.nasa.arc.spife.ui.timeline.model.ExpansionModel;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.plan.editor.SetExpandedOperation;
import gov.nasa.ensemble.emf.transaction.PostCommitListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.TransactionalEditingDomain;

public class PlanExpansionModel extends ExpansionModel {
	
	private TransactionalEditingDomain domain;
	private PostCommitListener listener = new FireExpandedListener();
	
	public PlanExpansionModel(PlanTimeline timeline) {
		this(timeline.getEditingDomain());
	}
	
	public PlanExpansionModel(TransactionalEditingDomain domain) {
		this.domain = domain;
		domain.addResourceSetListener(listener);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (domain != null) {
			domain.removeResourceSetListener(listener);
			domain = null;
		}
	}
	
	/**
	 * Delegate to the plan element when it is the object in question
	 */
	@Override
	public boolean isExpanded(Object pe) {
		if (pe instanceof EPlanElement) {
			EPlanElement element = (EPlanElement) pe;
			CommonMember member = element.getMember(CommonMember.class);
			return member.isExpanded();
		}
		return super.isExpanded(pe);
	}
	
	/**
	 * Delegate to the plan element when it is the object in question
	 */
	@Override
	public void setExpanded(Collection elements, boolean expanded) {
		List<EPlanChild> planChildren = new ArrayList<EPlanChild>();
		for (Object element : elements) {
			if (element instanceof EPlanChild) { // no need to "expand" the Plan
				planChildren.add((EPlanChild)element);
			}
		}
		if (!planChildren.isEmpty()) {
			SetExpandedOperation op = new SetExpandedOperation(planChildren, expanded, "expanding");
			op.execute(new NullProgressMonitor(), null);
		}
		super.setExpanded(planChildren, expanded);
	}
	
	protected final class FireExpandedListener extends PostCommitListener {

		@Override
		public void resourceSetChanged(ResourceSetChangeEvent event) {
			List<Notification> notifications = event.getNotifications();
			Boolean wasExpanded = null;
			for (Notification notification : notifications) {
				Object feature = notification.getFeature();
				Object newValue = notification.getNewValue();
				if ((feature == PlanPackage.Literals.COMMON_MEMBER__EXPANDED) && (newValue instanceof Boolean)) {
					wasExpanded = (Boolean) newValue;
				}
			}
			if (wasExpanded == null) {
				return;
			} else if (wasExpanded) {
				firePropertyChange(EXPANDED, false, true);
			} else {
				firePropertyChange(EXPANDED, true, false);
			}
		}
		
	}
	
}
