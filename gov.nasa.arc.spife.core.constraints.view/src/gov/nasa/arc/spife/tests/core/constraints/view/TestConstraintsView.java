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
/*
 */
package gov.nasa.arc.spife.tests.core.constraints.view;

import org.apache.log4j.Logger;
import junit.framework.TestCase;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 */
public class TestConstraintsView extends TestCase {

	public void testCreateConstraintsView() throws Exception {
		if (PlatformUI.isWorkbenchRunning() == false) {
			return; // TODO: obtain a workbench
		}
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage();
		
		IViewPart view = null;
		// Make the view
		view = workbenchPage.showView(
				"gov.nasa.arc.spife.core.constraints.view.ConstraintsView",
				null,
				IWorkbenchPage.VIEW_CREATE);
		assertNotNull(view);
	}
	
	/* Setup logging */
	@SuppressWarnings("unused")
	private static final Logger trace = Logger.getLogger("spife.core.constraints.view");

}
