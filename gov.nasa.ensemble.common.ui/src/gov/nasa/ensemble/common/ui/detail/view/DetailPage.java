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
/*
 * Created on Apr 6, 2005
 *
 */
package gov.nasa.ensemble.common.ui.detail.view;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.UndoRedoUtils;
import gov.nasa.ensemble.common.ui.detail.DetailFactoryRegistry;
import gov.nasa.ensemble.common.ui.detail.DetailUtils;
import gov.nasa.ensemble.common.ui.detail.IDetailSheet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

/**
 * This UI element simply listens to the workbench selection
 * and calls the DetailFactoryRegistry to get the relevant
 * sheet to add to the Page.
 * 
 *  The basic structure gets the title and icon from the 
 *  IDetailSheet. The DetailGroups are added and separated
 *  by the name of the groups and a line. IDetail elements 
 *  are inspected for their editing composites and added as
 *  the entire row. 
 * 
 * @author Arash
 *
 */
public class DetailPage extends Page {
	
	private final static Logger trace = Logger.getLogger(DetailPage.class);
	
    protected FormToolkit toolkit;
    private ScrolledForm form;
	
	private IDetailSheet currentSheet = null;
	
    private Set<Object> currentSelection = new HashSet<Object>();
	private final ISelectionListener selectionListener = new SelectionListener();

	private final IWorkbenchPart associatedPart;
	private final IWorkbenchPartSite viewSite;

	private static Map<IPerspectiveDescriptor, DetailPage> lastActivePageMap = new HashMap<IPerspectiveDescriptor, DetailPage>();
	
	private PreferenceListener preferenceListener = new PreferenceListener();

	private TableWrapLayout originalLayout;
	
	// MAE-2359 - Resets DetailPage when Perspective changes
	static {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().addPerspectiveListener(new PerspectiveAdapterImpl());
	}
	
	public DetailPage(IWorkbenchPart associatedPart, IWorkbenchPartSite viewSite) {
		this.associatedPart = associatedPart;
		this.viewSite = viewSite;
		DetailUtils.PREFERENCES.addPropertyChangeListener(preferenceListener);
	}

	@Override
	public void createControl(Composite parent) {
	    toolkit = new FormToolkit(parent.getDisplay());
	    toolkit.setBorderStyle(SWT.NONE);
	    form = toolkit.createScrolledForm(parent);
        form.setExpandHorizontal(true);
        form.setExpandVertical(true);
	    originalLayout = new TableWrapLayout();
		form.getBody().setLayout(originalLayout);
	    IPageSite site = getSite();
	    if (site != null) {
		    IWorkbenchPage page = site.getPage();
		    page.addPostSelectionListener(selectionListener);
	    	ISelectionProvider partSelectionProvider = associatedPart.getSite().getSelectionProvider();
	    	site.setSelectionProvider(partSelectionProvider);
		    if (associatedPart instanceof IEditorPart) {
				IEditorPart editorPart = (IEditorPart) associatedPart;
				IUndoContext undoContext = (IUndoContext)editorPart.getAdapter(IUndoContext.class);
				if (undoContext != null) {
					UndoRedoUtils.setupUndoRedo(site.getActionBars(), viewSite, undoContext);
				}
		    }
		    ISelection selection = partSelectionProvider.getSelection();
		    if ((selection != null) && (selection instanceof IStructuredSelection)) {
	        	try {
	        		updateSelection((IStructuredSelection)selection, false);
	        	} catch (Exception e) {
	        		printException(e);
	        		trace.error("DetailPage.updateSelection", e);
	        	}
        	}
		    createActions();
//		    page.addPartListener(new IPartListener2() {
//				@Override
//				public void partVisible(IWorkbenchPartReference partRef) {
//					System.out.println("visible");
//				}
//				@Override
//				public void partOpened(IWorkbenchPartReference partRef) {
//					System.out.println("opened");
//				}
//				@Override
//				public void partInputChanged(IWorkbenchPartReference partRef) {
//					System.out.println("inputChanged");
//				}
//				@Override
//				public void partHidden(IWorkbenchPartReference partRef) {
//					System.out.println("hidden");
//				}
//				@Override
//				public void partDeactivated(IWorkbenchPartReference partRef) {
//					System.out.println("deactivated");
//				}
//				@Override
//				public void partClosed(IWorkbenchPartReference partRef) {
//					System.out.println("closed");
//				}
//				@Override
//				public void partBroughtToTop(IWorkbenchPartReference partRef) {
//					System.out.println("top");
//				}
//				@Override
//				public void partActivated(IWorkbenchPartReference partRef) {
//					System.out.println("activated");
//				}
//			});
	    }
	}
	
