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

import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.common.mission.MissionExtendable;
import gov.nasa.ensemble.common.mission.MissionExtender;
import gov.nasa.ensemble.common.mission.MissionExtender.ConstructionException;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.model.plan.util.PlanResourceImpl;
import gov.nasa.ensemble.core.plan.IPlanEditApprover;
import gov.nasa.ensemble.core.plan.PlanEditApproverRegistry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.operations.IOperationApprover;
import org.eclipse.core.commands.operations.IOperationApprover2;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * This class is a backup check for readonlyness.
 * It will block edits to the model at the API level.
 * When checkReadOnly fails for an operation, 
 * this indicates that the UI should have disabled
 * that action, and not allowed the user to perform it.
 * 
 * @author abachman
 *
 */
public class PlanReadOnlyOperationApprover implements IOperationApprover2, MissionExtendable {

	private static PlanReadOnlyOperationApprover instance;
	private static IPlanEditApprover registry = PlanEditApproverRegistry.getInstance();

	public static IOperationApprover getInstance() {
		if (instance == null) {
			synchronized (PlanReadOnlyOperationApprover.class) {
				if (instance == null) {
					try {
						instance = MissionExtender.construct(PlanReadOnlyOperationApprover.class);
					} catch (ConstructionException e) {
						instance = new PlanReadOnlyOperationApprover();
					}
				}
			}
		}
		return instance;
	}

	protected PlanReadOnlyOperationApprover() {
		// construct from getInstance() only
	}

	@Override
	public IStatus proceedExecuting(IUndoableOperation operation, IOperationHistory history, IAdaptable info) {
		return checkReadOnly(operation); 
	}

	@Override
	public IStatus proceedRedoing(IUndoableOperation operation, IOperationHistory history, IAdaptable info) {
		return checkReadOnly(operation); 
	}

	@Override
	public IStatus proceedUndoing(IUndoableOperation operation, IOperationHistory history, IAdaptable info) {
		return checkReadOnly(operation); 
	}

	protected IStatus checkReadOnly(IUndoableOperation operation) {
		for (IUndoContext context : operation.getContexts()) {
			if (context instanceof ObjectUndoContext) {
				ObjectUndoContext objectUndoContext = (ObjectUndoContext) context;
				if (objectUndoContext.getObject() instanceof EditingDomain) {
					EditingDomain domain = (EditingDomain) objectUndoContext.getObject();
					ResourceSet resourceSet = domain.getResourceSet();
					List<Resource> resources = new ArrayList<Resource>(resourceSet.getResources());
					for (Resource resource : resources) {
						if (resource instanceof PlanResourceImpl) {
							for (EObject content : resource.getContents()) {
								if (content instanceof EPlan && !EPlanUtils.isTemplatePlan(content)) {
									EPlan plan = (EPlan) content;
									if (domain.isReadOnly(resource)
										|| (!registry.canModify(plan) 
												&& !registry.canModifyStructure(plan))) {
										Throwable exception = new IllegalStateException("checkReadOnly failed: " + operation);
										LogUtil.error(exception);
										return new Status(IStatus.ERROR, "gov.nasa.ensemble.core.plan.editor", 7, "plan is read only", exception); 
									}
								}
							}
						}
					}
				}
			}
		}
		return Status.OK_STATUS;
	}

}
