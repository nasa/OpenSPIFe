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
package gov.nasa.ensemble.core.model.plan.patch;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Test;

public interface ITestPatchBuilderAllMethods {

	/** add **/
	public void add__target_feature_newObject();
	
	public void add__target_feature_newObject_index();
	
	/** addAll **/
	public void addAll__target_feature_newObjects();
	
	public void addAll__target_feature_newObjects_index();
	
	/** remove **/
	public void remove__target_feature_object();
	
	public void remove__target_feature_object_index();
	
	/** removeAll **/
	public void removeAll__target_feature_objects();
	
	/** move **/
	public void move__target_feature_oldIndex_newIndex();
	
	/** modify **/
	public void modify__target_feature_newValue();
	
}
