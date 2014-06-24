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
package gov.nasa.ensemble.emf.resource;

import gov.nasa.ensemble.emf.transaction.TransactionUtils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.resource.ResourceSet;

public class ProjectResourceSetService {

	private static final Map<IProject, ProjectResourceSetSynchronizer> synchronizerByProject = new HashMap<IProject, ProjectResourceSetSynchronizer>();
	
	public static ResourceSet getResourceSet(IProject project) {
		ProjectResourceSetSynchronizer synchronizer = synchronizerByProject.get(project);
		if (synchronizer == null) {
			synchronizer = new ProjectResourceSetSynchronizer(project, TransactionUtils.createTransactionResourceSet());
			synchronizer.activate();
			synchronizerByProject.put(project, synchronizer);
		}
		return synchronizer.getResourceSet();
	}
	
	public static void dispose() {
		for (ProjectResourceSetSynchronizer synchronizer : synchronizerByProject.values()) {
			synchronizer.dispose();
		}
	}
	
}
