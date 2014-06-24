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
package gov.nasa.ensemble.core.plan.editor.view.template.actions;

import gov.nasa.ensemble.core.plan.editor.view.template.TemplatePlanView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 * Convenience class for implementing TemplatePlanView actions.
 *
 */
public abstract class TemplatePlanViewAction extends Action implements IViewActionDelegate {

	/** The current selection. Supplied by selectionChanged(). May be null. */
	protected IStructuredSelection structuredSelection = null;
	
	/**
	 * The TemplatePlanView that provides the context for this delegate. Supplied by init() and
	 * setTemplatePlanView().
	 */
	protected TemplatePlanView templatePlanView = null;	
	
	/** Return the TemplatePlanView that provides the context for this delegate. May be null. */
	public TemplatePlanView getTemplatePlanView() {
		return templatePlanView;
	}

	/**
	 * Set the TemplatePlanView that provides the context for this delegate.
	 * @param templatePlanView the TemplatePlanView that provides the context for this delegate; may be null
	 */
	public void setTemplatePlanView(TemplatePlanView templatePlanView) {
		this.templatePlanView = templatePlanView;
	}

    /**
     * Initializes this action delegate with the view it will work in.
     *
     * @param view the view that provides the context for this delegate; of interest only if it is a
     * TemplatePlanView
     */
	@Override
	public void init(IViewPart view) {
		if(templatePlanView == null && view instanceof TemplatePlanView ) {
			templatePlanView = (TemplatePlanView)view;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			structuredSelection = (IStructuredSelection)selection;
		}
	}
}
