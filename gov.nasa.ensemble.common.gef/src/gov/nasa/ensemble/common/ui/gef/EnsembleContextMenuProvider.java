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
package gov.nasa.ensemble.common.ui.gef;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.Collections;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class EnsembleContextMenuProvider extends ContextMenuProvider {
	
	public EnsembleContextMenuProvider(EditPartViewer viewer) {
		super(viewer);
	}
	
	private EditPart calculateTargetEditPart(Point point, final Request request) {
		EditPart ep = getViewer()
			.findObjectAtExcluding(
				point, 
				Collections.emptyList(),
				new EditPartViewer.Conditional() {
					@Override
					public boolean evaluate(EditPart editpart) {
						return editpart.understandsRequest(request);
					}
				});
		if (ep != null)
			ep = ep.getTargetEditPart(request);
		return ep;
	}
	
	private Point getCursorLocation(Control control) {
		Display display = control.getDisplay();
		org.eclipse.swt.graphics.Point location = display.getCursorLocation();
		location = display.map(null, control, location);
		return new Point(location.x, location.y);
	}
	
	@Override
	public void buildContextMenu(IMenuManager menu) {
		GEFActionConstants.addStandardActionGroups(menu);
		Request request = new Request(EnsembleRequestConstants.REQ_CONTEXT_MENU);
		Point cursor = getCursorLocation(getViewer().getControl());
		EditPart target = calculateTargetEditPart(cursor, request);
		if (target != null) {
			EditPolicy policy = target.getEditPolicy(ContextMenuEditPolicy.CONTEXT_MENU_ROLE);
			if (policy instanceof ContextMenuEditPolicy) {
				Point local = new Point(0, 0);
				if (target instanceof GraphicalEditPart) {
					local = ((GraphicalEditPart)target).getFigure().getBounds().getTopLeft();
					local.negate();
					local.translate(cursor);
				}
				try {
					((ContextMenuEditPolicy)policy).buildContextMenu(local, menu);
				} catch (Exception e) {
					LogUtil.error(e);
				}
			}
		}
	}

}
