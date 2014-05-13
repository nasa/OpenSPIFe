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

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;

import java.util.Comparator;

import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.Page;

/**
 * A page in that displays a tree viewer as it's main control
 */
public abstract class TreeViewerPage extends Page {
	
	/**
	 * The root composite of the activity dictionary view's GUI elements. Set in createControl().
	 */
	protected Composite rootComposite;
	
	/**
	 * TreeViewer object for responsible for managing displaying content. Set in createControl().
	 */
	protected TreeViewer treeViewer = null;
	
	/** 
	 * Return this page's TreeViewer. It will be null before createControl() is called.
	 * @return this page's TreeViewer
	 */
	public TreeViewer getTreeViewer() {
		return this.treeViewer;
	}
	
	/** Selection provider. Set in createControl(). */
	protected EnsembleSelectionProvider selectionProvider;
	
	/**
	 * Displays relevant text such as the location of the tree input
	 */
	private Label treeLabel = null;
	
	/**
	 * Add searching capabilities. Initializing this is redundant, since the zero-argument
	 * constructor also does so.
	 */
	protected final boolean searchable;
	
	/** 
	 * If greater than 0, prevent excess activity by not searching with every keystroke; pause for the 
	 * specified number of milliseconds to calm things down. 
	 */
	protected final int searchDelay;

	protected TreeViewerFilterWidget nameFilterWidget;
	
	/**
	 * The searchable field defaults to true in the zero-argument constructor.
	 * Initializing the searchable property is redundant, since the field is initialized to true.
	 */
	public TreeViewerPage() {
		this(true, 0);
	}
	
	/**
	 * A constructor that allows the caller to set the searchable property.
	 * @param searchable whether the page can be searched
	 */
	public TreeViewerPage(boolean searchable) {
		this(searchable, 0);
	}
	
	/**
	 * A constructor that allows the caller to set the searchable and searchDelay properties.
	 * @param searchable whether the page can be searched
	 * @param searchDelay milliseconds to delay after search text modification before initiating search
	 */
	public TreeViewerPage(boolean searchable, int searchDelay) {
		this.searchable = searchable;
		this.searchDelay = searchDelay;
	}
	
	/** Give the composite the keyboard focus. */
	@Override
	public void setFocus() {
		rootComposite.setFocus();
	}

	/** Return the composite. */
	@Override
	public Control getControl() {
		return rootComposite;
	}
	
	/**
	 * Creates the SWT control for this page. This entails setting the layout, creating a label,
	 * creating a tree viewer and, if enabled, a combo box for filtering.
	 * @param parent the Composite that is the parent of this editor page
	 */
	@Override
	public void createControl(Composite parent) {
		// create the root composite
		rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(createControlGridLayout());
		createControlUriLabel();
		createControlTreeViewer();
		if (searchable) {
			nameFilterWidget = new TreeViewerFilterWidget(this, rootComposite, "Name");
			nameFilterWidget.setSearchDelay(searchDelay);
		}
	}
	
	protected abstract void searchInputChanged(String comboText, String searchString);

	/**
	 * Create and initialize the tree viewer. Establish the sorter, the selection provider, the
	 * content provider and the label provider.
	 * Auxiliary to createControl(). Do not load.
	 */
	protected void createControlTreeViewer() {
		treeViewer = createTreeViewer();
        treeViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        setTreeViewerAlphabeticalSorter();
		selectionProvider = new EnsembleSelectionProvider(this.toString());
		selectionProvider.attachSelectionProvider(treeViewer);
		getSite().setSelectionProvider(selectionProvider);
		configureTreeViewer(treeViewer);
	}

	protected void setTreeViewerAlphabeticalSorter() {
		treeViewer.setSorter(new ViewerSorter() {
			@Override
			protected Comparator getComparator() {
				return new Comparator() {
					@Override
					public int compare(Object o1, Object o2) {
							return String.CASE_INSENSITIVE_ORDER.compare(String.valueOf(o1), String.valueOf(o2));
					}
				};
			}
		});
	}

	protected TreeViewer createTreeViewer() {
		return new TreeViewer(rootComposite, SWT.MULTI);
	}
	
	/**
	 * Create and initialize the URI label.
	 * Auxiliary to createControl()
	 */
	protected void createControlUriLabel() {
		treeLabel = new Label(rootComposite, SWT.NONE);
		treeLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	/**
	 * Create and initialize the top-level GridLayout for the control.
	 * Auxiliary to createControl().
	 */
	protected GridLayout createControlGridLayout() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		return gridLayout;
	}

	/**
	 * To dispose of this page, remove references to the selection provider and the template
	 * plan.
	 */
	@Override
	public void dispose() {
		if (selectionProvider != null) {
			getSite().setSelectionProvider(null);
			selectionProvider.setSelection(StructuredSelection.EMPTY);
			selectionProvider.detachSelectionProvider(treeViewer);
			selectionProvider = null;
		}
		super.dispose();
	}

	protected void applyExpandHeuristic(String key, String stringToMatch) {	
		ITreeContentProvider cp = (ITreeContentProvider) treeViewer.getContentProvider();
		final Object[] rootChildren = cp.getElements(null);
		if (rootChildren != null && rootChildren.length != 0) {
			if (rootChildren.length <= 25 && !CommonUtils.isNullOrEmpty(stringToMatch)) {
				for(Object object : rootChildren) {
					treeViewer.expandToLevel(object, AbstractTreeViewer.ALL_LEVELS);
				}
			} else {
				treeViewer.collapseAll();
			}
		}
	}
	
	protected String getNameFilterText() {
		if (nameFilterWidget != null) {
			return nameFilterWidget.getNameFilterText();
		}
		return null;
	}

	/**
	 * Set the content provider and label provider for the supplied TreeViewer.
	 * @param viewer a TreeViewer to be configured for content and labels
	 */
	protected abstract void configureTreeViewer(TreeViewer viewer);
	
	/**
	 * Sets the tree label. Checks to make sure we are capable of setting this. The MUST be
	 * done in a display thread to work
	 */
	protected void setTreeLabelText(String text) {
		if (treeLabel != null && !treeLabel.isDisposed()) {
			treeLabel.setText(text);
		}
	}
	
	protected final void setTreeLabelTextInDisplayThread(final String message) {
		WidgetUtils.runInDisplayThread(treeViewer.getControl(), new Runnable() {
			/** Change the label to an error message. */
			@Override
			public void run() {
				setTreeLabelText(message);
			}
		});
	}

	public void doFiltering(String key, String stringToMatch) {	
		searchInputChanged(key, stringToMatch);			
		treeViewer.refresh();
		applyExpandHeuristic(key, stringToMatch);
	}
	
}
