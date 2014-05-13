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
package gov.nasa.ensemble.common.ui.detail.view;

import gov.nasa.ensemble.common.extension.ClassIdRegistry;
import gov.nasa.ensemble.common.ui.GlobalAction;
import gov.nasa.ensemble.common.ui.view.page.DefaultPageBookView;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;

public class DetailView extends DefaultPageBookView {
	
	public static final String ID = ClassIdRegistry.getUniqueID(DetailView.class);
	
    @Override
	protected IPage createDefaultPage(PageBook book) {
        IPage page = super.createDefaultPage(book);
        if (page instanceof MessagePage) {
            ((MessagePage)page).setMessage("Nothing currently selected.");
        }
        return page;
    }

    public static DetailPage getDetailPageForPart(IWorkbenchPart part) {
		DetailView detailView = (DetailView) part.getSite().getPage().findView(DetailView.ID);
		if (detailView == null) {
			return null;
		}
		PageRec pageRec = detailView.getPageRec(part);
		if (pageRec == null) {
			return null;
		}
		return (DetailPage)pageRec.page;
	}
    
	@Override
	protected void showPageRec(PageRec pageRec) {
		super.showPageRec(pageRec);
		GlobalAction.updateActionBars();
	}

	@Override
	protected Page createPageForEditor(IEditorPart editorPart) {
		return new DetailPage(editorPart, getViewSite());
	}

	@Override
	protected Page createPageForView(IViewPart viewPart) {
		return new DetailPage(viewPart, getViewSite());
	}

	@Override
	protected boolean isImportant(IWorkbenchPart part) {
        return (part != this) && (part.getSite().getSelectionProvider() != null);
	}
	
	public static DetailView getDetailView(IWorkbenchPage page) {
		IViewReference viewRefs[] = page.getViewReferences();
		for (IViewReference viewRef : viewRefs)
			if (viewRef.getId().equals(DetailView.ID))
				return (DetailView)viewRef.getView(true);
		return null;
	}
}
