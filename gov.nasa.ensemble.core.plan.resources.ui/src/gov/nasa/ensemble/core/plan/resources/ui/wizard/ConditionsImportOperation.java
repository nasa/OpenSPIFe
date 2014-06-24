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
package gov.nasa.ensemble.core.plan.resources.ui.wizard;

import gov.nasa.ensemble.common.operation.AbstractEnsembleUndoableOperation;
import gov.nasa.ensemble.core.jscience.util.DateUtils;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.plan.resources.member.Conditions;
import gov.nasa.ensemble.core.plan.resources.member.ResourceConditionsMember;
import gov.nasa.ensemble.core.plan.resources.util.ResourceConditionsUtils;
import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.measure.unit.SI;

import org.jscience.physics.amount.Amount;

public class ConditionsImportOperation extends AbstractEnsembleUndoableOperation {
	
	private final EPlan plan;
	private final File file;
	private Conditions conditionToAdd;
	private Collection<Conditions> conditionsToRemove = new ArrayList<Conditions>();
	
	public ConditionsImportOperation(EPlan plan, File file) {
		super("Import conditions from "+file.getName());
		this.file = file;
		this.plan = plan;
	}

	@Override
	protected void dispose(UndoableState state) {
		// nothing to dispose
	}

	@Override
	protected void execute() throws Throwable {
		initialize();
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				ResourceConditionsMember member = plan.getMember(ResourceConditionsMember.class);
				member.getConditions().removeAll(conditionsToRemove);
				member.getConditions().add(conditionToAdd);
			}
		});
	}

	private void initialize() throws IOException {
		if (conditionToAdd == null) {
			conditionToAdd = ResourceConditionsUtils.readConditions(file);
			conditionToAdd.setActive(true);
			TransactionUtils.reading(plan, new Runnable() {
				@Override
				public void run() {
					ResourceConditionsMember member = plan.getMember(ResourceConditionsMember.class);
					Date time = conditionToAdd.getTime();
					Iterator<Conditions> iterator = member.getConditions().iterator();
					while (iterator.hasNext()) {
						Conditions oldConditions = iterator.next();
						Date oldTime = oldConditions.getTime();
						if (DateUtils.subtract(oldTime, time).abs().isLessThan(Amount.valueOf(500, SI.MILLI(SI.SECOND)))) {
							conditionsToRemove.add(oldConditions);
						}
					}
				}
			});
		}
	}

	@Override
	protected void undo() throws Throwable {
		TransactionUtils.writing(plan, new Runnable() {
			@Override
			public void run() {
				ResourceConditionsMember member = plan.getMember(ResourceConditionsMember.class);
				member.getConditions().remove(conditionToAdd);
				for (Conditions oldConditions : conditionsToRemove) {
					member.getConditions().add(oldConditions);
				}
			}
		});
	}

	@Override
	public String toString() {
		return getLabel();
	}

}
