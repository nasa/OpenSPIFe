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

import java.util.Collections;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.OpenFileAction;
import org.eclipse.ui.actions.OpenWithMenu;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;


/**
 * Shows a list of resources to the user with a text entry field for a string
 * pattern used to filter the list of resources.
 * 
 * @since 2.1
 */
@SuppressWarnings("restriction")
public abstract class EnsembleOpenResourceDialog extends FilteredResourcesSelectionDialog {

	private Button openWithButton;
	private IContainer container;

	/**
	 * Creates a new instance of the class.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param container
	 *            the container
	 * @param typesMask
	 *            the types mask
	 */
	public EnsembleOpenResourceDialog(Shell parentShell, IContainer container,
			int typesMask) {
		super(parentShell, true, container, typesMask);		
		
		setTitle(getTitle());
		setInitialPattern("?");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parentShell,
				IIDEHelpContextIds.OPEN_RESOURCE_DIALOG);
		this.container = container;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#fillContentProvider(org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.AbstractContentProvider,
	 *      org.eclipse.ui.dialogs.FilteredItemsSelectionDialog.ItemsFilter,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void fillContentProvider(AbstractContentProvider contentProvider,
			ItemsFilter itemsFilter, IProgressMonitor progressMonitor)
			throws CoreException {
		if (itemsFilter instanceof ResourceFilter) {
			IResourceProxyVisitor resourceProxyVisitor = getNewResourceProxyVisitor(contentProvider, itemsFilter,
					progressMonitor);
			container.accept(resourceProxyVisitor,
					IResource.NONE);
		}
		if (progressMonitor != null)
			progressMonitor.done();

	}

	protected abstract IResourceProxyVisitor getNewResourceProxyVisitor(
			AbstractContentProvider contentProvider, ItemsFilter itemsFilter,
			IProgressMonitor progressMonitor) throws CoreException;
	
