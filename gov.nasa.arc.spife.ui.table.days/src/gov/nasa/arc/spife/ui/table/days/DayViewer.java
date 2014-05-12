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
package gov.nasa.arc.spife.ui.table.days;

import gov.nasa.arc.spife.ui.table.days.preferences.DaysEditorPreferences;
import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.collections.DefaultComparator;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.common.ui.treetable.TreeTableComposite;
import gov.nasa.ensemble.common.ui.treetable.TreeTableViewerComparator;
import gov.nasa.ensemble.core.model.plan.EDay;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.PlanPackage;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTotalViewer;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTreeContentProvider;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTreeLabelProvider;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTreeViewer;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.MergeRowHighlightDecorator;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPartSite;

/**
 * This class is a viewer for a particular day in the days editor.
 * Constructing the viewer constructs the day composite.
 * 
 * @author abachman, ideliz
 *
 */
public class DayViewer extends Viewer {

	private static final int MAGIC_TRIM_WIDTH = 25; // enough room for the border around the tree, and the tree trim, including the vertical scroll bar 
	private static final String TIMELINE_BUBBLE_FORMAT = EnsembleProperties.getProperty("timeline.bubble.format");
	private static final SimpleDateFormat DAY_BUBBLE_FORMAT = new SimpleDateFormat(TIMELINE_BUBBLE_FORMAT);
	private static final SimpleDateFormat DAY_ID_FORMAT = new SimpleDateFormat("DDD/yyyy");
	static {
		Calendar calendar = MissionConstants.getInstance().getMissionCalendar();
		DAY_BUBBLE_FORMAT.setCalendar(calendar);
		DAY_ID_FORMAT.setCalendar(calendar);
	}

	private final DayComposite composite;
	private final DayFilter dayFilter = new DayFilter();
	private final MergeTreeViewer treeTableViewer;
	private final MergeTotalViewer totalViewer;

	private EDay eDay;
	private Day day;
	private DayContentProvider contentProvider;
	
	private final DayHeaderMouseListener mouseAdapter = new DayHeaderMouseListener();
	private final NoteListener noteListener = new NoteListener();

	public DayViewer(Composite parent, TreeTableColumnConfiguration configuration, final DayColumnProvider dayColumnProvider, IWorkbenchPartSite site) {
		composite = new DayComposite(parent, configuration);
		composite.addMouseAdapter(mouseAdapter);

		TreeTableComposite treeTableComposite = composite.getTreeTableComposite();
		final ScrollBar vBar = treeTableComposite.getTree().getVerticalBar();
		if (vBar != null) {
			vBar.setVisible(true);
		}
		treeTableViewer = new MergeTreeViewer(treeTableComposite, configuration, site) {

			private boolean initialized = false;
			
			@Override
			protected void inputChanged(Object input, Object oldInput) {
				initialized = false;
				super.inputChanged(input, oldInput);
				initialized = true;
			}
			
			@Override
			protected void createChildren(Widget widget) {
				if (initialized && (widget instanceof Tree)) {
					// SPF-11700 -- avoid repeated attempts to create the children of an empty tree
					return;
				}
				super.createChildren(widget);
			}

			@Override
			protected ViewerComparator getDefaultViewerComparator() {
				return DayDefaultViewerComparator.INSTANCE;
			}

			@Override
			public Comparator getDefaultComparator() {
				return DayDefaultComparator.INSTANCE;
			}
			
			@Override
			protected ViewerComparator getColumnComparator(ITreeTableColumn treeTableColumn, int sortDirection) {
				return new DayColumnComparator(treeTableColumn, sortDirection == SWT.UP);
			}
			
		};
		contentProvider = new DayContentProvider();
		treeTableViewer.setContentProvider(contentProvider);
		treeTableViewer.setLabelProvider(new DayLabelProvider());
		try {
			treeTableViewer.addFilter(dayFilter);
		} catch (Exception e) {
			LogUtil.error("failed to add the day filter", e);
		}
		totalViewer = new MergeTotalViewer(composite.getTotalComposite());
		composite.pack();
	}

