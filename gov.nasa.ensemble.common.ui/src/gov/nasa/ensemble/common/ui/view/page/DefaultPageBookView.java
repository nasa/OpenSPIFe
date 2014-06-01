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
package gov.nasa.ensemble.common.ui.view.page;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

/**
 * This class provides a reasonable default implementation for
 * most of the abstract methods in <code>PageBookView</code>.
 */
public abstract class DefaultPageBookView extends PageBookView {

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IPage createDefaultPage(PageBook book) {
		MessagePage page = new MessagePage();
		initPage(page);
		page.createControl(book);
		page.setMessage("default page");
		return page;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
		PageBook pageBook = getPageBook();
		if (pageBook.isDisposed()) {
			return null;
		}
		Page page = null;
		if (part instanceof IEditorPart) {
			IEditorPart editor = (IEditorPart) part;
			page = createPageForEditor(editor);
		} else if (part instanceof IViewPart) {
			page = createPageForView((IViewPart)part);
		}
		if (page == null) return null;
		initPage(page);
		page.createControl(pageBook);
		// A mouse listener has been added to listen for mouse clicks and grab focus
		// as a result. A bug existed (ENS-373) which when dual IEditParts existed and
		// switched between, the DefaultPageBookView would not regain focus when
		// the user mouse pressed within the control
		page.getControl().addMouseListener(new MouseListener() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// do nothing
			}
			@Override
			public void mouseDown(MouseEvent e) {
				getSite().getPage().activate(DefaultPageBookView.this);
			}
			@Override
			public void mouseUp(MouseEvent e) {
				// do nothing
			}
		});
		return new PageRec(part, page);
	}

	/**
	 * Create a page for the given view. If unable to create a page, should return null.
	 * 
	 * @param view
	 * @return Page
	 */
	protected abstract Page createPageForView(IViewPart view);

	/**
	 * Create a page for the input of the given editor. If unable to create a page, should return null.
	 * 
	 * @param editor
	 * @return Page
	 */
	protected abstract Page createPageForEditor(IEditorPart editor);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
		pageRecord.page.dispose();
		pageRecord.dispose();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected IWorkbenchPart getBootstrapPart() {
		IWorkbenchPage page = getSite().getPage();
		if(page != null)
			return page.getActiveEditor();
		return null;
	}
	
}
