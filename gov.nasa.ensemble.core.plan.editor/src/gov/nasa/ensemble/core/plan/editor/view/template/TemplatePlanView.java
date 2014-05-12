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
package gov.nasa.ensemble.core.plan.editor.view.template;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanPageBookView;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.Page;

/**
 * A view consisting of template elements that are relevant to the plan being
 * edited. The entire collection is called a "template plan".
 */
public class TemplatePlanView extends PlanPageBookView {

	/**
	 * The message string presented if the view is opened without a plan being opened. The
	 * constructor passes it to the PageBookView superclass constructor, which saves it.
	 */
	private static final String DEFAULT_MESSAGE = "A plan must be opened before templates can be viewed.";
	
	/** A unique ID string for this view. */
	public static final String ID = ClassIdRegistry.getUniqueID(TemplatePlanView.class);

	// action to add a new template
	protected TemplatePlanViewAddAction addAction;
	
	/**
	 * Supply the default message string to superclass PageBookView.
	 * This message is presented when no plan has been opened.
	 */
    public TemplatePlanView() {
	    super(DEFAULT_MESSAGE);
    }
    
    protected String getID() {
    	return ID;
    }
	
    /**
     * Add functionality to PlanPageBookView.doDestroyPage() if the page record's page is a
     * TemplatePlanPage: disable the pulldown action and remove its listener.
     * @param part the part containing the page
     * @param pageRecord the holder of the page being destroyed
     */
	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
		super.doDestroyPage(part, pageRecord);
		if (addAction != null && pageRecord.page instanceof TemplatePlanPage) {
			addAction.setEnabled(false);
			SelectionProvider selectionProvider = getSelectionProvider();
			if(selectionProvider != null) {
				selectionProvider.removePostSelectionChangedListener(addAction);
			}		
		}
	}

    /**
     * In addition to inherited behavior, adds a new item pull down action to the view's toolbar
     */
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		addAction = createAddNewItemAction();
		if (addAction != null) {
			IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
			getSelectionProvider().addPostSelectionChangedListener(addAction);
			toolBarManager.add(addAction);
			toolBarManager.update(true);
		}
	}

	protected TemplatePlanViewAddAction createAddNewItemAction() {
		return new TemplatePlanViewAddNewItemPulldownAction(this);
	}

	/**
	 * If the editor has a TemplatePlanPage adapter, return it; otherwise create and return a
	 * default page for the page book.
	 * Implements the method from Eclipse class PageBookView that was left undeclared in 
	 * abstract superclass PlanPageBookView.
	 * @param editor the plan editor part
	 * @param model the model being edited
	 * @return
	 */
	@Override
	protected Page createPage(IEditorPart editor, PlanEditorModel model) {
		TemplatePlanPage templatePlanPage = CommonUtils.getAdapter(editor, TemplatePlanPage.class);			
		if(templatePlanPage == null) {
			return (Page) createDefaultPage(this.getPageBook());
		}
		// listen to the selection provider to determine if the tree viewer's selection needs to be updated
		return templatePlanPage;
	}
	
	/**
	 * Make sure that the part is not considered if it is hidden.
	 * This method is redundant; the behavior would be the same if it were not declared.
	 * @param part a workbench part
	 */
	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
		PageRec pageRec = super.doCreatePage(part);
		if (addAction != null) {
			boolean enabled = false;
			if(pageRec.page instanceof TemplatePlanPage) {
				TemplatePlanPage page = (TemplatePlanPage) pageRec.page;
				ISelectionProvider selectionProvider = page.getSite().getSelectionProvider();
				ISelection selection = selectionProvider.getSelection();
				enabled = addAction.shouldBeEnabled(selection);
			}
			addAction.setEnabled(enabled);
		}
		return pageRec;
	}

	/**
	 * Return the current page's plan.
	 * @return the page's plan; may be null if the current page is not a TemplatePlanPage or
	 * if the page has no plan
	 */
	public EPlan getCurrentTemplatePlan() {
		IPage currentPage = getCurrentPage();
		EPlan plan = null;
		if(currentPage instanceof TemplatePlanPage) {
			TemplatePlanPage templatePlanPage = (TemplatePlanPage)currentPage;
			plan = templatePlanPage.getTemplatePlan();
		}
		
		return plan;
	}
	
	/**
	 * Override the default pageActivated behavior to update the enablement of the addAction
	 * 
	 * @param page an IPage that has been activated, becoming the current page of the view
	 */
	@Override
	protected void pageActivated(IPage page) {
		super.pageActivated(page);
		if(addAction != null) {
			addAction.updateEnablement();
		}
	}
	
	/**
	 * What to do when the TemplatePlanView becomes visible. 
	 * If the active page is a TemplatePlanView, report to the superclass that it is activated. 
	 * Load the template plan if either the page
	 * is a TemplatePlanView or if the site's ID is that of the TemplatePlanView class.
	 */
	@Override
	protected void partVisible(IWorkbenchPart part) {
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(getID());
		if (isImportant(part) && view instanceof TemplatePlanView && view.getSite().getPage().isPartVisible(this)) {
			super.partActivated(part);
			loadTemplatePlans();
		} else if (getID().equals(part.getSite().getId())) {
			loadTemplatePlans();
		}
	}

	/**
	 * Auxiliary factoring out a repeated operation in method partVisible(IWorkbenchPart).
	 * If the current page is a TemplatePlanPage and it has no template plan, load it into the
	 * TreeViewer.
	 */
	private void loadTemplatePlans() {
		IPage currentPage = getCurrentPage();
		if (currentPage instanceof TemplatePlanPage) {
			TemplatePlanPage templatePlanPage = (TemplatePlanPage) currentPage;
			if (templatePlanPage.getTemplatePlans().isEmpty()) {
				templatePlanPage.loadTemplatePlans();
			}
		}
	}

	/**
	 * Decorate by disposing of the add action if there is one.
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (addAction != null) {
			addAction.dispose();
		}
	}

}