    private void createActions() {
    	IActionBars bars = getSite().getActionBars();
    	ContributionItem item = new ContributionItem() {
    		
			@Override
			public void fill(Menu menu, int index) {
				Collection<String> disabledFilterFlags = DetailUtils.getDisabledFilterFlags();
				Collection<String> availableFilterFlags = DetailUtils.getAvailableFilterFlags();
				for (final String filterFlag : availableFilterFlags) {
					final MenuItem item = new MenuItem(menu, SWT.CHECK);
					item.setText("Hide "+filterFlag+" parameters");
					item.setSelection(!disabledFilterFlags.contains(filterFlag));
					
					Listener listener = new Listener() {
						@Override
						public void handleEvent(Event event) {
							switch (event.type) {
							case SWT.Selection:
								if (item.getSelection()) {
									DetailUtils.removeDisabledFilterFlags(filterFlag);
								} else {
									DetailUtils.addDisabledFilterFlags(filterFlag);
								}
								break;
							}
						}
					};
					item.addListener(SWT.Selection, listener);
				}
			}

			@Override
			public boolean isDynamic() {
				return true;
			}
			
		};
		bars.getMenuManager().add(item);
	}

	@Override
	public void dispose() {
	    IPageSite site = getSite();
	    if (site != null) {
		    IWorkbenchPage page = site.getPage();
		    page.removePostSelectionListener(selectionListener);
		    UndoRedoUtils.disposeUndoRedo(site.getActionBars());
	    }
	    clearChildren();
	    if ((form != null) && (!form.isDisposed())) {
	    	form.dispose();
	    }
	    DetailUtils.PREFERENCES.removePropertyChangeListener(preferenceListener);
	    currentSelection.clear();
	    super.dispose();
	}

	@Override
	public void setFocus() {
	    if ((form != null) && (!form.isDisposed())) {
	        form.setFocus();
	    }
	}

	@Override
	public ScrolledForm getControl() {
	    return form;
	}

	public void updateSelection() {
		updateSelection(new StructuredSelection(currentSelection.toArray()), false);
	}
	
	public void updateSelection(IStructuredSelection selection) {
		updateSelection(selection, true);
	}
	
	private IWorkbenchPart lastPart = null;
	public void updateSelection(IStructuredSelection selection, boolean ignoreSame) {
		if (getControl().isDisposed()) {
			return;
		}
		List<Object> selectionList = selection.toList();
		IPageSite site = getSite();
		if (selectionList.isEmpty() && (site != null)) {
			IWorkbenchPage page = site.getPage();
			IWorkbenchPart part = page.getActivePart();
			if (part instanceof DetailView) {
				part = lastPart;
			} else {
				lastPart = part;
			}
			if ((part != null) && page.isPartVisible(part)) {
				selectionList = Collections.<Object>singletonList(part);
			}
		}
    	if (ignoreSame 
    			&& currentSelection.size() == selectionList.size() 
    			&& currentSelection.containsAll(selectionList)) {
    		return;
    	}
    	currentSelection.clear();
    	currentSelection.addAll(selectionList);
    	
    	clearChildren();
    	
    	ISelectionProvider selectionProvider = (site != null ? site.getSelectionProvider() : null);
    	switch (currentSelection.size()) {
	    	case 0: {
				IDetailSheet sheet = DetailFactoryRegistry.buildDetailSheet(null, toolkit, form, selectionProvider);
	        	if (sheet != null) {
	        		setDetailSheet(sheet);
	        	} else {
	        		addTitle("Nothing selected");
	        	}
	    		break;
	    	}
	    	case 1: {
	    		Object object = currentSelection.toArray()[0];
				IDetailSheet sheet = DetailFactoryRegistry.buildDetailSheet(object, toolkit, form, selectionProvider);
	        	if (sheet != null) {
	        		setDetailSheet(sheet);
	        	} else {
	        		addTitle("Nothing selected");
	        	}
	    		break;
	    	}
	    	default: { // if comparison detail sheet is registered, then make one.
	    		IDetailSheet sheet = DetailFactoryRegistry.buildDetailSheet(selection.toList(), toolkit, form, selectionProvider);
	        	if (sheet != null) {
	        		setDetailSheet(sheet);
	        	} else {
	        		addTitle("You have selected " + currentSelection.size() + " different items.");
	        	}
    		}
    	}
	}
	
	private void clearChildren() {
    	if (this.currentSheet != null) {
    		// This should work, but alas, it does not
    		// this.currentSheet.setInput(null);
    		this.currentSheet.dispose();
    		this.currentSheet = null;
    	}
    	this.currentSheet = null;
    	if (!form.isDisposed() && !form.getBody().isDisposed()) {
	    	for (Control control : form.getBody().getChildren()) {
	    		control.dispose();
	    	}
    	}
	}

	private void addTitle(String title) {
		ManagedForm mForm = new ManagedForm(toolkit, form);
		mForm.addPart(new TitleFormPart());
		mForm.setInput(title);
		
    	// PageBook.showPage() should do this for us. This was causing SPF-785.
        // form.setVisible(true);
		
    	form.reflow(true);
	}
	
