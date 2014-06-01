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
package gov.nasa.ensemble.core.plan.editor.merge;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.help.ContextProvider;
import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.common.ui.operations.ClipboardCutOperation;
import gov.nasa.ensemble.common.ui.operations.DeleteOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.common.ui.treetable.TreeTableComposite;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewerSelectionListener;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.editor.AbstractPlanEditorPart;
import gov.nasa.ensemble.core.plan.editor.AddGroupOperation;
import gov.nasa.ensemble.core.plan.editor.PlanAddOperation;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.merge.action.IToggleFlattenEditor;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.IMergeRowHighlightDecorable;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.MergeRowHighlightDecorator;
import gov.nasa.ensemble.core.plan.editor.merge.operations.MergePlanClipboardCutOperation;
import gov.nasa.ensemble.core.plan.editor.merge.operations.MergePlanDeleteOperation;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.MergeEditorPreferencePage;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.runtime.Platform;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchSite;

public class MergeEditor extends AbstractPlanEditorPart implements IReusableEditor, IToggleFlattenEditor, IMergeRowHighlightDecorable {

	private TreeTableComposite treeComposite;   // the Tree to display ActivityGroups and Activities
	private MergeTotalComposite totalComposite; // the Table to display resource totals
	private Composite editorComposite = null;

	/* the viewer that handles tree updates based on model changes and tree interaction */
	private MergeTreeViewer treeViewer;
	private MergeTotalViewer totalViewer;
	private final MergeRowHighlightDecorator rowHighlightDecorator = new MergeRowHighlightDecorator(getRowHighlightDecoratorKey());
	
	@Override
	public String getRowHighlightDecoratorKey() {
		return "table_row_highlight_property";
	}
	
	@Override
	public String getId() {
		return "table";
	}

	/**
	 * @return the merge tree viewer
	 */
	public MergeTreeViewer getViewer() {
		return treeViewer;
	}
	
	@Override
	public PlanStructureModifier getStructureModifier() {
		return PlanStructureModifier.INSTANCE;
	}
	
	@Override
	public void refresh() {
		treeViewer.refresh();
	}
	
	@Override
	public void updateActionBars(IActionBars bars) {
		if (bars != null) {
			treeViewer.setActionBars(bars);
		}
		super.updateActionBars(bars);
	}
	
	@Override
	protected void setSite(IWorkbenchPartSite newSite) {
		newSite.setSelectionProvider(new EnsembleSelectionProvider(this.toString()));
	    super.setSite(newSite);
	}

	@Override
	public void toggleFlatten() {
		MergeTreeContentProvider provider = (MergeTreeContentProvider)treeViewer.getContentProvider();
		provider.toggleFlatten();
		treeViewer.refresh();
	}
	
	@Override
	public boolean isFlattened() {
		MergeTreeContentProvider provider = (MergeTreeContentProvider) this.treeViewer.getContentProvider();
		return provider.isFlattened();
	}		

