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
package gov.nasa.ensemble.common.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A simple implementation of a directed graph, capable of detecting cycles.
 * Originally written to ensure that TemporalAssignments in AD's are not cyclical
 * (see SPF-6188), but made generally available since this is such a general concept.
 */
public class DirectedGraph<E> {
	
	private Set<E> nodes = new TreeSet<E>();
	private Map<E,Set<E>> directDependencies = new HashMap<E,Set<E>>();
	
	
	/** Declares that one node is dependent on another.
	 * This can be the first or subsequent mention of either of the nodes.
	 */
	public void addDependency (E dependent, E dependsOn) {
		nodes.add(dependent);
		nodes.add(dependsOn);
		Set<E> existingDependencies = directDependencies.get(dependent);
		if (existingDependencies==null) {
			directDependencies.put(dependent, existingDependencies = new TreeSet<E>());
		}
		existingDependencies.add(dependsOn);
	}
	
	public boolean hasCyclicDependencies (E dependent) {
		return dependsOn(dependent, dependent);
	}
	
	public boolean hasCyclicDependencies () {
		for (E node : nodes) {
			if (hasCyclicDependencies(node)) return true;
		}
		return false;
	}

	public boolean dependsOn(E dependent, E dependsOn) {
		return getTransitiveDependencies(dependent).contains(dependsOn);
	}
	
	public Set<E> getTransitiveDependencies (E dependent) {
		return getTransitiveDependencies(dependent, new TreeSet<E>());
	}
		
		
	private Set<E> getTransitiveDependencies(E dependent, TreeSet<E> resultSoFar) {
		Set<E> dependencies = directDependencies.get(dependent);
		if (dependencies != null) {
			for (E dependency : dependencies) {
				if (!resultSoFar.contains(dependency)) {
					resultSoFar.add(dependency);
					getTransitiveDependencies(dependency, resultSoFar);
				}
			}
		}
		return resultSoFar;
	}

}
