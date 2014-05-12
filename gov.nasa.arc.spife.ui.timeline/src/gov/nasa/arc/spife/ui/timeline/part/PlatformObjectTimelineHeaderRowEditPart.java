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
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.arc.spife.ui.timeline.TimelineViewerEditPart;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.CommonUtils;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.jface.viewers.ILabelProvider;

public class PlatformObjectTimelineHeaderRowEditPart<T extends IAdaptable> extends TimelineViewerEditPart<T> {

	private ILabelProvider labelProvider 						= null;

	@Override
	protected IFigure createFigure() {
		Label label = new Label();
		ILabelProvider labelProvider = getLabelProvider();
		label.setIcon(labelProvider.getImage(getModel()));
		label.setText(labelProvider.getText(getModel()));
		label.setTextAlignment(PositionConstants.LEFT);
		return label;
	}

	@Override
	protected void createEditPolicies() {
		// no default edit policies
	}
	
	public ILabelProvider getLabelProvider() {
		if (labelProvider == null) {
			labelProvider = CommonUtils.getAdapter(getModel(), ILabelProvider.class);
		}
		return labelProvider;
	}
	
	@Override
	protected void registerModel() {
		TimelineUtils.registerAsList(this);
	}
	
	@Override
	protected void unregisterModel() {
		TimelineUtils.unregisterAsList(this);
	}
	
}