    private void setDetailSheet(IDetailSheet sheet) {
    	if(this.currentSheet != null) {
    		this.currentSheet.dispose();
    		this.currentSheet = null;
    	}
    	this.currentSheet = sheet;
    	
    	form.reflow(true);
    	form.setOrigin(0,0);
    }
    
    public boolean hasSheet() {
    	return currentSheet != null;
    }
	
	private void printException(Exception e) {
		try {
	        clearChildren();
	        form.getBody().setLayout(originalLayout);
	        addTitle(e.getClass().getSimpleName());
	        StringBuffer buffer = new StringBuffer();
	        buffer.append(e.getClass().getName()+": "+e.getMessage()+"\n");
	        for (StackTraceElement element : e.getStackTrace()) {
	        	buffer.append("\tat "+element.getClassName()+"."+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")\n");
	        }
	        Text text = toolkit.createText(form.getBody(), buffer.toString(), SWT.MULTI);
	        text.setEnabled(true);
	        
	        form.setVisible(true);
	        form.reflow(true);
        } catch (Exception e1) {
        	Logger.getLogger(DetailPage.class).error("unable to display error in view: ", e);
        }
	}

	/**
	 * @deprecated use DetailView.getDetailPageForPart instead
	 * @param part
	 * @return
	 */
	@Deprecated
	public static DetailPage getDetailPageForPart(IWorkbenchPart part) {
		return DetailView.getDetailPageForPart(part);
	}
	
    private class SelectionListener implements ISelectionListener {
        @Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        	if (!isRelevantChange(part)) 
        		return;
        	IPageSite site = getSite(); 
        	if ((site != null) && (site.getSelectionProvider() == null)) {
        		ISelectionProvider partSelectionProvider = associatedPart.getSite().getSelectionProvider();
        		site.setSelectionProvider(partSelectionProvider);
        	}
        	if ((selection != null) && (selection instanceof IStructuredSelection)) {
	        	try {
	        		updateSelection((IStructuredSelection)selection);
	        	} catch (Exception e) {
	        		printException(e);
	        		trace.error("DetailPage.updateSelection", e);
	        	}
        	}
        }
        
        /**
         * Determine if the change to the selection state of a workbench part is
         * relevant to the display of this detail page. If the part is a DetailView then
         * the change is only relevant if this page is the current page of that view.
         * Otherwise, the change is only relevant if the affected part is actually the part
         * for which this page was created; if not, there should be another page responsible
         * for reflecting the selection state of the part.
         * 
         * @param part an IWorkbenchPart that has become active or whose selection has changed
         * @return boolean true if a change to the part is relevant to this page, else false
         */
        private boolean isRelevantChange(IWorkbenchPart part) {
        	if (part instanceof DetailView) {
        		return ((DetailView)part).getCurrentPage() == DetailPage.this;
        	} else {
        		return part == associatedPart;
        	}
        }
    }
    
    private class PreferenceListener implements IPropertyChangeListener {
    	
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			if (CommonUtils.equals(DetailUtils.P_DISABLED_FILTER_FLAGS, event.getProperty())) {
				StructuredSelection selection = currentSelection == null ?
									new StructuredSelection() :
									new StructuredSelection(currentSelection.toArray(new Object[currentSelection.size()]));
				updateSelection(selection, false);
			}
		}
    	
    }
    
    private static class PerspectiveAdapterImpl extends PerspectiveAdapter {
    	
		@Override
		public void perspectivePreDeactivate(IWorkbenchPage workbenchPage, IPerspectiveDescriptor perspective) {
			DetailPage detailPage = getCurrentDetailPage(workbenchPage);
			if (detailPage != null)
				lastActivePageMap.put(perspective, detailPage);
		}
		
		@Override
		public void perspectiveActivated(IWorkbenchPage workbenchPage, IPerspectiveDescriptor perspective) {
			DetailPage lastDetailPage = lastActivePageMap.get(perspective);
			if (lastDetailPage != null) {
				DetailView detailView = DetailView.getDetailView(workbenchPage);
				if (detailView != null)
					detailView.partActivated(lastDetailPage.associatedPart);
			} else {
				// clear details manually
				DetailPage detailPage = getCurrentDetailPage(workbenchPage);
				if (detailPage != null)
					detailPage.updateSelection(new StructuredSelection());
			}
		}
		
		private DetailPage getCurrentDetailPage(IWorkbenchPage page) {
			DetailView detailView = DetailView.getDetailView(page);
			if (detailView != null) {
				IPage currentPage = detailView.getCurrentPage();
				if (currentPage instanceof DetailPage)
					return (DetailPage)currentPage;
			}
			return null;
		}
    	
    }
    
}
