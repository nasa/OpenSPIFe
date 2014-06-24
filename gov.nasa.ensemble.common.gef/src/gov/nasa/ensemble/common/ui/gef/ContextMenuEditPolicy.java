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

import org.apache.log4j.Logger;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.IMenuManager;

/**
 * Installing this edit policy on an edit part implies that
 * the edit part will popup a context menu.  This edit policy 
 * must be extended by a class that implements the getContextMenuCommand
 * method.
 * @author Andrew
 * @see gov.nasa.arc.spife.core.plan.editor.timeline.TransferContextMenuTargetListener
 */
public abstract class ContextMenuEditPolicy implements EditPolicy {

	private static final Logger trace = Logger.getLogger(ContextMenuEditPolicy.class);
	public static final String CONTEXT_MENU_ROLE = "ContextMenuEditPolicy";
	private EditPart editPart = null;

	public ContextMenuEditPolicy() {
		// default constructor
	}
	
	@Override
	public void activate() {
		// no special setup required
	}

	@Override
	public void deactivate() {
		// no special shutdown required
	}

	@Override
	public EditPart getHost() {
		return editPart;
	}

	@Override
	public void setHost(EditPart editPart) {
		this.editPart = editPart;
	}

	@Override
	public EditPart getTargetEditPart(Request request) {
		if (EnsembleRequestConstants.REQ_CONTEXT_MENU.equals(request.getType())) {
			trace.debug("ContextMenuEditPolicy: getTargetEditPart");
			return editPart;
		}
		return null;
	}
	
	@Override
	public Command getCommand(Request request) {
		if (EnsembleRequestConstants.REQ_CONTEXT_MENU.equals(request.getType())) {
			trace.debug("ContextMenuEditPolicy: getCommand");
			return null;
		}
		return null;
	}

	/**
	 * The user has requested a context menu on this edit part at the point.
	 * This method must be implemented by subclasses to populate a context menu.
	 * @param point the click location in figure coordinates.
	 * @param menu  the menu to add actions to.
	 */
	public abstract void buildContextMenu(Point point, IMenuManager menu);
	
	@Override
	public boolean understandsRequest(Request request) {
		if (EnsembleRequestConstants.REQ_CONTEXT_MENU.equals(request.getType())) {
			trace.debug("ContextMenuEditPolicy: understandsRequest");
			return true;
		}
		return false;
	}
	
	@Override
	public void showSourceFeedback(Request request) {
		if (EnsembleRequestConstants.REQ_CONTEXT_MENU.equals(request.getType())) {
			trace.debug("ContextMenuEditPolicy: showSourceFeedback");
		}
	}

	@Override
	public void eraseSourceFeedback(Request request) {
		if (EnsembleRequestConstants.REQ_CONTEXT_MENU.equals(request.getType())) {
			trace.debug("ContextMenuEditPolicy: eraseSourceFeedback");
		}
	}

	@Override
	public void showTargetFeedback(Request request) {
		if (EnsembleRequestConstants.REQ_CONTEXT_MENU.equals(request.getType())) {
			trace.debug("ContextMenuEditPolicy: showTargetFeedback");
		}
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		if (EnsembleRequestConstants.REQ_CONTEXT_MENU.equals(request.getType())) {
			trace.debug("ContextMenuEditPolicy: eraseTargetFeedback");
		}
	}

}
