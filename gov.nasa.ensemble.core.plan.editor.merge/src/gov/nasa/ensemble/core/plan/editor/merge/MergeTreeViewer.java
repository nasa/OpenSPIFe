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
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.collections.AutoListMap;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.common.ui.treetable.TreeTableComposite;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewer;
import gov.nasa.ensemble.core.activityDictionary.view.transfer.ActivityDefSerializableTransfer;
import gov.nasa.ensemble.core.model.plan.CommonMember;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.util.PlanVisitor;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.configuration.ColumnConfigurationResource;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.TableColumnsConfigurationDialog;
import gov.nasa.ensemble.core.plan.editor.transfers.ActivityTransferProvider;
import gov.nasa.ensemble.core.plan.editor.transfers.PlanContainerTransferProvider;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IWorkbenchPartSite;

public class MergeTreeViewer extends TreeTableViewer<EPlanElement, EStructuralFeature> {

	private static final boolean DYNAMIC_COLUMN_CONFIGURATION_DISABLED_ON_LINUX = EnsembleProperties.getBooleanPropertyValue("merge.column.disable.configuration.linux", false);
	protected static final Transfer[] TRANSFERS = new Transfer[] { ActivityDefSerializableTransfer.getInstance(), PlanContainerTransferProvider.transfer, ActivityTransferProvider.transfer, };

	public MergeTreeViewer(TreeTableComposite treeComposite, final TreeTableColumnConfiguration configuration, IWorkbenchPartSite site) {
		super(treeComposite, configuration, site);
		addFilter(new VisabilityFilter());
		treeComposite.getTree().addTreeListener(new PlanElementTreeListener()); // will be disposed with the Tree

		final Tree tree = getTree();
		tree.addMenuDetectListener(new MenuDetectListener() {
			@Override
			public void menuDetected(MenuDetectEvent e) {
				Point point = tree.toControl(e.x, e.y);
				Rectangle clientArea = tree.getClientArea();
				if (clientArea.y <= point.y && point.y < (clientArea.y + tree.getHeaderHeight())) {
					e.doit = false;
					int columnIndex = getColumnIndex(point.x);
					fillHeaderContextMenu(e.display, columnIndex, point, configuration.getColumns());
				}
			}
		});
	}

	@Override
	protected void inputChanged(Object input, Object oldInput) {
		super.inputChanged(input, oldInput);
	}

	@Override
	protected Transfer[] getSupportedTransfers() {
		return TRANSFERS;
	}

	@Override
	protected void update(Runnable runnable) {
		TransactionUtils.runInDisplayThread(getControl(), getPlan(), runnable);
	}

	@Override
	public void updateElementFeatures(Map<? extends EPlanElement, Set<EStructuralFeature>> map) {
		for (ITreeTableColumn column : configuration.getColumns()) {
			if (column instanceof AbstractMergeColumn && ((AbstractMergeColumn) column).updateAll()) {
				update(new Runnable() {
					@Override
					public void run() {
						refresh();
					}
				});
				return;
			}
		}
		super.updateElementFeatures(map);
	}

	private EPlan getPlan() {
		PlanEditorModel model = (PlanEditorModel) getModel();
		if (model != null) {
			return model.getEPlan();
		}
		return null;
	}

	/** From the mouse's x location get which column header you right clicked on! */
	public int getColumnIndex(int click_x) {
		int columnIndex = 0;
		int x = 0;
		Tree tree = this.getTree();
		TreeColumn[] columns = tree.getColumns();
		int[] order = tree.getColumnOrder();
		for (int i : order) {
			TreeColumn column = columns[i];
			int width = column.getWidth();
			if (click_x < x + width) {
				break;
			}
			x += width;
			columnIndex++;
		}
		return columnIndex;
	}

	@Override
	protected void setSort(ITreeTableColumn treeTableColumn, int sortDirection) {
		super.setSort(treeTableColumn, sortDirection);
		saveColumnConfigurationResource();
	}

	private Map<String, List<AbstractMergeColumn>> getAllColumnsByProvider() {
		List<AbstractMergeColumn> allColumns = TableEditorUtils.getAllColumns(getColumnConfigurationResource());
		Map<String, List<AbstractMergeColumn>> allColumnsByProviderMap = new AutoListMap(String.class);
		for (AbstractMergeColumn column : allColumns) {
			allColumnsByProviderMap.get(column.getProviderName()).add(column);
		}
		return allColumnsByProviderMap;
	}

