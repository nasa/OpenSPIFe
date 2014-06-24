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
package gov.nasa.ensemble.core.plan;

import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;

/**
 * The PlanEditApproverRegistry manages a chain of approvers and returns the
 * conjunction of the return values from all approvers.
 */
public class PlanEditApproverRegistry implements IPlanEditApprover {
	
	private static final Logger trace = Logger.getLogger(PlanEditApproverRegistry.class);
	private static final String APPROVER_EXTENSIONS = "gov.nasa.ensemble.core.plan.PlanEditApprover";
	
	private static PlanEditApproverRegistry instance;
	
	private List<IPlanEditApprover> chain;
	
	
	/**
	 * Initialize the chain of plan edit approvers.
	 */
	private void init()
	{
		chain = new ArrayList<IPlanEditApprover>();
		
		// construct the chain of approvers
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry.getExtensionPoint(APPROVER_EXTENSIONS);
		IConfigurationElement[] extensions = extensionPoint.getConfigurationElements();
		for (IConfigurationElement extension : extensions)
		{
			try {
				IPlanEditApprover approver = (IPlanEditApprover) extension.createExecutableExtension("class");
				chain.add(approver);
			} catch (ThreadDeath td) {
				throw td;
			} catch (Throwable t) {
				trace.error("Error during construction of PlanEditApprover chains: " + t, t);
			}
		}
	}

	
	/**
	 * @return the singleton
	 */
	public static IPlanEditApprover getInstance()
	{
		if (instance == null)
		{
			instance = new PlanEditApproverRegistry();
			instance.init();
		}
		return instance;
	}

	/**
	 * Call the canModify method for each plan edit approver in the chain.
	 * 
	 * @param planElement
	 * @return the conjunction of all return values or true if the chain is
	 *         empty
	 */
	@Override
	public boolean canModify(EPlanElement planElement) {
		if (trace.isDebugEnabled()) {
			trace.debug("canModify(" + planElement + ")");
		}
		for (IPlanEditApprover approver : chain) {
			if (!approver.canModify(planElement)) {
				trace.debug("\tFALSE");
				return false;
			}
		}
		trace.debug("\tTRUE");
		return true;
	}
	
	/**
	 * Call the canModifyStructure method for each plan edit approver in the
	 * chain.
	 * 
	 * @param plan
	 * @return the conjunction of all return values or true if the chain is
	 *         empty
	 */
	@Override
	public boolean canModifyStructure(EPlan plan)
	{
		for (IPlanEditApprover approver : chain) {
			if (!approver.canModifyStructure(plan)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean needsUpdate(ResourceSetChangeEvent event) {
		for (IPlanEditApprover approver : chain) {
			if (approver.needsUpdate(event)) {
				return true;
			}
		}
		return false;
	}
	
}
