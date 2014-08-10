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
/**
 * 
 */
package gov.nasa.ensemble.emf.transaction;

import gov.nasa.ensemble.common.logging.LogUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogBlockedHandler;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/* package */class FixedUITransactionEditingDomain extends FixedTransactionEditingDomain {

	static {
		if (PlatformUI.isWorkbenchRunning()) { // check for JUnit
			Display display = Display.findDisplay(Thread.currentThread());
			if (display != null) {
				hideBlockedDialog();
			} else {
				display = PlatformUI.getWorkbench().getDisplay();
				if (display != null) {
					display.asyncExec(new Runnable() {
						@Override
						public void run() {
							hideBlockedDialog();
						}
					});
				} else {
					LogUtil.warn("The workbench had no display, could not suppress the default blocked handler.");
				}
			}
		}
	}

	private static void hideBlockedDialog() {
		// first instantiate ProgressManager, and set the blocked handler there
		PlatformUI.getWorkbench().getProgressService();
		IDialogBlockedHandler oldHandler = Dialog.getBlockedHandler();
		// override it with our chosen handler
		Dialog.setBlockedHandler(new NoDialogBlockedHandler());
		// then clear it if it was up
		oldHandler.clearBlocked();
	}

	private TransactionDisplayThread thread = new TransactionDisplayThread(this);

	FixedUITransactionEditingDomain(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	@Override
	public void dispose() {
		thread.quit();
		super.dispose();
	}

	public void queueWriting(Control control, Runnable runnable) {
		thread.queueWriting(control, runnable);
	}

	public void queueReading(Control control, Runnable runnable) {
		thread.queueReading(control, runnable);
	}

}