	private void fillHeaderContextMenu(Display display, final int columnIndex, Point location, final List<? extends AbstractMergeColumn> oldColumns) {
		final Shell shell = new Shell(display);
		final Menu menu = new Menu(shell, SWT.POP_UP);
		final Map<String, List<AbstractMergeColumn>> allColumnsByProvider = getAllColumnsByProvider();
		if (!needToReopenEditorOnColumnConfigurationChange()) {
			for (Entry<String, List<AbstractMergeColumn>> entry : allColumnsByProvider.entrySet()) {
				List<AbstractMergeColumn> value = entry.getValue();
				if (value.isEmpty()) {
					continue;
				}
				Menu subMenu = new Menu(menu);
				MenuItem providerItem = new MenuItem(menu, SWT.CASCADE);
				providerItem.setText(entry.getKey());
				providerItem.setMenu(subMenu);
				for (final AbstractMergeColumn column : value) {
					final MenuItem item = new MenuItem(subMenu, SWT.CHECK);
					item.setText(column.getHeaderName());
					item.setSelection(oldColumns.contains(column));
					item.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							List<ITreeTableColumn> newColumns = new ArrayList<ITreeTableColumn>(oldColumns);
							if (item.getSelection()) {
								if (columnIndex >= newColumns.size()) {
									newColumns.add(column);
								} else {
									newColumns.add(columnIndex + 1, column);
								}
							} else {
								newColumns.remove(column);
							}
							configuration.setColumns(newColumns);
							saveColumnConfigurationResource();
						}
					});
				}
			}
			new MenuItem(menu, SWT.SEPARATOR);
		}
		MenuItem preferences = new MenuItem(menu, SWT.PUSH);
		preferences.setText("Configure table...");
		IFile file = PlanUtils.getFile(getPlan());
		final URI planURI = file.getLocationURI();
		final ColumnConfigurationResource columnConfigurationResource = getColumnConfigurationResource();
		preferences.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<AbstractMergeColumn> allColumns = new ArrayList();
				for (List<AbstractMergeColumn> columns : allColumnsByProvider.values()) {
					allColumns.addAll(columns);
				}
				Shell dialogShell = getControl().getShell();
				TableColumnsConfigurationDialog dialog = new TableColumnsConfigurationDialog(dialogShell, allColumns, columnConfigurationResource, planURI, needToReopenEditorOnColumnConfigurationChange());
				dialog.open();
			}
		});

		menu.setVisible(true);
		shell.setMenu(menu);
	}

	@Override
	public void refresh() {
		super.refresh();
		EPlan plan = getPlan();
		if (plan != null) {
			final List<Object> objects = new ArrayList<Object>();
			PlanVisitor visitor = new PlanVisitor() {
				@Override
				protected void visit(EPlanElement element) {
					CommonMember member = element.getMember(CommonMember.class);
					if (member.isExpanded()) {
						objects.add(element);
					}
				}
			};
			visitor.visitAll(plan);
			setExpandedElements(objects.toArray());
		}
	}

	public boolean needToReopenEditorOnColumnConfigurationChange() {
		return (CommonUtils.isOSLinux() && DYNAMIC_COLUMN_CONFIGURATION_DISABLED_ON_LINUX);
	}

	private ColumnConfigurationResource getColumnConfigurationResource() {
		EPlan plan = getPlan();
		if (plan != null) {
			for (Resource resource : plan.eResource().getResourceSet().getResources()) {
				if (resource instanceof ColumnConfigurationResource) {
					ColumnConfigurationResource columnConfigurationResource = (ColumnConfigurationResource) resource;
					if ((TreeTableColumnConfiguration<?>) columnConfigurationResource.getConfiguration() == configuration) {
						return columnConfigurationResource;
					}
				}
			}
		}
		return null;
	}

	public void saveColumnConfigurationResource() {
		ColumnConfigurationResource columnConfigurationResource = getColumnConfigurationResource();
		if (columnConfigurationResource != null) {
			try {
				columnConfigurationResource.save(null);
			} catch (IOException e) {
				LogUtil.error("Couldn't save column configuration: " + columnConfigurationResource.getURI().toString());
			}
		}
	}

}
