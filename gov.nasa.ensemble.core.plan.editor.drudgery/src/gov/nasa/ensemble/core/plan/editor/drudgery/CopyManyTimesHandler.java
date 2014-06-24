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
package gov.nasa.ensemble.core.plan.editor.drudgery;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.editor.PlanCopyPasteManyTimesOperation;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.constraints.DrudgerySavingHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

public class CopyManyTimesHandler extends DrudgerySavingHandler {

	public CopyManyTimesHandler() {
		super("Copy Many Times", "Copying Many Times");
	}
	
	@Override
	protected int getLowerBoundSelectionCount() {
		return 1;
	}
	
	@Override
	public Object execute(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		EList<EPlanElement> elements = getSelectedTemporalElements(selection);
		if (elements.size() > 0) {
			IEditorPart editor = getActiveEditor();
			if (editor == null) {
				return null;
			}
			CopyManyTimesInputValidator validator = new CopyManyTimesInputValidator();
			InputDialog d = new InputDialog(editor.getSite().getShell(), actionName, "How many copies do you want to make?", "", validator);
			int code = d.open();
			int nCopies = validator.getValue(); // this line must be after d.open();
			if ((code == Window.OK) && (nCopies > 0) && (nCopies < 101)) {
				ECollections.sort(elements, TemporalChainUtils.CHAIN_ORDER);
				makinCopies(elements, nCopies);
			}
		}
		return null;
	}
	
	private void makinCopies(List<? extends EPlanElement> elements, int nCopies) {
	    PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
	    IEditorPart editor = getActiveEditor();
	    if (editor == null) {
	    	return;
	    }
	    ISelection selection = new StructuredSelection(elements);
	    ISelectionProvider selectionProvider = editor.getSite().getSelectionProvider();
	    PlanCopyPasteManyTimesOperation operation = new PlanCopyPasteManyTimesOperation(selection, modifier, nCopies, selectionProvider);
	    CommonUtils.execute(operation, getUndoContext());
	}

	private static final class CopyManyTimesInputValidator implements IInputValidator {

		private int value;

		private CopyManyTimesInputValidator() {
			// nothing to do
		}

		@Override
		public String isValid(String newText) {
			if (newText != null && newText.length() > 0) {
				try {
					value = Integer.parseInt(newText);
					if (value < 1) {
						return "Please enter a positive number";
					} else if (value > 100) {
						return "You don't need that many";
					}
				} catch (NumberFormatException e) {
					return "Please enter a positive number";
				}
			}
			return null;
		}

		public int getValue() {
			return value;
		}

	}


	@Override
	protected Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> elements) {
		return null;
	}

	@Override
	public String getCommandId() {
		return COPY_MANY_TIMES_COMMAND_ID;
	}

}
