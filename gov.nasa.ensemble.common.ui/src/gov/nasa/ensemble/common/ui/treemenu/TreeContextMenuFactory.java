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
package gov.nasa.ensemble.common.ui.treemenu;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;

/**
 * A simple API for a right-click context menu on a tree whose menu items
 * depend on the object in the tree, especially its class.
 * Supports flat menus or one level of cascading.
 * @see ContextualCommandContributor
 * @see gov.nasa.ensemble.common.ui.contextmenu.test.ExampleOfMenuContributorContainer
 * @author kanef
 *
 */
public class TreeContextMenuFactory {

	private IContextualCommandContributor[] contributorProviders;

	public TreeContextMenuFactory(IContextualCommandContributor... contributorProviders) {
		this.contributorProviders = contributorProviders;
	}
	
	public void makePopupMenu(final TreeViewer viewer) {
		final Tree tree = viewer.getTree();
		tree.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent event) {
				Point clickedAt = tree.toControl(event.x, event.y);
				Point displayAt = viewer.getControl().toDisplay(clickedAt);
				final TreeItem treeItem = tree.getItem(clickedAt);
				if (treeItem != null) {
					Object objectInTree = treeItem.getData();
					List<ContextualCommandContributor> menuContributors = getMenuContributors(objectInTree);
					if (!menuContributors.isEmpty()) {
						Shell shell = viewer.getControl().getShell();
						popupMenu(objectInTree, menuContributors, shell, displayAt);
						event.doit = false;
					}
				}
			}
		});
	}

	protected void popupMenu(final Object objectInTree, List<ContextualCommandContributor> menuContributors, Shell shell, Point displayAt) {
		Map<String, Menu> submenus = new HashMap<String,Menu>(3);
		Menu menu = new Menu (shell, SWT.POP_UP);
		for (final ContextualCommandContributor menuContributor : menuContributors) {
			MenuItem menuItem;
			String submenuName = menuContributor.getSubmenuName();
			if (submenuName == null) {
				menuItem = new MenuItem(menu, SWT.PUSH);
			} else {
				Menu submenu = getOrCreateSubmenu(menu, submenuName, submenus);
				menuItem = new MenuItem(submenu, SWT.PUSH);
			}
			menuItem.setText (menuContributor.getMenuItemName(menuContributor.ensureCorrectType(objectInTree)));
			menuItem.addSelectionListener(new SelectionListener() {	

				@Override
				public void widgetSelected(SelectionEvent e) {
					menuContributor.performActionOnAdaptedObject(objectInTree);
					
					// re-evaluate the toolbar buttons - enabled/disabled
					final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					ISelectionService selectionService = window==null?null : window.getSelectionService();
					if (selectionService != null) {
						ISelection selection = selectionService.getSelection();
						if (selection instanceof IStructuredSelection) {
							Object selectedObject = ((IStructuredSelection) selection).getFirstElement();
							if (selectedObject != null && window != null) {
								IEvaluationService evaluationService = (IEvaluationService) window.getService(IEvaluationService.class);
								if (evaluationService != null) {
									evaluationService.requestEvaluation("gov.nasa.arc.spife.apex.rcp.buttonType");
								}
							}
						}
					}
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// No-op.				
				}
			});
		}
		menu.setLocation(displayAt);
		menu.setVisible(true);
		shell.setMenu(menu);
	}


	protected List<ContextualCommandContributor> getMenuContributors(Object objectInTree) {
		List<ContextualCommandContributor> list = new LinkedList<ContextualCommandContributor>();
		for (IContextualCommandContributor provider : contributorProviders) {
			Collection<? extends ContextualCommandContributor<?>> contributors = provider.getContributors(objectInTree);
			for (ContextualCommandContributor<?> contributor : contributors) {
				if (contributor.isApplicableToObject(objectInTree)) {
					list.add(contributor);
				}
			}
		}
		Collections.sort(list, getComparator());		
		return list;
	}
	
	/** Sorts according to getOrder() */
	static public Comparator<ContextualCommandContributor> getComparator () {
		return new Comparator<ContextualCommandContributor>() {

			@Override
			public int compare(ContextualCommandContributor item1,
					ContextualCommandContributor item2) {
				return item1.getOrder() - item2.getOrder();
			}
		};
	}


	private Menu getOrCreateSubmenu(Menu parentMenu, String name, Map<String,Menu>map) {
		if (map.containsKey(name)) {
			return map.get(name);
		}
		Menu submenu = new Menu(parentMenu);
		map.put(name, submenu);
		MenuItem submenuTitle = new MenuItem (parentMenu, SWT.CASCADE);
		submenuTitle.setText(name);
		submenuTitle.setMenu(submenu);
		return submenu;
	}


}
