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
package gov.nasa.ensemble.core.plan.editor.search;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.type.IStringifier;
import gov.nasa.ensemble.common.type.StringifierRegistry;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.Plan;
import gov.nasa.ensemble.core.plan.PlanUtils;

import java.lang.reflect.Field;
import java.util.Date;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DecoratingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.search.ui.IContextMenuConstants;
import org.eclipse.search.ui.text.AbstractTextSearchViewPage;
import org.eclipse.search2.internal.ui.OpenSearchPreferencesAction;
import org.eclipse.search2.internal.ui.PinSearchViewAction;
import org.eclipse.search2.internal.ui.basic.views.RemoveAllMatchesAction;
import org.eclipse.search2.internal.ui.basic.views.RemoveSelectedMatchesAction;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.internal.e4.compatibility.ActionBars;
import org.eclipse.ui.part.IPageSite;

/**
 * This class is what creates the actual page itself that is loaded
 * into the search results view, which contains the search results
 * that are displayed according to the PlanSearchTreeContentProvider.
 * 
 * @author abenavides
 */
@SuppressWarnings("restriction")
public class PlanSearchResultPage extends AbstractTextSearchViewPage {
	private ColumnViewer viewer;		
	private PlanSearchContentProvider fContentProvider;	

	public PlanSearchResultPage(){
		super(AbstractTextSearchViewPage.FLAG_LAYOUT_TREE | AbstractTextSearchViewPage.FLAG_LAYOUT_FLAT);	
	}
	
	@Override
	public void init(IPageSite site) {
		super.init(site);
		IMenuManager menuManager = site.getActionBars().getMenuManager();
		menuManager.appendToGroup(IContextMenuConstants.GROUP_PROPERTIES, new OpenSearchPreferencesAction());
	}
	
	@Override
	protected void clear() {	
		if (fContentProvider != null) {
			fContentProvider.clear();
		}
		if (viewer!=null) {
		    viewer.refresh();
		}
	}	
	
