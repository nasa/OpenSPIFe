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
package gov.nasa.ensemble.core.model.plan.diff.report.viewer;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.IViewerCreator;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;

public class PlanDiffViewerCreator implements IViewerCreator {

//	public Viewer createViewer(Composite parent, CompareConfiguration config) {
//		System.out.println("PlanDiffViewerCreator.createViewer()");
//		TreeViewer treeViewer = new TreeViewer(parent);
//		treeViewer.setLabelProvider(new PlanDiffLabelProvider());
//		treeViewer.setContentProvider(new PlanDiffContentProvider());
//		return treeViewer;
//	}
	
	@SuppressWarnings("restriction")
	public Viewer createViewer(Composite parent, CompareConfiguration config) {
		// TODO Auto-generated method stub
		PlanDiffBrowserViewer browser = new PlanDiffBrowserViewer(parent);
		return browser;
	}

}
