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
package gov.nasa.ensemble.core.plan.temporal.modification;

import gov.nasa.ensemble.core.jscience.TemporalExtent;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.emf.transaction.AbstractTransactionUndoableOperation;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.Map;

public class SetExtentsOperation extends AbstractTransactionUndoableOperation {
	
	private final TemporalExtentsCache initialExtents;
	private final Map<EPlanElement, TemporalExtent> changedTimes;
	private final EPlan context;
	
	public SetExtentsOperation(String name, EPlan plan, Map<EPlanElement, TemporalExtent> changedTimes, TemporalExtentsCache initialExtents) {
		super(name);
		this.initialExtents = initialExtents;
		this.changedTimes = changedTimes;
		this.context = plan;
	}
	
	@Override
	protected void dispose(UndoableState state) {
		// nothing to do in any case
	}

	@Override
	protected void execute() {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				TemporalUtils.setExtents(changedTimes);
			}
		});
	}

	@Override
	protected void undo() {
		TransactionUtils.writing(context, new Runnable() {
			@Override
			public void run() {
				TemporalUtils.resetExtents(changedTimes.keySet(), initialExtents);
			}
		});
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(getClass().getSimpleName());
		builder.append(":");
		builder.append(String.valueOf(changedTimes.keySet()));
		builder.append(" from ");
		builder.append(String.valueOf(initialExtents));
		builder.append(" to ");
		builder.append(String.valueOf(changedTimes.values()));
		return builder.toString();
	}

}
