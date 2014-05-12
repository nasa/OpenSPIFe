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
package gov.nasa.ensemble.core.plan.advisor;

import gov.nasa.ensemble.common.authentication.AuthenticationUtil;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.advisor.IWaivable;
import gov.nasa.ensemble.core.model.plan.advisor.util.WaiverUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

public class CreateWaiverOperation extends AbstractTransactionUndoableOperation implements ISuggestionOperation {

	private final IWaivable waivable;
	private final String prefix;
	private final List<String> waivers;
	protected String waiverRationale;

	public CreateWaiverOperation(String label, IWaivable waivable) {
		super(label);
		this.waivable = waivable;
		this.prefix = null;
		this.waivers = null;
	}
	
	public CreateWaiverOperation(String label, String prefix, List<String> waivers) {
		super(label);
		this.waivable = null;
		this.prefix = prefix;
		this.waivers = waivers;
	}
	
	@Override
	public boolean preExecute() {
		Shell shell = WidgetUtils.getShell();
		InputDialog dialog = getWaiverRationaleDialog(shell, "What is the rationale for waiving this requirement?");
		int result = dialog.open();
		if (result == Window.CANCEL) {
			return false;
		}
		waiverRationale = dialog.getValue();
		return true;
	}

	public static WaiverRationaleDialog getWaiverRationaleDialog(Shell shell, String dialogMessage) {
		return new WaiverRationaleDialog(shell,
				"Waiver Rationale",
				dialogMessage,
				"Not specified", new IInputValidator() {
					@Override
					public String isValid(String newText) {
						// Don't allow '<' '>' characters
						String invalidChars = newText.replaceAll("[^><]", "");
						if (!StringUtils.isEmpty(invalidChars)) {
							return "Please remove the following invalid character(s): '" + invalidChars + "'";
						}
						return null;
					}
				});
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to do
	}

	@Override
	protected void execute() throws Throwable {
		setRationale(waiverRationale);
	}

	@Override
	protected void undo() throws Throwable {
		setRationale(null);
	}
	
	@Override
	protected void redo() throws Throwable {
		setRationale(waiverRationale);
	}

	protected void setRationale(String rationale) {
		String userRationale = getUserRationale(rationale);
		if (waivable != null) {
			WaiverUtils.setRationale(waivable, userRationale);
		}
		if (prefix != null) {
			WaiverUtils.setRationale(waivers, prefix, userRationale);
		}
	}

	protected String getUserRationale(String rationale) {
		if (rationale != null) {
			String user = AuthenticationUtil.getEnsembleUser();
			if (user != null) {
				rationale = user + "::" + rationale;
			}
		}
		return rationale;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(CreateWaiverOperation.class.getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(waivable));
		builder.append(" to ");
		builder.append(String.valueOf(waiverRationale));
		return builder.toString();
	}

}
