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
package gov.nasa.ensemble.core.plan.resources.ui.view;

import gov.nasa.ensemble.common.time.DateFormatRegistry;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.ui.Activator;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class InconsTableUtils {
	
	private static DateFormat DATE_FORMAT = DateFormatRegistry.INSTANCE.getDefaultDateFormat();
	private static final Image CHECKED = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/checked.png").createImage();
	private static final Image UNCHECKED = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/unchecked.png").createImage();
	
	public static TableViewer getTableViewer(Composite composite, IStructuredContentProvider contentProvider) {
		TableColumnLayout layout = new TableColumnLayout();
		composite.setLayout(layout);
		TableViewer viewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
		viewer.getTable().setLinesVisible(true);
		viewer.getTable().setHeaderVisible(true);
		createColumns(viewer, layout);
		viewer.setContentProvider(contentProvider);
		viewer.setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				Date time1 = ((Conditions)e1).getTime();
				Date time2 = ((Conditions)e2).getTime();
				if (time1 == null) {
					return 1;
				}
				if (time2 == null) {
					return -1;
				}
				return time1.compareTo(time2);
			}
		});
		return viewer;
	}

	public static void createColumns(TableViewer viewer, TableColumnLayout layout) {
		TableViewerColumn column = createColumnWithWeight(viewer, layout, "Description", 100);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
            public String getText(Object element) {
				Conditions incon = (Conditions)element;
				return incon.getDescription();
			}
		});
		column = createColumnWithWeight(viewer, layout, "Time", 50);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
            public String getText(Object element) {
				Conditions incon = (Conditions)element;
				Date time = incon.getTime();
				return time == null ? "" : DATE_FORMAT.format(time);
			}
		});
		column = createColumnWithWeight(viewer, layout, "Active", 25);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return null;
			}

			@Override
			public Image getImage(Object element) {
				Conditions incon = (Conditions)element;
				if (incon.isActive()) {
					return CHECKED;
				} 
				return UNCHECKED;
			}
		});
		
	}
	
	public static TableViewerColumn createColumnWithWeight(TableViewer viewer, TableColumnLayout layout, String header, int weight) {
		TableViewerColumn column = new TableViewerColumn(viewer, SWT.NONE);
        column.getColumn().setText(header);
        column.getColumn().setAlignment(SWT.CENTER);
        column.getColumn().setMoveable(false);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(weight, ColumnWeightData.MINIMUM_WIDTH, false));
        return column;
	}
}
