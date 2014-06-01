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
package gov.nasa.ensemble.common.ui;

import gov.nasa.ensemble.common.logging.LogUtil;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public abstract class GlobalAction extends Action implements IAdaptable
{
	private static Set<GlobalAction> globalActions = new LinkedHashSet<GlobalAction>();

	protected GlobalAction() {
		if(getId() == null) {
			LogUtil.warn(this.getClass().getSimpleName() + " has no id.");
			return;
		} else {
			Object existingAction = getAdapter(getClass());
			if(existingAction != null) {
				throw new RuntimeException("singleton global action already exists!");
			} else {
				addToSet(this);
				updateActionBars(this);
			}
		}
	}

	private static void addToSet(final GlobalAction globalAction) {
		globalActions.add(globalAction);
	}

	public static void updateActionBars() {
		for (GlobalAction globalAction : globalActions) {
			updateActionBars(globalAction);
		}
	}

	private static void updateActionBars(final GlobalAction globalAction)
	{
		Display display = WidgetUtils.getDisplay();
		if (display != null) {
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					IWorkbench workbench = PlatformUI.getWorkbench();
					IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
					for (IWorkbenchWindow window : windows) {
						for (IWorkbenchPage page : window.getPages()) {
							IViewReference[] viewReferences = page.getViewReferences();
							for (IViewReference viewReference : viewReferences) {
								IViewPart viewPart = viewReference.getView(false);
								if (viewPart != null) {
									IViewSite viewSite = viewPart.getViewSite();
									IActionBars actionBars = viewSite.getActionBars();
									actionBars.setGlobalActionHandler(globalAction.getId(), globalAction);
									viewSite.getActionBars().updateActionBars();
								}
							}
						}
					}
				}
			});
		} else {
			LogUtil.warn("display is null, can't update global actions in display thread.");
		}
	}

	public GlobalAction(String text, int style) {
		super(text, style);
		if (this.getId() == null) {
			return;
		} else {
			addToSet(this);
			updateActionBars(this);
		}
	}

	public static Set<GlobalAction> getAllInstances() {
		return globalActions;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if(adapter.equals(GlobalAction.class)) {
			for(GlobalAction globalAction : globalActions) {
				if(globalAction.getClass().equals(adapter)) {
					return globalAction;
				}
			}
		}
		
		return null;
	}

	@Override
	public String getId() {
		String id = super.getId();
		if(id == null) {
			id = String.valueOf(this.hashCode());
		}

		return id;
	}
}
