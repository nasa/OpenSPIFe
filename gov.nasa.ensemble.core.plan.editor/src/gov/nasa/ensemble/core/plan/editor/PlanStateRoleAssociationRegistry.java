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

import gov.nasa.ensemble.core.plan.PlanStateRegistry;
import gov.nasa.ensemble.core.plan.PlanStateRegistry.PlanState;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class PlanStateRoleAssociationRegistry {
	private static String EXTENSION_POINT_ID = "gov.nasa.ensemble.core.plan.editor.PlanStateRoleAssociation";
	
	private static PlanStateRoleAssociationRegistry registry = null;
	public static PlanStateRoleAssociationRegistry getInstance() {
		if (registry == null) {
			synchronized (PlanStateRoleAssociationRegistry.class) {
				if (registry == null) {
					registry = new PlanStateRoleAssociationRegistry();
				}
			}
		}
		return registry;
	}

	private Map<PlanState, Set<PlanRole>> stateToRoleMap = new HashMap<PlanState, Set<PlanRole>>();
	private Map<PlanRole, Set<PlanState>> roleToStateMap = new HashMap<PlanRole, Set<PlanState>>();
	
	private PlanStateRoleAssociationRegistry() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);
        for (IExtension extension : point.getExtensions()) {
        	for (IConfigurationElement planState : extension.getConfigurationElements()) {
        		String stateName = planState.getAttribute("state");
        		String roleName = planState.getAttribute("role");
        		PlanState state = PlanStateRegistry.getPlanState(stateName);
        		PlanRole role = PlanRoleRegistry.getPlanRole(roleName);
        		Set<PlanRole> roles = stateToRoleMap.get(state);
        		if (roles == null) {
        			roles = new HashSet<PlanRole>();
        		}
        		roles.add(role);
        		stateToRoleMap.put(state, roles);
        		Set<PlanState> states = roleToStateMap.get(role);
        		if (states == null) {
        			states = new HashSet<PlanState>();
        		}
        		states.add(state);
        		roleToStateMap.put(role, states);
            }
        }
	}

	public Set<PlanState> getStatesForRole(PlanRole role) {
		Set<PlanState> states = roleToStateMap.get(role);
		if (states == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(states);
	}
	
	public Set<PlanRole> getRolesForState(PlanState state) {
		Set<PlanRole> roles = stateToRoleMap.get(state);
		if (roles == null) {
			return Collections.emptySet();
		}
		return Collections.unmodifiableSet(roles);
	}
	
}