	public void setRowHighlightDecorator(MergeRowHighlightDecorator decorator) {
		IBaseLabelProvider labelProvider = treeTableViewer.getLabelProvider();
		if (labelProvider instanceof MergeTreeLabelProvider) {
			((MergeTreeLabelProvider) labelProvider).setRowHighlightDecorator(decorator);
		}
	}
	
	public MergeTreeViewer getTreeTableViewer() {
		return treeTableViewer;
	}
	
	public MergeTotalViewer getTotalViewer() {
		return totalViewer;
	}
	
	@Override
	public DayComposite getControl() {
		return composite;
	}

	public void setDay(Day day, EnsembleSelectionProvider selectionProvider) {
		TreeTableComposite treeTableComposite = composite.getTreeTableComposite();
		final ScrollBar vBar = treeTableComposite.getTree().getVerticalBar();
		if (vBar != null) {
			day.saveScrollState(vBar);
		}
		this.day = day;
		eDay = getInput().getEDay(getDayID());
		if (eDay.getBubbleFormattedDate() == null) {
			TransactionUtils.writing(getInput(), new RunnableWithResult.Impl<EDay>() {
				@Override
				public void run() {
					String format = DAY_BUBBLE_FORMAT.format(getDay().getDate());
					eDay.setBubbleFormattedDate(format);
				}
			});
		}
		if (eDay.eContainer() == null) {
			TransactionUtils.writing(getInput(), new RunnableWithResult.Impl<EDay>() {
				@Override
				public void run() {
					getInput().getDays().add(eDay);
				}
			});
		}
		if (!eDay.eAdapters().contains(noteListener)) {
			eDay.eAdapters().add(noteListener);
		}
		composite.setTitle(DAY_BUBBLE_FORMAT.format(day.getDate()));
		composite.setNoteVisible(eDay.getNotes() != null);
		// update the day filter, and then refresh to display that day
		dayFilter.setDay(day);
		contentProvider.setDay(day);
		ISelection selection = selectionProvider.getSelection();
		treeTableViewer.refresh();
		restoreDaySpecificWidgetState(day);
		if (selection != null) {
			treeTableViewer.setSelection(selection);
		}
		totalViewer.refresh();
	}
	
	/**
	 * This method helps the illusion that we have many widgets
	 * by setting the state of this tree to be similar to the
	 * state of the tree that last displayed this day.
	 * 
	 * @param day
	 */
	private void restoreDaySpecificWidgetState(Day day) {
		TreeTableComposite treeTableComposite = composite.getTreeTableComposite();
		ScrollBar vBar = treeTableComposite.getTree().getVerticalBar();
		if (vBar != null) {
			day.restoreScrollState(vBar);
		}
	}
	
	public Day getDay() {
		return day;
	}
	
	public EDay getEDay() {
		return eDay;
	}
	
	public String getDayID() {
		Date date = getDay().getDate();
		return DAY_ID_FORMAT.format(date);
	}
	
	@Override
	public void setInput(Object input) {
		treeTableViewer.setInput(input);
		totalViewer.setInput(treeTableViewer);
	}

	@Override
	public EPlan getInput() {
		return (EPlan)treeTableViewer.getInput();
	}

	@Override
	public ISelection getSelection() {
		return treeTableViewer.getSelection();
	}

