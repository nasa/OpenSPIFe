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
package gov.nasa.arc.spife.ui.timeline.action;

import gov.nasa.arc.spife.ui.timeline.Activator;
import gov.nasa.arc.spife.ui.timeline.Timeline;
import gov.nasa.arc.spife.ui.timeline.util.TimelineUtils;
import gov.nasa.ensemble.common.ui.GlobalAction;
import gov.nasa.ensemble.common.ui.editor.CommandRefreshingActiveEditorPartListener;
import gov.nasa.ensemble.common.ui.editor.EditorPartUtils;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.services.IServiceLocator;

public abstract class AbstractTimelineCommandHandler extends AbstractHandler
	implements IElementUpdater {

	protected CommandRefreshingActiveEditorPartListener partListener;
	protected IPropertyListener propertyListener;

	/*
	 * Methods to be implemented by subclasses
	 */
	public abstract String getCommandId();
	@Override
	public abstract Object execute(ExecutionEvent event) throws ExecutionException;

	protected void installListeners(IPartService partService) {
		IWorkbenchPart workbenchPart = partService.getActivePart();

		// initialize the listeners
		if(partListener == null) {
			partListener = new CommandRefreshingActiveEditorPartListener(getCommandId()) {
				@Override
				public void partHidden(IWorkbenchPartReference partRef) {
					checkUpdateEnablement(partRef);
				}

				private void checkUpdateEnablement(IWorkbenchPartReference partRef) {
					IWorkbenchPart part = partRef.getPart(false);
					if(part instanceof IEditorPart) {
						IEditorPart editorPart = (IEditorPart)part;
						Object adapter = editorPart.getAdapter(Timeline.class);
						if(adapter != null) {
							updateEnablement();
						}
					}
				}

				@Override
				public void partVisible(IWorkbenchPartReference partRef) {
					checkUpdateEnablement(partRef);
				}										
			};					
		}

		if(propertyListener == null) {
			propertyListener = new IPropertyListener() {
				@Override
				public void propertyChanged(Object source, int propId) {
					updateEnablement();
				}
			};
		}

		// add the listeners
		partService.addPartListener(partListener);

		if(workbenchPart != null && workbenchPart instanceof IEditorPart) {
			workbenchPart.addPropertyListener(propertyListener);
		}
	}
	
	protected static IEditorPart getCurrentEditorPart() {
		IWorkbench workbench = Activator.getDefault().getWorkbench();
		return EditorPartUtils.getCurrent(workbench);
	}
	
	protected static Timeline getTimeline() {
		IEditorPart currentEditorPart = getCurrentEditorPart();
		return TimelineUtils.getTimeline(currentEditorPart);
	}
	
	protected Timeline getTimeline(ExecutionEvent event) {
		return TimelineUtils.getTimeline(event);
	}
	
	protected void updateEnablement() {
		updateGlobalActions();
		Timeline<?> timeline = getTimeline();
		IEditorPart editorPart = getCurrentEditorPart();
		boolean editorIsVisible = false;
		if(editorPart != null && editorPart.getSite().getPage().isEditorAreaVisible()) {
			editorIsVisible = true;
		}
		boolean enabled = timeline != null && editorIsVisible;
		if(this.isEnabled() != enabled) {
			this.setBaseEnabled(enabled);
		}
	}
	
	public static boolean isTimelineActive() {
		IWorkbench workbench = Activator.getDefault().getWorkbench();
		IEditorPart current = EditorPartUtils.getCurrent(workbench);
		if (current instanceof EditorPart) {
			IEditorPart activeEditor = current;
			if (activeEditor instanceof MultiPageEditorPart) {
				MultiPageEditorPart editor = (MultiPageEditorPart) current;
				int activePage = editor.getActivePage();
				if (activePage >= 0) {
					IEditorPart[] subEditors = editor.findEditors(editor.getEditorInput());
					if (activePage < subEditors.length) {
						activeEditor = subEditors[activePage];
					}
				}
			}
			Object adapter = activeEditor.getAdapter(Timeline.class);
			if (adapter != null) {
				return true;
			}
		}
		return false;
	}


	@Override
	@SuppressWarnings("unchecked")
	public final void updateElement(UIElement element, Map parameters) {
		IServiceLocator serviceLocator = element.getServiceLocator();
		IPartService partService = (IPartService) serviceLocator
				.getService(IPartService.class);

		installListeners(partService);
		updateEnablement();
	}

	/**
	 * Hook to tie in to global actions
	 */
	protected void updateGlobalActions() {
		Set<GlobalAction> globalActions = GlobalAction.getAllInstances();
		for (GlobalAction globalAction : globalActions) {
			if (globalAction.getAdapter(Timeline.class) != null) {
				IEditorPart editorPart = EditorPartUtils.getCurrent();
				boolean editorAreaVisible = false;
				if(editorPart != null) {
					IEditorSite editorSite = editorPart.getEditorSite();
					IWorkbenchPage page = editorSite.getPage();
					editorAreaVisible = page.isEditorAreaVisible();
				}
				globalAction.setEnabled(getTimeline() != null && editorAreaVisible);
			}
		}
	}
	
	/**
	 * Get the current command for this handler. The command instance can change
	 * based on the command service, which changes based on the active workbench
	 * window.
	 * 
	 * @return the current command for this handler.
	 */
	protected org.eclipse.core.commands.Command getCommand() {
		org.eclipse.core.commands.Command command = null;
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		if(workbench != null) {
			IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
			if(activeWorkbenchWindow != null) {
				Object service = activeWorkbenchWindow.getService(ICommandService.class);
				
				if(service != null && service instanceof ICommandService) {
					ICommandService commandService = (ICommandService)service;
					command = commandService.getCommand(getCommandId());
				}
			}
		}
		
		return command;		
	}
}
