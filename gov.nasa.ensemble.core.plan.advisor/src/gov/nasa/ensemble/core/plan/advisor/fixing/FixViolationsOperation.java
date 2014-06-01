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
package gov.nasa.ensemble.core.plan.advisor.fixing;

import gov.nasa.ensemble.common.time.DateUtils;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.operations.AddOperation;
import gov.nasa.ensemble.common.ui.operations.DeleteOperation;
import gov.nasa.ensemble.common.ui.operations.IDisplayOperation;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.plan.editor.PlanAddOperation;
import gov.nasa.ensemble.core.plan.editor.PlanDeleteOperation;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.core.plan.editor.util.PlanEditorUtil;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchSite;

public class FixViolationsOperation extends AbstractTransactionUndoableOperation implements IDisplayOperation {
	private final ViolationFixes violationFixes;
//	private final List<Boolean> oldScheduledStates = new Vector<Boolean>();
	private final List<TemporalExtent> oldExtents = new Vector<TemporalExtent>();
	private final ISelectionProvider selectionProvider;
	private final ISelection oldSelection;
	private final ISelection newSelection;
	private final Logger trace = Logger.getLogger(FixViolationsOperation.class);
	private final List<EActivity> adds = new Vector<EActivity>();
	
	public FixViolationsOperation(String name, ViolationFixes violationFixes, ISelectionProvider selectionProvider) {
		super(name);
		this.violationFixes = violationFixes;
		this.selectionProvider = selectionProvider;
		for (SuggestedStartTime suggestion : violationFixes.getStartTimes()) {
			EPlanElement node = suggestion.node;
			TemporalExtent extent = node.getMember(TemporalMember.class).getExtent();
			oldExtents.add(extent);
			if (extent == null && node instanceof EActivity) {
			    adds.add((EActivity) node);
			}
		}
		oldSelection = selectionProvider.getSelection();
		newSelection = createNewSelection(violationFixes);
	}

