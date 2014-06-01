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
package gov.nasa.ensemble.core.activityDictionary.view;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.ui.WidgetPlugin;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorMap;
import gov.nasa.ensemble.dictionary.INamedDefinition;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

public abstract class DefinitionTreeView extends ViewPart {

	/** The root composite of the activity dictionary view's GUI elements */
	private Composite rootComposite;
	
	/**
	 * The user can filter the elements in the view by entering search criteria in the filter text widget  
	 */
	private Text findFilterText  = null;
	
	/** This widget defines the different filter modes */
	private Combo findFilterMode = null;
	
	/** TreeViewer object for responsible for managing displaying content */
	private TreeViewer treeViewer = null;
	
	protected static final String DEFINITION_NAME = "Name";
	protected static final String DEFINITION_CATEGORY = "Category";
	
	/** it can be useful to give each Composite a color when debugging layouts */
	private final boolean useDebuggingColors = false;

	/** Previous tool tip text to prevent any unwarranted tooltip updates */
	private String oldTooltipText = null;
	
	private static final String TAB_FIND_FILTER_MODES[] = { DEFINITION_NAME, DEFINITION_CATEGORY };
    
	protected final TreeViewer getTreeViewer() {
		return treeViewer;
	}

	/**
     * Creates the SWT controls for this workbench part.
     *
     * This is a multi-step process:
     * <ol>
     *   <li>Create one or more controls within the parent.</li>
     *   <li>Set the parent layout as needed.</li>
     *   <li>Register any global actions with the site's <code>IActionBars</code>.</li>
     *   <li>Register any context menus with the site.</li>
     *   <li>Register a selection provider with the site, to make it available to 
     *     the workbench's <code>ISelectionService</code> (optional). </li>
     * </ol>
     * </p>
     * 
     * @param parent the parent control
     * @see IWorkbenchPart
	 */
	@Override
	public void createPartControl(Composite parent) {
		// create the root composite
		rootComposite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginRight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		rootComposite.setLayout(gridLayout);
		if(useDebuggingColors) rootComposite.setBackground(ColorMap.RGB_INSTANCE.getColor(new RGB(255, 200, 200)));

		treeViewer = buildTreeViewer(rootComposite);
		treeViewer.addFilter(new FindFilter());
        treeViewer.getTree().addMouseMoveListener(new MouseMoveListener() {
			@Override
			public void mouseMove(MouseEvent e) {
				updateToolTip(e);
			}
        });
		getSite().setSelectionProvider(treeViewer);
        
		// create the find filter composite
		Composite findFilterComposite = new Composite(rootComposite, SWT.NONE);
		findFilterComposite.setLayout(new RowLayout());
		if(useDebuggingColors) findFilterComposite.setBackground(ColorMap.RGB_INSTANCE.getColor(new RGB(255, 200, 255)));
		
		Composite comboTextComposite = new Composite(findFilterComposite, SWT.NONE);
		comboTextComposite.setLayout(new FillLayout());
		if(useDebuggingColors) comboTextComposite.setBackground(ColorMap.RGB_INSTANCE.getColor(new RGB(255, 200, 255)));
		
		// create the find filter combo mode box
		findFilterMode = new Combo(comboTextComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		
		// add a modify listeners to enable on-the-fly filtering as search
		// criteria is entered
		findFilterMode.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				treeViewer.refresh();
				applyExpandHeuristic(treeViewer);
			}
		});
		updateModeCombo();

		// create the find filter text field
		findFilterText = new Text(comboTextComposite, SWT.SEARCH | SWT.CANCEL);
		findFilterText.setData("name", "ActivityDictionaryView.findFilterText");
		findFilterText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				treeViewer.refresh();
				applyExpandHeuristic(treeViewer);
			}
		});
		findFilterText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				if (e.detail == SWT.CANCEL) {
					searchCancelled();
				} else {
					// searching for findFilterText.getText();
				}
			}
		});

		Image image = null;
		if ((findFilterText.getStyle() & SWT.CANCEL) == 0) {
			ImageRegistry registry = WidgetPlugin.getDefault().getImageRegistry();
			image = registry.get(WidgetPlugin.KEY_IMAGE_CLOSE_NICE);
			ToolBar toolBar = new ToolBar (comboTextComposite, SWT.FLAT);
			ToolItem clearButton = new ToolItem (toolBar, SWT.PUSH);
			clearButton.setImage (image);
			clearButton.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
				@Override
				public void widgetSelected(SelectionEvent e) {
					searchCancelled();
				}
			});
		}

		// Set the text field to be 20 empty spaces since the setSize method was
		// not successful in reserving the space.
		// The text field is set to an empty string after the
		// findFilterComposite.pack() method is called.
		findFilterText.setText("                    ");
		findFilterComposite.pack();
		findFilterText.setText("");
	}

	protected abstract TreeViewer buildTreeViewer(Composite parent);

	/** 
	 * Return the tree element's tooltip text
	 * @param element to get text for
	 * @return the tooltip text
	 */
	protected String getToolTipText(Object element) {
		return null;
	}

	private synchronized void updateToolTip(MouseEvent e) {
		TreeItem item = getTreeViewer().getTree().getItem(new Point(e.x, e.y));
		String tooltipText = null;
		if (item != null) {
			Object element = item.getData();
			tooltipText = getToolTipText(element);
		}
		if (CommonUtils.equals(oldTooltipText, tooltipText)) {
			return;
		}
		oldTooltipText = tooltipText;
		tooltipText = WidgetUtils.formatToolTipText(tooltipText);
		getTreeViewer().getTree().setToolTipText(tooltipText);
	}

	/**
	 * Update the text in the findFilterMode Combo based upon the currently
	 * selected tab.
	 */
	private void updateModeCombo(){
		if(findFilterMode == null) return;
		List<String> currentModes = Arrays.asList(findFilterMode.getItems());
		List<String> desiredModes = Arrays.asList(TAB_FIND_FILTER_MODES);
		
		if( currentModes.size() != desiredModes.size() ||
		    !currentModes.containsAll(desiredModes) ) {
			
			findFilterMode.removeAll();
			for(String mode : TAB_FIND_FILTER_MODES) {
				findFilterMode.add(getDisplayText(mode));
			}
			findFilterMode.select(0);
		}
	}
	
	/**
	 * Implementing classes can override in order to customize
	 * the combo box text.
	 * 
	 * @param filterModeText the programmatic text
	 * @return the display text for the combo box
	 */
	protected String getDisplayText(String filterModeText) {
		return filterModeText; 
	}
	
	/**
	 * A heuristic for determining how much information to show in this view. If
	 * nodes of the tree have an excessively large number of children then it
	 * may not always be ideal to display all the children by default. This
	 * method attempts to display a reasonable amount of information to the
	 * user.
	 * 
	 * @param treeViewer
	 */
	protected void applyExpandHeuristic(TreeViewer treeViewer) {
		ITreeContentProvider cp = (ITreeContentProvider) treeViewer.getContentProvider();
		
		Object[] rootChildren = cp.getElements(null);
		if (rootChildren.length == 0) return;
		Object root = cp.getParent(rootChildren[0]);
		
		FindFilter filter = new FindFilter();
		
		Object children[] = filter.filter(treeViewer, root, rootChildren);
		if(children.length == 1) {
			treeViewer.expandAll();
			return;
		}
		
		int totalChildren = 0;
		for(int i=0; i<25 && i<children.length; i++) {
			totalChildren += filter.filter(treeViewer, children[i], cp.getChildren(children[i])).length;
		}
		
		if(totalChildren > 25) {
			treeViewer.collapseAll();
			treeViewer.expandToLevel(root, 1);
		} else {
			treeViewer.expandAll();
		}
	}
	
	/**
	 * Asks this part to take focus within the workbench. This method passes the
	 * focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}
	
	private void searchCancelled() {
		findFilterText.setText("");
		applyExpandHeuristic(treeViewer);
	}

	/**
	 * This class filters the elements to display in the view based upon the
	 * currently specified search criteria.
	 */
	private final class FindFilter extends ViewerFilter {
		
		/**
		 * Returns whether the given element makes it through this filter.
		 * 
		 * @param viewer
		 * @param parentElement
		 * @param element
		 * @return true if element meets the search criteria 
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			
			// sanity check
			if(findFilterMode == null || findFilterText == null) return true; 
			
			// retrieve the current case insensitive search criteria
			String mode       = findFilterMode.getText();
			String filterText = findFilterText.getText().trim().toUpperCase();
			
			// return true if no search criteria specified
			if(filterText.length() == 0) return true;
			
			TreeViewer treeViewer         = (TreeViewer) viewer;
			ITreeContentProvider provider = (ITreeContentProvider) treeViewer.getContentProvider();
			
			
			if(mode.equals(getDisplayText(DEFINITION_CATEGORY))) {
				// walk up the tree until reaching a category containter
				while(!(element instanceof String))
					element = provider.getParent(element);
				
				// apply case insensitive search criteria to the category
				return ((String) element).toUpperCase().indexOf(filterText) != -1;
			}
			
			else if(mode.equals(getDisplayText(DEFINITION_NAME))) {
				// if the current element is an activity then immediately apply
				// case insensitive search criteria
				if (element instanceof INamedDefinition)
					return ((INamedDefinition) element).getName().toUpperCase().indexOf(filterText) != -1;
				
				// if the current element is not an activity then recurse
				// through all children until either a match is found or all
				// have been examined
				Queue<Object> openList = new LinkedList<Object>();
				openList.offer(element);
				while (!openList.isEmpty()) {
					Object thisElement = openList.poll();
					for(Object childElement : provider.getChildren(thisElement)) {
						if(childElement instanceof INamedDefinition &&
							((INamedDefinition) childElement).getName().toUpperCase().indexOf(filterText) != -1)
							return true;
						
						openList.offer(childElement);
					}
				}
			}
			
			// if all else fails, return false
			return false;
		}
		
	}
	
	@Override
	public void dispose() {
		treeViewer.setSelection(StructuredSelection.EMPTY);
		super.dispose();
	}
	
}
