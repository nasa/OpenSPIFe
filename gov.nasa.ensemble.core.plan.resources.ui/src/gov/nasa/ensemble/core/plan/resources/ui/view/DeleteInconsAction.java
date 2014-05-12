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
package gov.nasa.ensemble.core.plan.resources.ui.view;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class DeleteInconsAction implements IViewActionDelegate {

	InconsView view;
	ISelection selection;
	InconsPage inconsPage;
	
	
	@Override
	public void run(IAction action) {
		InconsPage inconsPage = (InconsPage) view.getCurrentPage();
		if (inconsPage == null) {
			LogUtil.error("Could not retrieve current InconsPage.");
			return;
		}
		if (!selection.isEmpty() && (selection instanceof IStructuredSelection)) {
			inconsPage.removeConditions((IStructuredSelection)selection);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
		action.setEnabled(shouldBeEnabled(selection));
	}
	
	@Override
	public void init(IViewPart view) {
		this.view = (InconsView)view;
	}
	
	private boolean shouldBeEnabled(ISelection selection) {
		if (!selection.isEmpty() && (selection instanceof IStructuredSelection)) {
			Object selected = ((IStructuredSelection) selection).getFirstElement();
			if (selected instanceof Conditions) {
				return true;
			}
		}
		return false;
	}

}
