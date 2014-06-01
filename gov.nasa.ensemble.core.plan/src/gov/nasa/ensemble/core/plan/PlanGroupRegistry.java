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

import gov.nasa.ensemble.common.extension.ClassRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class PlanGroupRegistry {
	
	private static String EXTENSION_POINT_ID = "gov.nasa.ensemble.core.plan.PlanGroup";
	private static List<PlanGroup> list = null;
	
	private static List<PlanGroup> buildPlanGroupRegistry() {
		List<PlanGroup> l = new ArrayList<PlanGroup>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);
        for (IExtension extension : point.getExtensions()) {
        	for (IConfigurationElement planGroup : extension.getConfigurationElements()) {
        		String name = planGroup.getAttribute("name");
        		String expandedName = planGroup.getAttribute("expandedName");
        		int sort = Integer.valueOf(planGroup.getAttribute("sortkey"));
        		PlanGroup type = new PlanGroup(name, expandedName, sort);
        		l.add(type);
            }
        }
        
        // Allow contributions
        //
        l.addAll(ClassRegistry.createInstances(PlanGroup.class));
        
        Collections.sort(l, new Comparator<PlanGroup>() {
			@Override
			public int compare(PlanGroup o1, PlanGroup o2) {
				return o1.sortKey - o2.sortKey;
			}
		});
		return Collections.unmodifiableList(l);
	}
	
    public static PlanGroup getPlanGroup(String name) {
    	for (PlanGroup group : values()) {
    		if (group.getName().equals(name)) return group;
    	}
    	return null;
    }
    
	public static PlanGroup[] values() {
		if (list == null) {
			synchronized (PlanGroupRegistry.class) {
				if (list == null) {
					list = buildPlanGroupRegistry();
				}
			}
		}
		return list.toArray(new PlanGroup[list.size()]);
	}
	
	public static class PlanGroup {
		
		private final String name;
		private final String expandedName;
		private final int sortKey; // sortKey used for sorting the set
		
		public PlanGroup(String name, String expandedName, int sortKey) {
	    	this.name = name;
	    	if (expandedName == null || expandedName.trim().isEmpty()) {
	    		this.expandedName = name;
	    	} else {
	    		this.expandedName = expandedName;
	    	}
	    	this.sortKey = sortKey;
	    }
	    
	    public String getName() { return name; }
		public String getExpandedName() { return expandedName; }
		@Override public String toString() { return name; }
	    
	}
	
}