	public abstract String getTitle();

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.FilteredItemsSelectionDialog#fillContextMenu(org.eclipse.jface.action.IMenuManager)
	 * @since 3.5
	 */
	@Override
	protected void fillContextMenu(IMenuManager menuManager) {
		super.fillContextMenu(menuManager);

		IStructuredSelection selectedItems = getSelectedItems();
		if (selectedItems.isEmpty()) {
			return;
		}
		
		IWorkbenchPage activePage = getActivePage();
		if (activePage == null) {
			return;
		}

		// Add 'Open' menu item
		OpenFileAction openFileAction = new OpenFileAction(activePage) {
			@Override
			public void run() {
				okPressed();
			}
		};
		openFileAction.selectionChanged(selectedItems);
		if (!openFileAction.isEnabled()) {
			return;
		}
		menuManager.add(new Separator());
		menuManager.add(openFileAction);

		IAdaptable selectedAdaptable = getSelectedAdaptable();
		if (selectedAdaptable == null) {
			return;
		}

		// Add 'Open With...'  sub-menu
		MenuManager subMenu = new MenuManager(IDEWorkbenchMessages.OpenResourceDialog_openWithMenu_label);
		OpenWithMenu openWithMenu = new OpenWithMenu(activePage, selectedAdaptable) {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ui.actions.OpenWithMenu#openEditor(org.eclipse.ui.IEditorDescriptor, boolean)
			 */
			@Override
			protected void openEditor(IEditorDescriptor editorDescriptor, boolean openUsingDescriptor) {
				computeResult();
				setResult(Collections.EMPTY_LIST);
				close();
				super.openEditor(editorDescriptor, openUsingDescriptor);
			}
		};
		subMenu.add(openWithMenu);
		menuManager.add(subMenu);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.SelectionDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 * @since 3.5
	 */
	@Override
	protected void createButtonsForButtonBar(final Composite parent) {
		// increment the number of columns in the button bar
		GridLayout parentLayout = (GridLayout)parent.getLayout();
		parentLayout.numColumns++;
		parentLayout.makeColumnsEqualWidth = false;
		
		final Composite openComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		openComposite.setLayout(layout);

		Button okButton = createButton(openComposite, IDialogConstants.OK_ID, IDEWorkbenchMessages.OpenResourceDialog_openButton_text, true);

		// Arrow down button for Open With menu
		((GridLayout)openComposite.getLayout()).numColumns++;
		openWithButton = new Button(openComposite, SWT.PUSH);
//		openWithButton.setToolTipText(IDEWorkbenchMessages.OpenResourceDialog_openWithButton_toolTip);
		openWithButton.setImage(WorkbenchImages.getImage(IWorkbenchGraphicConstants.IMG_LCL_BUTTON_MENU));

		GridData data = new GridData(SWT.CENTER, SWT.FILL, false, true);
		openWithButton.setLayoutData(data);

		openWithButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				showOpenWithMenu(openComposite);
			}
		});
		openWithButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				showOpenWithMenu(openComposite);
			}
		});

		Button cancelButton = createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		
		GridData cancelLayoutData = (GridData) cancelButton.getLayoutData();
		GridData okLayoutData = (GridData) okButton.getLayoutData();
		int buttonWidth = Math.max(cancelLayoutData.widthHint, okLayoutData.widthHint);
		cancelLayoutData.widthHint = buttonWidth;
		okLayoutData.widthHint = buttonWidth;
		
		if (openComposite.getDisplay().getDismissalAlignment() == SWT.RIGHT) {
			// Make the default button the right-most button.
			// See also special code in org.eclipse.jface.dialogs.Dialog#initializeBounds()
			openComposite.moveBelow(null);
			if (Util.isCarbon()) {
			okLayoutData.horizontalIndent = -10;
		}
	}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#initializeBounds()
	 * @since 3.5
	 */
	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		if (openWithButton.getDisplay().getDismissalAlignment() == SWT.RIGHT) {
			// Move the menu button back to the right of the default button.
			if (!Util.isMac()) {
				// On the Mac, the round buttons and the big padding would destroy the visual coherence of the split button.
				openWithButton.moveBelow(null);
				openWithButton.getParent().layout();
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#updateButtonsEnableState(org.eclipse.core.runtime.IStatus)
	 * @since 3.5
	 */
	@Override
	protected void updateButtonsEnableState(IStatus status) {
		super.updateButtonsEnableState(status);
		if (openWithButton != null && !openWithButton.isDisposed()) {
			openWithButton.setEnabled(!status.matches(IStatus.ERROR) && getSelectedItems().size() == 1);
		}
	}

	private IAdaptable getSelectedAdaptable() {
		IStructuredSelection s = getSelectedItems();
		if (s.size() != 1) {
			return null;
		}
		Object selectedElement = s.getFirstElement();
		if (selectedElement instanceof IAdaptable) {
			return (IAdaptable) selectedElement;
		}
		return null;
	}

	private IWorkbenchPage getActivePage() {
		IWorkbenchWindow activeWorkbenchWindow= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null) {
			return null;
		}
		return activeWorkbenchWindow.getActivePage();
	}

	private void showOpenWithMenu(final Composite openComposite) {
		IWorkbenchPage activePage = getActivePage();
		if (activePage == null) {
			return;
		}
		IAdaptable selectedAdaptable = getSelectedAdaptable();
		if (selectedAdaptable == null) {
			return;
		}

		OpenWithMenu openWithMenu = new OpenWithMenu(activePage, selectedAdaptable) {
			/*
			 * (non-Javadoc)
			 * @see org.eclipse.ui.actions.OpenWithMenu#openEditor(org.eclipse.ui.IEditorDescriptor, boolean)
			 */
			@Override
			protected void openEditor(IEditorDescriptor editorDescriptor, boolean openUsingDescriptor) {
				computeResult();
				setResult(Collections.EMPTY_LIST);
				close();
				super.openEditor(editorDescriptor, openUsingDescriptor);
			}
		};

		Menu menu = new Menu(openComposite.getParent());
		Control c = openComposite;
		Point p = c.getLocation();
		p.y = p.y + c.getSize().y;
		p = c.getParent().toDisplay(p);

		menu.setLocation(p);
		openWithMenu.fill(menu, -1);
		menu.setVisible(true);
	}

}
