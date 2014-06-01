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
package gov.nasa.ensemble.core.plan.editor.merge.action;

import gov.nasa.ensemble.common.ui.editor.CommandRefreshingActiveEditorPartListener;
import gov.nasa.ensemble.core.plan.editor.MultiPagePlanEditor;
import gov.nasa.ensemble.core.plan.editor.merge.MergeEditor;

import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.commands.IElementUpdater;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.menus.UIElement;
import org.eclipse.ui.services.IServiceLocator;

public class FlattenHandler extends AbstractHandler implements IElementUpdater {

	private static final String FLATTEN_COMMAND_ID = "gov.nasa.ensemble.core.plan.editor.merge.toggle_flatten";
	private static final CommandRefreshingActiveEditorPartListener ACTIVE_EDITOR_LISTENER = new CommandRefreshingActiveEditorPartListener(FLATTEN_COMMAND_ID);
	private IPropertyListener propertyListener;

	@Override
    public void setEnabled(Object evaluationContext) {
		boolean enabled = isEnabled(evaluationContext);
		setBaseEnabled(enabled);
    }

	private boolean isEnabled(Object evaluationContext) {
		Object activeEditor = HandlerUtil.getVariable(evaluationContext, ISources.ACTIVE_EDITOR_NAME);
		// The following line fixes SPF-5785, but there's a better way, as Andrew discovered:
		// TODO:  As of 3.5, the IPartListener2 can also implement IPageChangedListener to be notified about any parts that implement IPageChangeProvider and post PageChangedEvents.
		// That would be a better way, now, of telling when the Table or Timeline tab ("page") is selected. 
		if (activeEditor==null) activeEditor=getActiveEditor();
    	return getMergeEditor(activeEditor) != null;
    }

	/**
	 * @throws ExecutionException
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IToggleFlattenEditor editor = getMergeEditor(HandlerUtil.getActiveEditor(event));
		editor.toggleFlatten();
		IEditorSite editorSite = editor.getEditorSite();
		ICommandService commandService = (ICommandService)editorSite.getService(ICommandService.class);
		commandService.refreshElements(event.getCommand().getId(), null);
		return null;
	}

	@Override
	public void updateElement(UIElement element, Map parameters) {
		IServiceLocator serviceLocator = element.getServiceLocator();
		IPartService partService = (IPartService)serviceLocator.getService(IPartService.class);
		if(propertyListener == null) {
			propertyListener = new IPropertyListener() {
				@Override
				public void propertyChanged(Object source, int propId) {
					if(source instanceof MultiPagePlanEditor) {
						MultiPagePlanEditor multiPagePlanEditor
							= (MultiPagePlanEditor)source;
						IEditorPart editPart = multiPagePlanEditor.getCurrentEditor();
						setBaseEnabled(editPart instanceof MergeEditor);
					}
				}
			};
		}

		IWorkbenchPart workbenchPart = partService.getActivePart();
		if(workbenchPart != null && workbenchPart instanceof IEditorPart) {
			workbenchPart.addPropertyListener(propertyListener);
		}

		partService.addPartListener(ACTIVE_EDITOR_LISTENER); // adding the same listener multiple times is ok and ignored
		boolean checked = false;
		boolean enabled = false;
		Object workbenchWindow = parameters.get("org.eclipse.ui.IWorkbenchWindow");
		if (workbenchWindow instanceof IWorkbenchWindow) {
			IWorkbenchWindow window = (IWorkbenchWindow) workbenchWindow;
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				IEditorPart activeEditor = page.getActiveEditor();
				IToggleFlattenEditor editor = getMergeEditor(activeEditor);
				if (editor != null) {
					checked = editor.isFlattened();
					enabled = true;
				}
			}
		}
	    element.setChecked(checked);
	    this.setBaseEnabled(enabled);
    }

	private static IToggleFlattenEditor getMergeEditor(Object editor) {
    	if (editor instanceof MultiPagePlanEditor) {
    		editor = ((MultiPagePlanEditor)editor).getCurrentEditor();
    	}
	    if (editor instanceof IToggleFlattenEditor) {
	    	return (IToggleFlattenEditor)editor;
	    }
	    return null;
    }
	
	public static IEditorPart getActiveEditor() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench==null) return null;
		IWorkbenchWindow activeWindow = workbench.getActiveWorkbenchWindow();
		if (activeWindow==null) return null;
		IWorkbenchPage activePage = activeWindow.getActivePage();
		if (activePage==null) return null;
		return activePage.getActiveEditor();
	}

}
