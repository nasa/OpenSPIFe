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
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * Installing this edit policy on an edit part implies that
 * the edit part will accept drops.  This edit policy must
 * be extended by a class that implements the getDropCommand
 * method.
 * @author Andrew
 * @see gov.nasa.arc.spife.core.plan.editor.timeline.TransferDropTargetListener
 */
public abstract class DropEditPolicy extends AbstractEditPolicy {

	public static final String DROP_TARGET_EVENT = "dropTargetEvent";
	public static final String DROP_LOCATION = "dropLocation";
	public static final String TRANSFER = "transfer";
	
	private static final Logger trace = Logger.getLogger(DropEditPolicy.class);
	public static final String DROP_ROLE = "DropEditPolicy";
	
	/**
	 * This method must be implemented by subclasses to return a
	 * command to implement the drop.
	 * @param request
	 * @return the command which will perform the drop.
	 */
	protected abstract Command getDropCommand(Request request);
	
	/**	 
	 * This method should be overridden by subclasses to check a
	 * drop.
	 * @return whether a particular TransferData type is accepted
	 */
	protected boolean isSupportedTransferData(TransferData d) {
		return false;
	}
	
	protected boolean willAcceptDrop(Request request) {
		for (TransferData d : getTransfer(request).getSupportedTypes())
			if (isSupportedTransferData(d))
				return true;
		return false;
	}
	
	@Override
	public final Command getCommand(Request request) {
		if (understandsRequest(request) && willAcceptDrop(request)) {
			trace.debug("DropEditPolicy: getCommand");
			return getDropCommand(request);
		}
		return null;
	}
	
	@Override
	public final EditPart getTargetEditPart(Request request) {
		if (understandsRequest(request) && willAcceptDrop(request)) {
			trace.debug("DropEditPolicy: getTargetEditPart");
			return getHost();
		}
		return null;
	}
	
	@Override
	public boolean understandsRequest(Request request) {
		if (EnsembleRequestConstants.REQ_DROP.equals(request.getType())) {
			trace.debug("DropEditPolicy: understandsRequest");
			return true;
		}
		return false;
	}
	
	@Override
	public void showSourceFeedback(Request request) {
		if (understandsRequest(request))
			trace.debug("DropEditPolicy: showSourceFeedback");
	}

	@Override
	public void eraseSourceFeedback(Request request) {
		if (understandsRequest(request))
			trace.debug("DropEditPolicy: eraseSourceFeedback");
	}

	@Override
	public void showTargetFeedback(Request request) {
		if (understandsRequest(request))
			trace.debug("DropEditPolicy: showTargetFeedback");
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		if (understandsRequest(request))
			trace.debug("DropEditPolicy: eraseTargetFeedback");
	}

	// UTILITIES FOR SUBCLASSES
	
	protected Transfer getTransfer(Request request) {
		return (Transfer)request.getExtendedData().get(TRANSFER);
	}
	
	protected Point getDropLocation(Request request) {
		return (Point)request.getExtendedData().get(DROP_LOCATION);
	}

	protected DropTargetEvent getDropTargetEvent(Request request) {
		return (DropTargetEvent)request.getExtendedData().get(DROP_TARGET_EVENT);
	}
}
