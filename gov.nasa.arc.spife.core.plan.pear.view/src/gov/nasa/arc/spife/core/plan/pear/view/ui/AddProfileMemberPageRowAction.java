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
package gov.nasa.arc.spife.core.plan.pear.view.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class AddProfileMemberPageRowAction implements IViewActionDelegate {

	private IViewPart view;

	public void run(IAction action) {
		ProfileEffectsAndRequirementsView profileMemberView = (ProfileEffectsAndRequirementsView)view;
		ProfileEffectsAndRequirementsPage profileMemberPage = (ProfileEffectsAndRequirementsPage) profileMemberView.getCurrentPage();
		if (profileMemberPage == null) {
			return;
		}
		profileMemberPage.addRow();
	}

	public void selectionChanged(IAction action, ISelection selection) {
		// no implementation
	}

	public void init(IViewPart view) {
		this.view = view;
	}

}
