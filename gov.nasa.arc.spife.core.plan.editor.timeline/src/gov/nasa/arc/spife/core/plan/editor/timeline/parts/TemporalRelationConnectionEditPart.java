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

import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Label;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Image;

public class TemporalRelationConnectionEditPart extends ConstraintConnectionEditPart {

	protected final Listener listener = new Listener(); 

	public TemporalRelationConnectionEditPart() {
		super();
	}

	public TemporalRelationConnectionEditPart(Image image) {
		super(image);
	}

	public TemporalRelationConnectionEditPart(Label label) {
		super(label);
	}
	
	@Override
	public void activate() {
		super.activate();
		EditPart sourceEditPart = getSource();
		if (sourceEditPart != null) {
			EPlanElement source = (EPlanElement) sourceEditPart.getModel();
			source.getMember(CommonMember.class).eAdapters().add(listener);
			source.getMember(TemporalMember.class).eAdapters().add(listener);
		}
		
		EditPart targetEditPart = getTarget();
		if (targetEditPart != null) {
			EPlanElement target = (EPlanElement) targetEditPart.getModel();
			target.getMember(CommonMember.class).eAdapters().add(listener);
			target.getMember(TemporalMember.class).eAdapters().add(listener);
		}
	}

	@Override
	public void deactivate() {
		EditPart sourceEditPart = getSource();
		if (sourceEditPart != null) {
			EPlanElement source = (EPlanElement) sourceEditPart.getModel();
			source.getMember(CommonMember.class).eAdapters().remove(listener);
			source.getMember(TemporalMember.class).eAdapters().remove(listener);
		}
		EditPart targetEditPart = getTarget();
		if (targetEditPart != null) {
			EPlanElement target = (EPlanElement) targetEditPart.getModel();
			target.getMember(CommonMember.class).eAdapters().remove(listener);
			target.getMember(TemporalMember.class).eAdapters().remove(listener);
		}
		super.deactivate();
	}

	private class Listener extends AdapterImpl {

		@Override
		public void notifyChanged(final Notification notification) {
			GEFUtils.runInDisplayThread(TemporalRelationConnectionEditPart.this, new Runnable() {
				@Override
				public void run() {
					Object f = notification.getFeature();
					if (PlanPackage.Literals.COMMON_MEMBER__VISIBLE == f) {
						figure.setVisible((Boolean) notification.getNewValue());
					} else if (TemporalPackage.Literals.TEMPORAL_MEMBER__SCHEDULED == f) {
						Boolean b = (Boolean) notification.getNewValue();
						if ((b == null) || b.booleanValue()) {
							figure.setForegroundColor(ColorConstants.black);
						} else {
							figure.setForegroundColor(ColorConstants.lightGray);
						}
					}
				}
			});
		}
	}

}
