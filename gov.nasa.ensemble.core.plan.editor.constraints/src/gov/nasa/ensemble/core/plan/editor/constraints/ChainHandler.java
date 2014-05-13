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
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.TemporalChain;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.constraints.operations.UnchainOperation;
import gov.nasa.ensemble.core.plan.constraints.ui.operation.ChainOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class ChainHandler extends AbstractPlanEditorHandler {

	@Override
	protected int getLowerBoundSelectionCount() {
		return 1;
	}
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Command command = event.getCommand();
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Set<EPlanElement> allElements = PlanEditorUtil.emfFromSelection(selection);
		List<EPlanElement> elements = EPlanUtils.getConsolidatedPlanElements(allElements);
		boolean state = getCommandState(command);
		IEditorPart editor = getActiveEditor();
		final IUndoableOperation op;
		if (state) {
			op = new UnchainOperation(elements);
		} else {
			PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
			List<EPlanChild> children = CommonUtils.castList(EPlanChild.class, elements);
			op = new ChainOperation(modifier, children, true);
		}
		WidgetUtils.execute(op, getUndoContext(), null, editor.getSite());
		setCommandState(command, !state);
		return null;
	}
	
	@Override
	public boolean isEnabledForSelection(ISelection selection) {
		return (isCheckedForSelection(selection) || ChainOperation.isExecutableForSelection(selection));
	}
	
	@Override
	public boolean isCheckedForSelection(ISelection selection) {
		Set<EPlanElement> allElements = PlanEditorUtil.emfFromSelection(selection);
		List<EPlanElement> elements = EPlanUtils.getConsolidatedPlanElements(allElements);
		if (elements.isEmpty()) {
			return false;
		} else if (elements.size() == 1) {
			ConstraintsMember member = elements.get(0).getMember(ConstraintsMember.class, false);
			if (member != null && member.getChain() != null) {
				return true;
			}
		} else {
			TemporalChain chain = null;
			for (EPlanElement element : elements) {
				ConstraintsMember member = element.getMember(ConstraintsMember.class, false);
				if (member != null) {
					TemporalChain elementChain = member.getChain();
					if (elementChain == null) {
						return false;
					} else if (chain == null) {
						chain = elementChain;
					} else if (chain != elementChain) {
						return false;
					}
				}
			}
			return (chain != null);
		}
		return false;
	}

	@Override
	public String getCommandId() {
		return CHAIN_COMMAND_ID;
	}

}
