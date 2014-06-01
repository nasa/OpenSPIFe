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

import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.dnd.EnsembleDragAndDropOracle;
import gov.nasa.ensemble.common.ui.transfers.TransferRegistry;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

/**
 * This listener fills in the information for the drop 
 * request. It sets the type of the request to be REQ_DROP.
 * This type is handled by a DropEditPolicy.  The listener
 * also sets the "detail" for the event so that the drop
 * cursor will show feedback.  
 * 
 * There are some other handles overriden here although they 
 * merely are for debug purposes.  They could also be 
 * potentially used for more feedback in the future.
 * 
 * @author Andrew
 * @see gov.nasa.ensemble.common.ui.gef.DropEditPolicy
 */
public class TransferDropTargetListener extends AbstractTransferDropTargetListener {

	private static final Logger trace = Logger.getLogger(TransferDropTargetListener.class);

	public TransferDropTargetListener(EditPartViewer viewer, Transfer transfer) {
		super(viewer, transfer);
	}
	
	/**
	 * Fills in the information about the drop and 
	 * sets the type of the request to be REQ_DROP.
	 * This is the first of two important functions in this listener.
	 */
	@Override
	protected void updateTargetRequest() {
		@SuppressWarnings("unchecked") // the extended data is empty before our use
		Map<String, Object> data = getTargetRequest().getExtendedData();
		data.put(DropEditPolicy.DROP_TARGET_EVENT, getCurrentEvent());
		data.put(DropEditPolicy.DROP_LOCATION, getDropLocation());
		data.put(DropEditPolicy.TRANSFER, getTransfer());
		getTargetRequest().setType(EnsembleRequestConstants.REQ_DROP);
	}
	
	/**
	 * This method must set the event detail/feedback in
	 * order for a drop to be possible.  The only detail
	 * which seems to work is DROP_COPY. (Why?)
	 * This is the second of two important functions in this listener.
	 */
	@Override
	protected void handleDragOver() {
//		getCurrentEvent().feedback = DND.FEEDBACK_SELECT;
		getCurrentEvent().detail = DND.DROP_COPY; 
		super.handleDragOver();
	}

	/**
	 * Overriden for debug purposes.  Could be used for feedback.
	 */
	@Override
	protected void handleEnteredEditPart() {
		EditPart editPart = getTargetEditPart();
		trace.debug("handleEnteredEditPart: " + editPart);
		super.handleEnteredEditPart();
	}
	
	/**
	 * Overriden for debug purposes.  Could be used for feedback.
	 */
	@Override
	protected void handleExitingEditPart() {
		EditPart editPart = getTargetEditPart();
		trace.debug("handleExitingEditPart: " + editPart);
		super.handleExitingEditPart();
	}
	
	/**
	 * Overriden for debug purposes.
	 * Note: the drop is actually performed in the DropEditPolicy.
	 */
	@Override
	protected void handleDrop() {
		trace.debug("handleDrop");
		updateTargetRequest();
		updateTargetEditPart();
		if (getTargetEditPart() != null) {
			final Command command = getCommand();
			if (command != null && command.canExecute()) {
				EditPartViewer viewer = getViewer();
				final CommandStack commandStack = viewer.getEditDomain().getCommandStack();
				WidgetUtils.runLaterInDisplayThread(viewer.getControl(), new Runnable() {
					@Override
					public void run() {
						commandStack.execute(command);
					}
				});
			} else {
				getCurrentEvent().detail = DND.DROP_NONE;
			}
		} else {
			getCurrentEvent().detail = DND.DROP_NONE;
		}
	}
	
	/**
	 * Overriden to call EnsembleDragAndDropOracle.acquireDropping so that a more efficient form
	 * of drag data can be used for a same process drag and drop.
	 * 
	 * @see gov.nasa.ensemble.common.ui.transfers.TransferRegistry#getDragData(gov.nasa.ensemble.common.ui.transfers.ITransferable, org.eclipse.swt.dnd.TransferData)
	 */
	@Override
	public void dropAccept(DropTargetEvent event) {
		super.dropAccept(event);
		DropTarget target = (DropTarget)event.widget;
		EnsembleDragAndDropOracle.acquireDropping(target);
	}

	public static void addDropSupport(EditPartViewer viewer) {
		for (Transfer transfer : TransferRegistry.getInstance().getAllTransfers())
			viewer.addDropTargetListener(new TransferDropTargetListener(viewer, transfer));
	}

}
