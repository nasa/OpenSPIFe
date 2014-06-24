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
package gov.nasa.ensemble.core.model.common.transactions;

import org.eclipse.emf.transaction.util.Lock;

public class NoDialogLock extends Lock {

	/**
	 * @throws InterruptedException  
	 */
	@Override
	public void uiSafeAcquire(boolean exclusive) throws InterruptedException {
        final Thread current = Thread.currentThread();

        // if we already are interrupted, clear the interrupt status
        // because we will be performing a timed wait that must not
        // be interrupted. We ignore interrupts because we know that
        // the UI thread will often be interrupted by Display.syncExec
        Thread.interrupted();

        // try acquiring it just in case we can avoid scheduling a job.
        // Don't allow the UI thread to be interrupted during this interval
		while (true) {
			try {
				boolean acquired = acquire(Long.MAX_VALUE, exclusive);
		        if (acquired) {
		            assert getOwner() == current;
		            return;
		        }
		        System.out.println(current + " waiting...");
			} catch (InterruptedException e) {
				// ignore it and clear the interrupt status
				Thread.interrupted();
			}
		}
    }

}
