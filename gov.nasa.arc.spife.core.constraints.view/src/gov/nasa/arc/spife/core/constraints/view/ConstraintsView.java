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
package gov.nasa.arc.spife.core.constraints.view;

import gov.nasa.ensemble.common.help.ContextProvider;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanPageBookView;

import org.eclipse.help.IContextProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.Page;

/**
 * A page summarizing TemporalBound and TemporalRelation members
 */
public class ConstraintsView extends PlanPageBookView {
	
	private static final String DEFAULT_MESSAGE = "This is where constraints will be listed, once you create or open a plan.";
	public static final String ID = "gov.nasa.arc.spife.core.constraints.view.ConstraintsView";
	
    public ConstraintsView() {
	    super(DEFAULT_MESSAGE);
    }

    @Override
    protected Page createPage(IEditorPart editor, PlanEditorModel model) {
        return new ConstraintsPage(editor, model);
    }

    @Override
	protected void pageActivated(IPage page) {
	    if (page instanceof ConstraintsPage) {
	    	ConstraintsPage constraintsPage = (ConstraintsPage)page;
	    	final EPlan plan = constraintsPage.getPlan();
			WidgetUtils.runInDisplayThread(page.getControl(), new Runnable() {
				@Override
				public void run() {
					String planName = plan.getName();
					if(planName == null) {
						planName = "";
					}
					setContentDescription(planName);
				}
			});
	    } else {
			setContentDescription("");
		}
    }

	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IContextProvider.class)) {
			return new ContextProvider(ID);
		}
		return super.getAdapter(key);
	}

}
