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
package gov.nasa.arc.spife.core.plan.editor.timeline.policies;

import gov.nasa.arc.spife.core.plan.editor.timeline.parts.TemporalNodeEditPart;
import gov.nasa.arc.spife.ui.timeline.figure.BarFigure;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.common.ui.color.ColorUtils;
import gov.nasa.ensemble.common.ui.gef.GEFUtils;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.Request;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class TemporalNodeDecoratorColorEditPolicy extends PlanTimelineViewerEditPolicy {

	public static final String ROLE = "TemporalNodeColorDecoratorPolicy";
	
	private Color color = null;
	private boolean requiresDisposal = false;
	private Listener listener = new Listener();
	
	@Override
	public void activate() {
		super.activate();
		getCommonMember().eAdapters().add(listener);
		EPlanElement pe = ((TemporalNodeEditPart)getHost()).getModel();
		EObject data = pe.getData();
		if (data != null) {
			data.eAdapters().add(listener);
		}
		synchronizeStates();
		updateColor();
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		if (color != null && requiresDisposal) {
			//color.dispose();
			color = null;
		}
		getCommonMember().eAdapters().remove(listener);
		EPlanElement pe = ((TemporalNodeEditPart)getHost()).getModel();
		EObject data = pe.getData();
		if (data != null) {
			data.eAdapters().remove(listener);
		}
	}

	private CommonMember getCommonMember() {
		return getEPlanElement().getMember(CommonMember.class);
	}
	
	/**
	 * Returns the EPlanElement that this color edit policy manages
	 * @return
	 */
	private EPlanElement getEPlanElement() {
		return ((TemporalNodeEditPart)getHost()).getModel();
	}
	
	public Color getColor() {
		return color;
	}
	
	/**
	 * Update the color of the figure on the host edit part.
	 * WARNING: Do not dispose this color.
	 */
	@SuppressWarnings("unchecked")
	protected void updateColor(Color color) {
		final BarFigure figure = (BarFigure) getHostFigure();
		figure.setNormalColorPalette(color);
		List children = new ArrayList(figure.getChildren());
		for (Object o : children) {
			if (o instanceof BarFigure) {
				((BarFigure)o).setNormalColorPalette(color);
			}
		}
		GEFUtils.runInDisplayThread(getHost(), new Runnable() {
			@Override
			public void run() {
				figure.repaint();
			}
		});
	}
	
	protected void updateColor() {
		if (color != null && requiresDisposal) {
			//color.dispose();
			color = null;
		}
		
		EPlanElement ePlanElement = getEPlanElement();
		RGB rgb = ColorUtils.getAsRGB(ePlanElement.getMember(CommonMember.class).getColor());
		if (rgb == null) {
			color = ColorConstants.lightGray;
			requiresDisposal = false;
		} else {
			color = ColorMap.RGB_INSTANCE.getColor(rgb);
			requiresDisposal = true;
		}
		updateColor(color);
	}

	@Override
	public void showTargetFeedback(Request request) {
		super.showTargetFeedback(request);
		updateColor();
	}

	/**
	 * This method checks the feature details, in which we want to then manipulate the colors.
	 */
	private void synchronizeStates() {
		// Add any other synchronize states here
	}
	
	private class Listener extends AdapterImpl{
		@Override
		public void notifyChanged(Notification notification) {
			Object feature = notification.getFeature();
			if (feature instanceof EStructuralFeature) {
				synchronizeStates();
				updateColor();
			}
		}
	}
	
}
