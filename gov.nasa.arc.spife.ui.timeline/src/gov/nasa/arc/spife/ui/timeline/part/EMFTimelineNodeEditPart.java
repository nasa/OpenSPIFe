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
/**
 * 
 */
package gov.nasa.arc.spife.ui.timeline.part;

import gov.nasa.arc.spife.ui.timeline.figure.BarFigure;
import gov.nasa.arc.spife.ui.timeline.model.TemporalProvider;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.emf.util.EMFUtils;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.swt.graphics.Color;

public class EMFTimelineNodeEditPart extends TreeTimelineNodeEditPart<EObject> {
	
	@Override
	public TemporalExtent getTemporalExtent() {
		TemporalProvider tp = EMFUtils.adapt(getModel(), TemporalProvider.class);
		if (tp != null) {
			return new TemporalExtent(tp.getStartTime(getModel()), tp.getDuration(getModel()));
		}
		return null;
	}

	@Override
	protected IFigure createFigure() {
		BarFigure figure = (BarFigure) super.createFigure();
		IItemColorProvider provider = EMFUtils.adapt(getModel(), IItemColorProvider.class);
		if (provider != null) {
			Color bg = (Color) provider.getBackground(getModel());
			if (bg != null) {
				figure.setNormalColorPalette(bg);
			}
		}
		return figure;
	}
	
}
