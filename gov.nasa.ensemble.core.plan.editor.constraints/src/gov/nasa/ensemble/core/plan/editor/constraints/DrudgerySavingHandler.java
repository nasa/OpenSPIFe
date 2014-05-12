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
package gov.nasa.ensemble.core.plan.editor.constraints;

import gov.nasa.ensemble.common.CommonUtils;
import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.temporal.TemporalMember;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.constraints.TemporalChainUtils;
import gov.nasa.ensemble.core.plan.editor.actions.AbstractPlanEditorHandler;
import gov.nasa.ensemble.core.plan.temporal.modification.IPlanModifier;
import gov.nasa.ensemble.core.plan.temporal.modification.PlanModifierMember;
import gov.nasa.ensemble.core.plan.temporal.modification.SetExtentsOperation;
import gov.nasa.ensemble.core.plan.temporal.modification.TemporalExtentsCache;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class DrudgerySavingHandler extends AbstractPlanEditorHandler {

	protected String actionName;
	protected String actionVerb;
	
	public DrudgerySavingHandler(String actionName, String actionVerb) {
		this.actionName = actionName;
		this.actionVerb = actionVerb;
	}
	
	protected abstract Map<EPlanElement, Date> getChangedTimes(EList<EPlanElement> elements);
	
	@Override
	public Object execute(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		EList<EPlanElement> elements = getSelectedTemporalElements(selection);
		ECollections.sort(elements, TemporalChainUtils.CHAIN_ORDER);
		EPlan plan = EPlanUtils.getPlan(elements.get(0));
		Map<EPlanElement, Date> startTimes = getChangedTimes(elements);
		// create moves for children			
		IPlanModifier modifier = PlanModifierMember.get(plan).getModifier();
		TemporalExtentsCache cache = new TemporalExtentsCache(plan);
		Map<EPlanElement, TemporalExtent> changedTimes = new LinkedHashMap<EPlanElement, TemporalExtent>();
		for (EPlanElement element: startTimes.keySet()) {
			Date start = startTimes.get(element);
			Map<EPlanElement, TemporalExtent> extents = modifier.moveToStart(element, start, cache);
			changedTimes.putAll(extents);
			if (!extents.containsKey(element)) {
				TemporalMember member = element.getMember(TemporalMember.class);
				TemporalExtent extent = member.getExtent();
				changedTimes.put(element, extent.moveToStart(start));
			}
		}
		IUndoableOperation op = new SetExtentsOperation(actionVerb, plan, changedTimes, cache);
		IUndoContext undoContext = TransactionUtils.getUndoContext(plan);
		CommonUtils.execute(op, undoContext);
		return null;
	}


}
