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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

public class PlanStateRegistry {
	private static String EXTENSION_POINT_ID = "gov.nasa.ensemble.core.plan.PlanState";
	private static List<PlanState> list = null;
	
	public static class PlanState {
		private String name;
		private int sortKey; // sortKey used for sorting the set

		private PlanState(String name, int sortKey) {
	    	this.name = name;
	    	this.sortKey = sortKey;
	    }
	    @Override
		public String toString() { return name; }
	    public String getName() { return name; }
	}
	
	private static List<PlanState> buildPlanStateRegistry() {
		List<PlanState> l = new ArrayList<PlanState>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT_ID);
        for (IExtension extension : point.getExtensions()) {
        	for (IConfigurationElement planState : extension.getConfigurationElements()) {
        		String name = planState.getAttribute("name");
        		int sort = Integer.valueOf(planState.getAttribute("sortkey"));
        		PlanState type = new PlanState(name, sort);
        		l.add(type);
            }
        }
        Collections.sort(l, new Comparator<PlanState>() {
			@Override
			public int compare(PlanState o1, PlanState o2) {
				return o1.sortKey - o2.sortKey;
			}
		});
		return Collections.unmodifiableList(l);
	}
	
    public static PlanState getPlanState(String name) {
    	for (PlanState state : values()) {
    		if (state.getName().equals(name)) return state;
    	}
    	return null;
    }
    
	public static PlanState[] values() {
		if (list == null) {
			synchronized (PlanGroupRegistry.class) {
				if (list == null) {
					list = buildPlanStateRegistry();
				}
			}
		}
		return list.toArray(new PlanState[list.size()]);
	}
}
