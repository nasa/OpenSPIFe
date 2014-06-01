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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.operations.SelectAllOperation;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchSite;

public class PlanSelectAllOperation extends SelectAllOperation {

	private EPlan plan;
	private IWorkbenchSite site;

	public PlanSelectAllOperation(EPlan plan, IWorkbenchSite site) {
		this.plan = plan;
		this.site = site;
	}

	@Override
	protected void dispose(UndoableState state) {
		plan = null;
		site = null;
	}
	
	@Override
	protected boolean isExecutable() {
		return plan != null && !plan.getChildren().isEmpty();
	}
	
	@Override
	protected void execute() throws Throwable {
		final IWorkbenchSite fSite = site;
		if ((plan != null) && (fSite != null)) {
			final List<EPlanElement> elements = new ArrayList<EPlanElement>(500);
			new PlanVisitor() {
				@Override
				protected void visit(EPlanElement element) {
				    if (element != plan) {
				    	elements.add(element);
				    }
				}
			}.visitAll(plan);
			final ISelection selection = new StructuredSelection(elements);
			WidgetUtils.runInDisplayThread(fSite.getShell(), new Runnable() {
				@Override
				public void run() {
					ISelectionProvider provider = fSite.getSelectionProvider();
					provider.setSelection(selection);
				}
			});
		}
	}
}
