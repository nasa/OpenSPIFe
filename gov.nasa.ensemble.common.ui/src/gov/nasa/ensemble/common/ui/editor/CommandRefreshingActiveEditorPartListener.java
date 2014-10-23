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
/**
 * 
 */
package gov.nasa.ensemble.common.ui.editor;

import java.util.Collections;
import java.util.Map;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.services.IServiceScopes;

/**
 * This class is to be used to refresh the state of any elements associated with a command, whenever the active editor part changes.
 * <p>
 * To use this class, your IHandler implementation should also implement IElementUpdater. Your updateElement method should look like this:
 * 
 * <pre>
 * public void updateElement(UIElement element, Map parameters) {
 *     IServiceLocator serviceLocator = element.getServiceLocator();
 *     IPartService partService = (IPartService)serviceLocator.getService(IPartService.class);
 *     partService.addPartListener(ACTIVE_EDITOR_LISTENER);
 *     ... put the rest of your implementation here
 * }
 * </pre>
 * 
 * You should define ACTIVE_EDITOR_LISTENER as a constant in your handler like this:
 * 
 * <pre>
 * private static final IPartListener ACTIVE_EDITOR_LISTENER = new CommandRefreshingActiveEditorPartListener(PUT_YOUR_COMMAND_ID_HERE);
 * </pre>
 * 
 * @author abachman
 * 
 */

public class CommandRefreshingActiveEditorPartListener implements IPartListener2, IPageChangedListener {

	private final String commandId;
	private IEditorReference oldEditorPartReference;

	public CommandRefreshingActiveEditorPartListener(String commandId) {
		this.commandId = commandId;
	}

	/*
	 * IPartListener2
	 */

	@Override
	public void partActivated(IWorkbenchPartReference partReference) {
		if ((partReference instanceof IEditorReference) && (partReference != oldEditorPartReference)) {
			if (oldEditorPartReference != null) {
				IEditorPart oldEditor = oldEditorPartReference.getEditor(false);
				if (oldEditor instanceof MultiPageEditorPart) {
					MultiPageEditorPart multiPageEditorPart = (MultiPageEditorPart) oldEditor;
					multiPageEditorPart.removePageChangedListener(this);
				}
			}
			oldEditorPartReference = (IEditorReference) partReference;
			IEditorPart newEditor = refreshEditorCommand(oldEditorPartReference);
			if (newEditor instanceof MultiPageEditorPart) {
				MultiPageEditorPart multiPageEditorPart = (MultiPageEditorPart) newEditor;
				multiPageEditorPart.addPageChangedListener(this);
			}
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) { /* do nothing */
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) { /* do nothing */
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) { /* do nothing */
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) { /* do nothing */
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) { /* do nothing */
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) { /* do nothing */
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) { /* do nothing */
	}

	/*
	 * IPageChangedListener
	 */

	@Override
	public void pageChanged(PageChangedEvent event) {
		refreshEditorCommand(oldEditorPartReference);
	}

	/**
	 * Refresh the editor command UI representation through refreshElements.
	 * 
	 * @param editorPartReference
	 * @return
	 */
	protected IEditorPart refreshEditorCommand(IEditorReference editorPartReference) {
		if (editorPartReference == null) {
			return null;
		}
		IEditorPart newEditor = editorPartReference.getEditor(false);
		if (newEditor != null) {
			IEditorSite editorSite = newEditor.getEditorSite();
			ICommandService commandService = (ICommandService) editorSite.getService(ICommandService.class);
			IWorkbenchWindow activeWindow = editorSite.getPage().getWorkbenchWindow();
			Map<String, IWorkbenchWindow> refreshFilter = Collections.singletonMap(IServiceScopes.WINDOW_SCOPE, activeWindow);
			commandService.refreshElements(commandId, refreshFilter);
		}
		return newEditor;
	}

}
