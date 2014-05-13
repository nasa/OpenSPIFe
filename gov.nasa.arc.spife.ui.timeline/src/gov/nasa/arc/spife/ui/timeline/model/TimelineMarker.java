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
package gov.nasa.arc.spife.ui.timeline.model;

import gov.nasa.arc.spife.ui.timeline.util.DefaultModel;
import gov.nasa.ensemble.core.jscience.TemporalExtent;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public class TimelineMarker extends DefaultModel {

	public static final String COLOR = "color";
	public static final String TEMPORAL_EXTENT = "temporal extent";
	public static final String IMAGE = "image";
	
	private TemporalExtent temporalExtent = null;
	private Color color = null;
	private ImageDescriptor imageDescriptor = null;
	private String tooltip;
	private int lineStyle = SWT.LINE_SOLID;
	
	public int getLineStyle() {
		return lineStyle;
	}

	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		Color oldColor = this.color;
		this.color = color;
		firePropertyChange(COLOR, oldColor, color);
	}
	
	public boolean isSelectable() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}

	public void setImageDescriptor(ImageDescriptor imageDescriptor) {
		ImageDescriptor oldImageDescriptor = this.imageDescriptor;
		this.imageDescriptor = imageDescriptor;
		firePropertyChange(IMAGE, oldImageDescriptor, imageDescriptor);
	}

	public TemporalExtent getTemporalExtent() {
		return temporalExtent;
	}

	public void setTemporalExtent(TemporalExtent temporalExtent) {
		TemporalExtent oldExtent = this.temporalExtent;
		this.temporalExtent = temporalExtent;
		firePropertyChange(TEMPORAL_EXTENT, oldExtent, temporalExtent);
	}

	public boolean isInstantaneous() {
		return getTemporalExtent() != null && getTemporalExtent().getDurationMillis() == 0;
	}
	
	@Override
	public String toString(){
		TemporalExtent e = this.getTemporalExtent();
		return this.getClass().getSimpleName() + ": " + (e == null ? "{no extent}" : e.getStart()) + " " + this.getImageDescriptor(); 
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {
		return tooltip;
	}
	
	public boolean understandsRequest(Request request) {
		return false;
	}
	
	public Command getCommand(Request request) {
		return null;
	}
	
}
