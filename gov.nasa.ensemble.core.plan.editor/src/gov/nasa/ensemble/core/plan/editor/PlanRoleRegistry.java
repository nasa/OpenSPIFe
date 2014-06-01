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
import gov.nasa.ensemble.common.extension.ExtensionUtils;
import gov.nasa.ensemble.common.logging.LogUtil;
import gov.nasa.ensemble.core.activityDictionary.ActivityDictionary;
import gov.nasa.ensemble.dictionary.EActivityDef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class PlanRoleRegistry {
	
	private static final Logger trace = Logger.getLogger(PlanRoleRegistry.class);
	private static final String EXTENSION_POINT_ID = "gov.nasa.ensemble.core.plan.editor.PlanRole";
	private static final String P_ALLOW_ALL_ROLES = "ensemble.plan.roles.allow.all";
	private static List<PlanRole> list = null;
	private static PlanRole defaultPlanRole = null;
	
	private static ArrayList<PlanRole> buildPlanRoleRegistry() {
		final ArrayList<PlanRole> l = new ArrayList<PlanRole>();
		final IExtensionRegistry registry = Platform.getExtensionRegistry();
        final IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);
        for (final IExtension extension : point.getExtensions()) {
        	for (final IConfigurationElement planRole : extension.getConfigurationElements()) {
        		final List<String> categories = new ArrayList<String>();
        		final List<String> editableStates = new ArrayList<String>();
        		final String name = planRole.getAttribute("name");
				final boolean canModifyStructure = Boolean.parseBoolean(planRole.getAttribute("allowAll"));
				final boolean isDefaultRole      = Boolean.parseBoolean(planRole.getAttribute("defaultRole"));
				if (canModifyStructure) {
        			for (final EActivityDef activityDef : ActivityDictionary.getInstance().getActivityDefs()) {
        				final String category = activityDef.getCategory();
        				if (!categories.contains(category)) {
        					categories.add(category);
        				}
        			}
        		} else {
	        		for (final IConfigurationElement categoryElement : planRole.getChildren("category")) {
	        			categories.add(categoryElement.getAttribute("name"));
	        		}

	        		for (final IConfigurationElement stateElement : planRole.getChildren("planState")) {
	        			editableStates.add(stateElement.getAttribute("name"));
	        		}
        		}

				// MSLICE-1260 limit who can access certain roles
				if (!Boolean.getBoolean(P_ALLOW_ALL_ROLES)) {
					boolean rolePermitted = true;
					for (final IConfigurationElement auth : planRole.getChildren("roleAuthenticator")) {
						try {
							final Class implClass = ExtensionUtils.getClass(auth, auth.getAttribute("class"));
							final PlanRoleAuthenticator pra = (PlanRoleAuthenticator) implClass.newInstance();
							if (!pra.isRolePermitted()) {
								rolePermitted = false;
								break;
							}
						} catch (final InstantiationException e) {
							LogUtil.error(e);
						} catch (final IllegalAccessException e) {
							LogUtil.error(e);
						}
					}
					if (!rolePermitted)
						continue;
				}
				
        		final PlanRole role = new PlanRole(name, categories, canModifyStructure, editableStates);
        		l.add(role);
        		
        		// set the default role
        		if (isDefaultRole || role.isDefaultRole()) {
        			if (defaultPlanRole == null) {
        				defaultPlanRole = role;
        			} else {
        				trace.error("More than one plan role marked as default: " + name + ", " + defaultPlanRole.getName() + ".  Arbitrarily choosing " + defaultPlanRole.getName());
        			}
        		}
            }
        }
        
        // Add the plan roles that were created by external contributors
        //
        final List<PlanRole> roleInstances = ClassRegistry.createInstances(PlanRole.class);
        for (final PlanRole role : roleInstances) {
        	// set the default role
    		if (role.isDefaultRole()) {
    			if (defaultPlanRole == null) {
    				defaultPlanRole = role;
    			} else {
    				trace.error("More than one plan role marked as default: " + role.getName() + ", " + defaultPlanRole.getName() + ".  Arbitrarily choosing " + defaultPlanRole.getName());
    			}
    		}
        }
		l.addAll(roleInstances);
		
		Collections.sort(l, new Comparator<PlanRole>() {
			@Override
			public int compare(final PlanRole o1, final PlanRole o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return l; 
	}
	
    public static PlanRole getPlanRole(final String name) {
    	for (final PlanRole role : values()) {
    		if (role.getName().equals(name)) return role;
    	}
    	return null;
    }
    
    public static PlanRole getDefaultPlanRole() {
    	return defaultPlanRole;
    }
    
	public static List<PlanRole> values() {
		if (list == null) {
			list = Collections.unmodifiableList(buildPlanRoleRegistry());
		}
		return list;
	}
}
