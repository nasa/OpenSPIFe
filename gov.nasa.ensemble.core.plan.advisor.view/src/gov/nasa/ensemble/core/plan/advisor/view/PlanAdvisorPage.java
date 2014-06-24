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
package gov.nasa.ensemble.core.plan.advisor.view;

import gov.nasa.ensemble.common.EnsembleProperties;
import gov.nasa.ensemble.common.IdentifiableRegistry;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.ui.ActivatePreferencesAction;
import gov.nasa.ensemble.common.ui.EnsembleComposite;
import gov.nasa.ensemble.common.ui.WidgetUtils;
import gov.nasa.ensemble.common.ui.color.ColorConstants;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.advisor.AdvisorListener;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.PlanAdvisorMember;
import gov.nasa.ensemble.core.plan.advisor.Suggestion;
import gov.nasa.ensemble.core.plan.advisor.Violation;
import gov.nasa.ensemble.core.plan.advisor.ViolationKey;
import gov.nasa.ensemble.core.plan.advisor.ViolationTracker;
import gov.nasa.ensemble.core.plan.advisor.fixing.FixViolationsOperation;
import gov.nasa.ensemble.core.plan.advisor.fixing.SuggestedStartTime;
import gov.nasa.ensemble.core.plan.advisor.fixing.ViolationFixes;
import gov.nasa.ensemble.core.plan.advisor.markers.MarkerPlanAdvisor;
import gov.nasa.ensemble.core.plan.advisor.markers.MarkerViolation;
import gov.nasa.ensemble.core.plan.advisor.preferences.PlanAdvisorPreferences;
import gov.nasa.ensemble.core.plan.advisor.view.fixing.FixingViolationsWizard;
import gov.nasa.ensemble.core.plan.advisor.view.preferences.PlanAdvisorViewPreferencePage;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.TemporalNodeHyperlinkListener;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledFormText;

public class PlanAdvisorPage extends org.eclipse.ui.part.Page {

	private static final String PLAN_ADVISOR_TREE_CONTEXT_MENU_ID = "ensemble.plan.advisor.tree.contextmenu";

	private final IEditorPart editor;
	private final EPlan plan;
	private final PlanAdvisorMember planAdvisorMember;
    private final ISelectionProvider selectionProvider;
	private final IdentifiableRegistry<EPlanElement> identifiableRegistry = new IdentifiableRegistry<EPlanElement>();

	private SashForm sashForm;
	private PlanAdvisorTreeViewer viewer;
	private RefreshListener refreshListener;
	private ScrolledFormText scrolledFormText;
	
	private Action removeSelectedFixedViolationsAction;
	private Action removeAllFixedViolationsAction;
	private Action toggleFlattenedHeirarchyAction;
	private IAction fixViolationsAction;
	private IAction fixSelectedViolationsAction;
	
	private final FixViolationsListener fixViolationsListener = new FixViolationsListener();
	private final FixSelectedSelectionListener fixSelectionListener = new FixSelectedSelectionListener();
	
	private FormToolkit toolkit;


	/**
	 * The constructor.
	 */
	public PlanAdvisorPage(IEditorPart editor, PlanEditorModel model) {
		this.editor = editor;
		this.plan = model.getEPlan();
		this.planAdvisorMember = PlanAdvisorMember.get(plan);
		this.selectionProvider = editor.getSite().getSelectionProvider();
	}

	public EPlan getPlan() {
		return plan;
	}
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	public void createControl(Composite parent) {
		sashForm = new SashForm(parent, SWT.VERTICAL);
		
		viewer = new PlanAdvisorTreeViewer(sashForm);
		viewer.setContentProvider(new ViolationContentProvider());
		viewer.addDoubleClickListener(new ViolationClickListener(editor, planAdvisorMember));
		if (EnsembleProperties.getBooleanPropertyValue("advisor.singleclicktoselectculprits", true)) {
			viewer.addPostSelectionChangedListener(new ViolationClickListener(editor, planAdvisorMember));
		}
		viewer.addPostSelectionChangedListener(new FixTreeSelectionChangedListener());
		viewer.setInput(plan);
		createTreeContextMenu(viewer.getTree());
		
		Composite composite = new EnsembleComposite(sashForm, SWT.NONE);
		composite.setLayout(new FillLayout());
		composite.setBackground(ColorConstants.blue);
		
		scrolledFormText = new ScrolledFormText(composite, SWT.VERTICAL, true);
		scrolledFormText.setExpandHorizontal(true);
		scrolledFormText.setExpandVertical(true);
		scrolledFormText.setAlwaysShowScrollBars(true);
		FormText formText = scrolledFormText.getFormText();
		toolkit = new FormToolkit(parent.getDisplay());
		toolkit.adapt(formText);
		formText.addHyperlinkListener(new TemporalNodeHyperlinkListener(selectionProvider, plan, identifiableRegistry));
		
		ViolationDetailsTreeListener detailsListener = new ViolationDetailsTreeListener(viewer, scrolledFormText, identifiableRegistry);
		refreshListener = new RefreshListener(viewer, plan, detailsListener);
		
		sashForm.setWeights(new int[] {80, 20});
		
		setup();
	}

