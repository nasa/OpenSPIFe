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
package gov.nasa.ensemble.core.rcp;

import gov.nasa.ensemble.common.ui.wizard.WizardUtils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

/**
 * This file provides the basic capabilities for a typical Ensemble application by setting up
 * the typical menus.  
 *
 */
@SuppressWarnings("restriction")
public class EnsembleActionBarAdvisor extends ActionBarAdvisor {
	
	private Map<ActionFactory, IWorkbenchAction> globalActions = new HashMap<ActionFactory, IWorkbenchAction>();
	
	/**
	 * Creates an EnsembleActionBarAdvisor, this calls the parent ActionBarAdvisor constructor.  
	 * This is the only public constructor.
	 * 
	 * @param configurer The 
	 */
	public EnsembleActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
		PlatformUI.getPreferenceStore().setValue(
				IWorkbenchPreferenceConstants.DYNAMIC_HELP_ACTION_TEXT, "Related Help");
		PlatformUI.getPreferenceStore().setValue(
				IWorkbenchPreferenceConstants.HELP_CONTENTS_ACTION_TEXT, "User Guide");
	}

	public IWorkbenchAction getGlobalAction(ActionFactory actionFactory) {
		IWorkbenchAction action = globalActions.get(actionFactory);
		if (action == null) {
			IWorkbenchWindow window = getWindow();
			action = actionFactory.create(window);
			if (action != null) { // hopefully action isn't ever null here
				if (actionFactory == ActionFactory.DYNAMIC_HELP) {
			        action.setImageDescriptor(RcpPlugin.getImageDescriptor("icons/related_help_16_16.gif"));
				} else if (actionFactory == ActionFactory.SAVE) {
					action.setImageDescriptor(RcpPlugin.getImageDescriptor("icons/save.png"));
					action.setDisabledImageDescriptor(RcpPlugin.getImageDescriptor("icons/save-disabled.png"));
				} else if (actionFactory == ActionFactory.SAVE_ALL) {
					action.setImageDescriptor(RcpPlugin.getImageDescriptor("icons/save-all.png"));
					action.setDisabledImageDescriptor(RcpPlugin.getImageDescriptor("icons/save-all-disabled.png"));
				} else if (actionFactory == ActionFactory.SAVE_AS) {
					action.setImageDescriptor(RcpPlugin.getImageDescriptor("icons/save-as.png"));
					action.setDisabledImageDescriptor(RcpPlugin.getImageDescriptor("icons/save-as-disabled.png"));
				}

				register(action);
				globalActions.put(actionFactory, action);
			}
		}
		return action;
	}
	
	/**
	 * Create the file, edit, window, view, and help menu items.
	 * 
	 * @param menuBar The application's menu bar.
	 */
	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		IMenuManager menu = createFileMenu();
		if (menu != null) menuBar.add(menu);
		menu = createEditMenu();
		if (menu != null) menuBar.add(menu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menu = createWindowMenu();
		if (menu != null) menuBar.add(menu);
		menu = createViewMenu();
		if (menu != null) menuBar.add(menu);
		menu = createAdminMenu();
		if (menu != null) menuBar.add(menu);
		menu = createHelpMenu();
		if (menu != null) menuBar.add(menu);
	}
	
	protected IMenuManager createAdminMenu() {
		IMenuManager menu = new MenuManager("Admin", IEnsembleActionConstants.M_ADMIN);
		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		return menu;
	}

	/**
	 * Create the "File" menu.
	 * 
	 * @param window
	 * @return the menu
	 */
	protected IMenuManager createFileMenu() {
		IMenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_file, IWorkbenchActionConstants.M_FILE);    
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		menu.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
		menu.add(new GroupMarker(IWorkbenchActionConstants.OPEN_EXT));
		menu.add(new Separator());
		menu.add(getGlobalAction(ActionFactory.CLOSE));
		menu.add(getGlobalAction(ActionFactory.CLOSE_ALL));
		menu.add(new GroupMarker(IWorkbenchActionConstants.CLOSE_EXT));
		menu.add(new Separator());
		menu.add(getGlobalAction(ActionFactory.SAVE));
		menu.add(getGlobalAction(ActionFactory.SAVE_AS));
		menu.add(getGlobalAction(ActionFactory.SAVE_ALL));
//		menu.add(getGlobalAction(ActionFactory.REVERT));
		menu.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));
		menu.add(new Separator());
		menu.add(createActionFactoryCommandContributionItem(ActionFactory.PRINT));
		menu.add(new GroupMarker(IWorkbenchActionConstants.PRINT_EXT));
		menu.add(new Separator());
		IWorkbenchWindow workbenchWindow = this.getWindow();
		ISelection selection = workbenchWindow.getSelectionService().getSelection();
		WizardUtils.addImportWizardSubmenu(menu, getGlobalAction(ActionFactory.IMPORT), workbenchWindow, selection);
		WizardUtils.addExportWizardSubmenu(menu, getGlobalAction(ActionFactory.EXPORT), workbenchWindow, selection);
		menu.add(new GroupMarker(IWorkbenchActionConstants.IMPORT_EXT));
		menu.add(new Separator());
		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
