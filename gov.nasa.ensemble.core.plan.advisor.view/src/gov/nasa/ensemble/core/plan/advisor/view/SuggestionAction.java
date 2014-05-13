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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.core.plan.advisor.Suggestion;

import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Event;

public class SuggestionAction extends Action {

	private final Suggestion suggestion;
	private final IUndoContext undoContext;

	public SuggestionAction(Suggestion suggestion, IUndoContext undoContext) {
		super(suggestion.getDescription(), suggestion.getIcon());
		this.suggestion = suggestion;
		this.undoContext = undoContext;
	}
	
	@Override
	public boolean isEnabled() {
		return suggestion.canExecute();
	}
	
	@Override
	public void runWithEvent(Event event) {
		suggestion.execute(undoContext, event.widget, null);
	}
	
}