	private void doAddOperation(PlanEditorModel model, final EPlanElement parent, final List<EActivity> activities) {
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanTransferable transferable = new PlanTransferable();
		transferable.setPlanElements(activities);
		IStructureLocation location = modifier.getInsertionLocation(transferable, new StructuredSelection(parent), InsertionSemantics.ON);
		AddOperation operation = new PlanAddOperation(transferable, modifier, location);
		//IUndoContext undoContext = model.getUndoContext();
		//operation.addContext(undoContext);
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			@SuppressWarnings("unused")
			IStatus status = history.execute(operation, null, null);
		} catch (Exception e) {
			trace.error("FixViolationsOperation.doAddOperation:operation", e);
		}
	}

	private void doDeleteOperation(PlanEditorModel model, final List<EActivity> activities) {
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanTransferable transferable = new PlanTransferable();
		transferable.setPlanElements(activities);
		DeleteOperation operation = new PlanDeleteOperation(transferable, modifier);
		//IUndoContext undoContext = model.getUndoContext();
		//operation.addContext(undoContext);
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			@SuppressWarnings("unused")
			IStatus status = history.execute(operation, null, null);
		} catch (Exception e) {
			trace.error("FixViolationsOperation.doDeleteOperation:operation", e);
		}
	}

	private void addActivitiesToPlan(final List<EActivity> activities) {
		Display.getDefault().syncExec(
				new Runnable() {  @Override
				public void run()
				{
					PlanEditorModel model = PlanEditorModelRegistry.getCurrent();
					EPlan plan = model.getEPlan();
					doAddOperation(model, plan, activities);
				}
				});
	}

	private void removeActivitiesFromPlan(final List<EActivity> activities) {
		Display.getDefault().syncExec(
				new Runnable() {  @Override
				public void run()
				{
					PlanEditorModel model = PlanEditorModelRegistry.getCurrent();
					doDeleteOperation(model, activities);
				}
				});
	}


	public boolean hasAnyEffect() {
		for (SuggestedStartTime suggestion : violationFixes.getStartTimes()) {
			EPlanElement node = suggestion.node;
			TemporalExtent extent = node.getMember(TemporalMember.class).getExtent();
			if (extent == null) {
				return true;
			}
			Date currentStartTime = extent.getStart();
			if (currentStartTime.before(suggestion.earliest)) {
				return true;
			} else if (currentStartTime.after(suggestion.latest)) {
				return true;
			}
		}
		if (!violationFixes.getOpposingNodes().isEmpty()) {
			return true;
		}
		return false;
	}
	
	private static void sortByEndTime(List<EPlanElement> ePlanElements, final boolean sortDescendingOrder) {
		Collections.sort(ePlanElements, new Comparator<EPlanElement>() {
			@Override
			public int compare(EPlanElement e1, EPlanElement e2) {
				TemporalMember m1 = e1.getMember(TemporalMember.class);
				TemporalMember m2 = e2.getMember(TemporalMember.class);
				int compareTo;
				if (m1.getEndTime() == null && m2.getEndTime() == null)
				    compareTo = 0;
				else if (m2.getEndTime() == null)
				    compareTo = -1;
				else if (m1.getEndTime() == null)
				    compareTo = +1;
				else
				    compareTo = m1.getEndTime().compareTo(m2.getEndTime());
				if(sortDescendingOrder) {
					compareTo *= -1;
				}
				return compareTo;
			}			
		});
	}	
	
	private static void sortByIdealTime(List<SuggestedStartTime> startTimes, final boolean sortDescendingOrder) {
		Collections.sort(startTimes, new Comparator<SuggestedStartTime>() {
			@Override
			public int compare(SuggestedStartTime e1, SuggestedStartTime e2) {
			    
				Date m1 = e1.ideal;
				Date m2 = e2.ideal;
				int compareTo;
				if (m1 == null && m2 == null)
				    compareTo = 0;
				else if (m2 == null)
				    compareTo = -1;
				else if (m1 == null)
				    compareTo = +1;
				else
				    compareTo = m1.compareTo(m2);
				if(sortDescendingOrder) {
					compareTo *= -1;
				}
				return compareTo;
			}			
		});
	}	
	
	private ISelection createNewSelection(ViolationFixes violationFixes) {
		List<EPlanElement> list = new ArrayList<EPlanElement>(violationFixes.getOpposingNodes());
		if (oldSelection instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) oldSelection;
			list.addAll(PlanEditorUtil.emfFromSelection(selection));
		}
		if (list.isEmpty()) {
		    final List<SuggestedStartTime> startTimes = violationFixes.getStartTimes();
		    for (SuggestedStartTime startTime : startTimes) {
		    	list.add(startTime.node);
		    }
		    // HACK: A problem here is that the timeline view
		    // is focused on the end-time of the last-selected
		    // activity WHEN THE SELECTION IS FORMED, i.e.,
		    // HERE, before it has been moved.  We mitigate
		    // that by sorting so that the time is early, on
		    // the theory that earlier is generally better.
		    sortByEndTime(list, true);
		}
		return new StructuredSelection(list);
	}
	
	@Override
	protected void dispose(UndoableState state) {
		// nothing to dispose in any case
	}
	
	private void executeSuggestion(SuggestedStartTime suggestion) {
		EPlanElement node = suggestion.node;
		TemporalMember member = node.getMember(TemporalMember.class);
		member.setExtent(new TemporalExtent(suggestion.ideal, member.getDuration()));
	}
	
	@Override
	protected void execute() {
		List<SuggestedStartTime> sugs = new ArrayList<SuggestedStartTime>(violationFixes.getStartTimes());
		sortByIdealTime(sugs, false);
		final List<SuggestedStartTime> startTimes = sugs;
		if (!startTimes.isEmpty()) {
			List<EPlanElement> nodes = new ArrayList<EPlanElement>(startTimes.size());
			for (SuggestedStartTime startTime : startTimes) {
				nodes.add(startTime.node);
			}
			addActivitiesToPlan(adds);
			Boolean animate = false;
			if (animate) {
				for (final SuggestedStartTime suggestion : startTimes) {
					try {
						Thread.sleep(0000);
					} catch (Exception e) {
						//LogUtil.error(e);
					}
					TransactionUtils.writing(nodes, new Runnable() {
						@Override
						public void run() {
							executeSuggestion(suggestion);
						}
					});
				}
				// Would need something for unsatisfiable nodes if SPF-823 (see below) were reversed
			} else {
				TransactionUtils.writing(nodes, new Runnable() {
					@Override
					public void run() {
						for (final SuggestedStartTime suggestion : startTimes) {
							executeSuggestion(suggestion);
						}
						// SPF-823: don't unschedule unsatisfiable nodes, just leave them alone
						//	for (EPlanElement node : violationFixes.getUnsatisfiedNodes()) {
						//		Parameter<Boolean> statusParameter = node.getNamedParameter(AttributeDef.ATTRIBUTE_SCHEDULED);
						//		if (statusParameter != null) {
						//			statusParameter.setObject(Boolean.FALSE);
						//		}
						//	}
					}
				});
			}
		}
	}
	
	@Override
	protected void undo() {
		final List<SuggestedStartTime> startTimes = violationFixes.getStartTimes();
		if (!startTimes.isEmpty()) {
			List<EPlanElement> nodes = new ArrayList<EPlanElement>(startTimes.size());
			for (SuggestedStartTime startTime : startTimes) {
				nodes.add(startTime.node);
			}
			TransactionUtils.writing(nodes, new Runnable () {
				@Override
				public void run() {
					for (int i = 0 ; i < oldExtents.size() ; i++) {
						EPlanElement node = startTimes.get(i).node;
						TemporalMember member = node.getMember(TemporalMember.class);
						TemporalExtent extent = member.getExtent();
						Date currentStartTime = (extent != null ? extent.getStart() : null);
						TemporalExtent oldExtent = oldExtents.get(i);
						Date oldUserStartTime = (oldExtent != null ? oldExtent.getStart() : null);
						if (oldUserStartTime == null) {
							continue;
						}
						if (!DateUtils.same(currentStartTime, oldUserStartTime)) {
							member.setExtent(new TemporalExtent(oldUserStartTime, member.getDuration()));
						}
					}
//					for (int i = 0 ; i < oldScheduledStates.size() ; i++) {
//						EPlanElement node = violationFixes.getUnsatisfiedNodes().get(i);
//						Parameter<Boolean> statusParameter = node.getNamedParameter(AttributeDef.ATTRIBUTE_SCHEDULED);
//						if (statusParameter != null) {
//							statusParameter.setObject(oldScheduledStates.get(i));
//						}
//					}
				}
			});
			removeActivitiesFromPlan(adds);
		}
	}
	
	@Override
	public void displayExecute(Widget widget, IWorkbenchSite site) {
		selectionProvider.setSelection(newSelection);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(FixViolationsOperation.class.getSimpleName());
		builder.append(" place: ");
		for (SuggestedStartTime suggestion : violationFixes.getStartTimes()) {
			EPlanElement node = suggestion.node;
			Date ideal = suggestion.ideal;
			Date earliest = suggestion.earliest;
			Date latest = suggestion.latest;
			builder.append("[" + node.getName() + " @ " + ideal + " or (" + earliest + "," + latest + ") ] ");
		}
//		builder.append(" unschedule: ");
//		for (EPlanElement node : violationFixes.getUnsatisfiedNodes()) {
//			builder.append(node.getUniqueId() + " ");
//		}
		return builder.toString();
	}
	
}
