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
package gov.nasa.ensemble.core.plan.editor.constraints;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.PeriodicTemporalConstraint;
import gov.nasa.ensemble.core.plan.constraints.PinUtils;
import gov.nasa.ensemble.core.plan.constraints.operations.PinOperation;
import gov.nasa.ensemble.core.plan.constraints.operations.UnpinOperation;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class PinHandler extends AbstractPlanEditorHandler {

	@Override
	protected int getLowerBoundSelectionCount() {
		return 1;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Command command = event.getCommand();
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Set<? extends EPlanElement> elements = PlanEditorUtil.emfFromSelection(selection);
		final IUndoableOperation op;
		boolean state = getCommandState(command);
		if (state) {
			op = new UnpinOperation(elements);
		} else {
			op = new PinOperation(elements);
		}
		CommonUtils.execute(op, getUndoContext());
		setCommandState(command, !state);
		return null;
	}

	@Override
	public boolean isCheckedForSelection(ISelection selection) {
		EList<EPlanElement> elements = getSelectedTemporalElements(selection);
		boolean isChecked = !elements.isEmpty();
		for (EPlanElement element : elements) {
			List<PeriodicTemporalConstraint> constraints = PinUtils.getPinConstraints(element);
			if (constraints.isEmpty()) {
				isChecked = false;
				break;
			}
		}
		return isChecked;
	}
	
	@Override
	public String getCommandId() {
		return PIN_COMMAND_ID;
	}

}
