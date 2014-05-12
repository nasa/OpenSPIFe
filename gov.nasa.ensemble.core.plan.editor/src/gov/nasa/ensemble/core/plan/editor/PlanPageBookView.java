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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.UndoRedoUtils;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

/**
 * A plan-specific superclass for multi-page plan views. It is abstract because it does not
 * implement method createPage() from Eclipse superclass PageBookView. It supplies default
 * implementations for other methods of this superclass.
 *
 */
public abstract class PlanPageBookView extends PageBookView {

	/**
	 * The message given when a default page is created. Subclass constructors initialize it.
	 */
    private final String defaultMessage;

    /**
     * Set the defaultMessge.
     * @param emptyMessage what the defaultMessage is set to; despite the name, this is not
     * necessarily the empty string, but rather the message for an empty page
     */
	public PlanPageBookView(String emptyMessage) {
		this.defaultMessage = emptyMessage;
    }

	/**
	 * Implement in your subclass to create a page for the editor/model
	 * @param editor the editor in which the page is being created
	 * @param model the model which supplies content to the editor
	 * @return the newly-created page
	 */
	protected abstract Page createPage(IEditorPart editor, PlanEditorModel model);

	/**
	 * Override in your subclass to handle page activation, if necessary
	 * @param page the page being activated
	 */
	protected void pageActivated(IPage page) {
		// no default behavior
	}
	
	/**
	 * Creates, initializes and returns the default page for this view, giving it the default
	 * message. Implements the abstract method from Eclipse class PageBookView.
	 * @param book the PageBook control that will become the parent of the new page
	 * @return the newly-created page
	 */
	@Override
	protected IPage createDefaultPage(PageBook book) {
    	MessagePage page = new MessagePage();
		initPage(page);
		page.createControl(book);
		page.setMessage(defaultMessage);
        return page;
	}

	/**
	 * Returns the active, important workbench part for this view. Implements the abstract
	 * method from Eclipse class PageBookView.
	 * @return null if the page is null or the active editor is not important; otherwise, the
	 * page's active editor
	 */
	@Override
	protected IWorkbenchPart getBootstrapPart() {
		IWorkbenchPage page = getSite().getPage();
		if (page != null) {
			IEditorPart activeEditor = page.getActiveEditor();
			if (isImportant(activeEditor)) {
				return activeEditor;
			}
		}
		return null;
	}

	/**
	 * Indicates whether the given part should be added to this view. Implements the abstract
	 * method from Eclipse superclass PageBookView.
	 * @param part a part
	 * @return whether the part is an editor and the plan editor's model is not null
	 */
	@Override
	protected boolean isImportant(IWorkbenchPart part) {
    	if (part instanceof IEditorPart) {
    		PlanEditorModel model = CommonUtils.getAdapter(part, PlanEditorModel.class);
    		return (model != null);
    	}
    	return false;
    }

	/**
	 * Implement the abstract method declared in PageBookView.
	 * @param part the associated editor
	 * @return if the PageBook was disposed, return null; otherwise return a new page record
	 */
    @Override
    protected PageRec doCreatePage(IWorkbenchPart part) {
    	PageBook pageBook = getPageBook();
		if (pageBook.isDisposed()) {
			return null;
		}
		IEditorPart editor = (IEditorPart)part;
		PlanEditorModel model = CommonUtils.getAdapter(part, PlanEditorModel.class);
		Page page = createPage(editor, model);
		initPage(page);
		UndoRedoUtils.setupUndoRedo(page.getSite().getActionBars(), getSite(), model.getUndoContext());
		page.createControl(pageBook);
        return new PageRec(part, page);
    }

    /**
     * Implement the abstract method declared in PageBookView. Dispose of the undo history, the
     * page and the page record.
     * @param part ignored
     * @param pageRecord the page record being destroyed
     */
	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
		UndoRedoUtils.disposeUndoRedo(pageRecord.subActionBars);
        pageRecord.page.dispose();
        pageRecord.dispose();
	}

	/**
	 * If the argument is not null, prefix the call to the superclass by performing the
	 * pre-open operation. This operation can be supplied by the concrete subclass.
	 * @param pageRec the page being opened
	 */
	@Override
	protected void showPageRec(PageRec pageRec) {
		if (pageRec != null) {
			IPage page = pageRec.page;
			pageActivated(page);
		}
		super.showPageRec(pageRec);
	}

}
