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

import gov.nasa.arc.spife.ui.table.days.filter.AbstractDaysFilter;
import gov.nasa.arc.spife.ui.table.days.preferences.DaysEditorPreferencePage;
import gov.nasa.arc.spife.ui.table.days.preferences.DaysEditorPreferences;
import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.mission.MissionCalendarUtils;
import gov.nasa.ensemble.common.mission.MissionConstants;
import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.common.ui.FontUtils;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.editor.EnsembleSelectionProvider;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumn;
import gov.nasa.ensemble.common.ui.treetable.ITreeTableColumnConfigurationListener;
import gov.nasa.ensemble.common.ui.treetable.TreeTableColumnConfiguration;
import gov.nasa.ensemble.common.ui.treetable.TreeTableComposite;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.PlanTemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalPackage;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTotalComposite;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTreeContentProvider;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTreeLabelProvider;
import gov.nasa.ensemble.core.plan.editor.merge.MergeTreeViewer;
import gov.nasa.ensemble.core.plan.editor.merge.TableEditorUtils;
import gov.nasa.ensemble.core.plan.editor.merge.columns.AbstractMergeColumn;
import gov.nasa.ensemble.core.plan.editor.merge.decorator.MergeRowHighlightDecorator;
import gov.nasa.ensemble.core.plan.editor.merge.preferences.MergeEditorPreferences;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.SafeAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;

public class DaysComposite extends EnsembleComposite {
	
	private static final String DAY_VIEWER_KEY = "viewer";

	private final List<Day> days = new ArrayList<Day>();
	private final List<DayViewer> dayViewers = new ArrayList<DayViewer>();
	private final FocusListener focusListener = new DayFocusListener();
	private final HorizontalScrollListener horizontalScrollListener = new HorizontalScrollListener();
	private final ResizingFinishedListener resizingFinishedListener = new ResizingFinishedListener();
	private final EnsembleSelectionProvider selectionProvider;
	private final ScrollToSelectionListener scrollToSelectionListener = new ScrollToSelectionListener();
	private final DaysLayout layout;
	private final DaysEditor daysEditor;
	private final Adapter planDaysAdapter = new PlanDaysAdapter();
	private final ConfigurationListener configurationListener = new ConfigurationListener();
	/*
	 * These are populated by setInput
	 */
	private PlanEditorModel model;
	private DayColumnProvider dayColumnProvider;
	private TreeTableColumnConfiguration configuration;

	private Day activeDay = null;
	private boolean settingDay = false; // whether the day is being populated
	
	private IActionBars actionBars;
	
