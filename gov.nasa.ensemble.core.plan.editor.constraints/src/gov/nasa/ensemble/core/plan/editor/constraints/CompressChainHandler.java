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
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.plan.constraints.operations.CompressChainOperation;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class CompressChainHandler extends AbstractPlanEditorHandler {

	@Override
	protected int getLowerBoundSelectionCount() {
		return 1;
	}
	
	@Override
	public Object execute(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Set<EPlanElement> selected = PlanEditorUtil.emfFromSelection(selection);
		List<? extends EPlanElement> elements = CompressChainOperation.getRepresentativePlanElements(selected); 
		final IUndoableOperation op = new CompressChainOperation(elements);
		IUndoContext undoContext = getUndoContext();
		CommonUtils.execute(op, undoContext);
		return null;
	}
	
	@Override
	public boolean isEnabledForSelection(ISelection selection) {
		boolean enabled = super.isEnabledForSelection(selection);
		if (enabled) {
			Set<EPlanElement> elements = PlanEditorUtil.emfFromSelection(selection);
			boolean atLeastOneChain = false;
			for (EPlanElement element : elements) {
				ConstraintsMember member = element.getMember(ConstraintsMember.class, false); 
				if (member != null) {
					TemporalChain chain = member.getChain();
					if (chain != null) {
						atLeastOneChain = true;
						break;
					}
				}
			}
			return atLeastOneChain;
		}
		return enabled;
	}

	@Override
	public String getCommandId() {
		return COMPRESS_CHAIN_COMMAND_ID;
	}

}
