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
package gov.nasa.ensemble.core.model.common.transactions;

import gov.nasa.ensemble.common.logging.LogUtil;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.InternalTransaction;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

@SuppressWarnings("restriction")
public class TransactionUtils extends gov.nasa.ensemble.emf.transaction.TransactionUtils {
	
	/**
	 * Run the runnable in the display thread while holding the read lock for the object.
	 * The runnable will be queued to run asynchronously when the lock becomes available,
	 * or the runnable will run synchronously if this thread is the display thread and
	 * the lock is already held.
	 * 
	 * @param control
	 * @param object
	 * @param runnable
	 */
	public static void runInDisplayThread(Control control, Object object, Runnable runnable) {
		if (control.isDisposed())
			return;
		Display display = control.getDisplay();
		if ((display != null) && (object != null) && !display.isDisposed()) {
			TransactionalEditingDomain domain = getDomain(object);
			if (domain instanceof FixedUITransactionEditingDomain) {
				FixedUITransactionEditingDomain fixed = (FixedUITransactionEditingDomain) domain;
				InternalTransaction active = fixed.getActiveTransaction();
				if (((display.getThread() != Thread.currentThread())
					|| (active == null)
					|| !(active.isActive() && active.isReadOnly()
						&& (active.getOwner() == Thread.currentThread())))) {
					fixed.queueReading(control, runnable);
				} else {
					runnable.run();
				}
			} else if (domain != null) {
				TransactionUtils.reading(object, runnable);
			} else {
				LogUtil.warn("failed to get the transaction domain for runnable: " + runnable);
			}
		}
	}

}
