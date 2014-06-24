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
/**
 * 
 */
package gov.nasa.arc.spife.ui.table.days;

import gov.nasa.arc.spife.ui.table.days.filter.AbstractDaysFilter;
import gov.nasa.arc.spife.ui.table.days.filter.AllActivitiesDaysFilter;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.common.ui.operations.ClipboardCutOperation;
import gov.nasa.ensemble.common.ui.operations.DeleteOperation;
import gov.nasa.ensemble.common.ui.transfers.ITransferable;
import gov.nasa.ensemble.common.ui.treetable.TreeTableComposite;
import gov.nasa.ensemble.core.plan.editor.AbstractPlanEditorPart;
import gov.nasa.ensemble.core.plan.editor.merge.action.IToggleFlattenEditor;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.IMergeRowHighlightDecorable;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.MergeRowHighlightDecorator;
import gov.nasa.ensemble.core.plan.editor.merge.operations.MergePlanClipboardCutOperation;
import gov.nasa.ensemble.core.plan.editor.merge.operations.MergePlanDeleteOperation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * @author andrewb, ideliz
 *
 */
public class DaysEditor extends AbstractPlanEditorPart implements IToggleFlattenEditor,  IMergeRowHighlightDecorable {

	private Combo filterCombo;
	protected DaysComposite daysComposite;
	private EnsembleSelectionProvider selectionProvider = new EnsembleSelectionProvider(this.toString());
	
	private final MergeRowHighlightDecorator rowHighlightDecorator = new MergeRowHighlightDecorator(getRowHighlightDecoratorKey());

	//filters
	private static final String DEFAULT_FILTER_NAME = EnsembleProperties.getProperty("days.filter.default", AllActivitiesDaysFilter.ALL_ACTIVITIES_DAYS_FILTER_NAME);
	private static Map<String, AbstractDaysFilter> FILTERS = createFiltersMap();
	private AbstractDaysFilter currentFilter = FILTERS.get(DEFAULT_FILTER_NAME);
	
	public DaysEditor() {
		super();
	}
	
	@Override
	public String getRowHighlightDecoratorKey() {
		return "days_row_highlight_property";
	}
	
	@Override
	public String getId() {
		return "days";
	}
	
	private static Map<String, AbstractDaysFilter> createFiltersMap() {
		Map<String, AbstractDaysFilter> map = new LinkedHashMap();
		List<AbstractDaysFilter> instances = ClassRegistry.createInstances(AbstractDaysFilter.class);
		for (AbstractDaysFilter filter : instances) {
			map.put(filter.getName(), filter);
		}
		return Collections.unmodifiableMap(map);
	}
	
	// 1st
	@Override
	protected void setSite(IWorkbenchPartSite newSite) {
		newSite.setSelectionProvider(selectionProvider);
	    super.setSite(newSite);
	}
	
	// 2nd
	@Override
	public void setInput(IEditorInput input) {
		super.setInput(input);
		if (daysComposite != null) {
			daysComposite.setInput(input);
		}
		firePropertyChange(IWorkbenchPartConstants.PROP_INPUT);
	}
	
	@Override
	public void toggleFlatten() {
		daysComposite.toggleFlatten();
	}

	// 3rd
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		
		createAdditionalControls(composite);
		
		// filter combo box!
		filterCombo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		String[] filterNames = new String[FILTERS.size()];
		int index = 0;
		for (Entry<String, AbstractDaysFilter> entry : FILTERS.entrySet()) {
			filterNames[index++] = entry.getKey();
		}
		filterCombo.setItems(filterNames);
		filterCombo.setText(currentFilter.getName());
		GridData comboLayoutData = new GridData(GridData.END, GridData.FILL, false, false, 1, 1);
		comboLayoutData.minimumWidth = 100;
		comboLayoutData.verticalIndent = 3;
		filterCombo.setLayoutData(comboLayoutData);
		filterCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				daysComposite.removeDaysFilter(currentFilter);
				int index = filterCombo.getSelectionIndex();
				String item = filterCombo.getItem(index);
				currentFilter = FILTERS.get(item);
				daysComposite.addDaysFilter(currentFilter);
			}
		});
	
		daysComposite = new DaysComposite(composite, this);
		daysComposite.setInput(getEditorInput());
		daysComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, layout.numColumns, 1));
		daysComposite.setupPreferenceListener();
		daysComposite.addDaysFilter(currentFilter);
	}
	
	AbstractDaysFilter getCurrentDaysFilter() {
		return currentFilter;
	}
	
	protected void createAdditionalControls(Composite parent) {
		// hook for adding additional controls to left of combo box
		// override in subclass
		new Label(parent, SWT.NONE);
	}
	
	protected void daySelected(int day) {
		// hook for propagating selection of a day using the DaysComposite scrollbar
	}

	@Override
	public void dispose() {
		daysComposite.dispose();
		selectionProvider.setSelection(StructuredSelection.EMPTY);
		super.dispose();
	}

	@Override
	public void refresh() {
		daysComposite.refreshDayViewers();
	}
	
	@Override
	public void setFocus() {
		if (daysComposite.setFocus()) {
			return;
		}
		filterCombo.setFocus();
	}
	
	@Override
	public void updateActionBars(IActionBars bars) {
		DaysComposite composite = daysComposite;
		if ((composite != null) && (bars != null)) {
			composite.setActionBars(bars);
		}
		super.updateActionBars(bars);
	}
	
	@Override
	public IStructureModifier getStructureModifier() {
		if (daysComposite != null) {
			DayViewer activeViewer = daysComposite.getActiveViewer();
			if (activeViewer != null) {
				DayContentProvider contentProvider = (DayContentProvider) activeViewer.getTreeTableViewer().getContentProvider();
				if (contentProvider != null) {
					return contentProvider.getStructureModifier();
				}
			} 
		}
		return super.getStructureModifier();
	}
	
	@Override
	protected ClipboardCutOperation getCutOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		Tree tree = getActiveTree();
		if (tree == null) {
			return null;
		}
		return new MergePlanClipboardCutOperation(transferable, modifier, getSelectionProvider(), tree);
	}

	@Override
	protected DeleteOperation getDeleteOperation(IStructureModifier modifier, ISelection selection, Event event) {
		ITransferable transferable = modifier.getTransferable(selection);
		Tree tree = getActiveTree();
		if (tree == null) {
			return null;
		}
		return new MergePlanDeleteOperation(transferable, modifier, getSelectionProvider(), tree);
	}

	private Tree getActiveTree() {
		if (daysComposite == null) {
			return null;
		}
		DayViewer activeViewer = daysComposite.getActiveViewer();
		if (activeViewer == null) {
			return null;
		}
		DayComposite control = activeViewer.getControl();
		TreeTableComposite treeTableComposite = control.getTreeTableComposite();
		return treeTableComposite.getTree();
	}

	@Override
	public boolean isFlattened() {
		return this.daysComposite.isFlattened();
	}
	
	@Override
	public Object getAdapter(Class clazz) {
		if (clazz.equals(MergeRowHighlightDecorator.class)) {
			return rowHighlightDecorator;
		}
		return super.getAdapter(clazz);
	}
	
	public void updateDayViewerConfiguration(int fontSize, int nameColumnWidth, int restColumnWidth) {
		daysComposite.updateDayViewerConfiguration(fontSize, nameColumnWidth, restColumnWidth);
	}
	
}
