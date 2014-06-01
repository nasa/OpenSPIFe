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
package gov.nasa.ensemble.core.plan.editor.actions;

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

public abstract class AbstractCommandItemContributor extends ContributionItem implements MissionExtendable, PlanEditorCommandConstants {

	protected List<IContributionItem> items = new ArrayList<IContributionItem>();
	
	protected AbstractCommandItemContributor missionSpecificContributor = null;
	private boolean dirty;
	private IMenuListener menuListener = new IMenuListener() {
		@Override
		public void menuAboutToShow(IMenuManager manager) {
			manager.markDirty();
			dirty = true;
		}
	};
	
	public void setMissionSpecificContributor(AbstractCommandItemContributor missionSpecificContributor) {
		this.missionSpecificContributor = missionSpecificContributor;
	}
	
	public AbstractCommandItemContributor() {
		if (missionSpecificContributor == null && MissionExtender.hasMissionSpecificClass(getClass())) {
			try {
				AbstractCommandItemContributor construct = MissionExtender.construct(getClass());
				setMissionSpecificContributor(construct);
			} catch (ConstructionException e) {
				LogUtil.error(e);
			}
		} 
		if (missionSpecificContributor != null) {
			items.addAll(missionSpecificContributor.getItems());
		} else { 
			contributeCommandItems();
		}
	}

	protected abstract void contributeCommandItems();

	/* 
	 * Damn you Eclipse developer who only implemented it for Menus...
	 */
	@Override
	public void fill(ToolBar parent, int index) {
		int i = parent.getItemCount();
		if (parent.isVisible()) {
			for (IContributionItem item : getItems()) {
				item.fill(parent, i++);
			}
		}
		dirty = false;
	}
	
	 @Override
	 public void fill(Menu menu, int index) {	
		 dirty = true;
		 if (index == -1) {
			 index = menu.getItemCount();
		 }
		 if (!dirty && menu.getParentItem() != null) {
			 // insert a dummy item so that the parent item is not disabled
			 new MenuItem(menu, SWT.NONE, index);
			 return;
		 }

		 for (IContributionItem item : getItems()) {
			 int oldItemCount = menu.getItemCount();
			 if (item.isVisible()) {
				 item.fill(menu, index);
			 }
			 int newItemCount = menu.getItemCount();
			 int numAdded = newItemCount - oldItemCount;
			 index += numAdded;
		 }
		 dirty = false;
	 }

	@Override
	public boolean isDirty() {
		 return dirty;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	public List<IContributionItem> getItems() {
		return items;
	}

	/**
	 * Create an IContributionItem for commandId of default style SWT.PUSH 
	 * and contribute to both toolbar and menu.
	 * @param commandId
	 */
	protected void contribute(String commandId) {
		contribute(commandId, SWT.PUSH);
	}
	
	/**
	 * Create an IContributionItem for commandId of specified style 
	 * (SWT.PUSH or SWT.CHECK) and contribute to both toolbar and menu.
	 * @param commandId
	 * @param style
	 */
	protected void contribute(String commandId, int style) {
		CommandContributionItem item = createCommandContributionItem(commandId, style);
		getItems().add(item);
	}
	
	protected static CommandContributionItem createCommandContributionItem(String commandId, int style) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		CommandContributionItemParameter parameter = new CommandContributionItemParameter(workbench, null, commandId, style);
		return new CommandContributionItem(parameter);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		for (IContributionItem item : getItems()) {
			item.dispose();
		}
		getItems().clear();
	}

	@Override
	public void setParent(IContributionManager parent) {
		if (getParent() instanceof IMenuManager) {
			IMenuManager menuMgr = (IMenuManager) getParent();
			menuMgr.removeMenuListener(menuListener);
		}
		if (parent instanceof IMenuManager) {
			IMenuManager menuMgr = (IMenuManager) parent;
			menuMgr.addMenuListener(menuListener);
		}
		super.setParent(parent);
	}

}