	/**
     * Sets the input to this editor.
     * 
     * <p><b>Note:</b> Clients must fire the {@link IEditorPart#PROP_INPUT } 
     * property change within their implementation of 
     * <code>setInput()</code>.<p>
     *
     * @param input the editor input
     */	
	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		if (treeViewer != null) {
			PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
		    treeViewer.setInput(model.getEPlan());
		    treeViewer.setEditorModel(model);
		}
		firePropertyChange(IWorkbenchPartConstants.PROP_INPUT);		
	}
	
	@Override
	public void dispose() {
		if (treeViewer != null) {
			EnsembleSelectionProvider selectionProvider = (EnsembleSelectionProvider)getSelectionProvider();
			if (selectionProvider != null) {
				selectionProvider.detachSelectionProvider(treeViewer);
			}
			treeViewer = null;
		}
		/*
		if (mergeTreeFont != null) {
			mergeTreeFont.dispose();
		}*/
		if (treeComposite != null) {
			treeComposite.dispose();
		}
		if (editorComposite != null) {
			editorComposite.dispose();
		}
		totalViewer = null;
		super.dispose();
	}

	@Override
	public void createPartControl(Composite parent) {
		IEditorInput input = getEditorInput();
		final TreeTableColumnConfiguration configuration = TableEditorUtils.getTableConfiguration(getId(), input);
		final PlanEditorModel model = PlanEditorModelRegistry.getPlanEditorModel(input);
		final EPlan plan = model.getEPlan();
		EnsembleSelectionProvider selectionProvider = (EnsembleSelectionProvider)getSelectionProvider();

		editorComposite = new EnsembleComposite(parent, SWT.NONE);
		// layout for the editor (arrangement and spacing between tree and table)
		GridLayout layout = new GridLayout(1, true);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 0;
		editorComposite.setLayout(layout);
		
		// setup tree
		treeComposite = buildTree(editorComposite, configuration);
		treeComposite.setData("name", "MergeEditor.treeComposite");
		treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		treeViewer = new MergeTreeViewer(treeComposite, configuration, getSite());
		treeViewer.setContentProvider(new MergeTreeContentProvider());
		MergeTreeLabelProvider labelProvider = new MergeTreeLabelProvider();
		labelProvider.setRowHighlightDecorator(rowHighlightDecorator);
		treeViewer.setLabelProvider(labelProvider);
		treeViewer.setInput(plan);
		treeViewer.setEditorModel(model);
		treeViewer.expandToLevel(1);
		treeViewer.setAutoExpandLevel(1);
		selectionProvider.attachSelectionProvider(new TreeTableViewerSelectionListener(treeViewer));
		
		// setup table
		totalComposite = new MergeTotalComposite(editorComposite, configuration);
		totalComposite.setData("name", "MergeEditor.totalComposite");
		totalComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		totalViewer = new MergeTotalViewer(totalComposite);
		totalViewer.setInput(treeViewer);
		
		if (Platform.getOS().equals(Platform.OS_LINUX)) {
			syncTreeTableScrollBars();
		}
		
		IWorkbenchPage page = getSite().getPage();
		IEditorPart activeEditor = page.getActiveEditor();
		if ((activeEditor != null) && (activeEditor.getEditorInput() == input)) {
			treeViewer.setSelection(page.getSelection());
		}
		
		// make sure all the row/column sizes are correct given the current font
		setupFonts(editorComposite, treeComposite, totalComposite);
		
		MenuManager menuManager = WidgetUtils.createContextMenu(treeComposite.getTree(), new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});
		// allow extensions to the context menu but not those based on the editor input
		getEditorSite().registerContextMenu(menuManager, getSelectionProvider(), false);
	}

	/**
	 * This only works on linux
	 */
	private void syncTreeTableScrollBars() {
		final ScrollBar treeBar = treeComposite.getTree().getHorizontalBar();
		final ScrollBar totalBar = totalComposite.getTable().getHorizontalBar();
		treeBar.setVisible(false);
		treeBar.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				scrollTable();
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				scrollTable();
			}
			private void scrollTable() {
				int selection = treeBar.getSelection();
				totalBar.setSelection(selection);
			}
		});
		totalBar.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				scrollTree();
			}
			@Override
			public void widgetSelected(SelectionEvent e) {
				scrollTree();
			}
			private void scrollTree() {
				int selection = totalBar.getSelection();
				treeBar.setSelection(selection);
			}
		});
	}
	
	@Override
	public void setFocus() {
		treeComposite.setFocus();
	}

	@Override
	public Object getAdapter(Class key) {
		if (key.equals(IContextProvider.class)) {
			return new ContextProvider("gov.nasa.ensemble.core.plan.editor.merge.TreePlanEditor");
		} else if (key.equals(MergeRowHighlightDecorator.class)) {
			IBaseLabelProvider labelProvider = treeViewer.getLabelProvider();
			if (labelProvider instanceof MergeTreeLabelProvider) {
				return ((MergeTreeLabelProvider) labelProvider).getRowHighlightDecorator();
			}
		}
		return super.getAdapter(key);
	}

	@Override
	protected ClipboardCutOperation getCutOperation(IStructureModifier modifier, ISelection selection, Event event) {
		if (treeComposite == null) {
			return null;
		}
		ITransferable transferable = modifier.getTransferable(selection);
		return new MergePlanClipboardCutOperation(transferable, modifier, getSelectionProvider(), treeComposite.getTree());
	}

	@Override
	protected DeleteOperation getDeleteOperation(IStructureModifier modifier, ISelection selection, Event event) {
		if (treeComposite == null) {
			return null;
		}
		ITransferable transferable = modifier.getTransferable(selection);
		return new MergePlanDeleteOperation(transferable, modifier, getSelectionProvider(), treeComposite.getTree());
	}

	@Override
	protected IUndoableOperation getAddGroupOperation(IStructureModifier modifier, ISelection selection) {
		EPlanElement target = getTargetElement(modifier, selection);
		if (target == null) {
			return null;
		}
		return new AddGroupOperation(modifier, target) {
			@Override
			public void displayExecute(Widget widget, IWorkbenchSite site) {
				super.displayExecute(widget, site);
				final Tree tree = treeViewer.getTree();
				TreeItem[] selection = tree.getSelection();
				if (selection != null && selection.length == 1) {
					treeViewer.handleEditRequest(selection[0], 0);
				}
			}
		};
	}
	
	/*
	 * Utility methods
	 */
	
	private void duplicateCurrentSelection(KeyEvent e) {
		ISelection selection = treeViewer.getSelection();
		if (selection.isEmpty()) {
			return; // nothing to do
		}
		PlanStructureModifier modifier = getStructureModifier();
		PlanTransferable source = modifier.getTransferable(selection);
		PlanTransferable copy = (PlanTransferable)modifier.copy(source);
		IStructureLocation location = modifier.getInsertionLocation(copy, selection, InsertionSemantics.AFTER);
		AbstractOperation op = new PlanAddOperation(copy, modifier, location);
		op.setLabel("Duplicate element(s)");
		WidgetUtils.execute(op, getUndoContext(), e.widget, getSite());
	}

	/**
	 * Build the Tree widget for the editor to contain ActivityGroups and Activities.
	 * @param parent
	 * @param configuration 
	 * @return
	 */
	private TreeTableComposite buildTree(Composite parent, TreeTableColumnConfiguration configuration) {
		TreeTableComposite treeComposite = new TreeTableComposite(parent, configuration, true);
		treeComposite.setLayout(new FillLayout());
		treeComposite.getTree().addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.INSERT) {
					duplicateCurrentSelection(e);
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// nothing
			}
		});
		return treeComposite;
	}

	private void setupFonts(Composite parent, final TreeTableComposite tree, final MergeTotalComposite total) {
		final IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(MergeEditorPreferencePage.P_MERGE_EDITOR_FONT_SIZE)) {
					Font font = getMergeTreeFont();
					tree.getTree().setFont(font);
					total.getTable().setFont(font);
					editorComposite.layout(true);
					IBaseLabelProvider labelProvider = treeViewer.getLabelProvider();
					if (labelProvider instanceof MergeTreeLabelProvider) {
						((MergeTreeLabelProvider)labelProvider).updateFonts();
					}
					treeViewer.refresh(true);
				}
			}
		};
		parent.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				MergePlugin.getDefault().getPreferenceStore().removePropertyChangeListener(preferenceListener);
			}
		});
		MergePlugin.getDefault().getPreferenceStore().addPropertyChangeListener(preferenceListener);
		Font font = getMergeTreeFont();
		tree.getTree().setFont(font);
		total.getTable().setFont(font);
		total.layout(true);
	}

	private Font mergeTreeFont = null;
	private Font getMergeTreeFont() {
		if (mergeTreeFont != null) {
			//mergeTreeFont.dispose();
		}
		FontData systemFontData = Display.getDefault().getSystemFont().getFontData()[0];
		int height = MergePlugin.getDefault().getPreferenceStore().getInt(MergeEditorPreferencePage.P_MERGE_EDITOR_FONT_SIZE);
		mergeTreeFont = FontUtils.getStyledFont(null, systemFontData.getName(), height, systemFontData.getStyle()); 
		return mergeTreeFont;
	}

	/*
	 * Utility methods
	 */
	
	/**
	 * Get the TreeItem that should be selected after removing the current selection.
	 */
	public static TreeItem getNewlySelectedItem(Tree tree) {
		TreeItem[] selection = tree.getSelection();
		if ((selection == null) || (selection.length == 0)) {
			return null;
		}
		TreeSet<TreeItem> selectionSet = new TreeSet<TreeItem>(new Comparator<TreeItem>() {
			@Override
			public final int compare(TreeItem o1, TreeItem o2) {
				return CommonUtils.compare(o1.getBounds().y, o2.getBounds().y);
			}
		});
		List<TreeItem> selectionList = Arrays.asList(selection);
		selectionSet.addAll(selectionList);
		TreeItem previousItem = null;
		TreeItem parentItem = null;
		while (!selectionSet.isEmpty()) {
			TreeItem focusItem = selectionSet.iterator().next();
			parentItem = focusItem.getParentItem();
			TreeItem[] items = (parentItem != null ? parentItem.getItems() : focusItem.getParent().getItems());
			boolean found = false;
			for (int i = 0 ; i < items.length ; i++) {
				TreeItem item = items[i];
				if (focusItem == item) {
					found = true;
					if ((i > 0) && (previousItem == null)) {
						previousItem = items[i-1];
					}
				}
				if (found) {
					if (!selectionSet.remove(item)) {
						return item;
					}
				}
			}
		}
		if (previousItem != null && !selectionList.contains(previousItem)) {
			return previousItem;
		}
		if (parentItem != null && !selectionList.contains(parentItem)) {
			return parentItem;
		}
		return null;
	}

}