//		menu.add(ContributionItemFactory.REOPEN_EDITORS.create(getWindow()));
		menu.add(new Separator());
		menu.add(getGlobalAction(ActionFactory.QUIT));
		menu.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		return menu;
	}

	private CommandContributionItem createActionFactoryCommandContributionItem(ActionFactory factory) {
		IWorkbenchWindow window = getWindow();
		CommandContributionItemParameter parameter = new CommandContributionItemParameter(window, factory.getId(), factory.getCommandId(), SWT.PUSH);
		IWorkbenchAction action = factory.create(window);
		parameter.icon = action.getImageDescriptor();
		parameter.disabledIcon = action.getDisabledImageDescriptor();
		parameter.label = action.getText();
		parameter.tooltip = action.getToolTipText();
		window.getPartService().removePartListener((RetargetAction)action);
		return new CommandContributionItem(parameter);
	}

	/**
	 * Create the "Edit" menu. This adds the undo, redo, cut, copy, paste,
	 * delete, select all, and collapse all options to the menu.
	 * 
	 * @param window
	 * @return
	 */
	protected IMenuManager createEditMenu() {
		IMenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_edit, IWorkbenchActionConstants.M_EDIT);
		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));
		menu.add(getGlobalAction(ActionFactory.UNDO));
		menu.add(getGlobalAction(ActionFactory.REDO));
		menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
		menu.add(new Separator());
		menu.add(getGlobalAction(ActionFactory.CUT));
		menu.add(getGlobalAction(ActionFactory.COPY));
		menu.add(getGlobalAction(ActionFactory.PASTE));
//		menu.add(getGlobalAction(EnsembleActionFactory.PASTE_SPECIAL));
		menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
		menu.add(new Separator());
		menu.add(getGlobalAction(ActionFactory.DELETE));
		menu.add(getGlobalAction(ActionFactory.SELECT_ALL));
		menu.add(new Separator());
		menu.add(getGlobalAction(ActionFactory.FIND));
		menu.add(new GroupMarker(IWorkbenchActionConstants.FIND_EXT));
		menu.add(new Separator());
		menu.add(new GroupMarker(IWorkbenchActionConstants.ADD_EXT));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
		return menu;
	}
	
	/**
	 * Create the "Window" menu.
	 * file/new
	 * @return
	 */
	protected IMenuManager createWindowMenu() {
		IWorkbenchWindow window = getWindow();
		MenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_window, IWorkbenchActionConstants.M_WINDOW);
		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS+"2"));
		menu.add(new Separator());
		MenuManager perspectiveMenu = new MenuManager("Open Perspective", "openPerspective");
		IContributionItem perspectiveList = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);
		perspectiveMenu.add(perspectiveList);
		menu.add(perspectiveMenu);
		MenuManager viewMenu = new MenuManager("Show View", "showView");
		IContributionItem viewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		viewMenu.add(viewList);
		menu.add(viewMenu);
		menu.add(ActionFactory.RESET_PERSPECTIVE.create(window));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		IWorkbenchAction newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
		newWindowAction.setText("New Window");
		menu.add(newWindowAction);
		IWorkbenchAction preferences = ActionFactory.PREFERENCES.create(window);
		String oldText = preferences.getText();
		if (!oldText.contains("...")) {
			preferences.setText(oldText+"...");
		}
		ActionContributionItem preferenceItem = new ActionContributionItem(preferences);
		menu.add(preferenceItem);
		if (Platform.OS_MACOSX.equals(Platform.getOS())) {
			preferenceItem.setVisible(false);
		}
		menu.add(ContributionItemFactory.OPEN_WINDOWS.create(window));
		return menu;
	}

	protected final IWorkbenchWindow getWindow() {
		return getActionBarConfigurer().getWindowConfigurer().getWindow();
	}
	
	/**
	 * Create the "View" menu.
	 * @return The view MenuManager
	 */
	protected MenuManager createViewMenu() {
		MenuManager menu = new MenuManager("View", 	"view");
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		return menu;
	}
	
	/**
	 * Create the "Help" menu.
	 * 
	 * @param window
	 * @return the help MenuManager
	 */
	protected IMenuManager createHelpMenu() {
		IMenuManager menu = new MenuManager(IDEWorkbenchMessages.Workbench_help, IWorkbenchActionConstants.M_HELP);
		menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
        menu.add(new Separator());
        menu.add(getGlobalAction(ActionFactory.DYNAMIC_HELP));
        menu.add(getGlobalAction(ActionFactory.HELP_CONTENTS));
		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
        menu.add(new Separator());
        IWorkbenchAction about = getGlobalAction(ActionFactory.ABOUT);
		ActionContributionItem aboutItem = new ActionContributionItem(about);
		menu.add(aboutItem);
		if (Platform.OS_MACOSX.equals(Platform.getOS())) {
			aboutItem.setVisible(false);
		}
		return menu;
	}
	
	/**
	 * Adds the tool bar below the application menu and contains the undo, redo, and save buttons.
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		ToolBarContributionItem toolBar = createFileToolbar(coolBar);
		if (toolBar != null) coolBar.add(toolBar);
		toolBar = createEditToolbar(coolBar);
		if (toolBar != null) coolBar.add(toolBar);
		coolBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		coolBar.add(new GroupMarker("testStart"));
		coolBar.add(new GroupMarker("testEnd"));
	}

	protected ToolBarContributionItem createFileToolbar(ICoolBarManager coolBar) {
		IToolBarManager toolBar = new ToolBarManager(coolBar.getStyle());
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.FILE_START));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.NEW_EXT));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.OPEN_EXT));
		toolBar.add(getGlobalAction(ActionFactory.SAVE));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.SAVE_EXT));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.IMPORT_EXT));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.FILE_END));
		return new ToolBarContributionItem(toolBar, IWorkbenchActionConstants.M_FILE);
	}
	
	protected ToolBarContributionItem createEditToolbar(ICoolBarManager coolBar) {
		IToolBarManager toolBar = new ToolBarManager(coolBar.getStyle());
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));
		toolBar.add(getGlobalAction(ActionFactory.UNDO));
		toolBar.add(getGlobalAction(ActionFactory.REDO));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.ADD_EXT));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		toolBar.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
		return new ToolBarContributionItem(toolBar, IWorkbenchActionConstants.M_EDIT);
	}

}
