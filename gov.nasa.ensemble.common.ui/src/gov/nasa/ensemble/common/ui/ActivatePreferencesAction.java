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
package gov.nasa.ensemble.common.ui;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PreferencesUtil;

public class ActivatePreferencesAction extends Action {

	private final Shell shell;
	private final String preferencePageId;

	public ActivatePreferencesAction(Shell shell, String preferencePageId) {
		super("", AS_PUSH_BUTTON);
		this.shell = shell;
		this.preferencePageId = preferencePageId;
		setText("Preferences...");
	}

	@Override
	public void run() {
		PreferencesUtil.createPreferenceDialogOn(shell, preferencePageId, null, null).open();
	}

}
