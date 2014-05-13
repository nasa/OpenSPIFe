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
package gov.nasa.arc.spife.ui.timeline.chart.policy;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;

public class LinePlotDataSelectionEditPolicy extends SelectionEditPolicy {

	@Override
	protected void hideSelection() {
		((Polyline)getHostFigure()).setLineWidth(2);
	}
	
	@Override
	protected void showSelection() {
		Polyline line = (Polyline)getHostFigure();
		IFigure p = line.getParent();
		p.remove(line);
		p.add(line);
		line.setLineWidth(3);
	}

}