	private void setup() {
	    getSite().setSelectionProvider(selectionProvider);
		makeActions();
		contributeToActionBars();
		planAdvisorMember.addViolationsListener(fixViolationsListener);
		planAdvisorMember.addViolationsListener(refreshListener);
		fixViolationsListener.advisorsUpdated();
		getSite().getPage().addPostSelectionListener(fixSelectionListener);
    }

	@Override
	public void dispose() {
		getSite().getPage().removePostSelectionListener(fixSelectionListener);
		planAdvisorMember.removeViolationsListener(fixViolationsListener);
		planAdvisorMember.removeViolationsListener(refreshListener);
		getSite().setSelectionProvider(null);
		//TODO: dispose toolkit
		/*
		if(toolkit != null)
			toolkit.dispose();
			*/
		super.dispose();
	}

	/**
	 * Return the tree viewer's tree
	 */
	@Override
	public Control getControl() {
		return sashForm;
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	/*
	 * Utility methods
	 */
	
	private void createTreeContextMenu(Tree tree) {
		// Create menu manager.
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager mgr) {
				fillContextMenu(mgr);
			}
		});
		// Create menu.
		Menu menu = menuManager.createContextMenu(tree);
		tree.setMenu(menu);
		// Register menu for extension.
		getSite().registerContextMenu(PLAN_ADVISOR_TREE_CONTEXT_MENU_ID, menuManager, viewer);
	}

	private void fillContextMenu(IMenuManager menu) {
		ISelection selection = viewer.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			boolean someFixed = false;
			for (Object object : structuredSelection.toList()) {
				if (object instanceof ViolationTracker) {
					Violation violation = ((ViolationTracker)object).getViolation();
					if (!violation.isCurrentlyViolated()) {
						someFixed = true;
					} else {
						Set<Suggestion> suggestions = violation.getSuggestions();
						for (Suggestion suggestion : suggestions) {
							menu.add(new SuggestionAction(suggestion, TransactionUtils.getUndoContext(plan)));
						}
					}
				} else if (object instanceof PlanAdvisorGroup) {
					PlanAdvisorGroup group = (PlanAdvisorGroup)object;
					if (group.getKey() == ViolationKey.ELEMENTS) {
						PlanAdvisor advisor = null;
						Set<ViolationTracker> trackers = group.getViolationTrackers();
						for (ViolationTracker tracker : trackers) {
							PlanAdvisor violationAdviser = tracker.getViolation().getAdvisor();
							if (advisor == null) {
								advisor = violationAdviser;
							} else if (violationAdviser != advisor) {
								// the violations are not all from the same advisor
								advisor = null;
								break;
							}
						}
						if (advisor != null) {
							Set<Suggestion> suggestions = advisor.getSuggestions(trackers);
							for (Suggestion suggestion : suggestions) {
								menu.add(new SuggestionAction(suggestion, TransactionUtils.getUndoContext(plan)));
							}
						}
					}
				}
			}
			if (someFixed) {
				menu.add(removeSelectedFixedViolationsAction);
				menu.add(removeAllFixedViolationsAction);
			}
		}
		menu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void contributeToActionBars() {
		IActionBars bars = getSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(removeSelectedFixedViolationsAction);
		manager.add(removeAllFixedViolationsAction);
		manager.add(toggleFlattenedHeirarchyAction);
		manager.add(new Separator());
		manager.add(fixSelectedViolationsAction);
		manager.add(fixViolationsAction);
		manager.add(new Separator());
		manager.add(new ActivatePreferencesAction(getControl().getShell(), PlanAdvisorViewPreferencePage.ID));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(removeSelectedFixedViolationsAction);
		manager.add(removeAllFixedViolationsAction);
		manager.add(toggleFlattenedHeirarchyAction);
		manager.add(new Separator());
		manager.add(fixSelectedViolationsAction);
		manager.add(new Separator());
		manager.add(fixViolationsAction);
	}

	private void makeActions() {
		removeSelectedFixedViolationsAction = new RemoveSelectedFixedViolationsAction(viewer, PlanAdvisorMember.get(plan));
		removeAllFixedViolationsAction = new RemoveFixedViolationsAction(PlanAdvisorMember.get(plan));
		toggleFlattenedHeirarchyAction = new FlattenHierarchy(viewer);
		fixSelectedViolationsAction = buildFixSelectedAction();
		fixViolationsAction = buildFixViolationsAction();
	}
	
	/*
	 * Methods/classes relating to fixing violations
	 */
	
	/**
	 * Get the violation fixes from the plan advisor and apply them
	 */
	private void fixViolations(ISelection planElementSelection, ISelection violationSelection) {
		ViolationFixes combinedFixes = null;
		boolean useWizard = PlanAdvisorPreferences.isFixViolationsWizard();
		boolean fixAll = planElementSelection == null && violationSelection == null;
		ISelection remainingSelection = violationSelection;
		if (useWizard && hasNonMarkerViolation(planElementSelection)) {
			FixingViolationsWizard wizard = new FixingViolationsWizard(getPlan(), planElementSelection);
			WizardDialog dialog = new WizardDialog(getControl().getShell(), wizard);
			dialog.setBlockOnOpen(true);
			if (dialog.open() == Window.OK) {
				combinedFixes = wizard.getAcceptedFixes();
			}
		} else {
			remainingSelection = getCombinedSelection(planElementSelection, violationSelection);
		}
		if (!useWizard || fixAll || (remainingSelection != null && !remainingSelection.isEmpty())) {
			List<ViolationFixes> allFixes = new ArrayList<ViolationFixes>();
			if (combinedFixes != null) {
				allFixes.add(combinedFixes);
			}
			for (PlanAdvisor advisor : planAdvisorMember.getAdvisors()) {
				if (useWizard && !(advisor instanceof MarkerPlanAdvisor)) {
					// the wizard has already been used for other advisors
					continue;
				}
				try {
					ViolationFixes fixes = advisor.fixViolations(remainingSelection);
					if (fixes != null) {
						allFixes.add(fixes);
					}
				} catch (Exception e) {
					LogUtil.error("advisor failed to fixViolations: " + advisor.getName(), e);
				}
			}
			combinedFixes = ViolationFixes.combineFixes(allFixes);
		}
		if (combinedFixes != null) {
			executeFixViolationsOperation(planElementSelection, combinedFixes);
		}
	}
	
	private boolean hasNonMarkerViolation(ISelection selection) {
		Set<EPlanElement> selectedElements = PlanEditorUtil.emfFromSelection(selection);
		for (ViolationTracker tracker : planAdvisorMember.getViolationTrackers()) {
			Violation violation = tracker.getViolation();
			if (!violation.isCurrentlyViolated() || violation instanceof MarkerViolation) {
				continue;
			}
			List<? extends EPlanElement> violationElements = violation.getElements();
			if (violationElements.isEmpty() || selectedElements.isEmpty() || !Collections.disjoint(violationElements, selectedElements)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Create the operation to fix the violations and execute it
	 * @param fixes
	 */
	private void executeFixViolationsOperation(ISelection selection, ViolationFixes fixes) {
		String name = (selection != null ? "fix selected" : "fix violations");
		List<SuggestedStartTime> startTimes = fixes.getStartTimes();
		if (startTimes.isEmpty()) {
			name = "enlarge selection";
		}
		FixViolationsOperation operation = new FixViolationsOperation(name, fixes, selectionProvider);
		if (operation.hasAnyEffect()) {
			IUndoContext undoContext = TransactionUtils.getUndoContext(plan);
			WidgetUtils.execute(operation, undoContext, getControl(), getSite());
		}
	}
	
	/**
	 * Returns the action to fix the selected items
	 * @return the action to fix the selected items
	 */
	private IAction buildFixSelectedAction() {
		Action action = new Action("Fix selected", IAction.AS_PUSH_BUTTON) {
    		@Override
    		public void run() {
    			fixViolations(selectionProvider.getSelection(), viewer.getSelection());
    		}
    	};
    	action.setEnabled(false);
		return action;
	}
	
	private static ISelection getCombinedSelection(ISelection selection1, ISelection selection2) {
		if (selection2 == null || selection2.isEmpty() || !(selection2 instanceof IStructuredSelection)) {
			return selection1;
		}
		if (selection1 == null || selection1.isEmpty() || !(selection1 instanceof IStructuredSelection)) {
			return selection2;
		}
		Object[] elements1 = ((IStructuredSelection)selection1).toArray();
		Object[] elements2 = ((IStructuredSelection)selection2).toArray();
		Object[] combinedArray = ArrayUtils.addAll(elements1, elements2);
		return new StructuredSelection(combinedArray);
	}

	/**
	 * Returns the action to fix all violations in the plan
	 * @return the action to fix all violations in the plan
	 */
	private IAction buildFixViolationsAction() {
		Action action = new Action("Fix violations", IAction.AS_PUSH_BUTTON) {
    		@Override
    		public void run() {
    			fixViolations(null, null);
    		}	
    	};
    	action.setEnabled(false);
		return action;
	}

	private void updateFixSelectedEnablement(ISelection planElementsSelection, ISelection viewerSelection) {
		IAction fixSelectedAction = PlanAdvisorPage.this.fixSelectedViolationsAction;
		IAction fixViolationsAction = PlanAdvisorPage.this.fixViolationsAction;
		if ((fixSelectedAction != null) && (fixViolationsAction != null)) {
			boolean selectedToFix = false;
			if (fixViolationsAction.isEnabled()) {
				Set<Object> selectedObjects = getSelectedObjects(planElementsSelection, viewerSelection);
				if (!selectedObjects.isEmpty() ) {
					List<ViolationTracker> trackers = planAdvisorMember.getViolationTrackers();
					for (ViolationTracker tracker : trackers) {
						Violation violation = tracker.getViolation();
						if (violation.isCurrentlyViolated() && violation.isSelectedToFix(selectedObjects)) {
							selectedToFix = true;
							break;
						}
					}
				}
			}
			fixSelectedAction.setEnabled(selectedToFix);
		}
	}
	
	private static Set<Object> getSelectedObjects(ISelection planElementsSelection, ISelection viewerSelection) {
		Set<Object> selected = new HashSet<Object>();
		if (planElementsSelection instanceof IStructuredSelection) {
			selected.addAll(((IStructuredSelection)planElementsSelection).toList());
		}
		if (viewerSelection instanceof IStructuredSelection) {
			for (Object object : ((IStructuredSelection)viewerSelection).toList()) {
				if (object instanceof ViolationTracker) {
					Violation violation = ((ViolationTracker)object).getViolation();
					selected.add(violation);
				}
			}
		}
		return selected;
	}
	
	private final class FixSelectedSelectionListener implements ISelectionListener {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			updateFixSelectedEnablement(selection, getViewerSelection());
		}
	}
	
	public final class FixTreeSelectionChangedListener implements ISelectionChangedListener {
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			updateFixSelectedEnablement(selectionProvider.getSelection(), event.getSelection());
		}
	}

	private final class FixViolationsListener extends AdvisorListener {
		
		@Override
		public void advisorsUpdated() {
			boolean somethingToFix = false;
			boolean selectedToFix = false;
			Set<Object> selectedObjects = getSelectedObjects(selectionProvider.getSelection(), getViewerSelection());
			List<ViolationTracker> violationTrackers = planAdvisorMember.getViolationTrackers();
			for (ViolationTracker violationTracker : violationTrackers) {
				Violation violation = violationTracker.getViolation();
				try {
					if (violation.isCurrentlyViolated()
						&& !violation.isOutOfDate()
						&& !violation.isWaivedByInstance()
						&& !violation.isWaivedByRule()
						&& violation.isFixable()) {
						somethingToFix = true;
						if (selectedObjects.isEmpty() ) {
							break;
						}
						if (violation.isSelectedToFix(selectedObjects)) {
							selectedToFix = true;
							break;
						}
					}
				} catch (Exception e) {
					LogUtil.error("error while processing a violation to update violation fixing actions", e);
				}
			}
			fixViolationsAction.setEnabled(somethingToFix);
			fixSelectedViolationsAction.setEnabled(selectedToFix);
		}
	}
	
	private ISelection getViewerSelection() {
		Display display = WidgetUtils.getDisplay();
		final ISelection[] selection = new ISelection[1];
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				selection[0] = viewer.getSelection();
			}
		});
		return selection[0];
	}
	
	public PlanAdvisorTreeViewer getPlanAdvisorTreeViewer()	{
		return this.viewer;
	}
}
