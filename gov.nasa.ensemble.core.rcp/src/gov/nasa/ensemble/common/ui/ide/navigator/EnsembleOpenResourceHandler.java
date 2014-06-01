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
package gov.nasa.ensemble.common.ui.ide.navigator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;
import org.eclipse.ui.navigator.CommonNavigator;

/**
 * Implements the open resource action. Opens a dialog prompting for a file and
 * opens the selected file in an editor.
 * 
 * @since 2.1
 */
@SuppressWarnings("restriction")
public abstract class EnsembleOpenResourceHandler extends Action implements IHandler,
		IWorkbenchWindowActionDelegate {

	private IEnsembleArtificialResourceManager artificialResourceManager = null;
	
	/**
	 * The identifier of the parameter storing the file path.
	 */
	private static final String PARAM_ID_FILE_PATH = "filePath"; //$NON-NLS-1$

	/**
	 * A collection of objects listening to changes to this manager. This
	 * collection is <code>null</code> if there are no listeners.
	 */
	private transient ListenerList listenerList = null;

	protected abstract IEnsembleArtificialResourceManager getNewArtificialResourceManager();
	
	protected abstract List<EnsembleCommonNavigator> getEnsembleCommonNavigators();
	
	/**
	 * Creates a new instance of the class.
	 */
	public EnsembleOpenResourceHandler() {
		super();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IIDEHelpContextIds.OPEN_WORKSPACE_FILE_ACTION);
		
		artificialResourceManager = getNewArtificialResourceManager();
	}

	@Override
	public final void addHandlerListener(final IHandlerListener listener) {
		if (listenerList == null) {
			listenerList = new ListenerList(ListenerList.IDENTITY);
		}

		listenerList.add(listener);
	}

	@Override
	public final void dispose() {
		listenerList = null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final Object execute(final ExecutionEvent event)
			throws ExecutionException {
		final List files = new ArrayList();

		if (event.getParameter(PARAM_ID_FILE_PATH) == null) {
			// Prompt the user for the resource to open.
			Object[] result = queryFileResource();

			if (result != null) {
				for (int i = 0; i < result.length; i++) {
					if (result[i] instanceof IFile) {
						files.add(result[i]);
					}
				}
			}

		} else {
			// Use the given parameter.
			final IResource resource = (IResource) event
					.getObjectParameterForExecution(PARAM_ID_FILE_PATH);
			if (!(resource instanceof IFile)) {
				throw new ExecutionException(
						"filePath parameter must identify a file"); //$NON-NLS-1$
			}
			files.add(resource);
		}

		if (files.size() > 0) {

			final IWorkbenchWindow window = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow();
			if (window == null) {
				throw new ExecutionException("no active workbench window"); //$NON-NLS-1$
			}

			final IWorkbenchPage page = window.getActivePage();
			if (page == null) {
				throw new ExecutionException("no active workbench page"); //$NON-NLS-1$
			}

			try {
				for (Iterator it = files.iterator(); it.hasNext();) {
					IFile next = (IFile) it.next();
					
					// if the file exits, open it, otherwise check with the resource manager
					if(next.exists()) {
						IDE.openEditor(page, next, true);
					}
					
					else if (artificialResourceManager.canOpen(next)) {
						artificialResourceManager.open(next);
						
						TreePath treePath = artificialResourceManager.getTreePath(next);
						ISelection selection = new StructuredSelection(treePath);
						// select the node in the appropriate navigator
						List<EnsembleCommonNavigator> commonNavigators = getEnsembleCommonNavigators();
						for (CommonNavigator commonNavigator : commonNavigators) {						
							commonNavigator.selectReveal(selection);
						}
					} 
				}
			} catch (final PartInitException e) {
				throw new ExecutionException("error opening file in editor", e); //$NON-NLS-1$
			}
		}

		return null;
	}

	@Override
	public final void init(final IWorkbenchWindow window) {
		// Do nothing.
	}

	/**
	 * Query the user for the resources that should be opened
	 * 
	 * @return the resource that should be opened.
	 */
	private final Object[] queryFileResource() {
		final IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}
		final Shell parent = window.getShell();
		final IContainer input = ResourcesPlugin.getWorkspace().getRoot();

		final SelectionDialog selectionDialog = getNewSelectionDialogInstance(parent, input, IResource.FILE);
		
		final int resultCode = selectionDialog.open();
		if (resultCode != Window.OK) {
			return null;
		}

		final Object[] result = selectionDialog.getResult();

		return result;
	}
	
	public abstract SelectionDialog getNewSelectionDialogInstance
		(Shell parentShell,	IContainer container, int typesMask);

	@Override
	public final void removeHandlerListener(final IHandlerListener listener) {
		if (listenerList != null) {
			listenerList.remove(listener);

			if (listenerList.isEmpty()) {
				listenerList = null;
			}
		}
	}

	@Override
	public final void run(final IAction action) {
		try {
			execute(new ExecutionEvent());
		} catch (final ExecutionException e) {
			// TODO Do something meaningful and poignant.
		}
	}

	@Override
	public final void selectionChanged(final IAction action,
			final ISelection selection) {
		// Do nothing.
	}
}