	@Override
	public void refresh() {
		treeTableViewer.refresh();
		totalViewer.refresh();
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		treeTableViewer.addSelectionChangedListener(listener);
	}
	
	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		treeTableViewer.removeSelectionChangedListener(listener);
	}

	@Override
	public void setSelection(ISelection selection, boolean reveal) {
		treeTableViewer.setSelection(selection, reveal);
	}

	public void setActionBars(IActionBars actionBars) {
		treeTableViewer.setActionBars(actionBars);
	}

	public void collapseAll() {
		treeTableViewer.collapseAll();
	}

	public void expandAll() {
		treeTableViewer.expandAll();
	}
	
	public void setEditorModel(final PlanEditorModel model) {
		treeTableViewer.setEditorModel(model);
	}

	/**
	 * Returns the width of the viewer required to exactly display the given configuration
	 * @param configuration
	 * @return
	 */
	public static int getWidth(TreeTableColumnConfiguration<AbstractMergeColumn> configuration) {
		int width = MAGIC_TRIM_WIDTH;
		for (AbstractMergeColumn column : configuration.getColumns()) {
			width += configuration.getColumnWidth(column);
			width++; // one more for in-between column trim
		}
		return width;
	}
	
	/**
	 * Delegating method adding filters to the treeTableViewer
	 */
	public void addFilter(ViewerFilter filter) {
		treeTableViewer.addFilter(filter);
		totalViewer.refresh();
	}
	
	/**
	 * Delegating method for removing filters from the treeTableViewer
	 */
	public void removeFilter(ViewerFilter filter) {
		treeTableViewer.removeFilter(filter);
		totalViewer.refresh();
	}

	/**
	 * Delegating method for flattening the content
	 */
	public void toggleFlatten() {
		MergeTreeContentProvider provider = (MergeTreeContentProvider)treeTableViewer.getContentProvider();
		provider.toggleFlatten();
		treeTableViewer.refresh();
	}
	
	private static int unscheduledCompare(Object e1, Object e2) {
		if (DaysEditorPreferences.areUnscheduledAtEnd() && e1 instanceof EPlanChild && e2 instanceof EPlanChild) {
			EPlanChild child1 = (EPlanChild)e1;
			EPlanChild child2 = (EPlanChild)e2;
			Boolean scheduled1 = child1.getMember(TemporalMember.class).getScheduled();
			Boolean scheduled2 = child2.getMember(TemporalMember.class).getScheduled();
			if (scheduled1==null) scheduled1 = false;
			if (scheduled2==null) scheduled2 = false;
			if (scheduled1 && !scheduled2) {
				return -1;
			}
		    if (!scheduled1 && scheduled2) {
		    	return 1;
		    }
		}
		return 0;
	}
	
	protected class DayHeaderMouseListener implements MouseListener, MouseTrackListener {

		@Override
		public void mouseHover(MouseEvent e) {
			String note = eDay.getNotes();
			composite.setHeaderToolTipText(note);
		}
		
		@Override
		public void mouseUp(MouseEvent e) {
			StructuredSelection selection = new StructuredSelection(eDay);
			setSelection(selection, true);
		}
		
		@Override
		public void mouseEnter(MouseEvent e) { /* do nothing */ }
		@Override
		public void mouseExit(MouseEvent e) { 
			composite.setHeaderToolTipText(null);
		}
		@Override
		public void mouseDoubleClick(MouseEvent e) { /* do nothing */ }
		@Override
		public void mouseDown(MouseEvent e) { /* do nothing */ }
	}
	
	protected class NoteListener extends AdapterImpl {
		
		@Override
		public void notifyChanged(Notification msg) {
			if (PlanPackage.Literals.EDAY__NOTES == msg.getFeature()) {
				composite.setNoteVisible(eDay.getNotes() != null);
			}
		}
		
	}
	
	private static class DayDefaultComparator implements Comparator<Object> {

		public static DayDefaultComparator INSTANCE = new DayDefaultComparator();
		
		@Override
		public int compare(Object e1, Object e2) {
			int result = unscheduledCompare(e1, e2);
			if (result != 0) {
				return result;
			}
			return DefaultComparator.INSTANCE.compare(e1, e2);
		}

	}
	
	private static class DayDefaultViewerComparator extends ViewerComparator {

		public static DayDefaultViewerComparator INSTANCE = new DayDefaultViewerComparator();

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			return DayDefaultComparator.INSTANCE.compare(e1, e2);
		}
		
	}
	
	private static class DayColumnComparator extends TreeTableViewerComparator {

		public DayColumnComparator(ITreeTableColumn column, boolean reverse) {
			super(column, reverse);
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			int result = unscheduledCompare(e1, e2);
			if (result != 0) {
				return result;
			}
			return super.compare(viewer, e1, e2);
		}
		
	}
	
}