	@Override
	protected void fillToolbar(IToolBarManager tbm) {
		super.fillToolbar(tbm);
		
		// removing the remove options from the view
		IContributionItem[] items = tbm.getItems();
		for (IContributionItem item : items){
			if (item instanceof ActionContributionItem){
				IAction action = ((ActionContributionItem) item).getAction();
				if (action instanceof RemoveAllMatchesAction){
					item.setVisible(false);
				}
				if (action instanceof RemoveSelectedMatchesAction){
					item.setVisible(false);
				}
			}
		}
	}
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		IPageSite site = getSite();
		IActionBars actionBars = site.getActionBars();
		try {
			Field field = SubActionBars.class.getDeclaredField("parent");
			field.setAccessible(true);
			ActionBars object = (ActionBars) field.get(actionBars);
			IToolBarManager manager = object.getToolBarManager();
			IContributionItem[] items = manager.getItems();
			for (IContributionItem item : items) {
				if (item instanceof ActionContributionItem){
					IAction action = ((ActionContributionItem) item).getAction();
					if (action instanceof PinSearchViewAction){
						item.setVisible(false);
					}
				}
			}
		} catch (SecurityException e) {
			LogUtil.error(e);
		} catch (NoSuchFieldException e) {
			LogUtil.error(e);
		} catch (IllegalArgumentException e) {
			LogUtil.error(e);
		} catch (IllegalAccessException e) {
			LogUtil.error(e);
		}
	}
	
	@Override
	protected void configureTableViewer(TableViewer viewer) {
		initializeViewer(viewer, true);
	}

	@Override
	protected void configureTreeViewer(TreeViewer viewer) {
		initializeViewer(viewer, false);
	}

	private void initializeViewer(ColumnViewer viewer, boolean flat) {
		this.viewer = viewer;

		// set content provider
		fContentProvider= new PlanSearchContentProvider(flat);
		viewer.setContentProvider(fContentProvider);
		
		// set label provider
		IStyledLabelProvider styleLabel = getStyledLabelProvider();
		ILabelDecorator labelDecorator = getLabelDecorator();
		viewer.setLabelProvider(new DecoratingStyledCellLabelProvider(styleLabel, labelDecorator, null));
		viewer.setUseHashlookup(true);
		
		// set listeners
		viewer.addSelectionChangedListener(new ISelectionChangedListener(){
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection realSelect = (IStructuredSelection) event.getSelection();
				if (realSelect == null || realSelect.size() == 0){
					return;
				}			
				Object elem = realSelect.getFirstElement();
				if (elem instanceof EPlanElement){
					EPlan selectedPlan = EPlanUtils.getPlan((EPlanElement)elem);
					IWorkbench workbench = PlatformUI.getWorkbench();
					for (IWorkbenchWindow w : workbench.getWorkbenchWindows()) {
						for (IWorkbenchPage p : w.getPages()) {
							for (IEditorPart part : p.getEditors()) {
								EPlan plan = CommonUtils.getAdapter(part, EPlan.class);
								if (selectedPlan.equals(plan)){
									//PlanEditorUtil.openPlanEditor(plan, w, true);
									ISelectionProvider provider = part.getSite().getSelectionProvider();
									provider.setSelection(realSelect);
									IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
									if(activeWorkbenchWindow != null) {
										IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
										if(activePage != null) {
											IViewPart searchView = activePage.findView("org.eclipse.search.ui.views.SearchView");
											if(searchView != null) {
												IWorkbenchPartSite site = searchView.getSite();
												Shell shell = site.getShell();
												shell.moveAbove(part.getSite().getShell());
											}
											
										}
									}
									return;
								}
							}
						}
					}
				}
			}	
		});
	}
	
	/**
	 * Finds the plan associated with the plan element.
	 * 
	 * @param plan element
	 * @return the plan for the plan element
	 */
	public Plan getPlan(Object elem){
		if (elem instanceof Plan){
			return (Plan) elem;
		} else {
			return getPlan(fContentProvider.getParent(elem));
		}	
	}

	@Override
	protected void elementsChanged(Object[] objects) {
		if(viewer != null){
			viewer.refresh();	
		}
	}
	
	/**
	 * This is the style label provider so that we can decorate
	 * the search results and the user can differentiate between
	 * the result name and extra information.
	 * 
	 * @return instance of IStyledLabelProvider
	 */
	private IStyledLabelProvider getStyledLabelProvider() {
		return new IStyledLabelProvider() {

			@Override
			public Image getImage(Object element) {
				return PlanUtils.getIcon((EPlanElement)element);
			}

			@Override
			public StyledString getStyledText(Object element) {
				return new StyledString(element.toString(), StyledString.DECORATIONS_STYLER);
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// do nothing
			}

			@Override
			public void dispose() {
				// do nothing
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// do nothing
			}
		};
	}

	/**
	 * This label decorater will add the time information 
	 * to the search result items that need to be displayed in 
	 * the search results view page. 
	 * 
	 * @return instance of ILabelDecorator
	 */
	private ILabelDecorator getLabelDecorator() {
		return new ILabelDecorator() {

			@Override
			public Image decorateImage(Image image, Object element) {
				return null;
			}

			@Override
			public String decorateText(String text, Object element) {
				if (element instanceof EPlanElement) {
					IStringifier<Date> DATE_STRINGIFIER = StringifierRegistry.getStringifier(Date.class);
					EPlanElement pe = (EPlanElement) element;
					StringBuffer buffer = new StringBuffer(pe.getName());
					Date startTime = pe.getMember(TemporalMember.class).getStartTime();
					if (startTime != null) {
						buffer.append(" - "+DATE_STRINGIFIER.getDisplayString(startTime));
					}
					return buffer.toString();
				}
				return element.toString();
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// do nothing
			}

			@Override
			public void dispose() {
				// do nothing
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
				// do nothing
			}
		};
	}
}
