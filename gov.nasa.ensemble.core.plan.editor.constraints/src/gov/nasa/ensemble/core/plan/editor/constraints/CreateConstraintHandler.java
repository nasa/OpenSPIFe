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

import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.constraints.ConstraintsMember;
import gov.nasa.ensemble.core.model.plan.constraints.util.ConstraintUtils;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.constraints.ConstraintsPlugin;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;

import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

public class CreateConstraintHandler extends AbstractPlanEditorHandler {

	@Override
	protected int getLowerBoundSelectionCount() {
		return 2;
	}
	
	@Override
	protected int getUpperBoundSelectionCount() {
		return 2;
	}
	
	@Override
	public Object execute(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		List<EPlanElement> elements = getPlanElementPair(selection);
		if (elements != null) {
			EPlanElement elementA = elements.get(0);
			EPlanElement elementB = elements.get(1);
			Shell shell = getActiveEditor().getSite().getShell();
			IUndoContext undoContext = getUndoContext();
			ConstraintDialog dialog = new ConstraintDialog(shell, undoContext, elementA, elementB);
			dialog.open();
		}
		return null;
	}
	
	@Override
	public boolean isEnabledForSelection(ISelection selection) {
		boolean isEnabled = super.isEnabledForSelection(selection);
		if (isEnabled) {
			List<EPlanElement> elements = getPlanElementPair(selection);
			if (elements == null) {
				return false;
			} else {
				if (ConstraintUtils.isNested(elements)) {
					return false;
				}
				for (EPlanElement element : elements) {
					ConstraintsMember member = element.getMember(ConstraintsMember.class, false);
					if ((member == null)
							|| ((element instanceof EActivityGroup) && !ConstraintsPlugin.ALLOW_PULL_GROUP_CONSTRAINTS)
							|| !PlanEditApproverRegistry.getInstance().canModify(element)) {
						return false;
					}
				}
			}
		}
		return isEnabled;
	}
	
	private static List<EPlanElement> getPlanElementPair(ISelection selection) {
		Set<EPlanElement> elements = PlanEditorUtil.emfFromSelection(selection);
		if (elements.size() == 2) {
			EList<EPlanElement> result = new BasicEList<EPlanElement>(elements);
			ECollections.sort(result, PlanUtils.INHERENT_ORDER);
			return result;
		}
		return null;
	}

	@Override
	public String getCommandId() {
		return BINARY_CONSTRAINT_COMMAND_ID;
	}

}
