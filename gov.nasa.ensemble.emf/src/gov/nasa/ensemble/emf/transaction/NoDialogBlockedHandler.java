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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogBlockedHandler;
import org.eclipse.swt.widgets.Shell;

/* package */ final class NoDialogBlockedHandler implements IDialogBlockedHandler {

	private int nestingDepth = 0;
	
    @Override
	public void showBlocked(IProgressMonitor blocking, IStatus blockingStatus, String blockedName) {
        showBlocked(null, blocking, blockingStatus, blockedName);
    }

    @Override
	public void showBlocked(Shell parentShell, IProgressMonitor blocking, IStatus blockingStatus, String blockedName) {
//	    	System.out.println("showBlocked");
    	nestingDepth++;
    }

    @Override
	public void clearBlocked() {
//	    	System.out.println("clearBlocked");
    	if (nestingDepth > 0) {
    		nestingDepth--;
    	} else {
    		nestingDepth = 0;
    	}
    }

}
