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

import gov.nasa.ensemble.common.ui.WidgetUtils;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPartSite;

public final class Suggestion {

	private final ImageDescriptor icon;
	private final String description;
	private final IUndoableOperation operation;

	/**
	 * Create a suggestion with no icon.  See getters for parameter descriptions.
	 * @param description
	 * @param operation
	 */
	public Suggestion(String description, IUndoableOperation operation) {
		this(null, description, operation);
	}

	/**
	 * Create a suggestion.  See getters for parameter descriptions.
	 * @param icon
	 * @param description
	 * @param operation
	 */
	public Suggestion(ImageDescriptor icon, String description, IUndoableOperation operation) {
		super();
		this.icon = icon;
		this.description = (description != null ? description : operation.getLabel());
		this.operation = operation;
	}

	/**
	 * Returns an icon for the suggestion.
	 * The icon might be presented in a menu. 
	 * @return
	 */
	public ImageDescriptor getIcon() {
		return icon;
	}
	
	/**
	 * Returns a description of the suggestion.
	 * This string should be no longer than 80 characters for
	 * reasonable presentation in a menu.
	 * @return
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns true if this suggestion can be executed
	 * 
	 * @return
	 */
	public boolean canExecute() {
		return operation.canExecute();
	}
	
	/**
	 * Perform this suggestion
	 * 
	 * @param undoContext 
	 * @param widget 
	 * @param site 
	 */
	public void execute(IUndoContext undoContext, Widget widget, IWorkbenchPartSite site) {
		if (operation instanceof ISuggestionOperation) {
			ISuggestionOperation suggestionOperation = (ISuggestionOperation) operation;
			if (!suggestionOperation.preExecute()) {
				return;
			}
		}
		WidgetUtils.execute(operation, undoContext, widget, site);
	}
	
}
