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
package gov.nasa.arc.spife.rcp.importer;

import gov.nasa.ensemble.emf.transaction.TransactionUtils;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.PlanUtils;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;

import java.util.LinkedList;
import java.util.List;

/** Add orbital activities imported as a profile,
 * and more significantly, update the start times of the ones that already exist,
 * adjusting the tied activity groups appropriately.
 */
public class ImportCsvActivitiesOperation extends AbstractTransactionUndoableOperation {
	
	private final EPlan plan;
	protected List<EActivity> addedActivities = new LinkedList<EActivity>();
	
	public ImportCsvActivitiesOperation(EPlan plan, List<EActivity> activities) {
		super("Activity Import");
		this.plan = plan;
		addedActivities = activities;
	}

	@Override
	protected void dispose(UndoableState state) {
		// Nothing to dispose.
	}

	@Override
	protected void execute() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				plan.getChildren().addAll(addedActivities);
			}
		});
	}
	
	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				PlanUtils.removeElements(addedActivities);
			}
		});
	}

	@Override
	public String toString() {
		return "Activity Import";
	}

}
