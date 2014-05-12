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
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TemporalNodeToolTipPolicy extends PlanTimelineViewerEditPolicy {

	public static final String ROLE = TemporalNodeToolTipPolicy.class.getName();

	private Shell tip = null;
	private Browser browser = null;
	
	@Override
	public void activate() {
		Control parent = getViewer().getControl();
		Display display = WidgetUtils.getDisplay();
		
		tip = new Shell(parent.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
		tip.setBackground(display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
		tip.setLayout (new FillLayout());

		try {
			browser = new Browser(tip, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: " + e.getMessage());
			display.dispose();
			return;
		}
		
		updateBrowserText();
		super.activate();
	}

	@Override
	public void deactivate() {
		super.deactivate();
		if (tip != null && !tip.isDisposed()) {
			tip.dispose();
			tip = null;
		}
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		super.eraseTargetFeedback(request);
		
		if (request instanceof SelectionRequest) {
			tip.setVisible (false);
		}
	}

	@Override
	public void showTargetFeedback(Request request) {
		if (request instanceof SelectionRequest) {
			Control parent = getViewer().getControl();
			org.eclipse.swt.graphics.Point location = new org.eclipse.swt.graphics.Point(parent.getLocation().x, parent.getLocation().y);
			Display display = WidgetUtils.getDisplay();
			location = display.map(parent, null, location);
			
			Point pt = ((SelectionRequest)request).getLocation().getCopy();
			getHostFigure().translateToParent(pt);
			pt.x += location.x;
			pt.y += location.y;
			
			updateBrowserText();

			tip.setBounds (pt.x, pt.y, 400, 120);
			tip.setVisible (true);
		}
		super.showTargetFeedback(request);
	}

	private void updateBrowserText() {
		EPlanElement pe = (EPlanElement)getHost().getModel();
		String title = pe.getName();
		if (pe instanceof EActivity) {
			title += " ("+((EActivity)pe).getType()+")";
		}
		StringBuffer buffer = new StringBuffer("<HTML><BODY bgcolor=\"#FFFF99\">");
		buffer.append("<B>"+title+"</B>");
		buffer.append("<DIV><HR style=\"color:black\">");
		buffer.append("<TABLE BORDER=\"0\"  style=\"font-family:verdana;font-size:60%\">");
		buffer.append(buildFeatureRow(TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME));
		buffer.append(buildFeatureRow(TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME));
		buffer.append(buildFeatureRow(TemporalPackage.Literals.TEMPORAL_MEMBER__DURATION));
		buffer.append("</TABLE></DIV></BODY></HTML>");
		browser.setText(buffer.toString());
	}

	@SuppressWarnings("unchecked")
	private String buildFeatureRow(EAttribute feature) {
		TemporalNodeEditPart ep = (TemporalNodeEditPart) getHost();
		Object model = ep.getModel();
		IItemPropertySource source = EMFUtils.adapt(model, IItemPropertySource.class);
		IItemPropertyDescriptor startPD = source.getPropertyDescriptor(model, feature);
		
		// First check the instance name
		IStringifier stringifier = EMFUtils.getStringifier(feature);
		
		Object value = EMFUtils.getPropertyValue(startPD, model);
		return "<TR><TD>"+startPD.getDisplayName(model)+"</TD><TD>"+stringifier.getDisplayString(value)+"</TD></TR>";
	}
	
}