	// Overrides the preferences value; set by the updateDayViewerConfiguration method
	private Integer daysTreeFontSize = null;

	
	public DaysComposite(Composite parent, final DaysEditor daysEditor) {
		super(parent, SWT.H_SCROLL); // SWT.BORDER?
		this.daysEditor = daysEditor;
		this.selectionProvider = (EnsembleSelectionProvider) daysEditor.getSelectionProvider();
		this.layout = new DaysLayout();
		setLayout(layout);
		final ScrollBar hBar = getHorizontalBar();
		if (hBar != null) {
			hBar.setVisible(true);
			hBar.addListener (SWT.Selection, new Listener () {
				@Override
				public void handleEvent (Event e) {
					updateDaysChildren();
					daysEditor.daySelected(hBar.getSelection() / getChildWidth());
				}
			});
		}
		final ISelectionProvider provider = selectionProvider;
		if (provider != null) {
			provider.addSelectionChangedListener(scrollToSelectionListener);
			addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					provider.removeSelectionChangedListener(scrollToSelectionListener);
				}
			});
		}
	}
	
	private TreeTableColumnConfiguration<ITreeTableColumn> getTreeTableConfiguration(List<AbstractMergeColumn> columns, List<? extends AbstractMergeColumn> extraColumns) {
		return TableEditorUtils.getTableConfiguration(daysEditor.getId(), daysEditor.getEditorInput(), columns, extraColumns);
	}
	
	@Override
	public DaysLayout getLayout() {
		return layout;
	}
	
	public void setInput(IEditorInput input) {
		PlanEditorModel oldModel = model;
		model = PlanEditorModelRegistry.getPlanEditorModel(input);
		if (oldModel == model) {
			return;
		}
		if (oldModel != null) {
			TemporalMember member = oldModel.getEPlan().getMember(TemporalMember.class);
			member.eAdapters().remove(planDaysAdapter);
		}
		EPlan plan = model.getEPlan();
		if (plan != null) {
			TemporalMember member = plan.getMember(TemporalMember.class);
			member.eAdapters().add(planDaysAdapter);
		}
		synchronized (days) {
			days.clear();
		}
		dayColumnProvider = new DayColumnProvider(plan);
		List<AbstractMergeColumn> columns = new ArrayList<AbstractMergeColumn>();
		columns.add(MergeEditorPreferences.nameMergeColumn);
		columns.addAll(dayColumnProvider.getMentionedColumns());
		configuration = getTreeTableConfiguration(columns, dayColumnProvider.getColumns());
		configuration.addConfigurationListener(configurationListener);
		updateDays();
		for (DayViewer viewer : dayViewers) {
			if (viewer.getInput() != plan) {
				// recently constructed viewers may have the appropriate input already
				viewer.setInput(plan);
			}
		}
	}

	public List<Day> getDays() {
		return days;
	}

	/**
	 * Bring our listener out of the list and then add it back in,
	 * so that it will be the last one notified.  This is done so
	 * that by the time we are called, the new columns will be 
	 * constructed, and old columns will be disposed.
	 */
	private void resetConfigurationListenerToLastNotified() {
		configuration.removeConfigurationListener(configurationListener);
		configuration.addConfigurationListener(configurationListener);
	}

	/**
	 * Call to update the viewer control widths,
	 * viewer control count, viewer control positions,
	 * and virtual client area, when the configuration
	 * changes.
	 */
	private void configurationChanged() {
		// ensureChildCount may cause a viewer to be created,
		// which would affect the listener list of the configuration.  Since
		// this instance is in that list, we need to runLater to avoid a
		// concurrent modification exception.
		WidgetUtils.runLaterInDisplayThread(DaysComposite.this, new Runnable() {
			@Override
			public void run() {
				if (CommonUtils.isOSLinux()) {
					Control[] children = getChildren();
					if (children.length != 0) {
						Control control = children[0];
						control.pack();
					}
				}
				layout();
				ensureChildCount();
				updateDaysChildren();
			}
		});
	}
	
	/**
	 * Update the days displayed according to the days in the plan
	 * 1. if the plan contains a day that is not in our days, we add it in the appropriate place
	 * 2. if the plan does not contain a day that is in our days, we remove it
	 * 3. finally, we update the layout and widget count
	 */
	private void updateDays() {
		EPlan plan = model.getEPlan();
		List<Day> newDays = computeDaysInPlan(plan);
		if (newDays.isEmpty()) {
			throw new IllegalStateException("there should always be at least one day");
		}
		synchronized (days) {
			if (newDays.isEmpty()) {
				days.clear();
			} else {
				// #2 above
				Day firstDay = newDays.get(0);
				Day lastDay = newDays.get(newDays.size() - 1);
				int i = 0;
				int indexOfLastItemToRemoveFromTheBeginning = -1;
				int indexOfFirstItemToRemoveFromTheEnd = -1;
				for (Day day : days) {
					Date date = day.getDate();
					if (date.before(firstDay.getDate())) {
						indexOfLastItemToRemoveFromTheBeginning = i;
					}
					if (date.after(lastDay.getDate())) {
						indexOfFirstItemToRemoveFromTheEnd = i;
						break;
					}
					i++;
				}
				if (indexOfFirstItemToRemoveFromTheEnd != -1) {
					days.subList(indexOfFirstItemToRemoveFromTheEnd, days.size()).clear();
				}
				if (indexOfLastItemToRemoveFromTheBeginning != -1) {
					days.subList(0, indexOfLastItemToRemoveFromTheBeginning + 1).clear();
				}
			}
			// #1 above
			if (days.isEmpty()) {
				days.addAll(newDays);
			} else {
				// This section takes advantage of the fact that days
				// can be ordered temporally to avoid doing an expensive comparison,
				// because the number of days may get very large (hundreds)
				// @see Day.compareTo 
				Day firstExistingDay = days.get(0);
				Day lastExistingDay = days.get(days.size() - 1);
				int lastEarlierIndex = -1;
				int firstLaterIndex = -1;
				int index = 0;
				for (Day day : newDays) {
					if (day.compareTo(firstExistingDay) < 0) {
						lastEarlierIndex = index;
					} else if (day.compareTo(lastExistingDay) > 0) {
						firstLaterIndex = index;
						break;
					}
					index++;
				}
				if (lastEarlierIndex != -1) {
					List<Day> earlierDaysToInsert = newDays.subList(0, lastEarlierIndex + 1);
					days.addAll(0, earlierDaysToInsert);
				}
				if (firstLaterIndex != -1) {
					List<Day> laterDaysToInsert = newDays.subList(firstLaterIndex, newDays.size());
					days.addAll(laterDaysToInsert);
				}
			}
			// #3 above
			getLayout().setDayCount(days.size());
			if (days.isEmpty()) {
				activeDay = null;
			} else if ((activeDay != null) && !days.contains(activeDay)) {
				activeDay = days.get(0);
			}
			layout();
			ensureChildCount();
		}
	}

	/**
	 * Create a list of Day objects for the plan's temporal extent.
	 * 
	 * @param plan
	 * @return the days, in temporal order
	 */
	private List<Day> computeDaysInPlan(EPlan plan) {
		PlanTemporalMember planTemporalMember = plan.getMember(PlanTemporalMember.class);
		Calendar missionCalendar = MissionConstants.getInstance().getMissionCalendar();
		missionCalendar.setTime(planTemporalMember.getStartTime());
		Date date = MissionCalendarUtils.floorDay(missionCalendar);
		Calendar endCalendar = MissionConstants.getInstance().getMissionCalendar();
		endCalendar.setTime(planTemporalMember.getEndTime());
		Date endTime = MissionCalendarUtils.ceilingDay(endCalendar);
		List<Day> days = new ArrayList<Day>();
		while (date.before(endTime)) {
			days.add(new Day(date));
			missionCalendar.add(Calendar.DAY_OF_YEAR, 1);
			date = missionCalendar.getTime();
		}
		return days;
	}

	protected void refreshDayViewers() {
		for (DayViewer viewer : dayViewers) {
			viewer.getTreeTableViewer().refresh();
		}
	}
	
	@Override
	public boolean setFocus() {
		if (activeDay != null) {
			for (DayViewer viewer : dayViewers) {
				Day input = viewer.getDay();
				if (input == activeDay) {
					DayComposite control = viewer.getControl();
					return control.setFocus();
				}
			}
		}
		return forceFocus();
	}
	
	/**
	 * Set the inputs for the day viewers.
	 * Position the day viewer controls according to the scroll.
	 * Focus the active day viewer if visible.
	 */
	private void updateDaysChildren() {
		if (settingDay) {
			return;
		}
		synchronized (days) {
			int selection = getHorizontalBar().getSelection();
			int firstVisibleDay = findFirstVisibleDay(selection);
			int firstDisplayedControl = findFirstDisplayedControl();
			if (firstDisplayedControl == dayViewers.size()) {
				firstDisplayedControl = findControlWithLatestInput();
			}
			
			int horizontalPosition = getHorizontalPosition(firstVisibleDay);
			horizontalPosition -= selection;
			int i = firstDisplayedControl;
			if (i < 0) return;	//if we havent defined the viewers none is displayed
			
			int day = firstVisibleDay;
			boolean focused = false;
			List<Control> tabOrder = new ArrayList<Control>();
			do {
				DayViewer viewer = dayViewers.get(i);
				Day theDay;
				if (day < days.size()) {
					theDay = days.get(day);
				} else {
					theDay = days.get(days.size() - dayViewers.size());
				}
				Control control = viewer.getControl();
				control.setLocation(horizontalPosition, control.getLocation().y);
				if (viewer.getDay() != theDay) {
					try {
						settingDay = true;
						viewer.setDay(theDay, this.selectionProvider);
					} finally {
						settingDay = false;
					}
				}
				if (theDay == activeDay) {
					control.setFocus();
					focused = true;
				}
				if (horizontalPosition < getClientArea().width) {
					tabOrder.add(control);
				}
				horizontalPosition += control.getSize().x;
				day++;
				i = (i + 1) % dayViewers.size();
			} while (i != firstDisplayedControl);
			if (!focused) {
				// focus us to remove focus from any widget that has it
				forceFocus();
			}
			setTabList(tabOrder.toArray(new Control[tabOrder.size()]));
		}
	}

	/**
	 * Return the index of the day that is at the left of the composite
	 * when the scrollbar is at the position 'selection'
	 * 
	 * @param selection
	 * @return
	 */
	private int findFirstVisibleDay(int selection) {
		int childWidth = getChildWidth();
		return selection/childWidth;
	}

	/**
	 * Return the position that the indexed day will start at.  Does
	 * not adjust for scroll position.  
	 * 
	 * @param day
	 * @return
	 */
	private int getHorizontalPosition(int day) {
		int childWidth = getChildWidth();
		return day * childWidth;
	}
	
	public void scrollToDay(int dayIndex) {
		if (dayIndex < days.size()) {
			int selectionPosition = getHorizontalPosition(dayIndex);
			getHorizontalBar().setSelection(selectionPosition);
			updateDaysChildren();
		}
	}

	/**
	 * Makes sure that the number of children available is appropriate for
	 * the width that is currently being displayed.
	 * Will create new children if necessary.
	 * Will destroy extra children if they are not needed.
	 */
	public void ensureChildCount() {
		synchronized (days) {
			int clientWidth = this.getClientArea().width;
			int visibleChildCount = 1;
			if (clientWidth != 0) { // when clientWidth is zero, create 1 child
				int childWidth = getChildWidth();
				visibleChildCount = (int)Math.ceil(clientWidth / (float)childWidth) + 1;
			}
			int howManyChildren = Math.min(visibleChildCount, days.size());
			int existingChildCount = getChildren().length;
			if (howManyChildren == existingChildCount) {
				return;
			}
			if (howManyChildren < existingChildCount) {
				disposeExtraChildren(existingChildCount - howManyChildren);
			} else {
				createExtraChildren(howManyChildren - existingChildCount);
			}
			getHorizontalBar().setPageIncrement(getChildWidth());
			layout();
		}
	}

	/**
	 * Returns the width of one child
	 * @return
	 */
	private int getChildWidth() {
		DayViewer representative = dayViewers.get(0);
		return representative.getControl().getSize().x;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		boolean scroll = (getBounds().width == 0);
		super.setBounds(x, y, width, height);
		ensureChildCount();
		updateDaysChildren();
		if (scroll) {
			ISelection selection = daysEditor.getSelectionProvider().getSelection();
			if (selection != null) {
				scrollToSelectionListener.scrollToSelection(selection);
			}
		}
	}

	public DayViewer getActiveViewer() {
		for (DayViewer viewer : dayViewers) {
			Day day = viewer.getDay();
			if (day == activeDay) {
				return viewer;
			}
		}
		return null;
	}

	public void setActionBars(IActionBars bars) {
		this.actionBars = bars;
		for (DayViewer viewer : dayViewers) {
			viewer.setActionBars(bars);
		}
	}
	
	
	/**
	 * Create new children widgets
	 * 
	 * @param count how many new children to create
	 */
	private void createExtraChildren(int count) {
		int firstDisplayedControl = 0;
		if (!dayViewers.isEmpty()) {
			firstDisplayedControl = findFirstDisplayedControl();
			if (firstDisplayedControl == dayViewers.size()) {
				firstDisplayedControl = findLeftmostControl();
			}
		}
		for (int i = 0 ; i < count ; i++) {
			DayViewer dayViewer = createDayViewer();
			dayViewer.setInput(model.getEPlan());
			dayViewer.addFilter(daysEditor.getCurrentDaysFilter());
			dayViewers.add(firstDisplayedControl, dayViewer);
		}
		updateDaysChildren();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		for (DayViewer dayViewer : dayViewers) {
			destroyDayViewer(dayViewer);
		}
		dayViewers.clear();
		days.clear();
		if (model != null) {
			TemporalMember member = model.getEPlan().getMember(TemporalMember.class);
			member.eAdapters().remove(planDaysAdapter);
		}
	}

	/**
	 * Destroy unneeded children widgets
	 * 
	 * @param count
	 */
	private void disposeExtraChildren(int count) {
		int firstDisplayedControl = DaysEditorUtils.mod(findFirstDisplayedControl() - 1, dayViewers.size());
		for (int i = 0 ; i < count ; i++) {
			if (firstDisplayedControl == dayViewers.size()) {
				firstDisplayedControl = 0;
			}
			DayViewer oldChild = dayViewers.remove(firstDisplayedControl);
			destroyDayViewer(oldChild);
		}
	}

	private int findFirstDisplayedControl() {
		// look for the control showing the first visible day, if present
		int selection = getHorizontalBar().getSelection();
		int firstVisibleDay = findFirstVisibleDay(selection);
		Day day = days.get(firstVisibleDay);
		return findControlIndexForDay(day);
	}

	/**
	 * Create a new day viewer
	 * @return
	 */
	private DayViewer createDayViewer() {
		DayViewer viewer = new DayViewer(this, configuration, dayColumnProvider, daysEditor.getSite());
		MergeRowHighlightDecorator decorator = (MergeRowHighlightDecorator) daysEditor.getAdapter(MergeRowHighlightDecorator.class);
		viewer.setRowHighlightDecorator(decorator);
		resetConfigurationListenerToLastNotified();
		viewer.setEditorModel(model);
		DayComposite dayComposite = viewer.getControl();
		addScrollListeners(dayComposite);
		if (CommonUtils.isOSLinux() || CommonUtils.isOSWindows()) {
			addResizingFinishedListeners(dayComposite);
		}
		if (actionBars != null) {
			viewer.setActionBars(actionBars);
		}
		Font font = getDaysTreeFont(false);
		TreeTableComposite treeTableComposite = dayComposite.getTreeTableComposite();
		Tree tree = treeTableComposite.getTree();
		tree.setFont(font);
		tree.setData(DAY_VIEWER_KEY, viewer);
		tree.addFocusListener(focusListener);
		MenuManager menuManager = WidgetUtils.createContextMenu(tree, new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager mgr) {
				daysEditor.fillContextMenu(mgr);
			}
		});
		MergeTotalComposite totalComposite = (MergeTotalComposite)viewer.getTotalViewer().getControl();
		totalComposite.getTable().setFont(font);
		// allow extensions to the context menu but not those based on the editor input
		daysEditor.getEditorSite().registerContextMenu(menuManager, selectionProvider, false);
		selectionProvider.attachSelectionProvider(viewer);
		
		MergeTreeViewer treeViewer = viewer.getTreeTableViewer();
		IBaseLabelProvider labelProvider = treeViewer.getLabelProvider();
		if (labelProvider instanceof MergeTreeLabelProvider) {
			MergeTreeLabelProvider mergeTreeLabelProvider = (MergeTreeLabelProvider)labelProvider;
			mergeTreeLabelProvider.setLabelFontSize(getDaysTreeFontSize());
			mergeTreeLabelProvider.updateFonts();
		}
		return viewer;
	}

	/**
	 * Recursively add the resizing finished listener to control and its children
	 * @param control
	 */
	private void addResizingFinishedListeners(Control control) {
		control.addListener(SWT.MouseExit, resizingFinishedListener);
		control.addListener(SWT.MouseEnter, resizingFinishedListener);
		control.addListener(SWT.MouseDown, resizingFinishedListener);
		control.addListener(SWT.MouseUp, resizingFinishedListener);
		control.addListener(SWT.MouseMove, resizingFinishedListener);
		if (control instanceof Composite) {
			for (Control child : ((Composite)control).getChildren()) {
				addResizingFinishedListeners(child);
			}
		}
	}

	/**
	 * Recursively add the horizontal scroll listener to this control and children
	 * @param control
	 */
	private void addScrollListeners(Control control) {
		control.addListener(SWT.MouseHorizontalWheel, horizontalScrollListener);
		if (control instanceof Composite) {
			for (Control child : ((Composite)control).getChildren()) {
				addScrollListeners(child);
			}
		}
	}

	/**
	 * Destroy this particular day viewer
	 * @param viewer
	 */
	private void destroyDayViewer(DayViewer viewer) {
		DayComposite dayComposite = viewer.getControl();
		TreeTableComposite treeTableComposite = dayComposite.getTreeTableComposite();
		Tree tree = treeTableComposite.getTree();
		if (!tree.isDisposed()) {
			tree.removeFocusListener(focusListener);
		}
		selectionProvider.detachSelectionProvider(viewer);
		dayComposite.dispose();
	}

	private int findControlWithLatestInput() {
		boolean foundNull = false;
		Date max_input = new Date(Long.MIN_VALUE);
		int max_index = -1;
		int i = 0;
		for (DayViewer viewer : dayViewers) {
			Day input = viewer.getDay();
			if (input == null) {
				foundNull = true;
			} else {
				Date date = input.getDate();
				if (date.after(max_input)) {
					max_index = i;
					max_input = date;
				}
			}
			i++;
		}
		if (foundNull) {
			return (max_index + 1) % dayViewers.size(); 
		}
		return max_index;
	}

	private int findControlIndexForDay(Day day) {
		int index = 0;
		for (DayViewer viewer : dayViewers) {
			Day viewerDay = viewer.getDay();
			if ((viewerDay != null) && (viewerDay == day)) {
				return index;
			}
			index++;
		}
		return index;
	}

	private int findLeftmostControl() {
		int min_x = Integer.MAX_VALUE;
		int leftmostControlIndex = 0;
		int i = 0;
		for (DayViewer viewer : dayViewers) {
			int x = viewer.getControl().getBounds().x;
			if (x < min_x) {
				min_x = x;
				leftmostControlIndex = i;
			}
			i++;
		}
		return leftmostControlIndex;
	}
	
	/*package*/ void setupPreferenceListener() {
		final IPropertyChangeListener preferenceListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(DaysEditorPreferencePage.P_DAYS_EDITOR_FONT_SIZE)) {
					daysTreeFontSize = null;
					updateDayViewerFonts();
				} else if (event.getProperty().equals(DaysEditorPreferencePage.P_DAYS_EDITOR_EDITOR_UNSCHEDULED_AT_END)) {
					updateDayViewerOrder();
				}
			}
		};
		this.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				DaysPlugin.getDefault().getPreferenceStore().removePropertyChangeListener(preferenceListener);
			}
		});
		DaysPlugin.getDefault().getPreferenceStore().addPropertyChangeListener(preferenceListener);
	}

	private Font daysTreeFont = null;
	private Font getDaysTreeFont(boolean update) {
		if (daysTreeFont != null && !update) {
			return daysTreeFont;
		}
		FontData systemFontData = Display.getDefault().getSystemFont().getFontData()[0];
		int height = getDaysTreeFontSize();
		daysTreeFont = FontUtils.getStyledFont(null, systemFontData.getName(), height, systemFontData.getStyle()); 
		return daysTreeFont;
	}

	/*package*/ int getDaysTreeFontSize() {
		if (daysTreeFontSize != null) {
			return daysTreeFontSize;
		}
		return DaysEditorPreferences.getFontSize();
	}
	
	private void updateDayViewerFonts() {
		Font font = getDaysTreeFont(true);
		for (DayViewer dayViewer : dayViewers) {
			MergeTreeViewer treeViewer = dayViewer.getTreeTableViewer();
			treeViewer.getTree().setFont(font);
			MergeTotalComposite totalComposite = (MergeTotalComposite)dayViewer.getTotalViewer().getControl();
			totalComposite.getTable().setFont(font);
			IBaseLabelProvider labelProvider = treeViewer.getLabelProvider();
			if (labelProvider instanceof MergeTreeLabelProvider) {
				MergeTreeLabelProvider mergeTreeLabelProvider = (MergeTreeLabelProvider)labelProvider;
				mergeTreeLabelProvider.setLabelFontSize(getDaysTreeFontSize());
				mergeTreeLabelProvider.updateFonts();
			}
			treeViewer.refresh(true);
		}
	}
	
	private void updateDayViewerOrder() {
		for (DayViewer dayViewer : dayViewers) {
			dayViewer.getTreeTableViewer().refresh(false);
		}
	}
	
	/*package*/ void updateDayViewerConfiguration(int fontSize, int nameColumnWidth, int restColumnWidth) {
		daysTreeFontSize = fontSize;
		updateDayViewerFonts();
		boolean first = true;
		for (ITreeTableColumn column : (List<? extends ITreeTableColumn>)configuration.getColumns()) {
			if (first) {
				configuration.resizeColumn(column, nameColumnWidth);
			} else {
				configuration.resizeColumn(column, restColumnWidth);
			}
			first = false;
		}
		if (CommonUtils.isOSLinux() || CommonUtils.isOSWindows()) {
			configurationListener.checkResizedLinuxWindows();
		}
	}

	/*
	 * Delegate methods
	 */
	
	/**
	 * Adds the DaysFilter to every DayViewer in the Days Editor
	 * @param filter
	 */
	public void addDaysFilter(AbstractDaysFilter filter) {
		for (DayViewer viewer : dayViewers) {
			viewer.addFilter(filter);
		}
	}

	/**
	 * Removes a DaysFilter from every DayViewer in the Days Editor
	 * @param filter
	 */
	public void removeDaysFilter(AbstractDaysFilter filter) {
		for (DayViewer viewer : dayViewers) {
			viewer.removeFilter(filter);
		}
	}

	/**
	 * Toggle the flatten state for every DayViewer in the Days Editor
	 */
	public void toggleFlatten() {
		for (DayViewer viewer : dayViewers) {
			viewer.toggleFlatten();
		}
	}

	/*
	 * Inner classes
	 */
	
	private final class PlanDaysAdapter extends SafeAdapter {
		private Calendar calendar = MissionConstants.getInstance().getMissionCalendar();

		@Override
		protected void handleNotify(Notification notification) {
			if (notification.getEventType() == Notification.SET) {
				Object feature = notification.getFeature();
				if ((feature == TemporalPackage.Literals.TEMPORAL_MEMBER__START_TIME)
					|| (feature == TemporalPackage.Literals.TEMPORAL_MEMBER__END_TIME)) {
					Object oldValue = notification.getOldValue();
					if (oldValue == null) {
						updateDaysInDisplayThread();
					}
					Object newValue = notification.getNewValue();
					if (newValue != null) {
						Date oldFloor;
						Date newFloor;
						synchronized (calendar) {
							oldFloor = floorValue(oldValue);
							newFloor = floorValue(newValue);
						}
						if (!oldFloor.equals(newFloor)) {
							updateDaysInDisplayThread();
						}
					}
				}
			}
		}

		private void updateDaysInDisplayThread() {
			WidgetUtils.runInDisplayThread(DaysComposite.this, new Runnable() {
				@Override
				public void run() {
					updateDays();
					updateDaysChildren();
				}
			});
		}

		private Date floorValue(Object value) {
			Date date = (Date)value;
			calendar.setTime(date);
			MissionCalendarUtils.floorDay(calendar);
			return calendar.getTime();
		}
	}
	
	private final class DayFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			activeDay = getDay(e);
		}

		@Override
		public void focusLost(FocusEvent e) {
			// Don't clear the active day or it will be lost
			// when switching to another editor.
		}

		private Day getDay(FocusEvent e) {
			Tree tree = (Tree)e.widget;
			DayViewer viewer = (DayViewer)tree.getData(DAY_VIEWER_KEY);
			if (viewer != null) {
				return viewer.getDay();
			}
			return null;
		}
	}
	
	private final class HorizontalScrollListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			ScrollBar hBar = getHorizontalBar();
			int selection = hBar.getSelection();
			selection -= 5 * event.count;
			hBar.setSelection(selection);
			updateDaysChildren();
		}
	}

	private final class ResizingFinishedListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			configurationListener.checkResizedLinuxWindows();
		}
	}

	public boolean isFlattened() {
		DayViewer dayViewer = this.dayViewers.get(0);
		if (dayViewer == null) {
			return false;
		} 
		MergeTreeContentProvider provider = (MergeTreeContentProvider) dayViewer.getTreeTableViewer().getContentProvider();
		return provider.isFlattened();
	}

	private final class ConfigurationListener implements ITreeTableColumnConfigurationListener<ITreeTableColumn> {
		
		private boolean columnResized = false;

		@Override
		public void columnsChanged(List<? extends ITreeTableColumn> oldColumns, List<? extends ITreeTableColumn> newColumns) {
			configurationChanged();
		}

		/**
		 * Check for resizes that have occurred on linux or windows.
		 * 
		 * @return
		 */
		public void checkResizedLinuxWindows() {
			boolean resized = columnResized;
			columnResized = false;
			if (resized) {
				configurationChanged();
			}
		}

		@Override
		public void columnResized(ITreeTableColumn mergeColumn, int width) {
			columnResized = true;
			if (CommonUtils.isOSMac()) {
				configurationChanged();
			}
		}

		@Override
		public void sortChanged(ITreeTableColumn column, int direction) {
			// ignore
		}
		
	}

	public class ScrollToSelectionListener implements ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			if (settingDay) {
				return;
			}
			ISelection selection = event.getSelection();
			scrollToSelection(selection);
		}

		/**
		 * Scroll to the selected elements, if any.
		 * @param selection
		 */
		public void scrollToSelection(ISelection selection) {
			if (!selection.isEmpty()) {
				Set<EPlanElement> elements = PlanEditorUtil.emfFromSelection(selection);
				if (!elements.isEmpty()) {
					Date earliest = null;
					Date latest = null;
					for (EPlanElement element : elements) {
						TemporalMember member = element.getMember(TemporalMember.class);
						TemporalExtent extent = member.getExtent();
						if (extent != null) {
							if (earliest == null) {
								earliest = extent.getStart(); 
							} else {
								earliest = DateUtils.earliest(earliest, extent.getStart());
							}
							if (latest == null) {
								latest = extent.getEnd(); 
							} else {
								latest = DateUtils.latest(latest, extent.getEnd());
							}
						}
					}
					if ((earliest != null) && (latest != null)) {
						if (!earliest.equals(latest)) {
							latest = new Date(latest.getTime() - 1);
						}
						scrollTo(earliest, latest);
					}
				}
			}
		}

		/**
		 * If a visible day viewer intersects earliest and latest, do nothing.
		 * If the visible day viewers are earlier in the plan, scroll to the
		 * first day viewer showing the earliest date.
		 * If the visible day viewers are later in the plan, scroll to the
		 * first day viewer showing the latest date. 
		 * 
		 * @param earliest
		 * @param latest
		 */
		private void scrollTo(Date earliest, Date latest) {
			ScrollBar hBar = getHorizontalBar();
			int leftVisible = hBar.getSelection();
			int rightVisible = leftVisible + getClientArea().width;
			if (leftVisible == rightVisible) {
				return;
			}
			int earliestDay = findDay(earliest);
			int latestDay = findDay(latest);
			if ((earliestDay == -1) || (latestDay == -1)) {
				return;
			}
			int earliestPosition = getHorizontalPosition(earliestDay);
			int latestPosition = getHorizontalPosition(latestDay) + getChildWidth();
			int selection;
			if (rightVisible < earliestPosition) { // need to scroll to the right.
				selection = earliestPosition;
				activeDay = days.get(earliestDay);
			} else if (leftVisible > latestPosition) { // need to scroll to the left.
				selection = latestPosition - getChildWidth();
				activeDay = days.get(latestDay);
			} else { // it is visible someplace
				List<Day> selectionDays = days.subList(earliestDay, latestDay + 1);
				if (!selectionDays.contains(activeDay)) {
					// if the active day doesn't yet contain the selection,
					// find the visible viewer that contains it and make it active
					if (earliestPosition > leftVisible) {
						activeDay = days.get(earliestDay);
					} else {
						activeDay = days.get(latestDay);
					}
					setFocus();
				}
				return;
			}
			// adjust the day viewer selection such that it is in the middle of the editor
			selection = selection - getClientArea().width / 2 + getChildWidth() / 2;
			hBar.setSelection(selection);
			updateDaysChildren();
		}

		private int findDay(Date date) {
			synchronized (days) {
				int left = 0;
				int right = days.size();
				while (right > left) {
					int midpoint = (int)Math.floor((right + left) / 2);
					Day day = days.get(midpoint);
					TemporalExtent extent = day.getExtent();
					if (date.before(extent.getStart())) {
						right = midpoint;
						continue;
					}
					if (date.after(extent.getEnd())) {
						left = midpoint;
						continue;
					}
					return midpoint;
				}
				return -1;
			}
		}

	}

}
