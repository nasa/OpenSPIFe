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
package gov.nasa.ensemble.core.plan.editor.view.template;

import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.editor.view.template.actions.TemplatePlanViewAddNewItemAction;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;

public class TemplatePlanViewAddNewItemPulldownAction extends TemplatePlanViewAddAction implements IMenuCreator {
	private List<EActivityDef> activityDefs;
	private EActivityDef activityDef;
	private Menu menu;
	private List<ActionContributionItem> items;
	private static final String NAME = "name";
	
	protected void setActivityDef(EActivityDef activityDef) {
		this.activityDef = activityDef;
		this.setText(this.getText());
	}    	    
	
	public TemplatePlanViewAddNewItemPulldownAction(final TemplatePlanView templatePlanView) {
		super("", IAction.AS_DROP_DOWN_MENU, templatePlanView);
		items = new ArrayList<ActionContributionItem>();
		ActivityDictionary activityDictionary = ActivityDictionary.getInstance();
		List<EActivityDef> activityDefs = activityDictionary.getActivityDefs();
		this.activityDefs = new ArrayList<EActivityDef>();
		this.activityDefs.addAll(activityDefs);
		
		// re-purpose the variable, don't need the original list anymore
		activityDefs = this.activityDefs;
		
		Collections.sort(activityDefs, new Comparator<EActivityDef>() {
			@Override
			public int compare(EActivityDef arg0, EActivityDef arg1) {
				String displayName0 = EPlanUtils.getDisplayName(NAME, arg0, null);
				String displayName1 = EPlanUtils.getDisplayName(NAME, arg1, null);
				return displayName0.compareTo(displayName1);
			}
		}); 
		if(activityDefs.size() > 0) {
			activityDef = activityDefs.get(0);
		}
		initializeActionContributionItems();
	}
	
	private void initializeActionContributionItems() {
		for(final EActivityDef activityDef : activityDefs) {
			String activityName = EPlanUtils.getDisplayName(NAME, activityDef, null);
			TemplatePlanViewAddNewItemAction action = new TemplatePlanViewAddNewItemAction(activityName) {
				
				@Override
				public void run() {		
					super.run();
					TemplatePlanViewAddNewItemPulldownAction.this.setActivityDef(activityDef);
				}
				@Override
				public int getStyle() {
					return IAction.AS_CHECK_BOX;
				}
				@Override
				public boolean isChecked() {
					return activityDef.equals(TemplatePlanViewAddNewItemPulldownAction.this.activityDef);
				}																		
			};
			ActionContributionItem actionContributionItem = new ActionContributionItem(action);
			action.setTemplatePlanView(templatePlanView);
			items.add(actionContributionItem);
		}
	}    	 	
	
	@Override
	public String getText() {
		if (activityDef == null) {
			return "";
		}
		 return EPlanUtils.getDisplayName(NAME, activityDef, null);
	}

	@Override
	public void dispose() {
		super.dispose();
		
    	if(activityDefs != null) {
    		activityDefs.clear();
    		activityDef = null;
    	}

    	activityDef = null;
    	
    	if(menu != null && !menu.isDisposed()) {
    		menu.dispose();
    	}
	}

	@Override
	public IMenuCreator getMenuCreator() {
		return this;
	}	
	
	@Override
	public void run() {
		for(ActionContributionItem item : items) {
			IAction action = item.getAction();
			if(action.isChecked()) {
				action.run();
			}
		}
	}

	@Override
	public Menu getMenu(Control parent) {
		if(menu != null && !menu.isDisposed()) {
			menu.dispose();
		}
		menu = new Menu(parent);
		
		int i = 0;
		for(ActionContributionItem item : items) {
			item.fill(menu, i);
			i++;
		}		
		
		return menu;
	}

	@Override
	public Menu getMenu(Menu parent) {
		return null;
	}

	/**
	 * Given a particular selection, determine if this action should be enabled
	 * @param selection a selection
	 * @return true if this action should be enabled, false otherwise
	 */
	@Override
	public boolean shouldBeEnabled(ISelection selection) {
		boolean enabled = false;
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			// Enable for an empty selection as well as a single selection
			if(structuredSelection.size() <= 1) {
				enabled = true;
			}
		} else {
			enabled = false;
		}
		return enabled;
	}
	
}
