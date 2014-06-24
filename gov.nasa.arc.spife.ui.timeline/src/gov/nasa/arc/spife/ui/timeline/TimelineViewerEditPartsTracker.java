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
package gov.nasa.arc.spife.ui.timeline;

import java.util.Iterator;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

public class TimelineViewerEditPartsTracker extends DragEditPartsTracker implements TimelineConstants {
	
	private static final int CHANGE_VALUE_KEY_MODIFIER = SWT.ALT;
	private static final int ADD_VALUE_KEY_MODIFIER = SWT.ALT | SWT.SHIFT;
	
	private boolean changeValueActive = false;
	private boolean addValueActive = false;

	public TimelineViewerEditPartsTracker(EditPart sourceEditPart) {
		super(sourceEditPart);
	}

	@Override
	protected boolean handleKeyDown(KeyEvent e) {
		int key = e.keyCode;
		if (testModifierFlags(key, ADD_VALUE_KEY_MODIFIER)) {
			setAddValueActive(true);
			setChangeValueActive(false);
			handleDragInProgress();
			return true;
		} else if (testModifierFlags(key, CHANGE_VALUE_KEY_MODIFIER)) {
			setChangeValueActive(true);
			setAddValueActive(false);
			handleDragInProgress();
			return true;
		} // else...
		return super.handleKeyDown(e);
	}

	@Override
	protected boolean handleKeyUp(KeyEvent e) {
		int key = e.keyCode;
		if (testModifierFlags(key, ADD_VALUE_KEY_MODIFIER)) {
			setAddValueActive(false);
			setChangeValueActive(false);
			handleDragInProgress();
			return true;
		} else if (testModifierFlags(key, CHANGE_VALUE_KEY_MODIFIER)) {
			setChangeValueActive(false);
			setAddValueActive(false);
			handleDragInProgress();
			return true;
		} // else...
		return super.handleKeyUp(e);
	}

	@Override
	protected void performDirectEdit() {
		if (getSourceEditPart().getViewer() != null) {
			super.performDirectEdit();
		}
	}

	@Override
	protected boolean isCloneActive() {
		return false;
	}

	@Override
	protected void setCloneActive(boolean cloneActive) {
//		if (this.cloneActive == cloneActive)
//			return;
//		eraseSourceFeedback();
//		eraseTargetFeedback();
//		this.cloneActive = cloneActive;
	}

	@Override
	protected void setState(int state) {
		super.setState(state);
		if (isInState(STATE_ACCESSIBLE_DRAG | STATE_DRAG_IN_PROGRESS
				| STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
			Input input = getCurrentInput();
			if (input.isAltKeyDown() && input.isShiftKeyDown()) {
				setAddValueActive(true);
				handleDragInProgress();
			} else if (input.isAltKeyDown()) {
				setChangeValueActive(true);
				handleDragInProgress();
			}
		}
	}
	
	public boolean isAddValueActive() {
		return addValueActive;
	}

	public void setAddValueActive(boolean addValueActive) {
		this.addValueActive = addValueActive;
	}

	public boolean isChangeValueActive() {
		return changeValueActive;
	}

	public void setChangeValueActive(boolean changeValueActive) {
		this.changeValueActive = changeValueActive;
	}

	@Override
	protected Request createTargetRequest() {
		if (isChangeValueActive())
			return new ChangeBoundsRequest(REQ_CHANGE_VALUE_VIA_DROP);
		else if (isAddValueActive())
			return new ChangeBoundsRequest(REQ_CHANGE_VALUE_VIA_DROP);
//		else if (isCloneActive())
//			return new ChangeBoundsRequest(REQ_CLONE);
		else
			return new ChangeBoundsRequest(REQ_MOVE);
	}
	
	@Override
	protected Command getCommand() {
		CompoundCommand command = new CompoundCommand();
		command.setDebugLabel("Drag Object Tracker");//$NON-NLS-1$

		Iterator iter = getOperationSet().iterator();

		Request request = getTargetRequest();

		if (isCloneActive())
			request.setType(REQ_CLONE);
		else if (isAddValueActive())
			request.setType(REQ_ADD_VALUE_VIA_DROP);
		else if (isChangeValueActive())
			request.setType(REQ_CHANGE_VALUE_VIA_DROP);
		else if (isMove())
			request.setType(REQ_MOVE);
		else
			request.setType(REQ_ORPHAN);

		if (!isChangeValueActive() && !isAddValueActive()) {
			while (iter.hasNext()) {
				EditPart editPart = (EditPart) iter.next();
				command.add(editPart.getCommand(request));
			}
		}
		
		if (!isMove() || isChangeValueActive() || isAddValueActive()) {
			if (!isChangeValueActive() && !isAddValueActive())
				request.setType(REQ_ADD);

			if (getTargetEditPart() == null)
				command.add(UnexecutableCommand.INSTANCE);
			else
				command.add(getTargetEditPart().getCommand(getTargetRequest()));
		}

		return command.unwrap();
	}

	@Override
	protected String getCommandName() {
		if (isAddValueActive())
			return REQ_ADD_VALUE_VIA_DROP;
		else if (isChangeValueActive()) {
			return REQ_CHANGE_VALUE_VIA_DROP;
//		} else if (isCloneActive()) {
//			return REQ_CLONE;
		} else if (isMove()) {
			return REQ_MOVE;
		} else {
			return REQ_ADD;
		}
	}

	private boolean testModifierFlags(int key, int modifierFlags) {
		return (modifierFlags & key) == modifierFlags;
	}
	
}
