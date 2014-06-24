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
package gov.nasa.ensemble.core.plan.constraints.ui.operation;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.common.operation.JobOperationStatus;
import gov.nasa.ensemble.common.ui.IStructureLocation;
import gov.nasa.ensemble.common.ui.IStructureModifier;
import gov.nasa.ensemble.common.ui.InsertionSemantics;
import gov.nasa.ensemble.common.ui.operations.AddOperation;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanChild;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.plan.PlanFactory;
import gov.nasa.ensemble.core.plan.PlanPlugin;
import gov.nasa.ensemble.core.plan.editor.PlanAddOperation;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModel;
import gov.nasa.ensemble.core.plan.editor.PlanEditorModelRegistry;
import gov.nasa.ensemble.core.plan.editor.PlanStructureModifier;
import gov.nasa.ensemble.core.plan.editor.PlanTransferable;
import gov.nasa.ensemble.dictionary.EActivityDef;
import gov.nasa.ensemble.dictionary.util.DictionaryUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class OneOfEachAction extends Action implements IWorkbenchWindowActionDelegate, IHandler {

	protected static final Logger trace = Logger.getLogger(OneOfEachAction.class);

	protected IWorkbenchWindow window;
	private ActionHandler proxy;
	
	@Override
	public void dispose() {
		window = null;
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// do nothing
	}

	@Override
	public void run(IAction arg0) {
		run();
	}
	
	@Override
	public void run() {
		if (window == null) {
			window = PlanPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		}
		Job job = new Job("One of each") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				if (window == null) {
					return Status.CANCEL_STATUS;
				} else {
					createPlan(monitor);
				}
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}
	
	private void createPlan(IProgressMonitor monitor) {
		PlanEditorModel model = PlanEditorModelRegistry.getCurrent(window);
		if (model == null) {
			return;
		}
		EPlan plan = model.getEPlan();
		int activity_count = 0;
		EActivityGroup group = null;
		PlanFactory factory = PlanFactory.getInstance();
		List<EPlanChild> links = new ArrayList<EPlanChild>();
		Set<EActivityDef> activityDefs = activityDefs();
		monitor.beginTask("one of each", activityDefs.size() + 2);
		for (EActivityDef def: activityDefs) {
			monitor.worked(1);
			if (DictionaryUtil.isHidden(def)) {
				continue;
			}
			if (group == null || activity_count > 9) {
				group = factory.createActivityGroup(plan);
				createAddOperation(model, plan, group);
				activity_count = 0;
				// create chain
				if (links.size() > 1) {
					createChainOperation(model, new ArrayList<EPlanChild>(links));
					links.clear();
				}
			}
			EActivity activity = factory.createActivity(def, group);
			createAddOperation(model, group, activity);
			links.add(activity);
			activity_count++;
		}
		monitor.worked(1);
		// create chain
		if (links.size() > 1) {
			createChainOperation(model, new ArrayList<EPlanChild>(links));
			links.clear();
		}
		monitor.done();
	}

	private static Set<EActivityDef> activityDefs() {
		Comparator<EActivityDef> c = new Comparator<EActivityDef>() {
			@Override
			public int compare(EActivityDef o1, EActivityDef o2) {
				String s1 = o1.getCategory();
				String s2 = o2.getCategory();
				if (CommonUtils.equals(s1,  s2)) {
					s1 = o1.getName();
					s2 = o2.getName();
					return s1.compareTo(s2);
				} else if (s1 == null) {
					return -1;
				} else if (s2 == null) {
					return 1;
				} else {
					return s1.compareTo(s2);
				}
			}
		};
		SortedSet<EActivityDef> defs = new TreeSet<EActivityDef>(c);
		defs.addAll(ActivityDictionary.getInstance().getActivityDefs());
		return defs;
	}

	private void createAddOperation(PlanEditorModel model, final EPlanElement parent, final EPlanElement child) {
		IStructureModifier modifier = PlanStructureModifier.INSTANCE;
		PlanTransferable transferable = new PlanTransferable();
		transferable.setPlanElements(Collections.singletonList(child));
		IStructureLocation location = modifier.getInsertionLocation(transferable, new StructuredSelection(parent), InsertionSemantics.ON);
		AddOperation operation = new PlanAddOperation(transferable, modifier, location);
		IUndoContext undoContext = model.getUndoContext();
		operation.addContext(undoContext);
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			IStatus status = history.execute(operation, null, null);
			if (status instanceof JobOperationStatus) {
				((JobOperationStatus) status).getJob().join();
			}
		} catch (Exception e) {
			trace.error("OneOfEachAction.createAddGroupOperation:operation", e);
		}
	}
	
	private void createChainOperation(PlanEditorModel model, final List<EPlanChild> links) {
		IUndoContext undoContext = model.getUndoContext();
		PlanStructureModifier modifier = PlanStructureModifier.INSTANCE;
		ChainOperation operation = new ChainOperation(modifier, links, false);
		operation.addContext(undoContext);
		try {
			IOperationHistory history = OperationHistoryFactory.getOperationHistory();
			IStatus status = history.execute(operation, null, null);
			if (status instanceof JobOperationStatus) {
				((JobOperationStatus) status).getJob().join();
			}
		} catch (Exception e) {
			trace.error("OneOfEachAction.createChainOperation:operation", e);
		}
	}
	
	private ActionHandler getProxy() {
		if (proxy == null) {
			proxy = new ActionHandler(this);
		}
		return proxy;
	}
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		getProxy().addHandlerListener(handlerListener);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		getProxy().execute(event);
		return null;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		getProxy().removeHandlerListener(handlerListener);
	}	
	    
}
