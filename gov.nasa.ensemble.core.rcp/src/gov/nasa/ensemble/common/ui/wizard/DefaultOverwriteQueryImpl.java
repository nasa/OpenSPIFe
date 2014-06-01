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
package gov.nasa.ensemble.common.ui.wizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;

@SuppressWarnings("restriction")
public class DefaultOverwriteQueryImpl implements IOverwriteQuery {
	private boolean alwaysOverwrite = false;
	private boolean canceled = false;
	private Shell shell;

	public DefaultOverwriteQueryImpl(Shell shell) {
		this.shell = shell;
	}

	@Override
	public String queryOverwrite(String pathString) {
		if (alwaysOverwrite) {
			return ALL;
		}
		final String returnCode[] = { CANCEL };
		final String msg = NLS.bind(IDEWorkbenchMessages.CopyFilesAndFoldersOperation_overwriteQuestion, pathString);
		final String[] options = { IDialogConstants.YES_LABEL, IDialogConstants.YES_TO_ALL_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL };
		shell.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog dialog = new MessageDialog(shell, IDEWorkbenchMessages.CopyFilesAndFoldersOperation_question, null, msg, MessageDialog.QUESTION, options, 0) {
					@Override
					protected int getShellStyle() {
						return super.getShellStyle() | SWT.SHEET;
					}
				};
				dialog.open();
				int returnVal = dialog.getReturnCode();
				String[] returnCodes = { YES, ALL, NO, CANCEL };
				returnCode[0] = returnVal == -1 ? CANCEL : returnCodes[returnVal];
			}
		});
		if (returnCode[0] == ALL) {
			alwaysOverwrite = true;
		}
		else if (returnCode[0] == CANCEL) {
			canceled = true;
		}
		return returnCode[0];
	}

	public boolean isCanceled() {
		return canceled;
	}
}
