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
package gov.nasa.ensemble.core.plan.editor;

import gov.nasa.ensemble.common.extension.ClassRegistry;
import gov.nasa.ensemble.core.model.plan.EActivity;
import gov.nasa.ensemble.core.model.plan.EActivityGroup;
import gov.nasa.ensemble.core.model.plan.EPlan;
import gov.nasa.ensemble.core.model.plan.EPlanElement;
import gov.nasa.ensemble.core.model.plan.activityDictionary.util.ADParameterUtils;
import gov.nasa.ensemble.core.model.plan.util.EPlanUtils;
import gov.nasa.ensemble.core.plan.IPlanEditApprover;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.transaction.ResourceSetChangeEvent;

public class PlanRole implements IPlanEditApprover, Externalizable {
	
	private String name;
	private final List<String> categories = new ArrayList<String>();
	private final List<String> planStates = new ArrayList<String>();
	private boolean canModifyPlanStructure;
	
	private static final Logger trace = Logger.getLogger(PlanRole.class);
	private static final IPlanStateProvider stateProvider = ClassRegistry.createInstance(IPlanStateProvider.class);
	
	public PlanRole() { /* do nothing, but needed for java serialization */ }
	
	public PlanRole(String name, List<String> categories, boolean canModifyPlanStructure, List<String> planStates) {
    	this.name = name;
		this.categories.addAll(categories);
		this.canModifyPlanStructure = canModifyPlanStructure;
		this.planStates.addAll(planStates);
    }
	
    public String getName() {
    	return name;
    }
	
	public boolean isDefaultRole() {
		return false;
	}
    
	@Override
	public boolean canModify(EPlanElement e) {
		EPlan p = EPlanUtils.getPlan(e);
		if (p == null) {
			return false;
		}
		// If you have a role with the ability to modify plan structure (ie, the Science Planner), 
		// return true for all approval requests
		if (canModifyStructure(p)) {
			return true;
		}
		
		if (stateProvider != null && planStates.contains(stateProvider.getPlanState(e))) {
			return true;
		}
		
		// check role before any modification or plan action is performed
		if (e instanceof EActivity) {
			EActivity activity = (EActivity) e;
			// obtain the current category for the activity and check to see if
			// it's permissions allow modification
			EActivityDef def = ADParameterUtils.getActivityDef(activity);
			return isApprovedCategory(def.getCategory());
		} else if (e instanceof EActivityGroup || e instanceof EPlan) {
			// don't allow editing of activity groups or the plan
			// if this is an attempt to save the plan, other code will
			// be invoked to only save the elements that need saving
			return false;
		} else {
			trace.error("Can not determine permissions for user for Plan "+p.getName()+".  " +
					    "Defaulting to no edit permissions.  "+
					    "PlanElement is: "+ e.getName()+".  "+
					    "Role is: "+toString()+".");
			return false;
		}
	}

	@Override
	public boolean canModifyStructure(EPlan plan) {
		return canModifyPlanStructure();
	}
    
    @Override
	public boolean needsUpdate(ResourceSetChangeEvent event) {
    	// want to set to true if plan role has changed
		return false;
	}

	private boolean isApprovedCategory(String c) {
		if (categories == null) return true;
		for (String s : categories) {
			if (s.equalsIgnoreCase(c))
				return true;
		}
		return false;
	}
    
    private boolean canModifyPlanStructure() {
    	return canModifyPlanStructure;
    }
	
	@Override
	public String toString() {
		return "PlanRole[" + name + "]";
	}

    @Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    	this.name = (String) in.readObject();
		int numOfCategories = in.readInt();
		for (int i = 0; i < numOfCategories; i++) {
			categories.add((String) in.readObject());
		}
    	this.canModifyPlanStructure = in.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(name);
		out.writeInt(categories.size());
		for (String c : categories) {
			out.writeObject(c);
		}
		out.writeBoolean(canModifyPlanStructure);
	}
    
}
