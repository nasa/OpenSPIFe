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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.common.operation.CompositeOperation;

import java.util.Collection;

import org.eclipse.core.commands.operations.IUndoableOperation;

/**
 * An abstract class that may have subclasses defined that contribute additional operations to a CompositeOperation created by the addContributorOperations
 * static method, which may be called when creating a plan structure modifying operation.  This contributor mechanism is useful for updating bookkeeping features
 * of objects related to the structure modification. When the primary change is undone, the changes to these bookkeeping attributes will be undone as well.
 * 
 * @author rnado
 *
 */
public abstract class PlanStructureOperationContributor {
	
	public abstract void contributeOperations(CompositeOperation toDoList, IUndoableOperation primaryOp, IPlanElementTransferable transferable, PlanStructureLocation location);
	
	private static final Collection<PlanStructureOperationContributor> CONTRIBUTORS = ClassRegistry.createInstances(PlanStructureOperationContributor.class);
	
	public static IUndoableOperation addContributorOperations(IUndoableOperation operation, IPlanElementTransferable transferable, PlanStructureLocation location) {
		if (!CONTRIBUTORS.isEmpty()) {
			CompositeOperation toDoList = new CompositeOperation(operation.getLabel());
			toDoList.add(operation);
			for (PlanStructureOperationContributor contributor : CONTRIBUTORS) {
				contributor.contributeOperations(toDoList, operation, transferable, location);
			}
			return toDoList;
		}
		return operation;
	}
}
